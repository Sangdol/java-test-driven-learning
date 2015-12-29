import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collection;
import java.util.function.Predicate;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * https://docs.oracle.com/javase/tutorial/java/generics/QandE/generics-questions.html
 * @author hugh
 */
public class GenericExercisesTest {

    /**
     * 1. Write a generic method to count the number of elements in a collection
     *   that have a specific property (for example, odd integers, prime numbers, palindromes)
     */
    @Test
    public void countIfTest() throws Exception {
        assertThat(countIf(Lists.newArrayList(1, 2, 3), n -> n % 2 == 0), is(1));
    }

    private <T> int countIf(Collection<T> coll, Predicate<T> pred) {
        return (int) coll.stream()
                .filter(pred)
                .count();
    }

    /**
     * no.3
     */
    @Test
    public void swapArrayTest() throws Exception {
        Integer[] intArr = new Integer[]{1, 2};
        swapArr(intArr, 0, 1);
        assertThat(intArr, is(new Integer[]{2, 1}));

        String[] strArr = new String[]{"a", "b"};
        swapArr(strArr, 0, 1);
        assertThat(strArr, is(new String[]{"b", "a"}));
    }

    private <T> void swapArr(T[] arr, int x, int y) {
        T temp = arr[x];
        arr[x] = arr[y];
        arr[y] = temp;
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void findRelativelyPrimeTest() throws Exception {

    }
}
