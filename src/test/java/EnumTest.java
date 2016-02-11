import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author hugh
 */
public class EnumTest {
    enum Any {
        ABC;

        private final String value;

        Any() {
            this.value = this.name();
        }

        public String getValue() {
            return this.value;
        }
    }

    @Test
    public void nameInConstructorTest() throws Exception {
        assertThat(Any.ABC.getValue(), is("ABC"));
    }

    @Test
    public void valueOfTest() throws Exception {
        assertThat(Any.ABC.valueOf("ABC"), is(Any.ABC));
    }
}
