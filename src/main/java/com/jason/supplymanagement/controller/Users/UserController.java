package com.jason.supplymanagement.controller.Users;

import com.jason.supplymanagement.entity.Users.User;
import com.jason.supplymanagement.service.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户注册
    @PostMapping(value = "/register", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Void> register(@RequestParam String username, @RequestParam String email,
                                         @RequestParam String phone, @RequestParam String password, HttpSession session) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPhone(phone);
            user.setPassword(password);
            User registeredUser = userService.register(user);

            // Automatically log in
            session.setAttribute("user", registeredUser);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("用户名已存在")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 用户登录
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        try {
            User user = userService.login(username, password);
            session.setAttribute("user", user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 为用户分配角色
    @PutMapping("/assignRole")
    public ResponseEntity<String> assignRole(@RequestParam int userId, @RequestParam int roleId) {
        try {
            userService.assignRoleToUser(userId, roleId);
            return ResponseEntity.ok("分配角色成功");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("分配角色失败");
        }
    }

    // 查询某角色下的所有用户列表
    @GetMapping("/listUsersByRole")
    public ResponseEntity<List<User>> listUsersByRole(@RequestParam int roleId) {
        try {
            List<User> users = userService.listUsersByRole(roleId);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}