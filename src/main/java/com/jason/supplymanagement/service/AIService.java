package com.jason.supplymanagement.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.stereotype.Service;

@Service
public class AIService {
    private static final String API_URL = "https://api.siliconflow.cn/v1/chat/completions";
    private static final String TOKEN = "sk-mvtjliqfyyiubsfrzebjbujmxkygxcpjvdoacmcyxwksrrqj"; // 替换为你的API Token

    public String callAiApi(String content) {
        try {
            // 构建完整的提示词（无换行）
            String userPrompt = String.format(
                    "你是一个AI机器人。用户向你提问了 '%s'，请根据用户的需求返回对应的demandCode，并判断用户的提问语言（如果用户明确要求回复语言，以用户要求为准），输出language，并将demandCode和language输出为JSON。需求代码对照如下：| 用户的需求 | demandCode | | ----------------- | ----------- | | 正常交流 | 00 | | 不理解用户的意思 | 01 | | 用户交流菜单（你认为用户比较有趣） | 02 | | 查询产品，且明确查询的内容 | 10A | | 查询产品，未明确查询内容 | 10B | | 库存告警查询 | 11 | | 查询的是进行中的或物流的订单 | 12A | | 查询的是供应订单 | 12B | | 查询的是采购订单 | 12C | | 查询所有订单 | 12Y | | 未明确查询订单的类别或内容 | 12Z | | 彩蛋（触发关键词，给他们表演一个） | 90 |",
                    content
            );

            // 构建请求体
            String requestBody = String.format("""
                {
                    "model": "Pro/deepseek-ai/DeepSeek-V3",
                    "messages": [
                        {
                            "role": "user",
                            "content": "%s"
                        }
                    ],
                    "stream": false,
                    "max_tokens": 4096,
                    "stop": null,
                    "temperature": 0.7,
                    "top_p": 0.7,
                    "top_k": 50,
                    "frequency_penalty": 0.5,
                    "n": 1,
                    "response_format": {
                        "type": "text"
                    }
                }
                """, userPrompt.replace("\"", "\\\"")); // 转义双引号

            // 打印请求体（调试用）
            System.out.println("Request Body: " + requestBody);

            // 发送请求并获取响应
            HttpResponse<String> response = Unirest.post(API_URL)
                    .header("Authorization", "Bearer " + TOKEN)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .asString();

            // 打印响应（调试用）
            System.out.println("Response: " + response.getBody());

            // 解析响应并提取 demandCode 和 language
            return parseResponse(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while calling AI API";
        }
    }

    private String parseResponse(String response) {
        try {
            // 解析顶层 JSON
            JSONObject jsonResponse = new JSONObject(response);

            // 获取 choices 数组中的第一个元素
            JSONObject choice = jsonResponse.getJSONArray("choices").getJSONObject(0);

            // 获取 message 中的 content
            String content = choice.getJSONObject("message").getString("content");

            // 提取 content 中的 JSON 字符串（去除 Markdown 代码块标记）
            String jsonContent = content.replace("```json", "").replace("```", "").trim();

            // 解析提取的 JSON 字符串
            JSONObject result = new JSONObject(jsonContent);

            // 提取 demandCode 和 language
            String demandCode = result.getString("demandCode");
            String language = result.getString("language");

            // 返回结果
            return "demandCode: " + demandCode + ", language: " + language;
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error occurred while parsing the response";
        }
    }
}
