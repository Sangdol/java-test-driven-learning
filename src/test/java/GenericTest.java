import com.google.common.collect.Lists;
import common.Person;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class GenericTest {

    /**
     * http://techblog.bozho.net/on-java-generics-and-erasure/
     * http://tutorials.jenkov.com/java-reflection/generics.html
     *
     * You cannot see on a type itself what type it is parameterized to a runtime,
     * but you can see it in fields and methods where it is used and parameterized.
     * Its concrete parameterizations in other words.
     */
    @Test
    public void erasureTest() throws Exception {
        ParameterizedType type = (ParameterizedType) Bar.class.getGenericSuperclass();
        assertThat(type.getActualTypeArguments()[0].toString(), is("class java.lang.String"));

        ParameterizedType fieldType = (ParameterizedType) Foo.class.getField("children").getGenericType();
        assertThat(fieldType.getActualTypeArguments()[0].toString(), is("class GenericTest$Bar"));

        ParameterizedType paramType = (ParameterizedType) Foo.class.getMethod("foo", List.class)
                        .getGenericParameterTypes()[0];
        assertThat(paramType.getActualTypeArguments()[0].toString(), is("class java.lang.String"));
        assertThat(Foo.class.getTypeParameters()[0].getBounds()[0].toString(), is("interface java.lang.CharSequence"));
    }

    @SuppressWarnings("unused") // only for reflection
    class Foo<E extends CharSequence> {
        public List<Bar> children = new ArrayList<>();
        public List<StringBuilder> foo(List<String> foo) {return null; }
        public void bar(List<? extends String> param) {}
    }

    class Bar extends Foo<String> {}

    /**
     * Producer Extends, Consumer Super (from the point of view of the collection)
     *
     * https://dzone.com/articles/covariance-and-contravariance
     * http://stackoverflow.com/questions/4343202/difference-between-super-t-and-extends-t-in-java
     */
    @Test
    public void pecsTest() throws Exception {
        // covariance - producer / cannot add a value
        List<? extends Number> numbers = Lists.newArrayList(1, 2, 3);
        assertThat(numbers.toString(), is("[1, 2, 3]"));

        // It's not allowed to add to avoid a runtime error.
        // numbers.add(1.1); // compile error

        numbers = Lists.newArrayList(1L, 2L, 3L);
        assertThat(numbers.toString(), is("[1, 2, 3]"));

        // contravariance - consumer / cannot get and assign a value
        List<? super Integer> is = new ArrayList<>();
        is.add(1);
        assertThat(is.toString(), is("[1]"));

        List<? super Integer> ns = new ArrayList<Number>();
        ns.add(1);
        assertThat(ns.toString(), is("[1]"));

        List<? super Integer> os = new ArrayList<Object>();
        os.add(1);
        assertThat(os.toString(), is("[1]"));
        assertThat(os.get(0), is(1)); // get is okay

        // Incompatible type
        // int a = os.get(0); // compiler error
    }

    /**
     * Heap Pollution
     * - https://docs.oracle.com/javase/tutorial/java/generics/nonReifiableVarargsType.html
     *
     * Why the name is Heap Pollution?
     * - because it pollutes the heap
     * - http://programmers.stackexchange.com/questions/155994/java-heap-pollution
     */
    @Test(expected = ClassCastException.class)
    public void heapPollutionTest() throws Exception {
        List<Integer> ints;
        Object obj = "abc";
        ints = (List<Integer>) obj;

        for (Integer anInt : ints) {
            System.out.println(anInt);
        }
    }

    /**
     * Heap Pollution
     * - https://docs.oracle.com/javase/tutorial/java/generics/nonReifiableVarargsType.html
     */
    @Test(expected = ClassCastException.class)
    public void varargsHeapPollutionTest() throws Exception {
        varargsMethod(Arrays.asList("abc"));
    }

    private void varargsMethod(List<String>... list) {
        Object[] objects = list;
        objects[0] = Arrays.asList(10);
        String s = list[0].get(0); // ClassCastException thrown here
        System.out.println(s);
    }

    /**
     * https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html
     */
    @Test
    public void creatingInstanceTest() throws Exception {
        List<String> list = new ArrayList<>();
        append(list, String.class);
        assertThat(list.size(), is(1));
    }

    /**
     * Cannot make a instance with Long as it doesn't have constructors.
     */
    @Test(expected = InstantiationException.class)
    public void creatingInstanceTest2() throws Exception {
        List<Long> list = new ArrayList<>();
        append(list, Long.class);
        assertThat(list.size(), is(1));
    }

    /**
     * Cannot Create Instances of Type Parameters.
     */
    private <E> void append(List<E> list) {
//        E elem = new E();   // compile-time error
//        list.add(elem);
    }

    /**
     * Creating an instance with a type parameter is possible through reflection.
     */
    private <E> void append(List<E> list, Class<E> cls) throws IllegalAccessException, InstantiationException {
        E elem = cls.newInstance();
        list.add(elem);
    }

    /**
     * https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#createStatic
     */
    @SuppressWarnings("unused")
    static class StaticGenericFieldNotPossible<T> {
        private T possible;
        /**
         * This is not possible as a static field is shared - the type of 'impossible' must be predefined.
         */
//        private static T impossible;
    }

    /**
     * RTTI - Runtime Type Information
     *
     * https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#cannotCast
     */
    @SuppressWarnings("unused")
    private static <E> void rtti(List<E> list) {
        /**
         * Compile-time error. It's not possible to know the type at runtime.
         */
//        if (list instanceof ArrayList<Integer>) { }

        /**
         * instanceof requires a reifiable type
         *
         * what is reifiable type?
         * - a type whose type information is fully available at runtime
         * - http://stackoverflow.com/questions/18848885/why-following-types-are-reifiable-non-reifiable-in-java
         */
        if (list instanceof ArrayList<?>) { }
    }

    /**
     * https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#cannotCast
     */
    @SuppressWarnings("unused")
    private void castingTest() {
        List<Integer> li = new ArrayList<>();

        // Compile-time error
//        List<Number> ln = (List<Number>) li;

        List<?> ln = li;
        LinkedList<Integer> la = (LinkedList<Integer>) li;
    }

    /**
     * https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#createArrays
     */
    @SuppressWarnings("unused")
    private void cannotCreateArrays() throws Exception {
        // compile-error
        // List<Integer>[] arrayOfLists = new List<Integer>[2];

        // can create an array with a reifiable type
        List<?>[] arrayOfLists = new List<?>[2];

        Object[] strings = new String[2];
        strings[0] = "hi";
        strings[1] = 100;  // throws an ArrayStoreException - the same reason
    }

    class NewPerson<T> extends Person {
        T hoho;
    }

    /**
     * Cannot extends Throwable
     * https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#cannotCatch
     */
//    class NewException<T> extends Throwable { }
    
    @Test
    public void extendsTest() throws Exception {
        NewPerson<Integer> intPerson = new NewPerson<>();
        intPerson.hoho = 1;
        assertThat(intPerson.hoho, is(1));
    }

    /**
     * Cannot overload
     * https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#cannotOverload
     */
    class Example {
        public void print(Set<String> set) {}
//        public void print(Set<Integer> set) {} // compile-error
    }
}

