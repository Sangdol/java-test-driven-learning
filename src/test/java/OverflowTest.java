import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Actually it's not underflow
 * https://www.reddit.com/r/programming/comments/3iq9mr/sometimes_a_bug_is_hit_only_after_code_was/
 *
 * @author hugh
 */
public class OverflowTest {
    @Test
    public void overflow() {
        assertThat(-1 - Integer.MAX_VALUE, is(lessThan(0)));
        assertThat(-1 - Integer.MIN_VALUE, is(greaterThan(0)));
        assertThat(-2 - Integer.MIN_VALUE, is(-2 & Integer.MAX_VALUE));
        assertThat(-1 - Integer.MIN_VALUE, is(Integer.MAX_VALUE));
        assertThat(Integer.MAX_VALUE + 1, is(Integer.MIN_VALUE));
    }
}
