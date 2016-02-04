import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author hugh
 */
public class ObjectsTest {

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

    @Test
    public void equalsTest() throws Exception {
        Person person = new Person("SD", 34);
        Child child = new Child("SD", 14);

        assertTrue(person.equals(child));
        assertFalse(child.equals(person));
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
