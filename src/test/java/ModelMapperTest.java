import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;

import java.util.Set;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * http://modelmapper.org/
 *
 * @author hugh
 */
public class ModelMapperTest {

    private ModelMapper mapper = new ModelMapper();

    @Setter
    @Getter
    static class Person {
        @AllArgsConstructor
        static class Device {
            final private String name;
            final private String type;

            @Override
            public String toString() {
                return name + " " + type;
            }
        }

        Set<Device> devices;
    }

    @Getter
    @AllArgsConstructor
    static class PersonDTO {
        private String deviceName;
        private String deviceType;
    }

    class PersonPropertyMap extends PropertyMap<PersonDTO, Person> {
        @Override
        protected void configure() {

            using(new AbstractConverter<PersonDTO, Set<Person.Device>>() {
                @Override
                protected Set<Person.Device> convert(PersonDTO source) {
                    return Sets.newHashSet(new Person.Device(source.deviceName, source.deviceType));
                }
            }).map(source).setDevices(null);

        }
    }

    @Before
    public void setup() {
        mapper.addMappings(new PersonPropertyMap());
    }


    /**
     * http://modelmapper.org/user-manual/property-mapping/#converters
     */
    @Test
    public void coverterTest() throws Exception {
        PersonDTO dto = new PersonDTO("iPhone", "mobile");
        Person person = mapper.map(dto, new TypeToken<Person>() {}.getType());

        assertThat(person.getDevices().iterator().next().toString(), is("iPhone mobile"));
    }
}
