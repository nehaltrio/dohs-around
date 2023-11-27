package com.ecom.dohsaround.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.service.CustomerService;
import com.ecom.dohsaround.service.ShopService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AuthenticationSuccessHandlerClass implements AuthenticationSuccessHandler {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShopService shopService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            return;
        }

        response.sendRedirect(targetUrl);
    }

    private String determineTargetUrl(Authentication authentication) {
        
        String user = getUser(authentication);

        System.out.println(user);

        if ("CUSTOMER".equals(user)) {
            return "/products/0";
        } else if ("ADMIN".equals(user)) {
            return "/admin/dashboard";
        }

        return "/";
    }

    private String getUser(Authentication authentication) {

        String temp = null;
        String username = authentication.getName();

        Customer customer = customerService.findByUsername(username);
        Shop shop = shopService.findByUsername(username);

        if (customer != null) {
            temp = "CUSTOMER";
        }else if(shop != null){
            temp = "ADMIN";
        }

        return temp;
    }
}
