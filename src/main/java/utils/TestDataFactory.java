package utils;

import dto.UserLombok;

public class TestDataFactory {

    public static UserLombok validUser() {
        return new UserLombok(
                RandomUtils.generateFirstNameFromList(),
                RandomUtils.generateLastNameFromList(),
                RandomUtils.generateEmail(8),
                RandomUtils.generatePassword(10)
        );
    }

    public static UserLombok userWithoutEmail() {
        return UserLombok.builder()
                .firstName(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .username("") // ❌
                .password(RandomUtils.generatePassword(10))
                .build();
    }

    public static UserLombok userWithoutPassword() {
        return UserLombok.builder()
                .firstName(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .username(RandomUtils.generateEmail(8))
                .password("") // ❌
                .build();
    }

    public static UserLombok invalidEmailNoAtSymbol() {
        return UserLombok.builder()
                .firstName(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .username(RandomUtils.generateInvalidEmailNoAtSymbol(10)) // ❌
                .password(RandomUtils.generatePassword(10))
                .build();
    }

    public static UserLombok invalidEmailNoDomain() {
        return UserLombok.builder()
                .firstName(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .username(RandomUtils.generateInvalidEmailNoDomain(10)) // ❌
                .password(RandomUtils.generatePassword(10))
                .build();
    }

    public static UserLombok invalidEmailWithSpace() {
        return UserLombok.builder()
                .firstName(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .username(RandomUtils.generateEmail(4) + " " + RandomUtils.generateEmail(4)) // ❌
                .password(RandomUtils.generatePassword(10))
                .build();
    }

    public static UserLombok invalidPasswordTooShort() {
        return UserLombok.builder()
                .firstName(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .username(RandomUtils.generateEmail(8))
                .password(RandomUtils.generatePassword(1)) // ❌
                .build();
    }

    public static UserLombok invalidPasswordTooLong() {
        return UserLombok.builder()
                .firstName(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .username(RandomUtils.generateEmail(8))
                .password(RandomUtils.generatePassword(16)) // ❌
                .build();
    }

    public static UserLombok invalidPasswordNoDigit() {
        return UserLombok.builder()
                .firstName(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .username(RandomUtils.generateEmail(8))
                .password(RandomUtils.generatePasswordInvalidNoDigit(10)) // ❌
                .build();
    }

    public static UserLombok invalidPasswordNoSymbol() {
        return UserLombok.builder()
                .firstName(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .username(RandomUtils.generateEmail(8))
                .password(RandomUtils.generatePasswordInvalidNoSymbol(10)) // ❌
                .build();
    }
}
