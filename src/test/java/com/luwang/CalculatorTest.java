package com.luwang;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {
    @Test
    public void testRemoveExpressionAllSpace() {
        Calculator calculator = new Calculator();
        assertEquals(9, calculator.calculate(" ( 1  + 2 )  *  2 + 3 "));
    }

    @Test
    public void test() {
        Calculator calculator = new Calculator();
        assertEquals(9, calculator.calculate("(1+2)*2+3"));
    }
}
