package Java8.Date;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * https://docs.oracle.com/javase/tutorial/datetime/overview/naming.html
 *
 * @author hugh
 */
public class DateTimeMethodNamingConventionsTest {

    /**
     * static factory	Creates an instance where the factory is primarily validating the input parameters, not converting them.
     */
    @Test
    public void ofTest() throws Exception {
        assertThat(Month.of(11), is(Month.NOVEMBER));
    }

    /**
     * static factory	Converts the input parameters to an instance of the target class, which may involve losing information from the input.
     */
    @Test
    public void fromTest() throws Exception {

    }

    /**
     * static factory	Parses the input string to produce an instance of the target class.
     */
    @Test
    public void parseTest() throws Exception {

    }

    /**
     * instance	Uses the specified formatter to format the values in the temporal object to produce a string.
     */
    @Test
    public void formatTest() throws Exception {

    }

    /**
     * instance	Returns a part of the state of the target object.
     */
    @Test
    public void getTest() throws Exception {

    }

    /**
     * instance	Queries the state of the target object.
     */
    @Test
    public void isTest() throws Exception {

    }

    /**
     * instance	Returns a copy of the target object with one element changed; this is the immutable equivalent to a set method on a JavaBean.
     */
    @Test
    public void withTest() throws Exception {
        assertThat(LocalDate.of(2015, 9, 19).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                is(LocalDate.of(2015, 9, 21)));

    }

    /**
     * instance	Returns a copy of the target object with an amount of time added.
     */
    @Test
    public void plusTest() throws Exception {
        assertThat(Month.JANUARY.plus(3), is(Month.APRIL));

    }

    /**
     * instance	Returns a copy of the target object with an amount of time subtracted.
     */
    @Test
    public void minusTest() throws Exception {
        assertThat(DayOfWeek.MONDAY.minus(2), is(DayOfWeek.SATURDAY));
    }

    /**
     * instance	Converts this object to another type.
     */
    @Test
    public void toTest() throws Exception {

    }

    /**
     * instance	Combines this object with another.
     */
    @Test
    public void atTest() throws Exception {

    }
}
