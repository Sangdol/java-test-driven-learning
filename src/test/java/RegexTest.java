/**
 * @author hugh
 */
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Regex Tutorials
 * - http://www.vogella.com/tutorials/JavaRegularExpressions/article.html
 */
public class RegexTest {

    @Test
    public void stringMatches() throws Exception {
        String text = "abc abc";

        assertTrue(Pattern.matches(".*", text));
        assertTrue(text.matches(".*"));
        assertTrue(text.matches("ab.*"));

        assertFalse(text.matches("ab"));
        assertFalse(text.matches("abc"));
    }

    @Test
    public void shouldGroupCountShowTheCountOfGroupOfPattern() throws Exception {
        Matcher m = Pattern.compile("bc").matcher("");
        assertThat(m.groupCount(), is(0));

        m = Pattern.compile("(bc)").matcher("");
        assertThat(m.groupCount(), is(1));

        m = Pattern.compile("(bc)(d)").matcher("");
        assertThat(m.groupCount(), is(2));

        m = Pattern.compile("((bc)(d))").matcher("");
        assertThat(m.groupCount(), is(3));
    }

    @Test
    public void shouldFindAllGroups() throws Exception {
        String text = "abcd bcdf";
        Pattern pattern = Pattern.compile("a(bc)d b(cd)f");
        Matcher m = pattern.matcher(text);

        assertTrue(m.find());
        assertThat(m.group(0), is(text));
        assertThat(m.group(1), is("bc"));
        assertThat(m.group(2), is("cd"));

        assertFalse(m.find());
    }

    @Test
    public void shouldFindAllMatches() throws Exception {
        String text = "abcd bcdf";
        Pattern pattern = Pattern.compile("bc");
        Matcher m = pattern.matcher(text);

        int index = 0;

        while (m.find()) {
            assertThat(m.group(), is("bc"));
            index++;
        }

        assertThat(index, is(2));
    }
}
