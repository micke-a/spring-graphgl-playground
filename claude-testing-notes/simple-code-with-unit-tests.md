## class

```java
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
```

## Prompt

```
Complete the NameOnCardFormatter.format method with rules as folows
FIRST_NAME return first name
LAST_NAME return last name
F1_LAST return first character or first name + space + the last name
FIRST_L1 return first name + space + first character of last name
Add unit test for the format method logic
```
