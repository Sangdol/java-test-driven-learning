import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * http://www.joda.org/joda-time/
 * 
 * @author hugh
 */
public class JodaTest {
    @Test
    public void addMinus() {
        DateTime now = new DateTime(2015, 1, 1, 1, 0);
        DateTime twoMinutesLater = now.plusMinutes(2);

        assertThat(twoMinutesLater.getMinuteOfDay() - now.getMinuteOfDay(), is(2));
        
    }
    
    @Test
    public void parseDate() {
        DateTime date = DateTime.parse("1983-05-28T01:10");
        
        assertThat(date.getYear(), is(1983));
        assertThat(date.getMonthOfYear(), is(5));
        assertThat(date.getHourOfDay(), is(1));
        assertThat(date.getMinuteOfHour(), is(10));
    }
}
