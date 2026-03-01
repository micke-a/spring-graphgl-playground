package me.mikael.graphqlstuff.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static me.mikael.graphqlstuff.util.NameOnCardFormatter.Format.*;
import static org.junit.jupiter.api.Assertions.*;

class NameOnCardFormatterTest {

    record FormatCase(String firstName, String lastName, NameOnCardFormatter.Format format, String expected) {}

    static Stream<FormatCase> happyPathCases() {
        return Stream.of(
            new FormatCase("John", "Doe", FIRST_NAME, "John"),
            new FormatCase("John", "Doe", LAST_NAME,  "Doe"),
            new FormatCase("John", "Doe", F1_LAST,    "J Doe"),
            new FormatCase("John", "Doe", FIRST_L1,   "John D")
        );
    }

    @ParameterizedTest
    @MethodSource("happyPathCases")
    void format_happyPath(FormatCase tc) {
        assertEquals(tc.expected(), NameOnCardFormatter.format(tc.firstName(), tc.lastName(), tc.format()));
    }

    @Test
    void format_nullFirstName_firstNameFormat_returnsNull() {
        assertNull(NameOnCardFormatter.format(null, "Doe", FIRST_NAME));
    }

    @Test
    void format_nullLastName_lastNameFormat_returnsNull() {
        assertNull(NameOnCardFormatter.format("John", null, LAST_NAME));
    }

    @Test
    void format_nullFirstName_f1LastFormat_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
            () -> NameOnCardFormatter.format(null, "Doe", F1_LAST));
    }

    @Test
    void format_nullLastName_firstL1Format_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
            () -> NameOnCardFormatter.format("John", null, FIRST_L1));
    }

    @Test
    void format_emptyFirstName_firstNameFormat_returnsEmpty() {
        assertEquals("", NameOnCardFormatter.format("", "Doe", FIRST_NAME));
    }

    @Test
    void format_emptyLastName_lastNameFormat_returnsEmpty() {
        assertEquals("", NameOnCardFormatter.format("John", "", LAST_NAME));
    }

    @Test
    void format_emptyFirstName_f1LastFormat_throwsStringIndexOutOfBoundsException() {
        assertThrows(StringIndexOutOfBoundsException.class,
            () -> NameOnCardFormatter.format("", "Doe", F1_LAST));
    }

    @Test
    void format_emptyLastName_firstL1Format_throwsStringIndexOutOfBoundsException() {
        assertThrows(StringIndexOutOfBoundsException.class,
            () -> NameOnCardFormatter.format("John", "", FIRST_L1));
    }
}
