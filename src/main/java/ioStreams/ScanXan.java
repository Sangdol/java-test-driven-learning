package ioStreams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * https://docs.oracle.com/javase/tutorial/essential/io/scanning.html
 *
 * @author hugh
 */
public class ScanXan {
    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader("samples/xanadu.txt")))) {
            while (scanner.hasNext()) {
//                scanner.useDelimiter(",\\s*");
//                scanner.useDelimiter("\r");
                System.out.print(scanner.next());
            }
        }
    }
}
