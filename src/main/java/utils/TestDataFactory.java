package utils;

import dto.ContactLombok;
import dto.UserLombok;

public class TestDataFactory {

    // USERS
    public static UserLombok validUser() {
        return new UserLombok(RandomUtils.generateEmail(8),
                RandomUtils.generatePassword(10));
    }

    private static UserLombok.UserLombokBuilder baseUser() {
        return UserLombok.builder()
                .username(RandomUtils.generateEmail(8))
                .password(RandomUtils.generatePassword(10));
    }

    public static UserLombok userWithoutEmail() {
        return baseUser().username("").build();
    }

    public static UserLombok userWithoutPassword() {
        return baseUser().password("").build();
    }

    public static UserLombok invalidEmailNoAtSymbol() {
        return baseUser().username(RandomUtils.generateInvalidEmailNoAtSymbol(10)).build();
    }

    public static UserLombok invalidEmailNoDomain() {
        return baseUser().username(RandomUtils.generateInvalidEmailNoDomain(10)).build();
    }

    public static UserLombok invalidEmailWithSpace() {
        return baseUser().username(RandomUtils.generateEmail(4) + " " + RandomUtils.
                generateEmail(4)).build();
    }

    public static UserLombok invalidPasswordTooShort() {
        return baseUser().password(RandomUtils.generatePassword(1)).build();
    }

    public static UserLombok invalidPasswordTooLong() {
        return baseUser().password(RandomUtils.generatePassword(16)).build();
    }

    public static UserLombok invalidPasswordNoDigit() {
        return baseUser().password(RandomUtils.generatePasswordInvalidNoDigit(10)).build();
    }

    public static UserLombok invalidPasswordNoSymbol() {
        return baseUser().password(RandomUtils.generatePasswordInvalidNoSymbol(10)).build();
    }

    // CONTACTS
    public static ContactLombok validContact() {
        return new ContactLombok(
                RandomUtils.generateFirstNameFromList(),
                RandomUtils.generateLastNameFromList(),
                RandomUtils.generatePhoneNumber(),
                RandomUtils.generateEmail(8),
                RandomUtils.generateAddressList(),
                RandomUtils.generateDescription()
        );
    }

    private static ContactLombok.ContactLombokBuilder baseContact() {
        return ContactLombok.builder()
                .name(RandomUtils.generateFirstNameFromList())
                .lastName(RandomUtils.generateLastNameFromList())
                .phone(RandomUtils.generatePhoneNumber())
                .email(RandomUtils.generateEmail(8))
                .address(RandomUtils.generateAddressList())
                .description(RandomUtils.generateDescription());
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
        return baseContact().name("").build();
    }

    public static ContactLombok invalidFieldWithoutLastName() {
        return baseContact().lastName("").build();
    }

    public static ContactLombok invalidFieldWithoutPhone() {
        return baseContact().phone("").build();
    }

    public static ContactLombok invalidEmailFormat() {
        return baseContact().email("invalidEmailFormat").build();
    }

    public static ContactLombok invalidPhoneFormat() {
        return baseContact().phone("123abc4561").build();
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
