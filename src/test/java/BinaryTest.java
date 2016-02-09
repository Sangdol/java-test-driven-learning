import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class BinaryTest {
    
    @Test
    public void xorTest() throws Exception {
        assertThat(2 ^ 1, is(3));
        assertThat(2 ^ 1, is(0b11));
    }

    @Test
    public void shiftTest() throws Exception {
        assertThat(1 << 1, is(2));
        assertThat(2 << 1, is(4));
        assertThat(3 << 1, is(6));

        assertThat(1 >> 1, is(0));
        assertThat(2 >> 1, is(1));
        assertThat(3 >> 1, is(1));
        assertThat(4 >> 1, is(2));

        assertThat(-1 >>> 1, is((int)Math.pow(2, 31)));
        assertThat((1L << 1), is((long)Math.pow(2, 1)));
        assertThat((1L << 31), is((long)Math.pow(2, 31)));

        assertThat(Integer.MAX_VALUE, is((int)((1L << 31) - 1)));
        assertThat(bits(Integer.MIN_VALUE), is("10000000_00000000_00000000_00000000"));
        assertThat(bits(Integer.MIN_VALUE >> 1), is("11000000_00000000_00000000_00000000"));
        assertThat(bits(Integer.MIN_VALUE >>> 1), is("01000000_00000000_00000000_00000000"));
        assertThat(bits(1 << 30), is("01000000_00000000_00000000_00000000"));
        assertThat(bits((int)Math.pow(2, 30)), is("01000000_00000000_00000000_00000000"));
        assertThat(Integer.MIN_VALUE >>> 1, is((int)Math.pow(2, 30)));
    }

    /**
     * Binary Literal
     * - https://docs.oracle.com/javase/8/docs/technotes/guides/language/binary-literals.html
     */
    @Test
    public void binaryTest() throws Exception {
        assertThat(1, is(0b1));
        assertThat(2, is(0b10));

        assertThat(bits(-1), is("11111111_11111111_11111111_11111111"));
        assertThat(bits(-2), is("11111111_11111111_11111111_11111110"));

        assertThat(Integer.toBinaryString(1), is("1"));
        assertThat(Integer.toBinaryString(2), is("10"));
        assertThat(Integer.toString(1, 2), is("1"));
        assertThat(Integer.toString(2, 2), is("10"));
        assertThat(bits(Integer.MIN_VALUE).replaceAll("_", ""), is(Integer.toBinaryString(Integer.MIN_VALUE)));
    }

    private String bits(int n) {
        String bits = "";
        for (int i = 0; i < 32; i++) {
            bits += ((1L << (31 - i)) & n) == 0 ? 0 : 1;
            if (i % 8 == 7 && i != 31) {
                bits += "_";
            }
        }
        return bits;
    }

}
