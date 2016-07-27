package Java8;

import lombok.*;
import org.junit.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class LambdaTest {

    /**
     * https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
     */
    @Test
    public void methodReferenceTest() throws Exception {
        // Reference to a static method
        Function<Integer, Double> toDouble = Double::valueOf;
        assertThat(toDouble.apply(1), is(1d));

        // Reference to an instant method of a particular object
        Optional<Person> oPerson = Optional.of(new Person(1));
        int age = oPerson.map(Person::getAge).get();
        assertThat(age, is(1));

        // Reference to an instant method of an arbitrary object of a particular type
        Person person = new Person(10);
        age = oPerson.map(person::getDoubleAge).get();
        assertThat(age, is(2));

        // Reference to a constructor
        Stream<Integer> stream = Stream.of(1, 2, 3);
        Integer[] arr = stream.toArray(Integer[]::new);
        assertThat(arr[0], is(1));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Person {
        int age;

        int getDoubleAge(Person person) {
            return person.getAge() * 2;
        }
    }
    
    @Test
    public void comparatorTest() throws Exception {
        Integer[] arr = new Integer[]{1, 2, 3};

        Comparator<Integer> comp = (Integer o1, Integer o2) -> o2.compareTo(o1);
        Arrays.sort(arr, comp);

        assertThat(arr, is(new Integer[]{3, 2, 1}));
    }
    
    @Test
    public void functionTest() throws Exception {
        Function<Integer, Double> toDouble = Double::valueOf;
        assertThat(toDouble.apply(1), is(1d));
        
        Function<Double, Double> tenTimes = (d) -> 10 * d;
        assertThat(toDouble.andThen(tenTimes).apply(2), is(20d));
    }
    
    @Test
    public void doubleFunctionTest() throws Exception {
        DoubleFunction<Integer> toInt = (d) -> (int) d;
        assertThat(toInt.apply(1.1), is(1));
    }
    
    @Test
    public void unaryOperatorTest() throws Exception {
        UnaryOperator<Integer> twice = (i) -> i * 2;
        assertThat(twice.apply(10), is(20));
    }
    
    @Test
    public void binaryOperatorTest() throws Exception {
        BinaryOperator<Integer> add = (a, b) -> a + b;
        assertThat(add.apply(1, 2), is(3));
    }
    
    @Test
    public void supplierTest() throws Exception {
        Supplier<Integer> ten = () -> 10;
        assertThat(ten.get(), is(10));
    }
    
    @Test
    public void consumerTest() throws Exception {
        Consumer<Integer> tester = (i) -> assertThat(i, is(10));
        tester.accept(10);
    }

    @Test
    public void predicateTest() throws Exception {
        Predicate<Integer> isZero = (i) -> i == 0;
        assertThat(isZero.test(10), is(false));
        assertThat(isZero.test(0), is(true));
    }
}
