import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class NumberAndMathTest {

    /**
     * Actually it's not underflow
     * https://www.reddit.com/r/programming/comments/3iq9mr/sometimes_a_bug_is_hit_only_after_code_was/
     */
    @Test
    public void overflow() {
        assertThat(-1 - Integer.MAX_VALUE, is(lessThan(0)));
        assertThat(-1 - Integer.MIN_VALUE, is(greaterThan(0)));
        assertThat(-2 - Integer.MIN_VALUE, is(-2 & Integer.MAX_VALUE));
        assertThat(-1 - Integer.MIN_VALUE, is(Integer.MAX_VALUE));
        assertThat(Integer.MAX_VALUE + 1, is(Integer.MIN_VALUE));
    }

    @Test
    public void intAndDoubleDivisionTest() throws Exception {
        int a = 1;
        int b = 2;
        double da = a;
        double db = b;
        
        assertThat(a / b, is(0));
        assertThat(da / b, is(0.5));
        assertThat(a / db, is(0.5));
        assertThat(da / db, is(0.5));
    }

    @Test
    public void roundTest() throws Exception {
        assertThat(Math.round(1.4), is(1L));
        assertThat(Math.round(1.5), is(2L));
        assertThat(Math.round(1.45), is(1L));
        assertThat(Math.round(-1.5), is(-1L));
    }

    @Test
    public void intRangeTest() throws Exception {
        assertThat(Integer.MAX_VALUE, is(2147483647));
        assertThat(Integer.MIN_VALUE, is(-2147483648));
    }

    @Test
    public void atomicIntegerTest() throws Exception {
        AtomicInteger ai = new AtomicInteger(2147483647);
        assertThat(ai.get(), is(2147483647));
    }
    
    @Test
    public void bigIntegerHexTest() throws Exception {
        assertThat(new BigInteger(1, new byte[]{1}), is(BigInteger.valueOf(1)));
        assertThat(new BigInteger(1, new byte[]{1, 0}), is(BigInteger.valueOf(256)));
        assertThat(new BigInteger(1, new byte[]{1, 0, 0}), is(BigInteger.valueOf(65536)));
        assertThat(new BigInteger(1, new byte[]{4, 0}), is(BigInteger.valueOf(1024)));
    }
    
    @Test
    public void toHexStringTest() throws Exception {
        assertThat(Integer.toHexString(9), is("9"));
        assertThat(Integer.toHexString(10), is("a"));
        assertThat(Integer.toHexString(15), is("f"));
        assertThat(Integer.toHexString(16), is("10"));
        assertThat(Integer.toHexString(255), is("ff"));

        assertThat(Long.toHexString(255L), is("ff"));
    }

}
