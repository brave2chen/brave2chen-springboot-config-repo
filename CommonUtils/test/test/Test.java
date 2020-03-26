package test;

import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Test {

    public static void main(String[] args) throws Exception {
        Consumer<Object> println = System.out::println;
        println.accept("accept");

        @SuppressWarnings("deprecation")
        Supplier<String> date = () -> new Date().toLocaleString();
        println.accept(date.get());

        Predicate<Integer> eqZero = (i) -> i == 0;
        println.accept(eqZero.test(0));

        Function<String, String> greeting = (name) -> "Hello " + name;
        println.accept(greeting.apply("Brave"));

        BiConsumer<Object, Consumer<Object>> printProxy = (s, c) -> {
            c.accept("BiConsumer:start");
            c.accept(s);
            c.accept("BiConsumer:end");
        };
        printProxy.accept("BiConsumer", println);

        BinaryOperator<Integer> add = (o1, o2) -> o1 + o2;
        println.andThen((o) -> println.accept("3 + 6 = " + o)).andThen(println).accept(add.apply(3, 6));
    }

}
