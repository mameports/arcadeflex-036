package arcadeflex.common.libc;

import gr.codebb.arcadeflex.common.PtrLib.ShortPtr;

/**
 *
 * @author George Moralis
 */
public class cstring {
    
    public static void memset(ShortPtr buf, int value, int size) {
        for (int i = 0; i < size; i++) {
            buf.write(i, (short) value);
        }
    }
}
