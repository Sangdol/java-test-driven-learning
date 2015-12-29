import com.google.gson.*;
import lombok.AllArgsConstructor;
import org.junit.Test;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * GitHub https://github.com/google/gson
 *
 * Tutorials
 * - http://www.studytrails.com/java/json/java-google-json-introduction.jsp
 * - http://howtodoinjava.com/2014/06/17/google-gson-tutorial-convert-java-object-to-from-json/
 *
 * Java 8 Time serializer
 * - https://github.com/gkopff/gson-javatime-serialisers
 *
 * @author hugh
 */
public class GsonTest {

    @AllArgsConstructor
    class Person {
        final int age;
        final LocalDateTime birthDate;

        public String getName() {
            return "abc";
        }
    }

    /**
     * From http://stackoverflow.com/a/22726848/524588
     */
    class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type,
                JsonSerializationContext jsonSerializationContext) {
            long sec = localDateTime.toInstant(ZoneOffset.UTC).getEpochSecond();
            return new JsonPrimitive(sec);
        }
    }

    @Test
    public void jsonWithoutFormatTest() throws Exception {
        Gson gson = new GsonBuilder().create();

        Date date = new Date(115, 9, 20);
        assertThat(gson.toJson(date), is("\"Oct 20, 2015 12:00:00 AM\""));
    }

    @Test
    public void jsonWithFormatTest() throws Exception {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        Date date = new Date(115, 9, 20);
        System.out.print(date.getTime());
        assertThat(gson.toJson(date), is("\"2015-10-20\""));
    }

    /**
     * GSON doesn't use getters
     */
    @Test
    public void pojoTest() throws Exception {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .create();

        Person p = new Person(10, LocalDateTime.of(2015, 10, 20, 0, 0));

        assertThat(gson.toJson(p), is("{\"age\":10,\"birthDate\":1445299200}"));
    }
}
