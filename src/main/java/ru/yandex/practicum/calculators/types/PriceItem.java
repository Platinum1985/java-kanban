package ru.yandex.practicum.calculators.types;

public class PriceItem {
    private String name;
    private int discount;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(final int discount) {
        this.discount = discount;
    }
}
