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
public class ScanSum {
    public static void main(String[] args) throws IOException {
        double sum = 0;

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader("samples/usnumbers.txt")))) {
            while (scanner.hasNext()) {
                if (scanner.hasNextDouble()) {
                    sum += scanner.nextDouble();
                } else {
                    scanner.next();
                }
            }
        }

        System.out.println(sum);
    }
}
