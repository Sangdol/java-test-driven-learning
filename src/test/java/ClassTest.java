import lombok.AllArgsConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author hugh
 */
public class ClassTest {

    @Test
    public void instanceOfTest() throws Exception {
        Integer i = 1;
        Double d = 1.0;

        assertTrue(i instanceof Number);
        assertTrue(d instanceof Number);
    }

    /**
     * http://stackoverflow.com/questions/1092096/is-the-t-class-in-generic-classt-assignable-from-another-class
     */
    @Test
    public void isAssignableFrom() throws Exception {
        Class<Integer> intCls = Integer.class;
        assertTrue(intCls.isAssignableFrom(Integer.class));
        assertFalse(intCls.isAssignableFrom(Number.class));
        assertFalse(intCls.isAssignableFrom(List.class));

        Class<Number> numCls = Number.class;
        assertTrue(numCls.isAssignableFrom(Integer.class));
        assertTrue(numCls.isAssignableFrom(Number.class));
        assertFalse(numCls.isAssignableFrom(Object.class));
        assertFalse(numCls.isAssignableFrom(List.class));

        Class<?> listCls = List.class;
        assertTrue(listCls.isAssignableFrom(ArrayList.class));
        assertTrue(listCls.isAssignableFrom(LinkedList.class));
    }

    @Test
    public void classPathTest() throws Exception {
        String classPath = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(classPath);
    }
    
    static class Outer {
        @AllArgsConstructor
        static class StaticInner {
            public final int no;
        }

        @AllArgsConstructor
        class Inner {
            public final int no;
        }
    }

    /**
     * http://docs.oracle.com/javase/tutorial/java/javaOO/nested.html
     * http://stackoverflow.com/questions/70324/java-inner-class-and-static-nested-class
     */
    @Test
    public void nestedClassTest() throws Exception {
        Outer.StaticInner staticInner = new Outer.StaticInner(1);
        assertThat(staticInner.no, is(1));

        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner(2);
        assertThat(inner.no, is(2));
    }
}
