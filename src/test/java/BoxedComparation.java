import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author hugh
 */
public class BoxedComparation {
    @Test
    public void boxedLong() {
        // http://stackoverflow.com/questions/1514910/when-comparing-two-integers-in-java-does-auto-unboxing-occur
        Long a = 720l;
        long b = 720l;

        assertTrue(a == b);
    }
}
