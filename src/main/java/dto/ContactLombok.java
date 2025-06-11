package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ContactLombok {
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String description;
}