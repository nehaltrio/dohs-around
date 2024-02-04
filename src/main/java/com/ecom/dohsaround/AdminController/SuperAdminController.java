package com.ecom.dohsaround.AdminController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.dohsaround.dto.CustomerDto;
import com.ecom.dohsaround.dto.FreeListProductDto;
import com.ecom.dohsaround.dto.ShopDto;
import com.ecom.dohsaround.model.FreeListCategory;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.repository.RoleRepository;
import com.ecom.dohsaround.repository.ShopRepository;
import com.ecom.dohsaround.service.CustomerService;
import com.ecom.dohsaround.service.FreeListCategoryService;
import com.ecom.dohsaround.service.FreeListService;
import com.ecom.dohsaround.service.ShopService;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class SuperAdminController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FreeListCategoryService freeListCategoryService;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private FreeListService freeListService;

    @Autowired
    @Qualifier("passwordEncoderAdmin")
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/SuperAdmin")
    public String superAdmin(Principal principal, Model model) {
        if (validation(principal)) {
            model.addAttribute("title", "Register");
            model.addAttribute("adminDto", new ShopDto());
        }

        model.addAttribute("valid", validation(principal));

        model.addAttribute("msg", "You are not Super Admin");

        return "register";
    }

    @GetMapping("/marketplace-list/{pageNo}")
    public String showAllMarketPlaces(Model model, @PathVariable("pageNo") int pageNo,
            Principal principal) {

        Page<ShopDto> adminList = shopService.pageShops(pageNo);
        Shop shop = shopService.findByUsername(principal.getName());
        model.addAttribute("shop", shop);

        model.addAttribute("shopList", adminList);

        model.addAttribute("size", adminList.getSize());
        model.addAttribute("totalPages", adminList.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        return "shopList_main";
    }

    @GetMapping("/customer-list/{pageNo}")
    public String showAllCustomers(Model model, @PathVariable("pageNo") int pageNo,
            Principal principal) {
        Page<CustomerDto> customerList = customerService.pageCustomers(pageNo);
        Shop shop = shopService.findByUsername(principal.getName());
        model.addAttribute("shop", shop);

        model.addAttribute("customerList", customerList);

        model.addAttribute("size", customerList.getSize());
        model.addAttribute("totalPages", customerList.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        return "customerList_main";
    }

    @GetMapping("/product-list/{pageNo}")
    public String showAllProducts(Model model, @PathVariable int pageNo,
            Principal principal) {

        Page<FreeListProductDto> freeListProductDtos = freeListService.pageProducts(pageNo);
        Shop shop = shopService.findByUsername(principal.getName());
        model.addAttribute("shop", shop);

        model.addAttribute("freeListProduct", freeListProductDtos);

        model.addAttribute("size", freeListProductDtos.getSize());
        model.addAttribute("totalPages", freeListProductDtos.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        return "productList_main";
    }

    /* Customer Controls */

    @RequestMapping(value = "/enable-customer/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String enabledCustomer(@PathVariable("id") Long id, RedirectAttributes attributes,
            HttpServletRequest httpServletRequest) {
        try {
            customerService.setCustomerActive(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to enabled!");
        }
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @RequestMapping(value = "/disable-customer/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String disableCustomer(@PathVariable("id") Long id, RedirectAttributes attributes,
            HttpServletRequest httpServletRequest) {
        try {
            customerService.setCustomerDeactivate(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to enabled!");
        }
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    /* MarketPlace Controls */

    @RequestMapping(value = "/enable-shop/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String enableShop(@PathVariable("id") Long id, RedirectAttributes attributes,
            HttpServletRequest httpServletRequest) {
        try {
            shopService.setAdminActive(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to enabled!");
        }
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @RequestMapping(value = "/disable-shop/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String disableShop(@PathVariable("id") Long id, RedirectAttributes attributes,
            HttpServletRequest httpServletRequest) {
        try {
            shopService.setAdminDeactive(id);
            attributes.addFlashAttribute("success", "Disabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to disable!");
        }
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    /* Product Controls */

    @RequestMapping(value = "/disable-freelistProd/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String disableProduct(@PathVariable("id") Long id, RedirectAttributes attributes,
            HttpServletRequest httpServletRequest) {
        try {
            freeListService.deleteById(id);
            attributes.addFlashAttribute("success", "Disabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to disable!");
        }
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @RequestMapping(value = "/enable-freelistProd/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String enableProduct(@PathVariable("id") Long id, RedirectAttributes attributes,
            HttpServletRequest httpServletRequest) {
        try {
            freeListService.enableById(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to enable!");
        }
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    /* Category Controls */

    @GetMapping("/categoriesFreeList")
    public String categories(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Shop shop = shopService.findByUsername(principal.getName());
        model.addAttribute("shop", shop);

        List<FreeListCategory> categories = freeListCategoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("size", categories.size());
        model.addAttribute("title", "Category");
        model.addAttribute("categoryNew", new FreeListCategory());
        return "catergories_main_super";
    }

    @PostMapping("/add-freeListCategory")
    public String addCategory(@ModelAttribute("categoryNew") FreeListCategory category,
            RedirectAttributes attributes, Principal principal) {
        try {
            freeListCategoryService.save(category);
            attributes.addFlashAttribute("success", "Added successfully");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to add because duplicate name");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error server");
        }
        return "redirect:/categoriesFreeList";

    }

    @RequestMapping(value = "/findByIdCat", method = { RequestMethod.PUT, RequestMethod.GET })
    @ResponseBody
    public FreeListCategory findById(Long id) {
        return freeListCategoryService.findById(id);
    }

    @GetMapping("/update-freeListCategory")
    public String update(FreeListCategory category, RedirectAttributes attributes) {
        try {
            freeListCategoryService.update(category);
            attributes.addFlashAttribute("success", "Updated successfully");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to update because duplicate name");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error server");
        }
        return "redirect:/categoriesFreeList";
    }

    @RequestMapping(value = "/delete-freeListCategory", method = { RequestMethod.PUT, RequestMethod.GET })
    public String delete(Long id, RedirectAttributes attributes) {
        try {
            freeListCategoryService.deleteById(id);
            attributes.addFlashAttribute("success", "Deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to deleted");
        }
        return "redirect:/categoriesFreeList";
    }

    @RequestMapping(value = "/enable-freeListCategory", method = { RequestMethod.PUT, RequestMethod.GET })
    public String enable(Long id, RedirectAttributes attributes) {
        try {
            freeListCategoryService.enabledById(id);
            attributes.addFlashAttribute("success", "Enabled successfully");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to enabled");
        }
        return "redirect:/categoriesFreeList";
    }

    @RequestMapping("/create_super")
    private String createSuperAdmin() {

        if (shopRepository.findByUsername("ahmed@gmail.com") == null) {
            Shop shop = new Shop();
            shop.setFirstName("Super");
            shop.setLastName("Admin");
            shop.setUsername("ahmed@gmail.com");
            shop.setPassword(passwordEncoder.encode("12345"));
            shop.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
            shop.setShopName("SUPER");
            shop.setActive(false);
            shop.setShopCategory(null);
            shop.setDeliveryLocation(null);

            shopRepository.save(shop);

            System.out.println("superadmin created successfully");
        }

        return "redirect:/adminlogin";
    }

    private boolean validation(Principal principal) {
        return principal.getName().equals("ahmed@gmail.com");
    }

}
