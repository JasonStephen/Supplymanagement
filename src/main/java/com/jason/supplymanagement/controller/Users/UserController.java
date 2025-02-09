package com.jason.supplymanagement.controller.Users;

import com.jason.supplymanagement.entity.Users.User;
import com.jason.supplymanagement.service.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
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

    // 获取当前用户信息
    @GetMapping("/currentUser")
    public ResponseEntity<User> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(user);
    }


    // 更新用户信息
    @PutMapping("/updateUserInfo")
    public ResponseEntity<Void> updateUserInfo(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // 只更新传入的非空字段
            if (username != null) {
                user.setUsername(username);
            }
            if (phone != null) {
                user.setPhone(phone);
            }
            if (email != null) {
                user.setEmail(email);
            }

            userService.updateUser(user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 更新用户密码
    @PutMapping("/updatePassword")
    public ResponseEntity<Void> updatePassword(@RequestParam String password, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // 只更新密码字段
            user.setPassword(password);
            userService.updateUser(user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 上传用户头像
    @PostMapping("/uploadAvatar")
    public ResponseEntity<Void> uploadAvatar(@RequestParam("avatar") MultipartFile file, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            // 保存文件到绝对路径
            String uploadDir = "E:/Project/SupplyManagement/uploads/avatar/"; // 绝对路径
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = user.getUserId() + "." + file.getOriginalFilename().split("\\.")[1];
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());

            // 更新用户头像路径
            user.setAvatar("/api/v1/user/uploads/avatar/" + fileName);
            userService.updateUser(user);

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/uploads/avatar/{fileName}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String fileName) {
        try {
            String uploadDir = "E:/Project/SupplyManagement/uploads/avatar/";
            Path path = Paths.get(uploadDir + fileName);
            byte[] imageBytes = Files.readAllBytes(path);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }





}