package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class ContactLombok {

    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ContactLombok that = (ContactLombok) o;
        return Objects.equals(name, that.name) && Objects.equals(lastName, that.lastName)
                && Objects.equals(phone, that.phone) && Objects.equals(email, that.email)
                && Objects.equals(address, that.address) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, phone, email, address, description);
    }
}

