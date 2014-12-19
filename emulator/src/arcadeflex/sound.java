package arcadeflex;

import static mame.mame.*;
import static sound.mixer.*;

public class sound {

    static int master_volume = 256;

    static int stream_playing;
    static short[] stream_cache_data;
    static int stream_cache_len;
    static boolean stream_cache_stereo;

    static final int NUM_BUFFERS = 3;	/* raising this number should improve performance with frameskip, */
    /* but also increases the latency. */

    static int voice_pos;
    static int audio_buffer_length;

    /* global sample tracking */
    static double samples_per_frame;
    static double samples_left_over;
    static int snd_samples_this_frame;

    static SoundPlayer soundInstance;
    static int stream_buffer_in, stream_buffer_size;
    static int nBlockAlign;

    public static int osd_start_audio_stream(boolean stereo) {
        stream_cache_stereo = stereo;
        soundInstance = new SoundPlayer(Machine.sample_rate, stereo, (int) Machine.drv.frames_per_second);
        stream_buffer_size = soundInstance.GetStreamBufferSize();
        nBlockAlign = 16 * (stereo ? 2 : 1) / 8;
        /* determine the number of samples per frame */
        samples_per_frame = (double) Machine.sample_rate / (double) Machine.drv.frames_per_second;

        /* compute how many samples to generate this frame */
        samples_left_over = samples_per_frame;
        snd_samples_this_frame = (/*uint32*/int) samples_left_over;
        samples_left_over -= (double) snd_samples_this_frame;

        if (Machine.sample_rate == 0) {
            return 0;
        }
        stream_playing = 1;
        voice_pos = 0;

        audio_buffer_length = 1940;//soundInstance.GetSampleSizeInBytes(TimeSpan.FromMilliseconds(22));

        return (int) samples_this_frame;
    }

    public static void osd_stop_audio_stream() {
        if (Machine.sample_rate == 0) {
            return;
        }

        soundInstance.Stop();
        stream_playing = 0;
    }

    public static int osd_update_audio_stream(short[] buffer) {
        // adjust the input pointer
        stream_buffer_in = (stream_buffer_in + samples_this_frame) % stream_buffer_size;
        int bytes_to_copy = (int) (samples_this_frame * nBlockAlign);
        for (int i = 0; i < bytes_to_copy / 2; i++) {
            soundInstance.WriteSample(i * 2, (short) ((buffer[i] * master_volume / 256)));
        }

        if (bytes_to_copy > 0) {
            soundInstance.SubmitBuffer(0, bytes_to_copy);
        }
        // adjust for the number of bytes
        bytes_to_copy -= bytes_to_copy;

        // copy the second chunk
        if (bytes_to_copy != 0) {
            for (int i = 0; i < bytes_to_copy / 2; i++) {
                soundInstance.WriteSample(i * 2, (short) ((buffer[i] * master_volume / 256)));
            }

            if (bytes_to_copy > 0) {
                soundInstance.SubmitBuffer(0, bytes_to_copy);
            }
        }

        stream_cache_data = buffer;
        stream_cache_len = (int) samples_this_frame;

        /* compute how many samples to generate next frame */
        samples_left_over += samples_per_frame;
        samples_this_frame = /*(uint)*/ (int) samples_left_over;
        samples_left_over -= (double) samples_this_frame;

        return (int) samples_this_frame;
    }

    public static int update_audio() {
        if (Machine.sample_rate == 0 || stream_cache_data == null) {
            return 0;
        }

        updateaudiostream();
        return 1;
    }
    static int bytesCopiedToStream = 0;

    static void updateaudiostream() {
        bytesCopiedToStream = 0;
        short[] data = stream_cache_data;
        boolean stereo = stream_cache_stereo;
        int len = stream_cache_len;
        int buflen;
        int start, end;

        if (stream_playing == 0) {
            return;	/* error */
        }

        buflen = audio_buffer_length;
        start = voice_pos;
        end = voice_pos + len;
        if (end > buflen) {
            end -= buflen; // IS this intended to wrap-around ?
        }

        if (stereo) {
            return;//xxx no steresound for now

            /*int p = start;
            int bi = 0;
            int di = 0;
            while (p != end) {
                if (p >= buflen) {
                    p -= buflen;
                }
                soundInstance.WriteSample(2 * p, (short) ((data[di++] * master_volume / 256)));
                soundInstance.WriteSample(2 * p + 1, (short) ((data[di++] * master_volume / 256)));
                p += 2;
                bytesCopiedToStream += 4;
            }*/

        } else {
            int p = start;
            int bi = 0;
            while (p != end) {
                if (p >= buflen) {
                    p -= buflen;
                }
                soundInstance.WriteSample(bi, (short) ((data[p++] * master_volume / 256)));
                bytesCopiedToStream += 2;
                bi += 2;
            }
        }

        voice_pos = end;
        if (voice_pos == buflen) {
            voice_pos = 0;
        }
    }

}
