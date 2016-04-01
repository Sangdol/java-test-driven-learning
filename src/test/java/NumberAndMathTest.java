import org.junit.Test;

import java.math.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class NumberAndMathTest {

    @Test
    public void decodeTest() throws Exception {
        assertThat(Integer.decode("0xff"), is(255));
        try {
            Integer.decode("0b10");
        } catch (NumberFormatException e) { }
    }
    
    @Test
    public void castingTest() throws Exception {
        assertThat((long)1.0, is(1L));
        assertThat((long)1.1, is(1L));
    }

    /**
     * Actually it's not underflow
     * https://www.reddit.com/r/programming/comments/3iq9mr/sometimes_a_bug_is_hit_only_after_code_was/
     */
    @Test
    public void overflowTest() {
        assertThat(Integer.MAX_VALUE, is(2_147_483_647));
        assertThat(Integer.MIN_VALUE, is(-2_147_483_648));

        assertThat(Integer.MAX_VALUE, is(0b01111111_11111111_11111111_11111111));
        assertThat(Integer.MIN_VALUE, is(0b10000000_00000000_00000000_00000000));

        assertThat(-1 - Integer.MAX_VALUE, is(Integer.MIN_VALUE));
        assertThat(-1 - Integer.MIN_VALUE, is(Integer.MAX_VALUE));
        assertThat(-2 - Integer.MIN_VALUE, is(-2 & Integer.MAX_VALUE));
        assertThat(Integer.MAX_VALUE + 1, is(Integer.MIN_VALUE));

        assertThat((byte)(Byte.MAX_VALUE + 1), is(Byte.MIN_VALUE));
        assertThat((short)(Short.MAX_VALUE + 1), is(Short.MIN_VALUE));
    }
    
    @Test
    public void doubleRangeTest() throws Exception {
        assertThat(1.0 / 3, is(0.3333333333333333));
        assertThat(1.0 / 7, is(0.14285714285714285));
        assertThat(100000.0 / 7, is(14285.714285714286));
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
        assertThat((double) a / b, is(0.5));
    }

    /**
     * Why does floor and ceil returns double instead of long?
     *
     * http://stackoverflow.com/questions/7287099/why-does-math-ceil-return-a-double
     * http://stackoverflow.com/questions/511921/why-does-math-floor-return-a-double
     * http://stackoverflow.com/questions/3412449/why-does-math-round-return-a-long-but-math-floor-return-a-double
     *
     * - For handling infinity and NAN
     * - The range of double is greater than that of long.
     * - There's double version of round - rint.
     */
    @Test
    public void floorAndCeilAndRintTest() throws Exception {
        assertThat(Math.floor(1.8), is(1.0));
        assertThat(Math.ceil(1.8), is(2.0));
        assertThat(Math.rint(1.8), is(2.0));

        assertThat(Math.floor(-1.8), is(-2.0));
        assertThat(Math.ceil(-1.8), is(-1.0));
        assertThat(Math.rint(-1.8), is(-2.0));
        assertThat(Math.rint(-1.5), is(-2.0));

        assertThat(Math.floor(Double.NEGATIVE_INFINITY), is(Double.NEGATIVE_INFINITY));
        assertThat(Math.floor(Double.NaN), is(Double.NaN));
    }

    @Test
    public void roundTest() throws Exception {
        assertThat(Math.round(1.4), is(1L));
        assertThat(Math.round(1.5), is(2L));
        assertThat(Math.round(1.45), is(1L));
        assertThat(Math.round(-1.5), is(-1L));

        assertThat(round(1.11555, 3), is(1.116));
        assertThat(round(0.1, 18), is(0.1));
        assertThat(round(0.1, 19), is(0.1));
        assertThat(round(0.1, 20), is(0.09223372036854775));

        assertThat(round2(1.11555, 3), is(1.116));
        assertThat(round2(0.1, 18), is(0.1));
        assertThat(round2(0.1, 19), is(0.1));
        assertThat(round2(0.1, 20), is(0.1));

        assertThat((int) 1.5, is(1));
        assertThat((int) -1.5, is(-1));
        assertThat((long) 1.5, is(1L));
        assertThat((long) -1.5, is(-1L));
    }

    /**
     * Super accurate but a bit slow
     * http://stackoverflow.com/a/154354/524588
     */
    private double round2(double n, int scale) {
        return new BigDecimal(String.valueOf(n)).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Not super accurate but fast
     * http://stackoverflow.com/a/153753/524588
     */
    private double round(double n, int scale) {
        double multiplier = Math.pow(10, scale);
        return Math.round(n * multiplier) / multiplier;
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
