import org.junit.Test;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author hugh
 */
public class DataStructureTest {

    @Test
    public void sortedSetTest() throws Exception {
        SortedSet<Integer> sortedInts = new TreeSet<>();
        sortedInts.add(1);
        sortedInts.add(3);
        sortedInts.add(2);

        sortedInts.forEach(System.out::println);
    }
}
