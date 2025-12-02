package ru.yandex.practicum.calculators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.IncorrectDiscountException;
import ru.yandex.practicum.exceptions.IncorrectFinalPriceException;

public class DiscountCalculatorTests {

    @Test
    public void testDiscount100Price1000()
        throws IncorrectFinalPriceException, IncorrectDiscountException {
        final PriceCalculator calculator = CalculatorsFactory.makeNewDiscount(100);
        final double finalPrice = calculator.calculateFinalPrice(1000);
        assertEquals(900, finalPrice);
    }

    @Test
    public void testDiscount200Price2000()
        throws IncorrectFinalPriceException, IncorrectDiscountException {
        final PriceCalculator calculator = CalculatorsFactory.makeNewDiscount(200);
        final double finalPrice = calculator.calculateFinalPrice(2000);
        assertEquals(1800, finalPrice);
    }

    @Test
    public void testDiscount200Price100() throws IncorrectDiscountException {
        final PriceCalculator calculator = CalculatorsFactory.makeNewDiscount(200);
        assertThrows(IncorrectFinalPriceException.class, () -> calculator.calculateFinalPrice(100));
    }

    @Test
    public void testDiscount200Price0() throws IncorrectDiscountException {
        final PriceCalculator calculator = CalculatorsFactory.makeNewDiscount(200);
        assertThrows(IncorrectFinalPriceException.class, () -> calculator.calculateFinalPrice(0));
    }

    @Test
    public void testDiscount200PriceMinus100() throws IncorrectDiscountException {
        final PriceCalculator calculator = CalculatorsFactory.makeNewDiscount(200);
        assertThrows(
            IncorrectFinalPriceException.class, () -> calculator.calculateFinalPrice(-100)
        );
    }

    @Test
    public void testDiscount0() {
        assertThrows(IncorrectDiscountException.class, () -> CalculatorsFactory.makeNewDiscount(0));
    }

    @Test
    public void testDiscountMinus100() {
        assertThrows(
            IncorrectDiscountException.class, () -> CalculatorsFactory.makeNewDiscount(-100)
        );
    }

    @Test
    public void testDiscountOverMax() {
        assertThrows(
            IncorrectDiscountException.class,
            () -> CalculatorsFactory.makeNewDiscount(DiscountCalculator.MAX_DISCOUNT + 1)
        );
    }

    @Test
    @Disabled
    public void testDiscountMax() {
        assertThrows(
            IncorrectDiscountException.class,
            () -> CalculatorsFactory.makeNewDiscount(DiscountCalculator.MAX_DISCOUNT)
        );
    }
}
