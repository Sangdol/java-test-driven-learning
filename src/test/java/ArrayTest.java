import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class ArrayTest {

    @Test(expected = ClassCastException.class)
    public void arrayCastingExceptionTest() throws Exception {
        Object[] objects = new Object[]{1, 2};
        Integer[] ints = (Integer[]) objects;
    }

    @Test
    public void arrayCastingTest() throws Exception {
        Object[] objects = new Object[]{1, 2};
        Integer[] ints = Arrays.copyOf(objects, objects.length, Integer[].class);
        assertThat(ints[0], is(2));
        assertThat(ints[1], is(2));
    }
}
