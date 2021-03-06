package RecurrenceEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RecurrenceEvaluatorStreamTest {
    @org.junit.Test
    public void StreamLimitTest() {
        RecurrenceEvaluator solver = new RecurrenceEvaluator();
        solver.setRecurrence(new Double[]{1.0, 1.0}).setInitialValues(new Double[]{1.0, 1.0});

        List<Double> result = new ArrayList<>();
        List<Double> expected = Arrays.asList(1.0, 1.0, 2.0, 3.0, 5.0, 8.0, 13.0, 21.0, 34.0, 55.0);
        solver.stream().limit(10).forEach(result::add);
        solver.stream().skip(1000).limit(20).forEach(System.out::println);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void StreamSkipLimitTest() {
        RecurrenceEvaluator solver = new RecurrenceEvaluator();
        solver.setRecurrence(new Double[]{1.0, 1.0}).setInitialValues(new Double[]{1.0, 1.0});

        List<Double> result = new ArrayList<>();
        List<Double> expected = Arrays.asList(3.0, 5.0, 8.0, 13.0, 21.0, 34.0, 55.0);
        solver.stream().limit(7).skip(3).forEach(result::add);
        assertEquals(expected, result);

        result = new ArrayList<>();
        solver.stream().skip(3).limit(7).forEach(result::add);
        assertEquals(expected, result);

    }

    @org.junit.Test
    public void StreamTest() {
        RecurrenceEvaluator solver = new RecurrenceEvaluator();
        solver.setRecurrence(new Double[]{1.0, 1.0}).setInitialValues(new Double[]{1.0, 1.0});

        List<Double> result = new ArrayList<>();
        List<Double> expected = Arrays
                .asList(3.0, 21.0, 144.0, 987.0, 6765.0, 46368.0, 317811.0, 2178309.0, 1.4930352E7, 1.02334155E8, 7.01408733E8, 4.807526976E9);
        solver.stream().limit(50).filter(x -> x%3 == 0).forEach(result::add);
        assertEquals(expected, result);

        expected = Arrays.asList(21.0, 144.0, 987.0, 6765.0);
        result.clear();
        solver.stream().skip(4).filter(x -> x%3 == 0).limit(4).forEach(result::add);
        assertEquals(expected, result);

        assertTrue(solver.stream().anyMatch(x -> x==13.0));
    }

    @org.junit.Test
    public void SimpleStreamTest(){
        RecurrenceEvaluator solver = new RecurrenceEvaluator();
        solver.setRecurrence(new Double[]{-1.5, 2.0}).setInitialValues(new Double[]{1.0, 1.0});

        List<Integer> result = new ArrayList<>();
        List<Integer> expected = Arrays.asList(-43, -7, 0, 0, 1, 1, 1, 3, 18, 102);
        solver.stream().limit(10).sorted().map(Double::intValue).forEach(result::add);
        assertEquals(expected, result);

        result = new ArrayList<>();
        expected = Arrays.asList(0, 1, 1, 1, 3, 18, 102);
        solver.stream().limit(10).sorted().skip(3).map(Double::intValue).forEach(result::add);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void ParallelSortTest(){
        RecurrenceEvaluator solver = new RecurrenceEvaluator();
        solver.setRecurrence(new Double[]{-0.5, 1.0}).setInitialValues(new Double[]{1.0, 1.0});

        List<Long> sequential = new ArrayList<>();
        List<Long> parallel = new ArrayList<>();
        solver.stream().limit(100).sorted().map(Double::longValue).forEach(sequential::add);
        solver.parallelStream().limit(100).sorted().map(Double::longValue).forEachOrdered(parallel::add);
        assertEquals(sequential, parallel);
    }

    @org.junit.Test
    public void ParallelAdvancedTest(){
        RecurrenceEvaluator solver = new RecurrenceEvaluator();
        solver.setRecurrence(new Double[]{-0.5, 1.0}).setInitialValues(new Double[]{1.0, 1.0});

        List<Long> sequential = new ArrayList<>();
        List<Long> parallel = new ArrayList<>();
        solver.stream().limit(100).sorted().skip(99).map(Double::longValue).forEachOrdered(sequential::add);
        solver.parallelStream().limit(100).sorted().skip(99).map(Double::longValue).forEachOrdered(parallel::add);
        assertEquals(sequential, parallel);

        Long result = solver.stream().limit(100).max(Double::compareTo).map(Double::longValue).get();
        assertEquals(result, parallel.get(parallel.size()-1));
    }
}