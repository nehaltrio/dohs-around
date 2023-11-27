package com.ecom.dohsaround.AdminController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.dohsaround.dto.ServiceDto;
import com.ecom.dohsaround.dto.ShopDto;
import com.ecom.dohsaround.model.Order;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.model.ShopCategories;
import com.ecom.dohsaround.repository.CategoryRepository;
import com.ecom.dohsaround.repository.CustomerRepository;
import com.ecom.dohsaround.repository.FreeListProductRepository;
import com.ecom.dohsaround.repository.OrderRepository;
import com.ecom.dohsaround.repository.ProductRepository;
import com.ecom.dohsaround.repository.ShopRepository;
import com.ecom.dohsaround.repository.serviceRepository;
import com.ecom.dohsaround.service.OrderService;
import com.ecom.dohsaround.service.ShopService;
import com.ecom.dohsaround.service.serviceService;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private serviceService servService;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private serviceRepository serviceRepo;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FreeListProductRepository freeListProductRepository;

    @GetMapping("OrderList")
    public String showShopOrder(Principal principal, Model model) {
        Shop shop = shopService.findByUsername(principal.getName());

        List<Order> orderList = orderRepository.findOrderByShopId(shop.getId());

        // cartItems.get(0).getCart().getCustomer();
        model.addAttribute("shop", shop);
        model.addAttribute("orders", orderList);

        return "orders_main";
    }

    @RequestMapping(value = "/accept-order/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String acceptOrder(@PathVariable("id") Long id) {

        orderService.acceptOrderById(id);

        return "redirect:/OrderList";
    }

    @RequestMapping(value = "/decline-order/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String declineOrder(@PathVariable("id") Long id) {

        orderService.declineOrderById(id);

        return "redirect:/OrderList";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Principal principal, Model model) {

        if (principal == null) {
            return "redirect:/login";
        }

        Shop shop = shopService.findByUsername(principal.getName());



        if (shop == null) {
                return "redirect:/login";
        }

        int productCount = productRepository.countProductByAdmin_Id(shop.getId());
        int categoryCount = categoryRepository.countCategoryByAdmin_Id(shop.getId());
        int orderCount = orderRepository.countOrderByAdmin_Id(shop.getId());

        List<Order> orderList = orderRepository.findOrderByShopIdRecent(shop.getId(), PageRequest.of(0, 5));

        model.addAttribute("shop", shop);
        model.addAttribute("orders", orderList);
        model.addAttribute("productCount", productCount);
        model.addAttribute("catCount", categoryCount);
        model.addAttribute("orderCount", orderCount);

        Page<ServiceDto> serviceDtos = servService.pageServices(0, shop);
        model.addAttribute("services", serviceDtos);

        int serviceCount = serviceRepo.countServiceByAdmin_Id(shop.getId());
        model.addAttribute("serviceCount", serviceCount);

        System.out.println(serviceCount);

        if (superAdmin(principal)) {

            List<Shop> shopList = shopRepository.findTopFiveShop(PageRequest.of(0, 5));

            model.addAttribute("shopList", shopList);

            model.addAttribute("numberOfShop", shopRepository.count());
            model.addAttribute("numberOfCustomer", customerRepository.count());
            model.addAttribute("numberOfProduct", freeListProductRepository.count());

            // serviceRepo

            return "dashboard_main_super";
        }

        if (shop.getShopCategory().equals(ShopCategories.KEY_MAKER.toString())) {
            return "dashboard_main_service";
        } else if (shop.getShopCategory().equals(ShopCategories.CAR_SERVICE.toString())) {
            return "dashboard_main_service";
            // }else if (shop.getShopCategory().equals(ShopCategories.PET_SHOP.toString())){
            //
            // }else if (shop.getShopCategory().equals(ShopCategories.TUTOR.toString())){
            //
            // }else if
            // (shop.getShopCategory().equals(ShopCategories.CLOUD_KITCHEN.toString())){

        } else {
            return "dashboard_main";
        }
    }

    @RequestMapping("/setThemeColor")
    @Transactional
    public String setThemeColorForShop(HttpServletRequest httpServletRequest,
            Principal principal,
            @RequestParam("color") String color) {

        Shop shop = shopService.findByUsername(principal.getName());
        shopRepository.setShopColorByShop(shop.getId(), color);

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @GetMapping("/update-shop/{shopName}")
    public String updateShopDetails(@PathVariable("shopName") String shopName,
            Model model, Principal principal) {

        Shop shop = shopService.findByUsername(principal.getName());
        model.addAttribute("shop", shop);
        ShopDto shopDto = shopService.findAdminByShopName(shop.getShopName());
        model.addAttribute("shopDto", shopDto);

        return "shopDetailsUpdate";
    }

    @PostMapping("/update-shop/{shopName}")
    public String processUpdateShop(@PathVariable("shopName") String shopName,
            @ModelAttribute("adminDto") ShopDto shopDto,
            @RequestParam("imageShop") MultipartFile imageShop, Principal principal) {
        shopService.updateShop(imageShop, shopDto, principal);
        return "redirect:/dashboard";
    }

    private boolean superAdmin(Principal principal) {
        return principal.getName().equals("ahmed@gmail.com");
    }

}
