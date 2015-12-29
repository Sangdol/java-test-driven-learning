import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author hugh
 */
public class ObjectsTest {

    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class Person {
        public final int age;
        public final String name;
    }

    /**
     * Refer to
     * http://stackoverflow.com/questions/909843/how-to-get-the-unique-id-of-an-object-which-overrides-hashcode
     */
    @Test
    public void hashCodeAndIdentityTest() throws Exception {
        Person p1 = new Person(10, "Tom");
        Person p2 = new Person(10, "Tom");

        assertThat(p1.hashCode(), is(p2.hashCode()));
        assertThat(System.identityHashCode(p1), is(not(System.identityHashCode(p2))));
        assertFalse(p1 == p2);
    }
}
