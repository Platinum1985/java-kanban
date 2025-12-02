package ru.yandex.practicum.calculators;

import ru.yandex.practicum.exceptions.IncorrectDiscountException;
import ru.yandex.practicum.exceptions.IncorrectFinalPriceException;

class DiscountCalculator implements PriceCalculator {

    public static final int MAX_DISCOUNT = 10000;
    private final int discount;

    public DiscountCalculator(final int discount) throws IncorrectDiscountException {
        if (discount <= 0 || discount > MAX_DISCOUNT) {
            throw new IncorrectDiscountException("Введена неверная скидка " + discount);
        }
        this.discount = discount;
    }

    @Override
    public double calculateFinalPrice(final double price) throws IncorrectFinalPriceException {
        if (price <= 0) {
            throw new IncorrectFinalPriceException("Начальная цена должна быть положительная");
        }
        final double priceAfterDiscount = price - discount;
        if (priceAfterDiscount <= 0) {
            throw new IncorrectFinalPriceException("Финальная цена должна оказаться больше 0");
        }
        return priceAfterDiscount;
    }
}
