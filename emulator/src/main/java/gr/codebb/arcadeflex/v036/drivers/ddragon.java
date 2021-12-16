/*
 Double Dragon, Double Dragon (bootleg)  Double Dragon II

 By Carlos A. Lozano  Rob Rosenbrock
 Help to do the original drivers from Chris Moore
 Sprite CPU support and additional code by Phil Stroffolino
 Sprite CPU emulation, vblank support, and partial sound code by Ernesto Corvi.
 Dipswitch to dd2 by Marco Cassili.
 High Score support by Roberto Fresca.

 TODO:
 - Find the original MCU code so original Double Dragon ROMs can be supported

 NOTES:
 The OKI M5205 chip null sampling rate is 8000hz (8khz).
 The OKI M5205 chip 1 sampling rate is 4000hz (4khz).
 Until the ADPCM interface is updated to be able to use
 multiple sampling rates, all samples currently play at 8khz.
 */

/*
 * ported to v0.36
 * using automatic conversion tool v0.08
 */
package gr.codebb.arcadeflex.v036.drivers;

//mame imports
import static arcadeflex.v037b7.mame.driverH.*;

//to be organized
import static arcadeflex.v037b7.sound._2151intf.YM2151_data_port_0_w;
import static arcadeflex.v037b7.sound._2151intf.YM2151_register_port_0_w;
import static arcadeflex.v037b7.sound._2151intf.YM2151_status_port_0_r;
import arcadeflex.v037b7.sound._2151intfH.YM2151interface;
import static arcadeflex.v037b7.sound._2151intfH.YM3012_VOL;
import static arcadeflex.v037b7.generic.fucPtr.*;
import static gr.codebb.arcadeflex.v037b7.mame.memoryH.*;
import static gr.codebb.arcadeflex.common.PtrLib.*;
import static gr.codebb.arcadeflex.v036.mame.commonH.*;
import static arcadeflex.v037b7.mame.inptport.*;
import static arcadeflex.v037b7.mame.drawgfxH.*;
import static gr.codebb.arcadeflex.v036.vidhrdw.generic.*;
import static gr.codebb.arcadeflex.v037b7.mame.cpuintrf.*;
import static gr.codebb.arcadeflex.v037b7.mame.cpuintrfH.*;
import static arcadeflex.v037b7.mame.inptportH.*;
import static arcadeflex.v037b7.mame.sndintrf.*;
import static arcadeflex.v037b7.mame.sndintrfH.*;
import static gr.codebb.arcadeflex.v036.cpu.m6809.m6809H.*;
import static gr.codebb.arcadeflex.v036.vidhrdw.ddragon.*;
import static gr.codebb.arcadeflex.v037b7.cpu.z80.z80H.*;
import static gr.codebb.arcadeflex.v036.mame.common.*;
import static gr.codebb.arcadeflex.v037b7.mame.palette.*;
import static gr.codebb.arcadeflex.v036.sound.adpcmH.*;
import static gr.codebb.arcadeflex.v036.sound.adpcm.*;
import static gr.codebb.arcadeflex.v037b7.sound.okim6295H.*;
import static gr.codebb.arcadeflex.v037b7.sound.okim6295.*;
import static gr.codebb.arcadeflex.v036.sound.mixerH.*;

public class ddragon {

    /* private globals */
    static int dd_sub_cpu_busy;
    static int sprite_irq, sound_irq, ym_irq;
    /* end of private globals */

    public static InitMachinePtr dd_init_machine = new InitMachinePtr() {
        public void handler() {
            sprite_irq = M6809_INT_NMI;
            sound_irq = M6809_INT_IRQ;
            ym_irq = 1;//M6809_INT_FIRQ;
            dd2_video = 0;
            dd_sub_cpu_busy = 0x10;
        }
    };
    public static InitMachinePtr dd2_init_machine = new InitMachinePtr() {
        public void handler() {
            sprite_irq = Z80_NMI_INT;
            sound_irq = Z80_NMI_INT;
            ym_irq = 0;//-1000;
            dd2_video = 1;
            dd_sub_cpu_busy = 0x10;
        }
    };
    public static WriteHandlerPtr dd_bankswitch_w = new WriteHandlerPtr() {
        public void handler(int offset, int data) {
            UBytePtr RAM = memory_region(REGION_CPU1);

            dd_scrolly_hi = ((data & 0x02) << 7);
            dd_scrollx_hi = ((data & 0x01) << 8);

            if ((data & 0x10) == 0x10) {
                dd_sub_cpu_busy = 0x00;
            } else if (dd_sub_cpu_busy == 0x00) {
                cpu_cause_interrupt(1, sprite_irq);
            }

            cpu_setbank(1, new UBytePtr(RAM, 0x10000 + (0x4000 * ((data >> 5) & 7))));
        }
    };
    public static WriteHandlerPtr dd_forcedIRQ_w = new WriteHandlerPtr() {
        public void handler(int offset, int data) {
            cpu_cause_interrupt(0, M6809_INT_IRQ);
        }
    };
    public static ReadHandlerPtr port4_r = new ReadHandlerPtr() {
        public int handler(int offset) {
            int port = readinputport(4);

            return port | dd_sub_cpu_busy;
        }
    };
    public static ReadHandlerPtr dd_spriteram_r = new ReadHandlerPtr() {
        public int handler(int offset) {
            return dd_spriteram.read(offset);
        }
    };
    public static WriteHandlerPtr dd_spriteram_w = new WriteHandlerPtr() {
        public void handler(int offset, int data) {

            if (cpu_getactivecpu() == 1 && offset == 0) {
                dd_sub_cpu_busy = 0x10;
            }

            dd_spriteram.write(offset, data);
        }
    };
    public static WriteHandlerPtr cpu_sound_command_w = new WriteHandlerPtr() {
        public void handler(int offset, int data) {
            soundlatch_w.handler(offset, data);
            cpu_cause_interrupt(2, sound_irq);
        }
    };
    static int[] start_snd = new int[2];
    static int[] end_snd = new int[2];
    public static WriteHandlerPtr dd_adpcm_w = new WriteHandlerPtr() {
        public void handler(int offset, int data) {

            int chip = offset & 1;

            offset >>= 1;

            switch (offset) {
                case 3:
                    break;

                case 2:
                    start_snd[chip] = data & 0x7f;
                    break;

                case 1:
                    end_snd[chip] = data & 0x7f;
                    break;

                case 0:
                    ADPCM_play(chip, 0x10000 * chip + start_snd[chip] * 0x200, (end_snd[chip] - start_snd[chip]) * 0x400);
                    break;
            }
        }
    };
    public static ReadHandlerPtr dd_adpcm_status_r = new ReadHandlerPtr() {
        public int handler(int offset) {
            return (ADPCM_playing(0) + (ADPCM_playing(1) << 1));
        }
    };

    static MemoryReadAddress readmem[]
            = {
                new MemoryReadAddress(0x0000, 0x1fff, MRA_RAM),
                new MemoryReadAddress(0x2000, 0x2fff, dd_spriteram_r),
                new MemoryReadAddress(0x3000, 0x37ff, MRA_RAM),
                new MemoryReadAddress(0x3800, 0x3800, input_port_0_r),
                new MemoryReadAddress(0x3801, 0x3801, input_port_1_r),
                new MemoryReadAddress(0x3802, 0x3802, port4_r),
                new MemoryReadAddress(0x3803, 0x3803, input_port_2_r),
                new MemoryReadAddress(0x3804, 0x3804, input_port_3_r),
                new MemoryReadAddress(0x3805, 0x3fff, MRA_RAM),
                new MemoryReadAddress(0x4000, 0x7fff, MRA_BANK1),
                new MemoryReadAddress(0x8000, 0xffff, MRA_ROM),
                new MemoryReadAddress(-1) /* end of table */};

    static MemoryWriteAddress writemem[]
            = {
                new MemoryWriteAddress(0x0000, 0x0fff, MWA_RAM),
                new MemoryWriteAddress(0x1000, 0x11ff, paletteram_xxxxBBBBGGGGRRRR_split1_w, paletteram),
                new MemoryWriteAddress(0x1200, 0x13ff, paletteram_xxxxBBBBGGGGRRRR_split2_w, paletteram_2),
                new MemoryWriteAddress(0x1400, 0x17ff, MWA_RAM),
                new MemoryWriteAddress(0x1800, 0x1fff, MWA_RAM, videoram),
                new MemoryWriteAddress(0x2000, 0x2fff, dd_spriteram_w, dd_spriteram),
                new MemoryWriteAddress(0x3000, 0x37ff, dd_background_w, dd_videoram),
                new MemoryWriteAddress(0x3800, 0x3807, MWA_RAM),
                new MemoryWriteAddress(0x3808, 0x3808, dd_bankswitch_w),
                new MemoryWriteAddress(0x3809, 0x3809, MWA_RAM, dd_scrollx_lo),
                new MemoryWriteAddress(0x380a, 0x380a, MWA_RAM, dd_scrolly_lo),
                new MemoryWriteAddress(0x380b, 0x380b, MWA_RAM),
                new MemoryWriteAddress(0x380c, 0x380d, MWA_RAM),
                new MemoryWriteAddress(0x380e, 0x380e, cpu_sound_command_w),
                new MemoryWriteAddress(0x380f, 0x380f, dd_forcedIRQ_w),
                new MemoryWriteAddress(0x3810, 0x3fff, MWA_RAM),
                new MemoryWriteAddress(0x4000, 0xffff, MWA_ROM),
                new MemoryWriteAddress(-1) /* end of table */};

    static MemoryWriteAddress dd2_writemem[]
            = {
                new MemoryWriteAddress(0x0000, 0x17ff, MWA_RAM),
                new MemoryWriteAddress(0x1800, 0x1fff, MWA_RAM, videoram),
                new MemoryWriteAddress(0x2000, 0x2fff, dd_spriteram_w, dd_spriteram),
                new MemoryWriteAddress(0x3000, 0x37ff, dd_background_w, dd_videoram),
                new MemoryWriteAddress(0x3800, 0x3807, MWA_RAM),
                new MemoryWriteAddress(0x3808, 0x3808, dd_bankswitch_w),
                new MemoryWriteAddress(0x3809, 0x3809, MWA_RAM, dd_scrollx_lo),
                new MemoryWriteAddress(0x380a, 0x380a, MWA_RAM, dd_scrolly_lo),
                new MemoryWriteAddress(0x380b, 0x380b, MWA_RAM),
                new MemoryWriteAddress(0x380c, 0x380d, MWA_RAM),
                new MemoryWriteAddress(0x380e, 0x380e, cpu_sound_command_w),
                new MemoryWriteAddress(0x380f, 0x380f, dd_forcedIRQ_w),
                new MemoryWriteAddress(0x3810, 0x3bff, MWA_RAM),
                new MemoryWriteAddress(0x3c00, 0x3dff, paletteram_xxxxBBBBGGGGRRRR_split1_w, paletteram),
                new MemoryWriteAddress(0x3e00, 0x3fff, paletteram_xxxxBBBBGGGGRRRR_split2_w, paletteram_2),
                new MemoryWriteAddress(0x4000, 0xffff, MWA_ROM),
                new MemoryWriteAddress(-1) /* end of table */};

    static MemoryReadAddress sub_readmem[]
            = {
                new MemoryReadAddress(0x0000, 0x0fff, MRA_RAM),
                new MemoryReadAddress(0x8000, 0x8fff, dd_spriteram_r),
                new MemoryReadAddress(0xc000, 0xffff, MRA_ROM),
                new MemoryReadAddress(-1) /* end of table */};

    static MemoryWriteAddress sub_writemem[]
            = {
                new MemoryWriteAddress(0x0000, 0x0fff, MWA_RAM),
                new MemoryWriteAddress(0x8000, 0x8fff, dd_spriteram_w),
                new MemoryWriteAddress(0xc000, 0xffff, MWA_ROM),
                new MemoryWriteAddress(-1) /* end of table */};

    static MemoryReadAddress sound_readmem[]
            = {
                new MemoryReadAddress(0x0000, 0x0fff, MRA_RAM),
                new MemoryReadAddress(0x1000, 0x1000, soundlatch_r),
                new MemoryReadAddress(0x1800, 0x1800, dd_adpcm_status_r),
                new MemoryReadAddress(0x2800, 0x2801, YM2151_status_port_0_r),
                new MemoryReadAddress(0x8000, 0xffff, MRA_ROM),
                new MemoryReadAddress(-1) /* end of table */};

    static MemoryWriteAddress sound_writemem[]
            = {
                new MemoryWriteAddress(0x0000, 0x0fff, MWA_RAM),
                new MemoryWriteAddress(0x2800, 0x2800, YM2151_register_port_0_w),
                new MemoryWriteAddress(0x2801, 0x2801, YM2151_data_port_0_w),
                new MemoryWriteAddress(0x3800, 0x3807, dd_adpcm_w),
                new MemoryWriteAddress(0x8000, 0xffff, MWA_ROM),
                new MemoryWriteAddress(-1) /* end of table */};

    static MemoryReadAddress dd2_sub_readmem[]
            = {
                new MemoryReadAddress(0x0000, 0xbfff, MRA_ROM),
                new MemoryReadAddress(0xc000, 0xcfff, dd_spriteram_r),
                new MemoryReadAddress(0xd000, 0xffff, MRA_RAM),
                new MemoryReadAddress(-1) /* end of table */};

    static MemoryWriteAddress dd2_sub_writemem[]
            = {
                new MemoryWriteAddress(0x0000, 0xbfff, MWA_ROM),
                new MemoryWriteAddress(0xc000, 0xcfff, dd_spriteram_w),
                new MemoryWriteAddress(0xd000, 0xffff, MWA_RAM),
                new MemoryWriteAddress(-1) /* end of table */};

    static MemoryReadAddress dd2_sound_readmem[]
            = {
                new MemoryReadAddress(0x0000, 0x7fff, MRA_ROM),
                new MemoryReadAddress(0x8000, 0x87ff, MRA_RAM),
                new MemoryReadAddress(0x8801, 0x8801, YM2151_status_port_0_r),
                new MemoryReadAddress(0x9800, 0x9800, OKIM6295_status_0_r),
                new MemoryReadAddress(0xA000, 0xA000, soundlatch_r),
                new MemoryReadAddress(-1) /* end of table */};

    static MemoryWriteAddress dd2_sound_writemem[]
            = {
                new MemoryWriteAddress(0x0000, 0x7fff, MWA_ROM),
                new MemoryWriteAddress(0x8000, 0x87ff, MWA_RAM),
                new MemoryWriteAddress(0x8800, 0x8800, YM2151_register_port_0_w),
                new MemoryWriteAddress(0x8801, 0x8801, YM2151_data_port_0_w),
                new MemoryWriteAddress(0x9800, 0x9800, OKIM6295_data_0_w),
                new MemoryWriteAddress(-1) /* end of table */};

    static InputPortPtr input_ports_dd1 = new InputPortPtr() {
        public void handler() {
            PORT_START();
            PORT_BIT(0x01, IP_ACTIVE_LOW, IPT_JOYSTICK_RIGHT | IPF_8WAY);
            PORT_BIT(0x02, IP_ACTIVE_LOW, IPT_JOYSTICK_LEFT | IPF_8WAY);
            PORT_BIT(0x04, IP_ACTIVE_LOW, IPT_JOYSTICK_UP | IPF_8WAY);
            PORT_BIT(0x08, IP_ACTIVE_LOW, IPT_JOYSTICK_DOWN | IPF_8WAY);
            PORT_BIT(0x10, IP_ACTIVE_LOW, IPT_BUTTON1);
            PORT_BIT(0x20, IP_ACTIVE_LOW, IPT_BUTTON2);
            PORT_BIT(0x40, IP_ACTIVE_LOW, IPT_START1);
            PORT_BIT(0x80, IP_ACTIVE_LOW, IPT_START2);
            PORT_START();
            PORT_BIT(0x01, IP_ACTIVE_LOW, IPT_JOYSTICK_RIGHT | IPF_8WAY | IPF_PLAYER2);
            PORT_BIT(0x02, IP_ACTIVE_LOW, IPT_JOYSTICK_LEFT | IPF_8WAY | IPF_PLAYER2);
            PORT_BIT(0x04, IP_ACTIVE_LOW, IPT_JOYSTICK_UP | IPF_8WAY | IPF_PLAYER2);
            PORT_BIT(0x08, IP_ACTIVE_LOW, IPT_JOYSTICK_DOWN | IPF_8WAY | IPF_PLAYER2);
            PORT_BIT(0x10, IP_ACTIVE_LOW, IPT_BUTTON1 | IPF_PLAYER2);
            PORT_BIT(0x20, IP_ACTIVE_LOW, IPT_BUTTON2 | IPF_PLAYER2);
            PORT_BIT(0x40, IP_ACTIVE_LOW, IPT_COIN1);
            PORT_BIT(0x80, IP_ACTIVE_LOW, IPT_COIN2);
            PORT_START();
            PORT_DIPNAME(0x07, 0x07, DEF_STR("Coin_A"));
            PORT_DIPSETTING(0x00, DEF_STR("4C_1C"));
            PORT_DIPSETTING(0x01, DEF_STR("3C_1C"));
            PORT_DIPSETTING(0x02, DEF_STR("2C_1C"));
            PORT_DIPSETTING(0x07, DEF_STR("1C_1C"));
            PORT_DIPSETTING(0x06, DEF_STR("1C_2C"));
            PORT_DIPSETTING(0x05, DEF_STR("1C_3C"));
            PORT_DIPSETTING(0x04, DEF_STR("1C_4C"));
            PORT_DIPSETTING(0x03, DEF_STR("1C_5C"));
            PORT_DIPNAME(0x38, 0x38, DEF_STR("Coin_B"));
            PORT_DIPSETTING(0x00, DEF_STR("4C_1C"));
            PORT_DIPSETTING(0x08, DEF_STR("3C_1C"));
            PORT_DIPSETTING(0x10, DEF_STR("2C_1C"));
            PORT_DIPSETTING(0x38, DEF_STR("1C_1C"));
            PORT_DIPSETTING(0x30, DEF_STR("1C_2C"));
            PORT_DIPSETTING(0x28, DEF_STR("1C_3C"));
            PORT_DIPSETTING(0x20, DEF_STR("1C_4C"));
            PORT_DIPSETTING(0x18, DEF_STR("1C_5C"));
            PORT_DIPNAME(0x40, 0x40, "Screen Orientation?");
            PORT_DIPSETTING(0x40, DEF_STR("Off"));
            PORT_DIPSETTING(0x00, DEF_STR("On"));
            PORT_DIPNAME(0x80, 0x80, DEF_STR("Flip_Screen"));
            PORT_DIPSETTING(0x80, DEF_STR("Off"));
            PORT_DIPSETTING(0x00, DEF_STR("On"));

            PORT_START();       /* DSW1 */

            PORT_DIPNAME(0x03, 0x03, DEF_STR("Difficulty"));
            PORT_DIPSETTING(0x02, "Easy");
            PORT_DIPSETTING(0x03, "Normal");
            PORT_DIPSETTING(0x01, "Hard");
            PORT_DIPSETTING(0x00, "Very Hard");
            PORT_DIPNAME(0x04, 0x04, DEF_STR("Demo_Sounds"));
            PORT_DIPSETTING(0x00, DEF_STR("Off"));
            PORT_DIPSETTING(0x04, DEF_STR("On"));
            PORT_BIT(0x08, IP_ACTIVE_LOW, IPT_UNUSED);
            PORT_DIPNAME(0x30, 0x30, DEF_STR("Bonus_Life"));
            PORT_DIPSETTING(0x10, "20k");
            PORT_DIPSETTING(0x00, "40k");
            PORT_DIPSETTING(0x30, "30k and every 60k");
            PORT_DIPSETTING(0x20, "40k and every 80k");
            PORT_DIPNAME(0xc0, 0xc0, DEF_STR("Lives"));
            PORT_DIPSETTING(0xc0, "2");
            PORT_DIPSETTING(0x80, "3");
            PORT_DIPSETTING(0x40, "4");
            PORT_BITX(0, 0x00, IPT_DIPSWITCH_SETTING | IPF_CHEAT, "Infinite", IP_KEY_NONE, IP_JOY_NONE);

            PORT_START();
            PORT_BIT(0x01, IP_ACTIVE_LOW, IPT_COIN3);
            PORT_BIT(0x02, IP_ACTIVE_LOW, IPT_BUTTON3);
            PORT_BIT(0x04, IP_ACTIVE_LOW, IPT_BUTTON3 | IPF_PLAYER2);
            PORT_BIT(0x08, IP_ACTIVE_LOW, IPT_VBLANK);
            PORT_BIT(0x10, IP_ACTIVE_HIGH, IPT_UNKNOWN);
            PORT_BIT(0x20, IP_ACTIVE_LOW, IPT_UNKNOWN);
            PORT_BIT(0x40, IP_ACTIVE_LOW, IPT_UNKNOWN);
            PORT_BIT(0x80, IP_ACTIVE_LOW, IPT_UNKNOWN);
            INPUT_PORTS_END();
        }
    };

    static InputPortPtr input_ports_dd2 = new InputPortPtr() {
        public void handler() {
            PORT_START();
            PORT_BIT(0x01, IP_ACTIVE_LOW, IPT_JOYSTICK_RIGHT | IPF_8WAY);
            PORT_BIT(0x02, IP_ACTIVE_LOW, IPT_JOYSTICK_LEFT | IPF_8WAY);
            PORT_BIT(0x04, IP_ACTIVE_LOW, IPT_JOYSTICK_UP | IPF_8WAY);
            PORT_BIT(0x08, IP_ACTIVE_LOW, IPT_JOYSTICK_DOWN | IPF_8WAY);
            PORT_BIT(0x10, IP_ACTIVE_LOW, IPT_BUTTON1);
            PORT_BIT(0x20, IP_ACTIVE_LOW, IPT_BUTTON2);
            PORT_BIT(0x40, IP_ACTIVE_LOW, IPT_START1);
            PORT_BIT(0x80, IP_ACTIVE_LOW, IPT_START2);
            PORT_START();
            PORT_BIT(0x01, IP_ACTIVE_LOW, IPT_JOYSTICK_RIGHT | IPF_8WAY | IPF_PLAYER2);
            PORT_BIT(0x02, IP_ACTIVE_LOW, IPT_JOYSTICK_LEFT | IPF_8WAY | IPF_PLAYER2);
            PORT_BIT(0x04, IP_ACTIVE_LOW, IPT_JOYSTICK_UP | IPF_8WAY | IPF_PLAYER2);
            PORT_BIT(0x08, IP_ACTIVE_LOW, IPT_JOYSTICK_DOWN | IPF_8WAY | IPF_PLAYER2);
            PORT_BIT(0x10, IP_ACTIVE_LOW, IPT_BUTTON1 | IPF_PLAYER2);
            PORT_BIT(0x20, IP_ACTIVE_LOW, IPT_BUTTON2 | IPF_PLAYER2);
            PORT_BIT(0x40, IP_ACTIVE_LOW, IPT_COIN1);
            PORT_BIT(0x80, IP_ACTIVE_LOW, IPT_COIN2);
            PORT_START();
            PORT_DIPNAME(0x07, 0x07, DEF_STR("Coin_A"));
            PORT_DIPSETTING(0x00, DEF_STR("4C_1C"));
            PORT_DIPSETTING(0x01, DEF_STR("3C_1C"));
            PORT_DIPSETTING(0x02, DEF_STR("2C_1C"));
            PORT_DIPSETTING(0x07, DEF_STR("1C_1C"));
            PORT_DIPSETTING(0x06, DEF_STR("1C_2C"));
            PORT_DIPSETTING(0x05, DEF_STR("1C_3C"));
            PORT_DIPSETTING(0x04, DEF_STR("1C_4C"));
            PORT_DIPSETTING(0x03, DEF_STR("1C_5C"));
            PORT_DIPNAME(0x38, 0x38, DEF_STR("Coin_B"));
            PORT_DIPSETTING(0x00, DEF_STR("4C_1C"));
            PORT_DIPSETTING(0x08, DEF_STR("3C_1C"));
            PORT_DIPSETTING(0x10, DEF_STR("2C_1C"));
            PORT_DIPSETTING(0x38, DEF_STR("1C_1C"));
            PORT_DIPSETTING(0x30, DEF_STR("1C_2C"));
            PORT_DIPSETTING(0x28, DEF_STR("1C_3C"));
            PORT_DIPSETTING(0x20, DEF_STR("1C_4C"));
            PORT_DIPSETTING(0x18, DEF_STR("1C_5C"));
            PORT_DIPNAME(0x40, 0x40, "Screen Orientation?");
            PORT_DIPSETTING(0x40, DEF_STR("Off"));
            PORT_DIPSETTING(0x00, DEF_STR("On"));
            PORT_DIPNAME(0x80, 0x80, DEF_STR("Flip_Screen"));
            PORT_DIPSETTING(0x80, DEF_STR("Off"));
            PORT_DIPSETTING(0x00, DEF_STR("On"));

            PORT_START();       /* DSW1 */

            PORT_DIPNAME(0x03, 0x03, DEF_STR("Difficulty"));
            PORT_DIPSETTING(0x02, "Easy");
            PORT_DIPSETTING(0x03, "Normal");
            PORT_DIPSETTING(0x01, "Medium");
            PORT_DIPSETTING(0x00, "Hard");
            PORT_DIPNAME(0x04, 0x04, DEF_STR("Demo_Sounds"));
            PORT_DIPSETTING(0x00, DEF_STR("Off"));
            PORT_DIPSETTING(0x04, DEF_STR("On"));
            PORT_DIPNAME(0x08, 0x08, "Hurricane Kick");
            PORT_DIPSETTING(0x00, "Easy");
            PORT_DIPSETTING(0x08, "Normal");
            PORT_DIPNAME(0x30, 0x30, "Timer");
            PORT_DIPSETTING(0x00, "60");
            PORT_DIPSETTING(0x10, "65");
            PORT_DIPSETTING(0x30, "70");
            PORT_DIPSETTING(0x20, "80");
            PORT_DIPNAME(0xc0, 0xc0, DEF_STR("Lives"));
            PORT_DIPSETTING(0xc0, "1");
            PORT_DIPSETTING(0x80, "2");
            PORT_DIPSETTING(0x40, "3");
            PORT_DIPSETTING(0x00, "4");

            PORT_START();
            PORT_BIT(0x01, IP_ACTIVE_LOW, IPT_COIN3);
            PORT_BIT(0x02, IP_ACTIVE_LOW, IPT_BUTTON3);
            PORT_BIT(0x04, IP_ACTIVE_LOW, IPT_BUTTON3 | IPF_PLAYER2);
            PORT_BIT(0x08, IP_ACTIVE_LOW, IPT_VBLANK);
            PORT_BIT(0x10, IP_ACTIVE_HIGH, IPT_UNKNOWN);
            PORT_BIT(0x20, IP_ACTIVE_LOW, IPT_UNKNOWN);
            PORT_BIT(0x40, IP_ACTIVE_LOW, IPT_UNKNOWN);
            PORT_BIT(0x80, IP_ACTIVE_LOW, IPT_UNKNOWN);
            INPUT_PORTS_END();
        }
    };

    static GfxLayout char_layout = new GfxLayout(
            8, 8, /* 8*8 chars */
            1024, /* 'num' characters */
            4, /* 4 bits per pixel */
            new int[]{0, 2, 4, 6}, /* plane offset */
            new int[]{1, 0, 65, 64, 129, 128, 193, 192},
            new int[]{0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8},
            32 * 8 /* every char takes 32 consecutive bytes */
    );

    static GfxLayout tile_layout = new GfxLayout(
            16, 16, /* 16x16 chars */
            2048, /* 'num' characters */
            4, /* 4 bits per pixel */
            new int[]{0x20000 * 8 + 0, 0x20000 * 8 + 4, 0, 4}, /* plane offset */
            new int[]{3, 2, 1, 0, 16 * 8 + 3, 16 * 8 + 2, 16 * 8 + 1, 16 * 8 + 0,
                32 * 8 + 3, 32 * 8 + 2, 32 * 8 + 1, 32 * 8 + 0, 48 * 8 + 3, 48 * 8 + 2, 48 * 8 + 1, 48 * 8 + 0},
            new int[]{0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8,
                8 * 8, 9 * 8, 10 * 8, 11 * 8, 12 * 8, 13 * 8, 14 * 8, 15 * 8},
            64 * 8 /* every char takes 64 consecutive bytes */
    );
    static GfxLayout sprite_layout = new GfxLayout(
            16, 16, /* 16x16 chars */
            2048 * 2, /* 'num' characters */
            4, /* 4 bits per pixel */
            new int[]{0x40000 * 8 + 0, 0x40000 * 8 + 4, 0, 4}, /* plane offset */
            new int[]{3, 2, 1, 0, 16 * 8 + 3, 16 * 8 + 2, 16 * 8 + 1, 16 * 8 + 0,
                32 * 8 + 3, 32 * 8 + 2, 32 * 8 + 1, 32 * 8 + 0, 48 * 8 + 3, 48 * 8 + 2, 48 * 8 + 1, 48 * 8 + 0},
            new int[]{0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8,
                8 * 8, 9 * 8, 10 * 8, 11 * 8, 12 * 8, 13 * 8, 14 * 8, 15 * 8},
            64 * 8 /* every char takes 64 consecutive bytes */
    );

    static GfxDecodeInfo gfxdecodeinfo[]
            = {
                new GfxDecodeInfo(REGION_GFX1, 0, char_layout, 0, 8), /* 8x8 text */
                new GfxDecodeInfo(REGION_GFX2, 0, sprite_layout, 128, 8), /* 16x16 sprites */
                new GfxDecodeInfo(REGION_GFX3, 0, tile_layout, 256, 8), /* 16x16 background tiles */
                new GfxDecodeInfo(-1)
            };
    static GfxLayout dd2_char_layout = new GfxLayout(
            8, 8, /* 8*8 chars */
            2048, /* 'num' characters */
            4, /* 4 bits per pixel */
            new int[]{0, 2, 4, 6}, /* plane offset */
            new int[]{1, 0, 65, 64, 129, 128, 193, 192},
            new int[]{0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8},
            32 * 8 /* every char takes 32 consecutive bytes */
    );
    static GfxLayout dd2_sprite_layout = new GfxLayout(
            16, 16, /* 16x16 chars */
            2048 * 3, /* 'num' characters */
            4, /* 4 bits per pixel */
            new int[]{0x60000 * 8 + 0, 0x60000 * 8 + 4, 0, 4}, /* plane offset */
            new int[]{3, 2, 1, 0, 16 * 8 + 3, 16 * 8 + 2, 16 * 8 + 1, 16 * 8 + 0,
                32 * 8 + 3, 32 * 8 + 2, 32 * 8 + 1, 32 * 8 + 0, 48 * 8 + 3, 48 * 8 + 2, 48 * 8 + 1, 48 * 8 + 0},
            new int[]{0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8,
                8 * 8, 9 * 8, 10 * 8, 11 * 8, 12 * 8, 13 * 8, 14 * 8, 15 * 8},
            64 * 8 /* every char takes 64 consecutive bytes */
    );

    /* background tiles encoding for dd2 is the same as dd1 */
    static GfxDecodeInfo dd2_gfxdecodeinfo[]
            = {
                new GfxDecodeInfo(REGION_GFX1, 0, dd2_char_layout, 0, 8), /* 8x8 chars */
                new GfxDecodeInfo(REGION_GFX2, 0, dd2_sprite_layout, 128, 8), /* 16x16 sprites */
                new GfxDecodeInfo(REGION_GFX3, 0, tile_layout, 256, 8), /* 16x16 background tiles */
                new GfxDecodeInfo(-1) // end of array
            };
    public static WriteYmHandlerPtr dd_irq_handler = new WriteYmHandlerPtr() {
        public void handler(int irq) {
            cpu_set_irq_line(2, ym_irq, irq != 0 ? ASSERT_LINE : CLEAR_LINE);
        }
    };

    static YM2151interface ym2151_interface = new YM2151interface(
            1, /* 1 chip */
            3579545, /* ??? */
            new int[]{YM3012_VOL(60, MIXER_PAN_LEFT, 60, MIXER_PAN_RIGHT)},
            new WriteYmHandlerPtr[]{dd_irq_handler}
    );

    static ADPCMinterface adpcm_interface = new ADPCMinterface(
            2, /* 2 channels */
            8000, /* 8000Hz playback */
            REGION_SOUND1, /* memory region 4 */
            null, /* init function */
            new int[]{50, 50}
    );

    static OKIM6295interface okim6295_interface = new OKIM6295interface(
            1, /* 1 chip */
            new int[]{8000}, /* frequency (Hz) */
            new int[]{REGION_SOUND1}, /* memory region */
            new int[]{15}
    );

    public static InterruptPtr dd_interrupt = new InterruptPtr() {
        public int handler() {
            cpu_set_irq_line(0, 1, HOLD_LINE); /* hold the FIRQ line */

            cpu_set_nmi_line(0, PULSE_LINE); /* pulse the NMI line */

            return M6809_INT_NONE;
        }
    };

    static MachineDriver machine_driver_ddragon = new MachineDriver(
            /* basic machine hardware */
            new MachineCPU[]{
                new MachineCPU(
                        CPU_HD6309,
                        3579545, /* 3.579545 Mhz */
                        readmem, writemem, null, null,
                        dd_interrupt, 1
                ),
                new MachineCPU(
                        CPU_HD63701, /* we're missing the code for this one */
                        2000000, /* 2 Mhz ???*/
                        sub_readmem, sub_writemem, null, null,
                        ignore_interrupt, 0
                ),
                new MachineCPU(
                        CPU_HD6309 | CPU_AUDIO_CPU, /* ? */
                        3579545, /* 3.579545 Mhz */
                        sound_readmem, sound_writemem, null, null,
                        ignore_interrupt, 0 /* irq on command */
                )
            },
            60, DEFAULT_REAL_60HZ_VBLANK_DURATION, /* frames per second, vblank duration */
            100, /* heavy interleaving to sync up sprite<.main cpu's */
            dd_init_machine,
            /* video hardware */
            32 * 8, 32 * 8, new rectangle(1 * 8, 31 * 8 - 1, 2 * 8, 30 * 8 - 1),
            gfxdecodeinfo,
            384, 384,
            null,
            VIDEO_TYPE_RASTER | VIDEO_MODIFIES_PALETTE,
            null,
            dd_vh_start,
            dd_vh_stop,
            dd_vh_screenrefresh,
            SOUND_SUPPORTS_STEREO,0,0,0,
            new MachineSound[]{
                new MachineSound(
                        SOUND_YM2151,
                        ym2151_interface
                ),
                new MachineSound(
                        SOUND_ADPCM,
                        adpcm_interface
                ),}
    );

    static MachineDriver machine_driver_ddragonb = new MachineDriver(
            /* basic machine hardware */
            new MachineCPU[]{
                new MachineCPU(
                        CPU_HD6309,
                        3579545, /* 3.579545 Mhz */
                        readmem, writemem, null, null,
                        dd_interrupt, 1
                ),
                new MachineCPU(
                        CPU_HD6309, /* ? */
                        12000000 / 3, /* 4 Mhz */
                        sub_readmem, sub_writemem, null, null,
                        ignore_interrupt, 0
                ),
                new MachineCPU(
                        CPU_HD6309 | CPU_AUDIO_CPU, /* ? */
                        3579545, /* 3.579545 Mhz */
                        sound_readmem, sound_writemem, null, null,
                        ignore_interrupt, 0 /* irq on command */
                )
            },
            60, DEFAULT_REAL_60HZ_VBLANK_DURATION, /* frames per second, vblank duration */
            100, /* heavy interleaving to sync up sprite<.main cpu's */
            dd_init_machine,
            /* video hardware */
            32 * 8, 32 * 8, new rectangle(1 * 8, 31 * 8 - 1, 2 * 8, 30 * 8 - 1),
            gfxdecodeinfo,
            384, 384,
            null,
            VIDEO_TYPE_RASTER | VIDEO_MODIFIES_PALETTE,
            null,
            dd_vh_start,
            dd_vh_stop,
            dd_vh_screenrefresh,
            /* sound hardware */
            SOUND_SUPPORTS_STEREO,0,0,0,
            new MachineSound[]{
                new MachineSound(
                        SOUND_YM2151,
                        ym2151_interface
                ),
                new MachineSound(
                        SOUND_ADPCM,
                        adpcm_interface
                ),}
    );

    static MachineDriver machine_driver_ddragon2 = new MachineDriver(
            /* basic machine hardware */
            new MachineCPU[]{
                new MachineCPU(
                        CPU_HD6309,
                        3579545, /* 3.579545 Mhz */
                        readmem, dd2_writemem, null, null,
                        dd_interrupt, 1
                ),
                new MachineCPU(
                        CPU_Z80,
                        12000000 / 3, /* 4 Mhz */
                        dd2_sub_readmem, dd2_sub_writemem, null, null,
                        ignore_interrupt, 0
                ),
                new MachineCPU(
                        CPU_Z80 | CPU_AUDIO_CPU,
                        3579545, /* 3.579545 Mhz */
                        dd2_sound_readmem, dd2_sound_writemem, null, null,
                        ignore_interrupt, 0
                )
            },
            60, DEFAULT_REAL_60HZ_VBLANK_DURATION, /* frames per second, vblank duration */
            100, /* heavy interleaving to sync up sprite<.main cpu's */
            dd2_init_machine,
            /* video hardware */
            32 * 8, 32 * 8, new rectangle(1 * 8, 31 * 8 - 1, 2 * 8, 30 * 8 - 1),
            dd2_gfxdecodeinfo,
            384, 384,
            null,
            VIDEO_TYPE_RASTER | VIDEO_MODIFIES_PALETTE,
            null,
            dd_vh_start,
            dd_vh_stop,
            dd_vh_screenrefresh,
            /* sound hardware */
            SOUND_SUPPORTS_STEREO,0,0,0,
            new MachineSound[]{
                new MachineSound(
                        SOUND_YM2151,
                        ym2151_interface
                ),
                new MachineSound(
                        SOUND_OKIM6295,
                        okim6295_interface
                )
            }
    );

    /**
     * *************************************************************************
     *
     * Game driver(s)
     *
     **************************************************************************
     */
    static RomLoadPtr rom_ddragon = new RomLoadPtr() {
        public void handler() {
            ROM_REGION(0x28000, REGION_CPU1);/* 64k for code + bankswitched memory */

            ROM_LOAD("a_m2_d02.bin", 0x08000, 0x08000, 0x668dfa19);
            ROM_LOAD("a_k2_d03.bin", 0x10000, 0x08000, 0x5779705e);/* banked at 0x4000-0x8000 */

            ROM_LOAD("a_h2_d04.bin", 0x18000, 0x08000, 0x3bdea613);/* banked at 0x4000-0x8000 */

            ROM_LOAD("a_g2_d05.bin", 0x20000, 0x08000, 0x728f87b9);/* banked at 0x4000-0x8000 */

            ROM_REGION(0x10000, REGION_CPU2);/* sprite cpu */
            /* missing mcu code */

            ROM_LOAD("63701.bin", 0xc000, 0x4000, 0x00000000);

            ROM_REGION(0x10000, REGION_CPU3);/* audio cpu */

            ROM_LOAD("a_s2_d01.bin", 0x08000, 0x08000, 0x9efa95bb);

            ROM_REGION(0x08000, REGION_GFX1 | REGIONFLAG_DISPOSE);
            ROM_LOAD("a_a2_d06.bin", 0x00000, 0x08000, 0x7a8b8db4);/* 0,1,2,3 */ /* text */


            ROM_REGION(0x80000, REGION_GFX2 | REGIONFLAG_DISPOSE);
            ROM_LOAD("b_r7_d11.bin", 0x00000, 0x10000, 0x574face3);/* 0,1 */ /* sprites */


            ROM_LOAD("b_p7_d12.bin", 0x10000, 0x10000, 0x40507a76);/* 0,1 */ /* sprites */


            ROM_LOAD("b_m7_d13.bin", 0x20000, 0x10000, 0xbb0bc76f);/* 0,1 */ /* sprites */


            ROM_LOAD("b_l7_d14.bin", 0x30000, 0x10000, 0xcb4f231b);/* 0,1 */ /* sprites */


            ROM_LOAD("b_j7_d15.bin", 0x40000, 0x10000, 0xa0a0c261);/* 2,3 */ /* sprites */


            ROM_LOAD("b_h7_d16.bin", 0x50000, 0x10000, 0x6ba152f6);/* 2,3 */ /* sprites */


            ROM_LOAD("b_f7_d17.bin", 0x60000, 0x10000, 0x3220a0b6);/* 2,3 */ /* sprites */


            ROM_LOAD("b_d7_d18.bin", 0x70000, 0x10000, 0x65c7517d);/* 2,3 */ /* sprites */


            ROM_REGION(0x40000, REGION_GFX3 | REGIONFLAG_DISPOSE);
            ROM_LOAD("b_c5_d09.bin", 0x00000, 0x10000, 0x7c435887);/* 0,1 */ /* tiles */


            ROM_LOAD("b_a5_d10.bin", 0x10000, 0x10000, 0xc6640aed);/* 0,1 */ /* tiles */


            ROM_LOAD("b_c7_d19.bin", 0x20000, 0x10000, 0x5effb0a0);/* 2,3 */ /* tiles */


            ROM_LOAD("b_a7_d20.bin", 0x30000, 0x10000, 0x5fb42e7c);/* 2,3 */ /* tiles */


            ROM_REGION(0x20000, REGION_SOUND1);/* adpcm samples */

            ROM_LOAD("a_s6_d07.bin", 0x00000, 0x10000, 0x34755de3);
            ROM_LOAD("a_r6_d08.bin", 0x10000, 0x10000, 0x904de6f8);
            ROM_END();
        }
    };

    static RomLoadPtr rom_ddragonb = new RomLoadPtr() {
        public void handler() {
            ROM_REGION(0x28000, REGION_CPU1);/* 64k for code + bankswitched memory */

            ROM_LOAD("ic26", 0x08000, 0x08000, 0xae714964);
            ROM_LOAD("a_k2_d03.bin", 0x10000, 0x08000, 0x5779705e);/* banked at 0x4000-0x8000 */

            ROM_LOAD("ic24", 0x18000, 0x08000, 0xdbf24897);/* banked at 0x4000-0x8000 */

            ROM_LOAD("ic23", 0x20000, 0x08000, 0x6c9f46fa);/* banked at 0x4000-0x8000 */

            ROM_REGION(0x10000, REGION_CPU2);/* sprite cpu */

            ROM_LOAD("ic38", 0x0c000, 0x04000, 0x6a6a0325);

            ROM_REGION(0x10000, REGION_CPU3);/* audio cpu */

            ROM_LOAD("a_s2_d01.bin", 0x08000, 0x08000, 0x9efa95bb);

            ROM_REGION(0x08000, REGION_GFX1 | REGIONFLAG_DISPOSE);
            ROM_LOAD("a_a2_d06.bin", 0x00000, 0x08000, 0x7a8b8db4);/* 0,1,2,3 */ /* text */


            ROM_REGION(0x80000, REGION_GFX2 | REGIONFLAG_DISPOSE);
            ROM_LOAD("b_r7_d11.bin", 0x00000, 0x10000, 0x574face3);/* 0,1 */ /* sprites */


            ROM_LOAD("b_p7_d12.bin", 0x10000, 0x10000, 0x40507a76);/* 0,1 */ /* sprites */


            ROM_LOAD("b_m7_d13.bin", 0x20000, 0x10000, 0xbb0bc76f);/* 0,1 */ /* sprites */


            ROM_LOAD("b_l7_d14.bin", 0x30000, 0x10000, 0xcb4f231b);/* 0,1 */ /* sprites */


            ROM_LOAD("b_j7_d15.bin", 0x40000, 0x10000, 0xa0a0c261);/* 2,3 */ /* sprites */


            ROM_LOAD("b_h7_d16.bin", 0x50000, 0x10000, 0x6ba152f6);/* 2,3 */ /* sprites */


            ROM_LOAD("b_f7_d17.bin", 0x60000, 0x10000, 0x3220a0b6);/* 2,3 */ /* sprites */


            ROM_LOAD("b_d7_d18.bin", 0x70000, 0x10000, 0x65c7517d);/* 2,3 */ /* sprites */


            ROM_REGION(0x40000, REGION_GFX3 | REGIONFLAG_DISPOSE);
            ROM_LOAD("b_c5_d09.bin", 0x00000, 0x10000, 0x7c435887);/* 0,1 */ /* tiles */


            ROM_LOAD("b_a5_d10.bin", 0x10000, 0x10000, 0xc6640aed);/* 0,1 */ /* tiles */


            ROM_LOAD("b_c7_d19.bin", 0x20000, 0x10000, 0x5effb0a0);/* 2,3 */ /* tiles */


            ROM_LOAD("b_a7_d20.bin", 0x30000, 0x10000, 0x5fb42e7c);/* 2,3 */ /* tiles */


            ROM_REGION(0x20000, REGION_SOUND1);/* adpcm samples */

            ROM_LOAD("a_s6_d07.bin", 0x00000, 0x10000, 0x34755de3);
            ROM_LOAD("a_r6_d08.bin", 0x10000, 0x10000, 0x904de6f8);
            ROM_END();
        }
    };

    static RomLoadPtr rom_ddragon2 = new RomLoadPtr() {
        public void handler() {
            ROM_REGION(0x28000, REGION_CPU1);/* region#0: 64k for code */

            ROM_LOAD("26a9-04.bin", 0x08000, 0x8000, 0xf2cfc649);
            ROM_LOAD("26aa-03.bin", 0x10000, 0x8000, 0x44dd5d4b);
            ROM_LOAD("26ab-0.bin", 0x18000, 0x8000, 0x49ddddcd);
            ROM_LOAD("26ac-02.bin", 0x20000, 0x8000, 0x097eaf26);

            ROM_REGION(0x10000, REGION_CPU2);/* region#2: sprite CPU 64kb (Upper 16kb = 0) */

            ROM_LOAD("26ae-0.bin", 0x00000, 0x10000, 0xea437867);

            ROM_REGION(0x10000, REGION_CPU3);/* region#3: music CPU, 64kb */

            ROM_LOAD("26ad-0.bin", 0x00000, 0x8000, 0x75e36cd6);

            ROM_REGION(0x10000, REGION_GFX1 | REGIONFLAG_DISPOSE);
            ROM_LOAD("26a8-0.bin", 0x00000, 0x10000, 0x3ad1049c);/* 0,1,2,3 */ /* text */


            ROM_REGION(0xc0000, REGION_GFX2 | REGIONFLAG_DISPOSE);
            ROM_LOAD("26j0-0.bin", 0x00000, 0x20000, 0xdb309c84);/* 0,1 */ /* sprites */


            ROM_LOAD("26j1-0.bin", 0x20000, 0x20000, 0xc3081e0c);/* 0,1 */ /* sprites */


            ROM_LOAD("26af-0.bin", 0x40000, 0x20000, 0x3a615aad);/* 0,1 */ /* sprites */


            ROM_LOAD("26j2-0.bin", 0x60000, 0x20000, 0x589564ae);/* 2,3 */ /* sprites */


            ROM_LOAD("26j3-0.bin", 0x80000, 0x20000, 0xdaf040d6);/* 2,3 */ /* sprites */


            ROM_LOAD("26a10-0.bin", 0xa0000, 0x20000, 0x6d16d889);/* 2,3 */ /* sprites */


            ROM_REGION(0x40000, REGION_GFX3 | REGIONFLAG_DISPOSE);
            ROM_LOAD("26j4-0.bin", 0x00000, 0x20000, 0xa8c93e76);/* 0,1 */ /* tiles */


            ROM_LOAD("26j5-0.bin", 0x20000, 0x20000, 0xee555237);/* 2,3 */ /* tiles */


            ROM_REGION(0x40000, REGION_SOUND1);/* region#4: adpcm */

            ROM_LOAD("26j6-0.bin", 0x00000, 0x20000, 0xa84b2a29);
            ROM_LOAD("26j7-0.bin", 0x20000, 0x20000, 0xbc6a48d5);
            ROM_END();
        }
    };

    public static GameDriver driver_ddragon = new GameDriver("1987", "ddragon", "ddragon.java", rom_ddragon, null, machine_driver_ddragon, input_ports_dd1, null, ROT0, "bootleg?", "Double Dragon", GAME_NOT_WORKING);
    public static GameDriver driver_ddragonb = new GameDriver("1987", "ddragonb", "ddragon.java", rom_ddragonb, driver_ddragon, machine_driver_ddragonb, input_ports_dd1, null, ROT0, "bootleg", "Double Dragon (bootleg)");
    public static GameDriver driver_ddragon2 = new GameDriver("1988", "ddragon2", "ddragon.java", rom_ddragon2, null, machine_driver_ddragon2, input_ports_dd2, null, ROT0, "Technos", "Double Dragon II - The Revenge");
}
