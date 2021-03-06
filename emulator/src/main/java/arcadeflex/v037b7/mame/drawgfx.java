/*
 * Ported to v0.37b7
 */
package arcadeflex.v037b7.mame;

//mame imports
import static arcadeflex.v037b7.mame.drawgfxH.*;
import static arcadeflex.v037b7.mame.driverH.*;
//to be organized
import static arcadeflex.v037b7.mame.tilemapC.priority_bitmap;
import gr.codebb.arcadeflex.common.PtrLib.UBytePtr;
import gr.codebb.arcadeflex.common.SubArrays.UShortArray;
import static gr.codebb.arcadeflex.v036.mame.mame.Machine;
import gr.codebb.arcadeflex.v036.mame.osdependH.osd_bitmap;
import gr.codebb.arcadeflex.v036.platform.libc_old.IntPtr;

public class drawgfx {

    public static final int BL0 = 0;
    public static final int BL1 = 1;
    public static final int BL2 = 2;
    public static final int BL3 = 3;
    public static final int WL0 = 0;
    public static final int WL1 = 1;

    /*TODO*///UINT8 gfx_drawmode_table[256];
/*TODO*///plot_pixel_proc plot_pixel;
/*TODO*///read_pixel_proc read_pixel;
    public static plot_box_procPtr plot_box;

    /*TODO*///
/*TODO*///static UINT8 is_raw[TRANSPARENCY_MODES];
/*TODO*///
/*TODO*///
/*TODO*///#ifdef ALIGN_INTS /* GSL 980108 read/write nonaligned dword routine for ARM processor etc */
/*TODO*///
/*TODO*///INLINE UINT32 read_dword(void *address)
/*TODO*///{
/*TODO*///	if ((long)address & 3)
/*TODO*///	{
/*TODO*///#ifdef LSB_FIRST  /* little endian version */
/*TODO*///  		return ( *((UINT8 *)address) +
/*TODO*///				(*((UINT8 *)address+1) << 8)  +
/*TODO*///				(*((UINT8 *)address+2) << 16) +
/*TODO*///				(*((UINT8 *)address+3) << 24) );
/*TODO*///#else             /* big endian version */
/*TODO*///  		return ( *((UINT8 *)address+3) +
/*TODO*///				(*((UINT8 *)address+2) << 8)  +
/*TODO*///				(*((UINT8 *)address+1) << 16) +
/*TODO*///				(*((UINT8 *)address)   << 24) );
/*TODO*///#endif
/*TODO*///	}
/*TODO*///	else
/*TODO*///		return *(UINT32 *)address;
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void write_dword(void *address, UINT32 data)
/*TODO*///{
/*TODO*///  	if ((long)address & 3)
/*TODO*///	{
/*TODO*///#ifdef LSB_FIRST
/*TODO*///    		*((UINT8 *)address) =    data;
/*TODO*///    		*((UINT8 *)address+1) = (data >> 8);
/*TODO*///    		*((UINT8 *)address+2) = (data >> 16);
/*TODO*///    		*((UINT8 *)address+3) = (data >> 24);
/*TODO*///#else
/*TODO*///    		*((UINT8 *)address+3) =  data;
/*TODO*///    		*((UINT8 *)address+2) = (data >> 8);
/*TODO*///    		*((UINT8 *)address+1) = (data >> 16);
/*TODO*///    		*((UINT8 *)address)   = (data >> 24);
/*TODO*///#endif
/*TODO*///		return;
/*TODO*///  	}
/*TODO*///  	else
/*TODO*///		*(UINT32 *)address = data;
/*TODO*///}
/*TODO*///#else
/*TODO*///#define read_dword(address) *(int *)address
/*TODO*///#define write_dword(address,data) *(int *)address=data
/*TODO*///#endif
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*///INLINE int readbit(const UINT8 *src,int bitnum)
/*TODO*///{
/*TODO*///	return src[bitnum / 8] & (0x80 >> (bitnum % 8));
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///void decodechar(struct GfxElement *gfx,int num,const UINT8 *src,const struct GfxLayout *gl)
/*TODO*///{
/*TODO*///	int plane,x,y;
/*TODO*///	UINT8 *dp;
/*TODO*///	int baseoffs;
/*TODO*///	const UINT32 *xoffset,*yoffset;
/*TODO*///
/*TODO*///
/*TODO*///	if (Machine->orientation & ORIENTATION_SWAP_XY)
/*TODO*///	{
/*TODO*///		xoffset = gl->yoffset;
/*TODO*///		yoffset = gl->xoffset;
/*TODO*///	}
/*TODO*///	else
/*TODO*///	{
/*TODO*///		xoffset = gl->xoffset;
/*TODO*///		yoffset = gl->yoffset;
/*TODO*///	}
/*TODO*///
/*TODO*///	dp = gfx->gfxdata + num * gfx->char_modulo;
/*TODO*///	memset(dp,0,gfx->height * gfx->line_modulo);
/*TODO*///
/*TODO*///	baseoffs = num * gl->charincrement;
/*TODO*///
/*TODO*///	for (plane = 0;plane < gl->planes;plane++)
/*TODO*///	{
/*TODO*///		int shiftedbit = 1 << (gl->planes-1-plane);
/*TODO*///		int offs = baseoffs + gl->planeoffset[plane];
/*TODO*///
/*TODO*///		dp = gfx->gfxdata + num * gfx->char_modulo + (gfx->height-1) * gfx->line_modulo;
/*TODO*///
/*TODO*///
/*TODO*///#ifdef PREROTATE_GFX
/*TODO*///		y = gfx->height;
/*TODO*///		while (--y >= 0)
/*TODO*///		{
/*TODO*///			int yoffs;
/*TODO*///
/*TODO*///			yoffs = y;
/*TODO*///			if (Machine->orientation & ORIENTATION_FLIP_Y)
/*TODO*///				yoffs = gfx->height-1 - yoffs;
/*TODO*///
/*TODO*///			x = gfx->width;
/*TODO*///			while (--x >= 0)
/*TODO*///			{
/*TODO*///				int xoffs;
/*TODO*///
/*TODO*///				xoffs = x;
/*TODO*///				if (Machine->orientation & ORIENTATION_FLIP_X)
/*TODO*///					xoffs = gfx->width-1 - xoffs;
/*TODO*///
/*TODO*///				if (readbit(src,offs + xoffset[xoffs] + yoffset[yoffs]))
/*TODO*///					dp[x] |= shiftedbit;
/*TODO*///			}
/*TODO*///			dp -= gfx->line_modulo;
/*TODO*///		}
/*TODO*///#else
/*TODO*///		y = gfx->height;
/*TODO*///		while (--y >= 0)
/*TODO*///		{
/*TODO*///			int offs2 = offs + yoffset[y];
/*TODO*///
/*TODO*///			x = gfx->width;
/*TODO*///			while (--x >= 0)
/*TODO*///			{
/*TODO*///				if (readbit(src,offs2 + xoffset[x]))
/*TODO*///					dp[x] |= shiftedbit;
/*TODO*///			}
/*TODO*///			dp -= gfx->line_modulo;
/*TODO*///		}
/*TODO*///#endif
/*TODO*///	}
/*TODO*///
/*TODO*///
/*TODO*///	if (gfx->pen_usage)
/*TODO*///	{
/*TODO*///		/* fill the pen_usage array with info on the used pens */
/*TODO*///		gfx->pen_usage[num] = 0;
/*TODO*///
/*TODO*///		dp = gfx->gfxdata + num * gfx->char_modulo;
/*TODO*///		for (y = 0;y < gfx->height;y++)
/*TODO*///		{
/*TODO*///			for (x = 0;x < gfx->width;x++)
/*TODO*///			{
/*TODO*///				gfx->pen_usage[num] |= 1 << dp[x];
/*TODO*///			}
/*TODO*///			dp += gfx->line_modulo;
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///struct GfxElement *decodegfx(const UINT8 *src,const struct GfxLayout *gl)
/*TODO*///{
/*TODO*///	int c;
/*TODO*///	struct GfxElement *gfx;
/*TODO*///
/*TODO*///
/*TODO*///	if ((gfx = malloc(sizeof(struct GfxElement))) == 0)
/*TODO*///		return 0;
/*TODO*///	memset(gfx,0,sizeof(struct GfxElement));
/*TODO*///
/*TODO*///	if (Machine->orientation & ORIENTATION_SWAP_XY)
/*TODO*///	{
/*TODO*///		gfx->width = gl->height;
/*TODO*///		gfx->height = gl->width;
/*TODO*///	}
/*TODO*///	else
/*TODO*///	{
/*TODO*///		gfx->width = gl->width;
/*TODO*///		gfx->height = gl->height;
/*TODO*///	}
/*TODO*///
/*TODO*///	gfx->line_modulo = gfx->width;
/*TODO*///	gfx->char_modulo = gfx->line_modulo * gfx->height;
/*TODO*///	if ((gfx->gfxdata = malloc(gl->total * gfx->char_modulo * sizeof(UINT8))) == 0)
/*TODO*///	{
/*TODO*///		free(gfx);
/*TODO*///		return 0;
/*TODO*///	}
/*TODO*///
/*TODO*///	gfx->total_elements = gl->total;
/*TODO*///	gfx->color_granularity = 1 << gl->planes;
/*TODO*///
/*TODO*///	gfx->pen_usage = 0; /* need to make sure this is NULL if the next test fails) */
/*TODO*///	if (gfx->color_granularity <= 32)	/* can't handle more than 32 pens */
/*TODO*///		gfx->pen_usage = malloc(gfx->total_elements * sizeof(int));
/*TODO*///		/* no need to check for failure, the code can work without pen_usage */
/*TODO*///
/*TODO*///	for (c = 0;c < gl->total;c++)
/*TODO*///		decodechar(gfx,c,src,gl);
/*TODO*///
/*TODO*///	return gfx;
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///void freegfx(struct GfxElement *gfx)
/*TODO*///{
/*TODO*///	if (gfx)
/*TODO*///	{
/*TODO*///		free(gfx->pen_usage);
/*TODO*///		free(gfx->gfxdata);
/*TODO*///		free(gfx);
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*///INLINE void blockmove_NtoN_transpen_noremap8(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		UINT8 *dstdata,int dstmodulo,
/*TODO*///		int transpen)
/*TODO*///{
/*TODO*///	UINT8 *end;
/*TODO*///	int trans4;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	trans4 = transpen * 0x01010101;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (col != transpen) *dstdata = col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			if ((col4 = *(sd4++)) != trans4)
/*TODO*///			{
/*TODO*///				UINT32 xod4;
/*TODO*///
/*TODO*///				xod4 = col4 ^ trans4;
/*TODO*///				if( (xod4&0x000000ff) && (xod4&0x0000ff00) &&
/*TODO*///					(xod4&0x00ff0000) && (xod4&0xff000000) )
/*TODO*///				{
/*TODO*///					write_dword((UINT32 *)dstdata,col4);
/*TODO*///				}
/*TODO*///				else
/*TODO*///				{
/*TODO*///					if (xod4 & 0xff000000) dstdata[BL3] = col4 >> 24;
/*TODO*///					if (xod4 & 0x00ff0000) dstdata[BL2] = col4 >> 16;
/*TODO*///					if (xod4 & 0x0000ff00) dstdata[BL1] = col4 >>  8;
/*TODO*///					if (xod4 & 0x000000ff) dstdata[BL0] = col4;
/*TODO*///				}
/*TODO*///			}
/*TODO*///			dstdata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (col != transpen) *dstdata = col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///INLINE void blockmove_NtoN_transpen_noremap_flipx8(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		UINT8 *dstdata,int dstmodulo,
/*TODO*///		int transpen)
/*TODO*///{
/*TODO*///	UINT8 *end;
/*TODO*///	int trans4;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///	srcdata -= 3;
/*TODO*///
/*TODO*///	trans4 = transpen * 0x01010101;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (col != transpen) *dstdata = col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			if ((col4 = *(sd4--)) != trans4)
/*TODO*///			{
/*TODO*///				UINT32 xod4;
/*TODO*///
/*TODO*///				xod4 = col4 ^ trans4;
/*TODO*///				if (xod4 & 0x000000ff) dstdata[BL3] = col4;
/*TODO*///				if (xod4 & 0x0000ff00) dstdata[BL2] = col4 >>  8;
/*TODO*///				if (xod4 & 0x00ff0000) dstdata[BL1] = col4 >> 16;
/*TODO*///				if (xod4 & 0xff000000) dstdata[BL0] = col4 >> 24;
/*TODO*///			}
/*TODO*///			dstdata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (col != transpen) *dstdata = col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void blockmove_NtoN_transpen_noremap16(
/*TODO*///		const UINT16 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		UINT16 *dstdata,int dstmodulo,
/*TODO*///		int transpen)
/*TODO*///{
/*TODO*///	UINT16 *end;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (col != transpen) *dstdata = col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///INLINE void blockmove_NtoN_transpen_noremap_flipx16(
/*TODO*///		const UINT16 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		UINT16 *dstdata,int dstmodulo,
/*TODO*///		int transpen)
/*TODO*///{
/*TODO*///	UINT16 *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata--);
/*TODO*///			if (col != transpen) *dstdata = col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///#define DATA_TYPE UINT8
/*TODO*///#define DECLARE(function,args,body) INLINE void function##8 args body
/*TODO*///#define BLOCKMOVE(function,flipx,args) \
/*TODO*///	if (flipx) blockmove_##function##_flipx##8 args ; \
/*TODO*///	else blockmove_##function##8 args
/*TODO*///#include "drawgfx.c"
/*TODO*///#undef DATA_TYPE
/*TODO*///#undef DECLARE
/*TODO*///#undef BLOCKMOVE
/*TODO*///
/*TODO*///#define DATA_TYPE UINT16
/*TODO*///#define DECLARE(function,args,body) INLINE void function##16 args body
/*TODO*///#define BLOCKMOVE(function,flipx,args) \
/*TODO*///	if (flipx) blockmove_##function##_flipx##16 args ; \
/*TODO*///	else blockmove_##function##16 args
/*TODO*///#include "drawgfx.c"
/*TODO*///#undef DATA_TYPE
/*TODO*///#undef DECLARE
/*TODO*///#undef BLOCKMOVE
/*TODO*///
/*TODO*///
/*TODO*////***************************************************************************
/*TODO*///
/*TODO*///  Draw graphic elements in the specified bitmap.
/*TODO*///
/*TODO*///  transparency == TRANSPARENCY_NONE - no transparency.
/*TODO*///  transparency == TRANSPARENCY_PEN - bits whose _original_ value is == transparent_color
/*TODO*///                                     are transparent. This is the most common kind of
/*TODO*///									 transparency.
/*TODO*///  transparency == TRANSPARENCY_PENS - as above, but transparent_color is a mask of
/*TODO*///  									 transparent pens.
/*TODO*///  transparency == TRANSPARENCY_COLOR - bits whose _remapped_ palette index (taken from
/*TODO*///                                     Machine->game_colortable) is == transparent_color
/*TODO*///  transparency == TRANSPARENCY_THROUGH - if the _destination_ pixel is == transparent_color,
/*TODO*///                                     the source pixel is drawn over it. This is used by
/*TODO*///									 e.g. Jr. Pac Man to draw the sprites when the background
/*TODO*///									 has priority over them.
/*TODO*///
/*TODO*///  transparency == TRANSPARENCY_PEN_TABLE - the transparency condition is same as TRANSPARENCY_PEN
/*TODO*///					A special drawing is done according to gfx_drawmode_table[source pixel].
/*TODO*///					DRAWMODE_NONE      transparent
/*TODO*///					DRAWMODE_SOURCE    normal, draw source pixel.
/*TODO*///					DRAWMODE_SHADOW    destination is changed through palette_shadow_table[]
/*TODO*///
/*TODO*///***************************************************************************/
/*TODO*///
/*TODO*///INLINE void common_drawgfx(struct osd_bitmap *dest,const struct GfxElement *gfx,
/*TODO*///		unsigned int code,unsigned int color,int flipx,int flipy,int sx,int sy,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color,
/*TODO*///		struct osd_bitmap *pri_buffer,UINT32 pri_mask)
/*TODO*///{
/*TODO*///	struct rectangle myclip;
/*TODO*///
/*TODO*///	if (!gfx)
/*TODO*///	{
/*TODO*///		usrintf_showmessage("drawgfx() gfx == 0");
/*TODO*///		return;
/*TODO*///	}
/*TODO*///	if (!gfx->colortable && !is_raw[transparency])
/*TODO*///	{
/*TODO*///		usrintf_showmessage("drawgfx() gfx->colortable == 0");
/*TODO*///		return;
/*TODO*///	}
/*TODO*///
/*TODO*///	code %= gfx->total_elements;
/*TODO*///	if (!is_raw[transparency])
/*TODO*///		color %= gfx->total_colors;
/*TODO*///
/*TODO*///	if (gfx->pen_usage && (transparency == TRANSPARENCY_PEN || transparency == TRANSPARENCY_PENS))
/*TODO*///	{
/*TODO*///		int transmask = 0;
/*TODO*///
/*TODO*///		if (transparency == TRANSPARENCY_PEN)
/*TODO*///		{
/*TODO*///			transmask = 1 << transparent_color;
/*TODO*///		}
/*TODO*///		else	/* transparency == TRANSPARENCY_PENS */
/*TODO*///		{
/*TODO*///			transmask = transparent_color;
/*TODO*///		}
/*TODO*///
/*TODO*///		if ((gfx->pen_usage[code] & ~transmask) == 0)
/*TODO*///			/* character is totally transparent, no need to draw */
/*TODO*///			return;
/*TODO*///		else if ((gfx->pen_usage[code] & transmask) == 0)
/*TODO*///			/* character is totally opaque, can disable transparency */
/*TODO*///			transparency = TRANSPARENCY_NONE;
/*TODO*///	}
/*TODO*///
/*TODO*///	if (Machine->orientation & ORIENTATION_SWAP_XY)
/*TODO*///	{
/*TODO*///		int temp;
/*TODO*///
/*TODO*///		temp = sx;
/*TODO*///		sx = sy;
/*TODO*///		sy = temp;
/*TODO*///
/*TODO*///		temp = flipx;
/*TODO*///		flipx = flipy;
/*TODO*///		flipy = temp;
/*TODO*///
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			/* clip and myclip might be the same, so we need a temporary storage */
/*TODO*///			temp = clip->min_x;
/*TODO*///			myclip.min_x = clip->min_y;
/*TODO*///			myclip.min_y = temp;
/*TODO*///			temp = clip->max_x;
/*TODO*///			myclip.max_x = clip->max_y;
/*TODO*///			myclip.max_y = temp;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	if (Machine->orientation & ORIENTATION_FLIP_X)
/*TODO*///	{
/*TODO*///		sx = dest->width - gfx->width - sx;
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			int temp;
/*TODO*///
/*TODO*///
/*TODO*///			/* clip and myclip might be the same, so we need a temporary storage */
/*TODO*///			temp = clip->min_x;
/*TODO*///			myclip.min_x = dest->width-1 - clip->max_x;
/*TODO*///			myclip.max_x = dest->width-1 - temp;
/*TODO*///			myclip.min_y = clip->min_y;
/*TODO*///			myclip.max_y = clip->max_y;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///#ifndef PREROTATE_GFX
/*TODO*///		flipx = !flipx;
/*TODO*///#endif
/*TODO*///	}
/*TODO*///	if (Machine->orientation & ORIENTATION_FLIP_Y)
/*TODO*///	{
/*TODO*///		sy = dest->height - gfx->height - sy;
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			int temp;
/*TODO*///
/*TODO*///
/*TODO*///			myclip.min_x = clip->min_x;
/*TODO*///			myclip.max_x = clip->max_x;
/*TODO*///			/* clip and myclip might be the same, so we need a temporary storage */
/*TODO*///			temp = clip->min_y;
/*TODO*///			myclip.min_y = dest->height-1 - clip->max_y;
/*TODO*///			myclip.max_y = dest->height-1 - temp;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///#ifndef PREROTATE_GFX
/*TODO*///		flipy = !flipy;
/*TODO*///#endif
/*TODO*///	}
/*TODO*///
/*TODO*///	if (dest->depth != 16)
/*TODO*///		drawgfx_core8(dest,gfx,code,color,flipx,flipy,sx,sy,clip,transparency,transparent_color,pri_buffer,pri_mask);
/*TODO*///	else
/*TODO*///		drawgfx_core16(dest,gfx,code,color,flipx,flipy,sx,sy,clip,transparency,transparent_color,pri_buffer,pri_mask);
/*TODO*///}
/*TODO*///
/*TODO*///void drawgfx(struct osd_bitmap *dest,const struct GfxElement *gfx,
/*TODO*///		unsigned int code,unsigned int color,int flipx,int flipy,int sx,int sy,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color)
/*TODO*///{
/*TODO*///	profiler_mark(PROFILER_DRAWGFX);
/*TODO*///	common_drawgfx(dest,gfx,code,color,flipx,flipy,sx,sy,clip,transparency,transparent_color,NULL,0);
/*TODO*///	profiler_mark(PROFILER_END);
/*TODO*///}
/*TODO*///
/*TODO*///void pdrawgfx(struct osd_bitmap *dest,const struct GfxElement *gfx,
/*TODO*///		unsigned int code,unsigned int color,int flipx,int flipy,int sx,int sy,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color,UINT32 priority_mask)
/*TODO*///{
/*TODO*///	profiler_mark(PROFILER_DRAWGFX);
/*TODO*///	common_drawgfx(dest,gfx,code,color,flipx,flipy,sx,sy,clip,transparency,transparent_color,priority_bitmap,priority_mask);
/*TODO*///	profiler_mark(PROFILER_END);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*////***************************************************************************
/*TODO*///
/*TODO*///  Use drawgfx() to copy a bitmap onto another at the given position.
/*TODO*///  This function will very likely change in the future.
/*TODO*///
/*TODO*///***************************************************************************/
/*TODO*///void copybitmap(struct osd_bitmap *dest,struct osd_bitmap *src,int flipx,int flipy,int sx,int sy,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color)
/*TODO*///{
/*TODO*///	/* translate to proper transparency here */
/*TODO*///	if (transparency == TRANSPARENCY_NONE)
/*TODO*///		transparency = TRANSPARENCY_NONE_RAW;
/*TODO*///	else if (transparency == TRANSPARENCY_PEN)
/*TODO*///		transparency = TRANSPARENCY_PEN_RAW;
/*TODO*///	else if (transparency == TRANSPARENCY_COLOR)
/*TODO*///	{
/*TODO*///		transparent_color = Machine->pens[transparent_color];
/*TODO*///		transparency = TRANSPARENCY_PEN_RAW;
/*TODO*///	}
/*TODO*///	else if (transparency == TRANSPARENCY_THROUGH)
/*TODO*///		transparency = TRANSPARENCY_THROUGH_RAW;
/*TODO*///
/*TODO*///	copybitmap_remap(dest,src,flipx,flipy,sx,sy,clip,transparency,transparent_color);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///void copybitmap_remap(struct osd_bitmap *dest,struct osd_bitmap *src,int flipx,int flipy,int sx,int sy,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color)
/*TODO*///{
/*TODO*///	struct rectangle myclip;
/*TODO*///
/*TODO*///
/*TODO*///	profiler_mark(PROFILER_COPYBITMAP);
/*TODO*///
/*TODO*///	if (Machine->orientation & ORIENTATION_SWAP_XY)
/*TODO*///	{
/*TODO*///		int temp;
/*TODO*///
/*TODO*///		temp = sx;
/*TODO*///		sx = sy;
/*TODO*///		sy = temp;
/*TODO*///
/*TODO*///		temp = flipx;
/*TODO*///		flipx = flipy;
/*TODO*///		flipy = temp;
/*TODO*///
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			/* clip and myclip might be the same, so we need a temporary storage */
/*TODO*///			temp = clip->min_x;
/*TODO*///			myclip.min_x = clip->min_y;
/*TODO*///			myclip.min_y = temp;
/*TODO*///			temp = clip->max_x;
/*TODO*///			myclip.max_x = clip->max_y;
/*TODO*///			myclip.max_y = temp;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	if (Machine->orientation & ORIENTATION_FLIP_X)
/*TODO*///	{
/*TODO*///		sx = dest->width - src->width - sx;
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			int temp;
/*TODO*///
/*TODO*///
/*TODO*///			/* clip and myclip might be the same, so we need a temporary storage */
/*TODO*///			temp = clip->min_x;
/*TODO*///			myclip.min_x = dest->width-1 - clip->max_x;
/*TODO*///			myclip.max_x = dest->width-1 - temp;
/*TODO*///			myclip.min_y = clip->min_y;
/*TODO*///			myclip.max_y = clip->max_y;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	if (Machine->orientation & ORIENTATION_FLIP_Y)
/*TODO*///	{
/*TODO*///		sy = dest->height - src->height - sy;
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			int temp;
/*TODO*///
/*TODO*///
/*TODO*///			myclip.min_x = clip->min_x;
/*TODO*///			myclip.max_x = clip->max_x;
/*TODO*///			/* clip and myclip might be the same, so we need a temporary storage */
/*TODO*///			temp = clip->min_y;
/*TODO*///			myclip.min_y = dest->height-1 - clip->max_y;
/*TODO*///			myclip.max_y = dest->height-1 - temp;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///	}
/*TODO*///
/*TODO*///	if (dest->depth != 16)
/*TODO*///		copybitmap_core8(dest,src,flipx,flipy,sx,sy,clip,transparency,transparent_color);
/*TODO*///	else
/*TODO*///		copybitmap_core16(dest,src,flipx,flipy,sx,sy,clip,transparency,transparent_color);
/*TODO*///
/*TODO*///	profiler_mark(PROFILER_END);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*////***************************************************************************
/*TODO*///
/*TODO*///  Copy a bitmap onto another with scroll and wraparound.
/*TODO*///  This function supports multiple independently scrolling rows/columns.
/*TODO*///  "rows" is the number of indepentently scrolling rows. "rowscroll" is an
/*TODO*///  array of integers telling how much to scroll each row. Same thing for
/*TODO*///  "cols" and "colscroll".
/*TODO*///  If the bitmap cannot scroll in one direction, set rows or columns to 0.
/*TODO*///  If the bitmap scrolls as a whole, set rows and/or cols to 1.
/*TODO*///  Bidirectional scrolling is, of course, supported only if the bitmap
/*TODO*///  scrolls as a whole in at least one direction.
/*TODO*///
/*TODO*///***************************************************************************/
/*TODO*///void copyscrollbitmap(struct osd_bitmap *dest,struct osd_bitmap *src,
/*TODO*///		int rows,const int *rowscroll,int cols,const int *colscroll,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color)
/*TODO*///{
/*TODO*///	/* translate to proper transparency here */
/*TODO*///	if (transparency == TRANSPARENCY_NONE)
/*TODO*///		transparency = TRANSPARENCY_NONE_RAW;
/*TODO*///	else if (transparency == TRANSPARENCY_PEN)
/*TODO*///		transparency = TRANSPARENCY_PEN_RAW;
/*TODO*///	else if (transparency == TRANSPARENCY_COLOR)
/*TODO*///	{
/*TODO*///		transparent_color = Machine->pens[transparent_color];
/*TODO*///		transparency = TRANSPARENCY_PEN_RAW;
/*TODO*///	}
/*TODO*///	else if (transparency == TRANSPARENCY_THROUGH)
/*TODO*///		transparency = TRANSPARENCY_THROUGH_RAW;
/*TODO*///
/*TODO*///	copyscrollbitmap_remap(dest,src,rows,rowscroll,cols,colscroll,clip,transparency,transparent_color);
/*TODO*///}
/*TODO*///
/*TODO*///void copyscrollbitmap_remap(struct osd_bitmap *dest,struct osd_bitmap *src,
/*TODO*///		int rows,const int *rowscroll,int cols,const int *colscroll,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color)
/*TODO*///{
/*TODO*///	int srcwidth,srcheight,destwidth,destheight;
/*TODO*///	struct rectangle orig_clip;
/*TODO*///
/*TODO*///
/*TODO*///	if (clip)
/*TODO*///	{
/*TODO*///		orig_clip.min_x = clip->min_x;
/*TODO*///		orig_clip.max_x = clip->max_x;
/*TODO*///		orig_clip.min_y = clip->min_y;
/*TODO*///		orig_clip.max_y = clip->max_y;
/*TODO*///	}
/*TODO*///	else
/*TODO*///	{
/*TODO*///		orig_clip.min_x = 0;
/*TODO*///		orig_clip.max_x = dest->width-1;
/*TODO*///		orig_clip.min_y = 0;
/*TODO*///		orig_clip.max_y = dest->height-1;
/*TODO*///	}
/*TODO*///	clip = &orig_clip;
/*TODO*///
/*TODO*///	if (rows == 0 && cols == 0)
/*TODO*///	{
/*TODO*///		copybitmap(dest,src,0,0,0,0,clip,transparency,transparent_color);
/*TODO*///		return;
/*TODO*///	}
/*TODO*///
/*TODO*///	profiler_mark(PROFILER_COPYBITMAP);
/*TODO*///
/*TODO*///	if (Machine->orientation & ORIENTATION_SWAP_XY)
/*TODO*///	{
/*TODO*///		srcwidth = src->height;
/*TODO*///		srcheight = src->width;
/*TODO*///		destwidth = dest->height;
/*TODO*///		destheight = dest->width;
/*TODO*///	}
/*TODO*///	else
/*TODO*///	{
/*TODO*///		srcwidth = src->width;
/*TODO*///		srcheight = src->height;
/*TODO*///		destwidth = dest->width;
/*TODO*///		destheight = dest->height;
/*TODO*///	}
/*TODO*///
/*TODO*///	if (rows == 0)
/*TODO*///	{
/*TODO*///		/* scrolling columns */
/*TODO*///		int col,colwidth;
/*TODO*///		struct rectangle myclip;
/*TODO*///
/*TODO*///
/*TODO*///		colwidth = srcwidth / cols;
/*TODO*///
/*TODO*///		myclip.min_y = clip->min_y;
/*TODO*///		myclip.max_y = clip->max_y;
/*TODO*///
/*TODO*///		col = 0;
/*TODO*///		while (col < cols)
/*TODO*///		{
/*TODO*///			int cons,scroll;
/*TODO*///
/*TODO*///
/*TODO*///			/* count consecutive columns scrolled by the same amount */
/*TODO*///			scroll = colscroll[col];
/*TODO*///			cons = 1;
/*TODO*///			while (col + cons < cols &&	colscroll[col + cons] == scroll)
/*TODO*///				cons++;
/*TODO*///
/*TODO*///			if (scroll < 0) scroll = srcheight - (-scroll) % srcheight;
/*TODO*///			else scroll %= srcheight;
/*TODO*///
/*TODO*///			myclip.min_x = col * colwidth;
/*TODO*///			if (myclip.min_x < clip->min_x) myclip.min_x = clip->min_x;
/*TODO*///			myclip.max_x = (col + cons) * colwidth - 1;
/*TODO*///			if (myclip.max_x > clip->max_x) myclip.max_x = clip->max_x;
/*TODO*///
/*TODO*///			copybitmap(dest,src,0,0,0,scroll,&myclip,transparency,transparent_color);
/*TODO*///			copybitmap(dest,src,0,0,0,scroll - srcheight,&myclip,transparency,transparent_color);
/*TODO*///
/*TODO*///			col += cons;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else if (cols == 0)
/*TODO*///	{
/*TODO*///		/* scrolling rows */
/*TODO*///		int row,rowheight;
/*TODO*///		struct rectangle myclip;
/*TODO*///
/*TODO*///
/*TODO*///		rowheight = srcheight / rows;
/*TODO*///
/*TODO*///		myclip.min_x = clip->min_x;
/*TODO*///		myclip.max_x = clip->max_x;
/*TODO*///
/*TODO*///		row = 0;
/*TODO*///		while (row < rows)
/*TODO*///		{
/*TODO*///			int cons,scroll;
/*TODO*///
/*TODO*///
/*TODO*///			/* count consecutive rows scrolled by the same amount */
/*TODO*///			scroll = rowscroll[row];
/*TODO*///			cons = 1;
/*TODO*///			while (row + cons < rows &&	rowscroll[row + cons] == scroll)
/*TODO*///				cons++;
/*TODO*///
/*TODO*///			if (scroll < 0) scroll = srcwidth - (-scroll) % srcwidth;
/*TODO*///			else scroll %= srcwidth;
/*TODO*///
/*TODO*///			myclip.min_y = row * rowheight;
/*TODO*///			if (myclip.min_y < clip->min_y) myclip.min_y = clip->min_y;
/*TODO*///			myclip.max_y = (row + cons) * rowheight - 1;
/*TODO*///			if (myclip.max_y > clip->max_y) myclip.max_y = clip->max_y;
/*TODO*///
/*TODO*///			copybitmap(dest,src,0,0,scroll,0,&myclip,transparency,transparent_color);
/*TODO*///			copybitmap(dest,src,0,0,scroll - srcwidth,0,&myclip,transparency,transparent_color);
/*TODO*///
/*TODO*///			row += cons;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else if (rows == 1 && cols == 1)
/*TODO*///	{
/*TODO*///		/* XY scrolling playfield */
/*TODO*///		int scrollx,scrolly,sx,sy;
/*TODO*///
/*TODO*///
/*TODO*///		if (rowscroll[0] < 0) scrollx = srcwidth - (-rowscroll[0]) % srcwidth;
/*TODO*///		else scrollx = rowscroll[0] % srcwidth;
/*TODO*///
/*TODO*///		if (colscroll[0] < 0) scrolly = srcheight - (-colscroll[0]) % srcheight;
/*TODO*///		else scrolly = colscroll[0] % srcheight;
/*TODO*///
/*TODO*///		for (sx = scrollx - srcwidth;sx < destwidth;sx += srcwidth)
/*TODO*///			for (sy = scrolly - srcheight;sy < destheight;sy += srcheight)
/*TODO*///				copybitmap(dest,src,0,0,sx,sy,clip,transparency,transparent_color);
/*TODO*///	}
/*TODO*///	else if (rows == 1)
/*TODO*///	{
/*TODO*///		/* scrolling columns + horizontal scroll */
/*TODO*///		int col,colwidth;
/*TODO*///		int scrollx;
/*TODO*///		struct rectangle myclip;
/*TODO*///
/*TODO*///
/*TODO*///		if (rowscroll[0] < 0) scrollx = srcwidth - (-rowscroll[0]) % srcwidth;
/*TODO*///		else scrollx = rowscroll[0] % srcwidth;
/*TODO*///
/*TODO*///		colwidth = srcwidth / cols;
/*TODO*///
/*TODO*///		myclip.min_y = clip->min_y;
/*TODO*///		myclip.max_y = clip->max_y;
/*TODO*///
/*TODO*///		col = 0;
/*TODO*///		while (col < cols)
/*TODO*///		{
/*TODO*///			int cons,scroll;
/*TODO*///
/*TODO*///
/*TODO*///			/* count consecutive columns scrolled by the same amount */
/*TODO*///			scroll = colscroll[col];
/*TODO*///			cons = 1;
/*TODO*///			while (col + cons < cols &&	colscroll[col + cons] == scroll)
/*TODO*///				cons++;
/*TODO*///
/*TODO*///			if (scroll < 0) scroll = srcheight - (-scroll) % srcheight;
/*TODO*///			else scroll %= srcheight;
/*TODO*///
/*TODO*///			myclip.min_x = col * colwidth + scrollx;
/*TODO*///			if (myclip.min_x < clip->min_x) myclip.min_x = clip->min_x;
/*TODO*///			myclip.max_x = (col + cons) * colwidth - 1 + scrollx;
/*TODO*///			if (myclip.max_x > clip->max_x) myclip.max_x = clip->max_x;
/*TODO*///
/*TODO*///			copybitmap(dest,src,0,0,scrollx,scroll,&myclip,transparency,transparent_color);
/*TODO*///			copybitmap(dest,src,0,0,scrollx,scroll - srcheight,&myclip,transparency,transparent_color);
/*TODO*///
/*TODO*///			myclip.min_x = col * colwidth + scrollx - srcwidth;
/*TODO*///			if (myclip.min_x < clip->min_x) myclip.min_x = clip->min_x;
/*TODO*///			myclip.max_x = (col + cons) * colwidth - 1 + scrollx - srcwidth;
/*TODO*///			if (myclip.max_x > clip->max_x) myclip.max_x = clip->max_x;
/*TODO*///
/*TODO*///			copybitmap(dest,src,0,0,scrollx - srcwidth,scroll,&myclip,transparency,transparent_color);
/*TODO*///			copybitmap(dest,src,0,0,scrollx - srcwidth,scroll - srcheight,&myclip,transparency,transparent_color);
/*TODO*///
/*TODO*///			col += cons;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else if (cols == 1)
/*TODO*///	{
/*TODO*///		/* scrolling rows + vertical scroll */
/*TODO*///		int row,rowheight;
/*TODO*///		int scrolly;
/*TODO*///		struct rectangle myclip;
/*TODO*///
/*TODO*///
/*TODO*///		if (colscroll[0] < 0) scrolly = srcheight - (-colscroll[0]) % srcheight;
/*TODO*///		else scrolly = colscroll[0] % srcheight;
/*TODO*///
/*TODO*///		rowheight = srcheight / rows;
/*TODO*///
/*TODO*///		myclip.min_x = clip->min_x;
/*TODO*///		myclip.max_x = clip->max_x;
/*TODO*///
/*TODO*///		row = 0;
/*TODO*///		while (row < rows)
/*TODO*///		{
/*TODO*///			int cons,scroll;
/*TODO*///
/*TODO*///
/*TODO*///			/* count consecutive rows scrolled by the same amount */
/*TODO*///			scroll = rowscroll[row];
/*TODO*///			cons = 1;
/*TODO*///			while (row + cons < rows &&	rowscroll[row + cons] == scroll)
/*TODO*///				cons++;
/*TODO*///
/*TODO*///			if (scroll < 0) scroll = srcwidth - (-scroll) % srcwidth;
/*TODO*///			else scroll %= srcwidth;
/*TODO*///
/*TODO*///			myclip.min_y = row * rowheight + scrolly;
/*TODO*///			if (myclip.min_y < clip->min_y) myclip.min_y = clip->min_y;
/*TODO*///			myclip.max_y = (row + cons) * rowheight - 1 + scrolly;
/*TODO*///			if (myclip.max_y > clip->max_y) myclip.max_y = clip->max_y;
/*TODO*///
/*TODO*///			copybitmap(dest,src,0,0,scroll,scrolly,&myclip,transparency,transparent_color);
/*TODO*///			copybitmap(dest,src,0,0,scroll - srcwidth,scrolly,&myclip,transparency,transparent_color);
/*TODO*///
/*TODO*///			myclip.min_y = row * rowheight + scrolly - srcheight;
/*TODO*///			if (myclip.min_y < clip->min_y) myclip.min_y = clip->min_y;
/*TODO*///			myclip.max_y = (row + cons) * rowheight - 1 + scrolly - srcheight;
/*TODO*///			if (myclip.max_y > clip->max_y) myclip.max_y = clip->max_y;
/*TODO*///
/*TODO*///			copybitmap(dest,src,0,0,scroll,scrolly - srcheight,&myclip,transparency,transparent_color);
/*TODO*///			copybitmap(dest,src,0,0,scroll - srcwidth,scrolly - srcheight,&myclip,transparency,transparent_color);
/*TODO*///
/*TODO*///			row += cons;
/*TODO*///		}
/*TODO*///	}
/*TODO*///
/*TODO*///	profiler_mark(PROFILER_END);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*////* notes:
/*TODO*///   - startx and starty MUST be UINT32 for calculations to work correctly
/*TODO*///   - srcbitmap->width and height are assumed to be a power of 2 to speed up wraparound
/*TODO*///   */
/*TODO*///void copyrozbitmap(struct osd_bitmap *dest,struct osd_bitmap *src,
/*TODO*///		UINT32 startx,UINT32 starty,int incxx,int incxy,int incyx,int incyy,int wraparound,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color,UINT32 priority)
/*TODO*///{
/*TODO*///	profiler_mark(PROFILER_COPYBITMAP);
/*TODO*///
/*TODO*///	/* cheat, the core doesn't support TRANSPARENCY_NONE yet */
/*TODO*///	if (transparency == TRANSPARENCY_NONE)
/*TODO*///	{
/*TODO*///		transparency = TRANSPARENCY_PEN;
/*TODO*///		transparent_color = -1;
/*TODO*///	}
/*TODO*///
/*TODO*///	/* if necessary, remap the transparent color */
/*TODO*///	if (transparency == TRANSPARENCY_COLOR)
/*TODO*///	{
/*TODO*///		transparency = TRANSPARENCY_PEN;
/*TODO*///		transparent_color = Machine->pens[transparent_color];
/*TODO*///	}
/*TODO*///
/*TODO*///	if (transparency != TRANSPARENCY_PEN)
/*TODO*///	{
/*TODO*///		usrintf_showmessage("copyrozbitmap unsupported trans %02x",transparency);
/*TODO*///		return;
/*TODO*///	}
/*TODO*///
/*TODO*///	if (dest->depth != 16)
/*TODO*///		copyrozbitmap_core8(dest,src,startx,starty,incxx,incxy,incyx,incyy,wraparound,clip,transparency,transparent_color,priority);
/*TODO*///	else
/*TODO*///		copyrozbitmap_core16(dest,src,startx,starty,incxx,incxy,incyx,incyy,wraparound,clip,transparency,transparent_color,priority);
/*TODO*///
/*TODO*///	profiler_mark(PROFILER_END);
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///
/*TODO*////* fill a bitmap using the specified pen */
/*TODO*///void fillbitmap(struct osd_bitmap *dest,int pen,const struct rectangle *clip)
/*TODO*///{
/*TODO*///	int sx,sy,ex,ey,y;
/*TODO*///	struct rectangle myclip;
/*TODO*///
/*TODO*///
/*TODO*///	if (Machine->orientation & ORIENTATION_SWAP_XY)
/*TODO*///	{
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			myclip.min_x = clip->min_y;
/*TODO*///			myclip.max_x = clip->max_y;
/*TODO*///			myclip.min_y = clip->min_x;
/*TODO*///			myclip.max_y = clip->max_x;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	if (Machine->orientation & ORIENTATION_FLIP_X)
/*TODO*///	{
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			int temp;
/*TODO*///
/*TODO*///
/*TODO*///			temp = clip->min_x;
/*TODO*///			myclip.min_x = dest->width-1 - clip->max_x;
/*TODO*///			myclip.max_x = dest->width-1 - temp;
/*TODO*///			myclip.min_y = clip->min_y;
/*TODO*///			myclip.max_y = clip->max_y;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	if (Machine->orientation & ORIENTATION_FLIP_Y)
/*TODO*///	{
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			int temp;
/*TODO*///
/*TODO*///
/*TODO*///			myclip.min_x = clip->min_x;
/*TODO*///			myclip.max_x = clip->max_x;
/*TODO*///			temp = clip->min_y;
/*TODO*///			myclip.min_y = dest->height-1 - clip->max_y;
/*TODO*///			myclip.max_y = dest->height-1 - temp;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///	}
/*TODO*///
/*TODO*///
/*TODO*///	sx = 0;
/*TODO*///	ex = dest->width - 1;
/*TODO*///	sy = 0;
/*TODO*///	ey = dest->height - 1;
/*TODO*///
/*TODO*///	if (clip && sx < clip->min_x) sx = clip->min_x;
/*TODO*///	if (clip && ex > clip->max_x) ex = clip->max_x;
/*TODO*///	if (sx > ex) return;
/*TODO*///	if (clip && sy < clip->min_y) sy = clip->min_y;
/*TODO*///	if (clip && ey > clip->max_y) ey = clip->max_y;
/*TODO*///	if (sy > ey) return;
/*TODO*///
/*TODO*///	if (Machine->drv->video_attributes & VIDEO_SUPPORTS_DIRTY)
/*TODO*///		osd_mark_dirty (sx,sy,ex,ey,0);	/* ASG 971011 */
/*TODO*///
/*TODO*///	/* ASG 980211 */
/*TODO*///	if (dest->depth == 16)
/*TODO*///	{
/*TODO*///		if ((pen >> 8) == (pen & 0xff))
/*TODO*///		{
/*TODO*///			for (y = sy;y <= ey;y++)
/*TODO*///				memset(&dest->line[y][sx*2],pen&0xff,(ex-sx+1)*2);
/*TODO*///		}
/*TODO*///		else
/*TODO*///		{
/*TODO*///			UINT16 *sp = (UINT16 *)dest->line[sy];
/*TODO*///			int x;
/*TODO*///
/*TODO*///			for (x = sx;x <= ex;x++)
/*TODO*///				sp[x] = pen;
/*TODO*///			sp+=sx;
/*TODO*///			for (y = sy+1;y <= ey;y++)
/*TODO*///				memcpy(&dest->line[y][sx*2],sp,(ex-sx+1)*2);
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else
/*TODO*///	{
/*TODO*///		for (y = sy;y <= ey;y++)
/*TODO*///			memset(&dest->line[y][sx],pen,ex-sx+1);
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///
/*TODO*///INLINE void common_drawgfxzoom( struct osd_bitmap *dest_bmp,const struct GfxElement *gfx,
/*TODO*///		unsigned int code,unsigned int color,int flipx,int flipy,int sx,int sy,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color,
/*TODO*///		int scalex, int scaley,struct osd_bitmap *pri_buffer,UINT32 pri_mask)
/*TODO*///{
/*TODO*///	struct rectangle myclip;
/*TODO*///
/*TODO*///
/*TODO*///	if (!scalex || !scaley) return;
/*TODO*///
/*TODO*///	if (scalex == 0x10000 && scaley == 0x10000)
/*TODO*///	{
/*TODO*///		common_drawgfx(dest_bmp,gfx,code,color,flipx,flipy,sx,sy,clip,transparency,transparent_color,pri_buffer,pri_mask);
/*TODO*///		return;
/*TODO*///	}
/*TODO*///
/*TODO*///
/*TODO*///	pri_mask |= (1<<31);
/*TODO*///
/*TODO*///	if (transparency != TRANSPARENCY_PEN && transparency != TRANSPARENCY_PEN_RAW
/*TODO*///			&& transparency != TRANSPARENCY_PENS && transparency != TRANSPARENCY_COLOR
/*TODO*///			&& transparency != TRANSPARENCY_PEN_TABLE && transparency != TRANSPARENCY_PEN_TABLE_RAW)
/*TODO*///	{
/*TODO*///		usrintf_showmessage("drawgfxzoom unsupported trans %02x",transparency);
/*TODO*///		return;
/*TODO*///	}
/*TODO*///
/*TODO*///	if (transparency == TRANSPARENCY_COLOR)
/*TODO*///		transparent_color = Machine->pens[transparent_color];
/*TODO*///
/*TODO*///
/*TODO*///	/*
/*TODO*///	scalex and scaley are 16.16 fixed point numbers
/*TODO*///	1<<15 : shrink to 50%
/*TODO*///	1<<16 : uniform scale
/*TODO*///	1<<17 : double to 200%
/*TODO*///	*/
/*TODO*///
/*TODO*///
/*TODO*///	if (Machine->orientation & ORIENTATION_SWAP_XY)
/*TODO*///	{
/*TODO*///		int temp;
/*TODO*///
/*TODO*///		temp = sx;
/*TODO*///		sx = sy;
/*TODO*///		sy = temp;
/*TODO*///
/*TODO*///		temp = flipx;
/*TODO*///		flipx = flipy;
/*TODO*///		flipy = temp;
/*TODO*///
/*TODO*///		temp = scalex;
/*TODO*///		scalex = scaley;
/*TODO*///		scaley = temp;
/*TODO*///
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			/* clip and myclip might be the same, so we need a temporary storage */
/*TODO*///			temp = clip->min_x;
/*TODO*///			myclip.min_x = clip->min_y;
/*TODO*///			myclip.min_y = temp;
/*TODO*///			temp = clip->max_x;
/*TODO*///			myclip.max_x = clip->max_y;
/*TODO*///			myclip.max_y = temp;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	if (Machine->orientation & ORIENTATION_FLIP_X)
/*TODO*///	{
/*TODO*///		sx = dest_bmp->width - ((gfx->width * scalex + 0x7fff) >> 16) - sx;
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			int temp;
/*TODO*///
/*TODO*///
/*TODO*///			/* clip and myclip might be the same, so we need a temporary storage */
/*TODO*///			temp = clip->min_x;
/*TODO*///			myclip.min_x = dest_bmp->width-1 - clip->max_x;
/*TODO*///			myclip.max_x = dest_bmp->width-1 - temp;
/*TODO*///			myclip.min_y = clip->min_y;
/*TODO*///			myclip.max_y = clip->max_y;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///#ifndef PREROTATE_GFX
/*TODO*///		flipx = !flipx;
/*TODO*///#endif
/*TODO*///	}
/*TODO*///	if (Machine->orientation & ORIENTATION_FLIP_Y)
/*TODO*///	{
/*TODO*///		sy = dest_bmp->height - ((gfx->height * scaley + 0x7fff) >> 16) - sy;
/*TODO*///		if (clip)
/*TODO*///		{
/*TODO*///			int temp;
/*TODO*///
/*TODO*///
/*TODO*///			myclip.min_x = clip->min_x;
/*TODO*///			myclip.max_x = clip->max_x;
/*TODO*///			/* clip and myclip might be the same, so we need a temporary storage */
/*TODO*///			temp = clip->min_y;
/*TODO*///			myclip.min_y = dest_bmp->height-1 - clip->max_y;
/*TODO*///			myclip.max_y = dest_bmp->height-1 - temp;
/*TODO*///			clip = &myclip;
/*TODO*///		}
/*TODO*///#ifndef PREROTATE_GFX
/*TODO*///		flipy = !flipy;
/*TODO*///#endif
/*TODO*///	}
/*TODO*///
/*TODO*///	/* KW 991012 -- Added code to force clip to bitmap boundary */
/*TODO*///	if(clip)
/*TODO*///	{
/*TODO*///		myclip.min_x = clip->min_x;
/*TODO*///		myclip.max_x = clip->max_x;
/*TODO*///		myclip.min_y = clip->min_y;
/*TODO*///		myclip.max_y = clip->max_y;
/*TODO*///
/*TODO*///		if (myclip.min_x < 0) myclip.min_x = 0;
/*TODO*///		if (myclip.max_x >= dest_bmp->width) myclip.max_x = dest_bmp->width-1;
/*TODO*///		if (myclip.min_y < 0) myclip.min_y = 0;
/*TODO*///		if (myclip.max_y >= dest_bmp->height) myclip.max_y = dest_bmp->height-1;
/*TODO*///
/*TODO*///		clip=&myclip;
/*TODO*///	}
/*TODO*///
/*TODO*///
/*TODO*///	/* ASG 980209 -- added 16-bit version */
/*TODO*///	if (dest_bmp->depth != 16)
/*TODO*///	{
/*TODO*///		if( gfx && gfx->colortable )
/*TODO*///		{
/*TODO*///			const UINT16 *pal = &gfx->colortable[gfx->color_granularity * (color % gfx->total_colors)]; /* ASG 980209 */
/*TODO*///			int source_base = (code % gfx->total_elements) * gfx->height;
/*TODO*///
/*TODO*///			int sprite_screen_height = (scaley*gfx->height+0x8000)>>16;
/*TODO*///			int sprite_screen_width = (scalex*gfx->width+0x8000)>>16;
/*TODO*///
/*TODO*///			if (sprite_screen_width && sprite_screen_height)
/*TODO*///			{
/*TODO*///				/* compute sprite increment per screen pixel */
/*TODO*///				int dx = (gfx->width<<16)/sprite_screen_width;
/*TODO*///				int dy = (gfx->height<<16)/sprite_screen_height;
/*TODO*///
/*TODO*///				int ex = sx+sprite_screen_width;
/*TODO*///				int ey = sy+sprite_screen_height;
/*TODO*///
/*TODO*///				int x_index_base;
/*TODO*///				int y_index;
/*TODO*///
/*TODO*///				if( flipx )
/*TODO*///				{
/*TODO*///					x_index_base = (sprite_screen_width-1)*dx;
/*TODO*///					dx = -dx;
/*TODO*///				}
/*TODO*///				else
/*TODO*///				{
/*TODO*///					x_index_base = 0;
/*TODO*///				}
/*TODO*///
/*TODO*///				if( flipy )
/*TODO*///				{
/*TODO*///					y_index = (sprite_screen_height-1)*dy;
/*TODO*///					dy = -dy;
/*TODO*///				}
/*TODO*///				else
/*TODO*///				{
/*TODO*///					y_index = 0;
/*TODO*///				}
/*TODO*///
/*TODO*///				if( clip )
/*TODO*///				{
/*TODO*///					if( sx < clip->min_x)
/*TODO*///					{ /* clip left */
/*TODO*///						int pixels = clip->min_x-sx;
/*TODO*///						sx += pixels;
/*TODO*///						x_index_base += pixels*dx;
/*TODO*///					}
/*TODO*///					if( sy < clip->min_y )
/*TODO*///					{ /* clip top */
/*TODO*///						int pixels = clip->min_y-sy;
/*TODO*///						sy += pixels;
/*TODO*///						y_index += pixels*dy;
/*TODO*///					}
/*TODO*///					/* NS 980211 - fixed incorrect clipping */
/*TODO*///					if( ex > clip->max_x+1 )
/*TODO*///					{ /* clip right */
/*TODO*///						int pixels = ex-clip->max_x-1;
/*TODO*///						ex -= pixels;
/*TODO*///					}
/*TODO*///					if( ey > clip->max_y+1 )
/*TODO*///					{ /* clip bottom */
/*TODO*///						int pixels = ey-clip->max_y-1;
/*TODO*///						ey -= pixels;
/*TODO*///					}
/*TODO*///				}
/*TODO*///
/*TODO*///				if( ex>sx )
/*TODO*///				{ /* skip if inner loop doesn't draw anything */
/*TODO*///					int y;
/*TODO*///
/*TODO*///					/* case 1: TRANSPARENCY_PEN */
/*TODO*///					if (transparency == TRANSPARENCY_PEN)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///											dest[x] = pal[c];
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color ) dest[x] = pal[c];
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///
/*TODO*///					/* case 1b: TRANSPARENCY_PEN_RAW */
/*TODO*///					if (transparency == TRANSPARENCY_PEN_RAW)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///											dest[x] = color + c;
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color ) dest[x] = color + c;
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///
/*TODO*///					/* case 2: TRANSPARENCY_PENS */
/*TODO*///					if (transparency == TRANSPARENCY_PENS)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if (((1 << c) & transparent_color) == 0)
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///											dest[x] = pal[c];
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if (((1 << c) & transparent_color) == 0)
/*TODO*///										dest[x] = pal[c];
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///
/*TODO*///					/* case 3: TRANSPARENCY_COLOR */
/*TODO*///					else if (transparency == TRANSPARENCY_COLOR)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = pal[source[x_index>>16]];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///											dest[x] = c;
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = pal[source[x_index>>16]];
/*TODO*///									if( c != transparent_color ) dest[x] = c;
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///
/*TODO*///					/* case 4: TRANSPARENCY_PEN_TABLE */
/*TODO*///					if (transparency == TRANSPARENCY_PEN_TABLE)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///										{
/*TODO*///											switch(gfx_drawmode_table[c])
/*TODO*///											{
/*TODO*///											case DRAWMODE_SOURCE:
/*TODO*///												dest[x] = pal[c];
/*TODO*///												break;
/*TODO*///											case DRAWMODE_SHADOW:
/*TODO*///												dest[x] = palette_shadow_table[dest[x]];
/*TODO*///												break;
/*TODO*///											}
/*TODO*///										}
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										switch(gfx_drawmode_table[c])
/*TODO*///										{
/*TODO*///										case DRAWMODE_SOURCE:
/*TODO*///											dest[x] = pal[c];
/*TODO*///											break;
/*TODO*///										case DRAWMODE_SHADOW:
/*TODO*///											dest[x] = palette_shadow_table[dest[x]];
/*TODO*///											break;
/*TODO*///										}
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///
/*TODO*///					/* case 4b: TRANSPARENCY_PEN_TABLE_RAW */
/*TODO*///					if (transparency == TRANSPARENCY_PEN_TABLE_RAW)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///										{
/*TODO*///											switch(gfx_drawmode_table[c])
/*TODO*///											{
/*TODO*///											case DRAWMODE_SOURCE:
/*TODO*///												dest[x] = color + c;
/*TODO*///												break;
/*TODO*///											case DRAWMODE_SHADOW:
/*TODO*///												dest[x] = palette_shadow_table[dest[x]];
/*TODO*///												break;
/*TODO*///											}
/*TODO*///										}
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT8 *dest = dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										switch(gfx_drawmode_table[c])
/*TODO*///										{
/*TODO*///										case DRAWMODE_SOURCE:
/*TODO*///											dest[x] = color + c;
/*TODO*///											break;
/*TODO*///										case DRAWMODE_SHADOW:
/*TODO*///											dest[x] = palette_shadow_table[dest[x]];
/*TODO*///											break;
/*TODO*///										}
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///				}
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///
/*TODO*///	/* ASG 980209 -- new 16-bit part */
/*TODO*///	else
/*TODO*///	{
/*TODO*///		if( gfx && gfx->colortable )
/*TODO*///		{
/*TODO*///			const UINT16 *pal = &gfx->colortable[gfx->color_granularity * (color % gfx->total_colors)]; /* ASG 980209 */
/*TODO*///			int source_base = (code % gfx->total_elements) * gfx->height;
/*TODO*///
/*TODO*///			int sprite_screen_height = (scaley*gfx->height+0x8000)>>16;
/*TODO*///			int sprite_screen_width = (scalex*gfx->width+0x8000)>>16;
/*TODO*///
/*TODO*///			if (sprite_screen_width && sprite_screen_height)
/*TODO*///			{
/*TODO*///				/* compute sprite increment per screen pixel */
/*TODO*///				int dx = (gfx->width<<16)/sprite_screen_width;
/*TODO*///				int dy = (gfx->height<<16)/sprite_screen_height;
/*TODO*///
/*TODO*///				int ex = sx+sprite_screen_width;
/*TODO*///				int ey = sy+sprite_screen_height;
/*TODO*///
/*TODO*///				int x_index_base;
/*TODO*///				int y_index;
/*TODO*///
/*TODO*///				if( flipx )
/*TODO*///				{
/*TODO*///					x_index_base = (sprite_screen_width-1)*dx;
/*TODO*///					dx = -dx;
/*TODO*///				}
/*TODO*///				else
/*TODO*///				{
/*TODO*///					x_index_base = 0;
/*TODO*///				}
/*TODO*///
/*TODO*///				if( flipy )
/*TODO*///				{
/*TODO*///					y_index = (sprite_screen_height-1)*dy;
/*TODO*///					dy = -dy;
/*TODO*///				}
/*TODO*///				else
/*TODO*///				{
/*TODO*///					y_index = 0;
/*TODO*///				}
/*TODO*///
/*TODO*///				if( clip )
/*TODO*///				{
/*TODO*///					if( sx < clip->min_x)
/*TODO*///					{ /* clip left */
/*TODO*///						int pixels = clip->min_x-sx;
/*TODO*///						sx += pixels;
/*TODO*///						x_index_base += pixels*dx;
/*TODO*///					}
/*TODO*///					if( sy < clip->min_y )
/*TODO*///					{ /* clip top */
/*TODO*///						int pixels = clip->min_y-sy;
/*TODO*///						sy += pixels;
/*TODO*///						y_index += pixels*dy;
/*TODO*///					}
/*TODO*///					/* NS 980211 - fixed incorrect clipping */
/*TODO*///					if( ex > clip->max_x+1 )
/*TODO*///					{ /* clip right */
/*TODO*///						int pixels = ex-clip->max_x-1;
/*TODO*///						ex -= pixels;
/*TODO*///					}
/*TODO*///					if( ey > clip->max_y+1 )
/*TODO*///					{ /* clip bottom */
/*TODO*///						int pixels = ey-clip->max_y-1;
/*TODO*///						ey -= pixels;
/*TODO*///					}
/*TODO*///				}
/*TODO*///
/*TODO*///				if( ex>sx )
/*TODO*///				{ /* skip if inner loop doesn't draw anything */
/*TODO*///					int y;
/*TODO*///
/*TODO*///					/* case 1: TRANSPARENCY_PEN */
/*TODO*///					if (transparency == TRANSPARENCY_PEN)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///											dest[x] = pal[c];
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color ) dest[x] = pal[c];
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///
/*TODO*///					/* case 1b: TRANSPARENCY_PEN_RAW */
/*TODO*///					if (transparency == TRANSPARENCY_PEN_RAW)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///											dest[x] = color + c;
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color ) dest[x] = color + c;
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///
/*TODO*///					/* case 2: TRANSPARENCY_PENS */
/*TODO*///					if (transparency == TRANSPARENCY_PENS)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if (((1 << c) & transparent_color) == 0)
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///											dest[x] = pal[c];
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if (((1 << c) & transparent_color) == 0)
/*TODO*///										dest[x] = pal[c];
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///
/*TODO*///					/* case 3: TRANSPARENCY_COLOR */
/*TODO*///					else if (transparency == TRANSPARENCY_COLOR)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = pal[source[x_index>>16]];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///											dest[x] = c;
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = pal[source[x_index>>16]];
/*TODO*///									if( c != transparent_color ) dest[x] = c;
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///
/*TODO*///					/* case 4: TRANSPARENCY_PEN_TABLE */
/*TODO*///					if (transparency == TRANSPARENCY_PEN_TABLE)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///										{
/*TODO*///											switch(gfx_drawmode_table[c])
/*TODO*///											{
/*TODO*///											case DRAWMODE_SOURCE:
/*TODO*///												dest[x] = pal[c];
/*TODO*///												break;
/*TODO*///											case DRAWMODE_SHADOW:
/*TODO*///												dest[x] = palette_shadow_table[dest[x]];
/*TODO*///												break;
/*TODO*///											}
/*TODO*///										}
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										switch(gfx_drawmode_table[c])
/*TODO*///										{
/*TODO*///										case DRAWMODE_SOURCE:
/*TODO*///											dest[x] = pal[c];
/*TODO*///											break;
/*TODO*///										case DRAWMODE_SHADOW:
/*TODO*///											dest[x] = palette_shadow_table[dest[x]];
/*TODO*///											break;
/*TODO*///										}
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///
/*TODO*///					/* case 4b: TRANSPARENCY_PEN_TABLE_RAW */
/*TODO*///					if (transparency == TRANSPARENCY_PEN_TABLE_RAW)
/*TODO*///					{
/*TODO*///						if (pri_buffer)
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///								UINT8 *pri = pri_buffer->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										if (((1 << pri[x]) & pri_mask) == 0)
/*TODO*///										{
/*TODO*///											switch(gfx_drawmode_table[c])
/*TODO*///											{
/*TODO*///											case DRAWMODE_SOURCE:
/*TODO*///												dest[x] = color + c;
/*TODO*///												break;
/*TODO*///											case DRAWMODE_SHADOW:
/*TODO*///												dest[x] = palette_shadow_table[dest[x]];
/*TODO*///												break;
/*TODO*///											}
/*TODO*///										}
/*TODO*///										pri[x] = 31;
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							for( y=sy; y<ey; y++ )
/*TODO*///							{
/*TODO*///								UINT8 *source = gfx->gfxdata + (source_base+(y_index>>16)) * gfx->line_modulo;
/*TODO*///								UINT16 *dest = (UINT16 *)dest_bmp->line[y];
/*TODO*///
/*TODO*///								int x, x_index = x_index_base;
/*TODO*///								for( x=sx; x<ex; x++ )
/*TODO*///								{
/*TODO*///									int c = source[x_index>>16];
/*TODO*///									if( c != transparent_color )
/*TODO*///									{
/*TODO*///										switch(gfx_drawmode_table[c])
/*TODO*///										{
/*TODO*///										case DRAWMODE_SOURCE:
/*TODO*///											dest[x] = color + c;
/*TODO*///											break;
/*TODO*///										case DRAWMODE_SHADOW:
/*TODO*///											dest[x] = palette_shadow_table[dest[x]];
/*TODO*///											break;
/*TODO*///										}
/*TODO*///									}
/*TODO*///									x_index += dx;
/*TODO*///								}
/*TODO*///
/*TODO*///								y_index += dy;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///				}
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///void drawgfxzoom( struct osd_bitmap *dest_bmp,const struct GfxElement *gfx,
/*TODO*///		unsigned int code,unsigned int color,int flipx,int flipy,int sx,int sy,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color,int scalex, int scaley)
/*TODO*///{
/*TODO*///	profiler_mark(PROFILER_DRAWGFX);
/*TODO*///	common_drawgfxzoom(dest_bmp,gfx,code,color,flipx,flipy,sx,sy,
/*TODO*///			clip,transparency,transparent_color,scalex,scaley,NULL,0);
/*TODO*///	profiler_mark(PROFILER_END);
/*TODO*///}
/*TODO*///
/*TODO*///    public static void pdrawgfxzoom(osd_bitmap dest_bmp, GfxElement gfx,
/*TODO*///            int code, int color, int flipx, int flipy, int sx, int sy,
/*TODO*///            rectangle clip, int transparency, int transparent_color, int scalex, int scaley,
/*TODO*///            int/*UINT32*/ priority_mask) {
/*TODO*///        common_drawgfxzoom(dest_bmp, gfx, code, color, flipx, flipy, sx, sy,
/*TODO*///                clip, transparency, transparent_color, scalex, scaley, priority_bitmap, priority_mask);
/*TODO*///    }
/*TODO*///
/*TODO*///
/*TODO*///void plot_pixel2(struct osd_bitmap *bitmap1,struct osd_bitmap *bitmap2,int x,int y,int pen)
/*TODO*///{
/*TODO*///	plot_pixel(bitmap1, x, y, pen);
/*TODO*///	plot_pixel(bitmap2, x, y, pen);
/*TODO*///}
/*TODO*///
/*TODO*///static void pp_8_nd(struct osd_bitmap *b,int x,int y,int p)  { b->line[y][x] = p; }
/*TODO*///static void pp_8_nd_fx(struct osd_bitmap *b,int x,int y,int p)  { b->line[y][b->width-1-x] = p; }
/*TODO*///static void pp_8_nd_fy(struct osd_bitmap *b,int x,int y,int p)  { b->line[b->height-1-y][x] = p; }
/*TODO*///static void pp_8_nd_fxy(struct osd_bitmap *b,int x,int y,int p)  { b->line[b->height-1-y][b->width-1-x] = p; }
/*TODO*///static void pp_8_nd_s(struct osd_bitmap *b,int x,int y,int p)  { b->line[x][y] = p; }
/*TODO*///static void pp_8_nd_fx_s(struct osd_bitmap *b,int x,int y,int p)  { b->line[x][b->width-1-y] = p; }
/*TODO*///static void pp_8_nd_fy_s(struct osd_bitmap *b,int x,int y,int p)  { b->line[b->height-1-x][y] = p; }
/*TODO*///static void pp_8_nd_fxy_s(struct osd_bitmap *b,int x,int y,int p)  { b->line[b->height-1-x][b->width-1-y] = p; }
/*TODO*///
/*TODO*///static void pp_8_d(struct osd_bitmap *b,int x,int y,int p)  { b->line[y][x] = p; osd_mark_dirty (x,y,x,y,0); }
/*TODO*///static void pp_8_d_fx(struct osd_bitmap *b,int x,int y,int p)  { x = b->width-1-x;  b->line[y][x] = p; osd_mark_dirty (x,y,x,y,0); }
/*TODO*///static void pp_8_d_fy(struct osd_bitmap *b,int x,int y,int p)  { y = b->height-1-y; b->line[y][x] = p; osd_mark_dirty (x,y,x,y,0); }
/*TODO*///static void pp_8_d_fxy(struct osd_bitmap *b,int x,int y,int p)  { x = b->width-1-x; y = b->height-1-y; b->line[y][x] = p; osd_mark_dirty (x,y,x,y,0); }
/*TODO*///static void pp_8_d_s(struct osd_bitmap *b,int x,int y,int p)  { b->line[x][y] = p; osd_mark_dirty (y,x,y,x,0); }
/*TODO*///static void pp_8_d_fx_s(struct osd_bitmap *b,int x,int y,int p)  { y = b->width-1-y; b->line[x][y] = p; osd_mark_dirty (y,x,y,x,0); }
/*TODO*///static void pp_8_d_fy_s(struct osd_bitmap *b,int x,int y,int p)  { x = b->height-1-x; b->line[x][y] = p; osd_mark_dirty (y,x,y,x,0); }
/*TODO*///static void pp_8_d_fxy_s(struct osd_bitmap *b,int x,int y,int p)  { x = b->height-1-x; y = b->width-1-y; b->line[x][y] = p; osd_mark_dirty (y,x,y,x,0); }
/*TODO*///
/*TODO*///static void pp_16_nd(struct osd_bitmap *b,int x,int y,int p)  { ((UINT16 *)b->line[y])[x] = p; }
/*TODO*///static void pp_16_nd_fx(struct osd_bitmap *b,int x,int y,int p)  { ((UINT16 *)b->line[y])[b->width-1-x] = p; }
/*TODO*///static void pp_16_nd_fy(struct osd_bitmap *b,int x,int y,int p)  { ((UINT16 *)b->line[b->height-1-y])[x] = p; }
/*TODO*///static void pp_16_nd_fxy(struct osd_bitmap *b,int x,int y,int p)  { ((UINT16 *)b->line[b->height-1-y])[b->width-1-x] = p; }
/*TODO*///static void pp_16_nd_s(struct osd_bitmap *b,int x,int y,int p)  { ((UINT16 *)b->line[x])[y] = p; }
/*TODO*///static void pp_16_nd_fx_s(struct osd_bitmap *b,int x,int y,int p)  { ((UINT16 *)b->line[x])[b->width-1-y] = p; }
/*TODO*///static void pp_16_nd_fy_s(struct osd_bitmap *b,int x,int y,int p)  { ((UINT16 *)b->line[b->height-1-x])[y] = p; }
/*TODO*///static void pp_16_nd_fxy_s(struct osd_bitmap *b,int x,int y,int p)  { ((UINT16 *)b->line[b->height-1-x])[b->width-1-y] = p; }
/*TODO*///
/*TODO*///static void pp_16_d(struct osd_bitmap *b,int x,int y,int p)  { ((UINT16 *)b->line[y])[x] = p; osd_mark_dirty (x,y,x,y,0); }
/*TODO*///static void pp_16_d_fx(struct osd_bitmap *b,int x,int y,int p)  { x = b->width-1-x;  ((UINT16 *)b->line[y])[x] = p; osd_mark_dirty (x,y,x,y,0); }
/*TODO*///static void pp_16_d_fy(struct osd_bitmap *b,int x,int y,int p)  { y = b->height-1-y; ((UINT16 *)b->line[y])[x] = p; osd_mark_dirty (x,y,x,y,0); }
/*TODO*///static void pp_16_d_fxy(struct osd_bitmap *b,int x,int y,int p)  { x = b->width-1-x; y = b->height-1-y; ((UINT16 *)b->line[y])[x] = p; osd_mark_dirty (x,y,x,y,0); }
/*TODO*///static void pp_16_d_s(struct osd_bitmap *b,int x,int y,int p)  { ((UINT16 *)b->line[x])[y] = p; osd_mark_dirty (y,x,y,x,0); }
/*TODO*///static void pp_16_d_fx_s(struct osd_bitmap *b,int x,int y,int p)  { y = b->width-1-y; ((UINT16 *)b->line[x])[y] = p; osd_mark_dirty (y,x,y,x,0); }
/*TODO*///static void pp_16_d_fy_s(struct osd_bitmap *b,int x,int y,int p)  { x = b->height-1-x; ((UINT16 *)b->line[x])[y] = p; osd_mark_dirty (y,x,y,x,0); }
/*TODO*///static void pp_16_d_fxy_s(struct osd_bitmap *b,int x,int y,int p)  { x = b->height-1-x; y = b->width-1-y; ((UINT16 *)b->line[x])[y] = p; osd_mark_dirty (y,x,y,x,0); }
/*TODO*///
/*TODO*///
/*TODO*///static int rp_8(struct osd_bitmap *b,int x,int y)  { return b->line[y][x]; }
/*TODO*///static int rp_8_fx(struct osd_bitmap *b,int x,int y)  { return b->line[y][b->width-1-x]; }
/*TODO*///static int rp_8_fy(struct osd_bitmap *b,int x,int y)  { return b->line[b->height-1-y][x]; }
/*TODO*///static int rp_8_fxy(struct osd_bitmap *b,int x,int y)  { return b->line[b->height-1-y][b->width-1-x]; }
/*TODO*///static int rp_8_s(struct osd_bitmap *b,int x,int y)  { return b->line[x][y]; }
/*TODO*///static int rp_8_fx_s(struct osd_bitmap *b,int x,int y)  { return b->line[x][b->width-1-y]; }
/*TODO*///static int rp_8_fy_s(struct osd_bitmap *b,int x,int y)  { return b->line[b->height-1-x][y]; }
/*TODO*///static int rp_8_fxy_s(struct osd_bitmap *b,int x,int y)  { return b->line[b->height-1-x][b->width-1-y]; }
/*TODO*///
/*TODO*///static int rp_16(struct osd_bitmap *b,int x,int y)  { return ((UINT16 *)b->line[y])[x]; }
/*TODO*///static int rp_16_fx(struct osd_bitmap *b,int x,int y)  { return ((UINT16 *)b->line[y])[b->width-1-x]; }
/*TODO*///static int rp_16_fy(struct osd_bitmap *b,int x,int y)  { return ((UINT16 *)b->line[b->height-1-y])[x]; }
/*TODO*///static int rp_16_fxy(struct osd_bitmap *b,int x,int y)  { return ((UINT16 *)b->line[b->height-1-y])[b->width-1-x]; }
/*TODO*///static int rp_16_s(struct osd_bitmap *b,int x,int y)  { return ((UINT16 *)b->line[x])[y]; }
/*TODO*///static int rp_16_fx_s(struct osd_bitmap *b,int x,int y)  { return ((UINT16 *)b->line[x])[b->width-1-y]; }
/*TODO*///static int rp_16_fy_s(struct osd_bitmap *b,int x,int y)  { return ((UINT16 *)b->line[b->height-1-x])[y]; }
/*TODO*///static int rp_16_fxy_s(struct osd_bitmap *b,int x,int y)  { return ((UINT16 *)b->line[b->height-1-x])[b->width-1-y]; }
/*TODO*///
/*TODO*///
/*TODO*///static void pb_8_nd(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; while(h-->0){ int c=w; x=t; while(c-->0){ b->line[y][x] = p; x++; } y++; } }
/*TODO*///static void pb_8_nd_fx(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->width-1-x; while(h-->0){ int c=w; x=t; while(c-->0){ b->line[y][x] = p; x--; } y++; } }
/*TODO*///static void pb_8_nd_fy(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; y = b->height-1-y; while(h-->0){ int c=w; x=t; while(c-->0){ b->line[y][x] = p; x++; } y--; } }
/*TODO*///static void pb_8_nd_fxy(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->width-1-x; y = b->height-1-y; while(h-->0){ int c=w; x=t; while(c-->0){ b->line[y][x] = p; x--; } y--; } }
/*TODO*///static void pb_8_nd_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; while(h-->0){ int c=w; x=t; while(c-->0){ b->line[x][y] = p; x++; } y++; } }
/*TODO*///static void pb_8_nd_fx_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; y = b->width-1-y; while(h-->0){ int c=w; x=t; while(c-->0){ b->line[x][y] = p; x++; } y--; } }
/*TODO*///static void pb_8_nd_fy_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->height-1-x; while(h-->0){ int c=w; x=t; while(c-->0){ b->line[x][y] = p; x--; } y++; } }
/*TODO*///static void pb_8_nd_fxy_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->height-1-x; y = b->width-1-y; while(h-->0){ int c=w; x=t; while(c-->0){ b->line[x][y] = p; x--; } y--; } }
/*TODO*///
/*TODO*///static void pb_8_d(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; osd_mark_dirty (t,y,t+w-1,y+h-1,0); while(h-->0){ int c=w; x=t; while(c-->0){ b->line[y][x] = p; x++; } y++; } }
/*TODO*///static void pb_8_d_fx(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->width-1-x;  osd_mark_dirty (t-w+1,y,t,y+h-1,0); while(h-->0){ int c=w; x=t; while(c-->0){ b->line[y][x] = p; x--; } y++; } }
/*TODO*///static void pb_8_d_fy(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; y = b->height-1-y; osd_mark_dirty (t,y-h+1,t+w-1,y,0); while(h-->0){ int c=w; x=t; while(c-->0){ b->line[y][x] = p; x++; } y--; } }
/*TODO*///static void pb_8_d_fxy(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->width-1-x; y = b->height-1-y; osd_mark_dirty (t-w+1,y-h+1,t,y,0); while(h-->0){ int c=w; x=t; while(c-->0){ b->line[y][x] = p; x--; } y--; } }
/*TODO*///static void pb_8_d_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; osd_mark_dirty (y,t,y+h-1,t+w-1,0); while(h-->0){ int c=w; x=t; while(c-->0){ b->line[x][y] = p; x++; } y++; } }
/*TODO*///static void pb_8_d_fx_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; y = b->width-1-y;  osd_mark_dirty (y-h+1,t,y,t+w-1,0); while(h-->0){ int c=w; x=t; while(c-->0){ b->line[x][y] = p; x++; } y--; } }
/*TODO*///static void pb_8_d_fy_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->height-1-x; osd_mark_dirty (y,t-w+1,y+h-1,t,0); while(h-->0){ int c=w; x=t; while(c-->0){ b->line[x][y] = p; x--; } y++; } }
/*TODO*///static void pb_8_d_fxy_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->height-1-x; y = b->width-1-y; osd_mark_dirty (y-h+1,t-w+1,y,t,0); while(h-->0){ int c=w; x=t; while(c-->0){ b->line[x][y] = p; x--; } y--; } }
/*TODO*///
/*TODO*///static void pb_16_nd(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[y])[x] = p; x++; } y++; } }
/*TODO*///static void pb_16_nd_fx(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->width-1-x; while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[y])[x] = p; x--; } y++; } }
/*TODO*///static void pb_16_nd_fy(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; y = b->height-1-y; while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[y])[x] = p; x++; } y--; } }
/*TODO*///static void pb_16_nd_fxy(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->width-1-x; y = b->height-1-y; while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[y])[x] = p; x--; } y--; } }
/*TODO*///static void pb_16_nd_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[x])[y] = p; x++; } y++; } }
/*TODO*///static void pb_16_nd_fx_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; y = b->width-1-y; while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[x])[y] = p; x++; } y--; } }
/*TODO*///static void pb_16_nd_fy_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->height-1-x; while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[x])[y] = p; x--; } y++; } }
/*TODO*///static void pb_16_nd_fxy_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->height-1-x; y = b->width-1-y; while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[x])[y] = p; x--; } y--; } }
/*TODO*///
/*TODO*///static void pb_16_d(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; osd_mark_dirty (t,y,t+w-1,y+h-1,0); while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[y])[x] = p; x++; } y++; } }
/*TODO*///static void pb_16_d_fx(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->width-1-x;  osd_mark_dirty (t-w+1,y,t,y+h-1,0); while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[y])[x] = p; x--; } y++; } }
/*TODO*///static void pb_16_d_fy(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; y = b->height-1-y; osd_mark_dirty (t,y-h+1,t+w-1,y,0); while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[y])[x] = p; x++; } y--; } }
/*TODO*///static void pb_16_d_fxy(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->width-1-x; y = b->height-1-y; osd_mark_dirty (t-w+1,y-h+1,t,y,0); while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[y])[x] = p; x--; } y--; } }
/*TODO*///static void pb_16_d_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; osd_mark_dirty (y,t,y+h-1,t+w-1,0); while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[x])[y] = p; x++; } y++; } }
/*TODO*///static void pb_16_d_fx_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=x; y = b->width-1-y; osd_mark_dirty (y-h+1,t,y,t+w-1,0); while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[x])[y] = p; x++; } y--; } }
/*TODO*///static void pb_16_d_fy_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->height-1-x; osd_mark_dirty (y,t-w+1,y+h-1,t,0); while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[x])[y] = p; x--; } y++; } }
/*TODO*///static void pb_16_d_fxy_s(struct osd_bitmap *b,int x,int y,int w,int h,int p)  { int t=b->height-1-x; y = b->width-1-y; osd_mark_dirty (y-h+1,t-w+1,y,t,0); while(h-->0){ int c=w; x=t; while(c-->0){ ((UINT16 *)b->line[x])[y] = p; x--; } y--; } }
/*TODO*///
/*TODO*///
/*TODO*///static plot_pixel_proc pps_8_nd[] =
/*TODO*///		{ pp_8_nd, 	 pp_8_nd_fx,   pp_8_nd_fy, 	 pp_8_nd_fxy,
/*TODO*///		  pp_8_nd_s, pp_8_nd_fx_s, pp_8_nd_fy_s, pp_8_nd_fxy_s };
/*TODO*///
/*TODO*///static plot_pixel_proc pps_8_d[] =
/*TODO*///		{ pp_8_d, 	pp_8_d_fx,   pp_8_d_fy,	  pp_8_d_fxy,
/*TODO*///		  pp_8_d_s, pp_8_d_fx_s, pp_8_d_fy_s, pp_8_d_fxy_s };
/*TODO*///
/*TODO*///static plot_pixel_proc pps_16_nd[] =
/*TODO*///		{ pp_16_nd,   pp_16_nd_fx,   pp_16_nd_fy, 	pp_16_nd_fxy,
/*TODO*///		  pp_16_nd_s, pp_16_nd_fx_s, pp_16_nd_fy_s, pp_16_nd_fxy_s };
/*TODO*///
/*TODO*///static plot_pixel_proc pps_16_d[] =
/*TODO*///		{ pp_16_d,   pp_16_d_fx,   pp_16_d_fy, 	 pp_16_d_fxy,
/*TODO*///		  pp_16_d_s, pp_16_d_fx_s, pp_16_d_fy_s, pp_16_d_fxy_s };
/*TODO*///
/*TODO*///
/*TODO*///static read_pixel_proc rps_8[] =
/*TODO*///		{ rp_8,	  rp_8_fx,   rp_8_fy,	rp_8_fxy,
/*TODO*///		  rp_8_s, rp_8_fx_s, rp_8_fy_s, rp_8_fxy_s };
/*TODO*///
/*TODO*///static read_pixel_proc rps_16[] =
/*TODO*///		{ rp_16,   rp_16_fx,   rp_16_fy,   rp_16_fxy,
/*TODO*///		  rp_16_s, rp_16_fx_s, rp_16_fy_s, rp_16_fxy_s };
/*TODO*///
/*TODO*///
/*TODO*///static plot_box_proc pbs_8_nd[] =
/*TODO*///		{ pb_8_nd, 	 pb_8_nd_fx,   pb_8_nd_fy, 	 pb_8_nd_fxy,
/*TODO*///		  pb_8_nd_s, pb_8_nd_fx_s, pb_8_nd_fy_s, pb_8_nd_fxy_s };
/*TODO*///
/*TODO*///static plot_box_proc pbs_8_d[] =
/*TODO*///		{ pb_8_d, 	pb_8_d_fx,   pb_8_d_fy,	  pb_8_d_fxy,
/*TODO*///		  pb_8_d_s, pb_8_d_fx_s, pb_8_d_fy_s, pb_8_d_fxy_s };
/*TODO*///
/*TODO*///static plot_box_proc pbs_16_nd[] =
/*TODO*///		{ pb_16_nd,   pb_16_nd_fx,   pb_16_nd_fy, 	pb_16_nd_fxy,
/*TODO*///		  pb_16_nd_s, pb_16_nd_fx_s, pb_16_nd_fy_s, pb_16_nd_fxy_s };
/*TODO*///
/*TODO*///static plot_box_proc pbs_16_d[] =
/*TODO*///		{ pb_16_d,   pb_16_d_fx,   pb_16_d_fy, 	 pb_16_d_fxy,
/*TODO*///		  pb_16_d_s, pb_16_d_fx_s, pb_16_d_fy_s, pb_16_d_fxy_s };
/*TODO*///
/*TODO*///
/*TODO*///void set_pixel_functions(void)
/*TODO*///{
/*TODO*///	if (Machine->color_depth == 8)
/*TODO*///	{
/*TODO*///		read_pixel = rps_8[Machine->orientation];
/*TODO*///
/*TODO*///		if (Machine->drv->video_attributes & VIDEO_SUPPORTS_DIRTY)
/*TODO*///		{
/*TODO*///			plot_pixel = pps_8_d[Machine->orientation];
/*TODO*///			plot_box = pbs_8_d[Machine->orientation];
/*TODO*///		}
/*TODO*///		else
/*TODO*///		{
/*TODO*///			plot_pixel = pps_8_nd[Machine->orientation];
/*TODO*///			plot_box = pbs_8_nd[Machine->orientation];
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else
/*TODO*///	{
/*TODO*///		read_pixel = rps_16[Machine->orientation];
/*TODO*///
/*TODO*///		if (Machine->drv->video_attributes & VIDEO_SUPPORTS_DIRTY)
/*TODO*///		{
/*TODO*///			plot_pixel = pps_16_d[Machine->orientation];
/*TODO*///			plot_box = pbs_16_d[Machine->orientation];
/*TODO*///		}
/*TODO*///		else
/*TODO*///		{
/*TODO*///			plot_pixel = pps_16_nd[Machine->orientation];
/*TODO*///			plot_box = pbs_16_nd[Machine->orientation];
/*TODO*///		}
/*TODO*///	}
/*TODO*///
/*TODO*///	/* while we're here, fill in the raw drawing mode table as well */
/*TODO*///	is_raw[TRANSPARENCY_NONE_RAW]      = 1;
/*TODO*///	is_raw[TRANSPARENCY_PEN_RAW]       = 1;
/*TODO*///	is_raw[TRANSPARENCY_PENS_RAW]      = 1;
/*TODO*///	is_raw[TRANSPARENCY_THROUGH_RAW]   = 1;
/*TODO*///	is_raw[TRANSPARENCY_PEN_TABLE_RAW] = 1;
/*TODO*///	is_raw[TRANSPARENCY_BLEND_RAW]     = 1;
/*TODO*///}
/*TODO*///
/*TODO*///#else /* DECLARE */
/*TODO*///
/*TODO*////* -------------------- included inline section --------------------- */
/*TODO*///
/*TODO*////* don't put this file in the makefile, it is #included by common.c to */
/*TODO*////* generate 8-bit and 16-bit versions                                  */
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_opaque,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			dstdata[0] = paldata[srcdata[0]];
/*TODO*///			dstdata[1] = paldata[srcdata[1]];
/*TODO*///			dstdata[2] = paldata[srcdata[2]];
/*TODO*///			dstdata[3] = paldata[srcdata[3]];
/*TODO*///			dstdata[4] = paldata[srcdata[4]];
/*TODO*///			dstdata[5] = paldata[srcdata[5]];
/*TODO*///			dstdata[6] = paldata[srcdata[6]];
/*TODO*///			dstdata[7] = paldata[srcdata[7]];
/*TODO*///			dstdata += 8;
/*TODO*///			srcdata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///			*(dstdata++) = paldata[*(srcdata++)];
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_opaque_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			srcdata -= 8;
/*TODO*///			dstdata[0] = paldata[srcdata[8]];
/*TODO*///			dstdata[1] = paldata[srcdata[7]];
/*TODO*///			dstdata[2] = paldata[srcdata[6]];
/*TODO*///			dstdata[3] = paldata[srcdata[5]];
/*TODO*///			dstdata[4] = paldata[srcdata[4]];
/*TODO*///			dstdata[5] = paldata[srcdata[3]];
/*TODO*///			dstdata[6] = paldata[srcdata[2]];
/*TODO*///			dstdata[7] = paldata[srcdata[1]];
/*TODO*///			dstdata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///			*(dstdata++) = paldata[*(srcdata--)];
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_opaque_pri,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,UINT8 *pridata,UINT32 pmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	pmask |= (1<<31);
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			if (((1 << pridata[0]) & pmask) == 0) dstdata[0] = paldata[srcdata[0]];
/*TODO*///			if (((1 << pridata[1]) & pmask) == 0) dstdata[1] = paldata[srcdata[1]];
/*TODO*///			if (((1 << pridata[2]) & pmask) == 0) dstdata[2] = paldata[srcdata[2]];
/*TODO*///			if (((1 << pridata[3]) & pmask) == 0) dstdata[3] = paldata[srcdata[3]];
/*TODO*///			if (((1 << pridata[4]) & pmask) == 0) dstdata[4] = paldata[srcdata[4]];
/*TODO*///			if (((1 << pridata[5]) & pmask) == 0) dstdata[5] = paldata[srcdata[5]];
/*TODO*///			if (((1 << pridata[6]) & pmask) == 0) dstdata[6] = paldata[srcdata[6]];
/*TODO*///			if (((1 << pridata[7]) & pmask) == 0) dstdata[7] = paldata[srcdata[7]];
/*TODO*///			memset(pridata,31,8);
/*TODO*///			srcdata += 8;
/*TODO*///			dstdata += 8;
/*TODO*///			pridata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (((1 << *pridata) & pmask) == 0)
/*TODO*///				*dstdata = paldata[*srcdata];
/*TODO*///			*pridata = 31;
/*TODO*///			srcdata++;
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		pridata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_opaque_pri_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,UINT8 *pridata,UINT32 pmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	pmask |= (1<<31);
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			srcdata -= 8;
/*TODO*///			if (((1 << pridata[0]) & pmask) == 0) dstdata[0] = paldata[srcdata[8]];
/*TODO*///			if (((1 << pridata[1]) & pmask) == 0) dstdata[1] = paldata[srcdata[7]];
/*TODO*///			if (((1 << pridata[2]) & pmask) == 0) dstdata[2] = paldata[srcdata[6]];
/*TODO*///			if (((1 << pridata[3]) & pmask) == 0) dstdata[3] = paldata[srcdata[5]];
/*TODO*///			if (((1 << pridata[4]) & pmask) == 0) dstdata[4] = paldata[srcdata[4]];
/*TODO*///			if (((1 << pridata[5]) & pmask) == 0) dstdata[5] = paldata[srcdata[3]];
/*TODO*///			if (((1 << pridata[6]) & pmask) == 0) dstdata[6] = paldata[srcdata[2]];
/*TODO*///			if (((1 << pridata[7]) & pmask) == 0) dstdata[7] = paldata[srcdata[1]];
/*TODO*///			memset(pridata,31,8);
/*TODO*///			dstdata += 8;
/*TODO*///			pridata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (((1 << *pridata) & pmask) == 0)
/*TODO*///				*dstdata = paldata[*srcdata];
/*TODO*///			*pridata = 31;
/*TODO*///			srcdata--;
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		pridata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_opaque_raw,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		unsigned int colorbase),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			dstdata[0] = colorbase + srcdata[0];
/*TODO*///			dstdata[1] = colorbase + srcdata[1];
/*TODO*///			dstdata[2] = colorbase + srcdata[2];
/*TODO*///			dstdata[3] = colorbase + srcdata[3];
/*TODO*///			dstdata[4] = colorbase + srcdata[4];
/*TODO*///			dstdata[5] = colorbase + srcdata[5];
/*TODO*///			dstdata[6] = colorbase + srcdata[6];
/*TODO*///			dstdata[7] = colorbase + srcdata[7];
/*TODO*///			dstdata += 8;
/*TODO*///			srcdata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///			*(dstdata++) = colorbase + *(srcdata++);
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_opaque_raw_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		unsigned int colorbase),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			srcdata -= 8;
/*TODO*///			dstdata[0] = colorbase + srcdata[8];
/*TODO*///			dstdata[1] = colorbase + srcdata[7];
/*TODO*///			dstdata[2] = colorbase + srcdata[6];
/*TODO*///			dstdata[3] = colorbase + srcdata[5];
/*TODO*///			dstdata[4] = colorbase + srcdata[4];
/*TODO*///			dstdata[5] = colorbase + srcdata[3];
/*TODO*///			dstdata[6] = colorbase + srcdata[2];
/*TODO*///			dstdata[7] = colorbase + srcdata[1];
/*TODO*///			dstdata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///			*(dstdata++) = colorbase + *(srcdata--);
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transpen,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transpen),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	int trans4;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	trans4 = transpen * 0x01010101;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (col != transpen) *dstdata = paldata[col];
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			if ((col4 = *(sd4++)) != trans4)
/*TODO*///			{
/*TODO*///				UINT32 xod4;
/*TODO*///
/*TODO*///				xod4 = col4 ^ trans4;
/*TODO*///				if (xod4 & 0x000000ff) dstdata[BL0] = paldata[(col4) & 0xff];
/*TODO*///				if (xod4 & 0x0000ff00) dstdata[BL1] = paldata[(col4 >>  8) & 0xff];
/*TODO*///				if (xod4 & 0x00ff0000) dstdata[BL2] = paldata[(col4 >> 16) & 0xff];
/*TODO*///				if (xod4 & 0xff000000) dstdata[BL3] = paldata[col4 >> 24];
/*TODO*///			}
/*TODO*///			dstdata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (col != transpen) *dstdata = paldata[col];
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transpen_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transpen),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	int trans4;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///	srcdata -= 3;
/*TODO*///
/*TODO*///	trans4 = transpen * 0x01010101;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (col != transpen) *dstdata = paldata[col];
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			if ((col4 = *(sd4--)) != trans4)
/*TODO*///			{
/*TODO*///				UINT32 xod4;
/*TODO*///
/*TODO*///				xod4 = col4 ^ trans4;
/*TODO*///				if (xod4 & 0xff000000) dstdata[BL0] = paldata[col4 >> 24];
/*TODO*///				if (xod4 & 0x00ff0000) dstdata[BL1] = paldata[(col4 >> 16) & 0xff];
/*TODO*///				if (xod4 & 0x0000ff00) dstdata[BL2] = paldata[(col4 >>  8) & 0xff];
/*TODO*///				if (xod4 & 0x000000ff) dstdata[BL3] = paldata[col4 & 0xff];
/*TODO*///			}
/*TODO*///			dstdata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (col != transpen) *dstdata = paldata[col];
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transpen_pri,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transpen,UINT8 *pridata,UINT32 pmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	int trans4;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	pmask |= (1<<31);
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	trans4 = transpen * 0x01010101;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (col != transpen)
/*TODO*///			{
/*TODO*///				if (((1 << *pridata) & pmask) == 0)
/*TODO*///					*dstdata = paldata[col];
/*TODO*///				*pridata = 31;
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			if ((col4 = *(sd4++)) != trans4)
/*TODO*///			{
/*TODO*///				UINT32 xod4;
/*TODO*///
/*TODO*///				xod4 = col4 ^ trans4;
/*TODO*///				if (xod4 & 0x000000ff)
/*TODO*///				{
/*TODO*///					if (((1 << pridata[BL0]) & pmask) == 0)
/*TODO*///						dstdata[BL0] = paldata[(col4) & 0xff];
/*TODO*///					pridata[BL0] = 31;
/*TODO*///				}
/*TODO*///				if (xod4 & 0x0000ff00)
/*TODO*///				{
/*TODO*///					if (((1 << pridata[BL1]) & pmask) == 0)
/*TODO*///						dstdata[BL1] = paldata[(col4 >>  8) & 0xff];
/*TODO*///					pridata[BL1] = 31;
/*TODO*///				}
/*TODO*///				if (xod4 & 0x00ff0000)
/*TODO*///				{
/*TODO*///					if (((1 << pridata[BL2]) & pmask) == 0)
/*TODO*///						dstdata[BL2] = paldata[(col4 >> 16) & 0xff];
/*TODO*///					pridata[BL2] = 31;
/*TODO*///				}
/*TODO*///				if (xod4 & 0xff000000)
/*TODO*///				{
/*TODO*///					if (((1 << pridata[BL3]) & pmask) == 0)
/*TODO*///						dstdata[BL3] = paldata[col4 >> 24];
/*TODO*///					pridata[BL3] = 31;
/*TODO*///				}
/*TODO*///			}
/*TODO*///			dstdata += 4;
/*TODO*///			pridata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (col != transpen)
/*TODO*///			{
/*TODO*///				if (((1 << *pridata) & pmask) == 0)
/*TODO*///					*dstdata = paldata[col];
/*TODO*///				*pridata = 31;
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		pridata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transpen_pri_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transpen,UINT8 *pridata,UINT32 pmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	int trans4;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	pmask |= (1<<31);
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///	srcdata -= 3;
/*TODO*///
/*TODO*///	trans4 = transpen * 0x01010101;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (col != transpen)
/*TODO*///			{
/*TODO*///				if (((1 << *pridata) & pmask) == 0)
/*TODO*///					*dstdata = paldata[col];
/*TODO*///				*pridata = 31;
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			if ((col4 = *(sd4--)) != trans4)
/*TODO*///			{
/*TODO*///				UINT32 xod4;
/*TODO*///
/*TODO*///				xod4 = col4 ^ trans4;
/*TODO*///				if (xod4 & 0xff000000)
/*TODO*///				{
/*TODO*///					if (((1 << pridata[BL0]) & pmask) == 0)
/*TODO*///						dstdata[BL0] = paldata[col4 >> 24];
/*TODO*///					pridata[BL0] = 31;
/*TODO*///				}
/*TODO*///				if (xod4 & 0x00ff0000)
/*TODO*///				{
/*TODO*///					if (((1 << pridata[BL1]) & pmask) == 0)
/*TODO*///						dstdata[BL1] = paldata[(col4 >> 16) & 0xff];
/*TODO*///					pridata[BL1] = 31;
/*TODO*///				}
/*TODO*///				if (xod4 & 0x0000ff00)
/*TODO*///				{
/*TODO*///					if (((1 << pridata[BL2]) & pmask) == 0)
/*TODO*///						dstdata[BL2] = paldata[(col4 >>  8) & 0xff];
/*TODO*///					pridata[BL2] = 31;
/*TODO*///				}
/*TODO*///				if (xod4 & 0x000000ff)
/*TODO*///				{
/*TODO*///					if (((1 << pridata[BL3]) & pmask) == 0)
/*TODO*///						dstdata[BL3] = paldata[col4 & 0xff];
/*TODO*///					pridata[BL3] = 31;
/*TODO*///				}
/*TODO*///			}
/*TODO*///			dstdata += 4;
/*TODO*///			pridata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (col != transpen)
/*TODO*///			{
/*TODO*///				if (((1 << *pridata) & pmask) == 0)
/*TODO*///					*dstdata = paldata[col];
/*TODO*///				*pridata = 31;
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		pridata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transpen_raw,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		unsigned int colorbase,int transpen),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	int trans4;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	trans4 = transpen * 0x01010101;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (col != transpen) *dstdata = colorbase + col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			if ((col4 = *(sd4++)) != trans4)
/*TODO*///			{
/*TODO*///				UINT32 xod4;
/*TODO*///
/*TODO*///				xod4 = col4 ^ trans4;
/*TODO*///				if (xod4 & 0x000000ff) dstdata[BL0] = colorbase + ((col4) & 0xff);
/*TODO*///				if (xod4 & 0x0000ff00) dstdata[BL1] = colorbase + ((col4 >>  8) & 0xff);
/*TODO*///				if (xod4 & 0x00ff0000) dstdata[BL2] = colorbase + ((col4 >> 16) & 0xff);
/*TODO*///				if (xod4 & 0xff000000) dstdata[BL3] = colorbase + (col4 >> 24);
/*TODO*///			}
/*TODO*///			dstdata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (col != transpen) *dstdata = colorbase + col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transpen_raw_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		unsigned int colorbase, int transpen),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	int trans4;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///	srcdata -= 3;
/*TODO*///
/*TODO*///	trans4 = transpen * 0x01010101;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (col != transpen) *dstdata = colorbase + col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			if ((col4 = *(sd4--)) != trans4)
/*TODO*///			{
/*TODO*///				UINT32 xod4;
/*TODO*///
/*TODO*///				xod4 = col4 ^ trans4;
/*TODO*///				if (xod4 & 0xff000000) dstdata[BL0] = colorbase + (col4 >> 24);
/*TODO*///				if (xod4 & 0x00ff0000) dstdata[BL1] = colorbase + ((col4 >> 16) & 0xff);
/*TODO*///				if (xod4 & 0x0000ff00) dstdata[BL2] = colorbase + ((col4 >>  8) & 0xff);
/*TODO*///				if (xod4 & 0x000000ff) dstdata[BL3] = colorbase + (col4 & 0xff);
/*TODO*///			}
/*TODO*///			dstdata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (col != transpen) *dstdata = colorbase + col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///#define PEN_IS_OPAQUE ((1<<col)&transmask) == 0
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transmask,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (PEN_IS_OPAQUE) *dstdata = paldata[col];
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			col4 = *(sd4++);
/*TODO*///			col = (col4 >>  0) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL0] = paldata[col];
/*TODO*///			col = (col4 >>  8) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL1] = paldata[col];
/*TODO*///			col = (col4 >> 16) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL2] = paldata[col];
/*TODO*///			col = (col4 >> 24) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL3] = paldata[col];
/*TODO*///			dstdata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (PEN_IS_OPAQUE) *dstdata = paldata[col];
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transmask_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///	srcdata -= 3;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (PEN_IS_OPAQUE) *dstdata = paldata[col];
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			col4 = *(sd4--);
/*TODO*///			col = (col4 >> 24) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL0] = paldata[col];
/*TODO*///			col = (col4 >> 16) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL1] = paldata[col];
/*TODO*///			col = (col4 >>  8) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL2] = paldata[col];
/*TODO*///			col = (col4 >>  0) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL3] = paldata[col];
/*TODO*///			dstdata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (PEN_IS_OPAQUE) *dstdata = paldata[col];
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
    public static void blockmove_8toN_transmask_pri8(UBytePtr srcdata, int srcwidth, int srcheight, int srcmodulo, UBytePtr dstdata, int dstmodulo, UShortArray paldata, int transmask, UBytePtr pridata, int/*UINT32*/ pmask) {
        int end;
        IntPtr sd4;

        pmask |= (1 << 31);

        srcmodulo -= srcwidth;
        dstmodulo -= srcwidth;

        while (srcheight != 0) {
            end = dstdata.offset + srcwidth;
            while ((srcdata.offset & 3) != 0 && dstdata.offset < end)//while (((long)srcdata & 3) && dstdata < end)	/* longword align */
            {
                int col;
                col = srcdata.readinc();
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read()) & pmask) == 0) {
                        dstdata.write(paldata.read(col));
                    }
                    pridata.write(31);
                }
                dstdata.inc();
                pridata.inc();
            }
            sd4 = new IntPtr(srcdata);
            while (dstdata.offset <= end - 4) {
                int col;
                int col4;

                col4 = sd4.read(0);
                col = (col4 >>> 0) & 0xff;
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read(BL0)) & pmask) == 0) {
                        dstdata.write(BL0, paldata.read(col));
                    }
                    pridata.write(BL0, 31);
                }
                col = (col4 >>> 8) & 0xff;
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read(BL1)) & pmask) == 0) {
                        dstdata.write(BL1, paldata.read(col));
                    }
                    pridata.write(BL1, 31);
                }
                col = (col4 >>> 16) & 0xff;
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read(BL2)) & pmask) == 0) {
                        dstdata.write(BL2, paldata.read(col));
                    }
                    pridata.write(BL2, 31);
                }
                col = (col4 >>> 24) & 0xff;
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read(BL3)) & pmask) == 0) {
                        dstdata.write(BL3, paldata.read(col));
                    }
                    pridata.write(BL3, 31);
                }
                sd4.base += 4;
                dstdata.inc(4);
                pridata.inc(4);
            }
            srcdata.set(sd4.readCA(), sd4.getBase());//srcdata = (unsigned char *)sd4;
            while (dstdata.offset < end) {
                int col;

                col = srcdata.readinc();
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read()) & pmask) == 0) {
                        dstdata.write(paldata.read(col));
                    }
                    pridata.write(31);
                }
                dstdata.inc();
                pridata.inc();
            }

            srcdata.inc(srcmodulo);
            dstdata.inc(dstmodulo);
            pridata.inc(dstmodulo);
            srcheight--;
        }
    }

    /*TODO*///DECLARE(blockmove_8toN_transmask_pri,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transmask,UINT8 *pridata,UINT32 pmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	pmask |= (1<<31);
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << *pridata) & pmask) == 0)
/*TODO*///					*dstdata = paldata[col];
/*TODO*///				*pridata = 31;
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			col4 = *(sd4++);
/*TODO*///			col = (col4 >>  0) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << pridata[BL0]) & pmask) == 0)
/*TODO*///					dstdata[BL0] = paldata[col];
/*TODO*///				pridata[BL0] = 31;
/*TODO*///			}
/*TODO*///			col = (col4 >>  8) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << pridata[BL1]) & pmask) == 0)
/*TODO*///					dstdata[BL1] = paldata[col];
/*TODO*///				pridata[BL1] = 31;
/*TODO*///			}
/*TODO*///			col = (col4 >> 16) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << pridata[BL2]) & pmask) == 0)
/*TODO*///					dstdata[BL2] = paldata[col];
/*TODO*///				pridata[BL2] = 31;
/*TODO*///			}
/*TODO*///			col = (col4 >> 24) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << pridata[BL3]) & pmask) == 0)
/*TODO*///					dstdata[BL3] = paldata[col];
/*TODO*///				pridata[BL3] = 31;
/*TODO*///			}
/*TODO*///			dstdata += 4;
/*TODO*///			pridata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << *pridata) & pmask) == 0)
/*TODO*///					*dstdata = paldata[col];
/*TODO*///				*pridata = 31;
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		pridata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
    public static void blockmove_8toN_transmask_pri_flipx8(UBytePtr srcdata, int srcwidth, int srcheight, int srcmodulo, UBytePtr dstdata, int dstmodulo, UShortArray paldata, int transmask, UBytePtr pridata, int/*UINT32*/ pmask) {
        int end;
        IntPtr sd4;

        pmask |= (1 << 31);

        srcmodulo += srcwidth;
        dstmodulo -= srcwidth;
        //srcdata += srcwidth-1;
        srcdata.dec(3);

        while (srcheight != 0) {
            end = dstdata.offset + srcwidth;
            while ((srcdata.offset & 3) != 0 && dstdata.offset < end)//while (((long)srcdata & 3) && dstdata < end)	/* longword align */
            {
                int col;

                col = srcdata.read(3);
                srcdata.dec();
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read()) & pmask) == 0) {
                        dstdata.write(paldata.read(col));
                    }
                    pridata.write(31);
                }
                dstdata.inc();
                pridata.inc();
            }
            sd4 = new IntPtr(srcdata);
            while (dstdata.offset <= end - 4) {
                int col;
                int col4;
                col4 = sd4.read(0);
                col = (col4 >>> 24) & 0xff;
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read(BL0)) & pmask) == 0) {
                        dstdata.write(BL0, paldata.read(col));
                    }
                    pridata.write(BL0, 31);
                }
                col = (col4 >>> 16) & 0xff;
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read(BL1)) & pmask) == 0) {
                        dstdata.write(BL1, paldata.read(col));
                    }
                    pridata.write(BL1, 31);
                }
                col = (col4 >>> 8) & 0xff;
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read(BL2)) & pmask) == 0) {
                        dstdata.write(BL2, paldata.read(col));
                    }
                    pridata.write(BL2, 31);
                }
                col = (col4 >>> 0) & 0xff;
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read(BL3)) & pmask) == 0) {
                        dstdata.write(BL3, paldata.read(col));
                    }
                    pridata.write(BL3, 31);
                }
                sd4.base -= 4;
                dstdata.inc(4);
                pridata.inc(4);
            }
            srcdata.set(sd4.readCA(), sd4.getBase());//srcdata = (unsigned char *)sd4;
            while (dstdata.offset < end) {
                int col;
                col = srcdata.read(3);
                srcdata.dec();
                if (((1 << col) & transmask) == 0) {
                    if (((1 << pridata.read()) & pmask) == 0) {
                        dstdata.write(paldata.read(col));
                    }
                    pridata.write(31);
                }
                dstdata.inc();
                pridata.inc();
            }
            srcdata.inc(srcmodulo);
            dstdata.inc(dstmodulo);
            pridata.inc(dstmodulo);
            srcheight--;
        }
    }

    /*TODO*///DECLARE(blockmove_8toN_transmask_pri_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transmask,UINT8 *pridata,UINT32 pmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	pmask |= (1<<31);
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///	srcdata -= 3;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << *pridata) & pmask) == 0)
/*TODO*///					*dstdata = paldata[col];
/*TODO*///				*pridata = 31;
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			col4 = *(sd4--);
/*TODO*///			col = (col4 >> 24) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << pridata[BL0]) & pmask) == 0)
/*TODO*///					dstdata[BL0] = paldata[col];
/*TODO*///				pridata[BL0] = 31;
/*TODO*///			}
/*TODO*///			col = (col4 >> 16) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << pridata[BL1]) & pmask) == 0)
/*TODO*///					dstdata[BL1] = paldata[col];
/*TODO*///				pridata[BL1] = 31;
/*TODO*///			}
/*TODO*///			col = (col4 >>  8) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << pridata[BL2]) & pmask) == 0)
/*TODO*///					dstdata[BL2] = paldata[col];
/*TODO*///				pridata[BL2] = 31;
/*TODO*///			}
/*TODO*///			col = (col4 >>  0) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << pridata[BL3]) & pmask) == 0)
/*TODO*///					dstdata[BL3] = paldata[col];
/*TODO*///				pridata[BL3] = 31;
/*TODO*///			}
/*TODO*///			dstdata += 4;
/*TODO*///			pridata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (PEN_IS_OPAQUE)
/*TODO*///			{
/*TODO*///				if (((1 << *pridata) & pmask) == 0)
/*TODO*///					*dstdata = paldata[col];
/*TODO*///				*pridata = 31;
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		pridata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transmask_raw,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		unsigned int colorbase,int transmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (PEN_IS_OPAQUE) *dstdata = colorbase + col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			col4 = *(sd4++);
/*TODO*///			col = (col4 >>  0) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL0] = colorbase + col;
/*TODO*///			col = (col4 >>  8) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL1] = colorbase + col;
/*TODO*///			col = (col4 >> 16) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL2] = colorbase + col;
/*TODO*///			col = (col4 >> 24) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL3] = colorbase + col;
/*TODO*///			dstdata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (PEN_IS_OPAQUE) *dstdata = colorbase + col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transmask_raw_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		unsigned int colorbase,int transmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	UINT32 *sd4;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///	srcdata -= 3;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (((long)srcdata & 3) && dstdata < end)	/* longword align */
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (PEN_IS_OPAQUE) *dstdata = colorbase + col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///		sd4 = (UINT32 *)srcdata;
/*TODO*///		while (dstdata <= end - 4)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///			UINT32 col4;
/*TODO*///
/*TODO*///			col4 = *(sd4--);
/*TODO*///			col = (col4 >> 24) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL0] = colorbase + col;
/*TODO*///			col = (col4 >> 16) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL1] = colorbase + col;
/*TODO*///			col = (col4 >>  8) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL2] = colorbase + col;
/*TODO*///			col = (col4 >>  0) & 0xff;
/*TODO*///			if (PEN_IS_OPAQUE) dstdata[BL3] = colorbase + col;
/*TODO*///			dstdata += 4;
/*TODO*///		}
/*TODO*///		srcdata = (UINT8 *)sd4;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = srcdata[3];
/*TODO*///			srcdata--;
/*TODO*///			if (PEN_IS_OPAQUE) *dstdata = colorbase + col;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transcolor,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	const UINT16 *lookupdata = Machine->game_colortable + (paldata - Machine->remapped_colortable);
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (lookupdata[*srcdata] != transcolor) *dstdata = paldata[*srcdata];
/*TODO*///			srcdata++;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transcolor_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	const UINT16 *lookupdata = Machine->game_colortable + (paldata - Machine->remapped_colortable);
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (lookupdata[*srcdata] != transcolor) *dstdata = paldata[*srcdata];
/*TODO*///			srcdata--;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transcolor_pri,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transcolor,UINT8 *pridata,UINT32 pmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	const UINT16 *lookupdata = Machine->game_colortable + (paldata - Machine->remapped_colortable);
/*TODO*///
/*TODO*///	pmask |= (1<<31);
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (lookupdata[*srcdata] != transcolor)
/*TODO*///			{
/*TODO*///				if (((1 << *pridata) & pmask) == 0)
/*TODO*///					*dstdata = paldata[*srcdata];
/*TODO*///				*pridata = 31;
/*TODO*///			}
/*TODO*///			srcdata++;
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		pridata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transcolor_pri_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transcolor,UINT8 *pridata,UINT32 pmask),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///	const UINT16 *lookupdata = Machine->game_colortable + (paldata - Machine->remapped_colortable);
/*TODO*///
/*TODO*///	pmask |= (1<<31);
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (lookupdata[*srcdata] != transcolor)
/*TODO*///			{
/*TODO*///				if (((1 << *pridata) & pmask) == 0)
/*TODO*///					*dstdata = paldata[*srcdata];
/*TODO*///				*pridata = 31;
/*TODO*///			}
/*TODO*///			srcdata--;
/*TODO*///			dstdata++;
/*TODO*///			pridata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		pridata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transthrough,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (*dstdata == transcolor) *dstdata = paldata[*srcdata];
/*TODO*///			srcdata++;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transthrough_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (*dstdata == transcolor) *dstdata = paldata[*srcdata];
/*TODO*///			srcdata--;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transthrough_raw,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		unsigned int colorbase,int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (*dstdata == transcolor) *dstdata = colorbase + *srcdata;
/*TODO*///			srcdata++;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_transthrough_raw_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		unsigned int colorbase,int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (*dstdata == transcolor) *dstdata = colorbase + *srcdata;
/*TODO*///			srcdata--;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_pen_table,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (col != transcolor)
/*TODO*///			{
/*TODO*///				switch(gfx_drawmode_table[col])
/*TODO*///				{
/*TODO*///				case DRAWMODE_SOURCE:
/*TODO*///					*dstdata = paldata[col];
/*TODO*///					break;
/*TODO*///				case DRAWMODE_SHADOW:
/*TODO*///					*dstdata = palette_shadow_table[*dstdata];
/*TODO*///					break;
/*TODO*///				}
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_pen_table_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata--);
/*TODO*///			if (col != transcolor)
/*TODO*///			{
/*TODO*///				switch(gfx_drawmode_table[col])
/*TODO*///				{
/*TODO*///				case DRAWMODE_SOURCE:
/*TODO*///					*dstdata = paldata[col];
/*TODO*///					break;
/*TODO*///				case DRAWMODE_SHADOW:
/*TODO*///					*dstdata = palette_shadow_table[*dstdata];
/*TODO*///					break;
/*TODO*///				}
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_pen_table_raw,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		unsigned int colorbase,int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata++);
/*TODO*///			if (col != transcolor)
/*TODO*///			{
/*TODO*///				switch(gfx_drawmode_table[col])
/*TODO*///				{
/*TODO*///				case DRAWMODE_SOURCE:
/*TODO*///					*dstdata = colorbase + col;
/*TODO*///					break;
/*TODO*///				case DRAWMODE_SHADOW:
/*TODO*///					*dstdata = palette_shadow_table[*dstdata];
/*TODO*///					break;
/*TODO*///				}
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_8toN_pen_table_raw_flipx,(
/*TODO*///		const UINT8 *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		unsigned int colorbase,int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			int col;
/*TODO*///
/*TODO*///			col = *(srcdata--);
/*TODO*///			if (col != transcolor)
/*TODO*///			{
/*TODO*///				switch(gfx_drawmode_table[col])
/*TODO*///				{
/*TODO*///				case DRAWMODE_SOURCE:
/*TODO*///					*dstdata = colorbase + col;
/*TODO*///					break;
/*TODO*///				case DRAWMODE_SHADOW:
/*TODO*///					*dstdata = palette_shadow_table[*dstdata];
/*TODO*///					break;
/*TODO*///				}
/*TODO*///			}
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_NtoN_opaque_noremap,(
/*TODO*///		const DATA_TYPE *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo),
/*TODO*///{
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		memcpy(dstdata,srcdata,srcwidth * sizeof(DATA_TYPE));
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_NtoN_opaque_noremap_flipx,(
/*TODO*///		const DATA_TYPE *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			srcdata -= 8;
/*TODO*///			dstdata[0] = srcdata[8];
/*TODO*///			dstdata[1] = srcdata[7];
/*TODO*///			dstdata[2] = srcdata[6];
/*TODO*///			dstdata[3] = srcdata[5];
/*TODO*///			dstdata[4] = srcdata[4];
/*TODO*///			dstdata[5] = srcdata[3];
/*TODO*///			dstdata[6] = srcdata[2];
/*TODO*///			dstdata[7] = srcdata[1];
/*TODO*///			dstdata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///			*(dstdata++) = *(srcdata--);
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_NtoN_opaque_remap,(
/*TODO*///		const DATA_TYPE *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			dstdata[0] = paldata[srcdata[0]];
/*TODO*///			dstdata[1] = paldata[srcdata[1]];
/*TODO*///			dstdata[2] = paldata[srcdata[2]];
/*TODO*///			dstdata[3] = paldata[srcdata[3]];
/*TODO*///			dstdata[4] = paldata[srcdata[4]];
/*TODO*///			dstdata[5] = paldata[srcdata[5]];
/*TODO*///			dstdata[6] = paldata[srcdata[6]];
/*TODO*///			dstdata[7] = paldata[srcdata[7]];
/*TODO*///			dstdata += 8;
/*TODO*///			srcdata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///			*(dstdata++) = paldata[*(srcdata++)];
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_NtoN_opaque_remap_flipx,(
/*TODO*///		const DATA_TYPE *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			srcdata -= 8;
/*TODO*///			dstdata[0] = paldata[srcdata[8]];
/*TODO*///			dstdata[1] = paldata[srcdata[7]];
/*TODO*///			dstdata[2] = paldata[srcdata[6]];
/*TODO*///			dstdata[3] = paldata[srcdata[5]];
/*TODO*///			dstdata[4] = paldata[srcdata[4]];
/*TODO*///			dstdata[5] = paldata[srcdata[3]];
/*TODO*///			dstdata[6] = paldata[srcdata[2]];
/*TODO*///			dstdata[7] = paldata[srcdata[1]];
/*TODO*///			dstdata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///			*(dstdata++) = paldata[*(srcdata--)];
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///
/*TODO*///DECLARE(blockmove_NtoN_transthrough_noremap,(
/*TODO*///		const DATA_TYPE *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (*dstdata == transcolor) *dstdata = *srcdata;
/*TODO*///			srcdata++;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_NtoN_transthrough_noremap_flipx,(
/*TODO*///		const DATA_TYPE *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		int transcolor),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			if (*dstdata == transcolor) *dstdata = *srcdata;
/*TODO*///			srcdata--;
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_NtoN_blend_noremap,(
/*TODO*///		const DATA_TYPE *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		int srcshift),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			dstdata[0] |= srcdata[0] << srcshift;
/*TODO*///			dstdata[1] |= srcdata[1] << srcshift;
/*TODO*///			dstdata[2] |= srcdata[2] << srcshift;
/*TODO*///			dstdata[3] |= srcdata[3] << srcshift;
/*TODO*///			dstdata[4] |= srcdata[4] << srcshift;
/*TODO*///			dstdata[5] |= srcdata[5] << srcshift;
/*TODO*///			dstdata[6] |= srcdata[6] << srcshift;
/*TODO*///			dstdata[7] |= srcdata[7] << srcshift;
/*TODO*///			dstdata += 8;
/*TODO*///			srcdata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///			*(dstdata++) |= *(srcdata++) << srcshift;
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_NtoN_blend_noremap_flipx,(
/*TODO*///		const DATA_TYPE *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		int srcshift),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			srcdata -= 8;
/*TODO*///			dstdata[0] |= srcdata[8] << srcshift;
/*TODO*///			dstdata[1] |= srcdata[7] << srcshift;
/*TODO*///			dstdata[2] |= srcdata[6] << srcshift;
/*TODO*///			dstdata[3] |= srcdata[5] << srcshift;
/*TODO*///			dstdata[4] |= srcdata[4] << srcshift;
/*TODO*///			dstdata[5] |= srcdata[3] << srcshift;
/*TODO*///			dstdata[6] |= srcdata[2] << srcshift;
/*TODO*///			dstdata[7] |= srcdata[1] << srcshift;
/*TODO*///			dstdata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///			*(dstdata++) |= *(srcdata--) << srcshift;
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_NtoN_blend_remap,(
/*TODO*///		const DATA_TYPE *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int srcshift),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo -= srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			dstdata[0] = paldata[dstdata[0] | (srcdata[0] << srcshift)];
/*TODO*///			dstdata[1] = paldata[dstdata[1] | (srcdata[1] << srcshift)];
/*TODO*///			dstdata[2] = paldata[dstdata[2] | (srcdata[2] << srcshift)];
/*TODO*///			dstdata[3] = paldata[dstdata[3] | (srcdata[3] << srcshift)];
/*TODO*///			dstdata[4] = paldata[dstdata[4] | (srcdata[4] << srcshift)];
/*TODO*///			dstdata[5] = paldata[dstdata[5] | (srcdata[5] << srcshift)];
/*TODO*///			dstdata[6] = paldata[dstdata[6] | (srcdata[6] << srcshift)];
/*TODO*///			dstdata[7] = paldata[dstdata[7] | (srcdata[7] << srcshift)];
/*TODO*///			dstdata += 8;
/*TODO*///			srcdata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			*dstdata = paldata[*dstdata | (*(srcdata++) << srcshift)];
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(blockmove_NtoN_blend_remap_flipx,(
/*TODO*///		const DATA_TYPE *srcdata,int srcwidth,int srcheight,int srcmodulo,
/*TODO*///		DATA_TYPE *dstdata,int dstmodulo,
/*TODO*///		const UINT16 *paldata,int srcshift),
/*TODO*///{
/*TODO*///	DATA_TYPE *end;
/*TODO*///
/*TODO*///	srcmodulo += srcwidth;
/*TODO*///	dstmodulo -= srcwidth;
/*TODO*///	//srcdata += srcwidth-1;
/*TODO*///
/*TODO*///	while (srcheight)
/*TODO*///	{
/*TODO*///		end = dstdata + srcwidth;
/*TODO*///		while (dstdata <= end - 8)
/*TODO*///		{
/*TODO*///			srcdata -= 8;
/*TODO*///			dstdata[0] = paldata[dstdata[0] | (srcdata[8] << srcshift)];
/*TODO*///			dstdata[1] = paldata[dstdata[1] | (srcdata[7] << srcshift)];
/*TODO*///			dstdata[2] = paldata[dstdata[2] | (srcdata[6] << srcshift)];
/*TODO*///			dstdata[3] = paldata[dstdata[3] | (srcdata[5] << srcshift)];
/*TODO*///			dstdata[4] = paldata[dstdata[4] | (srcdata[4] << srcshift)];
/*TODO*///			dstdata[5] = paldata[dstdata[5] | (srcdata[3] << srcshift)];
/*TODO*///			dstdata[6] = paldata[dstdata[6] | (srcdata[2] << srcshift)];
/*TODO*///			dstdata[7] = paldata[dstdata[7] | (srcdata[1] << srcshift)];
/*TODO*///			dstdata += 8;
/*TODO*///		}
/*TODO*///		while (dstdata < end)
/*TODO*///		{
/*TODO*///			*dstdata = paldata[*dstdata | (*(srcdata--) << srcshift)];
/*TODO*///			dstdata++;
/*TODO*///		}
/*TODO*///
/*TODO*///		srcdata += srcmodulo;
/*TODO*///		dstdata += dstmodulo;
/*TODO*///		srcheight--;
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///
/*TODO*///DECLARE(drawgfx_core,(
/*TODO*///		struct osd_bitmap *dest,const struct GfxElement *gfx,
/*TODO*///		unsigned int code,unsigned int color,int flipx,int flipy,int sx,int sy,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color,
/*TODO*///		struct osd_bitmap *pri_buffer,UINT32 pri_mask),
/*TODO*///{
/*TODO*///	int ox;
/*TODO*///	int oy;
/*TODO*///	int ex;
/*TODO*///	int ey;
/*TODO*///
/*TODO*///
/*TODO*///	/* check bounds */
/*TODO*///	ox = sx;
/*TODO*///	oy = sy;
/*TODO*///
/*TODO*///	ex = sx + gfx->width-1;
/*TODO*///	if (sx < 0) sx = 0;
/*TODO*///	if (clip && sx < clip->min_x) sx = clip->min_x;
/*TODO*///	if (ex >= dest->width) ex = dest->width-1;
/*TODO*///	if (clip && ex > clip->max_x) ex = clip->max_x;
/*TODO*///	if (sx > ex) return;
/*TODO*///
/*TODO*///	ey = sy + gfx->height-1;
/*TODO*///	if (sy < 0) sy = 0;
/*TODO*///	if (clip && sy < clip->min_y) sy = clip->min_y;
/*TODO*///	if (ey >= dest->height) ey = dest->height-1;
/*TODO*///	if (clip && ey > clip->max_y) ey = clip->max_y;
/*TODO*///	if (sy > ey) return;
/*TODO*///
/*TODO*///	if (Machine->drv->video_attributes & VIDEO_SUPPORTS_DIRTY)
/*TODO*///		osd_mark_dirty (sx,sy,ex,ey,0);	/* ASG 971011 */
/*TODO*///
/*TODO*///	{
/*TODO*///		UINT8 *sd = gfx->gfxdata + code * gfx->char_modulo;		/* source data */
/*TODO*///		int sw = ex-sx+1;										/* source width */
/*TODO*///		int sh = ey-sy+1;										/* source height */
/*TODO*///		int sm = gfx->line_modulo;								/* source modulo */
/*TODO*///		DATA_TYPE *dd = ((DATA_TYPE *)dest->line[sy]) + sx;		/* dest data */
/*TODO*///		int dm = ((DATA_TYPE *)dest->line[1])-((DATA_TYPE *)dest->line[0]);	/* dest modulo */
/*TODO*///		const UINT16 *paldata = &gfx->colortable[gfx->color_granularity * color];
/*TODO*///		UINT8 *pribuf = (pri_buffer) ? pri_buffer->line[sy] + sx : NULL;
/*TODO*///
/*TODO*///		if (flipx)
/*TODO*///		{
/*TODO*///			//if ((sx-ox) == 0) sd += gfx->width - sw;
/*TODO*///			sd += gfx->width -1 -(sx-ox);
/*TODO*///		}
/*TODO*///		else
/*TODO*///			sd += (sx-ox);
/*TODO*///
/*TODO*///		if (flipy)
/*TODO*///		{
/*TODO*///			//if ((sy-oy) == 0) sd += sm * (gfx->height - sh);
/*TODO*///			//dd += dm * (sh - 1);
/*TODO*///			//dm = -dm;
/*TODO*///			sd += sm * (gfx->height -1 -(sy-oy));
/*TODO*///			sm = -sm;
/*TODO*///		}
/*TODO*///		else
/*TODO*///			sd += sm * (sy-oy);
/*TODO*///
/*TODO*///		switch (transparency)
/*TODO*///		{
/*TODO*///			case TRANSPARENCY_NONE:
/*TODO*///				if (pribuf)
/*TODO*///					BLOCKMOVE(8toN_opaque_pri,flipx,(sd,sw,sh,sm,dd,dm,paldata,pribuf,pri_mask));
/*TODO*///				else
/*TODO*///					BLOCKMOVE(8toN_opaque,flipx,(sd,sw,sh,sm,dd,dm,paldata));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_PEN:
/*TODO*///				if (pribuf)
/*TODO*///					BLOCKMOVE(8toN_transpen_pri,flipx,(sd,sw,sh,sm,dd,dm,paldata,transparent_color,pribuf,pri_mask));
/*TODO*///				else
/*TODO*///					BLOCKMOVE(8toN_transpen,flipx,(sd,sw,sh,sm,dd,dm,paldata,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_PENS:
/*TODO*///				if (pribuf)
/*TODO*///					BLOCKMOVE(8toN_transmask_pri,flipx,(sd,sw,sh,sm,dd,dm,paldata,transparent_color,pribuf,pri_mask));
/*TODO*///				else
/*TODO*///					BLOCKMOVE(8toN_transmask,flipx,(sd,sw,sh,sm,dd,dm,paldata,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_COLOR:
/*TODO*///				if (pribuf)
/*TODO*///					BLOCKMOVE(8toN_transcolor_pri,flipx,(sd,sw,sh,sm,dd,dm,paldata,transparent_color,pribuf,pri_mask));
/*TODO*///				else
/*TODO*///					BLOCKMOVE(8toN_transcolor,flipx,(sd,sw,sh,sm,dd,dm,paldata,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_THROUGH:
/*TODO*///				if (pribuf)
/*TODO*///usrintf_showmessage("pdrawgfx TRANS_THROUGH not supported");
/*TODO*/////					BLOCKMOVE(8toN_transthrough,flipx,(sd,sw,sh,sm,dd,dm,paldata,transparent_color));
/*TODO*///				else
/*TODO*///					BLOCKMOVE(8toN_transthrough,flipx,(sd,sw,sh,sm,dd,dm,paldata,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_PEN_TABLE:
/*TODO*///				if (pribuf)
/*TODO*///usrintf_showmessage("pdrawgfx TRANS_PEN_TABLE not supported");
/*TODO*/////					BLOCKMOVE(8toN_pen_table_pri,flipx,(sd,sw,sh,sm,dd,dm,paldata,transparent_color));
/*TODO*///				else
/*TODO*///					BLOCKMOVE(8toN_pen_table,flipx,(sd,sw,sh,sm,dd,dm,paldata,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_PEN_TABLE_RAW:
/*TODO*///				if (pribuf)
/*TODO*///usrintf_showmessage("pdrawgfx TRANS_PEN_TABLE_RAW not supported");
/*TODO*/////					BLOCKMOVE(8toN_pen_table_pri_raw,flipx,(sd,sw,sh,sm,dd,dm,color,transparent_color));
/*TODO*///				else
/*TODO*///					BLOCKMOVE(8toN_pen_table_raw,flipx,(sd,sw,sh,sm,dd,dm,color,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_NONE_RAW:
/*TODO*///				if (pribuf)
/*TODO*///usrintf_showmessage("pdrawgfx TRANS_NONE_RAW not supported");
/*TODO*/////					BLOCKMOVE(8toN_opaque_pri_raw,flipx,(sd,sw,sh,sm,dd,dm,paldata,pribuf,pri_mask));
/*TODO*///				else
/*TODO*///					BLOCKMOVE(8toN_opaque_raw,flipx,(sd,sw,sh,sm,dd,dm,color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_PEN_RAW:
/*TODO*///				if (pribuf)
/*TODO*///usrintf_showmessage("pdrawgfx TRANS_PEN_RAW not supported");
/*TODO*/////					BLOCKMOVE(8toN_transpen_pri_raw,flipx,(sd,sw,sh,sm,dd,dm,paldata,pribuf,pri_mask));
/*TODO*///				else
/*TODO*///					BLOCKMOVE(8toN_transpen_raw,flipx,(sd,sw,sh,sm,dd,dm,color,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_PENS_RAW:
/*TODO*///				if (pribuf)
/*TODO*///usrintf_showmessage("pdrawgfx TRANS_PENS_RAW not supported");
/*TODO*/////					BLOCKMOVE(8toN_transmask_pri_raw,flipx,(sd,sw,sh,sm,dd,dm,paldata,pribuf,pri_mask));
/*TODO*///				else
/*TODO*///					BLOCKMOVE(8toN_transmask_raw,flipx,(sd,sw,sh,sm,dd,dm,color,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_THROUGH_RAW:
/*TODO*///				if (pribuf)
/*TODO*///usrintf_showmessage("pdrawgfx TRANS_PEN_RAW not supported");
/*TODO*/////					BLOCKMOVE(8toN_transpen_pri_raw,flipx,(sd,sw,sh,sm,dd,dm,paldata,pribuf,pri_mask));
/*TODO*///				else
/*TODO*///					BLOCKMOVE(8toN_transthrough_raw,flipx,(sd,sw,sh,sm,dd,dm,color,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			default:
/*TODO*///				if (pribuf)
/*TODO*///					usrintf_showmessage("pdrawgfx pen mode not supported");
/*TODO*///				else
/*TODO*///					usrintf_showmessage("drawgfx pen mode not supported");
/*TODO*///				break;
/*TODO*///		}
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE(copybitmap_core,(
/*TODO*///		struct osd_bitmap *dest,struct osd_bitmap *src,
/*TODO*///		int flipx,int flipy,int sx,int sy,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color),
/*TODO*///{
/*TODO*///	int ox;
/*TODO*///	int oy;
/*TODO*///	int ex;
/*TODO*///	int ey;
/*TODO*///
/*TODO*///
/*TODO*///	/* check bounds */
/*TODO*///	ox = sx;
/*TODO*///	oy = sy;
/*TODO*///
/*TODO*///	ex = sx + src->width-1;
/*TODO*///	if (sx < 0) sx = 0;
/*TODO*///	if (clip && sx < clip->min_x) sx = clip->min_x;
/*TODO*///	if (ex >= dest->width) ex = dest->width-1;
/*TODO*///	if (clip && ex > clip->max_x) ex = clip->max_x;
/*TODO*///	if (sx > ex) return;
/*TODO*///
/*TODO*///	ey = sy + src->height-1;
/*TODO*///	if (sy < 0) sy = 0;
/*TODO*///	if (clip && sy < clip->min_y) sy = clip->min_y;
/*TODO*///	if (ey >= dest->height) ey = dest->height-1;
/*TODO*///	if (clip && ey > clip->max_y) ey = clip->max_y;
/*TODO*///	if (sy > ey) return;
/*TODO*///
/*TODO*///	{
/*TODO*///		DATA_TYPE *sd = ((DATA_TYPE *)src->line[0]);							/* source data */
/*TODO*///		int sw = ex-sx+1;														/* source width */
/*TODO*///		int sh = ey-sy+1;														/* source height */
/*TODO*///		int sm = ((DATA_TYPE *)src->line[1])-((DATA_TYPE *)src->line[0]);		/* source modulo */
/*TODO*///		DATA_TYPE *dd = ((DATA_TYPE *)dest->line[sy]) + sx;						/* dest data */
/*TODO*///		int dm = ((DATA_TYPE *)dest->line[1])-((DATA_TYPE *)dest->line[0]);		/* dest modulo */
/*TODO*///
/*TODO*///		if (flipx)
/*TODO*///		{
/*TODO*///			//if ((sx-ox) == 0) sd += gfx->width - sw;
/*TODO*///			sd += src->width -1 -(sx-ox);
/*TODO*///		}
/*TODO*///		else
/*TODO*///			sd += (sx-ox);
/*TODO*///
/*TODO*///		if (flipy)
/*TODO*///		{
/*TODO*///			//if ((sy-oy) == 0) sd += sm * (gfx->height - sh);
/*TODO*///			//dd += dm * (sh - 1);
/*TODO*///			//dm = -dm;
/*TODO*///			sd += sm * (src->height -1 -(sy-oy));
/*TODO*///			sm = -sm;
/*TODO*///		}
/*TODO*///		else
/*TODO*///			sd += sm * (sy-oy);
/*TODO*///
/*TODO*///		switch (transparency)
/*TODO*///		{
/*TODO*///			case TRANSPARENCY_NONE:
/*TODO*///				BLOCKMOVE(NtoN_opaque_remap,flipx,(sd,sw,sh,sm,dd,dm,Machine->pens));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_NONE_RAW:
/*TODO*///				BLOCKMOVE(NtoN_opaque_noremap,flipx,(sd,sw,sh,sm,dd,dm));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_PEN_RAW:
/*TODO*///				BLOCKMOVE(NtoN_transpen_noremap,flipx,(sd,sw,sh,sm,dd,dm,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_THROUGH_RAW:
/*TODO*///				BLOCKMOVE(NtoN_transthrough_noremap,flipx,(sd,sw,sh,sm,dd,dm,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_BLEND:
/*TODO*///				BLOCKMOVE(NtoN_blend_remap,flipx,(sd,sw,sh,sm,dd,dm,Machine->pens,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			case TRANSPARENCY_BLEND_RAW:
/*TODO*///				BLOCKMOVE(NtoN_blend_noremap,flipx,(sd,sw,sh,sm,dd,dm,transparent_color));
/*TODO*///				break;
/*TODO*///
/*TODO*///			default:
/*TODO*///				usrintf_showmessage("copybitmap pen mode not supported");
/*TODO*///				break;
/*TODO*///		}
/*TODO*///	}
/*TODO*///})
/*TODO*///
    public static void copyrozbitmap_core8(osd_bitmap bitmap, osd_bitmap srcbitmap,
            long u32_startx, long u32_starty, int incxx, int incxy, int incyx, int incyy, int wraparound,
            rectangle clip, int transparency, int transparent_color,/*UINT32*/ int priority) {
        long u32_cx;
        long u32_cy;
        int x;
        int sx;
        int sy;
        int ex;
        int ey;
        int xmask = srcbitmap.width - 1;
        int ymask = srcbitmap.height - 1;
        int widthshifted = srcbitmap.width << 16;
        int heightshifted = srcbitmap.height << 16;
        UBytePtr dest;

        if (clip != null) {
            u32_startx = (u32_startx + clip.min_x * incxx + clip.min_y * incyx) & 0xFFFFFFFFL;
            u32_starty = (u32_starty + clip.min_x * incxy + clip.min_y * incyy) & 0xFFFFFFFFL;

            sx = clip.min_x;
            sy = clip.min_y;
            ex = clip.max_x;
            ey = clip.max_y;
        } else {
            sx = 0;
            sy = 0;
            ex = bitmap.width - 1;
            ey = bitmap.height - 1;
        }

        if ((Machine.orientation & ORIENTATION_SWAP_XY) != 0) {
            int t;

            t = (int) u32_startx;
            u32_startx = u32_starty;
            u32_starty = t & 0xFFFFFFFFL;
            t = sx;
            sx = sy;
            sy = t;
            t = ex;
            ex = ey;
            ey = t;
            t = incxx;
            incxx = incyy;
            incyy = t;
            t = incxy;
            incxy = incyx;
            incyx = t;
        }

        if ((Machine.orientation & ORIENTATION_FLIP_X) != 0) {
            int w = ex - sx;

            incxy = -incxy;
            incyx = -incyx;
            u32_startx = (widthshifted - u32_startx - 1) & 0xFFFFFFFFL;
            u32_startx = (u32_startx - incxx * w) & 0xFFFFFFFFL;
            u32_starty = (u32_starty - incxy * w) & 0xFFFFFFFFL;

            w = sx;
            sx = bitmap.width - 1 - ex;
            ex = bitmap.width - 1 - w;
        }

        if ((Machine.orientation & ORIENTATION_FLIP_Y) != 0) {
            int h = ey - sy;

            incxy = -incxy;
            incyx = -incyx;
            u32_starty = (heightshifted - u32_starty - 1) & 0xFFFFFFFFL;
            u32_startx = (u32_startx - incyx * h) & 0xFFFFFFFFL;
            u32_starty = (u32_starty - incyy * h) & 0xFFFFFFFFL;

            h = sy;
            sy = bitmap.height - 1 - ey;
            ey = bitmap.height - 1 - h;
        }
        if (incxy == 0 && incyx == 0 && wraparound == 0) {
            /* optimized loop for the not rotated case */

            if (incxx == 0x10000) {
                /* optimized loop for the not zoomed case */

 /* startx is unsigned */
                u32_startx = (((int) u32_startx) >>> 16) & 0xFFFFFFFFL;

                if (u32_startx >= srcbitmap.width) {
                    sx += -u32_startx;
                    u32_startx = 0;
                }

                if (sx <= ex) {
                    while (sy <= ey) {
                        if (u32_starty < heightshifted) {
                            x = sx;
                            u32_cx = u32_startx & 0xFFFFFFFFL;
                            u32_cy = (u32_starty >>> 16) & 0xFFFFFFFFL;
                            dest = new UBytePtr(bitmap.line[sy], sx);
                            if (priority != 0) {
                                UBytePtr pri = new UBytePtr(priority_bitmap.line[sy], sx);
                                UBytePtr src = new UBytePtr(srcbitmap.line[(int) u32_cy]);

                                while (x <= ex && u32_cx < srcbitmap.width) {
                                    int c = src.read((int) u32_cx);

                                    if (c != transparent_color) {
                                        dest.write(c);
                                        pri.write(pri.read() | priority);
                                    }

                                    u32_cx = (u32_cx + 1) & 0xFFFFFFFFL;
                                    x++;
                                    dest.inc();
                                    pri.inc();
                                }
                            } else {
                                UBytePtr src = new UBytePtr(srcbitmap.line[(int) u32_cy]);

                                while (x <= ex && u32_cx < srcbitmap.width) {
                                    int c = src.read((int) u32_cx);

                                    if (c != transparent_color) {
                                        dest.write(c);
                                    }

                                    u32_cx = (u32_cx + 1) & 0xFFFFFFFFL;
                                    x++;
                                    dest.inc();
                                }
                            }
                        }
                        u32_starty = (u32_starty + incyy) & 0xFFFFFFFFL;
                        sy++;
                    }
                }
            } else {
                while (u32_startx >= widthshifted && sx <= ex) {
                    u32_startx = (u32_startx + incxx) & 0xFFFFFFFFL;
                    sx++;
                }

                if (sx <= ex) {
                    while (sy <= ey) {
                        if (u32_starty < heightshifted) {
                            x = sx;
                            u32_cx = u32_startx & 0xFFFFFFFFL;
                            u32_cy = (u32_starty >>> 16) & 0xFFFFFFFFL;
                            dest = new UBytePtr(bitmap.line[sy], sx);
                            if (priority != 0) {
                                UBytePtr pri = new UBytePtr(priority_bitmap.line[sy], sx);
                                UBytePtr src = new UBytePtr(srcbitmap.line[(int) u32_cy]);
                                while (x <= ex && u32_cx < widthshifted) {
                                    int c = src.read((int) ((u32_cx >>> 16) & 0xFFFFFFFFL));

                                    if (c != transparent_color) {
                                        dest.write(c);
                                        pri.write(pri.read() | priority);
                                    }
                                    u32_cx = (u32_cx + incxx) & 0xFFFFFFFFL;
                                    x++;
                                    dest.inc();
                                    pri.inc();
                                }
                            } else {
                                UBytePtr src = new UBytePtr(srcbitmap.line[(int) u32_cy]);

                                while (x <= ex && u32_cx < widthshifted) {
                                    int c = src.read((int) ((u32_cx >>> 16) & 0xFFFFFFFFL));

                                    if (c != transparent_color) {
                                        dest.write(c);
                                    }

                                    u32_cx = (u32_cx + incxx) & 0xFFFFFFFFL;
                                    x++;
                                    dest.inc();
                                }
                            }
                        }
                        u32_starty = (u32_starty + incyy) & 0xFFFFFFFFL;
                        sy++;
                    }
                }
            }
        } else {
            if (wraparound != 0) {
                /* plot with wraparound */
                while (sy <= ey) {
                    x = sx;
                    u32_cx = u32_startx;
                    u32_cy = u32_starty;
                    dest = new UBytePtr(bitmap.line[sy], sx);
                    if (priority != 0) {
                        UBytePtr pri = new UBytePtr(priority_bitmap.line[sy], sx);

                        while (x <= ex) {
                            UBytePtr c1 = new UBytePtr(srcbitmap.line[(int) (((u32_cy >>> 16) & xmask) & 0xFFFFFFFFL)]);
                            int c = c1.read((int) (((u32_cx >>> 16) & ymask) & 0xFFFFFFFFL));

                            if (c != transparent_color) {
                                dest.write(c);
                                pri.write(pri.read() | priority);
                            }

                            u32_cx = (u32_cx + incxx) & 0xFFFFFFFFL;;
                            u32_cy = (u32_cy + incxy) & 0xFFFFFFFFL;
                            x++;
                            dest.inc();
                            pri.inc();
                        }
                    } else {
                        while (x <= ex) {
                            UBytePtr c1 = new UBytePtr(srcbitmap.line[(int) (((u32_cy >>> 16) & xmask) & 0xFFFFFFFFL)]);
                            int c = c1.read((int) (((u32_cx >>> 16) & ymask) & 0xFFFFFFFFL));

                            if (c != transparent_color) {
                                dest.write(c);
                            }

                            u32_cx = (u32_cx + incxx) & 0xFFFFFFFFL;;
                            u32_cy = (u32_cy + incxy) & 0xFFFFFFFFL;
                            x++;
                            dest.inc();
                        }
                    }
                    u32_startx = (u32_startx + incyx) & 0xFFFFFFFFL;
                    u32_starty = (u32_starty + incyy) & 0xFFFFFFFFL;
                    sy++;
                }
            } else {
                while (sy <= ey) {
                    x = sx;
                    u32_cx = u32_startx;
                    u32_cy = u32_starty;
                    dest = new UBytePtr(bitmap.line[sy], sx);
                    if (priority != 0) {
                        UBytePtr pri = new UBytePtr(priority_bitmap.line[sy], sx);

                        while (x <= ex) {
                            if (u32_cx < widthshifted && u32_cy < heightshifted) {
                                UBytePtr c1 = new UBytePtr(srcbitmap.line[(int) ((u32_cy >>> 16) & 0xFFFFFFFFL)]);
                                int c = c1.read((int) ((u32_cx >>> 16) & 0xFFFFFFFFL));

                                if (c != transparent_color) {
                                    dest.write(c);
                                    pri.write(pri.read() | priority);
                                }
                            }

                            u32_cx = (u32_cx + incxx) & 0xFFFFFFFFL;;
                            u32_cy = (u32_cy + incxy) & 0xFFFFFFFFL;
                            x++;
                            dest.inc();
                            pri.inc();
                        }
                    } else {
                        while (x <= ex) {
                            if (u32_cx < widthshifted && u32_cy < heightshifted) {
                                UBytePtr c1 = new UBytePtr(srcbitmap.line[(int) ((u32_cy >>> 16) & 0xFFFFFFFFL)]);
                                int c = c1.read((int) ((u32_cx >>> 16) & 0xFFFFFFFFL));

                                if (c != transparent_color) {
                                    dest.write(c);
                                }
                            }

                            u32_cx = (u32_cx + incxx) & 0xFFFFFFFFL;;
                            u32_cy = (u32_cy + incxy) & 0xFFFFFFFFL;
                            x++;
                            dest.inc();
                        }
                    }
                    u32_startx = (u32_startx + incyx) & 0xFFFFFFFFL;
                    u32_starty = (u32_starty + incyy) & 0xFFFFFFFFL;
                    sy++;
                }
            }
        }
    }
    /*TODO*///DECLARE(copyrozbitmap_core,(struct osd_bitmap *bitmap,struct osd_bitmap *srcbitmap,
/*TODO*///		UINT32 startx,UINT32 starty,int incxx,int incxy,int incyx,int incyy,int wraparound,
/*TODO*///		const struct rectangle *clip,int transparency,int transparent_color,UINT32 priority),
/*TODO*///{
/*TODO*///	UINT32 cx;
/*TODO*///	UINT32 cy;
/*TODO*///	int x;
/*TODO*///	int sx;
/*TODO*///	int sy;
/*TODO*///	int ex;
/*TODO*///	int ey;
/*TODO*///	const int xmask = srcbitmap->width-1;
/*TODO*///	const int ymask = srcbitmap->height-1;
/*TODO*///	const int widthshifted = srcbitmap->width << 16;
/*TODO*///	const int heightshifted = srcbitmap->height << 16;
/*TODO*///	DATA_TYPE *dest;
/*TODO*///
/*TODO*///
/*TODO*///	if (clip)
/*TODO*///	{
/*TODO*///		startx += clip->min_x * incxx + clip->min_y * incyx;
/*TODO*///		starty += clip->min_x * incxy + clip->min_y * incyy;
/*TODO*///
/*TODO*///		sx = clip->min_x;
/*TODO*///		sy = clip->min_y;
/*TODO*///		ex = clip->max_x;
/*TODO*///		ey = clip->max_y;
/*TODO*///	}
/*TODO*///	else
/*TODO*///	{
/*TODO*///		sx = 0;
/*TODO*///		sy = 0;
/*TODO*///		ex = bitmap->width-1;
/*TODO*///		ey = bitmap->height-1;
/*TODO*///	}
/*TODO*///
/*TODO*///
/*TODO*///	if (Machine->orientation & ORIENTATION_SWAP_XY)
/*TODO*///	{
/*TODO*///		int t;
/*TODO*///
/*TODO*///		t = startx; startx = starty; starty = t;
/*TODO*///		t = sx; sx = sy; sy = t;
/*TODO*///		t = ex; ex = ey; ey = t;
/*TODO*///		t = incxx; incxx = incyy; incyy = t;
/*TODO*///		t = incxy; incxy = incyx; incyx = t;
/*TODO*///	}
/*TODO*///
/*TODO*///	if (Machine->orientation & ORIENTATION_FLIP_X)
/*TODO*///	{
/*TODO*///		int w = ex - sx;
/*TODO*///
/*TODO*///		incxy = -incxy;
/*TODO*///		incyx = -incyx;
/*TODO*///		startx = widthshifted - startx - 1;
/*TODO*///		startx -= incxx * w;
/*TODO*///		starty -= incxy * w;
/*TODO*///
/*TODO*///		w = sx;
/*TODO*///		sx = bitmap->width-1 - ex;
/*TODO*///		ex = bitmap->width-1 - w;
/*TODO*///	}
/*TODO*///
/*TODO*///	if (Machine->orientation & ORIENTATION_FLIP_Y)
/*TODO*///	{
/*TODO*///		int h = ey - sy;
/*TODO*///
/*TODO*///		incxy = -incxy;
/*TODO*///		incyx = -incyx;
/*TODO*///		starty = heightshifted - starty - 1;
/*TODO*///		startx -= incyx * h;
/*TODO*///		starty -= incyy * h;
/*TODO*///
/*TODO*///		h = sy;
/*TODO*///		sy = bitmap->height-1 - ey;
/*TODO*///		ey = bitmap->height-1 - h;
/*TODO*///	}
/*TODO*///
/*TODO*///	if (incxy == 0 && incyx == 0 && !wraparound)
/*TODO*///	{
/*TODO*///		/* optimized loop for the not rotated case */
/*TODO*///
/*TODO*///		if (incxx == 0x10000)
/*TODO*///		{
/*TODO*///			/* optimized loop for the not zoomed case */
/*TODO*///
/*TODO*///			/* startx is unsigned */
/*TODO*///			startx = ((INT32)startx) >> 16;
/*TODO*///
/*TODO*///			if (startx >= srcbitmap->width)
/*TODO*///			{
/*TODO*///				sx += -startx;
/*TODO*///				startx = 0;
/*TODO*///			}
/*TODO*///
/*TODO*///			if (sx <= ex)
/*TODO*///			{
/*TODO*///				while (sy <= ey)
/*TODO*///				{
/*TODO*///					if (starty < heightshifted)
/*TODO*///					{
/*TODO*///						x = sx;
/*TODO*///						cx = startx;
/*TODO*///						cy = starty >> 16;
/*TODO*///						dest = ((DATA_TYPE *)bitmap->line[sy]) + sx;
/*TODO*///						if (priority)
/*TODO*///						{
/*TODO*///							UINT8 *pri = &priority_bitmap->line[sy][sx];
/*TODO*///							DATA_TYPE *src = (DATA_TYPE *)srcbitmap->line[cy];
/*TODO*///
/*TODO*///							while (x <= ex && cx < srcbitmap->width)
/*TODO*///							{
/*TODO*///								int c = src[cx];
/*TODO*///
/*TODO*///								if (c != transparent_color)
/*TODO*///								{
/*TODO*///									*dest = c;
/*TODO*///									*pri |= priority;
/*TODO*///								}
/*TODO*///
/*TODO*///								cx++;
/*TODO*///								x++;
/*TODO*///								dest++;
/*TODO*///								pri++;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							DATA_TYPE *src = (DATA_TYPE *)srcbitmap->line[cy];
/*TODO*///
/*TODO*///							while (x <= ex && cx < srcbitmap->width)
/*TODO*///							{
/*TODO*///								int c = src[cx];
/*TODO*///
/*TODO*///								if (c != transparent_color)
/*TODO*///									*dest = c;
/*TODO*///
/*TODO*///								cx++;
/*TODO*///								x++;
/*TODO*///								dest++;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///					starty += incyy;
/*TODO*///					sy++;
/*TODO*///				}
/*TODO*///			}
/*TODO*///		}
/*TODO*///		else
/*TODO*///		{
/*TODO*///			while (startx >= widthshifted && sx <= ex)
/*TODO*///			{
/*TODO*///				startx += incxx;
/*TODO*///				sx++;
/*TODO*///			}
/*TODO*///
/*TODO*///			if (sx <= ex)
/*TODO*///			{
/*TODO*///				while (sy <= ey)
/*TODO*///				{
/*TODO*///					if (starty < heightshifted)
/*TODO*///					{
/*TODO*///						x = sx;
/*TODO*///						cx = startx;
/*TODO*///						cy = starty >> 16;
/*TODO*///						dest = ((DATA_TYPE *)bitmap->line[sy]) + sx;
/*TODO*///						if (priority)
/*TODO*///						{
/*TODO*///							UINT8 *pri = &priority_bitmap->line[sy][sx];
/*TODO*///							DATA_TYPE *src = (DATA_TYPE *)srcbitmap->line[cy];
/*TODO*///
/*TODO*///							while (x <= ex && cx < widthshifted)
/*TODO*///							{
/*TODO*///								int c = src[cx >> 16];
/*TODO*///
/*TODO*///								if (c != transparent_color)
/*TODO*///								{
/*TODO*///									*dest = c;
/*TODO*///									*pri |= priority;
/*TODO*///								}
/*TODO*///
/*TODO*///								cx += incxx;
/*TODO*///								x++;
/*TODO*///								dest++;
/*TODO*///								pri++;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else
/*TODO*///						{
/*TODO*///							DATA_TYPE *src = (DATA_TYPE *)srcbitmap->line[cy];
/*TODO*///
/*TODO*///							while (x <= ex && cx < widthshifted)
/*TODO*///							{
/*TODO*///								int c = src[cx >> 16];
/*TODO*///
/*TODO*///								if (c != transparent_color)
/*TODO*///									*dest = c;
/*TODO*///
/*TODO*///								cx += incxx;
/*TODO*///								x++;
/*TODO*///								dest++;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///					starty += incyy;
/*TODO*///					sy++;
/*TODO*///				}
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else
/*TODO*///	{
/*TODO*///		if (wraparound)
/*TODO*///		{
/*TODO*///			/* plot with wraparound */
/*TODO*///			while (sy <= ey)
/*TODO*///			{
/*TODO*///				x = sx;
/*TODO*///				cx = startx;
/*TODO*///				cy = starty;
/*TODO*///				dest = ((DATA_TYPE *)bitmap->line[sy]) + sx;
/*TODO*///				if (priority)
/*TODO*///				{
/*TODO*///					UINT8 *pri = &priority_bitmap->line[sy][sx];
/*TODO*///
/*TODO*///					while (x <= ex)
/*TODO*///					{
/*TODO*///						int c = ((DATA_TYPE *)srcbitmap->line[(cy >> 16) & xmask])[(cx >> 16) & ymask];
/*TODO*///
/*TODO*///						if (c != transparent_color)
/*TODO*///						{
/*TODO*///							*dest = c;
/*TODO*///							*pri |= priority;
/*TODO*///						}
/*TODO*///
/*TODO*///						cx += incxx;
/*TODO*///						cy += incxy;
/*TODO*///						x++;
/*TODO*///						dest++;
/*TODO*///						pri++;
/*TODO*///					}
/*TODO*///				}
/*TODO*///				else
/*TODO*///				{
/*TODO*///					while (x <= ex)
/*TODO*///					{
/*TODO*///						int c = ((DATA_TYPE *)srcbitmap->line[(cy >> 16) & xmask])[(cx >> 16) & ymask];
/*TODO*///
/*TODO*///						if (c != transparent_color)
/*TODO*///							*dest = c;
/*TODO*///
/*TODO*///						cx += incxx;
/*TODO*///						cy += incxy;
/*TODO*///						x++;
/*TODO*///						dest++;
/*TODO*///					}
/*TODO*///				}
/*TODO*///				startx += incyx;
/*TODO*///				starty += incyy;
/*TODO*///				sy++;
/*TODO*///			}
/*TODO*///		}
/*TODO*///		else
/*TODO*///		{
/*TODO*///			while (sy <= ey)
/*TODO*///			{
/*TODO*///				x = sx;
/*TODO*///				cx = startx;
/*TODO*///				cy = starty;
/*TODO*///				dest = ((DATA_TYPE *)bitmap->line[sy]) + sx;
/*TODO*///				if (priority)
/*TODO*///				{
/*TODO*///					UINT8 *pri = &priority_bitmap->line[sy][sx];
/*TODO*///
/*TODO*///					while (x <= ex)
/*TODO*///					{
/*TODO*///						if (cx < widthshifted && cy < heightshifted)
/*TODO*///						{
/*TODO*///							int c = ((DATA_TYPE *)srcbitmap->line[cy >> 16])[cx >> 16];
/*TODO*///
/*TODO*///							if (c != transparent_color)
/*TODO*///							{
/*TODO*///								*dest = c;
/*TODO*///								*pri |= priority;
/*TODO*///							}
/*TODO*///						}
/*TODO*///
/*TODO*///						cx += incxx;
/*TODO*///						cy += incxy;
/*TODO*///						x++;
/*TODO*///						dest++;
/*TODO*///						pri++;
/*TODO*///					}
/*TODO*///				}
/*TODO*///				else
/*TODO*///				{
/*TODO*///					while (x <= ex)
/*TODO*///					{
/*TODO*///						if (cx < widthshifted && cy < heightshifted)
/*TODO*///						{
/*TODO*///							int c = ((DATA_TYPE *)srcbitmap->line[cy >> 16])[cx >> 16];
/*TODO*///
/*TODO*///							if (c != transparent_color)
/*TODO*///								*dest = c;
/*TODO*///						}
/*TODO*///
/*TODO*///						cx += incxx;
/*TODO*///						cy += incxy;
/*TODO*///						x++;
/*TODO*///						dest++;
/*TODO*///					}
/*TODO*///				}
/*TODO*///				startx += incyx;
/*TODO*///				starty += incyy;
/*TODO*///				sy++;
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///})
/*TODO*///
/*TODO*///#endif /* DECLARE */
/*TODO*///    
}
