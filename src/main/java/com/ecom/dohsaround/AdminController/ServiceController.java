package com.ecom.dohsaround.AdminController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.dohsaround.dto.ServiceDto;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.service.ShopService;
import com.ecom.dohsaround.service.serviceService;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class ServiceController {

    @Autowired
    private serviceService servService;

    @Autowired
    private ShopService shopService;

    @GetMapping("/services/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        Shop shop = shopService.findByUsername(principal.getName());
        model.addAttribute("shop", shop);

        Page<ServiceDto> serviceDtos = servService.pageServices(pageNo, shop);

        model.addAttribute("title", "Manage Service");
        model.addAttribute("size", serviceDtos.getSize());
        model.addAttribute("totalPages", serviceDtos.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("services", serviceDtos);

        model.addAttribute("service", new ServiceDto());

        model.addAttribute("url","/services/");

        return "services_main";
    }


    @GetMapping("/search-service/{pageNo}")
    public String searchProducts(@PathVariable("pageNo")int pageNo,
                                 @RequestParam("keyword") String keyword,
                                 Model model,
                                 Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        Shop shop = shopService.findByUsername(principal.getName());
        model.addAttribute("admin", shop);

        Page<ServiceDto> serviceDtos = servService.searchService(pageNo, keyword, shop);
        model.addAttribute("title", "Search Result");
        model.addAttribute("services", serviceDtos);
        model.addAttribute("size", serviceDtos.getSize());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", serviceDtos.getTotalPages());

        model.addAttribute("service", new ServiceDto());

        model.addAttribute("url","/search-service/");

        return "services_main";
    }



    @PostMapping("/save-service")
    public String saveProduct(@ModelAttribute("service")ServiceDto serviceDto,
                              RedirectAttributes attributes, Principal principal){
        try {
            Shop shop = shopService.findByUsername(principal.getName());
            servService.save( serviceDto, shop);
            attributes.addFlashAttribute("success", "Add successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to add!");
        }

        return "redirect:/services/0";
    }

    @GetMapping("/update-service/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        Shop shop = shopService.findByUsername(principal.getName());
        model.addAttribute("shop", shop);
        model.addAttribute("title", "Update service");

        ServiceDto serviceDto = servService.getById(id);

        model.addAttribute("serviceDto", serviceDto);
        return "update_service_main";
    }


    @PostMapping("/update-service/{id}")
    public String processUpdate(@PathVariable("id") Long id,
                                @ModelAttribute("serviceDto") ServiceDto serviceDto,
                                RedirectAttributes attributes
    ){
        try {
            servService.update(serviceDto);
            attributes.addFlashAttribute("success", "Update successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update!");
        }
        return "redirect:/services/0";

    }

    @RequestMapping(value = "/enable-service/{id}", method = {RequestMethod.PUT , RequestMethod.GET})
    public String enabledProduct(@PathVariable("id")Long id, RedirectAttributes attributes,
                                 HttpServletRequest httpServletRequest){
        try {
            servService.enableById(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to enabled!");
        }
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @RequestMapping(value = "/delete-service/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(@PathVariable("id") Long id, RedirectAttributes attributes,
                                 HttpServletRequest httpServletRequest){
        try {
            servService.deleteById(id);
            attributes.addFlashAttribute("success", "Deleted successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to deleted");
        }
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }
}
