import org.junit.Test;

import java.io.*;
import java.util.Scanner;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class SystemTest {
    
    @Test
    public void nanoTimeTest() throws Exception {
        assertThat(System.nanoTime(), is(greaterThan(824_674_741_842_607L)));
        assertThat(System.nanoTime() / 100_000, is(greaterThan(Long.valueOf(Integer.MAX_VALUE))));
        assertThat(System.nanoTime() / 1_000_000, is(lessThan(Long.valueOf(Integer.MAX_VALUE))));
    }

    /**
     * http://stackoverflow.com/questions/13329282/test-java-programs-that-read-from-stdin-and-write-to-stdout
     * http://stackoverflow.com/questions/1647907/junit-how-to-simulate-system-in-testing
     */
    @Test
    public void systemInOutReplaceTest() throws Exception {
        // Set In
        String hi = "hi";
        System.setIn(new ByteArrayInputStream(hi.getBytes()));
        Scanner scanner = new Scanner(System.in);

        // Set Out
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // Test
        System.out.print(scanner.next());
        assertThat(outputStream.toString(), is("hi"));
    }
}
