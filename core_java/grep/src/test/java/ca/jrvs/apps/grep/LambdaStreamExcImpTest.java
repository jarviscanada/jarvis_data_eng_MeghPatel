package ca.jrvs.apps.grep;

import ca.jrvs.apps.practice.LambdaStreamExc;
import ca.jrvs.apps.practice.LambdaStreamExcImp;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.*;

import static org.junit.Assert.*;

public class LambdaStreamExcImpTest {

    private LambdaStreamExc lse;

    @Before
    public void setup() {
        lse = new LambdaStreamExcImp();
    }

    // -----------------------------
    // createStrStream
    // -----------------------------
    @Test
    public void testCreateStrStream() {
        List<String> result =
                lse.createStrStream("a","b","c").collect(Collectors.toList());

        assertEquals(Arrays.asList("a","b","c"), result);
    }

    // -----------------------------
    // toUpperCase
    // -----------------------------
    @Test
    public void testToUpperCase() {
        List<String> result =
                lse.toUpperCase("a","b").collect(Collectors.toList());

        assertEquals(Arrays.asList("A","B"), result);
    }

    // -----------------------------
    // filter
    // -----------------------------
    @Test
    public void testFilter() {
        Stream<String> s = Stream.of("apple","bat","cat");

        List<String> result =
                lse.filter(s, "a").collect(Collectors.toList());

        assertEquals(Collections.emptyList(), result);
    }

    // -----------------------------
    // createIntStream
    // -----------------------------
    @Test
    public void testCreateIntStreamArray() {
        int[] arr = {1,2,3};

        List<Integer> result =
                lse.toList(lse.createIntStream(arr));

        assertEquals(Arrays.asList(1,2,3), result);
    }

    // -----------------------------
    // range stream
    // -----------------------------
    @Test
    public void testCreateIntStreamRange() {
        List<Integer> result =
                lse.toList(lse.createIntStream(1,3));

        assertEquals(Arrays.asList(1,2,3), result);
    }

    // -----------------------------
    // square root
    // -----------------------------
    @Test
    public void testSquareRootIntStream() {
        double[] result =
                lse.squareRootIntStream(IntStream.of(4,9)).toArray();

        assertArrayEquals(new double[]{2.0,3.0}, result, 0.001);
    }

    // -----------------------------
    // getOdd
    // -----------------------------
    @Test
    public void testGetOdd() {
        List<Integer> result =
                lse.toList(lse.getOdd(IntStream.rangeClosed(1,5)));

        assertEquals(Arrays.asList(1,3,5), result);
    }

    // -----------------------------
    // flatNestedInt
    // -----------------------------
    @Test
    public void testFlatNestedInt() {
        Stream<List<Integer>> input =
                Stream.of(
                        Arrays.asList(1,2),
                        Arrays.asList(3)
                );

        List<Integer> result =
                lse.flatNestedInt(input).collect(Collectors.toList());

        assertEquals(Arrays.asList(1,4,9), result);
    }
}
