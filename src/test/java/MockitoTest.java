import org.junit.Test;

import java.util.LinkedList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html
 *
 * @author hugh
 */
@SuppressWarnings("unchecked")
public class MockitoTest {

    @Test
    public void should_duplicated_verify_succeed() throws Exception {
        LinkedList mockedList = mock(LinkedList.class);
        mockedList.add("a");
        verify(mockedList).add("a");
        verify(mockedList).add("a");
    }

}
