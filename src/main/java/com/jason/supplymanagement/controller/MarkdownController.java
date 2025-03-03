package com.jason.supplymanagement.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/markdown")
public class MarkdownController {

    @GetMapping("/content")
    public ResponseEntity<String> getMarkdownContent(@RequestParam(required = false, defaultValue = "about") String file) throws IOException {
        // 安全性校验：防止路径遍历攻击
        if (file.contains("..") || file.contains("/")) {
            return ResponseEntity.badRequest().body("Invalid file name");
        }

        // 构建文件路径
        String filePath = "static/markdown/" + file + ".md";
        ClassPathResource resource = new ClassPathResource(filePath);

        // 检查文件是否存在
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 读取文件内容
        String markdownContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        return ResponseEntity.ok(markdownContent);
    }

}