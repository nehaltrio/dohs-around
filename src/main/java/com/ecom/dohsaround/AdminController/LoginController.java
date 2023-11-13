package com.ecom.dohsaround.AdminController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom.dohsaround.dto.ShopDto;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.model.ShopCategories;
import com.ecom.dohsaround.repository.ShopRepository;
import com.ecom.dohsaround.service.impl.ShopServiceImpl;

import jakarta.validation.Valid;
import java.security.Principal;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private ShopServiceImpl adminService;

    @Autowired
    @Qualifier("passwordEncoderAdmin")
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ShopRepository shopRepository;

    @GetMapping("/adminlogin")
    public String loginForm(Model model) {
        model.addAttribute("title", "Login");
        return "adminlogin";
    }

    @RequestMapping("/index")
    public String home(Model model, Principal principal) {
        model.addAttribute("title", "Home Page");

        Shop shop = adminService.findByUsername(principal.getName());
        model.addAttribute("admin", shop);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/adminlogin";
        }
        return "index";
    }

    @GetMapping("/admin_register")
    public String register(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/adminlogin";
        }

        Shop shop = shopRepository.findByUsername(principal.getName());
        model.addAttribute("shopAdmin", shop);
        model.addAttribute("title", "Register");
        model.addAttribute("adminDto", new ShopDto());
        model.addAttribute("categories",
                Stream.of(ShopCategories.values())
                        .map(ShopCategories::toString)
                        .collect(Collectors.toList()));
        return "shopCreate";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot Password");
        return "forgot-password";
    }

    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto") ShopDto shopDto,
            BindingResult result, Principal principal,
            Model model) {

        Shop shopAdmin = shopRepository.findByUsername(principal.getName());
        model.addAttribute("shopAdmin", shopAdmin);
        model.addAttribute("categories",
                Stream.of(ShopCategories.values())
                        .map(ShopCategories::toString)
                        .collect(Collectors.toList()));
        String error = null;
        try {

            if (result.hasErrors()) {
                model.addAttribute("adminDto", shopDto);
                result.toString();
                return "shopCreate";
            }
            String username = shopDto.getUsername();
            Shop shop = adminService.findByUsername(username);
            model.addAttribute("adminDto", shopDto);

            if (shop != null) {
                model.addAttribute("adminDto", shopDto);
                System.out.println("shop not null");
                error = "This email is already in use!";
                model.addAttribute("error", error);
                return "shopCreate";
            }
            if (shopDto.getPassword().equals(shopDto.getRepeatPassword())) {
                shopDto.setPassword(passwordEncoder.encode(shopDto.getPassword()));
                adminService.save(shopDto);
                System.out.println("success");
               
                model.addAttribute("success", "Registered Successfully!");
                model.addAttribute("adminDto", shopDto);
            } else {
                model.addAttribute("adminDto", shopDto);
                error = "Password doesn't match! Check again!";
                model.addAttribute("error", error);
                System.out.println("password not same");
                return "shopCreate";
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "The server has been wrong!";
            model.addAttribute("error", error);
        }

        

        return "shopCreate";

    }

}
