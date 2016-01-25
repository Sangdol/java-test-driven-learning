import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * New documentation http://spockframework.github.io/spock/docs/1.0/index.html
 * Old documentation https://code.google.com/p/spock/wiki/SpockBasics
 *
 * Tutorials
 * - http://thejavatar.com/testing-with-spock/
 *   - have a lot of usages
 * - https://dzone.com/articles/introduction-spock
 *   - good example for adding description for `when`, `then`, etc
 *
 * @author hugh
 */
class SpockTest extends Specification {

    // http://spockframework.github.io/spock/docs/1.0/data_driven_testing.html#sharing-of-objects-between-iterations
    @Shared
    def d = 1;

    /**
     *  http://spockframework.github.io/spock/docs/1.0/data_driven_testing.html
     */
    def "only @Shared and static variables can be accessed from within a `where` block"() {
        expect:
        func(a) > func(b)

        where:
        a     | b
        d * 1 | d * 2
        d * 3 | d * 4
    }

    def func(n) {
        return -n;
    }

    def "computing the maximum of two numbers"() {
        expect:
        Math.max(a, b) == c

        where:
        a << [5, 3]
        b << [1, 9]
        c << [5, 9]
    }

    class Dao {
        public String findOne() {
            return "one";
        }
    }

    class Service {
        Dao dao;

        public Service(Dao dao) {
            this.dao = dao;
        }

        public String findOne() {
            return dao.findOne()
        }
    }

    def "don't put mocking and stubbing in different blocks"() {
        setup:
        def dao = Mock(Dao)
        def service = new Service(dao)
        dao.findOne() >> "one"

        when:
        def result = service.findOne()

        then:
        1 * dao.findOne()
        result == null // WTF!!! It's because `1 * dao.findOne()` already called that.
    }

    /**
     * https://spockframework.github.io/spock/docs/1.0/interaction_based_testing.html
     */
    def "mocking test"() {
        setup: // 'setup(or given)' is called only once if there were many `when/then` blocks
        def dao = Mock(Dao)
        def service = new Service(dao)

        when:
        dao.findOne() >> "two"

        then: // use multiple 'then' when designating the invocation order
        service.findOne() == "two"
    }

    def "mocking - chaining method responses test"() {
        setup:
        def dao = Mock(Dao)
        def service = new Service(dao)

        when: "multiple pairs of when/then won't work as expected"
        dao.findOne() >>> ["two", "three"]

        then:
        service.findOne() == "two"
        service.findOne() == "three"
    }

    def "multiple paris of when then test"() {
        setup:
        def dao = Mock(Dao)
        def service = new Service(dao)

        when:
        dao.findOne() >> "two"
        then:
        service.findOne() == "two"

        when:
        dao.findOne() >> "three" // It doesn't override the previous action.
        then:
        service.findOne() == "two"  // WTF!!! Because it doesn't call 'setup' for each when.
        service.findOne() == "two"
        service.findOne() == "two"
    }

    def "block inside then"() {
        setup:
        def dao = Mock(Dao)
        def service = new Service(dao)
        def b = true
        dao.findOne() >> "two"

        when:
        def result = service.findOne()

        then:
        if (b) {
            result == "three" // Not evaluating
            assert result == "two" //
        }
    }

    def "mocking - interaction check"() {
        setup:
        def dao = Mock(Dao)
        def service = new Service(dao)

        when:
        service.findOne()

        then:
        1 * dao.findOne()
    }

    /**
     * http://stackoverflow.com/questions/31475317/grails-unit-test-exception-java-lang-exception-no-tests-found-matching-grails-t
     */
    def "assert test"() {
        expect:
        Math.max(10, 1) == 10
    }

    def "empty run"() {
        expect:
        println Math.max(10, 1)
    }

    /**
     * http://spockframework.github.io/spock/docs/1.0/spock_primer.html
     */
    def "exception conditions"() {
        given:
        def stack = new Stack<Integer>()

        when:
        stack.pop()

        then:
        def e = thrown(EmptyStackException)
        e.cause == null
    }

    /**
     * http://spockframework.github.io/spock/docs/1.0/interaction_based_testing.html#_performing_side_effects
     */
    def "throw exception from mock"() {
        given:
        def stack = Mock(Stack)
        stack.pop() >> {throw new EmptyStackException()}

        when:
        stack.pop()

        then:
        thrown(EmptyStackException)
    }

    /**
     *
     * http://spockframework.github.io/spock/javadoc/1.0/spock/lang/Unroll.html
     *
     * Unroll make iterations of a data-driven feature visible separately
     */
    @Unroll
    def "#name should have length #length"() {
        expect:
        name.size() == length

        where:
        name << ["SH", "Spock", "Java"]
        length << [2, 5, 4]
    }

    def "No unroll no replacing - #name should have length #length"() {
        expect:
        name.size() == length

        where:
        name << ["SH"]
        length << [2]
    }

    static class ArgumentConstraint {
        public void method() {}
        public void method(String str) {}
        public void method(List<String> list) {}
    }

    /**
     * https://spockframework.github.io/spock/docs/1.0/interaction_based_testing.html#_argument_constraints
     */
    def "argument constraints test"() {
        given:
        def ac = Mock(ArgumentConstraint)

        when:
        ac.method("hello")
        then:
        1 * ac.method("hello")

        when:
        ac.method("world")
        then:
        1 * ac.method(!"hello")

        when:
        ac.method()
        then:
        1 * ac.method()
        0 * ac.method(_)

        when:
        ac.method("")
        then:
        1 * ac.method(_)

        when:
        ac.method([1])
        then:
        0 * ac.method(_)
        1 * ac.method(*_)

        when:
        ac.method("")
        then:
        1 * ac.method(_ as String)

        when:
        ac.method([1, 2])
        then:
        1 * ac.method({ it.size() > 1 })
    }
}
