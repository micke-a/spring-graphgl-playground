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
        return null;
    }
}
