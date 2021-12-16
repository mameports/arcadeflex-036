/*
 * ported to 0.37b7
 */
package arcadeflex.v037b7.mame;

import gr.codebb.arcadeflex.v036.mame.osdependH.osd_bitmap;

public class tilemapC {

    /*TODO*////* tilemap.c
/*TODO*///
/*TODO*///In Progress
/*TODO*///-	visibility walk (temporarily broken)
/*TODO*///-	nowrap
/*TODO*///
/*TODO*///To Do:
/*TODO*///-	virtualization for huge tilemaps
/*TODO*///-	precompute spans per row (to speed up the low level code)
/*TODO*///-	support for unusual tile sizes (8x12, 8x10)
/*TODO*///-	screenwise scrolling
/*TODO*///-	internal profiling
/*TODO*///
/*TODO*///	Usage Notes:
/*TODO*///
/*TODO*///	When the videoram for a tile changes, call tilemap_mark_tile_dirty
/*TODO*///	with the appropriate tile_index.
/*TODO*///
/*TODO*///	In the video driver, follow these steps:
/*TODO*///
/*TODO*///	1)	set each tilemap's scroll registers
/*TODO*///
/*TODO*///	2)	call tilemap_update for each tilemap.
/*TODO*///
/*TODO*///	3)	call palette_init_used_colors.
/*TODO*///		mark the colors used by sprites.
/*TODO*///		call palette recalc.  If the palette manager has compressed the palette,
/*TODO*///			call tilemap_mark_all_pixels_dirty for each tilemap.
/*TODO*///
/*TODO*///	4)	call tilemap_render for each tilemap.
/*TODO*///
/*TODO*///	5)	call tilemap_draw to draw the tilemaps to the screen, from back to front
/*TODO*///*/
/*TODO*///
/*TODO*///#ifndef DECLARE
/*TODO*///
/*TODO*///#include "driver.h"
/*TODO*///#include "tilemap.h"
/*TODO*///
/*TODO*///#define SWAP(X,Y) {UINT32 temp=X; X=Y; Y=temp; }
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*////* some common mappings */
/*TODO*///
/*TODO*///UINT32 tilemap_scan_rows( UINT32 col, UINT32 row, UINT32 num_cols, UINT32 num_rows ){
/*TODO*///	/* logical (col,row) -> memory offset */
/*TODO*///	return row*num_cols + col;
/*TODO*///}
/*TODO*///UINT32 tilemap_scan_cols( UINT32 col, UINT32 row, UINT32 num_cols, UINT32 num_rows ){
/*TODO*///	/* logical (col,row) -> memory offset */
/*TODO*///	return col*num_rows + row;
/*TODO*///}
/*TODO*///
/*TODO*////*********************************************************************************/
/*TODO*///
/*TODO*///static struct osd_bitmap *create_tmpbitmap( int width, int height, int depth ){
/*TODO*///	return osd_alloc_bitmap( width,height,depth );
/*TODO*///}
/*TODO*///
/*TODO*///static struct osd_bitmap *create_bitmask( int width, int height ){
/*TODO*///	width = (width+7)/8; /* 8 bits per byte */
/*TODO*///	return osd_alloc_bitmap( width,height, 8 );
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///static int mappings_create( struct tilemap *tilemap ){
/*TODO*///	int max_memory_offset = 0;
/*TODO*///	UINT32 col,row;
/*TODO*///	UINT32 num_logical_rows = tilemap->num_logical_rows;
/*TODO*///	UINT32 num_logical_cols = tilemap->num_logical_cols;
/*TODO*///	/* count offsets (might be larger than num_tiles) */
/*TODO*///	for( row=0; row<num_logical_rows; row++ ){
/*TODO*///		for( col=0; col<num_logical_cols; col++ ){
/*TODO*///			UINT32 memory_offset = tilemap->get_memory_offset( col, row, num_logical_cols, num_logical_rows );
/*TODO*///			if( memory_offset>max_memory_offset ) max_memory_offset = memory_offset;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	max_memory_offset++;
/*TODO*///	tilemap->max_memory_offset = max_memory_offset;
/*TODO*///	/* logical to cached (tilemap_mark_dirty) */
/*TODO*///	tilemap->memory_offset_to_cached_index = malloc( sizeof(int)*max_memory_offset );
/*TODO*///	if( tilemap->memory_offset_to_cached_index ){
/*TODO*///		/* cached to logical (get_tile_info) */
/*TODO*///		tilemap->cached_index_to_memory_offset = malloc( sizeof(UINT32)*tilemap->num_tiles );
/*TODO*///		if( tilemap->cached_index_to_memory_offset ) return 0; /* no error */
/*TODO*///		free( tilemap->memory_offset_to_cached_index );
/*TODO*///	}
/*TODO*///	return -1; /* error */
/*TODO*///}
/*TODO*///
/*TODO*///static void mappings_dispose( struct tilemap *tilemap ){
/*TODO*///	free( tilemap->cached_index_to_memory_offset );
/*TODO*///	free( tilemap->memory_offset_to_cached_index );
/*TODO*///}
/*TODO*///
/*TODO*///static void mappings_update( struct tilemap *tilemap ){
/*TODO*///	int logical_flip;
/*TODO*///	UINT32 logical_index, cached_index;
/*TODO*///	UINT32 num_cached_rows = tilemap->num_cached_rows;
/*TODO*///	UINT32 num_cached_cols = tilemap->num_cached_cols;
/*TODO*///	UINT32 num_logical_rows = tilemap->num_logical_rows;
/*TODO*///	UINT32 num_logical_cols = tilemap->num_logical_cols;
/*TODO*///	for( logical_index=0; logical_index<tilemap->max_memory_offset; logical_index++ ){
/*TODO*///		tilemap->memory_offset_to_cached_index[logical_index] = -1;
/*TODO*///	}
/*TODO*///
/*TODO*///	logerror("log size(%dx%d); cach size(%dx%d)\n",
/*TODO*///			num_logical_cols,num_logical_rows,
/*TODO*///			num_cached_cols,num_cached_rows);
/*TODO*///
/*TODO*///	for( logical_index=0; logical_index<tilemap->num_tiles; logical_index++ ){
/*TODO*///		UINT32 logical_col = logical_index%num_logical_cols;
/*TODO*///		UINT32 logical_row = logical_index/num_logical_cols;
/*TODO*///		int memory_offset = tilemap->get_memory_offset( logical_col, logical_row, num_logical_cols, num_logical_rows );
/*TODO*///		UINT32 cached_col = logical_col;
/*TODO*///		UINT32 cached_row = logical_row;
/*TODO*///		if( tilemap->orientation & ORIENTATION_SWAP_XY ) SWAP(cached_col,cached_row)
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_X ) cached_col = (num_cached_cols-1)-cached_col;
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_Y ) cached_row = (num_cached_rows-1)-cached_row;
/*TODO*///		cached_index = cached_row*num_cached_cols+cached_col;
/*TODO*///		tilemap->memory_offset_to_cached_index[memory_offset] = cached_index;
/*TODO*///		tilemap->cached_index_to_memory_offset[cached_index] = memory_offset;
/*TODO*///	}
/*TODO*///	for( logical_flip = 0; logical_flip<4; logical_flip++ ){
/*TODO*///		int cached_flip = logical_flip;
/*TODO*///		if( tilemap->attributes&TILEMAP_FLIPX ) cached_flip ^= TILE_FLIPX;
/*TODO*///		if( tilemap->attributes&TILEMAP_FLIPY ) cached_flip ^= TILE_FLIPY;
/*TODO*///#ifndef PREROTATE_GFX
/*TODO*///		if( Machine->orientation & ORIENTATION_SWAP_XY ){
/*TODO*///			if( Machine->orientation & ORIENTATION_FLIP_X ) cached_flip ^= TILE_FLIPY;
/*TODO*///			if( Machine->orientation & ORIENTATION_FLIP_Y ) cached_flip ^= TILE_FLIPX;
/*TODO*///		}
/*TODO*///		else {
/*TODO*///			if( Machine->orientation & ORIENTATION_FLIP_X ) cached_flip ^= TILE_FLIPX;
/*TODO*///			if( Machine->orientation & ORIENTATION_FLIP_Y ) cached_flip ^= TILE_FLIPY;
/*TODO*///		}
/*TODO*///#endif
/*TODO*///		if( tilemap->orientation & ORIENTATION_SWAP_XY ){
/*TODO*///			cached_flip = ((cached_flip&1)<<1) | ((cached_flip&2)>>1);
/*TODO*///		}
/*TODO*///		tilemap->logical_flip_to_cached_flip[logical_flip] = cached_flip;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
    public static osd_bitmap priority_bitmap;/* priority buffer (corresponds to screen bitmap) */
 /*TODO*///int priority_bitmap_line_offset;
/*TODO*///
/*TODO*///static UINT8 flip_bit_table[0x100]; /* horizontal flip for 8 pixels */
/*TODO*///static struct tilemap *first_tilemap; /* resource tracking */
/*TODO*///static int screen_width, screen_height;
/*TODO*///struct tile_info tile_info;
/*TODO*///
/*TODO*///enum {
/*TODO*///	TILE_TRANSPARENT,
/*TODO*///	TILE_MASKED,
/*TODO*///	TILE_OPAQUE
/*TODO*///};
/*TODO*///
/*TODO*////* the following parameters are constant across tilemap_draw calls */
/*TODO*///static struct {
/*TODO*///	int clip_left, clip_top, clip_right, clip_bottom;
/*TODO*///	int source_width, source_height;
/*TODO*///	int dest_line_offset,source_line_offset,mask_line_offset;
/*TODO*///	int dest_row_offset,source_row_offset,mask_row_offset;
/*TODO*///	struct osd_bitmap *screen, *pixmap, *bitmask;
/*TODO*///	UINT8 **mask_data_row;
/*TODO*///	UINT8 **priority_data_row;
/*TODO*///	int tile_priority;
/*TODO*///	int tilemap_priority_code;
/*TODO*///} blit;
/*TODO*///
/*TODO*///#define MASKROWBYTES(W) (((W)+7)/8)
/*TODO*///
/*TODO*///static void memsetbitmask8( UINT8 *dest, int value, const UINT8 *bitmask, int count ){
/*TODO*////* TBA: combine with memcpybitmask */
/*TODO*///	for(;;){
/*TODO*///		UINT32 data = *bitmask++;
/*TODO*///		if( data&0x80 ) dest[0] |= value;
/*TODO*///		if( data&0x40 ) dest[1] |= value;
/*TODO*///		if( data&0x20 ) dest[2] |= value;
/*TODO*///		if( data&0x10 ) dest[3] |= value;
/*TODO*///		if( data&0x08 ) dest[4] |= value;
/*TODO*///		if( data&0x04 ) dest[5] |= value;
/*TODO*///		if( data&0x02 ) dest[6] |= value;
/*TODO*///		if( data&0x01 ) dest[7] |= value;
/*TODO*///		if( --count == 0 ) break;
/*TODO*///		dest+=8;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///static void memcpybitmask8( UINT8 *dest, const UINT8 *source, const UINT8 *bitmask, int count ){
/*TODO*///	for(;;){
/*TODO*///		UINT32 data = *bitmask++;
/*TODO*///		if( data&0x80 ) dest[0] = source[0];
/*TODO*///		if( data&0x40 ) dest[1] = source[1];
/*TODO*///		if( data&0x20 ) dest[2] = source[2];
/*TODO*///		if( data&0x10 ) dest[3] = source[3];
/*TODO*///		if( data&0x08 ) dest[4] = source[4];
/*TODO*///		if( data&0x04 ) dest[5] = source[5];
/*TODO*///		if( data&0x02 ) dest[6] = source[6];
/*TODO*///		if( data&0x01 ) dest[7] = source[7];
/*TODO*///		if( --count == 0 ) break;
/*TODO*///		source+=8;
/*TODO*///		dest+=8;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///static void memcpybitmask16( UINT16 *dest, const UINT16 *source, const UINT8 *bitmask, int count ){
/*TODO*///	for(;;){
/*TODO*///		UINT32 data = *bitmask++;
/*TODO*///		if( data&0x80 ) dest[0] = source[0];
/*TODO*///		if( data&0x40 ) dest[1] = source[1];
/*TODO*///		if( data&0x20 ) dest[2] = source[2];
/*TODO*///		if( data&0x10 ) dest[3] = source[3];
/*TODO*///		if( data&0x08 ) dest[4] = source[4];
/*TODO*///		if( data&0x04 ) dest[5] = source[5];
/*TODO*///		if( data&0x02 ) dest[6] = source[6];
/*TODO*///		if( data&0x01 ) dest[7] = source[7];
/*TODO*///		if( --count == 0 ) break;
/*TODO*///		source+=8;
/*TODO*///		dest+=8;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///#define TILE_WIDTH	8
/*TODO*///#define TILE_HEIGHT	8
/*TODO*///#define DATA_TYPE UINT8
/*TODO*///#define memcpybitmask memcpybitmask8
/*TODO*///#define DECLARE(function,args,body) static void function##8x8x8BPP args body
/*TODO*///#include "tilemap.c"
/*TODO*///
/*TODO*///#define TILE_WIDTH	16
/*TODO*///#define TILE_HEIGHT	16
/*TODO*///#define DATA_TYPE UINT8
/*TODO*///#define memcpybitmask memcpybitmask8
/*TODO*///#define DECLARE(function,args,body) static void function##16x16x8BPP args body
/*TODO*///#include "tilemap.c"
/*TODO*///
/*TODO*///#define TILE_WIDTH	32
/*TODO*///#define TILE_HEIGHT	32
/*TODO*///#define DATA_TYPE UINT8
/*TODO*///#define memcpybitmask memcpybitmask8
/*TODO*///#define DECLARE(function,args,body) static void function##32x32x8BPP args body
/*TODO*///#include "tilemap.c"
/*TODO*///
/*TODO*///#define TILE_WIDTH	8
/*TODO*///#define TILE_HEIGHT	8
/*TODO*///#define DATA_TYPE UINT16
/*TODO*///#define memcpybitmask memcpybitmask16
/*TODO*///#define DECLARE(function,args,body) static void function##8x8x16BPP args body
/*TODO*///#include "tilemap.c"
/*TODO*///
/*TODO*///#define TILE_WIDTH	16
/*TODO*///#define TILE_HEIGHT	16
/*TODO*///#define DATA_TYPE UINT16
/*TODO*///#define memcpybitmask memcpybitmask16
/*TODO*///#define DECLARE(function,args,body) static void function##16x16x16BPP args body
/*TODO*///#include "tilemap.c"
/*TODO*///
/*TODO*///#define TILE_WIDTH	32
/*TODO*///#define TILE_HEIGHT	32
/*TODO*///#define DATA_TYPE UINT16
/*TODO*///#define memcpybitmask memcpybitmask16
/*TODO*///#define DECLARE(function,args,body) static void function##32x32x16BPP args body
/*TODO*///#include "tilemap.c"
/*TODO*///
/*TODO*////*********************************************************************************/
/*TODO*///
/*TODO*///static void mask_dispose( struct tilemap_mask *mask ){
/*TODO*///	if( mask ){
/*TODO*///		free( mask->data_row );
/*TODO*///		free( mask->data );
/*TODO*///		osd_free_bitmap( mask->bitmask );
/*TODO*///		free( mask );
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///static struct tilemap_mask *mask_create( struct tilemap *tilemap ){
/*TODO*///	struct tilemap_mask *mask = malloc( sizeof(struct tilemap_mask) );
/*TODO*///	if( mask ){
/*TODO*///		mask->data = malloc( tilemap->num_tiles );
/*TODO*///		mask->data_row = malloc( tilemap->num_cached_rows * sizeof(UINT8 *) );
/*TODO*///		mask->bitmask = create_bitmask( tilemap->cached_width, tilemap->cached_height );
/*TODO*///		if( mask->data && mask->data_row && mask->bitmask ){
/*TODO*///			int row;
/*TODO*///			for( row=0; row<tilemap->num_cached_rows; row++ ){
/*TODO*///				mask->data_row[row] = mask->data + row*tilemap->num_cached_cols;
/*TODO*///			}
/*TODO*///			mask->line_offset = mask->bitmask->line[1] - mask->bitmask->line[0];
/*TODO*///			return mask;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	mask_dispose( mask );
/*TODO*///	return NULL;
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///static void install_draw_handlers( struct tilemap *tilemap ){
/*TODO*///	int tile_width = tilemap->cached_tile_width;
/*TODO*///	int tile_height = tilemap->cached_tile_height;
/*TODO*///	tilemap->draw = tilemap->draw_opaque = NULL;
/*TODO*///	if( Machine->scrbitmap->depth==16 ){
/*TODO*///		if( tile_width==8 && tile_height==8 ){
/*TODO*///			tilemap->draw = draw8x8x16BPP;
/*TODO*///			tilemap->draw_opaque = draw_opaque8x8x16BPP;
/*TODO*///		}
/*TODO*///		else if( tile_width==16 && tile_height==16 ){
/*TODO*///			tilemap->draw = draw16x16x16BPP;
/*TODO*///			tilemap->draw_opaque = draw_opaque16x16x16BPP;
/*TODO*///		}
/*TODO*///		else if( tile_width==32 && tile_height==32 ){
/*TODO*///			tilemap->draw = draw32x32x16BPP;
/*TODO*///			tilemap->draw_opaque = draw_opaque32x32x16BPP;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		if( tile_width==8 && tile_height==8 ){
/*TODO*///			tilemap->draw = draw8x8x8BPP;
/*TODO*///			tilemap->draw_opaque = draw_opaque8x8x8BPP;
/*TODO*///		}
/*TODO*///		else if( tile_width==16 && tile_height==16 ){
/*TODO*///			tilemap->draw = draw16x16x8BPP;
/*TODO*///			tilemap->draw_opaque = draw_opaque16x16x8BPP;
/*TODO*///		}
/*TODO*///		else if( tile_width==32 && tile_height==32 ){
/*TODO*///			tilemap->draw = draw32x32x8BPP;
/*TODO*///			tilemap->draw_opaque = draw_opaque32x32x8BPP;
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///int tilemap_init( void ){
/*TODO*///	UINT32 value, data, bit;
/*TODO*///	for( value=0; value<0x100; value++ ){
/*TODO*///		data = 0;
/*TODO*///		for( bit=0; bit<8; bit++ ) if( (value>>bit)&1 ) data |= 0x80>>bit;
/*TODO*///		flip_bit_table[value] = data;
/*TODO*///	}
/*TODO*///	screen_width = Machine->scrbitmap->width;
/*TODO*///	screen_height = Machine->scrbitmap->height;
/*TODO*///	first_tilemap = 0;
/*TODO*///	priority_bitmap = create_tmpbitmap( screen_width, screen_height, 8 );
/*TODO*///	if( priority_bitmap ){
/*TODO*///		priority_bitmap_line_offset = priority_bitmap->line[1] - priority_bitmap->line[0];
/*TODO*///		return 0;
/*TODO*///	}
/*TODO*///	return -1;
/*TODO*///}
/*TODO*///
/*TODO*///void tilemap_close( void ){
/*TODO*///	while( first_tilemap ){
/*TODO*///		struct tilemap *next = first_tilemap->next;
/*TODO*///		tilemap_dispose( first_tilemap );
/*TODO*///		first_tilemap = next;
/*TODO*///	}
/*TODO*///	osd_free_bitmap( priority_bitmap );
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///struct tilemap *tilemap_create(
/*TODO*///	void (*tile_get_info)( int memory_offset ),
/*TODO*///	UINT32 (*get_memory_offset)( UINT32 col, UINT32 row, UINT32 num_cols, UINT32 num_rows ),
/*TODO*///	int type,
/*TODO*///	int tile_width, int tile_height, /* in pixels */
/*TODO*///	int num_cols, int num_rows /* in tiles */
/*TODO*///){
/*TODO*///	struct tilemap *tilemap = calloc( 1,sizeof( struct tilemap ) );
/*TODO*///	if( tilemap ){
/*TODO*///		int num_tiles = num_cols*num_rows;
/*TODO*///		tilemap->num_logical_cols = num_cols;
/*TODO*///		tilemap->num_logical_rows = num_rows;
/*TODO*///		if( Machine->orientation & ORIENTATION_SWAP_XY ){
/*TODO*///		logerror("swap!!\n" );
/*TODO*///			SWAP( tile_width, tile_height )
/*TODO*///			SWAP( num_cols,num_rows )
/*TODO*///		}
/*TODO*///		tilemap->num_cached_cols = num_cols;
/*TODO*///		tilemap->num_cached_rows = num_rows;
/*TODO*///		tilemap->num_tiles = num_tiles;
/*TODO*///		tilemap->cached_tile_width = tile_width;
/*TODO*///		tilemap->cached_tile_height = tile_height;
/*TODO*///		tilemap->cached_width = tile_width*num_cols;
/*TODO*///		tilemap->cached_height = tile_height*num_rows;
/*TODO*///		tilemap->tile_get_info = tile_get_info;
/*TODO*///		tilemap->get_memory_offset = get_memory_offset;
/*TODO*///		tilemap->orientation = Machine->orientation;
/*TODO*///		tilemap->enable = 1;
/*TODO*///		tilemap->type = type;
/*TODO*///		tilemap->scroll_rows = 1;
/*TODO*///		tilemap->scroll_cols = 1;
/*TODO*///		tilemap->transparent_pen = -1;
/*TODO*///		tilemap->cached_tile_info = calloc( num_tiles, sizeof(struct cached_tile_info) );
/*TODO*///		tilemap->priority = calloc( num_tiles,1 );
/*TODO*///		tilemap->visible = calloc( num_tiles,1 );
/*TODO*///		tilemap->dirty_vram = malloc( num_tiles );
/*TODO*///		tilemap->dirty_pixels = malloc( num_tiles );
/*TODO*///		tilemap->rowscroll = calloc(tilemap->cached_height,sizeof(int));
/*TODO*///		tilemap->colscroll = calloc(tilemap->cached_width,sizeof(int));
/*TODO*///		tilemap->priority_row = malloc( sizeof(UINT8 *)*num_rows );
/*TODO*///		tilemap->pixmap = create_tmpbitmap( tilemap->cached_width, tilemap->cached_height, Machine->scrbitmap->depth );
/*TODO*///		tilemap->foreground = mask_create( tilemap );
/*TODO*///		tilemap->background = (type & TILEMAP_SPLIT)?mask_create( tilemap ):NULL;
/*TODO*///		if( tilemap->cached_tile_info &&
/*TODO*///			tilemap->priority && tilemap->visible &&
/*TODO*///			tilemap->dirty_vram && tilemap->dirty_pixels &&
/*TODO*///			tilemap->rowscroll && tilemap->colscroll &&
/*TODO*///			tilemap->priority_row &&
/*TODO*///			tilemap->pixmap && tilemap->foreground &&
/*TODO*///			((type&TILEMAP_SPLIT)==0 || tilemap->background) &&
/*TODO*///			(mappings_create( tilemap )==0)
/*TODO*///		){
/*TODO*///			UINT32 row;
/*TODO*///			for( row=0; row<num_rows; row++ ){
/*TODO*///				tilemap->priority_row[row] = tilemap->priority+num_cols*row;
/*TODO*///			}
/*TODO*///			install_draw_handlers( tilemap );
/*TODO*///			mappings_update( tilemap );
/*TODO*///			tilemap_set_clip( tilemap, &Machine->visible_area );
/*TODO*///			memset( tilemap->dirty_vram, 1, num_tiles );
/*TODO*///			memset( tilemap->dirty_pixels, 1, num_tiles );
/*TODO*///			tilemap->pixmap_line_offset = tilemap->pixmap->line[1] - tilemap->pixmap->line[0];
/*TODO*///			tilemap->next = first_tilemap;
/*TODO*///			first_tilemap = tilemap;
/*TODO*///			return tilemap;
/*TODO*///		}
/*TODO*///		tilemap_dispose( tilemap );
/*TODO*///	}
/*TODO*///	return 0;
/*TODO*///}
/*TODO*///
/*TODO*///void tilemap_dispose( struct tilemap *tilemap ){
/*TODO*///	if( tilemap==first_tilemap ){
/*TODO*///		first_tilemap = tilemap->next;
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		struct tilemap *prev = first_tilemap;
/*TODO*///		while( prev->next != tilemap ) prev = prev->next;
/*TODO*///		prev->next =tilemap->next;
/*TODO*///	}
/*TODO*///
/*TODO*///	free( tilemap->cached_tile_info );
/*TODO*///	free( tilemap->priority );
/*TODO*///	free( tilemap->visible );
/*TODO*///	free( tilemap->dirty_vram );
/*TODO*///	free( tilemap->dirty_pixels );
/*TODO*///	free( tilemap->rowscroll );
/*TODO*///	free( tilemap->colscroll );
/*TODO*///	free( tilemap->priority_row );
/*TODO*///	osd_free_bitmap( tilemap->pixmap );
/*TODO*///	mask_dispose( tilemap->foreground );
/*TODO*///	mask_dispose( tilemap->background );
/*TODO*///	mappings_dispose( tilemap );
/*TODO*///	free( tilemap );
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///static void unregister_pens( struct cached_tile_info *cached_tile_info, int num_pens ){
/*TODO*///	const UINT16 *pal_data = cached_tile_info->pal_data;
/*TODO*///	if( pal_data ){
/*TODO*///		UINT32 pen_usage = cached_tile_info->pen_usage;
/*TODO*///		if( pen_usage ){
/*TODO*///			palette_decrease_usage_count(
/*TODO*///				pal_data-Machine->remapped_colortable,
/*TODO*///				pen_usage,
/*TODO*///				PALETTE_COLOR_VISIBLE|PALETTE_COLOR_CACHED );
/*TODO*///		}
/*TODO*///		else {
/*TODO*///			palette_decrease_usage_countx(
/*TODO*///				pal_data-Machine->remapped_colortable,
/*TODO*///				num_pens,
/*TODO*///				cached_tile_info->pen_data,
/*TODO*///				PALETTE_COLOR_VISIBLE|PALETTE_COLOR_CACHED );
/*TODO*///		}
/*TODO*///		cached_tile_info->pal_data = NULL;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///static void register_pens( struct cached_tile_info *cached_tile_info, int num_pens ){
/*TODO*///	UINT32 pen_usage = cached_tile_info->pen_usage;
/*TODO*///	if( pen_usage ){
/*TODO*///		palette_increase_usage_count(
/*TODO*///			cached_tile_info->pal_data-Machine->remapped_colortable,
/*TODO*///			pen_usage,
/*TODO*///			PALETTE_COLOR_VISIBLE|PALETTE_COLOR_CACHED );
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		palette_increase_usage_countx(
/*TODO*///			cached_tile_info->pal_data-Machine->remapped_colortable,
/*TODO*///			num_pens,
/*TODO*///			cached_tile_info->pen_data,
/*TODO*///			PALETTE_COLOR_VISIBLE|PALETTE_COLOR_CACHED );
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///void tilemap_set_enable( struct tilemap *tilemap, int enable ){
/*TODO*///	tilemap->enable = enable;
/*TODO*///}
/*TODO*///
/*TODO*///void tilemap_set_flip( struct tilemap *tilemap, int attributes ){
/*TODO*///	if( tilemap==ALL_TILEMAPS ){
/*TODO*///		tilemap = first_tilemap;
/*TODO*///		while( tilemap ){
/*TODO*///			tilemap_set_flip( tilemap, attributes );
/*TODO*///			tilemap = tilemap->next;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else if( tilemap->attributes!=attributes ){
/*TODO*///		tilemap->attributes = attributes;
/*TODO*///		tilemap->orientation = Machine->orientation;
/*TODO*///		if( attributes&TILEMAP_FLIPY ){
/*TODO*///			tilemap->orientation ^= ORIENTATION_FLIP_Y;
/*TODO*///			tilemap->scrolly_delta = tilemap->dy_if_flipped;
/*TODO*///		}
/*TODO*///		else {
/*TODO*///			tilemap->scrolly_delta = tilemap->dy;
/*TODO*///		}
/*TODO*///		if( attributes&TILEMAP_FLIPX ){
/*TODO*///			tilemap->orientation ^= ORIENTATION_FLIP_X;
/*TODO*///			tilemap->scrollx_delta = tilemap->dx_if_flipped;
/*TODO*///		}
/*TODO*///		else {
/*TODO*///			tilemap->scrollx_delta = tilemap->dx;
/*TODO*///		}
/*TODO*///
/*TODO*///		mappings_update( tilemap );
/*TODO*///		tilemap_mark_all_tiles_dirty( tilemap );
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///void tilemap_set_clip( struct tilemap *tilemap, const struct rectangle *clip ){
/*TODO*///	int left,top,right,bottom;
/*TODO*///	if( clip ){
/*TODO*///		left = clip->min_x;
/*TODO*///		top = clip->min_y;
/*TODO*///		right = clip->max_x+1;
/*TODO*///		bottom = clip->max_y+1;
/*TODO*///		if( tilemap->orientation & ORIENTATION_SWAP_XY ){
/*TODO*///			SWAP(left,top)
/*TODO*///			SWAP(right,bottom)
/*TODO*///		}
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_X ){
/*TODO*///			SWAP(left,right)
/*TODO*///			left = screen_width-left;
/*TODO*///			right = screen_width-right;
/*TODO*///		}
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_Y ){
/*TODO*///			SWAP(top,bottom)
/*TODO*///			top = screen_height-top;
/*TODO*///			bottom = screen_height-bottom;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		left = 0;
/*TODO*///		top = 0;
/*TODO*///		right = tilemap->cached_width;
/*TODO*///		bottom = tilemap->cached_height;
/*TODO*///	}
/*TODO*///	tilemap->clip_left = left;
/*TODO*///	tilemap->clip_right = right;
/*TODO*///	tilemap->clip_top = top;
/*TODO*///	tilemap->clip_bottom = bottom;
/*TODO*/////	logerror("clip: %d,%d,%d,%d\n", left,top,right,bottom );
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///void tilemap_set_scroll_cols( struct tilemap *tilemap, int n ){
/*TODO*///	if( tilemap->orientation & ORIENTATION_SWAP_XY ){
/*TODO*///		if (tilemap->scroll_rows != n){
/*TODO*///			tilemap->scroll_rows = n;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		if (tilemap->scroll_cols != n){
/*TODO*///			tilemap->scroll_cols = n;
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///void tilemap_set_scroll_rows( struct tilemap *tilemap, int n ){
/*TODO*///	if( tilemap->orientation & ORIENTATION_SWAP_XY ){
/*TODO*///		if (tilemap->scroll_cols != n){
/*TODO*///			tilemap->scroll_cols = n;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		if (tilemap->scroll_rows != n){
/*TODO*///			tilemap->scroll_rows = n;
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///void tilemap_mark_tile_dirty( struct tilemap *tilemap, int memory_offset ){
/*TODO*///	if( memory_offset<tilemap->max_memory_offset ){
/*TODO*///		int cached_index = tilemap->memory_offset_to_cached_index[memory_offset];
/*TODO*///		if( cached_index>=0 ){
/*TODO*///			tilemap->dirty_vram[cached_index] = 1;
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///void tilemap_mark_all_tiles_dirty( struct tilemap *tilemap ){
/*TODO*///	if( tilemap==ALL_TILEMAPS ){
/*TODO*///		tilemap = first_tilemap;
/*TODO*///		while( tilemap ){
/*TODO*///			tilemap_mark_all_tiles_dirty( tilemap );
/*TODO*///			tilemap = tilemap->next;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		memset( tilemap->dirty_vram, 1, tilemap->num_tiles );
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///void tilemap_mark_all_pixels_dirty( struct tilemap *tilemap ){
/*TODO*///	if( tilemap==ALL_TILEMAPS ){
/*TODO*///		tilemap = first_tilemap;
/*TODO*///		while( tilemap ){
/*TODO*///			tilemap_mark_all_pixels_dirty( tilemap );
/*TODO*///			tilemap = tilemap->next;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		/* invalidate all offscreen tiles */
/*TODO*///		UINT32 cached_tile_index;
/*TODO*///		UINT32 num_pens = tilemap->cached_tile_width*tilemap->cached_tile_height;
/*TODO*///		for( cached_tile_index=0; cached_tile_index<tilemap->num_tiles; cached_tile_index++ ){
/*TODO*///			if( !tilemap->visible[cached_tile_index] ){
/*TODO*///				unregister_pens( &tilemap->cached_tile_info[cached_tile_index], num_pens );
/*TODO*///				tilemap->dirty_vram[cached_tile_index] = 1;
/*TODO*///			}
/*TODO*///		}
/*TODO*///		memset( tilemap->dirty_pixels, 1, tilemap->num_tiles );
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///static void draw_tile(
/*TODO*///		struct tilemap *tilemap,
/*TODO*///		UINT32 cached_index,
/*TODO*///		UINT32 col, UINT32 row
/*TODO*///){
/*TODO*///	struct osd_bitmap *pixmap = tilemap->pixmap;
/*TODO*///	UINT32 tile_width = tilemap->cached_tile_width;
/*TODO*///	UINT32 tile_height = tilemap->cached_tile_height;
/*TODO*///	struct cached_tile_info *cached_tile_info = &tilemap->cached_tile_info[cached_index];
/*TODO*///	const UINT8 *pendata = cached_tile_info->pen_data;
/*TODO*///	const UINT16 *paldata = cached_tile_info->pal_data;
/*TODO*///
/*TODO*///	UINT32 flags = cached_tile_info->flags;
/*TODO*///	int x, sx = tile_width*col;
/*TODO*///	int sy,y1,y2,dy;
/*TODO*///
/*TODO*///	if( Machine->scrbitmap->depth==16 ){
/*TODO*///		if( flags&TILE_FLIPY ){
/*TODO*///			y1 = tile_height*row+tile_height-1;
/*TODO*///			y2 = y1-tile_height;
/*TODO*///	 		dy = -1;
/*TODO*///	 	}
/*TODO*///	 	else {
/*TODO*///			y1 = tile_height*row;
/*TODO*///			y2 = y1+tile_height;
/*TODO*///	 		dy = 1;
/*TODO*///	 	}
/*TODO*///
/*TODO*///		if( flags&TILE_FLIPX ){
/*TODO*///			tile_width--;
/*TODO*///			for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///				UINT16 *dest  = sx + (UINT16 *)pixmap->line[sy];
/*TODO*///				for( x=tile_width; x>=0; x-- ) dest[x] = paldata[*pendata++];
/*TODO*///			}
/*TODO*///		}
/*TODO*///		else {
/*TODO*///			for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///				UINT16 *dest  = sx + (UINT16 *)pixmap->line[sy];
/*TODO*///				for( x=0; x<tile_width; x++ ) dest[x] = paldata[*pendata++];
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		if( flags&TILE_FLIPY ){
/*TODO*///			y1 = tile_height*row+tile_height-1;
/*TODO*///			y2 = y1-tile_height;
/*TODO*///	 		dy = -1;
/*TODO*///	 	}
/*TODO*///	 	else {
/*TODO*///			y1 = tile_height*row;
/*TODO*///			y2 = y1+tile_height;
/*TODO*///	 		dy = 1;
/*TODO*///	 	}
/*TODO*///
/*TODO*///		if( flags&TILE_FLIPX ){
/*TODO*///			tile_width--;
/*TODO*///			for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///				UINT8 *dest  = sx + (UINT8 *)pixmap->line[sy];
/*TODO*///				for( x=tile_width; x>=0; x-- ) dest[x] = paldata[*pendata++];
/*TODO*///			}
/*TODO*///		}
/*TODO*///		else {
/*TODO*///			for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///				UINT8 *dest  = sx + (UINT8 *)pixmap->line[sy];
/*TODO*///				for( x=0; x<tile_width; x++ ) dest[x] = paldata[*pendata++];
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///void tilemap_render( struct tilemap *tilemap ){
/*TODO*///profiler_mark(PROFILER_TILEMAP_RENDER);
/*TODO*///	if( tilemap==ALL_TILEMAPS ){
/*TODO*///		tilemap = first_tilemap;
/*TODO*///		while( tilemap ){
/*TODO*///			tilemap_render( tilemap );
/*TODO*///			tilemap = tilemap->next;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else if( tilemap->enable ){
/*TODO*///		UINT8 *dirty_pixels = tilemap->dirty_pixels;
/*TODO*///		const UINT8 *visible = tilemap->visible;
/*TODO*///		UINT32 cached_index = 0;
/*TODO*///		UINT32 row,col;
/*TODO*///
/*TODO*///		/* walk over cached rows/cols (better to walk screen coords) */
/*TODO*///		for( row=0; row<tilemap->num_cached_rows; row++ ){
/*TODO*///			for( col=0; col<tilemap->num_cached_cols; col++ ){
/*TODO*///				if( visible[cached_index] && dirty_pixels[cached_index] ){
/*TODO*///					draw_tile( tilemap, cached_index, col, row );
/*TODO*///					dirty_pixels[cached_index] = 0;
/*TODO*///				}
/*TODO*///				cached_index++;
/*TODO*///			} /* next col */
/*TODO*///		} /* next row */
/*TODO*///	}
/*TODO*///profiler_mark(PROFILER_END);
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///static int draw_bitmask(
/*TODO*///		struct osd_bitmap *mask,
/*TODO*///		UINT32 col, UINT32 row,
/*TODO*///		UINT32 tile_width, UINT32 tile_height,
/*TODO*///		const UINT8 *maskdata,
/*TODO*///		UINT32 flags )
/*TODO*///{
/*TODO*///	int is_opaque = 1, is_transparent = 1;
/*TODO*///	int x,sx = tile_width*col;
/*TODO*///	int sy,y1,y2,dy;
/*TODO*///
/*TODO*///	if( maskdata==TILEMAP_BITMASK_TRANSPARENT ) return TILE_TRANSPARENT;
/*TODO*///	if( maskdata==TILEMAP_BITMASK_OPAQUE) return TILE_OPAQUE;
/*TODO*///
/*TODO*///	if( flags&TILE_FLIPY ){
/*TODO*///		y1 = tile_height*row+tile_height-1;
/*TODO*///		y2 = y1-tile_height;
/*TODO*/// 		dy = -1;
/*TODO*/// 	}
/*TODO*/// 	else {
/*TODO*///		y1 = tile_height*row;
/*TODO*///		y2 = y1+tile_height;
/*TODO*/// 		dy = 1;
/*TODO*/// 	}
/*TODO*///
/*TODO*///	if( flags&TILE_FLIPX ){
/*TODO*///		tile_width--;
/*TODO*///		for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///			UINT8 *mask_dest  = mask->line[sy]+sx/8;
/*TODO*///			for( x=tile_width/8; x>=0; x-- ){
/*TODO*///				UINT8 data = flip_bit_table[*maskdata++];
/*TODO*///				if( data!=0x00 ) is_transparent = 0;
/*TODO*///				if( data!=0xff ) is_opaque = 0;
/*TODO*///				mask_dest[x] = data;
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///			UINT8 *mask_dest  = mask->line[sy]+sx/8;
/*TODO*///			for( x=0; x<tile_width/8; x++ ){
/*TODO*///				UINT8 data = *maskdata++;
/*TODO*///				if( data!=0x00 ) is_transparent = 0;
/*TODO*///				if( data!=0xff ) is_opaque = 0;
/*TODO*///				mask_dest[x] = data;
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///
/*TODO*///	if( is_transparent ) return TILE_TRANSPARENT;
/*TODO*///	if( is_opaque ) return TILE_OPAQUE;
/*TODO*///	return TILE_MASKED;
/*TODO*///}
/*TODO*///
/*TODO*///static int draw_color_mask(
/*TODO*///	struct osd_bitmap *mask,
/*TODO*///	UINT32 col, UINT32 row,
/*TODO*///	UINT32 tile_width, UINT32 tile_height,
/*TODO*///	const UINT8 *pendata,
/*TODO*///	const UINT16 *clut,
/*TODO*///	int transparent_color,
/*TODO*///	UINT32 flags )
/*TODO*///{
/*TODO*///	int is_opaque = 1, is_transparent = 1;
/*TODO*///
/*TODO*///	int x,bit,sx = tile_width*col;
/*TODO*///	int sy,y1,y2,dy;
/*TODO*///
/*TODO*///	if( flags&TILE_FLIPY ){
/*TODO*///		y1 = tile_height*row+tile_height-1;
/*TODO*///		y2 = y1-tile_height;
/*TODO*/// 		dy = -1;
/*TODO*/// 	}
/*TODO*/// 	else {
/*TODO*///		y1 = tile_height*row;
/*TODO*///		y2 = y1+tile_height;
/*TODO*/// 		dy = 1;
/*TODO*/// 	}
/*TODO*///
/*TODO*///	if( flags&TILE_FLIPX ){
/*TODO*///		tile_width--;
/*TODO*///		for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///			UINT8 *mask_dest  = mask->line[sy]+sx/8;
/*TODO*///			for( x=tile_width/8; x>=0; x-- ){
/*TODO*///				UINT32 data = 0;
/*TODO*///				for( bit=0; bit<8; bit++ ){
/*TODO*///					UINT32 pen = *pendata++;
/*TODO*///					data = data>>1;
/*TODO*///					if( clut[pen]!=transparent_color ) data |=0x80;
/*TODO*///				}
/*TODO*///				if( data!=0x00 ) is_transparent = 0;
/*TODO*///				if( data!=0xff ) is_opaque = 0;
/*TODO*///				mask_dest[x] = data;
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///			UINT8 *mask_dest  = mask->line[sy]+sx/8;
/*TODO*///			for( x=0; x<tile_width/8; x++ ){
/*TODO*///				UINT32 data = 0;
/*TODO*///				for( bit=0; bit<8; bit++ ){
/*TODO*///					UINT32 pen = *pendata++;
/*TODO*///					data = data<<1;
/*TODO*///					if( clut[pen]!=transparent_color ) data |=0x01;
/*TODO*///				}
/*TODO*///				if( data!=0x00 ) is_transparent = 0;
/*TODO*///				if( data!=0xff ) is_opaque = 0;
/*TODO*///				mask_dest[x] = data;
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///	if( is_transparent ) return TILE_TRANSPARENT;
/*TODO*///	if( is_opaque ) return TILE_OPAQUE;
/*TODO*///	return TILE_MASKED;
/*TODO*///}
/*TODO*///
/*TODO*///static int draw_pen_mask(
/*TODO*///	struct osd_bitmap *mask,
/*TODO*///	UINT32 col, UINT32 row,
/*TODO*///	UINT32 tile_width, UINT32 tile_height,
/*TODO*///	const UINT8 *pendata,
/*TODO*///	int transparent_pen,
/*TODO*///	UINT32 flags )
/*TODO*///{
/*TODO*///	int is_opaque = 1, is_transparent = 1;
/*TODO*///
/*TODO*///	int x,bit,sx = tile_width*col;
/*TODO*///	int sy,y1,y2,dy;
/*TODO*///
/*TODO*///	if( flags&TILE_FLIPY ){
/*TODO*///		y1 = tile_height*row+tile_height-1;
/*TODO*///		y2 = y1-tile_height;
/*TODO*/// 		dy = -1;
/*TODO*/// 	}
/*TODO*/// 	else {
/*TODO*///		y1 = tile_height*row;
/*TODO*///		y2 = y1+tile_height;
/*TODO*/// 		dy = 1;
/*TODO*/// 	}
/*TODO*///
/*TODO*///	if( flags&TILE_FLIPX ){
/*TODO*///		tile_width--;
/*TODO*///		for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///			UINT8 *mask_dest  = mask->line[sy]+sx/8;
/*TODO*///			for( x=tile_width/8; x>=0; x-- ){
/*TODO*///				UINT32 data = 0;
/*TODO*///				for( bit=0; bit<8; bit++ ){
/*TODO*///					UINT32 pen = *pendata++;
/*TODO*///					data = data>>1;
/*TODO*///					if( pen!=transparent_pen ) data |=0x80;
/*TODO*///				}
/*TODO*///				if( data!=0x00 ) is_transparent = 0;
/*TODO*///				if( data!=0xff ) is_opaque = 0;
/*TODO*///				mask_dest[x] = data;
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///			UINT8 *mask_dest  = mask->line[sy]+sx/8;
/*TODO*///			for( x=0; x<tile_width/8; x++ ){
/*TODO*///				UINT32 data = 0;
/*TODO*///				for( bit=0; bit<8; bit++ ){
/*TODO*///					UINT32 pen = *pendata++;
/*TODO*///					data = data<<1;
/*TODO*///					if( pen!=transparent_pen ) data |=0x01;
/*TODO*///				}
/*TODO*///				if( data!=0x00 ) is_transparent = 0;
/*TODO*///				if( data!=0xff ) is_opaque = 0;
/*TODO*///				mask_dest[x] = data;
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///	if( is_transparent ) return TILE_TRANSPARENT;
/*TODO*///	if( is_opaque ) return TILE_OPAQUE;
/*TODO*///	return TILE_MASKED;
/*TODO*///}
/*TODO*///
/*TODO*///static void draw_mask(
/*TODO*///	struct osd_bitmap *mask,
/*TODO*///	UINT32 col, UINT32 row,
/*TODO*///	UINT32 tile_width, UINT32 tile_height,
/*TODO*///	const UINT8 *pendata,
/*TODO*///	UINT32 transmask,
/*TODO*///	UINT32 flags )
/*TODO*///{
/*TODO*///	int x,bit,sx = tile_width*col;
/*TODO*///	int sy,y1,y2,dy;
/*TODO*///
/*TODO*///	if( flags&TILE_FLIPY ){
/*TODO*///		y1 = tile_height*row+tile_height-1;
/*TODO*///		y2 = y1-tile_height;
/*TODO*/// 		dy = -1;
/*TODO*/// 	}
/*TODO*/// 	else {
/*TODO*///		y1 = tile_height*row;
/*TODO*///		y2 = y1+tile_height;
/*TODO*/// 		dy = 1;
/*TODO*/// 	}
/*TODO*///
/*TODO*///	if( flags&TILE_FLIPX ){
/*TODO*///		tile_width--;
/*TODO*///		for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///			UINT8 *mask_dest  = mask->line[sy]+sx/8;
/*TODO*///			for( x=tile_width/8; x>=0; x-- ){
/*TODO*///				UINT32 data = 0;
/*TODO*///				for( bit=0; bit<8; bit++ ){
/*TODO*///					UINT32 pen = *pendata++;
/*TODO*///					data = data>>1;
/*TODO*///					if( !((1<<pen)&transmask) ) data |= 0x80;
/*TODO*///				}
/*TODO*///				mask_dest[x] = data;
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		for( sy=y1; sy!=y2; sy+=dy ){
/*TODO*///			UINT8 *mask_dest  = mask->line[sy]+sx/8;
/*TODO*///			for( x=0; x<tile_width/8; x++ ){
/*TODO*///				UINT32 data = 0;
/*TODO*///				for( bit=0; bit<8; bit++ ){
/*TODO*///					UINT32 pen = *pendata++;
/*TODO*///					data = (data<<1);
/*TODO*///					if( !((1<<pen)&transmask) ) data |= 0x01;
/*TODO*///				}
/*TODO*///				mask_dest[x] = data;
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///static void render_mask( struct tilemap *tilemap, UINT32 cached_index ){
/*TODO*///	const struct cached_tile_info *cached_tile_info = &tilemap->cached_tile_info[cached_index];
/*TODO*///	UINT32 col = cached_index%tilemap->num_cached_cols;
/*TODO*///	UINT32 row = cached_index/tilemap->num_cached_cols;
/*TODO*///	UINT32 type = tilemap->type;
/*TODO*///
/*TODO*///	UINT32 transparent_pen = tilemap->transparent_pen;
/*TODO*///	UINT32 *transmask = tilemap->transmask;
/*TODO*///	UINT32 tile_width = tilemap->cached_tile_width;
/*TODO*///	UINT32 tile_height = tilemap->cached_tile_height;
/*TODO*///
/*TODO*///	UINT32 pen_usage = cached_tile_info->pen_usage;
/*TODO*///	const UINT8 *pen_data = cached_tile_info->pen_data;
/*TODO*///	UINT32 flags = cached_tile_info->flags;
/*TODO*///
/*TODO*///	if( type & TILEMAP_BITMASK ){
/*TODO*///		tilemap->foreground->data_row[row][col] =
/*TODO*///			draw_bitmask( tilemap->foreground->bitmask,col, row,
/*TODO*///				tile_width, tile_height,tile_info.mask_data, flags );
/*TODO*///	}
/*TODO*///	else if( type & TILEMAP_SPLIT ){
/*TODO*///		UINT32 pen_mask = (transparent_pen<0)?0:(1<<transparent_pen);
/*TODO*///		if( flags&TILE_IGNORE_TRANSPARENCY ){
/*TODO*///			tilemap->foreground->data_row[row][col] = TILE_OPAQUE;
/*TODO*///			tilemap->background->data_row[row][col] = TILE_OPAQUE;
/*TODO*///		}
/*TODO*///		else if( pen_mask == pen_usage ){ /* totally transparent */
/*TODO*///			tilemap->foreground->data_row[row][col] = TILE_TRANSPARENT;
/*TODO*///			tilemap->background->data_row[row][col] = TILE_TRANSPARENT;
/*TODO*///		}
/*TODO*///		else {
/*TODO*///			UINT32 fg_transmask = transmask[(flags>>2)&3];
/*TODO*///			UINT32 bg_transmask = (~fg_transmask)|pen_mask;
/*TODO*///			if( (pen_usage & fg_transmask)==0 ){ /* foreground totally opaque */
/*TODO*///				tilemap->foreground->data_row[row][col] = TILE_OPAQUE;
/*TODO*///				tilemap->background->data_row[row][col] = TILE_TRANSPARENT;
/*TODO*///			}
/*TODO*///			else if( (pen_usage & bg_transmask)==0 ){ /* background totally opaque */
/*TODO*///				tilemap->foreground->data_row[row][col] = TILE_TRANSPARENT;
/*TODO*///				tilemap->background->data_row[row][col] = TILE_OPAQUE;
/*TODO*///			}
/*TODO*///			else if( (pen_usage & ~bg_transmask)==0 ){ /* background transparent */
/*TODO*///				draw_mask( tilemap->foreground->bitmask,
/*TODO*///					col, row, tile_width, tile_height,
/*TODO*///					pen_data, fg_transmask, flags );
/*TODO*///				tilemap->foreground->data_row[row][col] = TILE_MASKED;
/*TODO*///				tilemap->background->data_row[row][col] = TILE_TRANSPARENT;
/*TODO*///			}
/*TODO*///			else if( (pen_usage & ~fg_transmask)==0 ){ /* foreground transparent */
/*TODO*///				draw_mask( tilemap->background->bitmask,
/*TODO*///					col, row, tile_width, tile_height,
/*TODO*///					pen_data, bg_transmask, flags );
/*TODO*///				tilemap->foreground->data_row[row][col] = TILE_TRANSPARENT;
/*TODO*///				tilemap->background->data_row[row][col] = TILE_MASKED;
/*TODO*///			}
/*TODO*///			else { /* split tile - opacity in both foreground and background */
/*TODO*///				draw_mask( tilemap->foreground->bitmask,
/*TODO*///					col, row, tile_width, tile_height,
/*TODO*///					pen_data, fg_transmask, flags );
/*TODO*///				draw_mask( tilemap->background->bitmask,
/*TODO*///					col, row, tile_width, tile_height,
/*TODO*///					pen_data, bg_transmask, flags );
/*TODO*///				tilemap->foreground->data_row[row][col] = TILE_MASKED;
/*TODO*///				tilemap->background->data_row[row][col] = TILE_MASKED;
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else if( type==TILEMAP_TRANSPARENT ){
/*TODO*///		if( pen_usage ){
/*TODO*///			UINT32 fg_transmask = 1 << transparent_pen;
/*TODO*///		 	if( flags&TILE_IGNORE_TRANSPARENCY ) fg_transmask = 0;
/*TODO*///			if( pen_usage == fg_transmask ){
/*TODO*///				tilemap->foreground->data_row[row][col] = TILE_TRANSPARENT;
/*TODO*///			}
/*TODO*///			else if( pen_usage & fg_transmask ){
/*TODO*///				draw_mask( tilemap->foreground->bitmask,
/*TODO*///					col, row, tile_width, tile_height,
/*TODO*///					pen_data, fg_transmask, flags );
/*TODO*///				tilemap->foreground->data_row[row][col] = TILE_MASKED;
/*TODO*///			}
/*TODO*///			else {
/*TODO*///				tilemap->foreground->data_row[row][col] = TILE_OPAQUE;
/*TODO*///			}
/*TODO*///		}
/*TODO*///		else {
/*TODO*///			tilemap->foreground->data_row[row][col] =
/*TODO*///				draw_pen_mask(
/*TODO*///					tilemap->foreground->bitmask,
/*TODO*///					col, row, tile_width, tile_height,
/*TODO*///					pen_data,
/*TODO*///					transparent_pen,
/*TODO*///					flags
/*TODO*///				);
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else if( type==TILEMAP_TRANSPARENT_COLOR ){
/*TODO*///		tilemap->foreground->data_row[row][col] =
/*TODO*///			draw_color_mask(
/*TODO*///				tilemap->foreground->bitmask,
/*TODO*///				col, row, tile_width, tile_height,
/*TODO*///				pen_data,
/*TODO*///				Machine->game_colortable +
/*TODO*///					(cached_tile_info->pal_data - Machine->remapped_colortable),
/*TODO*///				transparent_pen,
/*TODO*///				flags
/*TODO*///			);
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		tilemap->foreground->data_row[row][col] = TILE_OPAQUE;
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///static void update_tile_info( struct tilemap *tilemap ){
/*TODO*///	int *logical_flip_to_cached_flip = tilemap->logical_flip_to_cached_flip;
/*TODO*///	UINT32 num_pens = tilemap->cached_tile_width*tilemap->cached_tile_height;
/*TODO*///	UINT32 num_tiles = tilemap->num_tiles;
/*TODO*///	UINT32 cached_index;
/*TODO*///	UINT8 *visible = tilemap->visible;
/*TODO*///	UINT8 *dirty_vram = tilemap->dirty_vram;
/*TODO*///	UINT8 *dirty_pixels = tilemap->dirty_pixels;
/*TODO*///	tile_info.flags = 0;
/*TODO*///	tile_info.priority = 0;
/*TODO*///	for( cached_index=0; cached_index<num_tiles; cached_index++ ){
/*TODO*///		if( visible[cached_index] && dirty_vram[cached_index] ){
/*TODO*///			struct cached_tile_info *cached_tile_info = &tilemap->cached_tile_info[cached_index];
/*TODO*///			UINT32 memory_offset = tilemap->cached_index_to_memory_offset[cached_index];
/*TODO*///			unregister_pens( cached_tile_info, num_pens );
/*TODO*///			tilemap->tile_get_info( memory_offset );
/*TODO*///			{
/*TODO*///				UINT32 flags = tile_info.flags;
/*TODO*///				cached_tile_info->flags = (flags&0xfc)|logical_flip_to_cached_flip[flags&0x3];
/*TODO*///			}
/*TODO*///			cached_tile_info->pen_usage = tile_info.pen_usage;
/*TODO*///			cached_tile_info->pen_data = tile_info.pen_data;
/*TODO*///			cached_tile_info->pal_data = tile_info.pal_data;
/*TODO*///			tilemap->priority[cached_index] = tile_info.priority;
/*TODO*///			register_pens( cached_tile_info, num_pens );
/*TODO*///			dirty_pixels[cached_index] = 1;
/*TODO*///			dirty_vram[cached_index] = 0;
/*TODO*///			render_mask( tilemap, cached_index );
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///
/*TODO*///static void update_visible( struct tilemap *tilemap ){
/*TODO*///	// temporary hack
/*TODO*///	memset( tilemap->visible, 1, tilemap->num_tiles );
/*TODO*///
/*TODO*///#if 0
/*TODO*///	int yscroll = scrolly[0];
/*TODO*///	int row0, y0;
/*TODO*///
/*TODO*///	int xscroll = scrollx[0];
/*TODO*///	int col0, x0;
/*TODO*///
/*TODO*///	if( yscroll>=0 ){
/*TODO*///		row0 = yscroll/tile_height;
/*TODO*///		y0 = -(yscroll%tile_height);
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		yscroll = tile_height-1-yscroll;
/*TODO*///		row0 = num_rows - yscroll/tile_height;
/*TODO*///		y0 = (yscroll+1)%tile_height;
/*TODO*///		if( y0 ) y0 = y0-tile_height;
/*TODO*///	}
/*TODO*///
/*TODO*///	if( xscroll>=0 ){
/*TODO*///		col0 = xscroll/tile_width;
/*TODO*///		x0 = -(xscroll%tile_width);
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		xscroll = tile_width-1-xscroll;
/*TODO*///		col0 = num_cols - xscroll/tile_width;
/*TODO*///		x0 = (xscroll+1)%tile_width;
/*TODO*///		if( x0 ) x0 = x0-tile_width;
/*TODO*///	}
/*TODO*///
/*TODO*///	{
/*TODO*///		int ypos = y0;
/*TODO*///		int row = row0;
/*TODO*///		while( ypos<screen_height ){
/*TODO*///			int xpos = x0;
/*TODO*///			int col = col0;
/*TODO*///			while( xpos<screen_width ){
/*TODO*///				process_visible_tile( col, row );
/*TODO*///				col++;
/*TODO*///				if( col>=num_cols ) col = 0;
/*TODO*///				xpos += tile_width;
/*TODO*///			}
/*TODO*///			row++;
/*TODO*///			if( row>=num_rows ) row = 0;
/*TODO*///			ypos += tile_height;
/*TODO*///		}
/*TODO*///	}
/*TODO*///#endif
/*TODO*///}
/*TODO*///
/*TODO*///void tilemap_update( struct tilemap *tilemap ){
/*TODO*///profiler_mark(PROFILER_TILEMAP_UPDATE);
/*TODO*///	if( tilemap==ALL_TILEMAPS ){
/*TODO*///		tilemap = first_tilemap;
/*TODO*///		while( tilemap ){
/*TODO*///			tilemap_update( tilemap );
/*TODO*///			tilemap = tilemap->next;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else if( tilemap->enable ){
/*TODO*///		update_visible( tilemap );
/*TODO*///		update_tile_info( tilemap );
/*TODO*///	}
/*TODO*///profiler_mark(PROFILER_END);
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///void tilemap_set_scrolldx( struct tilemap *tilemap, int dx, int dx_if_flipped ){
/*TODO*///	tilemap->dx = dx;
/*TODO*///	tilemap->dx_if_flipped = dx_if_flipped;
/*TODO*///	tilemap->scrollx_delta = ( tilemap->attributes & TILEMAP_FLIPX )?dx_if_flipped:dx;
/*TODO*///}
/*TODO*///
/*TODO*///void tilemap_set_scrolldy( struct tilemap *tilemap, int dy, int dy_if_flipped ){
/*TODO*///	tilemap->dy = dy;
/*TODO*///	tilemap->dy_if_flipped = dy_if_flipped;
/*TODO*///	tilemap->scrolly_delta = ( tilemap->attributes & TILEMAP_FLIPY )?dy_if_flipped:dy;
/*TODO*///}
/*TODO*///
/*TODO*///void tilemap_set_scrollx( struct tilemap *tilemap, int which, int value ){
/*TODO*///	value = tilemap->scrollx_delta-value;
/*TODO*///
/*TODO*///	if( tilemap->orientation & ORIENTATION_SWAP_XY ){
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_X ) which = tilemap->scroll_cols-1 - which;
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_Y ) value = screen_height-tilemap->cached_height-value;
/*TODO*///		if( tilemap->colscroll[which]!=value ){
/*TODO*///			tilemap->colscroll[which] = value;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_Y ) which = tilemap->scroll_rows-1 - which;
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_X ) value = screen_width-tilemap->cached_width-value;
/*TODO*///		if( tilemap->rowscroll[which]!=value ){
/*TODO*///			tilemap->rowscroll[which] = value;
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*///void tilemap_set_scrolly( struct tilemap *tilemap, int which, int value ){
/*TODO*///	value = tilemap->scrolly_delta - value;
/*TODO*///
/*TODO*///	if( tilemap->orientation & ORIENTATION_SWAP_XY ){
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_Y ) which = tilemap->scroll_rows-1 - which;
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_X ) value = screen_width-tilemap->cached_width-value;
/*TODO*///		if( tilemap->rowscroll[which]!=value ){
/*TODO*///			tilemap->rowscroll[which] = value;
/*TODO*///		}
/*TODO*///	}
/*TODO*///	else {
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_X ) which = tilemap->scroll_cols-1 - which;
/*TODO*///		if( tilemap->orientation & ORIENTATION_FLIP_Y ) value = screen_height-tilemap->cached_height-value;
/*TODO*///		if( tilemap->colscroll[which]!=value ){
/*TODO*///			tilemap->colscroll[which] = value;
/*TODO*///		}
/*TODO*///	}
/*TODO*///}
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///void tilemap_draw( struct osd_bitmap *dest, struct tilemap *tilemap, UINT32 priority ){
/*TODO*///	int xpos,ypos;
/*TODO*///
/*TODO*///profiler_mark(PROFILER_TILEMAP_DRAW);
/*TODO*///	if( tilemap->enable ){
/*TODO*///		void (*draw)( int, int );
/*TODO*///
/*TODO*///		int rows = tilemap->scroll_rows;
/*TODO*///		const int *rowscroll = tilemap->rowscroll;
/*TODO*///		int cols = tilemap->scroll_cols;
/*TODO*///		const int *colscroll = tilemap->colscroll;
/*TODO*///
/*TODO*///		int left = tilemap->clip_left;
/*TODO*///		int right = tilemap->clip_right;
/*TODO*///		int top = tilemap->clip_top;
/*TODO*///		int bottom = tilemap->clip_bottom;
/*TODO*///
/*TODO*///		int tile_height = tilemap->cached_tile_height;
/*TODO*///
/*TODO*///		blit.screen = dest;
/*TODO*///		blit.dest_line_offset = dest->line[1] - dest->line[0];
/*TODO*///
/*TODO*///		blit.pixmap = tilemap->pixmap;
/*TODO*///		blit.source_line_offset = tilemap->pixmap_line_offset;
/*TODO*///
/*TODO*///		if( tilemap->type==TILEMAP_OPAQUE || (priority&TILEMAP_IGNORE_TRANSPARENCY) ){
/*TODO*///			draw = tilemap->draw_opaque;
/*TODO*///		}
/*TODO*///		else {
/*TODO*///			draw = tilemap->draw;
/*TODO*///			if( priority&TILEMAP_BACK ){
/*TODO*///				blit.bitmask = tilemap->background->bitmask;
/*TODO*///				blit.mask_line_offset = tilemap->background->line_offset;
/*TODO*///				blit.mask_data_row = tilemap->background->data_row;
/*TODO*///			}
/*TODO*///			else {
/*TODO*///				blit.bitmask = tilemap->foreground->bitmask;
/*TODO*///				blit.mask_line_offset = tilemap->foreground->line_offset;
/*TODO*///				blit.mask_data_row = tilemap->foreground->data_row;
/*TODO*///			}
/*TODO*///
/*TODO*///			blit.mask_row_offset = tile_height*blit.mask_line_offset;
/*TODO*///		}
/*TODO*///
/*TODO*///		if( dest->depth==16 ){
/*TODO*///			blit.dest_line_offset /= 2;
/*TODO*///			blit.source_line_offset /= 2;
/*TODO*///		}
/*TODO*///
/*TODO*///		blit.source_row_offset = tile_height*blit.source_line_offset;
/*TODO*///		blit.dest_row_offset = tile_height*blit.dest_line_offset;
/*TODO*///
/*TODO*///		blit.priority_data_row = tilemap->priority_row;
/*TODO*///		blit.source_width = tilemap->cached_width;
/*TODO*///		blit.source_height = tilemap->cached_height;
/*TODO*///		blit.tile_priority = priority&0xf;
/*TODO*///		blit.tilemap_priority_code = priority>>16;
/*TODO*///
/*TODO*///		if( rows == 1 && cols == 1 ){ /* XY scrolling playfield */
/*TODO*///			int scrollx = rowscroll[0];
/*TODO*///			int scrolly = colscroll[0];
/*TODO*///
/*TODO*///			if( scrollx < 0 ){
/*TODO*///				scrollx = blit.source_width - (-scrollx) % blit.source_width;
/*TODO*///			}
/*TODO*///			else {
/*TODO*///				scrollx = scrollx % blit.source_width;
/*TODO*///			}
/*TODO*///
/*TODO*///			if( scrolly < 0 ){
/*TODO*///				scrolly = blit.source_height - (-scrolly) % blit.source_height;
/*TODO*///			}
/*TODO*///			else {
/*TODO*///				scrolly = scrolly % blit.source_height;
/*TODO*///			}
/*TODO*///
/*TODO*///	 		blit.clip_left = left;
/*TODO*///	 		blit.clip_top = top;
/*TODO*///	 		blit.clip_right = right;
/*TODO*///	 		blit.clip_bottom = bottom;
/*TODO*///
/*TODO*///			for(
/*TODO*///				ypos = scrolly - blit.source_height;
/*TODO*///				ypos < blit.clip_bottom;
/*TODO*///				ypos += blit.source_height
/*TODO*///			){
/*TODO*///				for(
/*TODO*///					xpos = scrollx - blit.source_width;
/*TODO*///					xpos < blit.clip_right;
/*TODO*///					xpos += blit.source_width
/*TODO*///				){
/*TODO*///					draw( xpos,ypos );
/*TODO*///				}
/*TODO*///			}
/*TODO*///		}
/*TODO*///		else if( rows == 1 ){ /* scrolling columns + horizontal scroll */
/*TODO*///			int col = 0;
/*TODO*///			int colwidth = blit.source_width / cols;
/*TODO*///			int scrollx = rowscroll[0];
/*TODO*///
/*TODO*///			if( scrollx < 0 ){
/*TODO*///				scrollx = blit.source_width - (-scrollx) % blit.source_width;
/*TODO*///			}
/*TODO*///			else {
/*TODO*///				scrollx = scrollx % blit.source_width;
/*TODO*///			}
/*TODO*///
/*TODO*///			blit.clip_top = top;
/*TODO*///			blit.clip_bottom = bottom;
/*TODO*///
/*TODO*///			while( col < cols ){
/*TODO*///				int cons = 1;
/*TODO*///				int scrolly = colscroll[col];
/*TODO*///
/*TODO*///	 			/* count consecutive columns scrolled by the same amount */
/*TODO*///				if( scrolly != TILE_LINE_DISABLED ){
/*TODO*///					while( col + cons < cols &&	colscroll[col + cons] == scrolly ) cons++;
/*TODO*///
/*TODO*///					if( scrolly < 0 ){
/*TODO*///						scrolly = blit.source_height - (-scrolly) % blit.source_height;
/*TODO*///					}
/*TODO*///					else {
/*TODO*///						scrolly %= blit.source_height;
/*TODO*///					}
/*TODO*///
/*TODO*///					blit.clip_left = col * colwidth + scrollx;
/*TODO*///					if (blit.clip_left < left) blit.clip_left = left;
/*TODO*///					blit.clip_right = (col + cons) * colwidth + scrollx;
/*TODO*///					if (blit.clip_right > right) blit.clip_right = right;
/*TODO*///
/*TODO*///					for(
/*TODO*///						ypos = scrolly - blit.source_height;
/*TODO*///						ypos < blit.clip_bottom;
/*TODO*///						ypos += blit.source_height
/*TODO*///					){
/*TODO*///						draw( scrollx,ypos );
/*TODO*///					}
/*TODO*///
/*TODO*///					blit.clip_left = col * colwidth + scrollx - blit.source_width;
/*TODO*///					if (blit.clip_left < left) blit.clip_left = left;
/*TODO*///					blit.clip_right = (col + cons) * colwidth + scrollx - blit.source_width;
/*TODO*///					if (blit.clip_right > right) blit.clip_right = right;
/*TODO*///
/*TODO*///					for(
/*TODO*///						ypos = scrolly - blit.source_height;
/*TODO*///						ypos < blit.clip_bottom;
/*TODO*///						ypos += blit.source_height
/*TODO*///					){
/*TODO*///						draw( scrollx - blit.source_width,ypos );
/*TODO*///					}
/*TODO*///				}
/*TODO*///				col += cons;
/*TODO*///			}
/*TODO*///		}
/*TODO*///		else if( cols == 1 ){ /* scrolling rows + vertical scroll */
/*TODO*///			int row = 0;
/*TODO*///			int rowheight = blit.source_height / rows;
/*TODO*///			int scrolly = colscroll[0];
/*TODO*///			if( scrolly < 0 ){
/*TODO*///				scrolly = blit.source_height - (-scrolly) % blit.source_height;
/*TODO*///			}
/*TODO*///			else {
/*TODO*///				scrolly = scrolly % blit.source_height;
/*TODO*///			}
/*TODO*///			blit.clip_left = left;
/*TODO*///			blit.clip_right = right;
/*TODO*///			while( row < rows ){
/*TODO*///				int cons = 1;
/*TODO*///				int scrollx = rowscroll[row];
/*TODO*///				/* count consecutive rows scrolled by the same amount */
/*TODO*///				if( scrollx != TILE_LINE_DISABLED ){
/*TODO*///					while( row + cons < rows &&	rowscroll[row + cons] == scrollx ) cons++;
/*TODO*///					if( scrollx < 0){
/*TODO*///						scrollx = blit.source_width - (-scrollx) % blit.source_width;
/*TODO*///					}
/*TODO*///					else {
/*TODO*///						scrollx %= blit.source_width;
/*TODO*///					}
/*TODO*///					blit.clip_top = row * rowheight + scrolly;
/*TODO*///					if (blit.clip_top < top) blit.clip_top = top;
/*TODO*///					blit.clip_bottom = (row + cons) * rowheight + scrolly;
/*TODO*///					if (blit.clip_bottom > bottom) blit.clip_bottom = bottom;
/*TODO*///					for(
/*TODO*///						xpos = scrollx - blit.source_width;
/*TODO*///						xpos < blit.clip_right;
/*TODO*///						xpos += blit.source_width
/*TODO*///					){
/*TODO*///						draw( xpos,scrolly );
/*TODO*///					}
/*TODO*///					blit.clip_top = row * rowheight + scrolly - blit.source_height;
/*TODO*///					if (blit.clip_top < top) blit.clip_top = top;
/*TODO*///					blit.clip_bottom = (row + cons) * rowheight + scrolly - blit.source_height;
/*TODO*///					if (blit.clip_bottom > bottom) blit.clip_bottom = bottom;
/*TODO*///					for(
/*TODO*///						xpos = scrollx - blit.source_width;
/*TODO*///						xpos < blit.clip_right;
/*TODO*///						xpos += blit.source_width
/*TODO*///					){
/*TODO*///						draw( xpos,scrolly - blit.source_height );
/*TODO*///					}
/*TODO*///				}
/*TODO*///				row += cons;
/*TODO*///			}
/*TODO*///		}
/*TODO*///	}
/*TODO*///profiler_mark(PROFILER_END);
/*TODO*///}
/*TODO*///
/*TODO*////***********************************************************************************/
/*TODO*///
/*TODO*///#else // DECLARE
/*TODO*////*
/*TODO*///	The following procedure body is #included several times by
/*TODO*///	tilemap.c to implement a suite of tilemap_draw subroutines.
/*TODO*///
/*TODO*///	The constants TILE_WIDTH and TILE_HEIGHT are different in
/*TODO*///	each instance of this code, allowing arithmetic shifts to
/*TODO*///	be used by the compiler instead of multiplies/divides.
/*TODO*///
/*TODO*///	This routine should be fairly optimal, for C code, though of
/*TODO*///	course there is room for improvement.
/*TODO*///
/*TODO*///	It renders pixels one row at a time, skipping over runs of totally
/*TODO*///	transparent tiles, and calling custom blitters to handle runs of
/*TODO*///	masked/totally opaque tiles.
/*TODO*///*/
/*TODO*///
/*TODO*///DECLARE( draw, (int xpos, int ypos),
/*TODO*///{
/*TODO*///	int tilemap_priority_code = blit.tilemap_priority_code;
/*TODO*///	int x1 = xpos;
/*TODO*///	int y1 = ypos;
/*TODO*///	int x2 = xpos+blit.source_width;
/*TODO*///	int y2 = ypos+blit.source_height;
/*TODO*///
/*TODO*///	/* clip source coordinates */
/*TODO*///	if( x1<blit.clip_left ) x1 = blit.clip_left;
/*TODO*///	if( x2>blit.clip_right ) x2 = blit.clip_right;
/*TODO*///	if( y1<blit.clip_top ) y1 = blit.clip_top;
/*TODO*///	if( y2>blit.clip_bottom ) y2 = blit.clip_bottom;
/*TODO*///
/*TODO*///	if( x1<x2 && y1<y2 ){ /* do nothing if totally clipped */
/*TODO*///		DATA_TYPE *dest_baseaddr = xpos + (DATA_TYPE *)blit.screen->line[y1];
/*TODO*///		DATA_TYPE *dest_next;
/*TODO*///
/*TODO*///		int priority_bitmap_row_offset = priority_bitmap_line_offset*TILE_HEIGHT;
/*TODO*///		UINT8 *priority_bitmap_baseaddr = xpos + (UINT8 *)priority_bitmap->line[y1];
/*TODO*///		UINT8 *priority_bitmap_next;
/*TODO*///
/*TODO*///		int priority = blit.tile_priority;
/*TODO*///		const DATA_TYPE *source_baseaddr;
/*TODO*///		const DATA_TYPE *source_next;
/*TODO*///		const UINT8 *mask_baseaddr;
/*TODO*///		const UINT8 *mask_next;
/*TODO*///
/*TODO*///		int c1;
/*TODO*///		int c2; /* leftmost and rightmost visible columns in source tilemap */
/*TODO*///		int y; /* current screen line to render */
/*TODO*///		int y_next;
/*TODO*///
/*TODO*///		/* convert screen coordinates to source tilemap coordinates */
/*TODO*///		x1 -= xpos;
/*TODO*///		y1 -= ypos;
/*TODO*///		x2 -= xpos;
/*TODO*///		y2 -= ypos;
/*TODO*///
/*TODO*///		source_baseaddr = (DATA_TYPE *)blit.pixmap->line[y1];
/*TODO*///		mask_baseaddr = blit.bitmask->line[y1];
/*TODO*///
/*TODO*///		c1 = x1/TILE_WIDTH; /* round down */
/*TODO*///		c2 = (x2+TILE_WIDTH-1)/TILE_WIDTH; /* round up */
/*TODO*///
/*TODO*///		y = y1;
/*TODO*///		y_next = TILE_HEIGHT*(y1/TILE_HEIGHT) + TILE_HEIGHT;
/*TODO*///		if( y_next>y2 ) y_next = y2;
/*TODO*///
/*TODO*///		{
/*TODO*///			int dy = y_next-y;
/*TODO*///			dest_next = dest_baseaddr + dy*blit.dest_line_offset;
/*TODO*///			priority_bitmap_next = priority_bitmap_baseaddr + dy*priority_bitmap_line_offset;
/*TODO*///			source_next = source_baseaddr + dy*blit.source_line_offset;
/*TODO*///			mask_next = mask_baseaddr + dy*blit.mask_line_offset;
/*TODO*///		}
/*TODO*///
/*TODO*///		for(;;){
/*TODO*///			int row = y/TILE_HEIGHT;
/*TODO*///			UINT8 *mask_data = blit.mask_data_row[row];
/*TODO*///			UINT8 *priority_data = blit.priority_data_row[row];
/*TODO*///
/*TODO*///			int tile_type;
/*TODO*///			int prev_tile_type = TILE_TRANSPARENT;
/*TODO*///
/*TODO*///			int x_start = x1;
/*TODO*///			int x_end;
/*TODO*///
/*TODO*///			int column;
/*TODO*///			for( column=c1; column<=c2; column++ ){
/*TODO*///				if( column==c2 || priority_data[column]!=priority )
/*TODO*///					tile_type = TILE_TRANSPARENT;
/*TODO*///				else
/*TODO*///					tile_type = mask_data[column];
/*TODO*///
/*TODO*///				if( tile_type!=prev_tile_type ){
/*TODO*///					x_end = column*TILE_WIDTH;
/*TODO*///					if( x_end<x1 ) x_end = x1;
/*TODO*///					if( x_end>x2 ) x_end = x2;
/*TODO*///
/*TODO*///					if( prev_tile_type != TILE_TRANSPARENT ){
/*TODO*///						if( prev_tile_type == TILE_MASKED ){
/*TODO*///							int count = (x_end+7)/8 - x_start/8;
/*TODO*///							const UINT8 *mask0 = mask_baseaddr + x_start/8;
/*TODO*///							const DATA_TYPE *source0 = source_baseaddr + (x_start&0xfff8);
/*TODO*///							DATA_TYPE *dest0 = dest_baseaddr + (x_start&0xfff8);
/*TODO*///							UINT8 *pmap0 = priority_bitmap_baseaddr + (x_start&0xfff8);
/*TODO*///							int i = y;
/*TODO*///							for(;;){
/*TODO*///								memcpybitmask( dest0, source0, mask0, count );
/*TODO*///								memsetbitmask8( pmap0, tilemap_priority_code, mask0, count );
/*TODO*///								if( ++i == y_next ) break;
/*TODO*///
/*TODO*///								dest0 += blit.dest_line_offset;
/*TODO*///								source0 += blit.source_line_offset;
/*TODO*///								mask0 += blit.mask_line_offset;
/*TODO*///								pmap0 += priority_bitmap_line_offset;
/*TODO*///							}
/*TODO*///						}
/*TODO*///						else { /* TILE_OPAQUE */
/*TODO*///							int num_pixels = x_end - x_start;
/*TODO*///							DATA_TYPE *dest0 = dest_baseaddr+x_start;
/*TODO*///							const DATA_TYPE *source0 = source_baseaddr+x_start;
/*TODO*///							UINT8 *pmap0 = priority_bitmap_baseaddr + x_start;
/*TODO*///							int i = y;
/*TODO*///							for(;;){
/*TODO*///								memcpy( dest0, source0, num_pixels*sizeof(DATA_TYPE) );
/*TODO*///								memset( pmap0, tilemap_priority_code, num_pixels );
/*TODO*///								if( ++i == y_next ) break;
/*TODO*///
/*TODO*///								dest0 += blit.dest_line_offset;
/*TODO*///								source0 += blit.source_line_offset;
/*TODO*///								pmap0 += priority_bitmap_line_offset;
/*TODO*///							}
/*TODO*///						}
/*TODO*///					}
/*TODO*///					x_start = x_end;
/*TODO*///				}
/*TODO*///
/*TODO*///				prev_tile_type = tile_type;
/*TODO*///			}
/*TODO*///
/*TODO*///			if( y_next==y2 ) break; /* we are done! */
/*TODO*///
/*TODO*///			priority_bitmap_baseaddr = priority_bitmap_next;
/*TODO*///			dest_baseaddr = dest_next;
/*TODO*///			source_baseaddr = source_next;
/*TODO*///			mask_baseaddr = mask_next;
/*TODO*///
/*TODO*///			y = y_next;
/*TODO*///			y_next += TILE_HEIGHT;
/*TODO*///
/*TODO*///			if( y_next>=y2 ){
/*TODO*///				y_next = y2;
/*TODO*///			}
/*TODO*///			else {
/*TODO*///				dest_next += blit.dest_row_offset;
/*TODO*///				priority_bitmap_next += priority_bitmap_row_offset;
/*TODO*///				source_next += blit.source_row_offset;
/*TODO*///				mask_next += blit.mask_row_offset;
/*TODO*///			}
/*TODO*///		} /* process next row */
/*TODO*///	} /* not totally clipped */
/*TODO*///})
/*TODO*///
/*TODO*///DECLARE( draw_opaque, (int xpos, int ypos),
/*TODO*///{
/*TODO*///	int tilemap_priority_code = blit.tilemap_priority_code;
/*TODO*///	int x1 = xpos;
/*TODO*///	int y1 = ypos;
/*TODO*///	int x2 = xpos+blit.source_width;
/*TODO*///	int y2 = ypos+blit.source_height;
/*TODO*///	/* clip source coordinates */
/*TODO*///	if( x1<blit.clip_left ) x1 = blit.clip_left;
/*TODO*///	if( x2>blit.clip_right ) x2 = blit.clip_right;
/*TODO*///	if( y1<blit.clip_top ) y1 = blit.clip_top;
/*TODO*///	if( y2>blit.clip_bottom ) y2 = blit.clip_bottom;
/*TODO*///
/*TODO*///	if( x1<x2 && y1<y2 ){ /* do nothing if totally clipped */
/*TODO*///		UINT8 *priority_bitmap_baseaddr = xpos + (UINT8 *)priority_bitmap->line[y1];
/*TODO*///		int priority_bitmap_row_offset = priority_bitmap_line_offset*TILE_HEIGHT;
/*TODO*///
/*TODO*///		int priority = blit.tile_priority;
/*TODO*///		DATA_TYPE *dest_baseaddr = xpos + (DATA_TYPE *)blit.screen->line[y1];
/*TODO*///		DATA_TYPE *dest_next;
/*TODO*///		const DATA_TYPE *source_baseaddr;
/*TODO*///		const DATA_TYPE *source_next;
/*TODO*///
/*TODO*///		int c1;
/*TODO*///		int c2; /* leftmost and rightmost visible columns in source tilemap */
/*TODO*///		int y; /* current screen line to render */
/*TODO*///		int y_next;
/*TODO*///
/*TODO*///		/* convert screen coordinates to source tilemap coordinates */
/*TODO*///		x1 -= xpos;
/*TODO*///		y1 -= ypos;
/*TODO*///		x2 -= xpos;
/*TODO*///		y2 -= ypos;
/*TODO*///
/*TODO*///		source_baseaddr = (DATA_TYPE *)blit.pixmap->line[y1];
/*TODO*///
/*TODO*///		c1 = x1/TILE_WIDTH; /* round down */
/*TODO*///		c2 = (x2+TILE_WIDTH-1)/TILE_WIDTH; /* round up */
/*TODO*///
/*TODO*///		y = y1;
/*TODO*///		y_next = TILE_HEIGHT*(y1/TILE_HEIGHT) + TILE_HEIGHT;
/*TODO*///		if( y_next>y2 ) y_next = y2;
/*TODO*///
/*TODO*///		{
/*TODO*///			int dy = y_next-y;
/*TODO*///			dest_next = dest_baseaddr + dy*blit.dest_line_offset;
/*TODO*///			source_next = source_baseaddr + dy*blit.source_line_offset;
/*TODO*///		}
/*TODO*///
/*TODO*///		for(;;){
/*TODO*///			int row = y/TILE_HEIGHT;
/*TODO*///			UINT8 *priority_data = blit.priority_data_row[row];
/*TODO*///
/*TODO*///			int tile_type;
/*TODO*///			int prev_tile_type = TILE_TRANSPARENT;
/*TODO*///
/*TODO*///			int x_start = x1;
/*TODO*///			int x_end;
/*TODO*///
/*TODO*///			int column;
/*TODO*///			for( column=c1; column<=c2; column++ ){
/*TODO*///				if( column==c2 || priority_data[column]!=priority )
/*TODO*///					tile_type = TILE_TRANSPARENT;
/*TODO*///				else
/*TODO*///					tile_type = TILE_OPAQUE;
/*TODO*///
/*TODO*///				if( tile_type!=prev_tile_type ){
/*TODO*///					x_end = column*TILE_WIDTH;
/*TODO*///					if( x_end<x1 ) x_end = x1;
/*TODO*///					if( x_end>x2 ) x_end = x2;
/*TODO*///
/*TODO*///					if( prev_tile_type != TILE_TRANSPARENT ){
/*TODO*///						/* TILE_OPAQUE */
/*TODO*///						int num_pixels = x_end - x_start;
/*TODO*///						DATA_TYPE *dest0 = dest_baseaddr+x_start;
/*TODO*///						UINT8 *pmap0 = priority_bitmap_baseaddr+x_start;
/*TODO*///						const DATA_TYPE *source0 = source_baseaddr+x_start;
/*TODO*///						int i = y;
/*TODO*///						for(;;){
/*TODO*///							memcpy( dest0, source0, num_pixels*sizeof(DATA_TYPE) );
/*TODO*///							memset( pmap0, tilemap_priority_code, num_pixels );
/*TODO*///							if( ++i == y_next ) break;
/*TODO*///
/*TODO*///							dest0 += blit.dest_line_offset;
/*TODO*///							pmap0 += priority_bitmap_line_offset;
/*TODO*///							source0 += blit.source_line_offset;
/*TODO*///						}
/*TODO*///					}
/*TODO*///					x_start = x_end;
/*TODO*///				}
/*TODO*///
/*TODO*///				prev_tile_type = tile_type;
/*TODO*///			}
/*TODO*///
/*TODO*///			if( y_next==y2 ) break; /* we are done! */
/*TODO*///
/*TODO*///			priority_bitmap_baseaddr += priority_bitmap_row_offset;
/*TODO*///			dest_baseaddr = dest_next;
/*TODO*///			source_baseaddr = source_next;
/*TODO*///
/*TODO*///			y = y_next;
/*TODO*///			y_next += TILE_HEIGHT;
/*TODO*///
/*TODO*///			if( y_next>=y2 ){
/*TODO*///				y_next = y2;
/*TODO*///			}
/*TODO*///			else {
/*TODO*///				dest_next += blit.dest_row_offset;
/*TODO*///				source_next += blit.source_row_offset;
/*TODO*///			}
/*TODO*///		} /* process next row */
/*TODO*///	} /* not totally clipped */
/*TODO*///})
/*TODO*///
/*TODO*///#undef TILE_WIDTH
/*TODO*///#undef TILE_HEIGHT
/*TODO*///#undef DATA_TYPE
/*TODO*///#undef memcpybitmask
/*TODO*///#undef DECLARE
/*TODO*///
/*TODO*///#endif /* DECLARE */
/*TODO*///    
}
