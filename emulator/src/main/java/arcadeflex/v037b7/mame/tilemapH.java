/*
 * ported to 0.37b7
 */
package arcadeflex.v037b7.mame;

public class tilemapH {
/*TODO*////* tilemap.h */
/*TODO*///
/*TODO*///#ifndef TILEMAP_H
/*TODO*///#define TILEMAP_H
/*TODO*///
/*TODO*///#define ALL_TILEMAPS	0
/*TODO*////* ALL_TILEMAPS may be used with:
/*TODO*///	tilemap_update, tilemap_render, tilemap_set_flip, tilemap_mark_all_pixels_dirty
/*TODO*///*/
/*TODO*///
/*TODO*///#define TILEMAP_OPAQUE				0x00
/*TODO*///#define TILEMAP_TRANSPARENT			0x01
/*TODO*///#define TILEMAP_SPLIT				0x02
/*TODO*///#define TILEMAP_BITMASK				0x04
/*TODO*///#define TILEMAP_TRANSPARENT_COLOR	0x08
/*TODO*////*
/*TODO*///	TILEMAP_SPLIT should be used if the pixels from a single tile
/*TODO*///	can appear in more than one plane.
/*TODO*///
/*TODO*///	TILEMAP_BITMASK is needed for Namco SystemI
/*TODO*///*/
/*TODO*///
/*TODO*///#define TILEMAP_IGNORE_TRANSPARENCY		0x10
/*TODO*///#define TILEMAP_BACK					0x20
/*TODO*///#define TILEMAP_FRONT					0x40
/*TODO*////*
/*TODO*///	when rendering a split layer, pass TILEMAP_FRONT or TILEMAP_BACK or'd with the
/*TODO*///	tile_priority value to specify the part to draw.
/*TODO*///*/
/*TODO*///
/*TODO*///#define TILEMAP_BITMASK_TRANSPARENT (0)
/*TODO*///#define TILEMAP_BITMASK_OPAQUE ((UINT8 *)~0)
/*TODO*///
/*TODO*///struct cached_tile_info {
/*TODO*///	const UINT8 *pen_data;
/*TODO*///	const UINT16 *pal_data;
/*TODO*///	UINT32 pen_usage;
/*TODO*///	UINT32 flags;
/*TODO*///};
/*TODO*///
/*TODO*///extern struct tile_info {
/*TODO*///	/*
/*TODO*///		you must set tile_info.pen_data, tile_info.pal_data and tile_info.pen_usage
/*TODO*///		in the callback.  You can use the SET_TILE_INFO() macro below to do this.
/*TODO*///		tile_info.flags and tile_info.priority will be automatically preset to 0,
/*TODO*///		games that don't need them don't need to explicitly set them to 0
/*TODO*///	*/
/*TODO*///	const UINT8 *pen_data;
/*TODO*///	const UINT16 *pal_data;
/*TODO*///	UINT32 pen_usage;
/*TODO*///	UINT32 flags;
/*TODO*///
/*TODO*///	UINT32 priority;
/*TODO*///	UINT8 *mask_data;
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
/*TODO*////* tile flags, set by get_tile_info callback */
/*TODO*///#define TILE_FLIPX					0x01
/*TODO*///#define TILE_FLIPY					0x02
/*TODO*///#define TILE_SPLIT(T)				((T)<<2)
/*TODO*////* TILE_SPLIT is for use with TILEMAP_SPLIT layers.  It selects transparency type. */
/*TODO*///#define TILE_IGNORE_TRANSPARENCY	0x10
/*TODO*////* TILE_IGNORE_TRANSPARENCY is used if you need an opaque tile in a transparent layer */
/*TODO*///
/*TODO*///#define TILE_FLIPYX(YX)				(YX)
/*TODO*///#define TILE_FLIPXY(XY)				((((XY)>>1)|((XY)<<1))&3)
/*TODO*////*
/*TODO*///	TILE_FLIPYX is a shortcut that can be used by approx 80% of games,
/*TODO*///	since yflip frequently occurs one bit higher than xflip within a
/*TODO*///	tile attributes byte.
/*TODO*///*/
/*TODO*///
/*TODO*///#define TILE_LINE_DISABLED 0x80000000
/*TODO*///
/*TODO*///#ifndef OSD_CPU_H
/*TODO*///#include "osd_cpu.h"
/*TODO*///#endif
/*TODO*///
/*TODO*///extern struct osd_bitmap *priority_bitmap;
/*TODO*///
/*TODO*///struct tilemap_mask {
/*TODO*///	struct osd_bitmap *bitmask;
/*TODO*///	int line_offset;
/*TODO*///	UINT8 *data;
/*TODO*///	UINT8 **data_row;
/*TODO*///};
/*TODO*///
/*TODO*///struct tilemap {
/*TODO*///	UINT32 (*get_memory_offset)( UINT32 col, UINT32 row, UINT32 num_cols, UINT32 num_rows );
/*TODO*///	int *memory_offset_to_cached_index;
/*TODO*///	UINT32 *cached_index_to_memory_offset;
/*TODO*///	int logical_flip_to_cached_flip[4];
/*TODO*///
/*TODO*///	/* callback to interpret video VRAM for the tilemap */
/*TODO*///	void (*tile_get_info)( int memory_offset );
/*TODO*///
/*TODO*///	UINT32 max_memory_offset;
/*TODO*///	int num_tiles;
/*TODO*///	int num_logical_rows, num_logical_cols;
/*TODO*///	int num_cached_rows, num_cached_cols;
/*TODO*///	int cached_tile_width, cached_tile_height, cached_width, cached_height;
/*TODO*///
/*TODO*///	struct cached_tile_info *cached_tile_info;
/*TODO*///
/*TODO*///	int dx, dx_if_flipped;
/*TODO*///	int dy, dy_if_flipped;
/*TODO*///	int scrollx_delta, scrolly_delta;
/*TODO*///
/*TODO*///	int enable;
/*TODO*///	int attributes;
/*TODO*///
/*TODO*///	int type;
/*TODO*///	int transparent_pen;
/*TODO*///	unsigned int transmask[4];
/*TODO*///
/*TODO*///	void (*draw)( int, int );
/*TODO*///	void (*draw_opaque)( int, int );
/*TODO*///
/*TODO*///	UINT8 *priority,	/* priority for each tile */
/*TODO*///		**priority_row;
/*TODO*///
/*TODO*///	UINT8 *visible; /* boolean flag for each tile */
/*TODO*///
/*TODO*///	UINT8 *dirty_vram; /* boolean flag for each tile */
/*TODO*///
/*TODO*///	UINT8 *dirty_pixels;
/*TODO*///
/*TODO*///	int scroll_rows, scroll_cols;
/*TODO*///	int *rowscroll, *colscroll;
/*TODO*///
/*TODO*///	int orientation;
/*TODO*///	int clip_left,clip_right,clip_top,clip_bottom;
/*TODO*///
/*TODO*///	/* cached color data */
/*TODO*///	struct osd_bitmap *pixmap;
/*TODO*///	int pixmap_line_offset;
/*TODO*///
/*TODO*///	struct tilemap_mask *foreground;
/*TODO*///	/* for transparent layers, or the front half of a split layer */
/*TODO*///
/*TODO*///	struct tilemap_mask *background;
/*TODO*///	/* for the back half of a split layer */
/*TODO*///
/*TODO*///	struct tilemap *next; /* resource tracking */
/*TODO*///};
/*TODO*///
/*TODO*////* don't call these from drivers - they are called from mame.c */
/*TODO*///int tilemap_init( void );
/*TODO*///void tilemap_close( void );
/*TODO*///
/*TODO*///struct tilemap *tilemap_create(
/*TODO*///	void (*tile_get_info)( int memory_offset ),
/*TODO*///	UINT32 (*get_memory_offset)( UINT32 col, UINT32 row, UINT32 num_cols, UINT32 num_rows ),
/*TODO*///	int type,
/*TODO*///	int tile_width, int tile_height, /* in pixels */
/*TODO*///	int num_cols, int num_rows /* in tiles */
/*TODO*///);
/*TODO*///
/*TODO*///void tilemap_dispose( struct tilemap *tilemap );
/*TODO*////*	you shouldn't call this in vh_close; all tilemaps will be automatically
/*TODO*///	disposed.  tilemap_dispose is supplied for games that need to change
/*TODO*///	tile size or cols/rows dynamically.
/*TODO*///*/
/*TODO*///
/*TODO*///void tilemap_set_scroll_cols( struct tilemap *tilemap, int scroll_cols );
/*TODO*///void tilemap_set_scroll_rows( struct tilemap *tilemap, int scroll_rows );
/*TODO*////* scroll_rows and scroll_cols default to 1 for XY scrolling */
/*TODO*///
/*TODO*///void tilemap_mark_tile_dirty( struct tilemap *tilemap, int memory_offset );
/*TODO*///void tilemap_mark_all_tiles_dirty( struct tilemap *tilemap );
/*TODO*///void tilemap_mark_all_pixels_dirty( struct tilemap *tilemap );
/*TODO*///
/*TODO*///void tilemap_set_scrollx( struct tilemap *tilemap, int row, int value );
/*TODO*///void tilemap_set_scrolly( struct tilemap *tilemap, int col, int value );
/*TODO*///
/*TODO*///void tilemap_set_scrolldx( struct tilemap *tilemap, int dx, int dx_if_flipped );
/*TODO*///void tilemap_set_scrolldy( struct tilemap *tilemap, int dy, int dy_if_flipped );
/*TODO*///
/*TODO*///#define TILEMAP_FLIPX 0x1
/*TODO*///#define TILEMAP_FLIPY 0x2
/*TODO*///void tilemap_set_flip( struct tilemap *tilemap, int attributes );
/*TODO*///void tilemap_set_clip( struct tilemap *tilemap, const struct rectangle *clip );
/*TODO*///void tilemap_set_enable( struct tilemap *tilemap, int enable );
/*TODO*///
/*TODO*///void tilemap_update( struct tilemap *tilemap );
/*TODO*///void tilemap_render( struct tilemap *tilemap );
/*TODO*///void tilemap_draw( struct osd_bitmap *dest, struct tilemap *tilemap, UINT32 priority );
/*TODO*///
/*TODO*////*********************************************************************/
/*TODO*///
/*TODO*///UINT32 tilemap_scan_cols( UINT32 col, UINT32 row, UINT32 num_cols, UINT32 num_rows );
/*TODO*///UINT32 tilemap_scan_rows( UINT32 col, UINT32 row, UINT32 num_cols, UINT32 num_rows );
/*TODO*///
/*TODO*///#endif
/*TODO*///    
}
