import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class DataStructureTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * http://stackoverflow.com/questions/9343081/java-queues-why-poll-and-offer
     * offer() / poll() returns special value instead of throw exception
     */
    @Test
    public void priorityQueueTest() throws Exception {
        List<Integer> list = Arrays.asList(3, 1, 2, 5, 4);

        PriorityQueue<Integer> pq = new PriorityQueue<>(list);
        assertThat(pq.poll(), is(1));
        assertThat(pq.poll(), is(2));

        pq = new PriorityQueue<>(Collections.reverseOrder());
        pq.offer(1);
        pq.offer(2);
        pq.offer(3);
        assertThat(pq.poll(), is(3));
        assertThat(pq.poll(), is(2));
    }

    @Test
    public void stackTest() throws Exception {
        Stack<Integer> stack = new Stack<>();
        stack.add(0);
        stack.add(1);

        assertThat(stack.peek(), is(1));
        assertThat(stack.pop(), is(1));
        assertThat(stack.pop(), is(0));
        assertThat(stack.isEmpty(), is(true));
    }

    @Test
    public void queueTest() throws Exception {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        queue.add(1);

        assertThat(queue.poll(), is(0));
        assertThat(queue.poll(), is(1));
        assertThat(queue.poll(), is(nullValue()));

        exception.expect(NoSuchElementException.class);
        queue.remove();
    }

    @Test
    public void sortedSetTest() throws Exception {
        SortedSet<Integer> sortedInts = new TreeSet<>();
        sortedInts.add(1);
        sortedInts.add(3);
        sortedInts.add(2);

        sortedInts.forEach(System.out::println);
    }
}
