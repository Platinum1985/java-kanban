package ru.yandex.practicum.calculators;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.IncorrectDiscountException;
import ru.yandex.practicum.exceptions.IncorrectFinalPriceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SaleCalculatorTests {

    @Test
    public void testSale10Price100()
        throws IncorrectDiscountException, IncorrectFinalPriceException {
        final PriceCalculator calculator = CalculatorsFactory.makeNewSale(10);
        final double finalPrice = calculator.calculateFinalPrice(100);
        assertEquals(90, finalPrice);
    }

    @Test
    public void testSale20Price10000()
        throws IncorrectDiscountException, IncorrectFinalPriceException {
        final PriceCalculator calculator = CalculatorsFactory.makeNewDiscount(20);
        final double finalPrice = calculator.calculateFinalPrice(10000);
        assertEquals(8000, finalPrice);
    }

    @Test
    public void testSale30Price10000()
        throws IncorrectDiscountException, IncorrectFinalPriceException {
        final PriceCalculator calculator = CalculatorsFactory.makeNewSale(30);
        final double finalPrice = calculator.calculateFinalPrice(10000);
        assertEquals(7000, finalPrice);
    }

    @Test
    public void testSale10Price0() throws IncorrectDiscountException {
        final PriceCalculator calculator = CalculatorsFactory.makeNewSale(10);
        assertThrows(IncorrectFinalPriceException.class, () -> calculator.calculateFinalPrice(0));
    }

    @Test
    public void testSale10PriceMinus100() throws IncorrectDiscountException {
        final PriceCalculator calculator = CalculatorsFactory.makeNewSale(10);
        assertThrows(
            IncorrectFinalPriceException.class, () -> calculator.calculateFinalPrice(-100)
        );
    }

    @Test
    public void testSale0() {
        assertThrows(IncorrectDiscountException.class, () -> CalculatorsFactory.makeNewSale(0));
    }
}
