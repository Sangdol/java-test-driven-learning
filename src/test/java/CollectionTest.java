import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class CollectionTest {
    
    @Test
    public void mapCreation() throws Exception {
        Map<Integer, String> map = Collections.singletonMap(1, "one");

        assertThat(map.get(1), is("one"));
    }
    
    @Test
    public void mapTest() throws Exception {
        Map<Integer, String> immutableMap = ImmutableMap.of(1, "one", 2, "two");

        assertThat(immutableMap.get(1), is("one"));
        assertThat(immutableMap.get(3), is(nullValue()));
        assertThat(immutableMap.getOrDefault(3, "three"), is("three"));
    }

    @Test
    public void mapComputeTest() throws Exception {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        assertThat(map.compute(2, (k, v) -> k + v), is("2two"));
        assertThat(map.get(2), is("2two"));
        assertThat(map.computeIfAbsent(3, String::valueOf), is("3"));
    }
    
}
