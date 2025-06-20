package dto;

import lombok.*;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
public class ContactLombok {

    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String description;
}

