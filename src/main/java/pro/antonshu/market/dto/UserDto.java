package pro.antonshu.market.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String phone;
    private String firstName;
    private String lastName;
    private String email;

    public UserDto(Long id, String phone, String firstName, String lastName, String email) {
        this.id = id;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
