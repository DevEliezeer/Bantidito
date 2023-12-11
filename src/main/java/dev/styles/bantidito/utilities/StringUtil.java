package dev.styles.bantidito.utilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {

    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String lowerCaseRest = input.substring(1).toLowerCase();
        return Character.toUpperCase(input.charAt(0)) + lowerCaseRest;
    }
}
