package data_provider;

import dto.ContactLombok;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContactDataProviders {

    @DataProvider(name = "addNewContactDPFromFile")
    public Iterator<ContactLombok> addNewContactDPFromFile() {
        List<ContactLombok> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("data_provider/contacts.csv"),
                StandardCharsets.UTF_8))) {

            if (reader == null) {
                throw new FileNotFoundException("File contacts.csv not found in classpath");
            }

            String line = reader.readLine();
            while (line != null) {
                String[] fields = line.split(",");
                list.add(ContactLombok.builder()
                        .name(fields[0].trim())
                        .lastName(fields[1].trim())
                        .phone(fields[2].trim())
                        .email(fields[3].trim())
                        .address(fields[4].trim())
                        .description(fields[5].trim())
                        .build());
                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read contacts.csv", e);
        }
        return list.iterator();
    }
}
