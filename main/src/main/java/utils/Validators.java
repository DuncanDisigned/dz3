package utils;



import java.util.Scanner;
import java.util.regex.Pattern;

public class Validators {
    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static String validateStringInput(Scanner scanner, String prompt, String errorMessage) {
        String input;
        while (true) {
            System.out.println(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty() && containsDigit(input)) {
                break;
            }
            System.out.println(errorMessage);
        }
        return input;
    }

    public static int validateIntegerInput(Scanner scanner, String prompt, String errorMessage) {
        int input;
        while (true) {
            System.out.println(prompt);
            String userInput = scanner.nextLine().trim();
            if (isNumeric(userInput)) {
                input = Integer.parseInt(userInput);
                break;
            }
            System.out.println(errorMessage);
        }
        return input;
    }

    public static float validateFloatInput(Scanner scanner, String prompt, String errorMessage) {
        float input;
        while (true) {
            System.out.println(prompt);
            String userInput = scanner.nextLine().trim();
            if (isNumeric(userInput)) {
                input = Float.parseFloat(userInput);
                break;
            }
            System.out.println(errorMessage);
        }
        return input;
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    private static boolean containsDigit(String str) {
        return str.matches("[a-zA-Z]+");
    }
}
