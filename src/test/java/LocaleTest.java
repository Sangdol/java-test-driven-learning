import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LocaleTest {

    @Test
    public void toLanguageTagTest() {
        Locale lo = new Locale("ko-KR");
        assertThat(lo.toLanguageTag(), is("und"));

        lo = new Locale("it_IT");
        assertThat(lo.toLanguageTag(), is("und"));

        lo = new Locale("it","IT");
        assertThat(lo.toLanguageTag(), is("it-IT"));

        lo = new Locale("it","IT");
        assertThat(lo.toLanguageTag(), is("it-IT"));
    }

}
