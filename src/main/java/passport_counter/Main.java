package passport_counter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static final Function<String, String> REPLACE_NEW_LINE_WITH_SPACE =  it -> it.replace("\n", " ");

    public static final String DATA_TO_RUN = DataSets.COMPLETE_DATASET;

    public static void main(String[] args) {
        List<Passport> passports = Stream.of(DATA_TO_RUN.split("\n\n"))
                .map(REPLACE_NEW_LINE_WITH_SPACE)
                .map(Main::parseString)
                .map(Passport::fromMap)
                .filter(Passport::isValidPassport)
                .collect(Collectors.toList());

        System.out.println("Number of valid passports: " + passports.size());
    }

    public static Map<String, String> parseString(String rawString) {
        return Stream.of(rawString.split(" "))
                .collect(Collectors.toMap(
                        Main::getKey,
                        Main::getValue));
    }

    public static String getKey(String rawKeyValue) {
        return rawKeyValue.split(":")[0];
    }

    public static String getValue(String rawKeyValue) {
        return rawKeyValue.split(":")[1];
    }
}
