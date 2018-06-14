package ru.devsand.transmitter.thirdparty;

import ru.devsand.transmitter.model.entity.Currency;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

public class ExchangeRateServiceMock implements ExchangeRateService {

    private static final Map<Currency, BigDecimal> RATES = new EnumMap<>(Currency.class);

    static {
        RATES.put(Currency.GBP, BigDecimal.ONE);
        RATES.put(Currency.USD, BigDecimal.valueOf(1.3358));
        RATES.put(Currency.RUB, BigDecimal.valueOf(82.7248));
        RATES.put(Currency.EUR, BigDecimal.valueOf(1.142));
    }

    @Override
    public BigDecimal obtainRate(Currency from, Currency to) {
        if (from == to) {
            return BigDecimal.ONE;
        } else {
            BigDecimal fromRate = RATES.get(from);
            BigDecimal toRate = RATES.get(to);
            return toRate.divide(fromRate, BigDecimal.ROUND_CEILING);
        }
    }

}
