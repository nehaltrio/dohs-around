package com.ecom.dohsaround.CustomerController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.Product;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.model.ShoppingCart;
import com.ecom.dohsaround.repository.ShopRepository;
import com.ecom.dohsaround.service.CustomerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShopRepository shopRepository;

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String home(Model model, Principal principal, HttpSession session) {
        if (principal != null) {
            session.setAttribute("username", principal.getName());
            Customer customer = customerService.findByUsername(principal.getName());
            if (customer != null) {
                ShoppingCart cart = customer.getShoppingCart();


                if (cart != null) {
                    session.setAttribute("totalItems", cart.getTotalItems());
                } else {
                    session.setAttribute("totalItems", null);
                }
            } else {
                session.removeAttribute("username");
            }
        }
        return "index_main";
    }

    @GetMapping("/all_shop")
    public String allShop(Model model, Principal principal) {

        List<Shop> shops = shopRepository.findAllShops();

        // HashSet<Shop> shops = shopRepository.searchByShop("m");
        model.addAttribute("shops", shops);

        // List<Product> productList = shopRepository.searchByProduct("m");

        // model.addAttribute("products", productList);

        if (principal != null) {

            String username = principal.getName();
            ShoppingCart shoppingCart;

            Customer customer = customerService.findByUsername(username);

            if (customer != null) {
                shoppingCart = customer.getShoppingCart();
            } else {
                shoppingCart = null;
            }


            model.addAttribute("customer", customer);

            if (shoppingCart == null) {
                model.addAttribute("check", "No item in your cart");
            }

            model.addAttribute("shoppingCart", shoppingCart);

        }

        model.addAttribute("principal", principal);

        return "all_shops";
    }

    @GetMapping("/searchAll")
    public String marketplaceSeaarchResult(Model model, Principal principal, @RequestParam("keyword") String keyword) {

        HashSet<Shop> shops = shopRepository.searchByShop(keyword);
        model.addAttribute("shops", shops);

        List<Product> productList = shopRepository.searchByProduct(keyword);

        model.addAttribute("products", productList);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                ShoppingCart shoppingCart = customer.getShoppingCart();
                model.addAttribute("customer", customer);

                if (shoppingCart == null) {
                    model.addAttribute("check", "No item in your cart");
                }

                model.addAttribute("shoppingCart", shoppingCart);
            }
        }

        model.addAttribute("principal", principal);

        return "all_search";
    }

    @GetMapping("/return")
    public String goSame(HttpServletRequest request) {

        return "redirect:/products/0";
    }

}
