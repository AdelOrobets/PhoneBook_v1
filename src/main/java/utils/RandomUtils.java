package utils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    private static final SecureRandom random = new SecureRandom();

    // firstName
    private static final List<String> FIRST_NAMES = List.of(
            "Alice", "Shai", "Aleksey", "Diana", "Eitan", "Moshe",
            "George", "Alex", "Ivan", "Julia", "Doris", "Adel",
            "Mike", "Nina", "David", "Hana", "Maria", "Noa");

    // lastName
    private static final List<String> LAST_NAMES = List.of(
            "Cohen", "Levi", "Mizrahi", "Avraham", "Biton", "Peretz"
            , "BenDavid", "Malka", "Azoulay", "Elbaz", "Sharabi", "Dayan",
            "Mor", "Haim", "Zohar", "Halimi", "Alon", "Nahum");

    // address
    private static final List<String> ADDRESSES = List.of(
            "123 Main Street, Tel Aviv",
            "45 Herzl Blvd, Jerusalem",
            "78 Rothschild Ave, Haifa",
            "12 Carmel Street, Ramat Gan",
            "89 Dizengoff St, Netanya",
            "34 Ben Yehuda St, Beer Sheva",
            "56 Begin Rd, Holon",
            "17 Hillel Street, Petah Tikva",
            "9 Weizmann St, Ashdod",
            "25 Kaplan St, Kfar Saba",
            "101 Trumpeldor St, Rehovot",
            "3 HaHashmonaim St, Bat Yam",
            "66 Arlozorov St, Modi'in",
            "5 King George St, Herzliya",
            "77 Bialik St, Rishon LeZion"
    );

    public static String generateFirstNameFromList() {
        return FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
    }

    public static String generateLastNameFromList() {
        return LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
    }

    public static String generateAddressList() {
        return ADDRESSES.get(random.nextInt(ADDRESSES.size()));
    }

    // Generates valid random email
    public static String generateEmail(int length) {
        return generateRandomString(length,
                "abcdefghijklmnopqrstuvwxyz0123456789") + "@gmail.com";
    }

    // Generates invalid random email
    public static String generateInvalidEmailNoAtSymbol(int length) {
        return generateRandomString(length,
                "abcdefghijklmnopqrstuvwxyz0123456789") + "gmail.com";
    }

    public static String generateInvalidEmailNoDomain(int length) {
        return generateRandomString(length,
                "abcdefghijklmnopqrstuvwxyz0123456789") + "@gmail";
    }

    // Generates valid random password
    public static String generatePassword(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "@$#^&*!";

        String allChars = upper + lower + digits + special;
        StringBuilder password = new StringBuilder();

        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        return password.toString();
    }

    // Generates invalid random password
    public static String generatePasswordInvalidNoSymbol(int length) {
        return generateRandomString(length,
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
    }

    public static String generatePasswordInvalidNoDigit(int length) {
        return generateRandomString(length,
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@$#^&*!");
    }

    private static String generateRandomString(int length, String characters) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public static String generatePhoneNumber() {
        Random random = new Random();
        int[] prefixes = {50, 52, 53, 54, 55};
        int prefix = prefixes[random.nextInt(prefixes.length)];
        int number = 1000000 + random.nextInt(9000000);
        return String.format("+972%d%07d", prefix, number);
    }

}
