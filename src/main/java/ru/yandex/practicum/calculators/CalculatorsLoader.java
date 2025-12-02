package ru.yandex.practicum.calculators;

import com.google.gson.Gson;
import ru.yandex.practicum.calculators.types.Calculator;
import ru.yandex.practicum.calculators.types.PriceInformation;
import ru.yandex.practicum.exceptions.IncorrectDiscountException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CalculatorsLoader {
    private final List<Calculator> calculators = new ArrayList<>();

    public  CalculatorsLoader() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream inputStream = loader.getResourceAsStream("prices-information.json");
        if (inputStream == null) {
            return;
        }
        final InputStreamReader streamReader = new InputStreamReader(
            inputStream, StandardCharsets.UTF_8
        );
        final Gson gson = new Gson();
        final PriceInformation information = gson.fromJson(streamReader, PriceInformation.class);

        information.getSales().forEach(sale -> {
            try {
                calculators.add(CalculatorsFactory.makeNewCalculator(
                    CalculatorsFactory.makeNewSale(sale.getDiscount()), sale.getName()
                ));
            } catch (IncorrectDiscountException exception) {
                System.out.println(
                    "Проигнорирована скидка " + sale.getName() + " " + exception.getMessage()
                );
            }
        });

        information.getDiscounts().forEach(discount -> {
            try {
                calculators.add(CalculatorsFactory.makeNewCalculator(
                    CalculatorsFactory.makeNewDiscount(discount.getDiscount()), discount.getName()
                ));
            } catch (IncorrectDiscountException exception) {
                System.out.println(
                    "Проигнорирована скидка " + discount.getName() + " " + exception.getMessage()
                );
            }
        });
    }

    public List<Calculator> getCalculators() {
        return calculators;
    }
}
