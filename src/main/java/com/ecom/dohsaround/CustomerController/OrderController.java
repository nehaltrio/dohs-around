package com.ecom.dohsaround.CustomerController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom.dohsaround.model.CartItem;
import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.Order;
import com.ecom.dohsaround.model.ShoppingCart;
import com.ecom.dohsaround.repository.CartItemRepository;
import com.ecom.dohsaround.repository.OrderRepository;
import com.ecom.dohsaround.repository.ShoppingCartRepository;
import com.ecom.dohsaround.service.CustomerService;
import com.ecom.dohsaround.service.ShoppingCartService;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

@Controller
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ShoppingCartRepository cartRepository;

    @GetMapping("/check-out")
    public String checkout(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        if (customer != null) {
            if (customer.getPhoneNumber().trim().isEmpty() || customer.getAddress().trim().isEmpty()
                    || customer.getCity().trim().isEmpty() || customer.getCountry().trim().isEmpty()) {

                model.addAttribute("customer", customer);
                model.addAttribute("error", "You must fill the information after checkout!");
                return "account";
            } else {
                model.addAttribute("customer", customer);
                ShoppingCart cart = customer.getShoppingCart();
                model.addAttribute("cart", cart);
            }
        }
        return "checkout";
    }

    @RequestMapping("/request_order")
    // @Transactional
    public String placeOrder(Principal principal) {
        Customer customer = customerService.findByUsername(principal.getName());
        if (customer != null) {
            ShoppingCart cart = customer.getShoppingCart();
            Set<CartItem> cartItems = cart.getCartItem();

            for (CartItem item : cartItems) {
                Order order = new Order();
                LocalDate localDate = LocalDate.now();
                Date sqlDate = Date.valueOf(localDate);
                order.setOrderDate(sqlDate);
                order.setOrderStatus(false);
                order.setProduct(item.getProduct());
                order.setShopName(item.getProduct().getShop().getShopName());
                order.setQuantity(item.getQuantity());
                order.setCustomer(customer);
                order.setTotalPrice(item.getTotalPrice());
                orderRepository.save(order);

                cartItemRepository.delete(item);
                // shoppingCartService.deleteItemFromCart(item.getProduct(),customer);
            }
        }

        // cartRepository.deleteShoppingCartByCustomer(customer);

        return "after_checkout";
    }

    @GetMapping("/order")
    public String order() {
        return "order";
    }

}
