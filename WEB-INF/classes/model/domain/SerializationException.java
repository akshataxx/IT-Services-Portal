package model.domain;

public class SerializationException extends Exception {
    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable e) {
        super(message,e);
    }
}
