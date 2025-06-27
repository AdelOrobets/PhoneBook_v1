package utils;

import dto.ContactLombok;
import dto.UserLombok;

public class TestDataFactory {

    public static UserLombok validUser() {
        String email = RandomUtils.generateEmail(8);
        String password = RandomUtils.generatePassword(10);
        return new UserLombok(email, password);
    }

    public static UserLombok userWithoutEmail() {
        return UserLombok.builder()
                .username("") // ❌
                .password(RandomUtils.generatePassword(10))
                .build();
    }

    public static UserLombok userWithoutPassword() {
        return UserLombok.builder()
                .username(RandomUtils.generateEmail(8))
                .password("") // ❌
                .build();
    }

    public static UserLombok invalidEmailNoAtSymbol() {
        return UserLombok.builder()
                .username(RandomUtils.generateInvalidEmailNoAtSymbol(10)) // ❌
                .password(RandomUtils.generatePassword(10))
                .build();
    }

    public static UserLombok invalidEmailNoDomain() {
        return UserLombok.builder()
                .username(RandomUtils.generateInvalidEmailNoDomain(10)) // ❌
                .password(RandomUtils.generatePassword(10))
                .build();
    }

    public static UserLombok invalidEmailWithSpace() {
        return UserLombok.builder()
                .username(RandomUtils.generateEmail(4) + " " + RandomUtils.generateEmail(4)) // ❌
                .password(RandomUtils.generatePassword(10))
                .build();
    }

    public static UserLombok invalidPasswordTooShort() {
        return UserLombok.builder()
                .username(RandomUtils.generateEmail(8))
                .password(RandomUtils.generatePassword(1)) // ❌
                .build();
    }

    public static UserLombok invalidPasswordTooLong() {
        return UserLombok.builder()
                .username(RandomUtils.generateEmail(8))
                .password(RandomUtils.generatePassword(16)) // ❌
                .build();
    }

    public static UserLombok invalidPasswordNoDigit() {
        return UserLombok.builder()
                .username(RandomUtils.generateEmail(8))
                .password(RandomUtils.generatePasswordInvalidNoDigit(10)) // ❌
                .build();
    }

    public static UserLombok invalidPasswordNoSymbol() {
        return UserLombok.builder()
                .username(RandomUtils.generateEmail(8))
                .password(RandomUtils.generatePasswordInvalidNoSymbol(10)) // ❌
                .build();
    }

    // add new contact
    public static ContactLombok validContact() {
        return new ContactLombok(
                RandomUtils.generateFirstNameFromList(),
                RandomUtils.generateLastNameFromList(),
                RandomUtils.generatePhoneNumber(),
                RandomUtils.generateEmail(8),
                RandomUtils.generateAddressList(),
                RandomUtils.generateDescription());
    }

    public static ContactLombok allFieldsEmpty() {
        return ContactLombok.builder()
                .name("")
                .lastName("")
                .phone("")
                .email("")
                .address("")
                .description("")
                .build();
    }

    public static ContactLombok invalidFieldWithoutName() {
        return ContactLombok.builder()
                .name("") // ❌
                .lastName(RandomUtils.generateLastNameFromList())
                .phone(RandomUtils.generatePhoneNumber())
                .email(RandomUtils.generateEmail(8))
                .address(RandomUtils.generateAddressList())
                .description(RandomUtils.generateDescription())
                .build();
    }

    public static ContactLombok invalidFieldWithoutLastName() {
        return ContactLombok.builder()
                .name(RandomUtils.generateFirstNameFromList())
                .lastName("") // ❌
                .phone(RandomUtils.generatePhoneNumber())
                .email(RandomUtils.generateEmail(8))
                .address(RandomUtils.generateAddressList())
                .description(RandomUtils.generateDescription())
                .build();
    }

    public static ContactLombok invalidFieldWithoutPhone() {
        return ContactLombok.builder()
                .name(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .phone("") // ❌
                .email(RandomUtils.generateEmail(8))
                .address(RandomUtils.generateAddressList())
                .description(RandomUtils.generateDescription())
                .build();
    }

    public static ContactLombok invalidEmailFormat() {
        return ContactLombok.builder()
                .name(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .phone(RandomUtils.generatePhoneNumber())
                .email("invalidEmailFormat") // ❌
                .address(RandomUtils.generateAddressList())
                .description(RandomUtils.generateDescription())
                .build();
    }

    public static ContactLombok invalidPhoneFormat() {
        return ContactLombok.builder()
                .name(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .phone("123abc4561") // ❌
                .email(RandomUtils.generateEmail(8))
                .address(RandomUtils.generateAddressList())
                .description(RandomUtils.generateDescription())
                .build();
    }

    public static ContactLombok tooLongFields() {
        String longText = "A".repeat(300);
        return ContactLombok.builder()
                .name(longText)
                .lastName(longText)
                .phone("12345678901234567890")
                .email(longText + "@test.com")
                .address(longText)
                .description(longText)
                .build();
    }

    public static ContactLombok invalidFieldsWithSpecialCharacters() {
        return ContactLombok.builder()
                .name("@@@")
                .lastName("###")
                .phone("1234567!!")
                .email("test@@example.com")
                .address("!!! Address ???")
                .description("### Description ***")
                .build();
    }
}
