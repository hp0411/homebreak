package uk.ac.sheffield.com2008.team41.helper;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Validation helper to check inputs
 * Credit: Team 41 Homebreak submission 2021
 */
public class ValidationHelper {
    // https://www.baeldung.com/java-email-validation-regex#regular-expression-by-rfc-5322-for-email-validation
    // Regex to check if valid email
    public static final String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    // https://stackoverflow.com/a/3802238
    // Regex to check if valid password
    public static final String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$";

    //https://mkyong.com/regular-expressions/how-to-validate-date-with-regular-expression/
    //Regex to check if valid date
    private static final String dateRegex =
            "^((?:19|20)[0-9][0-9])-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$";
    //Date regex pattern
    private static final Pattern pattern = Pattern.compile(dateRegex);

    //Checks if a string matches a pattern
    public static boolean patternMatches(String string, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(string)
                .matches();
    }

    /**
     * Returns if a valid date in string
     * @param date
     * @return
     */
    public static boolean isValid(String date) {
        boolean result = false;
        Matcher matcher = pattern.matcher(date);
        if (matcher.matches()) {
            result = true;
            int year = Integer.parseInt(matcher.group(1));
            String month = matcher.group(2);
            String day = matcher.group(3);
            if ((month.equals("4") || month.equals("6") || month.equals("9") ||
                    month.equals("04") || month.equals("06") || month.equals("09") ||
                    month.equals("11")) && day.equals("31")) {
                result = false;
            } else if (month.equals("2") || month.equals("02")) {
                if (day.equals("30") || day.equals("31")) {
                    result = false;
                } else if (day.equals("29")) {
                    if (!isLeapYear(year)) {
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Calculates if a year is a leap year
     * @param year
     * @return Predicate
     */
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    }
}

