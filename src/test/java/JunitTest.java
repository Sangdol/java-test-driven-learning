import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author hugh
 */
public class JunitTest {

    /**
     * http://stackoverflow.com/a/2935935/524588
     *
     * This should be public.
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void exceptionTest() throws Exception {
        exception.expect(Exception.class);
        throw new Exception();
    }

}
