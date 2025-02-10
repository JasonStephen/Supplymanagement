package com.jason.supplymanagement.controller;

import com.jason.supplymanagement.entity.Users.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    // 从 session 中获取 user 对象
    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    // 将 user 添加到 Model 中
    private void addUserToModel(HttpSession session, Model model) {
        model.addAttribute("user", getUser(session));
    }

    // 判断 user 是否拥有某个权限
    private boolean hasPermission(User user, String code) {
        return user.getRole().getPermissions().stream()
                .anyMatch(permission -> code.equals(permission.getPermissionCode()));
    }

    // 将权限信息添加到 Model 中，并打印调试信息
    private void addUserPermissions(Model model, User user) {
        boolean hasRoleCreatePermission = hasPermission(user, "ROLE_CREATE");
        boolean hasPermissionBindPermission = hasPermission(user, "PERMISSION_BIND");
        boolean hasObjectManagementPermission = hasPermission(user, "OBJECT_MANAGEMENT");
        boolean hasObjectOrderCreatePermission = hasPermission(user, "OBJECT_ORDER_CREATE");

        model.addAttribute("hasRoleCreatePermission", hasRoleCreatePermission);
        model.addAttribute("hasPermissionBindPermission", hasPermissionBindPermission);
        model.addAttribute("hasObjectManagementPermission", hasObjectManagementPermission);
        model.addAttribute("hasObjectOrderCreatePermission", hasObjectOrderCreatePermission);

//        系统提示不再是必要的。
//        System.out.println("DEBUG - hasRoleCreatePermission: " + hasRoleCreatePermission);
//        System.out.println("DEBUG - hasPermissionBindPermission: " + hasPermissionBindPermission);
//        System.out.println("DEBUG - hasObjectManagementPermission: " + hasObjectManagementPermission);
//        System.out.println("DEBUG - hasObjectOrderCreatePermission: " + hasObjectOrderCreatePermission);

//        系统提示不再是必要的。
//        System.out.println("DEBUG - hasRoleCreatePermission: " + hasRoleCreatePermission);
//        System.out.println("DEBUG - hasPermissionBindPermission: " + hasPermissionBindPermission);
    }

    // 处理需要用户登录且需要权限校验的页面
    private String handleUserPage(HttpSession session, Model model, String viewName) {
        User user = getUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        addUserPermissions(model, user);
        return viewName;
    }

    // 处理仅要求用户登录的页面
    private String handleLoginRequired(HttpSession session, Model model, String viewName) {
        User user = getUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return viewName;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        addUserToModel(session, model);
        return "index"; // 对应 templates/index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/user/info")
    public String userInfo(HttpSession session, Model model) {
        return handleUserPage(session, model, "userinfo");
    }

    @GetMapping("/user/role")
    public String role(HttpSession session, Model model) {
        return handleUserPage(session, model, "role");
    }

    @GetMapping("/user/permission")
    public String permission(HttpSession session, Model model) {
        return handleUserPage(session, model, "permission");
    }

    @GetMapping("/product/list")
    public String products(HttpSession session, Model model) {
        addUserToModel(session, model);
        return "product-list";
    }

    @GetMapping("/produce")
    public String produce(HttpSession session, Model model) {
        addUserToModel(session, model);
        return "produce";
    }

    @GetMapping("/product-category")
    public String productCategory() {
        return "product_category";
    }



    //创世神功能系列
    @GetMapping("/creator/objects")
    public String creatorObjects(HttpSession session, Model model) {
        return handleUserPage(session, model, "creator-roles");
    }

    @GetMapping("/creator/objects-orders")
    public String creatorObjectsOrders(HttpSession session, Model model) {
        return handleUserPage(session, model, "creator-orders");
    }
    @GetMapping("/creator/objects/custom")
    public String saleGoods(HttpSession session, Model model) {
        addUserToModel(session, model);
        return "sale-goods";
    }

    @GetMapping("/creator/objects/supply")
    public String purchaseOrder(HttpSession session, Model model) {
        addUserToModel(session, model);
        return "supply-goods";
    }

    @GetMapping("/creator/objects-orders/customer")
    public String customers() {
        return "customer";
    }

    @GetMapping("/creator/objects-orders/supplier")
    public String suppliers() {
        return "supplier";
    }

    @GetMapping("/creator/objects-orders/logistics-company")
    public String logisticsCompanies() {
        return "logistics-company";
    }



    //买卖系统部分
    @GetMapping("/good-sale")
    public String goodSale(HttpSession session, Model model) {
        return handleLoginRequired(session, model, "good-sale");
    }

    @GetMapping("/good-supply")
    public String goodSupply(HttpSession session, Model model) {
        return handleLoginRequired(session, model, "good-supply");
    }

    @GetMapping("/users-orders")
    public String orders(HttpSession session, Model model) {
        addUserToModel(session, model);
        return "orders";
    }
}
