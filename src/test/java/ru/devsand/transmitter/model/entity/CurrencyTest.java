package ru.devsand.transmitter.model.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class CurrencyTest {

    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"GBP", true},
                {"RUR", false},
                {"RUB", true},
                {"USD", true},
                {"usd", false},
                {"EUR", true},
                {"ABC", false},
        });
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @Parameterized.Parameter(value = 0)
    public String currency;

    @Parameterized.Parameter(value = 1)
    public boolean veracity;

    @Test
    public void checkContains() {
        assertThat(Currency.contains(currency), is(veracity));
    }


}