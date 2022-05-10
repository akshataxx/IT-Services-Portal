package util;

public class Preconditions {

    public static void validateNotNull(Object obj) {
        validateNotNull(obj,null);
    }

    public static void validateNotNull(Object obj, String err) {
        if(obj==null)
            throw new IllegalArgumentException(err);
    }

    public static void validateLength(String parameter, int maxLength) {
        validateLength(parameter,0,maxLength);
    }

    public static void validateLength(String parameter, int minLength, int maxLength) {
        if(parameter==null) {
            if(minLength>0)
                throw new IllegalArgumentException("Parameter is missing");

            return;
        }
        checkArgument(parameter.length()>=minLength && parameter.length()<=maxLength,"Parameter '" + parameter + "' length is not between '"+minLength+"' and '"+maxLength+"'");
    }

    public static void checkArgument(boolean expression) {
        checkArgument(expression,null);
    }

    public static void checkArgument(boolean expression, String message) {
        if(!expression)
            throw new IllegalArgumentException(message);
    }
}
