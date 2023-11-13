package com.ecom.dohsaround.CustomerController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.ecom.dohsaround.dto.CustomerDto;
import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.service.CustomerService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/customer")
public class AuthController implements AuthenticationFailureHandler {

    @Autowired
    private CustomerService customerService;

    @Autowired
    @Qualifier("passwordEncoderCustomer")
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/customerlogin", method = RequestMethod.GET)
    public String login() {

        return "customerlogin";
    }

    @GetMapping("/customer_register")
    public String register(Model model) {
        model.addAttribute("customerDto", new CustomerDto());
        return "customer_register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
            BindingResult result,
            Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("customerDto", customerDto);
                return "customer_register";
            }
            Customer customer = customerService.findByUsername(customerDto.getUsername());
            if (customer != null) {
                model.addAttribute("error", "Username have been registered");
                model.addAttribute("customerDto", customerDto);
                return "customer_register";
            }
            if (customerDto.getPassword().equals(customerDto.getRepeatPassword())) {
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                customerService.save(customerDto);
                model.addAttribute("success", "Register successfully");
                return "/customerlogin";
            } else {
                model.addAttribute("error", "Password is not same");
                model.addAttribute("customerDto", customerDto);
                return "customer_register";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Server have ran some problems");
            model.addAttribute("customerDto", customerDto);
        }
        return "customer_register";
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Invalid username or password";

        // Customize error message based on the type of exception if needed
        if (exception instanceof BadCredentialsException) {
            errorMessage = "Invalid username or password";
        } else if (exception instanceof DisabledException) {
            errorMessage = "Your account has been disabled";
        } else if (exception instanceof LockedException) {
            errorMessage = "Your account has been locked";
        } // Add more conditions as per your requirements

        request.getSession().setAttribute("error", errorMessage);
        response.sendRedirect("/customerlogin"); // Redirect back to the login page
    }

}
