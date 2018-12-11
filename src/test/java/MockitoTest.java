import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;

import static org.mockito.Mockito.verify;

/**
 * http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html
 *
 * @author hugh
 */
@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class MockitoTest {

    // https://stackoverflow.com/questions/1652692/using-mockito-to-mock-classes-with-generic-parameters
    @Mock
    private LinkedList<String> mockedList;

    @Test
    public void should_duplicated_verify_succeed() {
        mockedList.add("a");
        verify(mockedList).add("a");
        verify(mockedList).add("a");
    }

}
