package com.jason.supplymanagement.controller.AI;

import com.jason.supplymanagement.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AIDemandController {
    @Autowired
    private AIService aiService;

    @PostMapping("/demand")
    public String submit(@RequestParam("content") String content) {
        // 调用 AIService 获取 API 响应
        return aiService.callAiApi(content);
    }
}
