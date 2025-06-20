package utils;

import dto.ContactLombok;
import dto.UserLombok;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDataFactory {

    private static final Logger logger = LoggerFactory.getLogger(TestDataFactory.class);

    public static UserLombok validUser() {
        String email = RandomUtils.generateEmail(8);
        String password = RandomUtils.generatePassword(10);

        logger.info("Generating valid user:");
        logger.info("Email: {}", email);
        logger.info("Password: {}", password);
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

    public static ContactLombok invalidContactAllFieldsEmpty() {
        return ContactLombok.builder()
                .name("")
                .lastName("")
                .phone("")
                .email("")
                .address("")
                .description("")
                .build();
    }
}
