package ru.yandex.practicum.calculators.types;

import ru.yandex.practicum.calculators.PriceCalculator;

public class Calculator {
    private final PriceCalculator calculator;
    private final String name;

    public Calculator(final PriceCalculator calculator, final String name) {
        this.calculator = calculator;
        this.name = name;
    }

    public PriceCalculator getCalculator() {
        return calculator;
    }

    public String getName() {
        return name;
    }
}
