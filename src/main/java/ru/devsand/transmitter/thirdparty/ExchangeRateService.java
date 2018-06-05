package ru.devsand.transmitter.thirdparty;

import ru.devsand.transmitter.model.entity.Currency;

import java.math.BigDecimal;

public interface ExchangeRateService {

    BigDecimal obtainRate(Currency from, Currency to);

}
