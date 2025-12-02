package ru.yandex.practicum.exceptions;

public class IncorrectDiscountException extends Exception {
    public IncorrectDiscountException(final String message) {
        super(message);
    }
}
