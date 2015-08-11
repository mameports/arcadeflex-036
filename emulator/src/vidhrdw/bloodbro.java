package vidhrdw;

import static arcadeflex.libc.*;
import static mame.drawgfxH.*;
import static mame.drawgfx.*;
import static vidhrdw.generic.*;
import static mame.driverH.*;
import static mame.osdependH.*;
import static mame.mame.*;
import static arcadeflex.libc_old.*;
import static arcadeflex.video.*;
import static arcadeflex.ptrlib.*;
import static mame.memoryH.*;
import static mame.palette.*;
import static mame.paletteH.*;

public class bloodbro {

    /**
     * *************************************************************************
     * /*TODO
     *///
/*TODO*///	Video Hardware for Blood Brothers
/*TODO*///
/*TODO*///***************************************************************************/
/*TODO*///
/*TODO*///#include "vidhrdw/generic.h"
/*TODO*///
/*TODO*///#define NUM_SPRITES 128
/*TODO*///
    public static UBytePtr textlayoutram = new UBytePtr();
    static char[] dirtybuffer2;
    static osd_bitmap tmpbitmap2;
    public static UBytePtr bloodbro_videoram2 = new UBytePtr();
    public static UBytePtr bloodbro_scroll = new UBytePtr();
    /*TODO*///static struct sprite_list *sprite_list;
/*TODO*///
/*TODO*///
    public static ReadHandlerPtr bloodbro_background_r = new ReadHandlerPtr() {
        public int handler(int offset) {
            return videoram.READ_WORD(offset);
        }
    };

    public static WriteHandlerPtr bloodbro_background_w = new WriteHandlerPtr() {
        public void handler(int offset, int data) {
            int oldword = videoram.READ_WORD(offset);
            int newword = COMBINE_WORD(oldword, data);
            if (oldword != newword) {
                videoram.WRITE_WORD(offset, newword);
                dirtybuffer[offset / 2] = 1;
            }
        }
    };

    public static ReadHandlerPtr bloodbro_foreground_r = new ReadHandlerPtr() {
        public int handler(int offset) {
            return bloodbro_videoram2.READ_WORD(offset);
        }
    };

    public static WriteHandlerPtr bloodbro_foreground_w = new WriteHandlerPtr() {
        public void handler(int offset, int data) {
            int oldword = bloodbro_videoram2.READ_WORD(offset);
            int newword = COMBINE_WORD(oldword, data);
            if (oldword != newword) {
                bloodbro_videoram2.WRITE_WORD(offset, newword);
                dirtybuffer2[offset / 2] = 1;
            }
        }
    };
    /*TODO*///
/*TODO*////**************************************************************************/
    public static VhStopPtr bloodbro_vh_stop = new VhStopPtr() {
        public void handler() {
            if (tmpbitmap != null) {
                osd_free_bitmap(tmpbitmap);
            }
            if (tmpbitmap2 != null) {
                osd_free_bitmap(tmpbitmap2);
            }
            dirtybuffer2 = null;
            dirtybuffer = null;
        }
    };
    public static VhStartPtr bloodbro_vh_start = new VhStartPtr() {
        public int handler() {
            tmpbitmap = osd_new_bitmap(512, 256, Machine.scrbitmap.depth);
            dirtybuffer = new char[32 * 16];
            tmpbitmap2 = osd_new_bitmap(512, 256, Machine.scrbitmap.depth);
            dirtybuffer2 = new char[32 * 16];
            /*TODO*///	sprite_list = sprite_list_create( NUM_SPRITES, SPRITE_LIST_FRONT_TO_BACK );
/*TODO*///
            if (tmpbitmap != null && tmpbitmap2 != null && dirtybuffer != null && dirtybuffer2 != null) {
                memset(dirtybuffer, 1, 32 * 16);
                memset(dirtybuffer2, 1, 32 * 16);
                /*TODO*///		sprite_list.transparent_pen = 0xf;
/*TODO*///		sprite_list.max_priority = 1;
/*TODO*///		sprite_list.sprite_type = SPRITE_TYPE_STACK;
                return 0;
            }
            bloodbro_vh_stop.handler();
            return 1;
        }
    };

    /**
     * ***********************************************************************
     */
    static void draw_text(osd_bitmap bitmap) {
        rectangle clip = Machine.drv.visible_area;
        UShortPtr source = new UShortPtr(textlayoutram);
        int sx, sy;
        for (sy = 0; sy < 32; sy++) {
            for (sx = 0; sx < 32; sx++) {
                char data = source.read(0);//*source++;
                source.offset += 2;

                drawgfx(bitmap, Machine.gfx[0],
                        data & 0xfff, /* tile number */
                        data >> 12, /* color */
                        0, 0, /* no flip */
                        8 * sx, 8 * sy,
                        clip, TRANSPARENCY_PEN, 0xf);
            }
        }
    }

    static void draw_background(osd_bitmap bitmap) {
        GfxElement gfx = Machine.gfx[1];
        UShortPtr source = new UShortPtr(videoram);
        int offs;
        for (offs = 0; offs < 32 * 16; offs++) {
            if (dirtybuffer[offs] != 0) {
                int sx = 16 * (offs % 32);
                int sy = 16 * (offs / 32);
                char data = source.read(offs);
                dirtybuffer[offs] = 0;

                drawgfx(tmpbitmap, gfx,
                        data & 0xfff, /* tile number */
                        (data & 0xf000) >> 12, /* color */
                        0, 0, /* no flip */
                        sx, sy,
                        null, TRANSPARENCY_NONE, 0);
            }
        }
        {
            int scrollx = -bloodbro_scroll.READ_WORD(0x20);
            /**
             * ? *
             */
            int scrolly = -bloodbro_scroll.READ_WORD(0x22);
            /**
             * ? *
             */

            copyscrollbitmap(bitmap, tmpbitmap,
                    1, new int[]{scrollx}, 1, new int[]{scrolly},
                    Machine.drv.visible_area,
                    TRANSPARENCY_NONE, 0);
        }
    }

    static void draw_foreground(osd_bitmap bitmap) {
        rectangle r = new rectangle();
        GfxElement gfx = Machine.gfx[2];
        UShortPtr source = new UShortPtr(bloodbro_videoram2);
        int offs;
        for (offs = 0; offs < 32 * 16; offs++) {
            if (dirtybuffer2[offs] != 0) {
                int sx = 16 * (offs % 32);
                int sy = 16 * (offs / 32);
                char data = source.read(offs);
                dirtybuffer2[offs] = 0;

                /* Necessary to use TRANSPARENCY_PEN here */
                r.min_x = sx;
                r.max_x = sx + 15;
                r.min_y = sy;
                r.max_y = sy + 15;
                fillbitmap(tmpbitmap2, 0xf, r);
                /**
                 * ***************************************
                 */

                drawgfx(tmpbitmap2, gfx,
                        data & 0xfff, /* tile number */
                        (data & 0xf000) >> 12, /* color */
                        0, 0,
                        sx, sy,
                        null, TRANSPARENCY_PEN, 0xf);
            }
        }
        {
            int scrollx = -bloodbro_scroll.READ_WORD(0x24);
            int scrolly = -bloodbro_scroll.READ_WORD(0x26);

            copyscrollbitmap(bitmap, tmpbitmap2, 1, new int[]{scrollx}, 1, new int[]{scrolly}, Machine.drv.visible_area,
                    TRANSPARENCY_PEN, 0xf);
        }
    }

    /*TODO*////* SPRITE INFO (8 bytes)
/*TODO*///
/*TODO*///   --F???SS SSSSCCCC
/*TODO*///   ---TTTTT TTTTTTTT
/*TODO*///   -------X XXXXXXXX
/*TODO*///   -------- YYYYYYYY */
/*TODO*///static void get_sprite_info( void ){
/*TODO*///	const struct GfxElement *gfx = Machine.gfx[3];
/*TODO*///	const unsigned short *source = (const UINT16 *)spriteram;
/*TODO*///	struct sprite *sprite = sprite_list.sprite;
/*TODO*///	int count = NUM_SPRITES;
/*TODO*///
/*TODO*///	int attributes, flags, number, color, vertical_size, horizontal_size, i;
/*TODO*///	while( count-- ){
/*TODO*///		attributes = source[0];
/*TODO*///		flags = 0;
/*TODO*///		if( (attributes&0x8000)==0 ){
/*TODO*///			flags |= SPRITE_VISIBLE;
/*TODO*///			horizontal_size = 1 + ((attributes>>7)&7);
/*TODO*///			vertical_size = 1 + ((attributes>>4)&7);
/*TODO*///			sprite.priority = (attributes>>11)&1;
/*TODO*///			number = source[1]&0x1fff;
/*TODO*///			sprite.x = source[2]&0x1ff;
/*TODO*///			sprite.y = source[3]&0x1ff;
/*TODO*///
/*TODO*///			/* wraparound - could be handled by Sprite Manager?*/
/*TODO*///			if( sprite.x >= 256) sprite.x -= 512;
/*TODO*///			if( sprite.y >= 256) sprite.y -= 512;
/*TODO*///
/*TODO*///			sprite.total_width = 16*horizontal_size;
/*TODO*///			sprite.total_height = 16*vertical_size;
/*TODO*///
/*TODO*///			sprite.tile_width = 16;
/*TODO*///			sprite.tile_height = 16;
/*TODO*///			sprite.line_offset = 16;
/*TODO*///
/*TODO*///			if( attributes&0x2000 ) flags |= SPRITE_FLIPX;
/*TODO*///			if( attributes&0x4000 ) flags |= SPRITE_FLIPY; /* ? */
/*TODO*///			color = attributes&0xf;
/*TODO*///
/*TODO*///			sprite.pen_data = gfx.gfxdata + number * gfx.char_modulo;
/*TODO*///			sprite.pal_data = &gfx.colortable[gfx.color_granularity * color];
/*TODO*///
/*TODO*///			sprite.pen_usage = 0;
/*TODO*///			for( i=0; i<vertical_size*horizontal_size; i++ ){
/*TODO*///				sprite.pen_usage |= gfx.pen_usage[number++];
/*TODO*///			}
/*TODO*///		}
/*TODO*///		sprite.flags = flags;
/*TODO*///
/*TODO*///		sprite++;
/*TODO*///		source+=4;
/*TODO*///	}
/*TODO*///}
/*TODO*///
    static void bloodbro_mark_used_colors() {
        int offs, i;
        int code, color;
        int[] colmask = new int[0x80];
        int pal_base = Machine.drv.gfxdecodeinfo[0].color_codes_start;

        /* Build the dynamic palette */
        palette_init_used_colors();

        /* Text layer */
        pal_base = Machine.drv.gfxdecodeinfo[0].color_codes_start;
        for (color = 0; color < 16; color++) {
            colmask[color] = 0;
        }
        for (offs = 0; offs < 0x800; offs += 2) {
            code = textlayoutram.READ_WORD(offs);
            color = code >> 12;
            if ((code & 0xfff) == 0xd) {
                continue;
            }
            colmask[color] |= Machine.gfx[0].pen_usage[code & 0xfff];
        }
        for (color = 0; color < 16; color++) {
            for (i = 0; i < 15; i++) {
                if ((colmask[color] & (1 << i)) != 0) {
                    palette_used_colors.write(pal_base + 16 * color + i, PALETTE_COLOR_USED);
                }
            }
        }

        /* Tiles - bottom layer */
        pal_base = Machine.drv.gfxdecodeinfo[1].color_codes_start;
        for (offs = 0; offs < 256; offs++) {
            palette_used_colors.write(pal_base + offs, PALETTE_COLOR_USED);
        }

        /* Tiles - top layer */
        pal_base = Machine.drv.gfxdecodeinfo[2].color_codes_start;
        for (color = 0; color < 16; color++) {
            colmask[color] = 0;
        }
        for (offs = 0x0000; offs < 0x400; offs += 2) {
            code = bloodbro_videoram2.READ_WORD(offs);
            color = code >> 12;
            colmask[color] |= Machine.gfx[2].pen_usage[code & 0xfff];
        }

        for (color = 0; color < 16; color++) {
            for (i = 0; i < 15; i++) {
                if ((colmask[color] & (1 << i)) != 0) {
                    palette_used_colors.write(pal_base + 16 * color + i, PALETTE_COLOR_USED);
                }
            }
            /* kludge */
            palette_used_colors.write(pal_base + 16 * color + 15, PALETTE_COLOR_TRANSPARENT);
            palette_change_color(pal_base + 16 * color + 15, 0, 0, 0);
        }
    }

    public static VhUpdatePtr bloodbro_vh_screenrefresh = new VhUpdatePtr() {
        public void handler(osd_bitmap bitmap, int full_refresh) {
            /*TODO*///	get_sprite_info();

            bloodbro_mark_used_colors();
            /*TODO*///	sprite_update();

            if (palette_recalc() != null) {
                memset(dirtybuffer, 1, 32 * 16);
                memset(dirtybuffer2, 1, 32 * 16);
            }
            draw_background(bitmap);
            /*TODO*///	sprite_draw( sprite_list, 1 );
            draw_foreground(bitmap);
            /*TODO*///	sprite_draw( sprite_list, 0 );
            draw_text(bitmap);
        }
    };
    /*TODO*///
/*TODO*////* SPRITE INFO (8 bytes)
/*TODO*///
/*TODO*///   -------- YYYYYYYY
/*TODO*///   ---TTTTT TTTTTTTT
/*TODO*///   CCCC--F? -?--????  Priority??
/*TODO*///   -------X XXXXXXXX
/*TODO*///*/
/*TODO*///
/*TODO*///static void weststry_draw_sprites( struct osd_bitmap *bitmap, int priority) {
/*TODO*///	int offs;
/*TODO*///	for( offs = 0x800-8; offs > 0; offs-=8 ){
/*TODO*///		int data = READ_WORD( &spriteram[offs+4] );
/*TODO*///		int data0 = READ_WORD( &spriteram[offs+0] );
/*TODO*///		int tile_number = READ_WORD( &spriteram[offs+2] )&0x1fff;
/*TODO*///		int sx = READ_WORD( &spriteram[offs+6] )&0xff;
/*TODO*///		int sy = 0xf0-(data0&0xff);
/*TODO*///		int flipx = (data&0x200)>>9;
/*TODO*///		int datax = (READ_WORD( &spriteram[offs+6] )&0x100);
/*TODO*///		int color = (data&0xf000)>>12;
/*TODO*///
/*TODO*///		/* Remap sprites */
/*TODO*///		switch(tile_number&0x1800) {
/*TODO*///			case 0x0000: break;
/*TODO*///			case 0x0800: tile_number = (tile_number&0x7ff) | 0x1000; break;
/*TODO*///			case 0x1000: tile_number = (tile_number&0x7ff) | 0x800; break;
/*TODO*///			case 0x1800: break;
/*TODO*///		}
/*TODO*///
/*TODO*///		if ((!(data0&0x8000)) && (!datax)) {
/*TODO*///			drawgfx(bitmap,Machine.gfx[3],
/*TODO*///				tile_number, color, flipx,0,
/*TODO*///				sx,sy,
/*TODO*///				&Machine.drv.visible_area,TRANSPARENCY_PEN,0xf);
/*TODO*///		}
/*TODO*///	}
/*TODO*///}

    static void weststry_mark_used_colors() {
        int offs, i;
        int[] colmask = new int[0x80];
        int code, pal_base, color;

        /* Build the dynamic palette */
        palette_init_used_colors();

        /* Text layer */
        pal_base = Machine.drv.gfxdecodeinfo[0].color_codes_start;
        for (color = 0; color < 16; color++) {
            colmask[color] = 0;
        }
        for (offs = 0; offs < 0x800; offs += 2) {
            code = textlayoutram.READ_WORD(offs);
            color = code >> 12;
            if ((code & 0xfff) == 0xd) {
                continue;
            }
            colmask[color] |= Machine.gfx[0].pen_usage[code & 0xfff];
        }
        for (color = 0; color < 16; color++) {
            for (i = 0; i < 15; i++) {
                if ((colmask[color] & (1 << i)) != 0) {
                    palette_used_colors.write(pal_base + 16 * color + i, PALETTE_COLOR_USED);
                }
            }
        }

        /* Tiles - bottom layer */
        pal_base = Machine.drv.gfxdecodeinfo[1].color_codes_start;
        for (offs = 0; offs < 256; offs++) {
            palette_used_colors.write(pal_base + offs, PALETTE_COLOR_USED);
        }

        /* Tiles - top layer */
        pal_base = Machine.drv.gfxdecodeinfo[2].color_codes_start;
        for (color = 0; color < 16; color++) {
            colmask[color] = 0;
        }
        for (offs = 0x0000; offs < 0x400; offs += 2) {
            code = bloodbro_videoram2.READ_WORD(offs);
            color = code >> 12;
            colmask[color] |= Machine.gfx[2].pen_usage[code & 0xfff];
        }
        for (color = 0; color < 16; color++) {
            for (i = 0; i < 15; i++) {
                if ((colmask[color] & (1 << i)) != 0) {
                    palette_used_colors.write(pal_base + 16 * color + i, PALETTE_COLOR_USED);
                }
            }

            /* kludge */
            palette_used_colors.write(pal_base + 16 * color + 15, PALETTE_COLOR_TRANSPARENT);
            palette_change_color(pal_base + 16 * color + 15, 0, 0, 0);
        }

        /* Sprites */
        pal_base = Machine.drv.gfxdecodeinfo[3].color_codes_start;
        for (color = 0; color < 16; color++) {
            colmask[color] = 0;
        }
        for (offs = 0; offs < 0x800; offs += 8) {
            color = spriteram.READ_WORD(offs + 4) >> 12;
            code = spriteram.READ_WORD(offs + 2) & 0x1fff;
            /* Remap code 0x800 <. 0x1000 */
            code = (code & 0x7ff) | ((code & 0x800) << 1) | ((code & 0x1000) >> 1);

            colmask[color] |= Machine.gfx[3].pen_usage[code];
        }
        for (color = 0; color < 16; color++) {
            for (i = 0; i < 15; i++) {
                if ((colmask[color] & (1 << i)) != 0) {
                    palette_used_colors.write(pal_base + 16 * color + i, PALETTE_COLOR_USED);
                }
            }
        }

        if (palette_recalc() != null) {
            memset(dirtybuffer, 1, 32 * 16);
            memset(dirtybuffer2, 1, 32 * 16);
        }
    }
    public static VhUpdatePtr weststry_vh_screenrefresh = new VhUpdatePtr() {
        public void handler(osd_bitmap bitmap, int full_refresh) {
            weststry_mark_used_colors();

            /*TODO*///           draw_background(bitmap);
            //weststry_draw_sprites(bitmap,0);
/*TODO*///            draw_foreground(bitmap);
/*TODO*///            weststry_draw_sprites(bitmap, 1);
/*TODO*///            draw_text(bitmap);
        }
    };
}
