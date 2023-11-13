package com.ecom.dohsaround.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admins")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    @Column(unique = true)
    private String shopName;

    private boolean active;

    private String shopDesc;

    private String shopAddress;

    private String shopGmapURL;

    private String shopFbURL;

    private String shopInstaURL;

    private String shopPhone;
    
    private String deliveryLocation;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "admins_roles", joinColumns = @JoinColumn(name = "admin_id", referencedColumnName = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Collection<Role> roles;

    private String themeColor;
    private String shopCategory;
}
