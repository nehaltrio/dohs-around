package  com.ecom.dohsaround.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import jakarta.validation.constraints.Size;

@Data @NoArgsConstructor @AllArgsConstructor
public class ShopDto {
    private Long id;
    @Size(min = 3, max = 10, message = "Invalid first name!(3-10 characters)")
    private String firstName;
    @Size(min = 3, max = 10, message = "Invalid first name!(3-10 characters)")
    private String lastName;

    private String username;

    @Size(min = 5, max = 15, message = "Invalid password !(5-15 characters)")
    private String password;

    private String repeatPassword;

    private boolean active;

    private String shopDesc;

    private String shopAddress;

    private String shopGmapURL;

    private String shopFbURL;

    private String shopInstaURL;

    private String shopPhone;

    private String shopName;

    private String image;

    private String themeColor;

    private String shopCategory;

    private String deliveryLocation;

    
}
