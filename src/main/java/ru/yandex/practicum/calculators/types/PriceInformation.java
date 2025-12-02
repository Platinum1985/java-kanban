package ru.yandex.practicum.calculators.types;

import java.util.List;

public class PriceInformation {
    private List<PriceItem> sales;
    private List<PriceItem> discounts;

    public List<PriceItem> getSales() {
        return sales;
    }

    public void setSales(final List<PriceItem> sales) {
        this.sales = sales;
    }

    public List<PriceItem> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(final List<PriceItem> discounts) {
        this.discounts = discounts;
    }
}
