import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import java.lang.reflect.Method;

/**
 * @author hugh
 */
public class ReflectionTest {

    @Test
    public void getMethodTest() throws Exception {
        Method concat = String.class.getMethod("concat", String.class);
        String abcd = (String) concat.invoke("ab", "cd");
        assertThat(abcd, is("abcd"));
    }
}
