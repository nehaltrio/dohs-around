package  com.ecom.dohsaround.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    @Size(min = 3, max = 15, message = "First name should have 3-15 characters")
    private String firstName;

    @Size(min = 3, max = 15, message = "Last name should have 3-15 characters")
    private String lastName;

    private String username;
    private boolean active;

    @Size(min = 5, max = 20, message = "Password should have 5-20 characters")
    private String password;

    private String phoneNumber;
    private String address;

    private String repeatPassword;
}
