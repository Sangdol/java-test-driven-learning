import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
/**
 * https://github.com/google/guava/wiki
 *
 * @author hugh
 */
public class GuavaTest {

    /**
     * https://github.com/google/guava/wiki/StringsExplained#caseformat
     */
    @Test
    public void caseFormatTest() throws Exception {
        assertThat(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "helloWorld"), is("hello-world"));
    }

    @Test
    public void joinerTest() throws Exception {
        assertThat(Joiner.on(",").join(Lists.newArrayList(1, 2)), is("1,2"));
    }

    @Test(expected = NullPointerException.class)
    public void joinNPETest() throws Exception {
        assertThat(Joiner.on(",").join(Lists.newArrayList(1, null)), is("1"));
    }

    @Test
    public void initializingListsTest() throws Exception {
        List<Integer> ints = Lists.newArrayList(1, 2, 3);
        assertThat(ints.toString(), is("[1, 2, 3]"));
        assertThat(ints, is(Arrays.asList(1, 2, 3)));
    }

    /**
     * http://stackoverflow.com/questions/9489384/initializing-a-guava-immutablemap
     * http://stackoverflow.com/questions/12218345/the-best-way-to-static-initialization-of-maps-in-google-collections
     */
    @Test
    public void initializingMapsTest() throws Exception {
        ImmutableMap<Integer, String> immutableMap = ImmutableMap.<Integer, String>builder()
                .put(1, "one")
                .put(2, "two")
                .build();
        assertThat(immutableMap.size(), is(2));

        immutableMap = ImmutableMap.of(1, "one", 2, "two");
        assertThat(immutableMap.size(), is(2));

        Map<Integer, String> map = Maps.newHashMap(immutableMap);
        assertThat(map.size(), is(2));
        
        assertThat(map.get(1), is("one"));
    }

    @Test
    public void setsTest() throws Exception {
        Set<Integer> setA = Sets.newHashSet(1, 2, 3);
        Set<Integer> setB = Sets.newHashSet(3, 4, 5);

        assertThat(Sets.difference(setA, setB), is(Sets.newHashSet(1, 2)));
        assertThat(Sets.symmetricDifference(setA, setB), is(Sets.newHashSet(1, 2, 4, 5)));

        // Be carefule about the types of elements.
        assertThat(Sets.difference(Sets.newHashSet(1, 2), Sets.newHashSet(2L, 3L)), is(Sets.newHashSet(1, 2)));
    }
}
