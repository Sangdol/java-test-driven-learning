import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author hugh
 *
 * from
 * http://www.glassdoor.com/Interview/Atlassian-Software-Engineer-Interview-Questions-EI_IE115699.0,9_KO10,27.htm
 */
public class TrickyQuestions {

    @Test
    public void instanceOf() {
        List<TrickyQuestions> tqs = new ArrayList<TrickyQuestions>();

        assertTrue(tqs instanceof List);
    }

    @Test
    public void testIntern() {
        String a = "foo";
        String b = "food".substring(0,3);
        String c = b.intern();

        assertTrue(a == c);
    }

    @Test(expected = NullPointerException.class)
    public void testNullIntValue() {
        Integer i = null;
        int j = i;

        assertThat(j, is(0));
    }

    @Test
    public void testTryFinallyReturn() {
        assertThat(tryFinallyReturn(), is(false));
    }

    private boolean tryFinallyReturn() {
        try {
            return true;
        } finally {
            return false;
        }
    }
}
