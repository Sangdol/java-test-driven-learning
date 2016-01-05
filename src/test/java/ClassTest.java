import lombok.AllArgsConstructor;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author hugh
 */
public class ClassTest {
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
