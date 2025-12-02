package ru.yandex.practicum.calculators;

import ru.yandex.practicum.exceptions.IncorrectDiscountException;
import ru.yandex.practicum.exceptions.IncorrectFinalPriceException;

class SaleCalculator implements PriceCalculator {
    private final int discount;

    public SaleCalculator(final int discount) throws IncorrectDiscountException {
        if (discount <= 0 || discount >= 100) {
            throw new IncorrectDiscountException("Введена неверная скидка " + discount);
        }
        this.discount = discount;
    }

    @Override
    public double calculateFinalPrice(final double price) throws IncorrectFinalPriceException {
        if (price <= 0) {
            throw new IncorrectFinalPriceException("Начальная цена должна быть положительная");
        }
        final double priceBeforeCeil = price - price * ((double)discount / 100);
        return Math.ceil(priceBeforeCeil);
    }
}
