import com.google.common.collect.Sets;
import lombok.*;
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
    @NoArgsConstructor
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

        private String name;
        private Set<Device> devices;

        public Person(String name) {
            this.name = name;
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class PersonDTO {
        private String name;
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
    public void converterSetTest() throws Exception {
        PersonDTO dto = new PersonDTO("name", "iPhone", "mobile");
        Person person = mapper.map(dto, new TypeToken<Person>() {}.getType());

        assertThat(person.getDevices().iterator().next().toString(), is("iPhone mobile"));
    }

    @Test
    public void converterTest() throws Exception {
        Person person = new Person("abc");
        PersonDTO dto = mapper.map(person, new TypeToken<PersonDTO>() {}.getType());

        assertThat(dto.getName(), is("abc"));
    }
}
