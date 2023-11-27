package com.ecom.dohsaround.CustomerController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.ShoppingCart;
import com.ecom.dohsaround.service.CustomerService;

import java.security.Principal;


@Controller
public class AccountController {

    @Autowired
    private CustomerService customerService;


    @GetMapping("/account")
    public String accountHome(Model model , Principal principal){
        if(principal == null){
            return "redirect:/customerlogin";
        }
       
        model.addAttribute("principal", principal);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            ShoppingCart shoppingCart = customer.getShoppingCart();
            model.addAttribute("customer", customer);
            if (shoppingCart == null) {
                model.addAttribute("check", "No item in your cart");
            }

            model.addAttribute("shoppingCart", shoppingCart);

        }

        return "account_main";
    }

    @RequestMapping(value = "/update-infor", method = {RequestMethod.GET, RequestMethod.PUT})
    public String updateCustomer(
            @ModelAttribute("customer") Customer customer,
            Model model,
            RedirectAttributes redirectAttributes,
            Principal principal){
        if(principal == null){
            return "redirect:/customerlogin";
        }
        Customer customerSaved = customerService.saveInfor(customer);

        redirectAttributes.addFlashAttribute("customer", customerSaved);
        model.addAttribute("principal", principal);
       


        return "redirect:/account";
    }

  
    
}
