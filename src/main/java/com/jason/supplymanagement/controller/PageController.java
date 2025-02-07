package com.jason.supplymanagement.controller;

import com.jason.supplymanagement.entity.Users.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "index"; // corresponds to templates/index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/role")
    public String role() {
        return "role";
    }

    @GetMapping("/permission")
    public String permission() {
        return "permission";
    }

    @GetMapping("/bindrole")
    public String bindrole() {
        return "BindRolePermission";
    }

    @GetMapping("/product-details")
    public String products(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "product-details";
    }

    @GetMapping("/product-category")
    public String productCategory() {
        return "product_category";
    }

    @GetMapping("/customer-management")
    public String customers() {
        return "customer";
    }

    @GetMapping("/supplier-management")
    public String suppliers() {
        return "supplier";
    }

    @GetMapping("/logistics-company-management")
    public String logisticsCompanies() {
        return "logistics-company";
    }

    @GetMapping("/sale-goods")
    public String saleGoods(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "sale-goods"; // corresponds to templates/sale-goods.html
    }

    @GetMapping("/good-sale")
    public String goodSale(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // 如果用户未登录，重定向到登录页面
        }
        model.addAttribute("user", user);
        return "good-sale";
    }

    @GetMapping("/supply-goods")
    public String purchaseOrder(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "supply-goods";
    }

    @GetMapping("/good-supply")
    public String goodSupply(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "good-supply";
    }


    @GetMapping("/users-orders")
    public String orders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "orders";
    }
}