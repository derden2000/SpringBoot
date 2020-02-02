package pro.antonshu.market.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordDto {

    private String password;
    private String newPassword;
}
