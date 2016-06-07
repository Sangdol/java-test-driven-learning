package Java8;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.ToString;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * References
 * - https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
 * - https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html
 *
 * Stream operations and pipelines
 * - operations are
 *   - divided into intermediate and terminal
 *   - combined to form stream pipelines
 * - Stream pipelines consists of
 *   - a source e.g. Collections, array
 *   - operations e.g. filter or map
 *   - terminal operation(e e.g. forEach, reduce
 * - Intermediate operations
 *   - return a new stream
 *   - lazy
 *   - does not begin until the terminal operation is executed
 * - Terminal operations
 *   - produce a result or a side-effect
 *   - the stream pipeline cannot be used after it's performed
 * - Laziness
 *   - allow significant efficiencies
 *     - operations can be fused
 *     - allows avoiding examining all data
 * - Intermediate operations are further divided into
 *   - stateless
 *     - filter, map
 *     - can be processed independently, in a single pass
 *   - stateful
 *     - distinct, sorted
 *     - may need to process the entire input
 *     - under parallel computation, some pipelines may need to buffer significant data
 *
 * @author hugh
 */
public class LambdaAndStreamTest {

    List<Person> roster = new ArrayList<>();

    @Getter
    @AllArgsConstructor
    @ToString
    public static class Person {
        public enum Sex {
            MALE, FEMALE
        }

        final public long id;
        final public String name;
        final public int age;
        final public LocalDate birthDay;
        final public Sex gender;
        final public List<String> hobbies;

        public void printPerson() {
            System.out.printf(this.name);
        }
    }

    public static void printPersons(List<Person> roster, Predicate<Person> tester) {
        for (Person p : roster) {
            if (tester.test(p)) {
                p.printPerson();
            }
        }
    }

    @Before
    public void setup() {
        roster.add(new Person(1l, "SH", 33, LocalDate.of(1983, 5, 28), Person.Sex.MALE, Arrays.asList("Piano", "Climbing")));
        roster.add(new Person(2l, "HJ", 33, LocalDate.of(1983, 6, 28), Person.Sex.FEMALE, Arrays.asList("Ballet", "Cooking")));
    }


    @Test
    public void printPersonsWithPredicate() throws Exception {
        printPersons(roster, p -> p.birthDay.isBefore(LocalDate.of(1983, 6, 1)));
    }

    public static void processPersons(List<Person> roster, Predicate<Person> tester, Consumer<Person> block) {
        for (Person p : roster) {
            if (tester.test(p)) {
                block.accept(p);
            }
        }
    }

    @Test
    public void processPersonsWithPredicateAndConsumer() throws Exception {
        processPersons(roster,
                p -> p.birthDay.isBefore(LocalDate.of(1983, 6, 1)),
                p -> p.printPerson());
    }

    public static void processPersonsWithFunction(
            List<Person> roster,
            Predicate<Person> tester,
            Function<Person, String> mapper,
            Consumer<String> block) {

        for (Person p : roster) {
            if (tester.test(p)) {
                String birthDay = mapper.apply(p);
                block.accept(birthDay);
            }
        }
    }

    @Test
    public void processPersonsWithFunctionTest() throws Exception {
        processPersonsWithFunction(roster,
                p -> p.birthDay.isBefore(LocalDate.of(1983, 6, 1)),
                p -> p.birthDay.toString(),
                System.out::println
        );
    }

    @Test
    public void aggregateOperationsTest() throws Exception {
        roster.stream()
                .filter(p -> p.birthDay.isBefore(LocalDate.of(1983, 6, 1)))
                .map(p -> p.birthDay.toString())
                .forEach(System.out::println);
    }

    /**
     * http://www.adam-bien.com/roller/abien/entry/java_8_flatmap_example
     *
     * [[a, b], [c, d]] -> [a, b, c, d]
     */
    @Test
    public void flatMapTest() throws Exception {
        List<List<String>> hobbiesList = roster.stream()
                .map(p -> p.hobbies)
                .collect(Collectors.toList());

        assertThat(hobbiesList.size(), is(2));
        assertThat(hobbiesList.get(0).toString(), is("[Piano, Climbing]"));

        List<String> hobbies= roster.stream()
                .map(p -> p.hobbies)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        
        assertThat(hobbies.size(), is(4));
        assertThat(hobbies.get(0), is("Piano"));

        List<Set<Integer>> setList = new ArrayList<>();
        Set<Integer> s0 = new HashSet<>(); s0.add(0);
        Set<Integer> s1 = new HashSet<>(); s1.add(1);
        setList.add(s0); setList.add(s1);

        List<String> setFlatMapList = setList.stream()
                .flatMap(Collection::stream)
                .map(String::valueOf)
                .collect(Collectors.toList());

        assertThat(setFlatMapList.get(0), is("0"));
        assertThat(setFlatMapList.get(1), is("1"));
    }

    /**
     * http://stackoverflow.com/questions/14830313/retrieving-a-list-from-a-java-util-stream-stream-in-java8
     */
    @Test
    public void mapToListTest() throws Exception {
        List<String> names = roster.stream()
                .map(p -> p.name)
                .collect(Collectors.toList());

        System.out.println(names);
    }

    @Test
    public void stringJoinTest() throws Exception {
        String ages = roster.stream()
                .filter(p -> p.age > 30)
                .map(p -> String.valueOf(p.age))
                .collect(joining(","));

        System.out.println(ages);
    }

    /**
     * Optional#orElse() and Optional#orElseThrow() take Supplier as an argument.
     */
    @Test
    public void supplierTest() throws Exception {
        Stream.generate(() -> "foo")
                .limit(10)
                .forEach(System.out::println);
    }

    @Test
    public void mapToIntTest() throws Exception {
        assertThat(roster.stream().mapToInt(p -> p.age).sum(), is(66));
        assertThat(roster.stream().mapToInt(p -> p.age).average().getAsDouble(), is(33D));
    }

    @Test
    public void arrayStreamTest() throws Exception {
        assertThat(Arrays.stream(new int[] {1, 2, 3}).sum(), is(6));
    }

    @Test
    public void streamOfTest() throws Exception {
        assertThat(Stream.of(1, 2, 3).findFirst().get(), is(1));
    }

    /**
     * http://www.java2s.com/Tutorials/Java_Streams/Tutorial/Streams/Stream_iterate_.htm
     */
    @Test
    public void iterateTest() throws Exception {
        assertThat(Arrays.toString(Stream.iterate('a', ch -> (char)(ch + 1)).limit(2).toArray()), is("[a, b]"));
    }

    @Test
    public void randomTest() throws Exception {
        Random random = new Random();
        assertThat(random.ints(1, 5).limit(1).findFirst().getAsInt(),
                is(both(greaterThan(0)).and(lessThan(5))));
    }

    @Test
    public void iteratorTest() throws Exception {
        roster.iterator().forEachRemaining(System.out::println);
    }

    /**
     * Should I use a parallel stream?
     * - http://stackoverflow.com/questions/20375176/should-i-always-use-a-parallel-stream-when-possible
     * - a parallel stream has a much higher overhead as it coordinates the thread tasks
     * - consider it when you have
     *   - a massive amount of items to process
     *   - a performance problem in the first place
     */
    @Test
    public void parallelTest() throws Exception {
        assertThat(roster.parallelStream().mapToInt(p -> p.age).sum(), is(66));
    }

    @Test
    public void nonInterferenceTest() throws Exception {
        List<String> list = Lists.newArrayList("one", "two");
        Stream<String> stream = list.stream();
        list.add("three");

        assertThat(stream.collect(joining(" ")), is("one two three"));
    }

    /**
     * Stateful behavioral parameters is bad with respect to
     * - safety
     *   - if you do not synchronize access to the state, the code is broken
     * - performance
     *   - if you synchronize access to the state, you risk having contention
     *
     * So try to avoid statefulness.
     */
    @Test
    public void statefulBehaviorsTest() throws Exception {
        Set<Integer> seen = Collections.synchronizedSet(new HashSet<>());
        Stream.of(1, 2, 3).parallel().map( e -> {
            if (seen.add(e))
                return 0;
            else
                return e;
        });
    }

    /**
     * https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html
     */
    @Test
    public void reduceTest() throws Exception {
        assertThat(roster.stream().map(p -> p.age).reduce((a, b) -> a + b).get(), is(33 + 33));
        assertThat(roster.stream().map(p -> p.age).reduce((a, b) -> a * b).get(), is(33 * 33));

        assertThat(roster.stream().map(p -> p.age).reduce(0, (a, b) -> a + b), is(33 + 33));
        assertThat(roster.stream().map(p -> p.age).reduce(1, (a, b) -> a * b), is(33 * 33));
        assertThat(roster.stream().map(p -> p.age).reduce(0, Integer::sum), is(33 + 33));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html
     */
    @Test
    public void testGroupBy() throws Exception {
        Map<Person.Sex, List<Person>> byGender = roster.stream()
                .collect(Collectors.groupingBy(p -> p.gender));

        assertThat(byGender.size(), is(2));

        Map<Person.Sex, List<String>> namesByGender = roster.stream()
                .collect(Collectors.groupingBy(p -> p.gender, Collectors.mapping(p -> p.name, Collectors.toList())));

        assertThat(namesByGender.size(), is(2));

        Map<Person.Sex, Integer> totalAgeByGender = roster.stream()
                .collect(Collectors.groupingBy(p -> p.gender, Collectors.reducing(0, p -> p.age, Integer::sum)));

        assertThat(totalAgeByGender.size(), is(2));

        List<String> list = Arrays.asList("c 1", "a 1", "b 2", "b 1", "a 10");
        Map<String, List<String>> byFirstAndSort = list.stream()
                .sorted()
                .collect(Collectors.groupingBy(s -> s.substring(0, 1)));

        Iterator<Map.Entry<String, List<String>>> iterator = byFirstAndSort.entrySet().iterator();
        assertThat(iterator.next().getKey(), is("a"));
        assertThat(iterator.next().getKey(), is("b"));
        assertThat(iterator.next().getKey(), is("c"));
    }

    @Test
    public void mapToSameTypeTest() throws Exception {
        List<Person> persons = roster.stream()
                .map(p -> {
                    if (p.gender == Person.Sex.MALE) {
                        return new Person(1l, "New", 33, LocalDate.of(1983, 5, 28), Person.Sex.MALE,
                                Arrays.asList("Piano", "Climbing"));
                    }
                    return p;
                })
                .collect(Collectors.toList());

        assertThat(persons.get(0).name, is("New"));
    }

    @Test
    public void maxTest() throws Exception {
        assertThat(IntStream.of(1, 2).max().getAsInt(), is(2));
    }

    /**
     * http://stackoverflow.com/questions/22561614/java-8-streams-min-and-max-why-does-this-compile
     */
    @Test
    public void minMaxArgumentTest() throws Exception {
        final List<Integer> list = IntStream.rangeClosed(1, 20)
                .boxed().collect(Collectors.toList());

        assertThat(list.stream().max(Integer::compare).get(), is(20));
    }

    /**
     * http://stackoverflow.com/questions/23079003/how-to-convert-a-java-8-stream-to-an-array
     * http://stackoverflow.com/questions/23007422/using-streams-with-primitives-data-types-and-corresponding-wrappers
     */
    @Test
    public void toArrayTest() throws Exception {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        Integer[] arr = stream.toArray(Integer[]::new);
        assertThat(arr[0], is(1));

        IntStream intStream = IntStream.of(4, 5, 6);
        int[] intArr = intStream.toArray();
        assertThat(intArr[0], is(4));
    }
}
