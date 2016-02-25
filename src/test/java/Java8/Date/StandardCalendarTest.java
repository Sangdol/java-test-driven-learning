package Java8.Date;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static java.time.temporal.ChronoField.*;

/**
 * https://docs.oracle.com/javase/tutorial/datetime/iso/index.html
 *
 * @author hugh
 */
@SuppressWarnings("ALL")
public class StandardCalendarTest {
    
    private Locale defaultLocale = Locale.getDefault();
    private ZoneId UTC = ZoneId.of("UTC");
    private ZoneId PST = ZoneId.of("America/Los_Angeles");

    /**
     * http://stackoverflow.com/questions/28263244/java-8-equality-of-utc-and-zulu-time
     */
    @Test
    public void utcAndZuluTest() throws Exception {
        ZoneId utc = ZoneId.of("UTC");
        ZoneId zulu = ZoneId.of("Z");

        assertThat(utc, is(not(zulu)));
        assertThat(utc.toString(), is("UTC"));
        assertThat(zulu.toString(), is("Z"));

        LocalDateTime ldt = LocalDateTime.of(2015, 11, 11, 1, 1);
        ZonedDateTime utcDate = ZonedDateTime.of(ldt, utc);
        ZonedDateTime zuluDate = ZonedDateTime.of(ldt, zulu);

        assertThat(utcDate.toString(), is("2015-11-11T01:01Z[UTC]"));
        assertThat(zuluDate.toString(), is("2015-11-11T01:01Z"));

        assertThat(utc.normalized().toString(), is("UTC"));
        assertThat(zulu.normalized().toString(), is("UTC"));

        assertThat(ZonedDateTime.of(ldt, zulu.normalized()), is("2015-11-11T01:01Z[UTC]"));
    }

    /**
     * http://stackoverflow.com/questions/25747499/java-8-calculate-difference-between-two-localdatetime
     */
    @Test
    public void diffTest() throws Exception {
        LocalDateTime start = LocalDateTime.of(2015, 1, 10, 10, 10);
        LocalDateTime end = LocalDateTime.of(2015, 2, 10, 10, 10);

        assertThat(start.until(end, ChronoUnit.MONTHS), is(1l));

        start = LocalDateTime.of(2015, 1, 10, 10, 11);
        assertThat(start.until(end, ChronoUnit.MONTHS), is(0l));

        start = LocalDateTime.of(2015, 1, 30, 10, 10);
        end = LocalDateTime.of(2015, 2, 28, 10, 10);
        assertThat(start.until(end, ChronoUnit.MONTHS), is(0l));

        assertThat(ChronoUnit.MONTHS.between(start, end), is(0l));

        assertThat(ChronoUnit.DAYS.between(start, end), is(29L));
        assertThat(ChronoUnit.SECONDS.between(start, end), is(2505600L));
        assertThat(ChronoUnit.SECONDS.between(end, start), is(-2505600L));

        assertThat(ChronoUnit.SECONDS.between(start, end),
                is(start.until(end, ChronoUnit.SECONDS)));
    }
    
    @Test
    public void comparisonTest() throws Exception {
        LocalDateTime before = LocalDateTime.now();
        LocalDateTime after = before.plusDays(1);

        assertThat(before.compareTo(after), is(-1));
        assertThat(before.isBefore(after), is(true));
    }

    @Test
    public void testDateInitialization() throws Exception {
        long milliseconds = 100l;

        Date date = new Date(milliseconds);
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), UTC);

        assertThat(date.getTime(), is(Date.from(ldt.atZone(UTC).toInstant()).getTime()));

        assertThat(new Date().getTime(), is(Instant.now().toEpochMilli()));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/enum.html
     */
    @Test
    public void dayOfWeekEnumTest() throws Exception {
        assertThat(DayOfWeek.MONDAY.plus(3), is(DayOfWeek.THURSDAY));
        assertThat(DayOfWeek.SUNDAY.plus(1), is(DayOfWeek.MONDAY));

        assertThat(DayOfWeek.MONDAY.getDisplayName(TextStyle.FULL, defaultLocale), is("Monday"));
        assertThat(DayOfWeek.MONDAY.getDisplayName(TextStyle.NARROW, defaultLocale), is("M"));
        assertThat(DayOfWeek.MONDAY.getDisplayName(TextStyle.SHORT, defaultLocale), is("Mon"));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/enum.html
     */
    @Test
    public void monthEnumTest() throws Exception {
        assertThat(Month.APRIL.plus(3), is(Month.JULY));
        assertThat(Month.NOVEMBER.plus(4), is(Month.MARCH));

        assertThat(Month.AUGUST.getDisplayName(TextStyle.FULL, defaultLocale), is("August"));
        assertThat(Month.AUGUST.getDisplayName(TextStyle.NARROW, defaultLocale), is("A"));
        assertThat(Month.AUGUST.getDisplayName(TextStyle.SHORT, defaultLocale), is("Aug"));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/date.html
     */
    @Test
    public void localDateTest() throws Exception {
        LocalDate date = LocalDate.of(2015, Month.SEPTEMBER, 18);

        assertThat(date.getDayOfWeek(), is(DayOfWeek.FRIDAY));
        assertThat(date.with(TemporalAdjusters.next(DayOfWeek.MONDAY)), is(LocalDate.of(2015, 9, 21)));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/date.html
     */
    @Test
    public void yearMonthTest() throws Exception {
        assertThat(YearMonth.of(2000, 2).lengthOfMonth(), is(29));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/date.html
     */
    @Test
    public void monthDayTest() throws Exception {
        assertThat(MonthDay.of(2, 29).isValidYear(2000), is(true));
        assertThat(MonthDay.of(2, 29).isValidYear(2001), is(false));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/datetime.html
     */
    @Test
    public void localTimeTest() throws Exception {
        assertThat(LocalTime.of(12, 30, 01).getSecond(), is(1));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/datetime.html
     *
     * Without timezone
     */
    @Test
    public void localDateTimeTest() throws Exception {
        assertThat(LocalDateTime.of(2015, 9, 24, 17, 40).getHour(), is(17));
        assertThat(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).getHour(), is(lessThan(24)));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/timezones.html
     */
    @Test
    public void zoneTest() throws Exception {
        Set<String> allZones = ZoneId.getAvailableZoneIds();
        List<String> zoneList = new ArrayList<>(allZones);
        Collections.sort(zoneList);

        LocalDateTime dt = LocalDateTime.now();

        for (String s : zoneList) {
            ZoneId zone= ZoneId.of(s);
            ZonedDateTime zdt = dt.atZone(zone);
            ZoneOffset offset = zdt.getOffset();
            int secondsOfHour = offset.getTotalSeconds() % (60 * 60);
            String out = String.format("%35s %10s", zone, offset);

            System.out.println("-- All List --");
            System.out.println(out);

            // Write only time zones that do not have a whole hour offset
            // to standard out.
//            System.out.println("-- Minutes.. --");
//            if (secondsOfHour != 0) {
                // System.out.println(out);
//            }
        }
    }

    @Test
    public void ChangingZoneTest() throws Exception {
        ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.of(2015, 11, 15, 0, 0), UTC);

        assertThat(zdt.withZoneSameLocal(PST).toString(), is("2015-11-15T00:00-08:00[America/Los_Angeles]"));
        assertThat(zdt.withZoneSameInstant(PST).toString(), is("2015-11-15T16:00-08:00[America/Los_Angeles]"));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/timezones.html
     *
     * ZoneDateTime = LocalDateTime + ZoneId
     */
    @Test
    public void zonedDateTimeTest() throws Exception {
        LocalDateTime leaving = LocalDateTime.of(2015, 9, 24, 17, 40);
        ZoneId leavingZone = ZoneId.of("Asia/Seoul");
        ZonedDateTime depature = ZonedDateTime.of(leaving, leavingZone);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
        String out1 = depature.format(formatter);

        assertThat(String.format("Leaving: %s (%s)", out1, leavingZone),
                is("Leaving: Sep 24 2015 05:40 PM (Asia/Seoul)"));

        ZoneId arrivingZone = PST;
        ZonedDateTime arriving = depature.withZoneSameInstant(arrivingZone).plusMinutes(650);
        String out2 = arriving.format(formatter);
        assertThat(String.format("Arriving: %s (%s)", out2, arrivingZone),
                is("Arriving: Sep 24 2015 12:30 PM (America/Los_Angeles)"));

        assertThat(arrivingZone.getRules().isDaylightSavings(arriving.toInstant()), is(true));
    }

    @Test
    public void zonedDateTimeToDateTest() throws Exception {
        LocalDateTime date = LocalDateTime.of(2015, 10, 10, 0, 0);
        ZoneId zone = ZoneId.of("Z");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, zone);

        assertThat(Date.from(zonedDateTime.toInstant()).toString(), is("Sat Oct 10 00:00:00 UTC 2015"));

        zone = ZoneId.of("Asia/Seoul");
        zonedDateTime = ZonedDateTime.of(date, zone);
        assertThat(Date.from(zonedDateTime.toInstant()).toString(), is("Sat Oct 09 15:00:00 UTC 2015"));
    }

    @Test
    public void zonedDateTimeStartOfMonthTest() throws Exception {
        LocalDateTime date = LocalDateTime.of(2015, 10, 10, 0, 0);
        ZoneId zone = ZoneId.of("Z");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, zone);
        ZonedDateTime startOfMonth = zonedDateTime.with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        assertThat(startOfMonth.toString(), is("2015-10-01T00:00Z"));

        zone = ZoneId.of("Asia/Seoul");
        zonedDateTime = ZonedDateTime.of(date, zone);
        startOfMonth = zonedDateTime.with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        assertThat(startOfMonth.toString(), is("2015-10-01T00:00+09:00[Asia/Seoul]"));

        zone = ZoneId.of("Asia/Seoul");
        zonedDateTime = ZonedDateTime.of(date, zone).withZoneSameInstant(ZoneId.of("Z"));
        startOfMonth = zonedDateTime.with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        assertThat(startOfMonth.toString(), is("2015-10-01T00:00Z"));
    }

    /**
     * http://stackoverflow.com/questions/29143910/java-8-date-time-get-start-of-day-from-zoneddatetime
     */
    @Test
    public void zonedDateTimeStartOfMonthTest2() throws Exception {
        LocalDateTime date = LocalDateTime.of(2015, 10, 10, 1, 10);
        ZoneId zone = ZoneId.of("Z");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, zone);
        
        assertThat(zonedDateTime.with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS).toString(),
                is("2015-10-01T00:00Z"));
    }

    @Test
    public void truncatedToTest() throws Exception {
        LocalDateTime date = LocalDateTime.of(2015, 10, 10, 1, 2, 3, 4);
        assertThat(date.truncatedTo(ChronoUnit.DAYS).toString(), is("2015-10-10T00:00"));
        assertThat(date.truncatedTo(ChronoUnit.HOURS).toString(), is("2015-10-10T01:00"));
        assertThat(date.truncatedTo(ChronoUnit.MINUTES).toString(), is("2015-10-10T01:02"));
        assertThat(date.truncatedTo(ChronoUnit.SECONDS).toString(), is("2015-10-10T01:02:03"));
    }

    @Test
    public void confusingZonedDateTimeTest() throws Exception {
        LocalDateTime date = LocalDateTime.of(2015, 10, 10, 0, 0);
        ZoneId zone = ZoneId.of("Z");
        ZonedDateTime utc = ZonedDateTime.of(date, zone);

        assertThat(utc.getHour(), is(0));

        ZonedDateTime pst = utc.withZoneSameInstant(PST);

        assertThat(pst.getHour(), is(17));
        assertThat(pst.toString(), is("2015-10-09T17:00-07:00[America/Los_Angeles]"));

        assertThat(utc.until(pst, ChronoUnit.MILLIS), is(0L));
    }

    @Test
    public void zoneChangeFromUtcToPstTest() throws Exception {
        DateTimeFormatter iso = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime date = LocalDateTime.of(2015, 9, 30, 0, 0);
        ZonedDateTime zonedDate = ZonedDateTime.of(date, PST);

        assertThat(zonedDate.format(iso), is("2015-09-30T00:00:00-07:00[America/Los_Angeles]"));

        zonedDate = zonedDate.withZoneSameInstant(UTC);
        assertThat(zonedDate.format(iso), is("2015-09-30T07:00:00Z"));
    }

    @Test
    public void zoneChangeFromPstToUtcTest() throws Exception {
        DateTimeFormatter iso = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime date = LocalDateTime.of(2015, 9, 30, 0, 0);
        ZonedDateTime zonedDate = ZonedDateTime.of(date, UTC);

        assertThat(zonedDate.format(iso), is("2015-09-30T00:00:00Z"));

        zonedDate = zonedDate.withZoneSameInstant(PST);
        assertThat(zonedDate.format(iso), is("2015-09-29T17:00:00-07:00[America/Los_Angeles]"));
    }

    @Test
    public void zonedToLocalTest() throws Exception {
        ZonedDateTime date = ZonedDateTime.of(LocalDateTime.of(2015, 10, 1, 0, 0), PST);
        LocalDateTime localDateTime = date.toLocalDateTime();
        
        assertThat(localDateTime.toString(), is("2015-10-01T00:00"));

        localDateTime = date.withZoneSameInstant(UTC).toLocalDateTime();
        assertThat(localDateTime.toString(), is("2015-10-01T07:00"));
    }
    
    @Test
    public void toStringTest() throws Exception {
        LocalDateTime date = LocalDateTime.of(2015, 10, 1, 14, 23);
        
        assertThat(date.toString(), is("2015-10-01T14:23"));

        ZonedDateTime zonedDate = ZonedDateTime.of(date, UTC);
        
        assertThat(zonedDate.toString(), is("2015-10-01T14:23Z[UTC]"));
        assertThat(zonedDate + "", is("2015-10-01T14:23Z[UTC]"));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/timezones.html
     *
     * OffsetDateTime = LocalDateTime + ZoneOffset
     */
    @Test
    public void offsetDateTimeTest() throws Exception {
        LocalDateTime localDate = LocalDateTime.of(2015, 9, 25, 11, 33);
        ZoneOffset offset = ZoneOffset.of("+09:00");

        OffsetDateTime offsetDate = OffsetDateTime.of(localDate, offset);
        OffsetDateTime lastThursday = offsetDate.with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));

        assertThat(lastThursday.getDayOfMonth(), is(24));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/timezones.html
     *
     * OffsetTime = LocalTime + ZoneOffset
     */
    @Test
    public void offsetTimeTest() throws Exception {
        LocalTime localTime = LocalTime.of(2, 43);
        ZoneOffset offset1 = ZoneOffset.of("-08:00");
        ZoneOffset offset2 = ZoneOffset.of("+09:00");

        OffsetTime offsetTime1 = OffsetTime.of(localTime, offset1);
        OffsetTime offsetTime2 = OffsetTime.of(localTime, offset2);

        assertThat(offsetTime1.getHour(), is(2));
        assertThat(offsetTime2.getHour(), is(2));

        assertThat(offsetTime1.withOffsetSameInstant(ZoneOffset.of("Z")).getHour(), is(10));
        assertThat(offsetTime2.withOffsetSameInstant(ZoneOffset.of("Z")).getHour(), is(17));

        assertTrue(offsetTime1.isAfter(offsetTime2));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/instant.html
     *
     * Instant class represents the start of a nanosecond on the timeline.
     */
    @Test
    public void instantTest() throws Exception {
        Instant nextDayOfEpoch = Instant.ofEpochSecond(3600 * 25);

        assertThat(nextDayOfEpoch.toString(), is("1970-01-02T01:00:00Z"));

        LocalDateTime ldt = LocalDateTime.ofInstant(nextDayOfEpoch, UTC);
        assertThat(ldt.getHour(), is(1));

        ldt = LocalDateTime.ofInstant(nextDayOfEpoch, ZoneId.of("Asia/Seoul"));
        assertThat(ldt.getHour(), is(10));

        Instant instant = Instant.parse("2015-10-01T11:10:00Z");
        assertThat(LocalDateTime.ofInstant(instant, UTC).toString(), is("2015-10-01T11:10"));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/format.html
     */
    @Test
    public void parsingTest() throws Exception {
        // throws DateTimeParseException
        LocalDate localDate = LocalDate.parse("20150925", DateTimeFormatter.BASIC_ISO_DATE);

        assertThat(localDate.toString(), is("2015-09-25"));

        localDate = LocalDate.parse("2015-09-25", DateTimeFormatter.ISO_LOCAL_DATE);
        assertThat(localDate.toString(), is("2015-09-25"));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        formatter.parse("2015-10-01", LocalDateTime::from);
    }

    @Test
    public void optionalParsing() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendValue(HOUR_OF_DAY,2)
                .optionalStart()
                .appendValue(MINUTE_OF_HOUR, 2)
                .toFormatter();

        assertThat(formatter.parse("1010", LocalTime::from).toString(), is("10:10"));
        assertThat(formatter.parse("10", LocalTime::from).toString(), is("10:00"));
    }

    /**
     * http://stackoverflow.com/questions/32962417/lenient-java-8-date-parsing/32964692
     */
    @Test
    public void lenientParsing0() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy")
                .appendLiteral('-')
                .appendValue(MONTH_OF_YEAR)
                .appendPattern("['-'d][ HH:mm]") // optional sections are surrounded by []
                .parseDefaulting(DAY_OF_MONTH, DAY_OF_MONTH.range().getMinimum())
                .parseDefaulting(HOUR_OF_DAY, HOUR_OF_DAY.range().getMinimum())
                .parseDefaulting(MINUTE_OF_HOUR, MINUTE_OF_HOUR.range().getMinimum())
                .parseDefaulting(SECOND_OF_MINUTE, SECOND_OF_MINUTE.range().getMinimum())
                .parseDefaulting(NANO_OF_SECOND, NANO_OF_SECOND.range().getMinimum())
                .toFormatter();

        assertThat(LocalDateTime.parse("2015-9", formatter).toString(), is("2015-09-01T00:00"));
        assertThat(LocalDateTime.parse("2015-09 10:10", formatter).toString(), is("2015-09-01T10:10"));
        assertThat(LocalDateTime.parse("2015-09-05", formatter).toString(), is("2015-09-05T00:00"));
        assertThat(LocalDateTime.parse("2015-10-01", formatter).toString(), is("2015-10-01T00:00"));
        assertThat(LocalDateTime.parse("2015-1-1 10:10", formatter).toString(), is("2015-01-01T10:10"));

        System.out.printf(LocalDate.of(2010,1,1).format(formatter));
    }

    /**
     * Need to answer this question first.
     * What is the end of day?
     */
    @Test
    public void endOfDayTest() throws Exception {
        LocalDateTime janFirst = LocalDateTime.of(2015, 1, 1, 0, 0);

        assertThat(janFirst.plusDays(1).minusNanos(1).toString(), is("2015-01-01T23:59:59.999999999"));
        assertThat(janFirst.plusDays(1).minusSeconds(1).toString(), is("2015-01-01T23:59:59"));
    }

    /**
     * Answer this
     * http://stackoverflow.com/questions/27293994/how-to-parse-zoneddatetime-with-default-zone
     */
    @Test
    public void isoZonedDateTimeParsingTest() throws Exception {
        ZonedDateTime parsed;

        parsed = ZonedDateTime.parse("2015-11-10T10:52:00+01:00[Europe/Paris]",
                DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(UTC));

        assertThat(parsed.toString(), is("2015-11-10T10:52+01:00[Europe/Paris]"));

        parsed = ZonedDateTime.parse("2015-11-10T10:52:00+01:00",
                DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(UTC));

        assertThat(parsed.toString(), is("2015-11-10T10:52Z"));
    }

    @Test
    public void lenientParsingWithZone0() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy")
                .appendLiteral('-')
                .appendValue(MONTH_OF_YEAR)
                .appendPattern("['-'d][ HH:mm][ z]") // optional sections are surrounded by []
                // or
//                .appendPattern("yyyy'-'M['-'d][ HH:mm][ z]")
                .parseDefaulting(DAY_OF_MONTH, DAY_OF_MONTH.range().getMinimum())
                .parseDefaulting(HOUR_OF_DAY, HOUR_OF_DAY.range().getMinimum())
                .parseDefaulting(MINUTE_OF_HOUR, MINUTE_OF_HOUR.range().getMinimum())
                .parseDefaulting(SECOND_OF_MINUTE, SECOND_OF_MINUTE.range().getMinimum())
                .parseDefaulting(NANO_OF_SECOND, NANO_OF_SECOND.range().getMinimum())
                .toFormatter()
                .withZone(UTC);

        assertThat(ZonedDateTime.parse("2015-9", formatter).toString(), is("2015-09-01T00:00Z"));
        assertThat(ZonedDateTime.parse("2015-9 PST", formatter).toString(), is("2015-09-01T00:00-07:00[America/Los_Angeles]"));
        assertThat(ZonedDateTime.parse("2015-09 10:10", formatter).toString(), is("2015-09-01T10:10Z"));
        assertThat(ZonedDateTime.parse("2015-09-05", formatter).toString(), is("2015-09-05T00:00Z"));
        assertThat(ZonedDateTime.parse("2015-10-01", formatter).toString(), is("2015-10-01T00:00Z"));
        assertThat(ZonedDateTime.parse("2015-1-1 10:10", formatter).toString(), is("2015-01-01T10:10Z"));
        assertThat(ZonedDateTime.parse("2015-1-1 10:10 KST", formatter).toString(), is("2015-01-01T10:10+09:00[Asia/Seoul]"));

        assertThat(LocalDateTime.parse("2015-1-1 10:10", formatter).toString(), is("2015-01-01T10:10"));
        assertThat(LocalDateTime.parse("2015-1-1 10:10 KST", formatter).toString(), is("2015-01-01T10:10"));

        System.out.printf(LocalDate.of(2010,1,1).format(formatter));
    }

    @Test
    public void lenientParsing1() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                                        .appendPattern("yyyy")
                                        .appendLiteral('-')
                                        .appendValue(MONTH_OF_YEAR)
                                        .appendLiteral('-')
                                        .appendValue(DAY_OF_MONTH)
                                        .appendPattern("[ HH:mm]") // optional sections are surrounded by []
                                        .parseDefaulting(HOUR_OF_DAY, HOUR_OF_DAY.range().getMinimum())
                                        .parseDefaulting(MINUTE_OF_HOUR, MINUTE_OF_HOUR.range().getMinimum())
                                        .parseDefaulting(SECOND_OF_MINUTE, SECOND_OF_MINUTE.range().getMinimum())
                                        .parseDefaulting(NANO_OF_SECOND, NANO_OF_SECOND.range().getMinimum())
                                        .toFormatter();

        assertThat(formatter.parse("2015-9-5", LocalDateTime::from).toString(), is("2015-09-05T00:00"));
        assertThat(formatter.parse("2015-10-01", LocalDateTime::from).toString(), is("2015-10-01T00:00"));
        assertThat(formatter.parse("2015-1-1 10:10", LocalDateTime::from).toString(), is("2015-01-01T10:10"));
    }

    @Test
    public void lenientParsing2() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy")
                .appendLiteral('-')
                .appendValue(MONTH_OF_YEAR)
                .appendLiteral('-')
                .appendValue(DAY_OF_MONTH)
                .optionalStart()
                .appendLiteral(' ')
                .appendValue(HOUR_OF_DAY)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR)
                .optionalEnd()
                .parseDefaulting(HOUR_OF_DAY, HOUR_OF_DAY.range().getMinimum())
                .parseDefaulting(MINUTE_OF_HOUR, MINUTE_OF_HOUR.range().getMinimum())
                .parseDefaulting(SECOND_OF_MINUTE, SECOND_OF_MINUTE.range().getMinimum())
                .parseDefaulting(NANO_OF_SECOND, NANO_OF_SECOND.range().getMinimum())
                .toFormatter();

        assertThat(formatter.parse("2015-9-5", LocalDateTime::from).toString(), is("2015-09-05T00:00"));
        assertThat(formatter.parse("2015-10-01", LocalDateTime::from).toString(), is("2015-10-01T00:00"));
        assertThat(formatter.parse("2015-10-01 10:10", LocalDateTime::from).toString(), is("2015-10-01T10:10"));
    }

    @Test(expected = DateTimeParseException.class)
    public void parseExceptionTest1() throws Exception {
        LocalDateTime.parse("2015-10", DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    @Test(expected = DateTimeParseException.class)
    public void parseExceptionTest2() throws Exception {
        LocalDate.parse("2015-9-5", DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/format.html
     */
    @Test
    public void formattingTest() throws Exception {
        LocalDate localDate = LocalDate.parse("20150925", DateTimeFormatter.BASIC_ISO_DATE);

        // throws DateTimeException
        assertThat(localDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")), is("Sep 25 2015"));

        LocalDateTime date = LocalDateTime.of(2015, 9, 25, 1, 2);
        assertThat(date.format(DateTimeFormatter.ISO_DATE_TIME), is("2015-09-25T01:02:00"));

        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, UTC);
        assertThat(zonedDateTime.format(DateTimeFormatter.ofPattern("a")), is("AM"));
        assertThat(zonedDateTime.format(DateTimeFormatter.ofPattern("Z")), is("+0000"));
    }

    @Test
    public void forammtingExceptionTest() throws Exception {
        LocalDateTime date = LocalDateTime.now();
        date.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z"));
    }

    @Test
    public void forammtingException2Test() throws Exception {
        ZonedDateTime date = ZonedDateTime.now();
        System.out.println(date.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z")));

        System.out.println(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(new Date()));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/adjusters.html
     */
    @Test
    public void temporalAdjustersForMonthTest() throws Exception {
        LocalDate date = LocalDate.of(2015, 9, 25);
        DayOfWeek dow = date.getDayOfWeek();

        assertThat(String.format("%s is on a %s", date, dow), is("2015-09-25 is on a FRIDAY"));
        assertThat(date.with(TemporalAdjusters.firstDayOfMonth()).toString(), is("2015-09-01"));
        assertThat(date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).toString(), is("2015-09-07"));
        assertThat(date.with(TemporalAdjusters.lastDayOfMonth()).toString(), is("2015-09-30"));
        assertThat(date.with(TemporalAdjusters.firstDayOfNextMonth()).toString(), is("2015-10-01"));
        assertThat(date.with(TemporalAdjusters.firstDayOfNextYear()).toString(), is("2016-01-01"));
        assertThat(date.with(TemporalAdjusters.firstDayOfYear()).toString(), is("2015-01-01"));

        LocalDateTime dateTime = LocalDateTime.of(2015, 10, 1, 4, 15);
        assertThat(dateTime.with(TemporalAdjusters.firstDayOfMonth()).toString(), is("2015-10-01T04:15"));
    }

    /**
     *    October 2015
     * Su Mo Tu We Th Fr Sa
     *              1  2  3
     *  4  5  6  7  8  9 10
     * 11 12 13 14 15 16 17
     * 18 19 20 21 22 23 24
     * 25 26 27 28 29 30 31
     */
    @Test
    public void temporalAdjustersForWeekTest() throws Exception {
        LocalDateTime date = LocalDateTime.of(2015, 10, 12, 0, 0);
        LocalDateTime lastSunday  = date.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        assertThat(lastSunday.toString(), is("2015-10-11T00:00"));

        LocalDateTime lastLastSunday = date.minusWeeks(1).with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        assertThat(lastLastSunday.toString(), is("2015-10-04T00:00"));
    }

    /**
     * https://docs.oracle.com/javase/tutorial/datetime/iso/adjusters.html
     */
    @Test
    public void customAdjustersTest() throws Exception {
        LocalDate date = LocalDate.of(2015, 9, 25);
        
        LocalDate nextPayday = date.with(input -> {
            LocalDate ld = LocalDate.from(input);
            int day;
            if (ld.getDayOfMonth() < 15) {
                day = 15;
            } else {
                day = ld.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
            }
            ld = ld.withDayOfMonth(day);

            if (ld.getDayOfWeek() == DayOfWeek.SATURDAY || ld.getDayOfWeek() == DayOfWeek.SUNDAY) {
                ld = ld.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
            }

            return input.with(ld);
        });

        assertThat(nextPayday.getDayOfMonth(), is(30));
    }

    /**
     * Period takes into account DST whereas Duration doesn't.
     */
    @Test
    public void durationTest() throws Exception {
        long totalSeconds = 172855;

        Duration duration = Duration.of(totalSeconds, ChronoUnit.SECONDS);

        long days = duration.toDays();
        duration = duration.minusDays(days);

        long hours = duration.toHours();
        duration = duration.minusHours(hours);

        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).toMillis() / 1000;

        assertThat(days, is(2L));
        assertThat(hours, is(0L));
        assertThat(minutes, is(0L));
        assertThat(seconds, is(55L));

        // or without Duration
        // http://stackoverflow.com/questions/266825/how-to-format-a-duration-in-java-e-g-format-hmmss
        final int DAY = 86400;
        final int HOUR = 3600;
        final int MINUTE = 60;

        days = totalSeconds / DAY;
        hours = (totalSeconds % DAY) / HOUR;
        minutes = (totalSeconds % HOUR) / MINUTE;
        seconds = (totalSeconds % MINUTE);

        assertThat(days, is(2L));
        assertThat(hours, is(0L));
        assertThat(minutes, is(0L));
        assertThat(seconds, is(55L));
    }
}
