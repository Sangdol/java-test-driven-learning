import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author hugh
 */
public class SetTest {
    @Test
    public void should() {
        Set<Integer> set = new HashSet<>();
        set.add(1);

        for (int i : set) {
            System.out.println(i);
        }
    }
}
