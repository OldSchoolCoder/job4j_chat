package ru.job4j.exception_handling;

public class WrongIdException extends RuntimeException {
    public WrongIdException(String message) {
        super(message);
    }
}
