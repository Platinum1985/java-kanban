package ru.yandex.practicum.calculators;

import ru.yandex.practicum.exceptions.IncorrectFinalPriceException;

public interface PriceCalculator {
    double calculateFinalPrice(double price) throws IncorrectFinalPriceException;
}
