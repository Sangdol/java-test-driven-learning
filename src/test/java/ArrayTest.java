import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class ArrayTest {

    /**
     * http://stackoverflow.com/questions/4146928/in-java-is-a-char-an-object/4146957#4146957
     */
    @Test
    public void typeTest() throws Exception {
        Object intArr = new int[]{};
        Object integerArr = new Integer[]{};

        assertThat(intArr.getClass().getName(), is("[I"));
        assertThat(intArr.getClass().getCanonicalName(), is("int[]"));
        assertThat(intArr.getClass().getComponentType().getName(), is("int"));
        assertThat(integerArr.getClass().getName(), is("[Ljava.lang.Integer;"));
        assertThat(integerArr.getClass().getCanonicalName(), is("java.lang.Integer[]"));
        assertThat(integerArr.getClass().getComponentType().getName(), is("java.lang.Integer"));
    }

    /**
     * http://stackoverflow.com/questions/4324633/how-to-convert-an-int-array-to-a-list/4324662#4324662
     */
    @Test
    public void toListTest() throws Exception {
        int[] intArr = {1, 2};
        List<int[]> list = Arrays.asList(intArr);
        assertThat(list.contains(1), is(false));
        assertThat(list.get(0)[0], is(1));

        Integer[] integerArr = {1, 2};
        List<Integer> list2 = Arrays.asList(integerArr);
        assertThat(list2.contains(1), is(true));
    }

    @Test
    public void fillTest() throws Exception {
        int[] arr = new int[2];
        Arrays.fill(arr, 1);
        assertThat(arr[0], is(1));
        assertThat(arr[1], is(1));
    }

    /**
     * http://stackoverflow.com/questions/8546500/why-isnt-there-a-java-lang-array-class-if-a-java-array-is-an-object-shouldnt
     */
    @Test
    public void classTest() throws Exception {
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

        matrix = new int[2][];
        assertThat(matrix[0], is(nullValue()));

        boolean[] bs = new boolean[10];
        assertThat(bs[0], is(false));
    }

    /**
     * http://stackoverflow.com/questions/3078441/getting-compiler-error-while-using-array-constants-in-the-constructor
     */
    @Test
    public void initializerTest() throws Exception {
        int[] arr = {1, 2};
        int[][] matrix = {{1}, {2}};

        assertThat(arr[0], is(1));
        assertThat(matrix[0][0], is(1));

        // Compile error
//        array = {1, 2};
//        matrix = {{1}, {2}};
    }

    @Test(expected = ClassCastException.class)
    public void castingExceptionTest() throws Exception {
        Object[] objects = new Object[]{1, 2};
        Integer[] ints = (Integer[]) objects;
    }

    @Test
    public void castingTest() throws Exception {
        Object[] objects = new Object[]{1, 2};
        Integer[] ints = Arrays.copyOf(objects, objects.length, Integer[].class);
        assertThat(ints[0], is(2));
        assertThat(ints[1], is(2));
    }

    @Test
    public void listToArrayTest() throws Exception {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        Integer[] arr = list.toArray(new Integer[list.size()]);
        assertThat(arr, is(new Integer[]{0, 1}));

        List<Integer[]> arrList = new ArrayList<>();
        arrList.add(new Integer[]{0});
        arrList.add(new Integer[]{1});
        Integer[][] matrix = arrList.toArray(new Integer[arrList.size()][]);
        assertThat(matrix, is(new Integer[][]{new Integer[]{0}, new Integer[]{1}}));
    }
}
