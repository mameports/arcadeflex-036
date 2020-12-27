/***************************************************************************

Mat Mania
Memetron, 1985
(copyright Taito, licensed by Technos, distributed by Memetron)

driver by Brad Oliver

MAIN BOARD:

0000-0fff RAM
1000-13ff Video RAM
1400-17ff Attribute RAM
1800-1fff ?? Only used in self-test ??
2000-21ff Background video RAM #1
2200-23ff Background attribute RAM #1
2400-25ff Background video RAM #2
2600-27ff Background attribute RAM #2
4000-ffff ROM

***************************************************************************/

/*
 * ported to v0.36
 * using automatic conversion tool v0.10
 */ 
package gr.codebb.arcadeflex.v036.drivers;
import static gr.codebb.arcadeflex.v036.mame.driverH.*;
import static gr.codebb.arcadeflex.v037b7.mame.memoryH.*;
import static gr.codebb.arcadeflex.v036.mame.commonH.*;
import static gr.codebb.arcadeflex.v036.mame.inputport.*;
import static gr.codebb.arcadeflex.v037b7.mame.drawgfxH.*;
import static gr.codebb.arcadeflex.v036.vidhrdw.generic.*;
import static gr.codebb.arcadeflex.v036.mame.sndintrfH.*;
import static gr.codebb.arcadeflex.v037b7.mame.cpuintrf.*;
import static gr.codebb.arcadeflex.v036.mame.common.*;
import static gr.codebb.arcadeflex.v036.platform.input.*;
import static gr.codebb.arcadeflex.v036.mame.inputportH.*;
import static gr.codebb.arcadeflex.v036.mame.inputH.*;
import static gr.codebb.arcadeflex.v036.platform.libc.*;
import static gr.codebb.arcadeflex.v036.platform.libc_old.*;
import static gr.codebb.arcadeflex.v037b7.vidhrdw.matmania.*;
import static gr.codebb.arcadeflex.v036.sound.samplesH.*;
import static gr.codebb.arcadeflex.v036.mame.memory.*;
import static gr.codebb.arcadeflex.v037b7.sound.ay8910.*;
import static gr.codebb.arcadeflex.v037b7.sound.ay8910H.*;
import static gr.codebb.arcadeflex.v036.mame.mame.*;
import static gr.codebb.arcadeflex.v036.mame.sndintrf.*;
import static gr.codebb.arcadeflex.v037b7.mame.palette.*;
import static gr.codebb.arcadeflex.v036.cpu.m6502.m6502H.*;
import static gr.codebb.arcadeflex.v036.cpu.m6809.m6809H.*;
import static gr.codebb.arcadeflex.v037b7.sound.dacH.*;
import static gr.codebb.arcadeflex.v036.machine.maniach.*;


public class matmania
{
	
	

	public static WriteHandlerPtr matmania_sh_command_w = new WriteHandlerPtr() { public void handler(int offset, int data)
	{
		soundlatch_w.handler(offset,data);
		cpu_cause_interrupt(1,M6502_INT_IRQ);
	} };
	
	public static WriteHandlerPtr matmania_dac_w = new WriteHandlerPtr() { public void handler(int offset, int data)
	{
/*TODO*///		DAC_signed_data_w(0,data);
	} };
	
	
	public static WriteHandlerPtr maniach_sh_command_w = new WriteHandlerPtr() { public void handler(int offset, int data)
	{
		soundlatch_w.handler(offset,data);
		cpu_cause_interrupt(1,M6809_INT_IRQ);
	} };
	
	
	
	static MemoryReadAddress matmania_readmem[] =
	{
		new MemoryReadAddress( 0x0000, 0x077f, MRA_RAM ),
		new MemoryReadAddress( 0x1000, 0x17ff, MRA_RAM ),
		new MemoryReadAddress( 0x2000, 0x27ff, MRA_RAM ),
		new MemoryReadAddress( 0x3000, 0x3000, input_port_0_r ),
		new MemoryReadAddress( 0x3010, 0x3010, input_port_1_r ),
		new MemoryReadAddress( 0x3020, 0x3020, input_port_2_r ),
		new MemoryReadAddress( 0x3030, 0x3030, input_port_3_r ),
		new MemoryReadAddress( 0x4000, 0xffff, MRA_ROM ),
		new MemoryReadAddress( -1 )	/* end of table */
	};
	
	static MemoryWriteAddress matmania_writemem[] =
	{
		new MemoryWriteAddress( 0x0000, 0x077f, MWA_RAM ),
		new MemoryWriteAddress( 0x0780, 0x07df, MWA_RAM, spriteram, spriteram_size ),
		new MemoryWriteAddress( 0x1000, 0x13ff, MWA_RAM, matmania_videoram2, matmania_videoram2_size ),
		new MemoryWriteAddress( 0x1400, 0x17ff, MWA_RAM, matmania_colorram2 ),
		new MemoryWriteAddress( 0x2000, 0x21ff, videoram_w, videoram, videoram_size ),
		new MemoryWriteAddress( 0x2200, 0x23ff, colorram_w, colorram ),
		new MemoryWriteAddress( 0x2400, 0x25ff, matmania_videoram3_w, matmania_videoram3, matmania_videoram3_size ),
		new MemoryWriteAddress( 0x2600, 0x27ff, matmania_colorram3_w, matmania_colorram3 ),
		new MemoryWriteAddress( 0x3000, 0x3000, MWA_RAM, matmania_pageselect ),
		new MemoryWriteAddress( 0x3010, 0x3010, matmania_sh_command_w ),
		new MemoryWriteAddress( 0x3020, 0x3020, MWA_RAM, matmania_scroll ),
	//	new MemoryWriteAddress( 0x3030, 0x3030, MWA_NOP ),	/* ?? */
		new MemoryWriteAddress( 0x3050, 0x307f, matmania_paletteram_w, paletteram ),
		new MemoryWriteAddress( 0x4000, 0xffff, MWA_ROM ),
		new MemoryWriteAddress( -1 )	/* end of table */
	};
	
	static MemoryReadAddress maniach_readmem[] =
	{
		new MemoryReadAddress( 0x0000, 0x077f, MRA_RAM ),
		new MemoryReadAddress( 0x1000, 0x17ff, MRA_RAM ),
		new MemoryReadAddress( 0x2000, 0x27ff, MRA_RAM ),
		new MemoryReadAddress( 0x3000, 0x3000, input_port_0_r ),
		new MemoryReadAddress( 0x3010, 0x3010, input_port_1_r ),
		new MemoryReadAddress( 0x3020, 0x3020, input_port_2_r ),
		new MemoryReadAddress( 0x3030, 0x3030, input_port_3_r ),
		new MemoryReadAddress( 0x3040, 0x3040, maniach_mcu_r ),
		new MemoryReadAddress( 0x3041, 0x3041, maniach_mcu_status_r ),
		new MemoryReadAddress( 0x4000, 0xffff, MRA_ROM ),
		new MemoryReadAddress( -1 )	/* end of table */
	};
	
	static MemoryWriteAddress maniach_writemem[] =
	{
		new MemoryWriteAddress( 0x0000, 0x077f, MWA_RAM ),
		new MemoryWriteAddress( 0x0780, 0x07df, MWA_RAM, spriteram, spriteram_size ),
		new MemoryWriteAddress( 0x1000, 0x13ff, MWA_RAM, matmania_videoram2, matmania_videoram2_size ),
		new MemoryWriteAddress( 0x1400, 0x17ff, MWA_RAM, matmania_colorram2 ),
		new MemoryWriteAddress( 0x2000, 0x21ff, videoram_w, videoram, videoram_size ),
		new MemoryWriteAddress( 0x2200, 0x23ff, colorram_w, colorram ),
		new MemoryWriteAddress( 0x2400, 0x25ff, matmania_videoram3_w, matmania_videoram3, matmania_videoram3_size ),
		new MemoryWriteAddress( 0x2600, 0x27ff, matmania_colorram3_w, matmania_colorram3 ),
		new MemoryWriteAddress( 0x3000, 0x3000, MWA_RAM, matmania_pageselect ),
		new MemoryWriteAddress( 0x3010, 0x3010, maniach_sh_command_w ),
		new MemoryWriteAddress( 0x3020, 0x3020, MWA_RAM, matmania_scroll ),
		new MemoryWriteAddress( 0x3030, 0x3030, MWA_NOP ),	/* ?? */
		new MemoryWriteAddress( 0x3040, 0x3040, maniach_mcu_w ),
		new MemoryWriteAddress( 0x3050, 0x307f, matmania_paletteram_w, paletteram ),
		new MemoryWriteAddress( 0x4000, 0xffff, MWA_ROM ),
		new MemoryWriteAddress( -1 )	/* end of table */
	};
	
	static MemoryReadAddress sound_readmem[] =
	{
		new MemoryReadAddress( 0x0000, 0x01ff, MRA_RAM ),
		new MemoryReadAddress( 0x2007, 0x2007, soundlatch_r ),
		new MemoryReadAddress( 0x8000, 0xffff, MRA_ROM ),
		new MemoryReadAddress( -1 )	/* end of table */
	};
	
	static MemoryWriteAddress sound_writemem[] =
	{
		new MemoryWriteAddress( 0x0000, 0x01ff, MWA_RAM ),
		new MemoryWriteAddress( 0x2000, 0x2000, AY8910_write_port_0_w ),
		new MemoryWriteAddress( 0x2001, 0x2001, AY8910_control_port_0_w ),
		new MemoryWriteAddress( 0x2002, 0x2002, AY8910_write_port_1_w ),
		new MemoryWriteAddress( 0x2003, 0x2003, AY8910_control_port_1_w ),
		new MemoryWriteAddress( 0x2004, 0x2004, matmania_dac_w ),
		new MemoryWriteAddress( 0x8000, 0xffff, MWA_ROM ),
		new MemoryWriteAddress( -1 )	/* end of table */
	};
	
	static MemoryReadAddress maniach_sound_readmem[] =
	{
		new MemoryReadAddress( 0x0000, 0x0fff, MRA_RAM ),
		new MemoryReadAddress( 0x2004, 0x2004, soundlatch_r ),
		new MemoryReadAddress( 0x4000, 0xffff, MRA_ROM ),
		new MemoryReadAddress( -1 )	/* end of table */
	};
	
	static MemoryWriteAddress maniach_sound_writemem[] =
	{
		new MemoryWriteAddress( 0x0000, 0x0fff, MWA_RAM ),
/*TODO*///		new MemoryWriteAddress( 0x2000, 0x2000, YM3526_control_port_0_w ),
/*TODO*///		new MemoryWriteAddress( 0x2001, 0x2001, YM3526_write_port_0_w ),
		new MemoryWriteAddress( 0x2002, 0x2002, matmania_dac_w ),
		new MemoryWriteAddress( 0x4000, 0xffff, MWA_ROM ),
		new MemoryWriteAddress( -1 )	/* end of table */
	};
	
	static MemoryReadAddress mcu_readmem[] =
	{
		new MemoryReadAddress( 0x0000, 0x0000, maniach_68705_portA_r ),
		new MemoryReadAddress( 0x0001, 0x0001, maniach_68705_portB_r ),
		new MemoryReadAddress( 0x0002, 0x0002, maniach_68705_portC_r ),
		new MemoryReadAddress( 0x0010, 0x007f, MRA_RAM ),
		new MemoryReadAddress( 0x0080, 0x07ff, MRA_ROM ),
		new MemoryReadAddress( -1 )	/* end of table */
	};
	
	static MemoryWriteAddress mcu_writemem[] =
	{
		new MemoryWriteAddress( 0x0000, 0x0000, maniach_68705_portA_w ),
		new MemoryWriteAddress( 0x0001, 0x0001, maniach_68705_portB_w ),
		new MemoryWriteAddress( 0x0002, 0x0002, maniach_68705_portC_w ),
		new MemoryWriteAddress( 0x0004, 0x0004, maniach_68705_ddrA_w ),
		new MemoryWriteAddress( 0x0005, 0x0005, maniach_68705_ddrB_w ),
		new MemoryWriteAddress( 0x0006, 0x0006, maniach_68705_ddrC_w ),
		new MemoryWriteAddress( 0x0010, 0x007f, MWA_RAM ),
		new MemoryWriteAddress( 0x0080, 0x07ff, MWA_ROM ),
		new MemoryWriteAddress( -1 )	/* end of table */
	};
	
	
	
	static InputPortPtr input_ports_matmania = new InputPortPtr(){ public void handler() { 
		PORT_START(); 	/* IN0 */
		PORT_BIT( 0x01, IP_ACTIVE_LOW, IPT_JOYSTICK_RIGHT | IPF_8WAY );
		PORT_BIT( 0x02, IP_ACTIVE_LOW, IPT_JOYSTICK_LEFT  | IPF_8WAY );
		PORT_BIT( 0x04, IP_ACTIVE_LOW, IPT_JOYSTICK_UP    | IPF_8WAY );
		PORT_BIT( 0x08, IP_ACTIVE_LOW, IPT_JOYSTICK_DOWN  | IPF_8WAY );
		PORT_BIT( 0x10, IP_ACTIVE_LOW, IPT_BUTTON1 );
		PORT_BIT( 0x20, IP_ACTIVE_LOW, IPT_BUTTON2 );
		PORT_BIT_IMPULSE( 0x40, IP_ACTIVE_LOW, IPT_COIN2, 1 );
		PORT_BIT_IMPULSE( 0x80, IP_ACTIVE_LOW, IPT_COIN1, 1 );
	
		PORT_START(); 	/* IN1 */
		PORT_BIT( 0x01, IP_ACTIVE_LOW, IPT_JOYSTICK_RIGHT | IPF_8WAY | IPF_PLAYER2 );
		PORT_BIT( 0x02, IP_ACTIVE_LOW, IPT_JOYSTICK_LEFT  | IPF_8WAY | IPF_PLAYER2 );
		PORT_BIT( 0x04, IP_ACTIVE_LOW, IPT_JOYSTICK_UP    | IPF_8WAY | IPF_PLAYER2 );
		PORT_BIT( 0x08, IP_ACTIVE_LOW, IPT_JOYSTICK_DOWN  | IPF_8WAY | IPF_PLAYER2 );
		PORT_BIT( 0x10, IP_ACTIVE_LOW, IPT_BUTTON1 | IPF_PLAYER2 );
		PORT_BIT( 0x20, IP_ACTIVE_LOW, IPT_BUTTON2 | IPF_PLAYER2 );
		PORT_BIT( 0x40, IP_ACTIVE_LOW, IPT_START1 );
		PORT_BIT( 0x80, IP_ACTIVE_LOW, IPT_START2 );
	
		PORT_START(); 	/* DSW1 */
		PORT_DIPNAME(0x03, 0x03, DEF_STR( "Difficulty") );
		PORT_DIPSETTING(   0x03, "Easy" );
		PORT_DIPSETTING(   0x02, "Medium" );
		PORT_DIPSETTING(   0x01, "Hard" );
		PORT_DIPSETTING(   0x00, "Hardest" );
		PORT_DIPNAME(0x04, 0x04, DEF_STR( "Unknown") );
		PORT_DIPSETTING(   0x04, DEF_STR( "Off") );
		PORT_DIPSETTING(   0x00, DEF_STR( "On") );
		PORT_DIPNAME(0x08, 0x08, DEF_STR( "Unknown") );
		PORT_DIPSETTING(   0x08, DEF_STR( "Off") );
		PORT_DIPSETTING(   0x00, DEF_STR( "On") );
		PORT_DIPNAME(0x10, 0x10, DEF_STR( "Unknown") );
		PORT_DIPSETTING(   0x10, DEF_STR( "Off") );
		PORT_DIPSETTING(   0x00, DEF_STR( "On") );
		PORT_DIPNAME(0x20, 0x20, DEF_STR( "Unknown") );
		PORT_DIPSETTING(   0x20, DEF_STR( "Off") );
		PORT_DIPSETTING(   0x00, DEF_STR( "On") );
		PORT_DIPNAME(0x40, 0x40, DEF_STR( "Unknown") );
		PORT_DIPSETTING(   0x40, DEF_STR( "Off") );
		PORT_DIPSETTING(   0x00, DEF_STR( "On") );
		PORT_DIPNAME(0x80, 0x80, DEF_STR( "Unknown") );
		PORT_DIPSETTING(   0x80, DEF_STR( "Off") );
		PORT_DIPSETTING(   0x00, DEF_STR( "On") );
	
		PORT_START(); 	/* DSW2 */
		PORT_DIPNAME(0x03, 0x03, DEF_STR( "Coin_A") );
		PORT_DIPSETTING(   0x00, DEF_STR( "2C_1C") );
		PORT_DIPSETTING(   0x03, DEF_STR( "1C_1C") );
		PORT_DIPSETTING(   0x02, DEF_STR( "1C_2C") );
		PORT_DIPSETTING(   0x01, DEF_STR( "1C_3C") );
		PORT_DIPNAME(0x0c, 0x0c, DEF_STR( "Coin_B") );
		PORT_DIPSETTING(   0x00, DEF_STR( "2C_1C") );
		PORT_DIPSETTING(   0x0c, DEF_STR( "1C_1C") );
		PORT_DIPSETTING(   0x08, DEF_STR( "1C_2C") );
		PORT_DIPSETTING(   0x04, DEF_STR( "1C_3C") );
		PORT_DIPNAME(0x10, 0x10, DEF_STR( "Demo_Sounds") );
		PORT_DIPSETTING(   0x00, DEF_STR( "Off") );
		PORT_DIPSETTING(   0x10, DEF_STR( "On") );
		PORT_DIPNAME(0x20, 0x00, DEF_STR( "Cabinet") );
		PORT_DIPSETTING(   0x00, DEF_STR( "Upright") );
		PORT_DIPSETTING(   0x20, DEF_STR( "Cocktail") );
		PORT_SERVICE( 0x40, IP_ACTIVE_LOW );
		PORT_BIT( 0x80, IP_ACTIVE_HIGH, IPT_VBLANK );
	INPUT_PORTS_END(); }}; 
	
	static GfxLayout charlayout = new GfxLayout
	(
		8,8,	/* 8*8 characters */
		1024,	/* 1024 characters */
		3,	/* 3 bits per pixel */
		new int[] { 2*1024*8*8, 1024*8*8, 0 },	/* the bitplanes are separated */
		new int[] { 0, 1, 2, 3, 4, 5, 6, 7 },
		new int[] { 0*8, 1*8, 2*8, 3*8, 4*8, 5*8, 6*8, 7*8 },
		8*8	/* every char takes 8 consecutive bytes */
	);
	
	static GfxLayout tilelayout = new GfxLayout
	(
		16,16,  /* 16*16 tiles */
		512,    /* 512 tiles */
		3,	/* 3 bits per pixel */
		new int[] { 2*512*16*16, 512*16*16, 0 },	/* the bitplanes are separated */
		new int[] { 16*8+0, 16*8+1, 16*8+2, 16*8+3, 16*8+4, 16*8+5, 16*8+6, 16*8+7,
				0, 1, 2, 3, 4, 5, 6, 7 },
		new int[] { 0*8, 1*8, 2*8, 3*8, 4*8, 5*8, 6*8, 7*8,
				8*8, 9*8, 10*8, 11*8, 12*8, 13*8, 14*8, 15*8 },
		32*8	/* every tile takes 16 consecutive bytes */
	);
	
	static GfxLayout matmania_spritelayout = new GfxLayout
	(
		16,16,  /* 16*16 sprites */
		3584,    /* 3584 sprites */
		3,	/* 3 bits per pixel */
		new int[] { 2*3584*16*16, 3584*16*16, 0 },	/* the bitplanes are separated */
		new int[] { 16*8+0, 16*8+1, 16*8+2, 16*8+3, 16*8+4, 16*8+5, 16*8+6, 16*8+7,
				0, 1, 2, 3, 4, 5, 6, 7 },
		new int[] { 0*8, 1*8, 2*8, 3*8, 4*8, 5*8, 6*8, 7*8,
				8*8, 9*8, 10*8, 11*8, 12*8, 13*8, 14*8, 15*8 },
		32*8	/* every sprite takes 16 consecutive bytes */
	);
	
	static GfxLayout maniach_spritelayout = new GfxLayout
	(
		16,16,  /* 16*16 sprites */
		3584,    /* 3584 sprites */
		3,	/* 3 bits per pixel */
		new int[] { 0, 3584*16*16, 2*3584*16*16 },	/* the bitplanes are separated */
		new int[] { 16*8+0, 16*8+1, 16*8+2, 16*8+3, 16*8+4, 16*8+5, 16*8+6, 16*8+7,
				0, 1, 2, 3, 4, 5, 6, 7 },
		new int[] { 0*8, 1*8, 2*8, 3*8, 4*8, 5*8, 6*8, 7*8,
				8*8, 9*8, 10*8, 11*8, 12*8, 13*8, 14*8, 15*8 },
		32*8	/* every sprite takes 16 consecutive bytes */
	);
	
	static GfxLayout maniach_tilelayout = new GfxLayout
	(
		16,16,  /* 16*16 tiles */
		1024,    /* 1024 tiles */
		3,	/* 3 bits per pixel */
		new int[] { 2*1024*16*16, 1024*16*16, 0 },	/* the bitplanes are separated */
		new int[] { 16*8+0, 16*8+1, 16*8+2, 16*8+3, 16*8+4, 16*8+5, 16*8+6, 16*8+7,
				0, 1, 2, 3, 4, 5, 6, 7 },
		new int[] { 0*8, 1*8, 2*8, 3*8, 4*8, 5*8, 6*8, 7*8,
				8*8, 9*8, 10*8, 11*8, 12*8, 13*8, 14*8, 15*8 },
		32*8	/* every tile takes 16 consecutive bytes */
	);
	
	static GfxDecodeInfo matmania_gfxdecodeinfo[] =
	{
		new GfxDecodeInfo( REGION_GFX1, 0, charlayout,              0, 4 ),
		new GfxDecodeInfo( REGION_GFX2, 0, tilelayout,            4*8, 4 ),
		new GfxDecodeInfo( REGION_GFX3, 0, matmania_spritelayout, 8*8, 2 ),
		new GfxDecodeInfo( -1 ) /* end of array */
	};
	
	static GfxDecodeInfo maniach_gfxdecodeinfo[] =
	{
		new GfxDecodeInfo( REGION_GFX1, 0, charlayout,             0, 4 ),
		new GfxDecodeInfo( REGION_GFX2, 0, maniach_tilelayout,   4*8, 4 ),
		new GfxDecodeInfo( REGION_GFX3, 0, maniach_spritelayout, 8*8, 2 ),
		new GfxDecodeInfo( -1 ) /* end of array */
	};
	
	
	static AY8910interface ay8910_interface = new AY8910interface
	(
		2,	/* 2 chips */
		1500000,	/* 1.5 MHz ? */
		new int[]{ 30, 30 },
		new ReadHandlerPtr[]{ null,null },
		new ReadHandlerPtr[]{ null,null },
		new WriteHandlerPtr[]{ null,null },
		new WriteHandlerPtr[]{ null,null }
	);
        
	static DACinterface dac_interface = new DACinterface
	(
		1,
		new int[] { 40 }
	);
	
	
	
	static MachineDriver machine_driver_matmania = new MachineDriver
	(
		/* basic machine hardware */
		new MachineCPU[] {
			new MachineCPU(
				CPU_M6502,
				1500000,	/* 1.5 Mhz ???? */
				matmania_readmem,matmania_writemem,null,null,
				interrupt,1
			),
			new MachineCPU(
				CPU_M6502 | CPU_AUDIO_CPU,
				1200000,	/* 1.2 Mhz ???? */
				sound_readmem,sound_writemem,null,null,
				nmi_interrupt,15	/* ???? */
									/* IRQs are caused by the main CPU */
			),
		},
		60, DEFAULT_REAL_60HZ_VBLANK_DURATION,	/* frames per second, vblank duration */
		10,	/* enough time for the audio CPU to get all commands */
		null,
	
		/* video hardware */
		32*8, 32*8, new rectangle( 0*8, 32*8-1, 1*8, 31*8-1 ),
		matmania_gfxdecodeinfo,
		64+16, 64+16,
		matmania_vh_convert_color_prom,
	
		VIDEO_TYPE_RASTER|VIDEO_MODIFIES_PALETTE,
		null,
		matmania_vh_start,
		matmania_vh_stop,
		matmania_vh_screenrefresh,
	
		/* sound hardware */
		0,0,0,0,
		new MachineSound[] {
			new MachineSound(
				SOUND_AY8910,
				ay8910_interface
			),
			new MachineSound(
				SOUND_DAC,
				dac_interface
			)
		}
	);
	
	
	
	/* handler called by the 3526 emulator when the internal timers cause an IRQ */
	static void irqhandler(int linestate)
	{
		cpu_set_irq_line(1,1,linestate);
		//cpu_cause_interrupt(1,M6809_INT_FIRQ);
	}
	
/*TODO*///	static struct YM3526interface ym3526_interface =
/*TODO*///	{
/*TODO*///		1,			/* 1 chip (no more supported) */
/*TODO*///		3600000,	/* 3.6 MHz ? */
/*TODO*///		{ 60 },		/* (not supported) */
/*TODO*///		{ irqhandler },
/*TODO*///	};
	
	
	static MachineDriver machine_driver_maniach = new MachineDriver
	(
		/* basic machine hardware */
		new MachineCPU[] {
			new MachineCPU(
				CPU_M6502,
				1500000,	/* 1.5 Mhz ???? */
				maniach_readmem,maniach_writemem,null,null,
				interrupt,1
			),
			new MachineCPU(
				CPU_M6809 | CPU_AUDIO_CPU,
				1500000,	/* 1.5 Mhz ???? */
				maniach_sound_readmem,maniach_sound_writemem,null,null,
				ignore_interrupt,0	/* FIRQs are caused by the YM3526 */
									/* IRQs are caused by the main CPU */
			),
			new MachineCPU(
				CPU_M68705,
				500000,	/* .5 Mhz (don't know really how fast, but it doesn't need to even be this fast) */
				mcu_readmem,mcu_writemem,null,null,
				ignore_interrupt,1
			)
		},
		60, DEFAULT_REAL_60HZ_VBLANK_DURATION,	/* frames per second, vblank duration */
		100,	/* 100 CPU slice per frame - high interleaving to sync main and mcu */
		null,
	
		/* video hardware */
		32*8, 32*8, new rectangle( 0*8, 32*8-1, 1*8, 31*8-1 ),
		maniach_gfxdecodeinfo,
		64+16, 64+16,
		matmania_vh_convert_color_prom,
	
		VIDEO_TYPE_RASTER|VIDEO_MODIFIES_PALETTE,
		null,
		matmania_vh_start,
		matmania_vh_stop,
		maniach_vh_screenrefresh,
	
		/* sound hardware */
		0,0,0,0,
		/*new MachineSound[] {
			new MachineSound(
				SOUND_YM3526,
				ym3526_interface
			),
			new MachineSound(
				SOUND_DAC,
				dac_interface
			)
		}*/
                null
	);
	
	/***************************************************************************
	
	  Mat Mania driver
	
	***************************************************************************/
	
	static RomLoadPtr rom_matmania = new RomLoadPtr(){ public void handler(){ 
		ROM_REGION( 0x10000, REGION_CPU1 );/* 64k for code */
		ROM_LOAD( "k0-03",        0x4000, 0x4000, 0x314ab8a4 );
		ROM_LOAD( "k1-03",        0x8000, 0x4000, 0x3b3c3f08 );
		ROM_LOAD( "k2-03",        0xc000, 0x4000, 0x286c0917 );
	
		ROM_REGION( 0x10000, REGION_CPU2 );/* 64k for audio code */
		ROM_LOAD( "k4-0",         0x8000, 0x4000, 0x86dab489 );
		ROM_LOAD( "k5-0",         0xc000, 0x4000, 0x4c41cdba );
	
		ROM_REGION( 0x06000, REGION_GFX1 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "ku-02",        0x00000, 0x2000, 0x613c8698 );/* Character ROMs - 1024 chars, 3 bpp */
		ROM_LOAD( "kv-02",        0x02000, 0x2000, 0x274ce14b );
		ROM_LOAD( "kw-02",        0x04000, 0x2000, 0x7588a9c4 );
	
		ROM_REGION( 0x0c000, REGION_GFX2 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "kt-02",        0x00000, 0x4000, 0x5d817c70 );/* tile set */
		ROM_LOAD( "ks-02",        0x04000, 0x4000, 0x2e9f3ba0 );
		ROM_LOAD( "kr-02",        0x08000, 0x4000, 0xb057d3e3 );
	
		ROM_REGION( 0x54000, REGION_GFX3 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "k6-00",        0x00000, 0x4000, 0x294d0878 );/* sprites */
		ROM_LOAD( "k7-00",        0x04000, 0x4000, 0x0908c2f5 );
		ROM_LOAD( "k8-00",        0x08000, 0x4000, 0xae8341e1 );
		ROM_LOAD( "k9-00",        0x0c000, 0x4000, 0x752ac2c6 );
		ROM_LOAD( "ka-00",        0x10000, 0x4000, 0x46a9cb16 );
		ROM_LOAD( "kb-00",        0x14000, 0x4000, 0xbf016772 );
		ROM_LOAD( "kc-00",        0x18000, 0x4000, 0x8d08bce7 );
		ROM_LOAD( "kd-00",        0x1c000, 0x4000, 0xaf1d6a60 );
		ROM_LOAD( "ke-00",        0x20000, 0x4000, 0x614f19b0 );
		ROM_LOAD( "kf-00",        0x24000, 0x4000, 0xbdf58c18 );
		ROM_LOAD( "kg-00",        0x28000, 0x4000, 0x2189f5cf );
		ROM_LOAD( "kh-00",        0x2c000, 0x4000, 0x6b11ed1f );
		ROM_LOAD( "ki-00",        0x30000, 0x4000, 0xd7ac4ec5 );
		ROM_LOAD( "kj-00",        0x34000, 0x4000, 0x2caee05d );
		ROM_LOAD( "kk-00",        0x38000, 0x4000, 0xeb54f010 );
		ROM_LOAD( "kl-00",        0x3c000, 0x4000, 0xfa4c7e0c );
		ROM_LOAD( "km-00",        0x40000, 0x4000, 0x6d2369b6 );
		ROM_LOAD( "kn-00",        0x44000, 0x4000, 0xc55733e2 );
		ROM_LOAD( "ko-00",        0x48000, 0x4000, 0xed3c3476 );
		ROM_LOAD( "kp-00",        0x4c000, 0x4000, 0x9c84a969 );
		ROM_LOAD( "kq-00",        0x50000, 0x4000, 0xfa2f0003 );
	
		ROM_REGION( 0x0080, REGION_PROMS );
		ROM_LOAD( "matmania.1",   0x0000, 0x0020, 0x1b58f01f );/* char palette red and green components */
		ROM_LOAD( "matmania.5",   0x0020, 0x0020, 0x2029f85f );/* tile palette red and green components */
		ROM_LOAD( "matmania.2",   0x0040, 0x0020, 0xb6ac1fd5 );/* char palette blue component */
		ROM_LOAD( "matmania.16",  0x0060, 0x0020, 0x09325dc2 );/* tile palette blue component */
	ROM_END(); }}; 
	
	static RomLoadPtr rom_excthour = new RomLoadPtr(){ public void handler(){ 
		ROM_REGION( 0x10000, REGION_CPU1 );/* 64k for code */
		ROM_LOAD( "e29",          0x04000, 0x4000, 0xc453e855 );
		ROM_LOAD( "e28",          0x08000, 0x4000, 0x17b63708 );
		ROM_LOAD( "e27",          0x0c000, 0x4000, 0x269ab3bc );
	
		ROM_REGION( 0x10000, REGION_CPU2 );/* 64k for audio code */
		ROM_LOAD( "k4-0",         0x8000, 0x4000, 0x86dab489 );
		ROM_LOAD( "k5-0",         0xc000, 0x4000, 0x4c41cdba );
	
		ROM_REGION( 0x06000, REGION_GFX1 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "e30",          0x00000, 0x2000, 0xb2875329 );/* Character ROMs - 1024 chars, 3 bpp */
		ROM_LOAD( "e31",          0x02000, 0x2000, 0xc9506de8 );
		ROM_LOAD( "e32",          0x04000, 0x2000, 0x00d1635f );
	
		ROM_REGION( 0x0c000, REGION_GFX2 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "e5",           0x00000, 0x4000, 0x0604dc55 );/* tile set */
		ROM_LOAD( "ks-02",        0x04000, 0x4000, 0x2e9f3ba0 );
		ROM_LOAD( "e3",           0x08000, 0x4000, 0xebd273c6 );
	
		ROM_REGION( 0x54000, REGION_GFX3 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "k6-00",        0x00000, 0x4000, 0x294d0878 );/* sprites */
		ROM_LOAD( "k7-00",        0x04000, 0x4000, 0x0908c2f5 );
		ROM_LOAD( "k8-00",        0x08000, 0x4000, 0xae8341e1 );
		ROM_LOAD( "k9-00",        0x0c000, 0x4000, 0x752ac2c6 );
		ROM_LOAD( "ka-00",        0x10000, 0x4000, 0x46a9cb16 );
		ROM_LOAD( "kb-00",        0x14000, 0x4000, 0xbf016772 );
		ROM_LOAD( "kc-00",        0x18000, 0x4000, 0x8d08bce7 );
		ROM_LOAD( "kd-00",        0x1c000, 0x4000, 0xaf1d6a60 );
		ROM_LOAD( "ke-00",        0x20000, 0x4000, 0x614f19b0 );
		ROM_LOAD( "kf-00",        0x24000, 0x4000, 0xbdf58c18 );
		ROM_LOAD( "kg-00",        0x28000, 0x4000, 0x2189f5cf );
		ROM_LOAD( "kh-00",        0x2c000, 0x4000, 0x6b11ed1f );
		ROM_LOAD( "ki-00",        0x30000, 0x4000, 0xd7ac4ec5 );
		ROM_LOAD( "kj-00",        0x34000, 0x4000, 0x2caee05d );
		ROM_LOAD( "kk-00",        0x38000, 0x4000, 0xeb54f010 );
		ROM_LOAD( "kl-00",        0x3c000, 0x4000, 0xfa4c7e0c );
		ROM_LOAD( "km-00",        0x40000, 0x4000, 0x6d2369b6 );
		ROM_LOAD( "kn-00",        0x44000, 0x4000, 0xc55733e2 );
		ROM_LOAD( "ko-00",        0x48000, 0x4000, 0xed3c3476 );
		ROM_LOAD( "kp-00",        0x4c000, 0x4000, 0x9c84a969 );
		ROM_LOAD( "kq-00",        0x50000, 0x4000, 0xfa2f0003 );
	
		ROM_REGION( 0x0080, REGION_PROMS );
		ROM_LOAD( "matmania.1",   0x0000, 0x0020, 0x1b58f01f );/* char palette red and green components */
		ROM_LOAD( "matmania.5",   0x0020, 0x0020, 0x2029f85f );/* tile palette red and green components */
		ROM_LOAD( "matmania.2",   0x0040, 0x0020, 0xb6ac1fd5 );/* char palette blue component */
		ROM_LOAD( "matmania.16",  0x0060, 0x0020, 0x09325dc2 );/* tile palette blue component */
	ROM_END(); }}; 
	
	static RomLoadPtr rom_maniach = new RomLoadPtr(){ public void handler(){ 
		ROM_REGION( 0x10000, REGION_CPU1 );/* 64k for code */
		ROM_LOAD( "mc-mb2.bin",   0x04000, 0x4000, 0xa6da1ba8 );
		ROM_LOAD( "mc-ma2.bin",   0x08000, 0x4000, 0x84583323 );
		ROM_LOAD( "mc-m92.bin",   0x0c000, 0x4000, 0xe209a500 );
	
		ROM_REGION( 0x10000, REGION_CPU2 );/* 64k for audio code */
		ROM_LOAD( "mc-m50.bin",   0x4000, 0x4000, 0xba415d68 );
		ROM_LOAD( "mc-m40.bin",   0x8000, 0x4000, 0x2a217ed0 );
		ROM_LOAD( "mc-m30.bin",   0xc000, 0x4000, 0x95af1723 );
	
		ROM_REGION( 0x0800, REGION_CPU3 );/* 8k for the microcontroller */
		ROM_LOAD( "01",           0x0000, 0x0800, 0x00c7f80c );
	
		ROM_REGION( 0x06000, REGION_GFX1 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "mc-m60.bin",   0x00000, 0x2000, 0x1cdbb117 );/* Character ROMs - 1024 chars, 3 bpp */
		ROM_LOAD( "mc-m70.bin",   0x02000, 0x2000, 0x553f0780 );
		ROM_LOAD( "mc-m80.bin",   0x04000, 0x2000, 0x9392ecb7 );
	
		ROM_REGION( 0x18000, REGION_GFX2 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "mc-m01.bin",   0x00000, 0x8000, 0xda558e4d );/* tile set */
		ROM_LOAD( "mc-m10.bin",   0x08000, 0x8000, 0x619a02f8 );
		ROM_LOAD( "mc-m20.bin",   0x10000, 0x8000, 0xa617c6c1 );
	
		ROM_REGION( 0x54000, REGION_GFX3 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "mc-mc0.bin",   0x00000, 0x4000, 0x133d644f );/* sprites */
		ROM_LOAD( "mc-md0.bin",   0x04000, 0x4000, 0xe387b036 );
		ROM_LOAD( "mc-me0.bin",   0x08000, 0x4000, 0xb36b1283 );
		ROM_LOAD( "mc-mf0.bin",   0x0c000, 0x4000, 0x2584d8a9 );
		ROM_LOAD( "mc-mg0.bin",   0x10000, 0x4000, 0xcf31a714 );
		ROM_LOAD( "mc-mh0.bin",   0x14000, 0x4000, 0x6292d589 );
		ROM_LOAD( "mc-mi0.bin",   0x18000, 0x4000, 0xee2e06e3 );
		ROM_LOAD( "mc-mj0.bin",   0x1c000, 0x4000, 0x7e73895b );
		ROM_LOAD( "mc-mk0.bin",   0x20000, 0x4000, 0x66c8bf75 );
		ROM_LOAD( "mc-ml0.bin",   0x24000, 0x4000, 0x88138a1d );
		ROM_LOAD( "mc-mm0.bin",   0x28000, 0x4000, 0xa1a4260d );
		ROM_LOAD( "mc-mn0.bin",   0x2c000, 0x4000, 0x6bc61b58 );
		ROM_LOAD( "mc-mo0.bin",   0x30000, 0x4000, 0xf96ef600 );
		ROM_LOAD( "mc-mp0.bin",   0x34000, 0x4000, 0x1259618e );
		ROM_LOAD( "mc-mq0.bin",   0x38000, 0x4000, 0x102a1666 );
		ROM_LOAD( "mc-mr0.bin",   0x3c000, 0x4000, 0x1e854453 );
		ROM_LOAD( "mc-ms0.bin",   0x40000, 0x4000, 0x7bc9d878 );
		ROM_LOAD( "mc-mt0.bin",   0x44000, 0x4000, 0x09cea985 );
		ROM_LOAD( "mc-mu0.bin",   0x48000, 0x4000, 0x5421769e );
		ROM_LOAD( "mc-mv0.bin",   0x4c000, 0x4000, 0x36fc3e2d );
		ROM_LOAD( "mc-mw0.bin",   0x50000, 0x4000, 0x135dce4c );
	
		ROM_REGION( 0x0080, REGION_PROMS );
		ROM_LOAD( "prom.2",       0x0000, 0x0020, 0x32db2cf4 );/* char palette red and green components */
		ROM_LOAD( "prom.16",      0x0020, 0x0020, 0x18836d26 );/* tile palette red and green components */
		ROM_LOAD( "prom.3",       0x0040, 0x0020, 0xc7925311 );/* char palette blue component */
		ROM_LOAD( "prom.17",      0x0060, 0x0020, 0x41f51d49 );/* tile palette blue component */
	ROM_END(); }}; 
	
	static RomLoadPtr rom_maniach2 = new RomLoadPtr(){ public void handler(){ 
		ROM_REGION( 0x10000, REGION_CPU1 );/* 64k for code */
		ROM_LOAD( "ic40-mb1",     0x04000, 0x4000, 0xb337a867 );
		ROM_LOAD( "ic41-ma1",     0x08000, 0x4000, 0x85ec8279 );
		ROM_LOAD( "ic42-m91",     0x0c000, 0x4000, 0xa14b86dd );
	
		ROM_REGION( 0x10000, REGION_CPU2 );/* 64k for audio code */
		ROM_LOAD( "mc-m50.bin",   0x4000, 0x4000, 0xba415d68 );
		ROM_LOAD( "mc-m40.bin",   0x8000, 0x4000, 0x2a217ed0 );
		ROM_LOAD( "mc-m30.bin",   0xc000, 0x4000, 0x95af1723 );
	
		ROM_REGION( 0x0800, REGION_CPU3 );/* 8k for the microcontroller */
		ROM_LOAD( "01",           0x0000, 0x0800, 0x00c7f80c );
	
		ROM_REGION( 0x06000, REGION_GFX1 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "mc-m60.bin",   0x00000, 0x2000, 0x1cdbb117 );/* Character ROMs - 1024 chars, 3 bpp */
		ROM_LOAD( "mc-m70.bin",   0x02000, 0x2000, 0x553f0780 );
		ROM_LOAD( "mc-m80.bin",   0x04000, 0x2000, 0x9392ecb7 );
	
		ROM_REGION( 0x18000, REGION_GFX2 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "mc-m01.bin",   0x00000, 0x8000, 0xda558e4d );/* tile set */
		ROM_LOAD( "mc-m10.bin",   0x08000, 0x8000, 0x619a02f8 );
		ROM_LOAD( "mc-m20.bin",   0x10000, 0x8000, 0xa617c6c1 );
	
		ROM_REGION( 0x54000, REGION_GFX3 | REGIONFLAG_DISPOSE );
		ROM_LOAD( "mc-mc0.bin",   0x00000, 0x4000, 0x133d644f );/* sprites */
		ROM_LOAD( "mc-md0.bin",   0x04000, 0x4000, 0xe387b036 );
		ROM_LOAD( "mc-me0.bin",   0x08000, 0x4000, 0xb36b1283 );
		ROM_LOAD( "mc-mf0.bin",   0x0c000, 0x4000, 0x2584d8a9 );
		ROM_LOAD( "mc-mg0.bin",   0x10000, 0x4000, 0xcf31a714 );
		ROM_LOAD( "mc-mh0.bin",   0x14000, 0x4000, 0x6292d589 );
		ROM_LOAD( "mc-mi0.bin",   0x18000, 0x4000, 0xee2e06e3 );
		ROM_LOAD( "mc-mj0.bin",   0x1c000, 0x4000, 0x7e73895b );
		ROM_LOAD( "mc-mk0.bin",   0x20000, 0x4000, 0x66c8bf75 );
		ROM_LOAD( "mc-ml0.bin",   0x24000, 0x4000, 0x88138a1d );
		ROM_LOAD( "mc-mm0.bin",   0x28000, 0x4000, 0xa1a4260d );
		ROM_LOAD( "mc-mn0.bin",   0x2c000, 0x4000, 0x6bc61b58 );
		ROM_LOAD( "mc-mo0.bin",   0x30000, 0x4000, 0xf96ef600 );
		ROM_LOAD( "mc-mp0.bin",   0x34000, 0x4000, 0x1259618e );
		ROM_LOAD( "mc-mq0.bin",   0x38000, 0x4000, 0x102a1666 );
		ROM_LOAD( "mc-mr0.bin",   0x3c000, 0x4000, 0x1e854453 );
		ROM_LOAD( "mc-ms0.bin",   0x40000, 0x4000, 0x7bc9d878 );
		ROM_LOAD( "mc-mt0.bin",   0x44000, 0x4000, 0x09cea985 );
		ROM_LOAD( "mc-mu0.bin",   0x48000, 0x4000, 0x5421769e );
		ROM_LOAD( "mc-mv0.bin",   0x4c000, 0x4000, 0x36fc3e2d );
		ROM_LOAD( "mc-mw0.bin",   0x50000, 0x4000, 0x135dce4c );
	
		ROM_REGION( 0x0080, REGION_PROMS );
		ROM_LOAD( "prom.2",       0x0000, 0x0020, 0x32db2cf4 );/* char palette red and green components */
		ROM_LOAD( "prom.16",      0x0020, 0x0020, 0x18836d26 );/* tile palette red and green components */
		ROM_LOAD( "prom.3",       0x0040, 0x0020, 0xc7925311 );/* char palette blue component */
		ROM_LOAD( "prom.17",      0x0060, 0x0020, 0x41f51d49 );/* tile palette blue component */
	ROM_END(); }}; 
	
	
	
	public static GameDriver driver_matmania	   = new GameDriver("1985"	,"matmania"	,"matmania.java"	,rom_matmania,null	,machine_driver_matmania	,input_ports_matmania	,null	,ROT270	,	"Technos (Taito America license)", "Mat Mania" );
	public static GameDriver driver_excthour	   = new GameDriver("1985"	,"excthour"	,"matmania.java"	,rom_excthour,driver_matmania	,machine_driver_matmania	,input_ports_matmania	,null	,ROT270	,	"Technos (Taito license)", "Exciting Hour" );
	public static GameDriver driver_maniach	   = new GameDriver("1986"	,"maniach"	,"matmania.java"	,rom_maniach,null	,machine_driver_maniach	,input_ports_matmania	,null	,ROT270	,	"Technos (Taito America license)", "Mania Challenge (set 1)" );
	public static GameDriver driver_maniach2	   = new GameDriver("1986"	,"maniach2"	,"matmania.java"	,rom_maniach2,driver_maniach	,machine_driver_maniach	,input_ports_matmania	,null	,ROT270	,	"Technos (Taito America license)", "Mania Challenge (set 2)" );	/* earlier version? */
}
