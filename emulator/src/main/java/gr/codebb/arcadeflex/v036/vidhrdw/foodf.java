/***************************************************************************

  vidhrdw.c

  Functions to emulate the video hardware of the machine.

***************************************************************************/

/*
 * ported to v0.36
 * using automatic conversion tool v0.10
 *
 *
 *
 */ 
package gr.codebb.arcadeflex.v036.vidhrdw;

import static gr.codebb.arcadeflex.common.libc.cstring.*;
import static arcadeflex.v037b7.mame.drawgfxH.*;
import static gr.codebb.arcadeflex.v036.mame.drawgfx.*;
import static arcadeflex.v037b7.generic.fucPtr.*;
import static gr.codebb.arcadeflex.v036.mame.osdependH.*;
import static gr.codebb.arcadeflex.v036.mame.mame.*;
import static gr.codebb.arcadeflex.v036.mame.memoryH.COMBINE_WORD;
import static gr.codebb.arcadeflex.common.PtrLib.*;
import static gr.codebb.arcadeflex.v036.platform.video.*;
import static gr.codebb.arcadeflex.v037b7.mame.palette.*;


public class foodf
{
	
	
	
	/*
	 *		Globals we own
	 */
	
	public static int[] foodf_playfieldram_size=new int[1];
	public static int[] foodf_spriteram_size=new int[1];
	
	public static UBytePtr foodf_playfieldram=new UBytePtr();
	public static UBytePtr foodf_spriteram=new UBytePtr();
	
	
	/*
	 *		Statics
	 */
	
	static char[] playfielddirty;
	
	static osd_bitmap playfieldbitmap;
	
	
	/***************************************************************************
	
	  Convert the color PROMs into a more useable format.
	
	  Food Fight doesn't have a color PROM. It uses 256 bytes of RAM to
	  dynamically create the palette. Each byte defines one
	  color (3-3-2 bits per R-G-B).
	  Graphics use 2 bitplanes.
	
	***************************************************************************/
        static int TOTAL_COLORS(int gfxn) {
            return Machine.gfx[gfxn].total_colors * Machine.gfx[gfxn].color_granularity;
        }
	public static VhConvertColorPromPtr foodf_vh_convert_color_prom = new VhConvertColorPromPtr() { public void handler(char []palette, char []colortable, UBytePtr color_prom) 
	{
		int i;
		//#define TOTAL_COLORS(gfxn) (Machine.gfx[gfxn].total_colors * Machine.gfx[gfxn].color_granularity)
		//#define COLOR(gfxn,offs) (colortable[Machine.drv.gfxdecodeinfo[gfxn].color_codes_start + offs])
                int p_inc = 0;
		for (i = 0;i < Machine.drv.total_colors;i++)
		{
			palette[p_inc++]=(char) (((i & 1) >> 0) * 0xff);
			palette[p_inc++]=(char) (((i & 2) >> 1) * 0xff);
			palette[p_inc++]=(char) (((i & 4) >> 2) * 0xff);
		}
	
		/* characters and sprites use the same palette */
		for (i = 0;i < TOTAL_COLORS(0);i++)
			colortable[Machine.drv.gfxdecodeinfo[0].color_codes_start + i] = (char)i;
	} };
	
	
	/*
	 *   video system start; we also initialize the system memory as well here
	 */
	
	public static VhStartPtr foodf_vh_start = new VhStartPtr() { public int handler() 
	{
		/* allocate dirty buffers */
		if (playfielddirty==null) playfielddirty = new char[foodf_playfieldram_size[0] / 2];
		if (playfielddirty==null)
		{
			foodf_vh_stop.handler();
			return 1;
		}
		memset (playfielddirty, 1, foodf_playfieldram_size[0] / 2);
	
		/* allocate bitmaps */
		if (playfieldbitmap==null) playfieldbitmap = osd_create_bitmap (32*8, 32*8);
		if (playfieldbitmap==null)
		{
			foodf_vh_stop.handler();
			return 1;
		}
	
		return 0;
	} };
	
	
	/*
	 *   video system shutdown; we also bring down the system memory as well here
	 */
	
	public static VhStopPtr foodf_vh_stop = new VhStopPtr() { public void handler() 
	{
		/* free bitmaps */
		if (playfieldbitmap != null) osd_free_bitmap (playfieldbitmap); playfieldbitmap = null;
	
		/* free dirty buffers */
		if (playfielddirty != null) playfielddirty = null;
	} };
	
	
	/*
	 *   playfield RAM read/write handlers
	 */
	
	public static ReadHandlerPtr foodf_playfieldram_r = new ReadHandlerPtr() { public int handler(int offset)
	{
		return foodf_playfieldram.READ_WORD(offset);//READ_WORD (&foodf_playfieldram[offset]);
	} };
	
	public static WriteHandlerPtr foodf_playfieldram_w = new WriteHandlerPtr() { public void handler(int offset, int data)
	{
		int oldword = foodf_playfieldram.READ_WORD(offset);//READ_WORD (&foodf_playfieldram[offset]);
		int newword = COMBINE_WORD (oldword, data);
	
		if (oldword != newword)
		{
			foodf_playfieldram.WRITE_WORD(offset, newword);//WRITE_WORD (&foodf_playfieldram[offset], newword);
			playfielddirty[offset / 2] = 1;
		}
	} };
	
	
	/*
	 *   palette RAM read/write handlers
	 */
	
	public static WriteHandlerPtr foodf_paletteram_w = new WriteHandlerPtr() { public void handler(int offset, int data)
	{
		int oldword = paletteram.READ_WORD(offset);//READ_WORD(&paletteram[offset]);
		int newword = COMBINE_WORD(oldword,data);
		int bit0,bit1,bit2;
		int r,g,b;
	
	
		paletteram.WRITE_WORD(offset, newword);//WRITE_WORD(&paletteram[offset],newword);
	
		/* only the bottom 8 bits are used */
		/* red component */
		bit0 = (newword >> 0) & 0x01;
		bit1 = (newword >> 1) & 0x01;
		bit2 = (newword >> 2) & 0x01;
		r = 0x21 * bit0 + 0x47 * bit1 + 0x97 * bit2;
		/* green component */
		bit0 = (newword >> 3) & 0x01;
		bit1 = (newword >> 4) & 0x01;
		bit2 = (newword >> 5) & 0x01;
		g = 0x21 * bit0 + 0x47 * bit1 + 0x97 * bit2;
		/* blue component */
		bit0 = 0;
		bit1 = (newword >> 6) & 0x01;
		bit2 = (newword >> 7) & 0x01;
		b = 0x21 * bit0 + 0x47 * bit1 + 0x97 * bit2;
	
		palette_change_color(offset / 2,r,g,b);
	} };
	
	
	
	/***************************************************************************
	
	  Draw the game screen in the given osd_bitmap.
	  Do NOT call osd_update_display() from this function, it will be called by
	  the main emulation engine.
	
	***************************************************************************/
	public static VhUpdatePtr foodf_vh_screenrefresh = new VhUpdatePtr() { public void handler(osd_bitmap bitmap,int full_refresh) 
	{
		int offs;
	
		/* recalc the palette if necessary */
		if (palette_recalc ()!=null)
			memset (playfielddirty,1,foodf_playfieldram_size[0] / 2);
	
	
		/* for every character in the Video RAM, check if it has been modified */
		/* since last time and update it accordingly. */
		for (offs = foodf_playfieldram_size[0] - 2; offs >= 0; offs -= 2)
		{
			int data = foodf_playfieldram.READ_WORD(offs);//READ_WORD (&foodf_playfieldram[offs]);
			int color = (data >> 8) & 0x3f;
	
			if (playfielddirty[offs / 2]!=0)
			{
				int pict = (data & 0xff) | ((data >> 7) & 0x100);
				int sx,sy;
	
				playfielddirty[offs / 2] = 0;
	
				sx = ((offs/2) / 32 + 1) % 32;
				sy = (offs/2) % 32;
	
				drawgfx (playfieldbitmap, Machine.gfx[0],
						pict, color,
						0, 0,
						8*sx, 8*sy,
						null,
						TRANSPARENCY_NONE, 0);
			}
		}
		copybitmap (bitmap, playfieldbitmap, 0, 0, 0, 0, Machine.visible_area, TRANSPARENCY_NONE, 0);
	
		/* walk the motion object list. */
		for (offs = 0; offs < foodf_spriteram_size[0]; offs += 4)
		{
			int data1 = foodf_spriteram.READ_WORD(offs);//READ_WORD (&foodf_spriteram[offs]);
			int data2 = foodf_spriteram.READ_WORD(offs+2);//READ_WORD (&foodf_spriteram[offs + 2]);
	
			int pict = data1 & 0xff;
			int color = (data1 >> 8) & 0x1f;
			int xpos = (data2 >> 8) & 0xff;
			int ypos = (0xff - data2 - 16) & 0xff;
			int hflip = (data1 >> 15) & 1;
			int vflip = (data1 >> 14) & 1;
	
			drawgfx(bitmap,Machine.gfx[1],
					pict,
					color,
					hflip,vflip,
					xpos,ypos,
					Machine.visible_area,TRANSPARENCY_PEN,0);
	
			/* draw again with wraparound (needed to get the end of level animation right) */
			drawgfx(bitmap,Machine.gfx[1],
					pict,
					color,
					hflip,vflip,
					xpos-256,ypos,
					Machine.visible_area,TRANSPARENCY_PEN,0);
		}
	} };
}
