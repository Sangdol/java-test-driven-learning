import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class NumberTest {

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
