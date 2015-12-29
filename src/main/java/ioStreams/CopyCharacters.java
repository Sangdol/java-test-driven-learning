package ioStreams;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * https://docs.oracle.com/javase/tutorial/essential/io/charstreams.html
 */
public class CopyCharacters {
    public static void main(String[] args) throws IOException {

        FileReader inputStream = null;
        FileWriter outputStream = null;

        try {
            inputStream = new FileReader("samples/xanadu.txt");
            outputStream = new FileWriter("samples/characteroutput.txt");

            int c;
            // c holds a character value in its last 16 bits.
            while ((c = inputStream.read()) != -1) {
                outputStream.write(c);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
