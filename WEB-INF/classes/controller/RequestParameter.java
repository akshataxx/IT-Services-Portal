package controller;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.UUID;

public class RequestParameter {

    private final String value;


    public static final int DEFAULT_NUMBER = 0;

    public static final String DEFAULT_STRING = null;

    public static final boolean DEFAULT_BOOLEAN = false;

    public RequestParameter(String value) {
        this.value = value;
    }

    public Object asObject() {
        if(isNumber())
            return asNumber();

        if(isBoolean())
            return asBoolean();

        return value;
    }

    private boolean isNumber() {
        try {
            Number num = NumberFormat.getInstance().parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private Number asNumber() {
        try {
            return NumberFormat.getInstance().parse(value);
        } catch (ParseException e) {
            return DEFAULT_NUMBER;
        }
    }

    public boolean isObject() {
        return value != null;
    }

    public String asString() {
        return value;
    }

    public boolean isString() {
        return value != null;
    }

    public int asInt() {
        if(value==null)
            return DEFAULT_NUMBER;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return DEFAULT_NUMBER;
        }
    }

    public boolean isInt() {
        if(value==null) {
            return false;
        }

        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean asBoolean() {
        try {
            return Boolean.parseBoolean(value);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isBoolean() {
        try {
            return Boolean.parseBoolean(value);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public double asDouble() {
        if(value==null)
            return DEFAULT_NUMBER;

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return DEFAULT_NUMBER;
        }
    }

    public boolean isDouble() {
        if(value==null)
            return false;

        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isUniqueId() {
        if(value==null)
            return false;

        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public UUID asUniqueId() {
        if(value==null)
            return new UUID(0,0);

        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return new UUID(0,0);
        }
    }

    public long asLong() {
        if(value==null)
            return DEFAULT_NUMBER;

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return DEFAULT_NUMBER;
        }
    }

    public boolean isLong() {
        if(value==null)
            return false;

        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}