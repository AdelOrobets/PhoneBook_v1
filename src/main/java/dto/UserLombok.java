package dto;

import lombok.*;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserLombok {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
}