import spock.lang.Specification

/**
 * http://docs.groovy-lang.org/latest/html/documentation/
 *
 * @author hugh
 */
class GroovyTest extends Specification {

    def "dynamic method test"() {
        when:
        def methodMap = [
                getName: { "SH" },
                getAge: { 10 }
        ]
        then:
        methodMap.getName() == "SH"
        methodMap.getAge() == 10
    }

    /**
     * http://docs.groovy-lang.org/next/html/documentation/core-testing-guide.html
     */
    def "assert test"() {
        when:
        assert 1 == 2

        then:
        thrown(AssertionError)
    }

    def "casting test"() {
        expect:
        "1" == 1 as String
    }

    enum ABC {
        A, B, C
    }

    def "Map creation"() {
        when:
        def map = [name: 'sd', age: 34]
        def map2 = [1: 1, 2: 2]
        def map3 = [(ABC.A): "A"]

        then:
        map.name == 'sd'
        map.get("age") == 34
        map2.get(1) == 1
        map3.get(ABC.A) == "A"
    }

    def "Set creation"() {
        when:
        def list = [1, 2, 3]
        def set = [1, 2, 3] as Set

        then:
        list.toSet() == set
    }

    def "List creation"() {
        when:
        def arrayList = [1, 2, 3]
        def linkedList1 = [1, 2, 3] as LinkedList
        LinkedList linkedList2 = [1, 2, 3]

        then:
        arrayList instanceof ArrayList
        linkedList1 instanceof LinkedList
        linkedList2 instanceof LinkedList
    }

    /**
     * http://www.javaworld.com/article/2072503/ranges-in-groovy-are-hip.html
     */
    def "range"() {
        when:
        def list1 = (0..10)
        def list2 = (0..<10)

        then:
        list1.size() == 11
        list2.size() == 10

        list2.each {
            it < 10
        }
    }

    /**
     * http://javazquez.com/juan/2013/02/05/add-map-reduce-and-filter-to-groovy-with-groovy-extension-modules/
     */
    def "map reduce filter"() {
        when:
        def list = (0..<10)
        def doubled = list.collect({ it * 2 })
        def sum = list.inject({ acc, val -> acc + val })
        def odd = list.grep({ it % 2 == 1 })

        then:
        doubled.each {
            it % 2 == 0
        }

        sum == 45

        odd.each {
            it % 2 == 1
        }
    }

    class PrivateTest {
        private long n = 1;
    }

    /**
     * http://stackoverflow.com/questions/7852370/private-method-groovy-is-not-private
     */
    def "private is not private in groovy"() {
        given: "create an instance"
        def privateTest = new PrivateTest()

        when: "set to a private field"
        privateTest.n = 2

        then: "get the private field"
        privateTest.n == 2
    }

    /**
     * http://www.groovy-lang.org/syntax.html#all-strings
     */
    def "strings test"() {
        given:
        def singleQuotedString
        def tripleSingleQuotedString
        def doubleQuotedString
        def name = 'SH'

        when:
        singleQuotedString = 'a' + 'b'

        then:
        singleQuotedString == 'ab'

        when:
        singleQuotedString = 'a' + 1L

        then:
        singleQuotedString == 'a1'

        when:
        tripleSingleQuotedString = '''
        abc'''

        then:
        tripleSingleQuotedString == '\n        abc'

        when:
        tripleSingleQuotedString = '''\
abc'''

        then:
        tripleSingleQuotedString == 'abc'

        when:
        tripleSingleQuotedString = '''${name}'''

        then:
        tripleSingleQuotedString == '${name}'

        when:
        doubleQuotedString = "My name is ${name}."

        then:
        doubleQuotedString == "My name is SH."

        when:
        doubleQuotedString = "2 + 3 = ${2 + 3}"

        then:
        doubleQuotedString == "2 + 3 = 5"

        when:
        def person = [name: "SH", age: 33]
        doubleQuotedString = "$person.name is $person.age"

        then:
        doubleQuotedString == 'SH is 33'
    }
}
