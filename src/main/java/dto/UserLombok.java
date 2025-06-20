package dto;

import lombok.*;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserLombok {

    private String username;
    private String password;
}