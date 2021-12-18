package arcadeflex.common.libc;

import gr.codebb.arcadeflex.common.PtrLib.ShortPtr;
import gr.codebb.arcadeflex.common.PtrLib.UBytePtr;

/**
 *
 * @author George Moralis
 */
public class cstring {

    /**
     * memset
     */
    public static void memset(ShortPtr buf, int value, int size) {
        for (int i = 0; i < size; i++) {
            buf.write(i, (short) value);
        }
    }

    /**
     * memcpy
     */
    public static void memcpy(UBytePtr dst, char[] src, int size) {
        for (int i = 0; i < Math.min(size, src.length); i++) {
            dst.write(i, src[i]);
        }
    }

    public static void memcpy(char[] dst, UBytePtr src, int size) {
        for (int i = 0; i < Math.min(size, src.memory.length); i++) {
            dst[i] = src.read(i);
        }
    }
}
