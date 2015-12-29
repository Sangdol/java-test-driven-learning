import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class RandomTest {

    private Random random = new Random();

    @Test
    public void randomTest() throws Exception {
        assertThat(random.nextLong(), is(notNullValue()));
        assertThat(random.nextInt(10), is(both(greaterThanOrEqualTo(0)).and(lessThan(10))));
    }

}
