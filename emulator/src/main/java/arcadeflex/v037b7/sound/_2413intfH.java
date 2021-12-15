/*
 * ported to v0.37b7
 *
 */
package arcadeflex.v037b7.sound;

import static arcadeflex.v037b7.sound._3812intfH.MAX_3812;
import arcadeflex.v037b7.sound._3812intfH.YM3812interface;
import static arcadeflex.v037b7.generic.fucPtr.*;

public class _2413intfH {

    public static final int MAX_2413 = MAX_3812;

    public static class YM2413interface extends YM3812interface {

        public YM2413interface(int num, int baseclock, int[] mixing_level, WriteYmHandlerPtr[] handler) {
            super(num, baseclock, mixing_level, handler);
        }

        public YM2413interface(int num, int baseclock, int[] mixing_level) {
            super(num, baseclock, mixing_level);
        }
    }
}
