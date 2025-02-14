package com.jason.supplymanagement.controller;

import com.jason.supplymanagement.dto.ProductDetailsDTO;
import com.jason.supplymanagement.entity.Users.User;
import com.jason.supplymanagement.service.Product.ProductCategoryService;
import com.jason.supplymanagement.service.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;

    // 从 session 中获取 user 对象
    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    // 将 user 添加到 Model 中
    private void addUserToModel(HttpSession session, Model model) {
        model.addAttribute("user", getUser(session));
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
        boolean hasGoodsSetPermission = hasPermission(user,"GOODS_SET");
        boolean hasGoodProducePermission = hasPermission(user,"GOOD_PRODUCE");
        boolean hasOrderSetupPermission = hasPermission(user,"ORDER_SETUP");

        model.addAttribute("hasRoleCreatePermission", hasRoleCreatePermission);
        model.addAttribute("hasPermissionBindPermission", hasPermissionBindPermission);
        model.addAttribute("hasObjectManagementPermission", hasObjectManagementPermission);
        model.addAttribute("hasObjectOrderCreatePermission", hasObjectOrderCreatePermission);
        model.addAttribute("hasGoodsSetPermission", hasGoodsSetPermission);
        model.addAttribute("hasGoodProducePermission", hasGoodProducePermission);
        model.addAttribute("hasOrderSetupPermission", hasOrderSetupPermission);

//        系统提示不再是必要的。
//        System.out.println("DEBUG - hasRoleCreatePermission: " + hasRoleCreatePermission);
//        System.out.println("DEBUG - hasPermissionBindPermission: " + hasPermissionBindPermission);
//        System.out.println("DEBUG - hasObjectManagementPermission: " + hasObjectManagementPermission);
//        System.out.println("DEBUG - hasObjectOrderCreatePermission: " + hasObjectOrderCreatePermission);

//        系统提示不再是必要的。
//        System.out.println("DEBUG - hasRoleCreatePermission: " + hasRoleCreatePermission);
//        System.out.println("DEBUG - hasPermissionBindPermission: " + hasPermissionBindPermission);
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        handleUserPage(session, model, "index");
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
        model.addAttribute("categories", productCategoryService.getAllProductCategories());
        User user = getUser(session);
        if (user != null) {
            model.addAttribute("hasGoodsSetPermission", hasPermission(user, "GOODS_SET"));
        } else {
            model.addAttribute("hasGoodsSetPermission", false);
        }
        handleUserPage(session, model, "product-list");
        return "product-show";
    }

    @GetMapping("/product/details/{productId}")
    public String productDetails(@PathVariable int productId, HttpSession session, Model model) {
        // 获取当前用户
        User user = getUser(session);
        if (user == null) {
            return "redirect:/login"; // 未登录则跳转到登录页
        }

        // 获取产品详情
        ResponseEntity<ProductDetailsDTO> response = productService.getProductDetails(productId);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return "redirect:/error"; // 产品不存在时跳转到错误页
        }

        // 将产品详情和权限信息传递给模板
        model.addAttribute("details", response.getBody());

        model.addAttribute("hasGoodsCategoryPermission", hasPermission(user, "GOODS_CATEGORY"));
        model.addAttribute("hasInventoryShowPermission", hasPermission(user, "INVENTORY_SHOW"));
        model.addAttribute("hasSetInventoryAlertPermission", hasPermission(user, "SET_INVENTORY_ALERT"));
        model.addAttribute("hasChangeInventoryPermission", hasPermission(user, "CHANGE_INVENTORY"));
        model.addAttribute("hasGoodsSetPermission", hasPermission(user, "GOODS_SET"));
        model.addAttribute("hasSetComponentPermission", hasPermission(user, "SET_COMPONENT"));

        return handleUserPage(session, model, "product-info");
    }



    @GetMapping("/product/list-legacy")
    public String productslegacy(HttpSession session, Model model) {
        handleUserPage(session, model, "product-list");
        return "product-list";
    }

    @GetMapping("/produce")
    public String produce(HttpSession session, Model model) {
        return handleUserPage(session, model, "produce");
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
    @GetMapping("/creator/objects-orders/custom")
    public String saleGoods(HttpSession session, Model model) {
        return handleUserPage(session, model, "sale-goods");
    }

    @GetMapping("/creator/objects-orders/supply")
    public String purchaseOrder(HttpSession session, Model model) {
        return handleUserPage(session, model, "supply-goods");
    }

    @GetMapping("/creator/objects/customer")
    public String customers(HttpSession session, Model model) {
        return handleUserPage(session, model, "customer");
    }

    @GetMapping("/creator/objects/supplier")
    public String suppliers(HttpSession session, Model model) {
        return handleUserPage(session, model, "supplier");
    }

    @GetMapping("/creator/objects/logistics-company")
    public String logisticsCompanies(HttpSession session, Model model) {
        return handleUserPage(session, model, "logistics-company");
    }



    //买卖系统部分
    @GetMapping("/trade")
    public String trade(HttpSession session, Model model) {
        return handleUserPage(session, model, "trade");
    }

    @GetMapping("/trade/sale")
    public String tradeSale(HttpSession session, Model model) {
        return handleUserPage(session, model, "trade-sale");
    }

    @GetMapping("/trade/supply")
    public String tradeSupply(HttpSession session, Model model) {
        return handleUserPage(session, model, "trade-supply");
    }

    @GetMapping("/trade/orders")
    public String tradeOrders(HttpSession session, Model model) {
        return handleUserPage(session, model, "trade-orders");
    }
}
