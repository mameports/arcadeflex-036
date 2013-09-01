package mame;

import static mame.driverH.*;
import static mame.osdependH.*;
import static arcadeflex.libc.*;
import static arcadeflex.libc_old.*;

public class tilemapH {
    public static final tilemap ALL_TILEMAPS	=null;
    /* ALL_TILEMAPS may be used with:
    	tilemap_update, tilemap_render, tilemap_set_flip, tilemap_mark_all_pixels_dirty
    */
    
    /*TODO*///#define TILEMAP_OPAQUE			0x00
    /*TODO*///#define TILEMAP_TRANSPARENT		0x01
    public static final int TILEMAP_SPLIT=		0x02;
    /*TODO*///#define TILEMAP_BITMASK			0x04
    /*
    	TILEMAP_SPLIT should be used if the pixels from a single tile
    	can appear in more than one plane.
    
    	TILEMAP_BITMASK is needed for Namco SystemI
    */
    
    public static final int TILEMAP_IGNORE_TRANSPARENCY		=0x10;
    public static final int TILEMAP_BACK			=0x20;
    public static final int TILEMAP_FRONT			=0x40;
    /*
    	when rendering a split layer, pass TILEMAP_FRONT or TILEMAP_BACK or'd with the
    	tile_priority value to specify the part to draw.
    */
    
    /*TODO*///#define TILEMAP_BITMASK_TRANSPARENT (0)
    /*TODO*///#define TILEMAP_BITMAK_OPAQUE       ((unsigned char *)-1)
    /*TODO*///
    /*TODO*///extern struct tile_info {
    /*TODO*///	unsigned char *pen_data; /* pointer to gfx data */
    /*TODO*///	unsigned short *pal_data; /* pointer to palette */
    /*TODO*///	unsigned char *mask_data; /* pointer to mask data (for TILEMAP_BITMASK) */
    /*TODO*///	unsigned int pen_usage;	/* used pens mask */
    /*TODO*///	/*
    /*TODO*///		you must set tile_info.pen_data, tile_info.pal_data and tile_info.pen_usage
    /*TODO*///		in the callback.  You can use the SET_TILE_INFO() macro below to do this.
    /*TODO*///		tile_info.flags and tile_info.priority will be automatically preset to 0,
    /*TODO*///		games that don't need them don't need to explicitly set them to 0
    /*TODO*///	*/
    /*TODO*///	unsigned char flags; /* see below */
    /*TODO*///	unsigned char priority;
    /*TODO*///} tile_info;
    /*TODO*///
    /*TODO*///#define SET_TILE_INFO(GFX,CODE,COLOR) { \
    /*TODO*///	const struct GfxElement *gfx = Machine->gfx[(GFX)]; \
    /*TODO*///	int _code = (CODE) % gfx->total_elements; \
    /*TODO*///	tile_info.pen_data = gfx->gfxdata + _code*gfx->char_modulo; \
    /*TODO*///	tile_info.pal_data = &gfx->colortable[gfx->color_granularity * (COLOR)]; \
    /*TODO*///	tile_info.pen_usage = gfx->pen_usage?gfx->pen_usage[_code]:0; \
    /*TODO*///}
    /*TODO*///
    /* tile flags, set by get_tile_info callback */
    public static final int TILE_FLIPX			=0x01;
    public static final int TILE_FLIPY			=0x02;
    /*TODO*///#define TILE_SPLIT(T)				((T)<<2)
    /*TODO*////* TILE_SPLIT is for use with TILEMAP_SPLIT layers.  It selects transparency type. */
    /*TODO*///#define TILE_IGNORE_TRANSPARENCY	0x10
    /*TODO*////* TILE_IGNORE_TRANSPARENCY is used if you need an opaque tile in a transparent layer */
    /*TODO*///
    /*TODO*///#define TILE_FLIPYX(YX)				(YX)
    /*TODO*///#define TILE_FLIPXY(XY)			((((XY)>>1)|((XY)<<1))&3)
    /*TODO*////*
    /*TODO*///	TILE_FLIPYX is a shortcut that can be used by approx 80% of games,
    /*TODO*///	since yflip frequently occurs one bit higher than xflip within a
    /*TODO*///	tile attributes byte.
    /*TODO*///*/
    /*TODO*///
    public static final int TILE_LINE_DISABLED =0x80000000;
    
    public static class tilemap 
    {
        public tilemap()
        {
            
        }
    	public int dx, dx_if_flipped;
    	public int dy, dy_if_flipped;
    	public int scrollx_delta, scrolly_delta;
    
    	public int type;
    	public int enable;
    	public int attributes;
    	public int transparent_pen;
    	public /*unsigned*/ int[] transmask=new int[4];//shoud be unsigned ;/
    
    	public int num_rows, num_cols, num_tiles;
    	public int tile_width, tile_height, width, height;
    
    	public WriteHandlerPtr mark_visible;//void (*mark_visible)( int, int );
    	public WriteHandlerPtr draw;//void (*draw)( int, int );
    	public WriteHandlerPtr draw_opaque;//void (*draw_opaque)( int, int );
    
    	public UBytePtr[] pendata;//unsigned char **pendata;
    	public UBytePtr[] maskdata;//unsigned char **maskdata;
        public CharPtr[] paldata; //unsigned short **paldata;
    	public int[] pen_usage;//unsigned int *pen_usage;
    
        public char[] priority;//char *priority,	/* priority for each tile */
        public UBytePtr[] priority_row;
   
        public char[] visible;//char *visible, /* boolean flag for each tile */
    	public UBytePtr[] visible_row;
    /*TODO*///
    	public char[] dirty_vram;//char *dirty_vram, /* boolean flag for each tile */
    /*TODO*///		**dirty_vram_row; /* TBA */
    /*TODO*///
    /*TODO*///	int *span,	/* contains transparency type, and run length, for adjacent tiles of same transparency_type and priority */
    /*TODO*///		**span_row;
    /*TODO*///
    	public char[] dirty_pixels;//char *dirty_pixels;
    	public char[] flags;//unsigned char *flags;
   
    	/* callback to interpret video VRAM for the tilemap */
    	public  WriteHandlerPtr tile_get_info;//void (*tile_get_info)( int col, int row );
    
    	public int scrolled;
    	public int scroll_rows, scroll_cols;
    	int rowscroll[], colscroll[];

    	public int orientation;
    	public int clip_left,clip_right,clip_top,clip_bottom;
    
    	/* cached color data */
    	public osd_bitmap pixmap;
    	public int pixmap_line_offset;
    
    	/* foreground mask - for transparent layers, or the front half of a split layer */
    	public osd_bitmap fg_mask;
        public char[] fg_mask_data; //unsigned char *fg_mask_data;
        public UBytePtr[] fg_mask_data_row;//unsigned char **fg_mask_data_row;
    	public int fg_mask_line_offset;
    /*TODO*///	unsigned short *fg_span, **fg_span_row;
    /*TODO*///
    	/* background mask - for the back half of a split layer */
    	public osd_bitmap bg_mask;
    	public char[] bg_mask_data;//unsigned char *bg_mask_data;
    	public UBytePtr[] bg_mask_data_row;//unsigned char **bg_mask_data_row;
    	public int bg_mask_line_offset;
    /*TODO*///	unsigned short *bg_span, **bg_span_row;
    /*TODO*///
        public tilemap next; /* resource tracking */
    };

    
    public static final int TILEMAP_FLIPX =0x1;
    public static final int TILEMAP_FLIPY =0x2;
   
}
