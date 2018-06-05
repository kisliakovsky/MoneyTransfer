package ru.devsand.transmitter.thirdparty;

import org.hamcrest.number.BigDecimalCloseTo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.devsand.transmitter.model.entity.Currency;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.devsand.transmitter.model.entity.Currency.*;

@RunWith(Parameterized.class)
public class ExchangeRateServiceMockTest {

    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {GBP, GBP, BigDecimal.ONE},
                {GBP, USD, BigDecimal.valueOf(1.34)},
                {RUB, USD, BigDecimal.valueOf(0.016)},
                {USD, EUR, BigDecimal.valueOf(0.851)},
        });
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @Parameterized.Parameter(value = 0)
    public Currency currencyFrom;

    @Parameterized.Parameter(value = 1)
    public Currency currencyTo;

    @Parameterized.Parameter(value = 2)
    public BigDecimal rate;

    private static final ExchangeRateService SERVICE = new ExchangeRateServiceMock();

    @Test
    public void checkRate() {
        assertThat(SERVICE.obtainRate(currencyFrom, currencyTo), BigDecimalCloseTo.closeTo(rate, BigDecimal.valueOf(0.01)));
    }

}