import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class SystemTest {
    
    @Test
    public void nanoTimeTest() throws Exception {
        assertThat(System.nanoTime(), is(greaterThan(824_674_741_842_607L)));
        assertThat(System.nanoTime() / 100_000, is(greaterThan(Long.valueOf(Integer.MAX_VALUE))));
        assertThat(System.nanoTime() / 1_000_000, is(lessThan(Long.valueOf(Integer.MAX_VALUE))));
    }
}
