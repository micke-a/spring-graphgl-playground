package me.mikael.graphqlstuff.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static me.mikael.graphqlstuff.util.NameOnCardFormatter.Format.*;
import static org.assertj.core.api.Assertions.assertThat;

class NameOnCardFormatterTest {

    record FormatCase(String firstName, String lastName, NameOnCardFormatter.Format format, String expected) {}

    static Stream<FormatCase> formatCases() {
        return Stream.of(
                new FormatCase("John", "Doe", FIRST_NAME, "John"),
                new FormatCase("John", "Doe", LAST_NAME, "Doe"),
                new FormatCase("John", "Doe", F1_LAST, "J Doe"),
                new FormatCase("John", "Doe", FIRST_L1, "John D")
        );
    }

    @ParameterizedTest
    @MethodSource("formatCases")
    void format(FormatCase formatCase) {
        String result = NameOnCardFormatter.format(formatCase.firstName(), formatCase.lastName(), formatCase.format());
        assertThat(result).isEqualTo(formatCase.expected());
    }
}
