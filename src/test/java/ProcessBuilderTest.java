import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * http://examples.javacodegeeks.com/core-java/lang/processbuilder/java-lang-processbuilder-example/
 *
 * @author hugh
 */
public class ProcessBuilderTest {

    String newLine = System.lineSeparator();

    @Test
    public void workingDirectoryTest() throws Exception {
        // The working directory is the root of the current project.
        // In case of a web project running on a tomcat server, the location is `$TOMCAT_HOME/bin`.
        Process ps = new ProcessBuilder("/bin/bash", "./scripts/process-builder-test.sh").start();
        ps.waitFor();
        assertThat(output(ps.getInputStream()), is("hi" + newLine));
    }

    @Test
    public void errorStreamTest() throws Exception {
        Process ps = new ProcessBuilder("/bin/bash", "not-existing-file.sh").start();
        ps.waitFor();

        assertThat(output(ps.getErrorStream()),
                is("/bin/bash: not-existing-file.sh: No such file or directory" + newLine));
    }

    @Test
    public void redirectTest() throws Exception {
        ProcessBuilder pb = new ProcessBuilder("/bin/bash", "not-existing-file.sh");
        pb.redirectError(new File("samples/process-builder-error.log"));
        Process ps = pb.start();
        ps.waitFor();

        assertThat(output(new FileInputStream("samples/process-builder-error.log")),
                is("/bin/bash: not-existing-file.sh: No such file or directory" + newLine));
    }

    @Test
    public void inheritIOTest() throws Exception {
        Process ps = new ProcessBuilder("/bin/bash", "not-existing-file.sh").inheritIO().start();
        ps.waitFor();

        assertThat("No errors from ErrorStream as the IO has inherited to JVM. " +
                        "You should be able to see the error message in the console.",
                output(ps.getErrorStream()), is(""));
    }

    private String output(InputStream inputStream) throws IOException {
        String result = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            int c;
            while ((c = br.read()) != -1) {
                result += String.valueOf((char) c);
            }

            return result;
        }
    }

}
