package ioStreams;

import java.io.*;

/**
 * @author hugh
 */
public class CopyLines {
    public static void main(String[] args) throws IOException {

        // try with resources
        // https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (
            BufferedReader inputStream = new BufferedReader(new FileReader("samples/xanadu.txt"));
            PrintWriter outputStream = new PrintWriter("samples/lineoutput.txt")) {

            String l;
            while ((l = inputStream.readLine()) != null) {
                outputStream.println(l);
            }
        }

    }
}
