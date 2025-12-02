package ru.yandex.practicum;

import ru.yandex.practicum.calculators.CalculatorsLoader;
import ru.yandex.practicum.calculators.types.Calculator;
import ru.yandex.practicum.exceptions.IncorrectFinalPriceException;

import java.util.List;
import java.util.Scanner;

public class App {
    final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        final CalculatorsLoader loader = new CalculatorsLoader();
        do {
            printMenu(loader.getCalculators());
            final String action = scanner.nextLine();
            if ("exit".equals(action)) {
                break;
            }
            processAction(action, loader.getCalculators());
        } while (true);
    }

    public static void processAction(final String action, final List<Calculator> calculators) {
        try {
            final int position = Integer.parseInt(action);
            if (position < 1 || position + 1 > calculators.size()) {
                System.out.println("Введена неверная позиция.");
            } else {
                calculatePrice(calculators.get(position));
            }
        } catch (NumberFormatException exception) {
            System.out.println("Ввведено не целое число.");
        }
    }

    public static void calculatePrice(final Calculator calculator) {
        System.out.print("Введите цену => ");
        final String rawPrice = scanner.nextLine();
        try {
            final double price = Double.parseDouble(rawPrice);
            final double finalPrice = calculator.getCalculator().calculateFinalPrice(price);
            System.out.println("Финальная цена = " + finalPrice);
        } catch (NumberFormatException exception) {
            System.out.println("Введена неверная цена");
        } catch (IncorrectFinalPriceException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void printMenu(final List<Calculator> calculators) {
        for (int position = 0; position < calculators.size(); position++) {
            final Calculator calculator = calculators.get(position);
            System.out.println((position + 1) + " - " + calculator.getName());
        }
        System.out.print("Введите пункт действия или exit => ");
    }
}
