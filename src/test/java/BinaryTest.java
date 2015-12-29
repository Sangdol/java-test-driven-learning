import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class BinaryTest {
    @Test
    public void shiftTest() throws Exception {
        assertThat(1 << 1, is(2));
        assertThat(2 << 1, is(4));
        assertThat(3 << 1, is(6));
        
        assertThat(1 >> 1, is(0));
        assertThat(2 >> 1, is(1));
        assertThat(3 >> 1, is(1));
        assertThat(4 >> 1, is(2));
    }
}
