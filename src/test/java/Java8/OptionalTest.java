package Java8;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * http://www.oracle.com/technetwork/articles/java/java8-optional-2175753.html
 * http://stackoverflow.com/questions/31922866/why-should-java-8s-optional-not-be-used-in-arguments?lq=1 - not for parameters?
 *
 * @author hugh
 */
public class OptionalTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test(expected = NullPointerException.class)
    public void ofExceptionTest() throws Exception {
        Optional.of(null);
    }

    @Test
    public void ofNullableTest() throws Exception {
        Optional<String> abc = Optional.ofNullable("Abc");
        assertThat(abc.get(), is("Abc"));
        
        Optional<String> nulla = Optional.ofNullable(null);
        assertThat(nulla.isPresent(), is(false));
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
        assertThat(name.orElse("ddd"), is("ddd"));
        assertThat(name.orElseGet(() -> {
            String abc = "abc";
            return abc + abc;
        }), is("abcabc"));
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

    @Test
    public void orElseThrowTest() throws Exception {
        Optional<String> name = Optional.ofNullable(null);

        exception.expect(IllegalStateException.class);
        name.orElseThrow(IllegalStateException::new);

        exception.expect(IllegalStateException.class);
        name.orElseThrow(() -> new IllegalStateException("msg"));
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
