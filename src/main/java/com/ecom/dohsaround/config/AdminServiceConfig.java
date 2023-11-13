package com.ecom.dohsaround.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.repository.ShopRepository;

import java.util.stream.Collectors;

public class AdminServiceConfig implements UserDetailsService {
    @Autowired
    private ShopRepository shopRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Shop shop = shopRepository.findByUsername(username);
        if(shop == null){
            throw new UsernameNotFoundException("Could not find username");
        }
        return new User(
                shop.getUsername(),
                shop.getPassword(),
                shop.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }
}
