import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class ArrayTest {

    /**
     * http://stackoverflow.com/questions/8546500/why-isnt-there-a-java-lang-array-class-if-a-java-array-is-an-object-shouldnt
     */
    @Test
    public void arrayClassTest() throws Exception {
        Integer a = 1;
        assertThat(a.getClass().toString(), is("class java.lang.Integer"));
        assertThat(a.getClass().getSimpleName(), is("Integer"));

        int[] arr = new int[10];
        assertThat(arr.getClass().toString(), is("class [I"));
        assertThat(arr.getClass().getSimpleName(), is("int[]"));
    }

    @Test
    public void initializationTest() throws Exception {
        int[] arr = new int[10];
        assertThat(arr[0], is(0));

        arr = new int[]{1, 2, 3};
        assertThat(arr[0], is(1));

        int[][] matrix = new int[10][10];
        assertThat(matrix[0][0], is(0));

        matrix = new int[][]{{1, 2}, {10, 20}};
        assertThat(matrix[0][0], is(1));
        assertThat(matrix[1][1], is(20));
    }

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
