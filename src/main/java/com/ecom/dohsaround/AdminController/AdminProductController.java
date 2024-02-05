package com.ecom.dohsaround.AdminController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.dohsaround.dto.ProductDto;
import com.ecom.dohsaround.model.Category;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.service.CategoryService;
import com.ecom.dohsaround.service.ProductService;
import com.ecom.dohsaround.service.ShopService;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ShopService adminService;

    @GetMapping("/products")
    public String products(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        Shop shop = adminService.findByUsername(principal.getName());
        model.addAttribute("admin", shop);

        List<ProductDto> productDtoList = productService.findAllByAdmin(shop);
        model.addAttribute("title", "Manage Product");
        model.addAttribute("products", productDtoList);
        model.addAttribute("size", productDtoList.size());
        return "product_main";
    }

    @GetMapping("/products/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        Shop shop = adminService.findByUsername(principal.getName());
        model.addAttribute("shop", shop);

        Page<ProductDto> products = productService.pageProducts(pageNo, shop);



        model.addAttribute("title", "Manage Product");
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);

        List<Category> categories = categoryService.findAllByShopActivated(shop);
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDto());
        model.addAttribute("url","/admin/products/");

        return "product_main";
    }


    @GetMapping("/search-result/{pageNo}")
    public String searchProducts(@PathVariable("pageNo")int pageNo,
                                 @RequestParam("keyword") String keyword,
                                 Model model,
                                 Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        Shop shop = adminService.findByUsername(principal.getName());
        model.addAttribute("shop", shop);
        Page<ProductDto> products = productService.searchProducts(pageNo, keyword, shop);
        model.addAttribute("title", "Search Result");
        model.addAttribute("products", products);
        model.addAttribute("size", products.getSize());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());


        List<Category> categories = categoryService.findAllByShopActivated(shop);
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDto());
        model.addAttribute("url","/admin/search-result/");
        return "product_main";
    }


    @GetMapping("/add-product")
    public String addProductForm(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        Shop shop = adminService.findByUsername(principal.getName());
        model.addAttribute("shop", shop);

        List<Category> categories = categoryService.findAllByShopActivated(shop);
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDto());
        return "add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("product")ProductDto productDto,
                              @RequestParam("imageProduct")MultipartFile[] imageProduct,
                              RedirectAttributes attributes, Principal principal){
        try {
            Shop shop = adminService.findByUsername(principal.getName());
            productService.save(imageProduct, productDto, shop);
            attributes.addFlashAttribute("success", "Add successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to add!");
        }

       

        return "redirect:/admin/products/0";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        Shop shop = adminService.findByUsername(principal.getName());
        model.addAttribute("shop", shop);
        model.addAttribute("title", "Update products");
        List<Category> categories = categoryService.findAllByActivated();
        ProductDto productDto = productService.getById(id);
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);
        return "update_product_main";
    }


    @PostMapping("/update-product/{id}")
    public String processUpdate(@PathVariable("id") Long id,
                                @ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct")MultipartFile[] imageProduct,
                                RedirectAttributes attributes
                                ){
        try {
            productService.update(imageProduct, productDto);
            attributes.addFlashAttribute("success", "Update successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update!");
        }
        return "redirect:/admin/products/0";

    }

    @RequestMapping(value = "/enable-product/{id}", method = {RequestMethod.PUT , RequestMethod.GET})
    public String enabledProduct(@PathVariable("id")Long id, RedirectAttributes attributes,
                                 HttpServletRequest httpServletRequest){
        try {
            productService.enableById(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to enabled!");
        }
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(@PathVariable("id") Long id, RedirectAttributes attributes,
                                 HttpServletRequest httpServletRequest){
        try {
            productService.deleteById(id);
            attributes.addFlashAttribute("success", "Deleted successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to deleted");
        }
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }
}
