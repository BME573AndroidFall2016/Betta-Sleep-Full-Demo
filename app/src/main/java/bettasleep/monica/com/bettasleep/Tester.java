package bettasleep.monica.com.bettasleep;

/**
 * Created by Bryce on 11/28/2016.
 */

import java.io.Console;

public class Tester {
    public static void main(String[] args) {
        Console c = System.console();
        System.out.println("Enter 40 hexidecimal characters:");

        String data = c.readLine("> ");

        System.out.println("Data read: '" + data + "'");
        int j = 0;
        byte cur = 0;
        byte input_bytes[] = new byte[20];

        for (int i = 0; i < data.length(); i += 2) {
            input_bytes[j++] = (byte) Integer.parseInt(data.substring(i, i + 2), 16);
            //System.out.println("Read: '"+data.substring(i,i+2)+"'");
        }

        BLEDataConverter converter = new BLEDataConverter();
        float output[] = converter.convert(input_bytes);
        //for (int k = 0; k < input_bytes.length; ++k)
        //  System.out.println("Byte "+k+": " + input_bytes[k]);
        for (int h = 0; h < output.length; ++h)
            System.out.println("Data " + h + ": " + output[h]);

    }
}