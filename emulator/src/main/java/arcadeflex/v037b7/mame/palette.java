/*
 * Ported to v0.37b7
 */
package arcadeflex.v037b7.mame;

import static gr.codebb.arcadeflex.v036.platform.libc_old.*;
import gr.codebb.arcadeflex.common.PtrLib.UBytePtr;
import static gr.codebb.arcadeflex.common.libc.cstring.memset;
import static gr.codebb.arcadeflex.v036.mame.mame.Machine;
import static gr.codebb.arcadeflex.v036.platform.video.osd_modify_pen;
import static gr.codebb.arcadeflex.v037b7.mame.palette.PALETTE_COLOR_NEEDS_REMAP;
import static gr.codebb.arcadeflex.v037b7.mame.palette.RESERVED_PENS;
import static gr.codebb.arcadeflex.v037b7.mame.palette.TRANSPARENT_PEN;
import static gr.codebb.arcadeflex.v037b7.mame.palette.build_rgb_to_pen;
import static gr.codebb.arcadeflex.v037b7.mame.palette.compress_palette;
import static gr.codebb.arcadeflex.v037b7.mame.palette.game_palette;
import static gr.codebb.arcadeflex.v037b7.mame.palette.just_remapped;
import static gr.codebb.arcadeflex.v037b7.mame.palette.new_palette;
import static gr.codebb.arcadeflex.v037b7.mame.palette.old_used_colors;
import static gr.codebb.arcadeflex.v037b7.mame.palette.palette_dirty;
import static gr.codebb.arcadeflex.v037b7.mame.palette.palette_map;
import static gr.codebb.arcadeflex.v037b7.mame.palette.palette_used_colors;
import static gr.codebb.arcadeflex.v037b7.mame.palette.pen_usage_count;
import static gr.codebb.arcadeflex.v037b7.mame.palette.rgb6_to_pen;
import static gr.codebb.arcadeflex.v037b7.mame.palette.shrinked_palette;
import static gr.codebb.arcadeflex.v037b7.mame.palette.shrinked_pens;
import static gr.codebb.arcadeflex.v037b7.mame.paletteH.DYNAMIC_MAX_PENS;
import static gr.codebb.arcadeflex.v037b7.mame.paletteH.PALETTE_COLOR_CACHED;
import static gr.codebb.arcadeflex.v037b7.mame.paletteH.PALETTE_COLOR_TRANSPARENT_FLAG;
import static gr.codebb.arcadeflex.v037b7.mame.paletteH.PALETTE_COLOR_VISIBLE;

public class palette {

    public static FILE palettelog = null;//fopen("palette.log", "wa");  //for debug purposes
/*TODO*///#define VERBOSE 0
/*TODO*///
/*TODO*///
/*TODO*///static UINT8 *game_palette;	/* RGB palette as set by the driver. */
/*TODO*///static UINT8 *new_palette;	/* changes to the palette are stored here before */
/*TODO*///							/* being moved to game_palette by palette_recalc() */
/*TODO*///static UINT8 *palette_dirty;
/*TODO*////* arrays which keep track of colors actually used, to help in the palette shrinking. */
/*TODO*///UINT8 *palette_used_colors;
/*TODO*///static UINT8 *old_used_colors;
/*TODO*///static int *pen_visiblecount,*pen_cachedcount;
/*TODO*///static UINT8 *just_remapped;	/* colors which have been remapped in this frame, */
/*TODO*///								/* returned by palette_recalc() */
/*TODO*///
/*TODO*///static int use_16bit;
/*TODO*///#define NO_16BIT			0
/*TODO*///#define STATIC_16BIT		1
/*TODO*///#define PALETTIZED_16BIT	2
/*TODO*///
/*TODO*///static int total_shrinked_pens;
/*TODO*///UINT16 *shrinked_pens;
/*TODO*///static UINT8 *shrinked_palette;
/*TODO*///static UINT16 *palette_map;	/* map indexes from game_palette to shrinked_palette */
/*TODO*///static UINT16 pen_usage_count[DYNAMIC_MAX_PENS];
/*TODO*///
/*TODO*///UINT16 palette_transparent_pen;
/*TODO*///int palette_transparent_color;
/*TODO*///
/*TODO*///
/*TODO*///#define BLACK_PEN		0
/*TODO*///#define TRANSPARENT_PEN	1
/*TODO*///#define RESERVED_PENS	2
/*TODO*///
/*TODO*///#define PALETTE_COLOR_NEEDS_REMAP 0x80
/*TODO*///
/*TODO*////* helper macro for 16-bit mode */
/*TODO*///#define rgbpenindex(r,g,b) ((Machine->scrbitmap->depth==16) ? ((((r)>>3)<<10)+(((g)>>3)<<5)+((b)>>3)) : ((((r)>>5)<<5)+(((g)>>5)<<2)+((b)>>6)))
/*TODO*///
/*TODO*///
/*TODO*///UINT16 *palette_shadow_table;
/*TODO*///
/*TODO*///void artwork_remap(void);
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*///int palette_start(void)
/*TODO*///{
/*TODO*///	int i,num;
/*TODO*///
/*TODO*///
/*TODO*///	game_palette = malloc(3 * Machine->drv->total_colors * sizeof(UINT8));
/*TODO*///	palette_map = malloc(Machine->drv->total_colors * sizeof(UINT16));
/*TODO*///	if (Machine->drv->color_table_len)
/*TODO*///	{
/*TODO*///		Machine->game_colortable = malloc(Machine->drv->color_table_len * sizeof(UINT16));
/*TODO*///		Machine->remapped_colortable = malloc(Machine->drv->color_table_len * sizeof(UINT16));
/*TODO*///	}
/*TODO*///	else Machine->game_colortable = Machine->remapped_colortable = 0;
/*TODO*///	Machine->debug_remapped_colortable = malloc(2*DEBUGGER_TOTAL_COLORS*DEBUGGER_TOTAL_COLORS * sizeof(UINT16));
/*TODO*///
/*TODO*///	if (Machine->color_depth == 16 || (Machine->gamedrv->flags & GAME_REQUIRES_16BIT))
/*TODO*///	{
/*TODO*///		if (Machine->color_depth == 8 || Machine->drv->total_colors > 65532)
/*TODO*///			use_16bit = STATIC_16BIT;
/*TODO*///		else
/*TODO*///			use_16bit = PALETTIZED_16BIT;
/*TODO*///	}
/*TODO*///	else
/*TODO*///		use_16bit = NO_16BIT;
/*TODO*///
/*TODO*///	switch (use_16bit)
/*TODO*///	{
/*TODO*///		case NO_16BIT:
/*TODO*///			if (Machine->drv->video_attributes & VIDEO_MODIFIES_PALETTE)
/*TODO*///				total_shrinked_pens = DYNAMIC_MAX_PENS;
/*TODO*///			else
/*TODO*///				total_shrinked_pens = STATIC_MAX_PENS;
/*TODO*///			break;
/*TODO*///		case STATIC_16BIT:
/*TODO*///			total_shrinked_pens = 32768;
/*TODO*///			break;
/*TODO*///		case PALETTIZED_16BIT:
/*TODO*///			total_shrinked_pens = Machine->drv->total_colors + RESERVED_PENS;
/*TODO*///			break;
/*TODO*///	}
/*TODO*///
/*TODO*///	shrinked_pens = malloc(total_shrinked_pens * sizeof(short));
/*TODO*///	shrinked_palette = malloc(3 * total_shrinked_pens * sizeof(UINT8));
/*TODO*///
/*TODO*///	Machine->pens = malloc(Machine->drv->total_colors * sizeof(short));
/*TODO*///	Machine->debug_pens = malloc(DEBUGGER_TOTAL_COLORS * sizeof(short));
/*TODO*///
/*TODO*///	if ((Machine->drv->video_attributes & VIDEO_MODIFIES_PALETTE))
/*TODO*///	{
/*TODO*///		/* if the palette changes dynamically, */
/*TODO*///		/* we'll need the usage arrays to help in shrinking. */
/*TODO*///		palette_used_colors = malloc((1+1+1+3+1) * Machine->drv->total_colors * sizeof(UINT8));
/*TODO*///		pen_visiblecount = malloc(2 * Machine->drv->total_colors * sizeof(int));
/*TODO*///
/*TODO*///		if (palette_used_colors == 0 || pen_visiblecount == 0)
/*TODO*///		{
/*TODO*///			palette_stop();
/*TODO*///			return 1;
/*TODO*///		}
/*TODO*///
/*TODO*///		old_used_colors = palette_used_colors + Machine->drv->total_colors * sizeof(UINT8);
/*TODO*///		just_remapped = old_used_colors + Machine->drv->total_colors * sizeof(UINT8);
/*TODO*///		new_palette = just_remapped + Machine->drv->total_colors * sizeof(UINT8);
/*TODO*///		palette_dirty = new_palette + 3*Machine->drv->total_colors * sizeof(UINT8);
/*TODO*///		memset(palette_used_colors,PALETTE_COLOR_USED,Machine->drv->total_colors * sizeof(UINT8));
/*TODO*///		memset(old_used_colors,PALETTE_COLOR_UNUSED,Machine->drv->total_colors * sizeof(UINT8));
/*TODO*///		memset(palette_dirty,0,Machine->drv->total_colors * sizeof(UINT8));
/*TODO*///		pen_cachedcount = pen_visiblecount + Machine->drv->total_colors;
/*TODO*///		memset(pen_visiblecount,0,Machine->drv->total_colors * sizeof(int));
/*TODO*///		memset(pen_cachedcount,0,Machine->drv->total_colors * sizeof(int));
/*TODO*///	}
/*TODO*///	else palette_used_colors = old_used_colors = just_remapped = new_palette = palette_dirty = 0;
/*TODO*///
/*TODO*///	if (Machine->color_depth == 8) num = 256;
/*TODO*///	else num = 65536;
/*TODO*///	palette_shadow_table = malloc(num * sizeof(UINT16));
/*TODO*///	if (palette_shadow_table == 0)
/*TODO*///	{
/*TODO*///		palette_stop();
/*TODO*///		return 1;
/*TODO*///	}
/*TODO*///	for (i = 0;i < num;i++)
/*TODO*///		palette_shadow_table[i] = i;
/*TODO*///
/*TODO*///	if ((Machine->drv->color_table_len && (Machine->game_colortable == 0 || Machine->remapped_colortable == 0))
/*TODO*///			|| game_palette == 0 ||	palette_map == 0
/*TODO*///			|| shrinked_pens == 0 || shrinked_palette == 0 || Machine->pens == 0
/*TODO*///			|| Machine->debug_pens == 0 || Machine->debug_remapped_colortable == 0)
/*TODO*///	{
/*TODO*///		palette_stop();
/*TODO*///		return 1;
/*TODO*///	}
/*TODO*///
/*TODO*///	return 0;
/*TODO*///}
/*TODO*///
/*TODO*///void palette_stop(void)
/*TODO*///{
/*TODO*///	free(palette_used_colors);
/*TODO*///	palette_used_colors = old_used_colors = just_remapped = new_palette = palette_dirty = 0;
/*TODO*///	free(pen_visiblecount);
/*TODO*///	pen_visiblecount = 0;
/*TODO*///	free(game_palette);
/*TODO*///	game_palette = 0;
/*TODO*///	free(palette_map);
/*TODO*///	palette_map = 0;
/*TODO*///	free(Machine->game_colortable);
/*TODO*///	Machine->game_colortable = 0;
/*TODO*///	free(Machine->remapped_colortable);
/*TODO*///	Machine->remapped_colortable = 0;
/*TODO*///	free(Machine->debug_remapped_colortable);
/*TODO*///	Machine->debug_remapped_colortable = 0;
/*TODO*///	free(shrinked_pens);
/*TODO*///	shrinked_pens = 0;
/*TODO*///	free(shrinked_palette);
/*TODO*///	shrinked_palette = 0;
/*TODO*///	free(Machine->pens);
/*TODO*///	Machine->pens = 0;
/*TODO*///	free(Machine->debug_pens);
/*TODO*///	Machine->debug_pens = 0;
/*TODO*///	free(palette_shadow_table);
/*TODO*///	palette_shadow_table = 0;
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*///int palette_init(void)
/*TODO*///{
/*TODO*///	int i;
/*TODO*///	UINT8 *debug_palette;
/*TODO*///	UINT16 *debug_pens;
/*TODO*///
/*TODO*///#ifdef MAME_DEBUG
/*TODO*///	if (mame_debug)
/*TODO*///	{
/*TODO*///		debug_palette = debugger_palette;
/*TODO*///		debug_pens = Machine->debug_pens;
/*TODO*///	}
/*TODO*///	else
/*TODO*///#endif
/*TODO*///	{
/*TODO*///		debug_palette = NULL;
/*TODO*///		debug_pens = NULL;
/*TODO*///	}
/*TODO*///
/*TODO*///	/* We initialize the palette and colortable to some default values so that */
/*TODO*///	/* drivers which dynamically change the palette don't need a vh_init_palette() */
/*TODO*///	/* function (provided the default color table fits their needs). */
/*TODO*///
/*TODO*///	for (i = 0;i < Machine->drv->total_colors;i++)
/*TODO*///	{
/*TODO*///		game_palette[3*i + 0] = ((i & 1) >> 0) * 0xff;
/*TODO*///		game_palette[3*i + 1] = ((i & 2) >> 1) * 0xff;
/*TODO*///		game_palette[3*i + 2] = ((i & 4) >> 2) * 0xff;
/*TODO*///	}
/*TODO*///
/*TODO*///	/* Preload the colortable with a default setting, following the same */
/*TODO*///	/* order of the palette. The driver can overwrite this in */
/*TODO*///	/* vh_init_palette() */
/*TODO*///	for (i = 0;i < Machine->drv->color_table_len;i++)
/*TODO*///		Machine->game_colortable[i] = i % Machine->drv->total_colors;
/*TODO*///
/*TODO*///	/* by default we use -1 to identify the transparent color, the driver */
/*TODO*///	/* can modify this. */
/*TODO*///	palette_transparent_color = -1;
/*TODO*///
/*TODO*///	/* now the driver can modify the default values if it wants to. */
/*TODO*///	if (Machine->drv->vh_init_palette)
/*TODO*///		(*Machine->drv->vh_init_palette)(game_palette,Machine->game_colortable,memory_region(REGION_PROMS));
/*TODO*///
/*TODO*///
/*TODO*///	switch (use_16bit)
/*TODO*///	{
/*TODO*///		case NO_16BIT:
/*TODO*///		{
/*TODO*///			/* initialize shrinked palette to all black */
/*TODO*///			for (i = 0;i < total_shrinked_pens;i++)
/*TODO*///			{
/*TODO*///				shrinked_palette[3*i + 0] =
/*TODO*///				shrinked_palette[3*i + 1] =
/*TODO*///				shrinked_palette[3*i + 2] = 0;
/*TODO*///			}
/*TODO*///
/*TODO*///			if (Machine->drv->video_attributes & VIDEO_MODIFIES_PALETTE)
/*TODO*///			{
/*TODO*///				/* initialize pen usage counters */
/*TODO*///				for (i = 0;i < DYNAMIC_MAX_PENS;i++)
/*TODO*///					pen_usage_count[i] = 0;
/*TODO*///
/*TODO*///				/* allocate two fixed pens at the beginning: */
/*TODO*///				/* transparent black */
/*TODO*///				pen_usage_count[TRANSPARENT_PEN] = 1;	/* so the pen will not be reused */
/*TODO*///
/*TODO*///				/* non transparent black */
/*TODO*///				pen_usage_count[BLACK_PEN] = 1;
/*TODO*///
/*TODO*///				/* create some defaults associations of game colors to shrinked pens. */
/*TODO*///				/* They will be dynamically modified at run time. */
/*TODO*///				for (i = 0;i < Machine->drv->total_colors;i++)
/*TODO*///					palette_map[i] = (i & 7) + 8;
/*TODO*///
/*TODO*///				if (osd_allocate_colors(total_shrinked_pens,shrinked_palette,shrinked_pens,1,debug_palette,debug_pens))
/*TODO*///					return 1;
/*TODO*///			}
/*TODO*///			else
/*TODO*///			{
/*TODO*///				int j,used;
/*TODO*///
/*TODO*///
/*TODO*///logerror("shrinking %d colors palette...\n",Machine->drv->total_colors);
/*TODO*///
/*TODO*///				/* shrink palette to fit */
/*TODO*///				used = 0;
/*TODO*///
/*TODO*///				for (i = 0;i < Machine->drv->total_colors;i++)
/*TODO*///				{
/*TODO*///					for (j = 0;j < used;j++)
/*TODO*///					{
/*TODO*///						if (	shrinked_palette[3*j + 0] == game_palette[3*i + 0] &&
/*TODO*///								shrinked_palette[3*j + 1] == game_palette[3*i + 1] &&
/*TODO*///								shrinked_palette[3*j + 2] == game_palette[3*i + 2])
/*TODO*///							break;
/*TODO*///					}
/*TODO*///
/*TODO*///					palette_map[i] = j;
/*TODO*///
/*TODO*///					if (j == used)
/*TODO*///					{
/*TODO*///						used++;
/*TODO*///						if (used > total_shrinked_pens)
/*TODO*///						{
/*TODO*///							used = total_shrinked_pens;
/*TODO*///							palette_map[i] = total_shrinked_pens-1;
/*TODO*///							usrintf_showmessage("cannot shrink static palette");
/*TODO*///logerror("error: ran out of free pens to shrink the palette.\n");
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							shrinked_palette[3*j + 0] = game_palette[3*i + 0];
/*TODO*///							shrinked_palette[3*j + 1] = game_palette[3*i + 1];
/*TODO*///							shrinked_palette[3*j + 2] = game_palette[3*i + 2];
/*TODO*///						}
/*TODO*///					}
/*TODO*///				}
/*TODO*///
/*TODO*///logerror("shrinked palette uses %d colors\n",used);
/*TODO*///
/*TODO*///				if (osd_allocate_colors(used,shrinked_palette,shrinked_pens,0,debug_palette,debug_pens))
/*TODO*///					return 1;
/*TODO*///			}
/*TODO*///
/*TODO*///
/*TODO*///			for (i = 0;i < Machine->drv->total_colors;i++)
/*TODO*///				Machine->pens[i] = shrinked_pens[palette_map[i]];
/*TODO*///
/*TODO*///			palette_transparent_pen = shrinked_pens[TRANSPARENT_PEN];	/* for dynamic palette games */
/*TODO*///		}
/*TODO*///		break;
/*TODO*///
/*TODO*///		case STATIC_16BIT:
/*TODO*///		{
/*TODO*///			UINT8 *p = shrinked_palette;
/*TODO*///			int r,g,b;
/*TODO*///
/*TODO*///			if (Machine->scrbitmap->depth == 16)
/*TODO*///			{
/*TODO*///				for (r = 0;r < 32;r++)
/*TODO*///				{
/*TODO*///					for (g = 0;g < 32;g++)
/*TODO*///					{
/*TODO*///						for (b = 0;b < 32;b++)
/*TODO*///						{
/*TODO*///							*p++ = (r << 3) | (r >> 2);
/*TODO*///							*p++ = (g << 3) | (g >> 2);
/*TODO*///							*p++ = (b << 3) | (b >> 2);
/*TODO*///						}
/*TODO*///					}
/*TODO*///				}
/*TODO*///
/*TODO*///				if (osd_allocate_colors(32768,shrinked_palette,shrinked_pens,0,debug_palette,debug_pens))
/*TODO*///					return 1;
/*TODO*///			}
/*TODO*///			else
/*TODO*///			{
/*TODO*///				for (r = 0;r < 8;r++)
/*TODO*///				{
/*TODO*///					for (g = 0;g < 8;g++)
/*TODO*///					{
/*TODO*///						for (b = 0;b < 4;b++)
/*TODO*///						{
/*TODO*///							*p++ = (r << 5) | (r << 2) | (r >> 1);
/*TODO*///							*p++ = (g << 5) | (g << 2) | (g >> 1);
/*TODO*///							*p++ = (b << 6) | (b << 4) | (b << 2) | b;
/*TODO*///						}
/*TODO*///					}
/*TODO*///				}
/*TODO*///
/*TODO*///				if (osd_allocate_colors(256,shrinked_palette,shrinked_pens,0,debug_palette,debug_pens))
/*TODO*///					return 1;
/*TODO*///			}
/*TODO*///
/*TODO*///			for (i = 0;i < Machine->drv->total_colors;i++)
/*TODO*///			{
/*TODO*///				r = game_palette[3*i + 0];
/*TODO*///				g = game_palette[3*i + 1];
/*TODO*///				b = game_palette[3*i + 2];
/*TODO*///
/*TODO*///				Machine->pens[i] = shrinked_pens[rgbpenindex(r,g,b)];
/*TODO*///			}
/*TODO*///
/*TODO*///			palette_transparent_pen = shrinked_pens[0];	/* we are forced to use black for the transparent pen */
/*TODO*///		}
/*TODO*///		break;
/*TODO*///
/*TODO*///		case PALETTIZED_16BIT:
/*TODO*///		{
/*TODO*///			for (i = 0;i < RESERVED_PENS;i++)
/*TODO*///			{
/*TODO*///				shrinked_palette[3*i + 0] =
/*TODO*///				shrinked_palette[3*i + 1] =
/*TODO*///				shrinked_palette[3*i + 2] = 0;
/*TODO*///			}
/*TODO*///
/*TODO*///			for (i = 0;i < Machine->drv->total_colors;i++)
/*TODO*///			{
/*TODO*///				shrinked_palette[3*(i+RESERVED_PENS) + 0] = game_palette[3*i + 0];
/*TODO*///				shrinked_palette[3*(i+RESERVED_PENS) + 1] = game_palette[3*i + 1];
/*TODO*///				shrinked_palette[3*(i+RESERVED_PENS) + 2] = game_palette[3*i + 2];
/*TODO*///			}
/*TODO*///
/*TODO*///			if (osd_allocate_colors(total_shrinked_pens,shrinked_palette,shrinked_pens,(Machine->drv->video_attributes & VIDEO_MODIFIES_PALETTE),debug_palette,debug_pens))
/*TODO*///				return 1;
/*TODO*///
/*TODO*///			for (i = 0;i < Machine->drv->total_colors;i++)
/*TODO*///				Machine->pens[i] = shrinked_pens[i + RESERVED_PENS];
/*TODO*///
/*TODO*///			palette_transparent_pen = shrinked_pens[TRANSPARENT_PEN];	/* for dynamic palette games */
/*TODO*///		}
/*TODO*///		break;
/*TODO*///	}
/*TODO*///
/*TODO*///	for (i = 0;i < Machine->drv->color_table_len;i++)
/*TODO*///	{
/*TODO*///		int color = Machine->game_colortable[i];
/*TODO*///
/*TODO*///		/* check for invalid colors set by Machine->drv->vh_init_palette */
/*TODO*///		if (color < Machine->drv->total_colors)
/*TODO*///			Machine->remapped_colortable[i] = Machine->pens[color];
/*TODO*///		else
/*TODO*///			usrintf_showmessage("colortable[%d] (=%d) out of range (total_colors = %d)",
/*TODO*///					i,color,Machine->drv->total_colors);
/*TODO*///	}
/*TODO*///
/*TODO*///	for (i = 0;i < DEBUGGER_TOTAL_COLORS*DEBUGGER_TOTAL_COLORS;i++)
/*TODO*///	{
/*TODO*///		Machine->debug_remapped_colortable[2*i+0] = Machine->debug_pens[i / DEBUGGER_TOTAL_COLORS];
/*TODO*///		Machine->debug_remapped_colortable[2*i+1] = Machine->debug_pens[i % DEBUGGER_TOTAL_COLORS];
/*TODO*///	}
/*TODO*///
/*TODO*///	return 0;
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*///INLINE void palette_change_color_16_static(int color,UINT8 red,UINT8 green,UINT8 blue)
/*TODO*///{
/*TODO*///	if (color == palette_transparent_color)
/*TODO*///	{
/*TODO*///		int i;
/*TODO*///
/*TODO*///
/*TODO*///		palette_transparent_pen = shrinked_pens[rgbpenindex(red,green,blue)];
/*TODO*///
/*TODO*///		if (color == -1) return;	/* by default, palette_transparent_color is -1 */
/*TODO*///
/*TODO*///		for (i = 0;i < Machine->drv->total_colors;i++)
/*TODO*///		{
/*TODO*///			if ((old_used_colors[i] & (PALETTE_COLOR_VISIBLE | PALETTE_COLOR_TRANSPARENT_FLAG))
/*TODO*///					== (PALETTE_COLOR_VISIBLE | PALETTE_COLOR_TRANSPARENT_FLAG))
/*TODO*///				old_used_colors[i] |= PALETTE_COLOR_NEEDS_REMAP;
/*TODO*///		}
/*TODO*///	}
/*TODO*///
/*TODO*///	if (	game_palette[3*color + 0] == red &&
/*TODO*///			game_palette[3*color + 1] == green &&
/*TODO*///			game_palette[3*color + 2] == blue)
/*TODO*///		return;
/*TODO*///
/*TODO*///	game_palette[3*color + 0] = red;
/*TODO*///	game_palette[3*color + 1] = green;
/*TODO*///	game_palette[3*color + 2] = blue;
/*TODO*///
/*TODO*///	if (old_used_colors[color] & PALETTE_COLOR_VISIBLE)
/*TODO*///		/* we'll have to reassign the color in palette_recalc() */
/*TODO*///		old_used_colors[color] |= PALETTE_COLOR_NEEDS_REMAP;
/*TODO*///}
/*TODO*///
/*TODO*///INLINE void palette_change_color_16_palettized(int color,UINT8 red,UINT8 green,UINT8 blue)
/*TODO*///{
/*TODO*///	if (color == palette_transparent_color)
/*TODO*///	{
/*TODO*///		osd_modify_pen(palette_transparent_pen,red,green,blue);
/*TODO*///
/*TODO*///		if (color == -1) return;	/* by default, palette_transparent_color is -1 */
/*TODO*///	}
/*TODO*///
/*TODO*///	if (	game_palette[3*color + 0] == red &&
/*TODO*///			game_palette[3*color + 1] == green &&
/*TODO*///			game_palette[3*color + 2] == blue)
/*TODO*///		return;
/*TODO*///
/*TODO*///	/* Machine->pens[color] might have been remapped to transparent_pen, so I */
/*TODO*///	/* use shrinked_pens[] directly */
/*TODO*///	osd_modify_pen(shrinked_pens[color + RESERVED_PENS],red,green,blue);
/*TODO*///	game_palette[3*color + 0] = red;
/*TODO*///	game_palette[3*color + 1] = green;
/*TODO*///	game_palette[3*color + 2] = blue;
/*TODO*///}
/*TODO*///
/*TODO*///INLINE void palette_change_color_8(int color,UINT8 red,UINT8 green,UINT8 blue)
/*TODO*///{
/*TODO*///	int pen;
/*TODO*///
/*TODO*///	if (color == palette_transparent_color)
/*TODO*///	{
/*TODO*///		osd_modify_pen(palette_transparent_pen,red,green,blue);
/*TODO*///
/*TODO*///		if (color == -1) return;	/* by default, palette_transparent_color is -1 */
/*TODO*///	}
/*TODO*///
/*TODO*///	if (	game_palette[3*color + 0] == red &&
/*TODO*///			game_palette[3*color + 1] == green &&
/*TODO*///			game_palette[3*color + 2] == blue)
/*TODO*///	{
/*TODO*///		palette_dirty[color] = 0;
/*TODO*///		return;
/*TODO*///	}
/*TODO*///
/*TODO*///	pen = palette_map[color];
/*TODO*///
/*TODO*///	/* if the color was used, mark it as dirty, we'll change it in palette_recalc() */
/*TODO*///	if (old_used_colors[color] & PALETTE_COLOR_VISIBLE)
/*TODO*///	{
/*TODO*///		new_palette[3*color + 0] = red;
/*TODO*///		new_palette[3*color + 1] = green;
/*TODO*///		new_palette[3*color + 2] = blue;
/*TODO*///		palette_dirty[color] = 1;
/*TODO*///	}
/*TODO*///	/* otherwise, just update the array */
/*TODO*///	else
/*TODO*///	{
/*TODO*///		game_palette[3*color + 0] = red;
/*TODO*///		game_palette[3*color + 1] = green;
/*TODO*///		game_palette[3*color + 2] = blue;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///void palette_change_color(int color,UINT8 red,UINT8 green,UINT8 blue)
/*TODO*///{
/*TODO*///	if ((Machine->drv->video_attributes & VIDEO_MODIFIES_PALETTE) == 0)
/*TODO*///	{
/*TODO*///logerror("Error: palette_change_color() called, but VIDEO_MODIFIES_PALETTE not set.\n");
/*TODO*///		return;
/*TODO*///	}
/*TODO*///
/*TODO*///	if (color >= Machine->drv->total_colors)
/*TODO*///	{
/*TODO*///logerror("error: palette_change_color() called with color %d, but only %d allocated.\n",color,Machine->drv->total_colors);
/*TODO*///		return;
/*TODO*///	}
/*TODO*///
/*TODO*///	switch (use_16bit)
/*TODO*///	{
/*TODO*///		case NO_16BIT:
/*TODO*///			palette_change_color_8(color,red,green,blue);
/*TODO*///			break;
/*TODO*///		case STATIC_16BIT:
/*TODO*///			palette_change_color_16_static(color,red,green,blue);
/*TODO*///			break;
/*TODO*///		case PALETTIZED_16BIT:
/*TODO*///			palette_change_color_16_palettized(color,red,green,blue);
/*TODO*///			break;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*///void palette_increase_usage_count(int table_offset,unsigned int usage_mask,int color_flags)
/*TODO*///{
/*TODO*///	/* if we are not dynamically reducing the palette, return immediately. */
/*TODO*///	if (palette_used_colors == 0) return;
/*TODO*///
/*TODO*///	while (usage_mask)
/*TODO*///	{
/*TODO*///		if (usage_mask & 1)
/*TODO*///		{
/*TODO*///			if (color_flags & PALETTE_COLOR_VISIBLE)
/*TODO*///				pen_visiblecount[Machine->game_colortable[table_offset]]++;
/*TODO*///			if (color_flags & PALETTE_COLOR_CACHED)
/*TODO*///				pen_cachedcount[Machine->game_colortable[table_offset]]++;
/*TODO*///		}
/*TODO*///		table_offset++;
/*TODO*///		usage_mask >>= 1;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///void palette_decrease_usage_count(int table_offset,unsigned int usage_mask,int color_flags)
/*TODO*///{
/*TODO*///	/* if we are not dynamically reducing the palette, return immediately. */
/*TODO*///	if (palette_used_colors == 0) return;
/*TODO*///
/*TODO*///	while (usage_mask)
/*TODO*///	{
/*TODO*///		if (usage_mask & 1)
/*TODO*///		{
/*TODO*///			if (color_flags & PALETTE_COLOR_VISIBLE)
/*TODO*///				pen_visiblecount[Machine->game_colortable[table_offset]]--;
/*TODO*///			if (color_flags & PALETTE_COLOR_CACHED)
/*TODO*///				pen_cachedcount[Machine->game_colortable[table_offset]]--;
/*TODO*///		}
/*TODO*///		table_offset++;
/*TODO*///		usage_mask >>= 1;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///void palette_increase_usage_countx(int table_offset,int num_pens,const UINT8 *pen_data,int color_flags)
/*TODO*///{
/*TODO*///	char flag[256];
/*TODO*///	memset(flag,0,256);
/*TODO*///
/*TODO*///	while (num_pens--)
/*TODO*///	{
/*TODO*///		int pen = pen_data[num_pens];
/*TODO*///		if (flag[pen] == 0)
/*TODO*///		{
/*TODO*///			if (color_flags & PALETTE_COLOR_VISIBLE)
/*TODO*///				pen_visiblecount[Machine->game_colortable[table_offset+pen]]++;
/*TODO*///			if (color_flags & PALETTE_COLOR_CACHED)
/*TODO*///				pen_cachedcount[Machine->game_colortable[table_offset+pen]]++;
/*TODO*///			flag[pen] = 1;
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///void palette_decrease_usage_countx(int table_offset, int num_pens, const UINT8 *pen_data,int color_flags)
/*TODO*///{
/*TODO*///	char flag[256];
/*TODO*///	memset(flag,0,256);
/*TODO*///
/*TODO*///	while (num_pens--)
/*TODO*///	{
/*TODO*///		int pen = pen_data[num_pens];
/*TODO*///		if (flag[pen] == 0)
/*TODO*///		{
/*TODO*///			if (color_flags & PALETTE_COLOR_VISIBLE)
/*TODO*///				pen_visiblecount[Machine->game_colortable[table_offset+pen]]--;
/*TODO*///			if (color_flags & PALETTE_COLOR_CACHED)
/*TODO*///				pen_cachedcount[Machine->game_colortable[table_offset+pen]]--;
/*TODO*///			flag[pen] = 1;
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///void palette_init_used_colors(void)
/*TODO*///{
/*TODO*///	int pen;
/*TODO*///
/*TODO*///
/*TODO*///	/* if we are not dynamically reducing the palette, return immediately. */
/*TODO*///	if (palette_used_colors == 0) return;
/*TODO*///
/*TODO*///	memset(palette_used_colors,PALETTE_COLOR_UNUSED,Machine->drv->total_colors * sizeof(UINT8));
/*TODO*///
/*TODO*///	for (pen = 0;pen < Machine->drv->total_colors;pen++)
/*TODO*///	{
/*TODO*///		if (pen_visiblecount[pen]) palette_used_colors[pen] |= PALETTE_COLOR_VISIBLE;
/*TODO*///		if (pen_cachedcount[pen]) palette_used_colors[pen] |= PALETTE_COLOR_CACHED;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*///static UINT8 rgb6_to_pen[64][64][64];
/*TODO*///
/*TODO*///static void build_rgb_to_pen(void)
/*TODO*///{
/*TODO*///	int i,rr,gg,bb;
/*TODO*///
/*TODO*///	memset(rgb6_to_pen,DYNAMIC_MAX_PENS,sizeof(rgb6_to_pen));
/*TODO*///	rgb6_to_pen[0][0][0] = BLACK_PEN;
/*TODO*///
/*TODO*///	for (i = 0;i < DYNAMIC_MAX_PENS;i++)
/*TODO*///	{
/*TODO*///		if (pen_usage_count[i] > 0)
/*TODO*///		{
/*TODO*///			rr = shrinked_palette[3*i + 0] >> 2;
/*TODO*///			gg = shrinked_palette[3*i + 1] >> 2;
/*TODO*///			bb = shrinked_palette[3*i + 2] >> 2;
/*TODO*///
/*TODO*///			if (rgb6_to_pen[rr][gg][bb] == DYNAMIC_MAX_PENS)
/*TODO*///			{
/*TODO*///				int j,max;
/*TODO*///
/*TODO*///				rgb6_to_pen[rr][gg][bb] = i;
/*TODO*///				max = pen_usage_count[i];
/*TODO*///
/*TODO*///				/* to reduce flickering during remaps, find the pen used by most colors */
/*TODO*///				for (j = i+1;j < DYNAMIC_MAX_PENS;j++)
/*TODO*///				{
/*TODO*///					if (pen_usage_count[j] > max &&
/*TODO*///							rr == (shrinked_palette[3*j + 0] >> 2) &&
/*TODO*///							gg == (shrinked_palette[3*j + 1] >> 2) &&
/*TODO*///							bb == (shrinked_palette[3*j + 2] >> 2))
/*TODO*///					{
/*TODO*///						rgb6_to_pen[rr][gg][bb] = j;
/*TODO*///						max = pen_usage_count[j];
/*TODO*///					}
/*TODO*///				}
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///static int compress_palette(void)
/*TODO*///{
/*TODO*///	int i,j,saved,r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	build_rgb_to_pen();
/*TODO*///
/*TODO*///	saved = 0;
/*TODO*///
/*TODO*///	for (i = 0;i < Machine->drv->total_colors;i++)
/*TODO*///	{
/*TODO*///		/* merge pens of the same color */
/*TODO*///		if ((old_used_colors[i] & PALETTE_COLOR_VISIBLE) &&
/*TODO*///				!(old_used_colors[i] & (PALETTE_COLOR_NEEDS_REMAP|PALETTE_COLOR_TRANSPARENT_FLAG)))
/*TODO*///		{
/*TODO*///			r = game_palette[3*i + 0] >> 2;
/*TODO*///			g = game_palette[3*i + 1] >> 2;
/*TODO*///			b = game_palette[3*i + 2] >> 2;
/*TODO*///
/*TODO*///			j = rgb6_to_pen[r][g][b];
/*TODO*///
/*TODO*///			if (palette_map[i] != j)
/*TODO*///			{
/*TODO*///				just_remapped[i] = 1;
/*TODO*///
/*TODO*///				pen_usage_count[palette_map[i]]--;
/*TODO*///				if (pen_usage_count[palette_map[i]] == 0)
/*TODO*///					saved++;
/*TODO*///				palette_map[i] = j;
/*TODO*///				pen_usage_count[palette_map[i]]++;
/*TODO*///				Machine->pens[i] = shrinked_pens[palette_map[i]];
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///
/*TODO*///#if VERBOSE
/*TODO*///{
/*TODO*///	int subcount[8];
/*TODO*///
/*TODO*///
/*TODO*///	for (i = 0;i < 8;i++)
/*TODO*///		subcount[i] = 0;
/*TODO*///
/*TODO*///	for (i = 0;i < Machine->drv->total_colors;i++)
/*TODO*///		subcount[palette_used_colors[i]]++;
/*TODO*///
/*TODO*///	logerror("Ran out of pens! %d colors used (%d unused, %d visible %d cached %d visible+cached, %d transparent)\n",
/*TODO*///			subcount[PALETTE_COLOR_VISIBLE]+subcount[PALETTE_COLOR_CACHED]+subcount[PALETTE_COLOR_VISIBLE|PALETTE_COLOR_CACHED]+subcount[PALETTE_COLOR_TRANSPARENT],
/*TODO*///			subcount[PALETTE_COLOR_UNUSED],
/*TODO*///			subcount[PALETTE_COLOR_VISIBLE],
/*TODO*///			subcount[PALETTE_COLOR_CACHED],
/*TODO*///			subcount[PALETTE_COLOR_VISIBLE|PALETTE_COLOR_CACHED],
/*TODO*///			subcount[PALETTE_COLOR_TRANSPARENT]);
/*TODO*///	logerror("Compressed the palette, saving %d pens\n",saved);
/*TODO*///}
/*TODO*///#endif
/*TODO*///
/*TODO*///	return saved;
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///static const UINT8 *palette_recalc_16_static(void)
/*TODO*///{
/*TODO*///	int i,color;
/*TODO*///	int did_remap = 0;
/*TODO*///	int need_refresh = 0;
/*TODO*///
/*TODO*///
/*TODO*///	memset(just_remapped,0,Machine->drv->total_colors * sizeof(UINT8));
/*TODO*///
/*TODO*///	for (color = 0;color < Machine->drv->total_colors;color++)
/*TODO*///	{
/*TODO*///		/* the comparison between palette_used_colors and old_used_colors also includes */
/*TODO*///		/* PALETTE_COLOR_NEEDS_REMAP which might have been set by palette_change_color() */
/*TODO*///		if ((palette_used_colors[color] & PALETTE_COLOR_VISIBLE) &&
/*TODO*///				palette_used_colors[color] != old_used_colors[color])
/*TODO*///		{
/*TODO*///			int r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///			did_remap = 1;
/*TODO*///			if (old_used_colors[color] & palette_used_colors[color] & PALETTE_COLOR_CACHED)
/*TODO*///			{
/*TODO*///				/* the color was and still is cached, we'll have to redraw everything */
/*TODO*///				need_refresh = 1;
/*TODO*///				just_remapped[color] = 1;
/*TODO*///			}
/*TODO*///
/*TODO*///			if (palette_used_colors[color] & PALETTE_COLOR_TRANSPARENT_FLAG)
/*TODO*///				Machine->pens[color] = palette_transparent_pen;
/*TODO*///			else
/*TODO*///			{
/*TODO*///				r = game_palette[3*color + 0];
/*TODO*///				g = game_palette[3*color + 1];
/*TODO*///				b = game_palette[3*color + 2];
/*TODO*///
/*TODO*///				Machine->pens[color] = shrinked_pens[rgbpenindex(r,g,b)];
/*TODO*///			}
/*TODO*///		}
/*TODO*///
/*TODO*///		old_used_colors[color] = palette_used_colors[color];
/*TODO*///	}
/*TODO*///
/*TODO*///
/*TODO*///	if (did_remap)
/*TODO*///	{
/*TODO*///		/* rebuild the color lookup table */
/*TODO*///		for (i = 0;i < Machine->drv->color_table_len;i++)
/*TODO*///			Machine->remapped_colortable[i] = Machine->pens[Machine->game_colortable[i]];
/*TODO*///	}
/*TODO*///
/*TODO*///	if (need_refresh) return just_remapped;
/*TODO*///	else return 0;
/*TODO*///}
/*TODO*///
/*TODO*///static const UINT8 *palette_recalc_16_palettized(void)
/*TODO*///{
/*TODO*///	int i,color;
/*TODO*///	int did_remap = 0;
/*TODO*///	int need_refresh = 0;
/*TODO*///
/*TODO*///
/*TODO*///	memset(just_remapped,0,Machine->drv->total_colors * sizeof(UINT8));
/*TODO*///
/*TODO*///	for (color = 0;color < Machine->drv->total_colors;color++)
/*TODO*///	{
/*TODO*///		if ((palette_used_colors[color] & PALETTE_COLOR_TRANSPARENT_FLAG) !=
/*TODO*///				(old_used_colors[color] & PALETTE_COLOR_TRANSPARENT_FLAG))
/*TODO*///		{
/*TODO*///			did_remap = 1;
/*TODO*///			if (old_used_colors[color] & palette_used_colors[color] & PALETTE_COLOR_CACHED)
/*TODO*///			{
/*TODO*///				/* the color was and still is cached, we'll have to redraw everything */
/*TODO*///				need_refresh = 1;
/*TODO*///				just_remapped[color] = 1;
/*TODO*///			}
/*TODO*///
/*TODO*///			if (palette_used_colors[color] & PALETTE_COLOR_TRANSPARENT_FLAG)
/*TODO*///				Machine->pens[color] = palette_transparent_pen;
/*TODO*///			else
/*TODO*///				Machine->pens[color] = shrinked_pens[color + RESERVED_PENS];
/*TODO*///		}
/*TODO*///
/*TODO*///		old_used_colors[color] = palette_used_colors[color];
/*TODO*///	}
/*TODO*///
/*TODO*///
/*TODO*///	if (did_remap)
/*TODO*///	{
/*TODO*///		/* rebuild the color lookup table */
/*TODO*///		for (i = 0;i < Machine->drv->color_table_len;i++)
/*TODO*///			Machine->remapped_colortable[i] = Machine->pens[Machine->game_colortable[i]];
/*TODO*///	}
/*TODO*///
/*TODO*///	if (need_refresh) return just_remapped;
/*TODO*///	else return 0;
/*TODO*///}
/*TODO*///
    static int rec_color;
    static int rec_did_remap;
    static int rec_need_refresh;
    static int rec_first_free_pen;
    static int rec_ran_out;
    static int rec_reuse_pens;
    static int rec_need, rec_avail;

    public static UBytePtr palette_recalc_8() {
        int i;
        rec_color = 0;
        rec_did_remap = 0;
        rec_need_refresh = 0;
        rec_first_free_pen = 0;
        rec_ran_out = 0;
        rec_reuse_pens = 0;
        rec_need = 0;
        rec_avail = 0;

        memset(just_remapped, 0, Machine.drv.total_colors);

        /* first of all, apply the changes to the palette which were */
 /* requested since last update */
        for (rec_color = 0; rec_color < Machine.drv.total_colors; rec_color++) {
            if (palette_dirty.read(rec_color) != 0) {
                int r, g, b, pen;
                pen = palette_map[rec_color];
                r = new_palette.read(3 * rec_color + 0);
                g = new_palette.read(3 * rec_color + 1);
                b = new_palette.read(3 * rec_color + 2);

                /* if the color maps to an exclusive pen, just change it */
                if (pen_usage_count[pen] == 1) {
                    palette_dirty.write(rec_color, 0);
                    game_palette[3 * rec_color + 0] = (char) (r & 0xFF);
                    game_palette[3 * rec_color + 1] = (char) (g & 0xFF);
                    game_palette[3 * rec_color + 2] = (char) (b & 0xFF);

                    shrinked_palette[3 * pen + 0] = (char) (r & 0xFF);
                    shrinked_palette[3 * pen + 1] = (char) (g & 0xFF);
                    shrinked_palette[3 * pen + 2] = (char) (b & 0xFF);
                    osd_modify_pen(Machine.pens[rec_color], r, g, b);
                } else {
                    if (pen < RESERVED_PENS) {
                        /* the color uses a reserved pen, the only thing we can do is remap it */
                        for (i = rec_color; i < Machine.drv.total_colors; i++) {
                            if (palette_dirty.read(i) != 0 && palette_map[i] == pen) {
                                palette_dirty.write(i, 0);
                                game_palette[3 * i + 0] = new_palette.read(3 * i + 0);
                                game_palette[3 * i + 1] = new_palette.read(3 * i + 1);
                                game_palette[3 * i + 2] = new_palette.read(3 * i + 2);
                                old_used_colors.write(i, old_used_colors.read(i) | PALETTE_COLOR_NEEDS_REMAP);
                            }
                        }
                    } else {
                        /* the pen is shared with other colors, let's see if all of them */
 /* have been changed to the same value */
                        for (i = 0; i < Machine.drv.total_colors; i++) {
                            if ((old_used_colors.read(i) & PALETTE_COLOR_VISIBLE) != 0
                                    && palette_map[i] == pen) {
                                if (palette_dirty.read(i) == 0
                                        || new_palette.read(3 * i + 0) != r
                                        || new_palette.read(3 * i + 1) != g
                                        || new_palette.read(3 * i + 2) != b) {
                                    break;
                                }
                            }
                        }

                        if (i == Machine.drv.total_colors) {
                            /* all colors sharing this pen still are the same, so we */
 /* just change the palette. */
                            shrinked_palette[3 * pen + 0] = (char) (r & 0xFF);
                            shrinked_palette[3 * pen + 1] = (char) (g & 0xFF);
                            shrinked_palette[3 * pen + 2] = (char) (b & 0xFF);
                            osd_modify_pen(Machine.pens[rec_color], r, g, b);

                            for (i = rec_color; i < Machine.drv.total_colors; i++) {
                                if (palette_dirty.read(i) != 0 && palette_map[i] == pen) {
                                    palette_dirty.write(i, 0);
                                    game_palette[3 * i + 0] = (char) (r & 0xFF);
                                    game_palette[3 * i + 1] = (char) (g & 0xFF);
                                    game_palette[3 * i + 2] = (char) (b & 0xFF);
                                }
                            }
                        } else {
                            /* the colors sharing this pen now are different, we'll */
 /* have to remap them. */
                            for (i = rec_color; i < Machine.drv.total_colors; i++) {
                                if (palette_dirty.read(i) != 0 && palette_map[i] == pen) {
                                    palette_dirty.write(i, 0);
                                    game_palette[3 * i + 0] = new_palette.read(3 * i + 0);
                                    game_palette[3 * i + 1] = new_palette.read(3 * i + 1);
                                    game_palette[3 * i + 2] = new_palette.read(3 * i + 2);
                                    old_used_colors.write(i, old_used_colors.read(i) | PALETTE_COLOR_NEEDS_REMAP);
                                }
                            }
                        }
                    }
                }
            }
        }
        rec_need = 0;
        for (i = 0; i < Machine.drv.total_colors; i++) {
            if (((palette_used_colors.read(i) & PALETTE_COLOR_VISIBLE) != 0) && palette_used_colors.read(i) != old_used_colors.read(i)) {
                rec_need++;
            }
        }
        if (rec_need > 0) {
            rec_avail = 0;
            for (i = 0; i < DYNAMIC_MAX_PENS; i++) {
                if (pen_usage_count[i] == 0) {
                    rec_avail++;
                }
            }

            if (rec_need > rec_avail) {
                if (palettelog != null) {
                    fprintf(palettelog, "Need %d new pens; %d available. I'll reuse some pens.\n", rec_need, rec_avail);
                    System.out.print(String.format("Need %d new pens; %d available. I'll reuse some pens.\n", rec_need, rec_avail));
                }
                rec_reuse_pens = 1;
                build_rgb_to_pen();
            }
        }

        rec_first_free_pen = RESERVED_PENS;
        for (rec_color = 0; rec_color < Machine.drv.total_colors; rec_color++) {
            /* the comparison between palette_used_colors and old_used_colors also includes */
 /* PALETTE_COLOR_NEEDS_REMAP which might have been set previously */
            if (((palette_used_colors.read(rec_color) & PALETTE_COLOR_VISIBLE) != 0)
                    && palette_used_colors.read(rec_color) != old_used_colors.read(rec_color)) {
                int r, g, b;
                if ((old_used_colors.read(rec_color) & PALETTE_COLOR_VISIBLE) != 0) {
                    pen_usage_count[palette_map[rec_color]]--;
                    old_used_colors.write(rec_color, old_used_colors.read(rec_color) & ~PALETTE_COLOR_VISIBLE);
                }

                r = game_palette[3 * rec_color + 0];
                g = game_palette[3 * rec_color + 1];
                b = game_palette[3 * rec_color + 2];

                if ((palette_used_colors.read(rec_color) & PALETTE_COLOR_TRANSPARENT_FLAG) != 0) {
                    if (palette_map[rec_color] != TRANSPARENT_PEN) {
                        /* use the fixed transparent black for this */
                        rec_did_remap = 1;
                        if (((old_used_colors.read(rec_color) & palette_used_colors.read(rec_color) & PALETTE_COLOR_CACHED)) != 0) {
                            /* the color was and still is cached, we'll have to redraw everything */
                            rec_need_refresh = 1;
                            just_remapped.write(rec_color, 1);
                        }

                        palette_map[rec_color] = TRANSPARENT_PEN;
                    }
                    pen_usage_count[palette_map[rec_color]]++;
                    Machine.pens[rec_color] = shrinked_pens[palette_map[rec_color]];
                    old_used_colors.write(rec_color, palette_used_colors.read(rec_color));
                } else {
                    if (rec_reuse_pens != 0) {
                        i = rgb6_to_pen[r >> 2][g >> 2][b >> 2];
                        if (i != DYNAMIC_MAX_PENS) {
                            if (palette_map[rec_color] != i) {
                                rec_did_remap = 1;
                                if ((old_used_colors.read(rec_color) & palette_used_colors.read(rec_color) & PALETTE_COLOR_CACHED) != 0) {
                                    /* the color was and still is cached, we'll have to redraw everything */
                                    rec_need_refresh = 1;
                                    just_remapped.write(rec_color, 1);
                                }

                                palette_map[rec_color] = (char) i;
                            }
                            pen_usage_count[palette_map[rec_color]]++;
                            Machine.pens[rec_color] = shrinked_pens[palette_map[rec_color]];
                            old_used_colors.write(rec_color, palette_used_colors.read(rec_color));
                        }
                    }

                    /* if we still haven't found a pen, choose a new one */
                    if (old_used_colors.read(rec_color) != palette_used_colors.read(rec_color)) {
                        /* if possible, reuse the last associated pen */
                        if (pen_usage_count[palette_map[rec_color]] == 0) {
                            pen_usage_count[palette_map[rec_color]]++;
                        } else /* allocate a new pen */ {
                            if (rec_retry() == 1) {
                                continue;
                            }
                        }

                        {
                            int rr, gg, bb;

                            i = palette_map[rec_color];
                            rr = shrinked_palette[3 * i + 0] >> 2;
                            gg = shrinked_palette[3 * i + 1] >> 2;
                            bb = shrinked_palette[3 * i + 2] >> 2;
                            if (rgb6_to_pen[rr][gg][bb] == i) {
                                rgb6_to_pen[rr][gg][bb] = DYNAMIC_MAX_PENS;
                            }

                            shrinked_palette[3 * i + 0] = (char) (r & 0xFF);
                            shrinked_palette[3 * i + 1] = (char) (g & 0xFF);
                            shrinked_palette[3 * i + 2] = (char) (b & 0xFF);
                            osd_modify_pen(Machine.pens[rec_color], r, g, b);

                            r >>= 2;
                            g >>= 2;
                            b >>= 2;
                            if (rgb6_to_pen[r][g][b] == DYNAMIC_MAX_PENS) {
                                rgb6_to_pen[r][g][b] = (char) (i & 0xFF);
                            }
                        }

                        old_used_colors.write(rec_color, palette_used_colors.read(rec_color));
                    }
                }
            }
        }

        if (rec_ran_out > 1) {
            if (palettelog != null) {
                fprintf(palettelog, "Error: no way to shrink the palette to 256 colors, left out %d colors.\n", rec_ran_out - 1);
                System.out.print(String.format("Error: no way to shrink the palette to 256 colors, left out %d colors.\n", rec_ran_out - 1));
            }
            if (palettelog != null) {
                fprintf(palettelog, "color list:\n");
                for (rec_color = 0; rec_color < Machine.drv.total_colors; rec_color++) {
                    int r, g, b;
                    r = game_palette[3 * rec_color + 0];
                    g = game_palette[3 * rec_color + 1];
                    b = game_palette[3 * rec_color + 2];
                    if (((palette_used_colors.read(rec_color) & PALETTE_COLOR_VISIBLE)) != 0) {
                        fprintf(palettelog, "%02x %02x %02x\n", r, g, b);
                    }
                }
            }
        }

        /* Reclaim unused pens; we do this AFTER allocating the new ones, to avoid */
 /* using the same pen for two different colors in two consecutive frames, */
 /* which might cause flicker. */
        for (rec_color = 0; rec_color < Machine.drv.total_colors; rec_color++) {
            if ((palette_used_colors.read(rec_color) & PALETTE_COLOR_VISIBLE) == 0) {
                if ((old_used_colors.read(rec_color) & PALETTE_COLOR_VISIBLE) != 0) {
                    pen_usage_count[palette_map[rec_color]]--;
                }
                old_used_colors.write(rec_color, palette_used_colors.read(rec_color));
            }
        }
        if (rec_did_remap != 0) {
            /* rebuild the color lookup table */
            for (i = 0; i < Machine.drv.color_table_len; i++) {
                Machine.remapped_colortable.write(i, Machine.pens[Machine.game_colortable[i]]);
            }
        }

        if (rec_need_refresh != 0) {
            int used;

            used = 0;
            for (i = 0; i < DYNAMIC_MAX_PENS; i++) {
                if (pen_usage_count[i] > 0) {
                    used++;
                }
            }
            if (palettelog != null) {
                fprintf(palettelog, "Did a palette remap, need a full screen redraw (%d pens used).\n", used);
                System.out.print(String.format("Did a palette remap, need a full screen redraw (%d pens used).\n", used));
            }

            return just_remapped;
        } else {
            return null;
        }
    }

    public static int rec_retry() {
        while (rec_first_free_pen < DYNAMIC_MAX_PENS && pen_usage_count[rec_first_free_pen] > 0) {
            rec_first_free_pen++;
        }

        if (rec_first_free_pen < DYNAMIC_MAX_PENS) {
            rec_did_remap = 1;
            if (((old_used_colors.read(rec_color) & palette_used_colors.read(rec_color) & PALETTE_COLOR_CACHED)) != 0) {
                /* the color was and still is cached, we'll have to redraw everything */
                rec_need_refresh = 1;
                just_remapped.write(rec_color, 1);
            }

            palette_map[rec_color] = (char) rec_first_free_pen;
            pen_usage_count[palette_map[rec_color]]++;
            Machine.pens[rec_color] = shrinked_pens[palette_map[rec_color]];
        } else {
            /* Ran out of pens! Let's see what we can do. */

            if (rec_ran_out == 0) {
                rec_ran_out++;

                /* from now on, try to reuse already allocated pens */
                rec_reuse_pens = 1;
                if (compress_palette() > 0) {
                    rec_did_remap = 1;
                    rec_need_refresh = 1;
                    /* we'll have to redraw everything */

                    rec_first_free_pen = RESERVED_PENS;
                    return rec_retry();
                }
            }

            rec_ran_out++;

            /* we failed, but go on with the loop, there might */
 /* be some transparent pens to remap */
            return 1;//continue
        }
        return 0;
    }
    /*TODO*///
/*TODO*///
/*TODO*///const UINT8 *palette_recalc(void)
/*TODO*///{
/*TODO*///	const UINT8 *ret = NULL;
/*TODO*///
/*TODO*///	/* if we are not dynamically reducing the palette, return NULL. */
/*TODO*///	if (palette_used_colors != 0)
/*TODO*///	{
/*TODO*///		switch (use_16bit)
/*TODO*///		{
/*TODO*///			case NO_16BIT:
/*TODO*///			default:
/*TODO*///				ret = palette_recalc_8();
/*TODO*///				break;
/*TODO*///			case STATIC_16BIT:
/*TODO*///				ret = palette_recalc_16_static();
/*TODO*///				break;
/*TODO*///			case PALETTIZED_16BIT:
/*TODO*///				ret = palette_recalc_16_palettized();
/*TODO*///				break;
/*TODO*///		}
/*TODO*///	}
/*TODO*///
/*TODO*///	if (ret) artwork_remap();
/*TODO*///
/*TODO*///	return ret;
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*////******************************************************************************
/*TODO*///
/*TODO*/// Commonly used palette RAM handling functions
/*TODO*///
/*TODO*///******************************************************************************/
/*TODO*///
/*TODO*///UINT8 *paletteram,*paletteram_2;
/*TODO*///
/*TODO*///READ_HANDLER( paletteram_r )
/*TODO*///{
/*TODO*///	return paletteram[offset];
/*TODO*///}
/*TODO*///
/*TODO*///READ_HANDLER( paletteram_2_r )
/*TODO*///{
/*TODO*///	return paletteram_2[offset];
/*TODO*///}
/*TODO*///
/*TODO*///READ_HANDLER( paletteram_word_r )
/*TODO*///{
/*TODO*///	return READ_WORD(&paletteram[offset]);
/*TODO*///}
/*TODO*///
/*TODO*///READ_HANDLER( paletteram_2_word_r )
/*TODO*///{
/*TODO*///	return READ_WORD(&paletteram_2[offset]);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_RRRGGGBB_w )
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///	int bit0,bit1,bit2;
/*TODO*///
/*TODO*///
/*TODO*///	paletteram[offset] = data;
/*TODO*///
/*TODO*///	/* red component */
/*TODO*///	bit0 = (data >> 5) & 0x01;
/*TODO*///	bit1 = (data >> 6) & 0x01;
/*TODO*///	bit2 = (data >> 7) & 0x01;
/*TODO*///	r = 0x21 * bit0 + 0x47 * bit1 + 0x97 * bit2;
/*TODO*///	/* green component */
/*TODO*///	bit0 = (data >> 2) & 0x01;
/*TODO*///	bit1 = (data >> 3) & 0x01;
/*TODO*///	bit2 = (data >> 4) & 0x01;
/*TODO*///	g = 0x21 * bit0 + 0x47 * bit1 + 0x97 * bit2;
/*TODO*///	/* blue component */
/*TODO*///	bit0 = 0;
/*TODO*///	bit1 = (data >> 0) & 0x01;
/*TODO*///	bit2 = (data >> 1) & 0x01;
/*TODO*///	b = 0x21 * bit0 + 0x47 * bit1 + 0x97 * bit2;
/*TODO*///
/*TODO*///	palette_change_color(offset,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_BBGGGRRR_w )
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///	int bit0,bit1,bit2;
/*TODO*///
/*TODO*///
/*TODO*///	paletteram[offset] = data;
/*TODO*///
/*TODO*///	/* red component */
/*TODO*///	bit0 = (data >> 0) & 0x01;
/*TODO*///	bit1 = (data >> 1) & 0x01;
/*TODO*///	bit2 = (data >> 2) & 0x01;
/*TODO*///	r = 0x21 * bit0 + 0x47 * bit1 + 0x97 * bit2;
/*TODO*///	/* green component */
/*TODO*///	bit0 = (data >> 3) & 0x01;
/*TODO*///	bit1 = (data >> 4) & 0x01;
/*TODO*///	bit2 = (data >> 5) & 0x01;
/*TODO*///	g = 0x21 * bit0 + 0x47 * bit1 + 0x97 * bit2;
/*TODO*///	/* blue component */
/*TODO*///	bit0 = 0;
/*TODO*///	bit1 = (data >> 6) & 0x01;
/*TODO*///	bit2 = (data >> 7) & 0x01;
/*TODO*///	b = 0x21 * bit0 + 0x47 * bit1 + 0x97 * bit2;
/*TODO*///
/*TODO*///	palette_change_color(offset,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_IIBBGGRR_w )
/*TODO*///{
/*TODO*///	int r,g,b,i;
/*TODO*///
/*TODO*///
/*TODO*///	paletteram[offset] = data;
/*TODO*///
/*TODO*///	i = (data >> 6) & 0x03;
/*TODO*///	/* red component */
/*TODO*///	r = (data << 2) & 0x0c;
/*TODO*///	if (r) r |= i;
/*TODO*///	r *= 0x11;
/*TODO*///	/* green component */
/*TODO*///	g = (data >> 0) & 0x0c;
/*TODO*///	if (g) g |= i;
/*TODO*///	g *= 0x11;
/*TODO*///	/* blue component */
/*TODO*///	b = (data >> 2) & 0x0c;
/*TODO*///	if (b) b |= i;
/*TODO*///	b *= 0x11;
/*TODO*///
/*TODO*///	palette_change_color(offset,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_BBGGRRII_w )
/*TODO*///{
/*TODO*///	int r,g,b,i;
/*TODO*///
/*TODO*///
/*TODO*///	paletteram[offset] = data;
/*TODO*///
/*TODO*///	i = (data >> 0) & 0x03;
/*TODO*///	/* red component */
/*TODO*///	r = (((data >> 0) & 0x0c) | i) * 0x11;
/*TODO*///	/* green component */
/*TODO*///	g = (((data >> 2) & 0x0c) | i) * 0x11;
/*TODO*///	/* blue component */
/*TODO*///	b = (((data >> 4) & 0x0c) | i) * 0x11;
/*TODO*///
/*TODO*///	palette_change_color(offset,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_xxxxBBBBGGGGRRRR(int color,int data)
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	r = (data >> 0) & 0x0f;
/*TODO*///	g = (data >> 4) & 0x0f;
/*TODO*///	b = (data >> 8) & 0x0f;
/*TODO*///
/*TODO*///	r = (r << 4) | r;
/*TODO*///	g = (g << 4) | g;
/*TODO*///	b = (b << 4) | b;
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxBBBBGGGGRRRR_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xxxxBBBBGGGGRRRR(offset / 2,paletteram[offset & ~1] | (paletteram[offset | 1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxBBBBGGGGRRRR_swap_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xxxxBBBBGGGGRRRR(offset / 2,paletteram[offset | 1] | (paletteram[offset & ~1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxBBBBGGGGRRRR_split1_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xxxxBBBBGGGGRRRR(offset,paletteram[offset] | (paletteram_2[offset] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxBBBBGGGGRRRR_split2_w )
/*TODO*///{
/*TODO*///	paletteram_2[offset] = data;
/*TODO*///	changecolor_xxxxBBBBGGGGRRRR(offset,paletteram[offset] | (paletteram_2[offset] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxBBBBGGGGRRRR_word_w )
/*TODO*///{
/*TODO*///	int oldword = READ_WORD(&paletteram[offset]);
/*TODO*///	int newword = COMBINE_WORD(oldword,data);
/*TODO*///
/*TODO*///
/*TODO*///	WRITE_WORD(&paletteram[offset],newword);
/*TODO*///	changecolor_xxxxBBBBGGGGRRRR(offset / 2,newword);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_xxxxBBBBRRRRGGGG(int color,int data)
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	r = (data >> 4) & 0x0f;
/*TODO*///	g = (data >> 0) & 0x0f;
/*TODO*///	b = (data >> 8) & 0x0f;
/*TODO*///
/*TODO*///	r = (r << 4) | r;
/*TODO*///	g = (g << 4) | g;
/*TODO*///	b = (b << 4) | b;
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxBBBBRRRRGGGG_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xxxxBBBBRRRRGGGG(offset / 2,paletteram[offset & ~1] | (paletteram[offset | 1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxBBBBRRRRGGGG_swap_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xxxxBBBBRRRRGGGG(offset / 2,paletteram[offset | 1] | (paletteram[offset & ~1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxBBBBRRRRGGGG_split1_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xxxxBBBBRRRRGGGG(offset,paletteram[offset] | (paletteram_2[offset] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxBBBBRRRRGGGG_split2_w )
/*TODO*///{
/*TODO*///	paletteram_2[offset] = data;
/*TODO*///	changecolor_xxxxBBBBRRRRGGGG(offset,paletteram[offset] | (paletteram_2[offset] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_xxxxRRRRBBBBGGGG(int color,int data)
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	r = (data >> 8) & 0x0f;
/*TODO*///	g = (data >> 0) & 0x0f;
/*TODO*///	b = (data >> 4) & 0x0f;
/*TODO*///
/*TODO*///	r = (r << 4) | r;
/*TODO*///	g = (g << 4) | g;
/*TODO*///	b = (b << 4) | b;
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxRRRRBBBBGGGG_split1_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xxxxRRRRBBBBGGGG(offset,paletteram[offset] | (paletteram_2[offset] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxRRRRBBBBGGGG_split2_w )
/*TODO*///{
/*TODO*///	paletteram_2[offset] = data;
/*TODO*///	changecolor_xxxxRRRRBBBBGGGG(offset,paletteram[offset] | (paletteram_2[offset] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_xxxxRRRRGGGGBBBB(int color,int data)
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	r = (data >> 8) & 0x0f;
/*TODO*///	g = (data >> 4) & 0x0f;
/*TODO*///	b = (data >> 0) & 0x0f;
/*TODO*///
/*TODO*///	r = (r << 4) | r;
/*TODO*///	g = (g << 4) | g;
/*TODO*///	b = (b << 4) | b;
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxRRRRGGGGBBBB_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xxxxRRRRGGGGBBBB(offset / 2,paletteram[offset & ~1] | (paletteram[offset | 1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxRRRRGGGGBBBB_swap_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xxxxRRRRGGGGBBBB(offset / 2,paletteram[offset | 1] | (paletteram[offset & ~1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xxxxRRRRGGGGBBBB_word_w )
/*TODO*///{
/*TODO*///	int oldword = READ_WORD(&paletteram[offset]);
/*TODO*///	int newword = COMBINE_WORD(oldword,data);
/*TODO*///
/*TODO*///
/*TODO*///	WRITE_WORD(&paletteram[offset],newword);
/*TODO*///	changecolor_xxxxRRRRGGGGBBBB(offset / 2,newword);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_RRRRGGGGBBBBxxxx(int color,int data)
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	r = (data >> 12) & 0x0f;
/*TODO*///	g = (data >>  8) & 0x0f;
/*TODO*///	b = (data >>  4) & 0x0f;
/*TODO*///
/*TODO*///	r = (r << 4) | r;
/*TODO*///	g = (g << 4) | g;
/*TODO*///	b = (b << 4) | b;
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_RRRRGGGGBBBBxxxx_swap_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_RRRRGGGGBBBBxxxx(offset / 2,paletteram[offset | 1] | (paletteram[offset & ~1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_RRRRGGGGBBBBxxxx_split1_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_RRRRGGGGBBBBxxxx(offset,paletteram[offset] | (paletteram_2[offset] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_RRRRGGGGBBBBxxxx_split2_w )
/*TODO*///{
/*TODO*///	paletteram_2[offset] = data;
/*TODO*///	changecolor_RRRRGGGGBBBBxxxx(offset,paletteram[offset] | (paletteram_2[offset] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_RRRRGGGGBBBBxxxx_word_w )
/*TODO*///{
/*TODO*///	int oldword = READ_WORD(&paletteram[offset]);
/*TODO*///	int newword = COMBINE_WORD(oldword,data);
/*TODO*///
/*TODO*///
/*TODO*///	WRITE_WORD(&paletteram[offset],newword);
/*TODO*///	changecolor_RRRRGGGGBBBBxxxx(offset / 2,newword);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_BBBBGGGGRRRRxxxx(int color,int data)
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	r = (data >>  4) & 0x0f;
/*TODO*///	g = (data >>  8) & 0x0f;
/*TODO*///	b = (data >> 12) & 0x0f;
/*TODO*///
/*TODO*///	r = (r << 4) | r;
/*TODO*///	g = (g << 4) | g;
/*TODO*///	b = (b << 4) | b;
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_BBBBGGGGRRRRxxxx_swap_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_BBBBGGGGRRRRxxxx(offset / 2,paletteram[offset | 1] | (paletteram[offset & ~1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_BBBBGGGGRRRRxxxx_split1_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_BBBBGGGGRRRRxxxx(offset,paletteram[offset] | (paletteram_2[offset] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_BBBBGGGGRRRRxxxx_split2_w )
/*TODO*///{
/*TODO*///	paletteram_2[offset] = data;
/*TODO*///	changecolor_BBBBGGGGRRRRxxxx(offset,paletteram[offset] | (paletteram_2[offset] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_BBBBGGGGRRRRxxxx_word_w )
/*TODO*///{
/*TODO*///	int oldword = READ_WORD(&paletteram[offset]);
/*TODO*///	int newword = COMBINE_WORD(oldword,data);
/*TODO*///
/*TODO*///
/*TODO*///	WRITE_WORD(&paletteram[offset],newword);
/*TODO*///	changecolor_BBBBGGGGRRRRxxxx(offset / 2,newword);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_xBBBBBGGGGGRRRRR(int color,int data)
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	r = (data >>  0) & 0x1f;
/*TODO*///	g = (data >>  5) & 0x1f;
/*TODO*///	b = (data >> 10) & 0x1f;
/*TODO*///
/*TODO*///	r = (r << 3) | (r >> 2);
/*TODO*///	g = (g << 3) | (g >> 2);
/*TODO*///	b = (b << 3) | (b >> 2);
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xBBBBBGGGGGRRRRR_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xBBBBBGGGGGRRRRR(offset / 2,paletteram[offset & ~1] | (paletteram[offset | 1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xBBBBBGGGGGRRRRR_swap_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xBBBBBGGGGGRRRRR(offset / 2,paletteram[offset | 1] | (paletteram[offset & ~1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xBBBBBGGGGGRRRRR_word_w )
/*TODO*///{
/*TODO*///	int oldword = READ_WORD(&paletteram[offset]);
/*TODO*///	int newword = COMBINE_WORD(oldword,data);
/*TODO*///
/*TODO*///
/*TODO*///	WRITE_WORD(&paletteram[offset],newword);
/*TODO*///	changecolor_xBBBBBGGGGGRRRRR(offset / 2,newword);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_xRRRRRGGGGGBBBBB(int color,int data)
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	r = (data >> 10) & 0x1f;
/*TODO*///	g = (data >>  5) & 0x1f;
/*TODO*///	b = (data >>  0) & 0x1f;
/*TODO*///
/*TODO*///	r = (r << 3) | (r >> 2);
/*TODO*///	g = (g << 3) | (g >> 2);
/*TODO*///	b = (b << 3) | (b >> 2);
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xRRRRRGGGGGBBBBB_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_xRRRRRGGGGGBBBBB(offset / 2,paletteram[offset & ~1] | (paletteram[offset | 1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xRRRRRGGGGGBBBBB_word_w )
/*TODO*///{
/*TODO*///	int oldword = READ_WORD(&paletteram[offset]);
/*TODO*///	int newword = COMBINE_WORD(oldword,data);
/*TODO*///
/*TODO*///
/*TODO*///	WRITE_WORD(&paletteram[offset],newword);
/*TODO*///	changecolor_xRRRRRGGGGGBBBBB(offset / 2,newword);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_xGGGGGRRRRRBBBBB(int color,int data)
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	r = (data >>  5) & 0x1f;
/*TODO*///	g = (data >> 10) & 0x1f;
/*TODO*///	b = (data >>  0) & 0x1f;
/*TODO*///
/*TODO*///	r = (r << 3) | (r >> 2);
/*TODO*///	g = (g << 3) | (g >> 2);
/*TODO*///	b = (b << 3) | (b >> 2);
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_xGGGGGRRRRRBBBBB_word_w )
/*TODO*///{
/*TODO*///	int oldword = READ_WORD(&paletteram[offset]);
/*TODO*///	int newword = COMBINE_WORD(oldword,data);
/*TODO*///
/*TODO*///
/*TODO*///	WRITE_WORD(&paletteram[offset],newword);
/*TODO*///	changecolor_xGGGGGRRRRRBBBBB(offset / 2,newword);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_RRRRRGGGGGBBBBBx(int color,int data)
/*TODO*///{
/*TODO*///	int r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	r = (data >> 11) & 0x1f;
/*TODO*///	g = (data >>  6) & 0x1f;
/*TODO*///	b = (data >>  1) & 0x1f;
/*TODO*///
/*TODO*///	r = (r << 3) | (r >> 2);
/*TODO*///	g = (g << 3) | (g >> 2);
/*TODO*///	b = (b << 3) | (b >> 2);
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_RRRRRGGGGGBBBBBx_w )
/*TODO*///{
/*TODO*///	paletteram[offset] = data;
/*TODO*///	changecolor_RRRRRGGGGGBBBBBx(offset / 2,paletteram[offset & ~1] | (paletteram[offset | 1] << 8));
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_RRRRRGGGGGBBBBBx_word_w )
/*TODO*///{
/*TODO*///	int oldword = READ_WORD(&paletteram[offset]);
/*TODO*///	int newword = COMBINE_WORD(oldword,data);
/*TODO*///
/*TODO*///
/*TODO*///	WRITE_WORD(&paletteram[offset],newword);
/*TODO*///	changecolor_RRRRRGGGGGBBBBBx(offset / 2,newword);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_IIIIRRRRGGGGBBBB(int color,int data)
/*TODO*///{
/*TODO*///	int i,r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	static const int ztable[16] =
/*TODO*///		{ 0x0, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf, 0x10, 0x11 };
/*TODO*///
/*TODO*///	i = ztable[(data >> 12) & 15];
/*TODO*///	r = ((data >> 8) & 15) * i;
/*TODO*///	g = ((data >> 4) & 15) * i;
/*TODO*///	b = ((data >> 0) & 15) * i;
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_IIIIRRRRGGGGBBBB_word_w )
/*TODO*///{
/*TODO*///	int oldword = READ_WORD(&paletteram[offset]);
/*TODO*///	int newword = COMBINE_WORD(oldword,data);
/*TODO*///
/*TODO*///
/*TODO*///	WRITE_WORD(&paletteram[offset],newword);
/*TODO*///	changecolor_IIIIRRRRGGGGBBBB(offset / 2,newword);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void changecolor_RRRRGGGGBBBBIIII(int color,int data)
/*TODO*///{
/*TODO*///	int i,r,g,b;
/*TODO*///
/*TODO*///
/*TODO*///	static const int ztable[16] =
/*TODO*///		{ 0x0, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf, 0x10, 0x11 };
/*TODO*///
/*TODO*///	i = ztable[(data >> 0) & 15];
/*TODO*///	r = ((data >> 12) & 15) * i;
/*TODO*///	g = ((data >>  8) & 15) * i;
/*TODO*///	b = ((data >>  4) & 15) * i;
/*TODO*///
/*TODO*///	palette_change_color(color,r,g,b);
/*TODO*///}
/*TODO*///
/*TODO*///WRITE_HANDLER( paletteram_RRRRGGGGBBBBIIII_word_w )
/*TODO*///{
/*TODO*///	int oldword = READ_WORD(&paletteram[offset]);
/*TODO*///	int newword = COMBINE_WORD(oldword,data);
/*TODO*///
/*TODO*///
/*TODO*///	WRITE_WORD(&paletteram[offset],newword);
/*TODO*///	changecolor_RRRRGGGGBBBBIIII(offset / 2,newword);
/*TODO*///}
/*TODO*///    
}
