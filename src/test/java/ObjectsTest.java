import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author hugh
 */
public class ObjectsTest {

    @Test
    public void hashCodeTest() throws Exception {
        Integer a = 1;
        assertThat(a.hashCode(), is(1));

        Long two = 2L;
        assertThat(two.hashCode(), is(2));

        assertThat(2L << 32, is(8589934592L));
        Long twoToThe32 = 8589934592L;
        assertThat(twoToThe32.hashCode(), is(2));

        Map<Long, String> map  = new HashMap<>();
        map.put(two, "two");
        map.put(twoToThe32, "two to the thirty two");

        assertThat(map.get(two), is("two"));
        assertThat(map.get(twoToThe32), is("two to the thirty two"));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    static class Person {
        String name;
        int age;

        @Override
        public boolean equals(Object obj) {
            return name.equals(((Person)obj).name);
        }

        @Override
        public int hashCode() {
            return age;
        }
    }

    static class Child extends Person {

        public Child(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object obj) {
            return this.age == ((Person)obj).age;
        }
    }

    /**
     * http://stackoverflow.com/questions/27581/what-issues-should-be-considered-when-overriding-equals-and-hashcode-in-java
     */
    @Test
    public void reflexiveEqualsTest() throws Exception {
        Person person = new Person("SD", 34);
        assertThat(person.equals(person), is(true));
    }

    @Test
    public void notSymmetricEqualsTest() throws Exception {
        Person person = new Person("SD", 34);
        Child child = new Child("SD", 14);

        assertTrue(person.equals(child));
        assertFalse(child.equals(person));
    }

    @Test
    public void notTransitiveEqualsTest() throws Exception {
        Person person1 = new Person("SD", 34);
        Person person2 = new Person("SD", 34);
        Person person3 = new Person("SD", 34);

        assertThat(person1.equals(person2), is(true));
        assertThat(person2.equals(person3), is(true));
        assertThat(person1.equals(person3), is(true));
    }

    /**
     * Refer to
     * http://stackoverflow.com/questions/909843/how-to-get-the-unique-id-of-an-object-which-overrides-hashcode
     */
    @Test
    public void hashCodeAndIdentityTest() throws Exception {
        Person p1 = new Person("Tom", 10);
        Person p2 = new Person("Tom", 10);

        assertThat(p1.hashCode(), is(p2.hashCode()));
        assertThat(System.identityHashCode(p1), is(not(System.identityHashCode(p2))));
        assertFalse(p1 == p2);
    }
}
