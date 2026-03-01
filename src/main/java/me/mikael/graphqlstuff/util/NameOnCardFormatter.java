package me.mikael.graphqlstuff.util;

public class NameOnCardFormatter {

    enum Format {
        FIRST_NAME,
        LAST_NAME,
        F1_LAST,
        FIRST_L1
    }

    private NameOnCardFormatter() {}

    public static String format(String firstName, String lastName, Format format) {
        return switch (format) {
            case FIRST_NAME -> firstName;
            case LAST_NAME -> lastName;
            case F1_LAST -> firstName.charAt(0) + " " + lastName;
            case FIRST_L1 -> firstName + " " + lastName.charAt(0);
        };
    }
}
