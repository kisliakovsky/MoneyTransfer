package ru.devsand.transmitter.model.entity;

public enum Currency {

    GBP, RUB, USD, EUR;

    public static boolean contains(String currency) {
        for (Currency c: Currency.values()) {
            if (c.name().equals(currency)) {
                return true;
            }
        }
        return false;
    }

}
