package com.ecom.dohsaround.CustomerController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.dohsaround.dto.FreeListCategoryDto;
import com.ecom.dohsaround.dto.FreeListProductDto;
import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.FreeListCategory;
import com.ecom.dohsaround.model.FreeListProduct;
import com.ecom.dohsaround.model.ShoppingCart;
import com.ecom.dohsaround.repository.CustomerRepository;
import com.ecom.dohsaround.repository.FreeListProductRepository;
import com.ecom.dohsaround.service.CustomerService;
import com.ecom.dohsaround.service.FreeListCategoryService;
import com.ecom.dohsaround.service.FreeListService;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class FreeListController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private FreeListService freeListService;
    @Autowired
    private FreeListCategoryService freeListCategoryService;
    @Autowired
    private FreeListProductRepository freeListProductRepository;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/list-product")
    public String saveProduct(@ModelAttribute("product") FreeListProductDto productDto,
            @RequestParam(value = "imageProduct", required = false) MultipartFile imageProduct,
            RedirectAttributes attributes, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }
        try {
            Customer customer = customerRepository.findByUsername(principal.getName());
            if (customer != null) {
                freeListService.save(imageProduct, customer, productDto);
                attributes.addFlashAttribute("success", "Add successfully!");
            } else {
                return "redirect:/login";
            }
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to add!");
        }
        return "redirect:/products/0";
    }

    @GetMapping("/post_add")
    public String ViewPostAdPage(Model model, Principal principal) {
        model.addAttribute("product", new FreeListProductDto());

        List<FreeListCategory> categoryList = freeListCategoryService.findAllByActivated();
        model.addAttribute("categories", categoryList);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                ShoppingCart shoppingCart = customer.getShoppingCart();
                model.addAttribute("customer", customer);

                if (shoppingCart == null) {
                    model.addAttribute("check", "No item in your cart");
                }

                model.addAttribute("principal", principal);

                model.addAttribute("shoppingCart", shoppingCart);
            }
        }

        return "freelistPost";
    }

//    @GetMapping("/products/{pageNo}")
    public String showFreeList(Model model, @PathVariable("pageNo") int pageNo, Principal principal) {
        Page<FreeListProductDto> freeListProducts = freeListService.freeListProductPage(pageNo);

        List<FreeListCategoryDto> categoryDtoList = freeListCategoryService.getCategoryAndProduct();
        model.addAttribute("categories", categoryDtoList);

        model.addAttribute("products", freeListProducts);
        model.addAttribute("product", new FreeListProductDto());

        model.addAttribute("size", freeListProducts.getSize());
        model.addAttribute("totalPages", freeListProducts.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                ShoppingCart shoppingCart = customer.getShoppingCart();
                model.addAttribute("customer", customer);

                if (shoppingCart == null) {
                    model.addAttribute("check", "No item in your cart");
                }

                model.addAttribute("principal", principal);

                model.addAttribute("shoppingCart", shoppingCart);
            }
        }

        return "freelist";
    }

    @GetMapping("/high-to-low/{pageNo}")
    public String SortHighToLow(@PathVariable("pageNo") int pageNo, Model model,
            Principal principal) {
        Page<FreeListProductDto> freeListProducts = freeListService.freeListProductPageHighToLow(pageNo);
        List<FreeListCategoryDto> categoryDtoList = freeListCategoryService.getCategoryAndProduct();

        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("products", freeListProducts);
        model.addAttribute("product", new FreeListProductDto());

        model.addAttribute("size", freeListProducts.getSize());
        model.addAttribute("totalPages", freeListProducts.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                ShoppingCart shoppingCart = customer.getShoppingCart();
                model.addAttribute("customer", customer);

                if (shoppingCart == null) {
                    model.addAttribute("check", "No item in your cart");
                }

                model.addAttribute("principal", principal);

                model.addAttribute("shoppingCart", shoppingCart);
            }
        }

        return "marketplace_high_to_low_main_freeList";
    }

    @GetMapping("/low-to-high/{pageNo}")
    public String SortLowToHigh(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        Page<FreeListProductDto> freeListProducts = freeListService.freeListProductPageLowToHigh(pageNo);
        List<FreeListCategoryDto> categoryDtoList = freeListCategoryService.getCategoryAndProduct();

        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("products", freeListProducts);
        model.addAttribute("product", new FreeListProductDto());

        model.addAttribute("size", freeListProducts.getSize());
        model.addAttribute("totalPages", freeListProducts.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                ShoppingCart shoppingCart = customer.getShoppingCart();
                model.addAttribute("customer", customer);

                if (shoppingCart == null) {
                    model.addAttribute("check", "No item in your cart");
                }

                model.addAttribute("principal", principal);

                model.addAttribute("shoppingCart", shoppingCart);
            }
        }

        return "marketplace_low_to_high_main_freeList";
    }

    @GetMapping("/search-result/{pageNo}")
    public String SearchProducts(Model model, @PathVariable("pageNo") int pageNo,
            @RequestParam("keyword") String keyword, Principal principal) {

        Page<FreeListProductDto> freeListProducts = freeListService.searchFreelist(pageNo, keyword);

        List<FreeListCategoryDto> categoryDtoList = freeListCategoryService.getCategoryAndProduct();

        model.addAttribute("categories", categoryDtoList);

        model.addAttribute("products", freeListProducts);
        model.addAttribute("product", new FreeListProductDto());

        model.addAttribute("size", freeListProducts.getSize());
        model.addAttribute("totalPages", freeListProducts.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                ShoppingCart shoppingCart = customer.getShoppingCart();
                model.addAttribute("customer", customer);

                if (shoppingCart == null) {
                    model.addAttribute("check", "No item in your cart");
                }

                model.addAttribute("principal", principal);

                model.addAttribute("shoppingCart", shoppingCart);
            }
        }

        return "freelist_search";
    }

    @GetMapping("/products-in-category/{id}/{pageNo}")
    public String getProductsInCategory(@PathVariable("id") Long categoryId, Model model,
            @PathVariable("pageNo") int pageNo, Principal principal) {
        FreeListCategory category = freeListCategoryService.findById(categoryId);
        List<FreeListCategoryDto> categories = freeListCategoryService.getCategoryAndProduct();

        Page<FreeListProductDto> products = freeListService.getProductsInCategory(categoryId, pageNo);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);

        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                ShoppingCart shoppingCart = customer.getShoppingCart();
                model.addAttribute("customer", customer);

                if (shoppingCart == null) {
                    model.addAttribute("check", "No item in your cart");
                }

                model.addAttribute("principal", principal);

                model.addAttribute("shoppingCart", shoppingCart);
            }
        }

        return "marketplace_catwise_main_freelist";
    }

    @GetMapping("/products-in-category-sort-high/{id}/{pageNo}")
    public String getProductsInCategorySortHigh(@PathVariable("id") Long categoryId,
            Model model, Principal principal,
            @PathVariable("pageNo") int pageNo) {
        FreeListCategory category = freeListCategoryService.findById(categoryId);
        List<FreeListCategoryDto> categories = freeListCategoryService.getCategoryAndProduct();
        Page<FreeListProductDto> products = freeListService.getProductByCategorySortedHighPrice(categoryId, pageNo);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);

        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                ShoppingCart shoppingCart = customer.getShoppingCart();
                model.addAttribute("customer", customer);

                if (shoppingCart == null) {
                    model.addAttribute("check", "No item in your cart");
                }

                model.addAttribute("principal", principal);

                model.addAttribute("shoppingCart", shoppingCart);
            }
        }

        return "freelist_catwise_main_sort_high";
    }

    @GetMapping("/products-in-category-sort-low/{id}/{pageNo}")
    public String getProductsInCategorySortLow(@PathVariable("id") Long categoryId,
            Model model, Principal principal,
            @PathVariable("pageNo") int pageNo) {
        FreeListCategory category = freeListCategoryService.findById(categoryId);
        List<FreeListCategoryDto> categories = freeListCategoryService.getCategoryAndProduct();
        Page<FreeListProductDto> products = freeListService.getProductByCategorySortedLowPrice(categoryId, pageNo);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);

        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                ShoppingCart shoppingCart = customer.getShoppingCart();
                model.addAttribute("customer", customer);

                if (shoppingCart == null) {
                    model.addAttribute("check", "No item in your cart");
                }

                model.addAttribute("principal", principal);

                model.addAttribute("shoppingCart", shoppingCart);
            }
        }

        return "freelist_catwise_main_sort_low";
    }

    @GetMapping("/customerProductList")
    private String showCustomerProductList(Principal principal, Model model) {
        if (principal != null) {
            Customer customer = customerRepository.findByUsername(principal.getName());
            if (customer != null) {
                List<FreeListProduct> products = freeListService.getProductsCustomerUploadedByCustomer(customer);

                model.addAttribute("customerProductList", products);

                ShoppingCart shoppingCart = customer.getShoppingCart();
                model.addAttribute("customer", customer);

                if (shoppingCart == null) {
                    model.addAttribute("check", "No item in your cart");
                }

                model.addAttribute("principal", principal);

                model.addAttribute("shoppingCart", shoppingCart);
            }
        }

        return "customer_products_main";
    }

    @RequestMapping(value = "/report-product/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String enabledProduct(@PathVariable("id") Long id, RedirectAttributes attributes,
            HttpServletRequest httpServletRequest) {
        try {
            freeListService.reportById(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to enabled!");
        }
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @RequestMapping("/delete-customerProduct/{id}")
    public String deleteCustomerProduct(
            @PathVariable("id") Long id,
            Principal principal,
            HttpServletRequest request) {

        if (principal == null) {
            return "redirect:/login";
        }

        freeListService.deleteCustomerProduct(id);

        return "redirect:" + request.getHeader("Referer");

    }

    @GetMapping("/find-freeList_product/{id}")
    public String findProductById(@PathVariable("id") Long id, Model model, Principal principal) {
        FreeListProduct product = freeListService.getProductById(id);
        Long categoryId = product.getCategory().getId();
        List<FreeListProduct> products = freeListProductRepository.getProductsInCategory(categoryId);
        model.addAttribute("product", product);
        model.addAttribute("products", products);

        if (principal != null) {

            String username = principal.getName();

            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                ShoppingCart shoppingCart = customer.getShoppingCart();
                model.addAttribute("customer", customer);

                if (shoppingCart == null) {
                    model.addAttribute("check", "No item in your cart");
                }

                model.addAttribute("principal", principal);

                model.addAttribute("shoppingCart", shoppingCart);
            }
        }

        return "product-detail_main_freelist";
    }

}
