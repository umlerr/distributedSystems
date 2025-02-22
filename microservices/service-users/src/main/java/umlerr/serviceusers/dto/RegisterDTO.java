package umlerr.serviceusers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class RegisterDTO {
    private String name;
    private String email;
    private String phone;
    private String password;
}
