import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class ExceptionTest {

    @Test
    public void stackTraceTest() throws Exception {
        Exception e = new Exception();
        e.setStackTrace(Thread.currentThread().getStackTrace());

        try {
            throw e;
        } catch (Exception ex) {
            StackTraceElement[] stes = ex.getStackTrace();

            // http://stackoverflow.com/questions/1149703/how-can-i-convert-a-stack-trace-to-a-string
//            for (StackTraceElement ste : stes) {
//                System.out.println(ste.toString());
//            }

            assertThat(stes.length, is(28));
        }
    }
    
    @Test
    public void causeTest() throws Exception {
        Exception e = new Exception("I'm the cause");
        assertThat(e.getCause(), is(nullValue()));

        try {
            throw e;
        } catch (Exception ex){
            RuntimeException re = new RuntimeException(ex);
            assertThat(re.getCause().getMessage(), is("I'm the cause"));
        }
    }

}
