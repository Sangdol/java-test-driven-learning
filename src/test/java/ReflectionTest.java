import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import java.lang.reflect.Method;

/**
 * @author hugh
 */
public class ReflectionTest {

    /**
     * http://stackoverflow.com/questions/20192552/get-value-of-a-parameter-of-an-annotation-in-java
     */
    @Test
    public void annotationTest() throws Exception {
        Method method = ReflectionTest.class.getMethod("annotationTest");
        Test test = method.getAnnotation(Test.class);
        assertThat(test.timeout(), is(0L));
    }

    @Test
    public void getMethodTest() throws Exception {
        Method concat = String.class.getMethod("concat", String.class);
        String abcd = (String) concat.invoke("ab", "cd");
        assertThat(abcd, is("abcd"));
    }
}
