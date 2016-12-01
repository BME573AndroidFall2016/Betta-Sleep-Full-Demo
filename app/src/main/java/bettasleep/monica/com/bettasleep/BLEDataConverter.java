package bettasleep.monica.com.bettasleep;

/**
 * Created by Bryce on 11/28/2016.
 */

public class BLEDataConverter {
    /*
     * convert - takes an input array of 20 bytes (actually, can be any
     *  even number of bytes), as returned from the BLE code, and
     *  returns an array of ten floating point numbers.
     */
    public static float[] convert(final byte[] input) {
        //16 unsigned big endian
        float[] output = new float[input.length / 2];
        int[] inputs = new int[input.length];

        for (int i = 0; i < input.length; ++i)
            if (input[i] < 0)
                inputs[i] = 255 - ~input[i];
            else
                inputs[i] = input[i];

        for (int i = 0; i < inputs.length; i += 2)
            output[i / 2] = 256 * inputs[i] + inputs[i + 1];

        return output;
    }
}
