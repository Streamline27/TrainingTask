package passport_counter;

import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Value
@Builder
public class Passport {

    private final static Pattern COLOR_REGEX = Pattern.compile("^[a-f0-9]{6}$");
    private final static Set<String> ALLOWED_EYE_COLORS = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    String birthYear;
    String issueYear;
    String expirationYear;
    String height;
    String hairColor;
    String eyeColor;
    String passportID;
    String countryID;

    public static Passport fromMap(Map<String, String> map) {
        return Passport.builder()
                .birthYear(map.get("byr"))
                .issueYear(map.get("iyr"))
                .expirationYear(map.get("eyr"))
                .height(map.get("hgt"))
                .hairColor(map.get("hcl"))
                .eyeColor(map.get("ecl"))
                .passportID(map.get("pid"))
                .countryID(map.get("cid"))
                .build();
    }

    public boolean isValidPassport() {
        return (isNorthPolePassport() || containsAllFields()) && hasValidFields();
    }

    public boolean containsAllFields() {
        return birthYear != null &&
                issueYear != null &&
                expirationYear != null &&
                height != null &&
                hairColor != null &&
                eyeColor != null &&
                passportID != null &&
                countryID != null;
    }

    private boolean isNorthPolePassport() {
        return birthYear != null &&
                issueYear != null &&
                expirationYear != null &&
                height != null &&
                hairColor != null &&
                eyeColor != null &&
                passportID != null &&
                countryID == null;
    }

    public boolean hasValidFields() {
        return isBirthYearValid() &&
                isIssueYearValid() &&
                isExpirationYearValid() &&
                isHeightValid() &&
                isHairColorValid() &&
                isEyeColorValid() &&
                isPassportIDValid();
    }

    private boolean isBirthYearValid() {
        return isNumberBetween(birthYear, 1920, 2002);
    }

    private boolean isIssueYearValid() {
        return isNumberBetween(issueYear, 2010, 2020);
    }

    private boolean isExpirationYearValid() {
        return isNumberBetween(expirationYear, 2020, 2030);
    }

    private boolean isHeightValid() {
        boolean containsUnits = height.contains("cm") ^ height.contains("in"); // Only one of them
        if (containsUnits) {
            if (height.contains("cm")) {
                return heightIsOfMetricBetween("cm", 150, 193);
            }
            if (height.contains("in")) {
                return heightIsOfMetricBetween("in", 59, 76);
            }
        }
        return false;
    }

    private boolean heightIsOfMetricBetween(String metric, int from, int to) {
        return isNumberBetween(height.substring(0, height.indexOf(metric)), from, to);
    }

    private boolean isHairColorValid() {
        return hairColor.length() == 7 &&
                hairColor.startsWith("#") &&
                COLOR_REGEX.matcher(hairColor.substring(1, 7)).matches();
    }

    private boolean isEyeColorValid() {
        return ALLOWED_EYE_COLORS.contains(eyeColor);
    }

    private boolean isPassportIDValid() {
        return passportID.length() == 9 && isNumber(passportID);
    }

    private boolean isNumberBetween(String numberString, int from, int to) {
        try {
            int number = Integer.parseInt(numberString);
            return number >= from && number <= to;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isNumber(String numberString) {
        try {
            Integer.parseInt(numberString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
