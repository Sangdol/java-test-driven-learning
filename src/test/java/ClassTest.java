import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author hugh
 */
public class ClassTest {
    @Test
    public void classPathTest() throws Exception {
        String classPath = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(classPath);
    }
}
