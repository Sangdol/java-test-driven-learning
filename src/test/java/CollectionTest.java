import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class CollectionTest {

    @Test
    public void toStringTest() throws Exception {
        List<Integer> intList = Arrays.asList(1, 2, 3);
        assertThat(intList.toString(), is("[1, 2, 3]"));

        List<String> strList = Arrays.asList("1", "2");
        assertThat(strList.toString(), is("[1, 2]"));

        strList = Arrays.asList("abc", "def");
        assertThat(strList.toString(), is("[abc, def]"));
    }
    
    /**
     * http://stackoverflow.com/questions/5374311/convert-arrayliststring-to-string-array
     */
    @Test
    public void toArrayTest() throws Exception {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Integer[] arr = list.toArray(new Integer[list.size()]);
        assertThat(arr[0], is(1));

        // http://stackoverflow.com/questions/23079003/how-to-convert-a-java-8-stream-to-an-array
        arr = list.stream().toArray(Integer[]::new);
        assertThat(arr[0], is(1));
    }
    
    @Test
    public void listRemoveTest() throws Exception {
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(2);
        list.add(1);
        list.add(0);
        
        list.remove(new Integer(0));
        assertThat(list.contains(0), is(false));
        assertThat(list.contains(3), is(true));
        
        list.remove(0);
        assertThat(list.contains(3), is(false));
        assertThat(list.get(0), is(2));
    }

    @Test
    public void treeMapTest() throws Exception {
        Map<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(3, "three");
        treeMap.put(1, "one");
        treeMap.put(2, "two");

        List<Integer> list = treeMap.keySet().stream()
                .collect(Collectors.toList());

        assertThat(list, is(Arrays.asList(1, 2, 3)));
    }

    /**
     * http://stackoverflow.com/questions/740299/how-do-i-sort-a-set-to-a-list-in-java
     */
    @Test
    public void setSortingTest() throws Exception {
        Set<String> set = new HashSet<>();
        set.add("1");
        set.add("2");
        set.add("11");

        TreeSet<String> treeSet = new TreeSet<>(set);
        Iterator<String> treeSetIt = treeSet.iterator();
        assertThat(treeSetIt.next(), is("1"));
        assertThat(treeSetIt.next(), is("11"));
        assertThat(treeSetIt.next(), is("2"));

        List<String> list = new ArrayList<>(set);
        Collections.sort(list);
        assertThat(list.get(0), is("1"));
        assertThat(list.get(1), is("11"));
        assertThat(list.get(2), is("2"));
    }
    
    @Test
    public void setIterationTest() throws Exception {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);

        for (Integer i : set) {
            assertThat(i, is(greaterThan(0)));
        }
    }

    @Test
    public void setTest() throws Exception {
        Set<Integer> set = new HashSet<>();

        assertThat(set.add(1), is(true));
        assertThat(set.add(1), is(false));
        assertThat(set.contains(1), is(true));
        assertThat(set.contains(2), is(false));
    }

    @Test
    public void mapSortTest() throws Exception {
        Map<Integer, String> map = new HashMap<>();
        map.put(2, "two");
        map.put(1, "one");
        map.put(3, "three");

        List<Map.Entry<Integer, String>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (e1, e2) -> e1.getKey().compareTo(e2.getKey()));
        Collections.sort(list, Map.Entry.comparingByKey());

        assertThat(list.get(0).getKey(), is(1));
        assertThat(list.get(1).getKey(), is(2));
        assertThat(list.get(2).getKey(), is(3));
    }

    @Test
    public void sortTest() throws Exception {
        List<Integer> list = Arrays.asList(2, 1, 3);
        Collections.sort(list);
        assertThat(list.get(0), is(1));
        assertThat(list.get(2), is(3));

        Collections.sort(list, Comparator.reverseOrder());
        assertThat(list.get(0), is(3));
        assertThat(list.get(2), is(1));

        List<String> strList = Arrays.asList("1", "2", "11");
        Collections.sort(strList);
        assertThat(strList.get(0), is("1"));
        assertThat(strList.get(2), is("2"));

        Collections.sort(strList, Comparator.naturalOrder());
        assertThat(strList.get(0), is("1"));
        assertThat(strList.get(2), is("2"));
    }

    @Test
    public void mapEntrySetTest() throws Exception {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");

        Set<Map.Entry<Integer, String>> set = map.entrySet();
        Map.Entry<Integer, String> firstItem = set.iterator().next();
        firstItem.setValue("1");
        
        assertThat(map.get(1), is("1"));
    }

    @Test
    public void mapCreationTest() throws Exception {
        Map<Integer, String> map = Collections.singletonMap(1, "one");

        assertThat(map.get(1), is("one"));
    }
    
    @Test
    public void replaceTest() throws Exception {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");

        assertThat(map.replace(1, "1"), is("one"));
        assertThat(map.replace(2, "2"), is(nullValue()));

        assertThat(map.replace(1, "1", "!"), is(true));
        assertThat(map.replace(1, "1", "!"), is(false));

        map.replaceAll((key, value) -> key + value);
        assertThat(map.get(1), is("1!"));
    }

    @Test
    public void mapIterationTest() throws Exception {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            assertThat(entry.getKey(), is(greaterThan(0)));
            assertThat(entry.getValue().length(), is(3));
        }
    }

    @Test
    public void mapPutTest() throws Exception {
        Map<Integer, String> map = new HashMap<>();

        assertThat(map.put(1, "one"), is(nullValue()));
        assertThat(map.put(1, "1"), is("one"));
        
        assertThat(map.putIfAbsent(1, "!"), is("1"));
        assertThat(map.get(1), is("1"));

        Map<Integer, String> map2 = new HashMap<>();
        map2.put(1, "one");
        map2.put(2, "two");

        map.putAll(map2);
        assertThat(map.size(), is(2));
        assertThat(map.get(1), is("one"));
    }
    
    @Test
    public void mapGetTest() throws Exception {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");

        assertThat(map.get(1), is("one"));
        assertThat(map.get(3), is(nullValue()));
        assertThat(map.getOrDefault(3, "three"), is("three"));
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
