package Java8;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * http://www.oracle.com/technetwork/articles/java/java8-optional-2175753.html
 *
 * @author hugh
 */
public class OptionalTest {

    @Test(expected = NullPointerException.class)
    public void ofExceptionTest() throws Exception {
        Optional.of(null);
    }

    @Test
    public void ofNullableTest() throws Exception {
        Optional<String> abc = Optional.ofNullable("Abc");
        assertThat(abc.get(), is("Abc"));
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyTest() throws Exception {
        Optional<String> name = Optional.empty();
        assertFalse(name.isPresent());
        name.get(); // throws Exception
    }

    @Test
    public void creatingOptionalTest() throws Exception {
        Optional<String> name = Optional.of("abc");
        assertThat(name.toString(), is("Optional[abc]"));
        assertThat(name.get(), is("abc"));
        assertThat(name.orElse("ddd"), is("abc"));

        name = Optional.ofNullable(null);
        assertThat(name.get(), is(nullValue()));
        assertThat(name.orElse("ddd"), is("ddd"));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenNoElement() throws Exception {
        Optional<String> name = Optional.ofNullable(null);
        assertThat(name.get(), is(nullValue()));
    }
    
    @Test
    public void ifPresentTest() throws Exception {
        Optional<String> name = Optional.of("abc");

        name.ifPresent(System.out::println);
    }

    @Test(expected = IllegalStateException.class)
    public void orElseThrowTest() throws Exception {
        Optional<String> name = Optional.ofNullable(null);

        name.orElseThrow(IllegalStateException::new);
    }

    @Test(expected = IllegalStateException.class)
    public void orElseThrowWithMessageTest() throws Exception {
        Optional<String> name = Optional.ofNullable(null);

        name.orElseThrow(() -> new IllegalStateException("Message"));
    }

    @Test
    public void filterTest() throws Exception {
        Optional<String> name = Optional.of("abc");

        name.filter("abc"::equals).ifPresent(n -> System.out.println("ok"));
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    static class Person {
        final private String name;
        final private Optional<String> laptop;
    }

    /**
     * http://www.nurkiewicz.com/2013/08/optional-in-java-8-cheat-sheet.html
     * http://stackoverflow.com/questions/30864583/java-8-difference-between-optional-flatmap-and-optional-map
     */
    @Test
    public void mapAndFlatMapTest() throws Exception {
        Optional<Person> p1 = Optional.of(Person.of("SH", Optional.of("Macbook")));
        assertThat(p1.map(Person::getName).orElse(""), is("SH"));

        // flatMap() is for a method that returns Optional
        Optional<Person> p2 = Optional.of(Person.of("HJ", Optional.of("LG")));
        assertThat(p2.flatMap(Person::getLaptop).get(), is("LG"));
    }
}