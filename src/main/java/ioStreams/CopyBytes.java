package ioStreams;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * https://docs.oracle.com/javase/tutorial/essential/io/streams.html
 */
public class CopyBytes {
    public static void main(String[] args) throws IOException {

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            // Since this is a text file, the best approach for this is to use character streams.
            in = new FileInputStream("samples/xanadu.txt");
            out = new FileOutputStream("samples/outagain.txt");
            int c;

            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } finally {
            // Always close streams to avoid serious resource leaks.
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
