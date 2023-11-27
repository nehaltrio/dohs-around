package com.ecom.dohsaround.CustomerController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom.dohsaround.dto.CategoryDto;
import com.ecom.dohsaround.dto.ProductDto;
import com.ecom.dohsaround.dto.ServiceDto;
import com.ecom.dohsaround.model.Category;
import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.Product;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.model.ShopCategories;
import com.ecom.dohsaround.model.ShoppingCart;
import com.ecom.dohsaround.repository.ShopRepository;
import com.ecom.dohsaround.service.CategoryService;
import com.ecom.dohsaround.service.CustomerService;
import com.ecom.dohsaround.service.ProductService;
import com.ecom.dohsaround.service.serviceService;

import java.security.Principal;
import java.util.List;


@Controller
public class CustomerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private serviceService service;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShopRepository shopRepository;

    @GetMapping("/{shopName}/products/{pageNo}")
    public String products(Model model, @PathVariable("pageNo") int pageNo,
            Principal principal, @PathVariable("shopName") String shopName) {

        Shop shop = shopRepository.getAdminByShopName(shopName);
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct(shopName);

        Page<ProductDto> products = productService.pageProductsForCustomer(pageNo, shopName);
        Page<ServiceDto> services = service.pageServices(pageNo, shop);

        List<Product> listViewProducts = productService.listViewProducts();

        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("viewProducts", listViewProducts);
        model.addAttribute("shopN", shopName);

        model.addAttribute("shop", shop);
        model.addAttribute("theme", shop.getThemeColor());
        // pagination
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);

        model.addAttribute("services", services);

        model.addAttribute("principal", principal);
        // cart

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

        if (shop.getShopCategory().equals(ShopCategories.CAR_SERVICE.toString())) {
            return "marketplace_main_service";
        }

        return "marketplace_main";
    }

    @GetMapping("{shopName}/search-result/{pageNo}")
    public String SearchProducts(Model model, @PathVariable("pageNo") int pageNo,
            @RequestParam("keyword") String keyword,
            Principal principal, @PathVariable("shopName") String shopName) {
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct(shopName);
        Shop shop = shopRepository.getAdminByShopName(shopName);
        Page<ProductDto> products = productService.searchProductsForCustomer(pageNo, keyword, shopName);
        List<Product> listViewProducts = productService.listViewProducts();
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("viewProducts", listViewProducts);
        model.addAttribute("products", products);
        model.addAttribute("shopN", shopName);
        model.addAttribute("shop", shop);

        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            ShoppingCart shoppingCart = customer.getShoppingCart();
            model.addAttribute("customer", customer);

            if (shoppingCart == null) {
                model.addAttribute("check", "No item in your cart");
            }

            model.addAttribute("principal", principal);

            model.addAttribute("shoppingCart", shoppingCart);

        }

        return "marketplace_main";
    }

    @GetMapping("/find-product/{id}")
    public String findProductById(@PathVariable("id") Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        Long categoryId = product.getCategory().getId();
        List<Product> products = productService.getRelatedProducts(categoryId);
        model.addAttribute("product", product);
        model.addAttribute("products", products);

        Shop shop = shopRepository.getAdminByShopName(product.getShop().getShopName());
        model.addAttribute("shop", shop);
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

        return "product-detail_main";
    }

    @GetMapping("{shopName}/products-in-category/{id}/{pageNo}")
    public String getProductsInCategory(@PathVariable("id") Long categoryId, Model model,
            @PathVariable("pageNo") int pageNo,
            Principal principal,
            @PathVariable("shopName") String shopName) {
        Category category = categoryService.findById(categoryId);
        List<CategoryDto> categories = categoryService.getCategoryAndProduct(shopName);
        model.addAttribute("shopN", shopName);

        Page<ProductDto> products = productService.getProductsInCategory(categoryId, pageNo, shopName);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        Shop shop = shopRepository.getAdminByShopName(shopName);
        model.addAttribute("shop", shop);

        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            ShoppingCart shoppingCart = customer.getShoppingCart();
            model.addAttribute("customer", customer);

            if (shoppingCart == null) {
                model.addAttribute("check", "No item in your cart");
            }

            model.addAttribute("principal", principal);

            model.addAttribute("shoppingCart", shoppingCart);

        }

        return "marketplace_catwise_main";
    }

    @GetMapping("{shopName}/high-price/{pageNo}")
    public String filterHighPrice(Model model, @PathVariable("pageNo") int pageNo,
            Principal principal,
            @PathVariable("shopName") String shopName) {
        // List<Category> categories = categoryService.findAllByActivated();
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct(shopName);
        Page<ProductDto> products = productService.filterHighPrice(pageNo, shopName);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("products", products);
        // model.addAttribute("categories", categories);

        model.addAttribute("shopN", shopName);
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);

        Shop shop = shopRepository.getAdminByShopName(shopName);
        model.addAttribute("shop", shop);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            ShoppingCart shoppingCart = customer.getShoppingCart();
            model.addAttribute("customer", customer);

            if (shoppingCart == null) {
                model.addAttribute("check", "No item in your cart");
            }

            model.addAttribute("principal", principal);

            model.addAttribute("shoppingCart", shoppingCart);

        }

        return "marketplace_high_to_low_main";
    }

    @GetMapping("{shopName}/low-price/{pageNo}")
    public String filterLowPrice(Model model, @PathVariable("pageNo") int pageNo,
            Principal principal,
            @PathVariable("shopName") String shopName) {
        // List<Category> categories = categoryService.findAllByActivated();
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct(shopName);
        Page<ProductDto> products = productService.filterLowPrice(pageNo, shopName);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("products", products);
        // model.addAttribute("categories", categories);
        model.addAttribute("shopN", shopName);

        Shop shop = shopRepository.getAdminByShopName(shopName);
        model.addAttribute("shop", shop);

        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            ShoppingCart shoppingCart = customer.getShoppingCart();
            model.addAttribute("customer", customer);

            if (shoppingCart == null) {
                model.addAttribute("check", "No item in your cart");
            }

            model.addAttribute("principal", principal);

            model.addAttribute("shoppingCart", shoppingCart);

        }

        return "marketplace_low_to_high_main";
    }

    @GetMapping("{shopName}/products-in-category-sort-high/{id}/{pageNo}")
    public String getProductsInCategorySortHigh(@PathVariable("id") Long categoryId,
            Model model,
            @PathVariable("pageNo") int pageNo,
            Principal principal,
            @PathVariable("shopName") String shopName) {
        Category category = categoryService.findById(categoryId);
        List<CategoryDto> categories = categoryService.getCategoryAndProduct(shopName);
        Page<ProductDto> products = productService.getProductByCategorySortedHighPrice(categoryId, pageNo, shopName);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("shopN", shopName);
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);

        Shop shop = shopRepository.getAdminByShopName(shopName);
        model.addAttribute("shop", shop);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            ShoppingCart shoppingCart = customer.getShoppingCart();
            model.addAttribute("customer", customer);

            if (shoppingCart == null) {
                model.addAttribute("check", "No item in your cart");
            }

            model.addAttribute("principal", principal);

            model.addAttribute("shoppingCart", shoppingCart);

        }

        return "marketplace_catwise_main_sort_high";
    }

    @GetMapping("{shopName}/products-in-category-sort-low/{id}/{pageNo}")
    public String getProductsInCategorySortLow(@PathVariable("id") Long categoryId,
            Model model,
            @PathVariable("pageNo") int pageNo,
            Principal principal, @PathVariable("shopName") String shopName) {
        Category category = categoryService.findById(categoryId);
        List<CategoryDto> categories = categoryService.getCategoryAndProduct(shopName);
        Page<ProductDto> products = productService.getProductByCategorySortedLowPrice(categoryId, pageNo, shopName);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("shopN", shopName);

        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);

        Shop shop = shopRepository.getAdminByShopName(shopName);
        model.addAttribute("shop", shop);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            ShoppingCart shoppingCart = customer.getShoppingCart();
            model.addAttribute("customer", customer);

            if (shoppingCart == null) {
                model.addAttribute("check", "No item in your cart");
            }

            model.addAttribute("principal", principal);

            model.addAttribute("shoppingCart", shoppingCart);

        }

        return "marketplace_catwise_main_sort_low";
    }

    @GetMapping("/searchShopProd")
    public String searchShopProd(Model model) {

        List<Product> productList = shopRepository.searchByProduct("m");

        for (Product product : productList) {
            System.out.println("Product: " + product.getName());
        }

        model.addAttribute("products", productList);

        return "all_shops";
    }

}
