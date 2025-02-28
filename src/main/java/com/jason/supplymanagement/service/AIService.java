package com.jason.supplymanagement.service;

import com.jason.supplymanagement.controller.AI.AISearchController;
import com.jason.supplymanagement.entity.Product.Product;
import com.jason.supplymanagement.entity.Product.ProductCategory;
import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;
import com.jason.supplymanagement.entity.Supply.PurchaseOrder;
import com.jason.supplymanagement.entity.Custom.SalesOrder;
import com.jason.supplymanagement.service.Product.ProductCategoryService;
import com.jason.supplymanagement.service.Product.ProductService;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private static final String API_URL = "https://api.siliconflow.cn/v1/chat/completions";
    private static final String TOKEN = "sk-mvtjliqfyyiubsfrzebjbujmxkygxcpjvdoacmcyxwksrrqj"; // 替换为你的API Token

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AISearchController aiSearchController;

    public String callAiApi(String content) {
        try {
            // 构建完整的提示词（无换行）
            String userPrompt = String.format(
                    "你是一个AI机器人。用户向你提问了 '%s'，请根据用户的需求返回对应的demandCode，并判断用户的提问语言（如果用户明确要求回复语言，以用户要求为准），输出language，并将demandCode和language输出为JSON。需求代码对照如下：| 用户的需求 | demandCode | | ----------------- | ----------- | | 正常交流 | 00 | | 不理解用户的意思 | 01 | | 用户交流菜单（你认为用户比较有趣） | 02 | | 查询产品，且明确查询的内容 | 10A | | 查询产品，未明确查询内容 | 10B | | 库存告警查询 | 11 | | 查询的是进行中的或物流的订单 | 12A | | 查询的是采购或供应订单 | 12B | | 查询的是销售订单 | 12C | | 查询所有订单 | 12Y | | 未明确查询订单的类别或内容 | 12Z | | 彩蛋（用户的词中必须包括例如表演的词汇（否则demandCode算02），给他们表演一个） | 90 |",
                    content
            );

            // 构建请求体
            String requestBody = String.format("""
                {
                    "model": "deepseek-ai/DeepSeek-V2.5",
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
            return parseResponse(response.getBody(), content);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while calling AI API";
        }
    }

    private String parseResponse(String response, String originalContent) {
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

            // 根据 demandCode 处理不同的逻辑
            switch (demandCode) {
                case "00":
                    return handleNormalConversation(originalContent, language);
                case "01":
                    return handleUnclearQuestion(language);
                case "02":
                    return handleCuteQuestion(language);
                case "10A":
                    return handleProductSearch(originalContent, language);
                case "10B":
                    return handleUnclearProductDemand(language);
                case "11":
                    return handleInventoryAlert(language);
                case "12A":
                    return handleLogisticsOrder(language);
                case "12B":
                    return handleSupplyOrder(language);
                case "12C":
                    return handleSalesOrder(language);
                case "12Y":
                    return handleAllOrders(language);
                case "12Z":
                    return handleUnclearOrder(language);
//                case "90":
//                    return handlePerformance(language);

                default:
                    return "demandCode: " + demandCode + ", language: " + language;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error occurred while parsing the response";
        }
    }

    /* ===================== */
    /* 以下函数为仅需对话的函数 */
    /* ===================== */

    //DemandCode为00
    private String handleNormalConversation(String content, String language) {
        String prompt = String.format("你是AI客服小青，你的用户想和您进行正常的交流沟通，他发送的内容是 '%s'，请您与他交流。请使用 %s 语言回复。", content, language);
        return callAiWithSimplePrompt(prompt);
    }

    //DemandCode为01
    private String handleUnclearQuestion(String language) {
        String prompt = String.format("你是AI客服小青，但是用户提的问题不清晰，请您使用 %s 语言告诉他他提的问题不清晰。", language);
        return callAiWithSimplePrompt(prompt);
    }

    //DemandCode为02
    private String handleCuteQuestion(String language) {
        String prompt = String.format("你是AI客服小青，你的用户提了一个非常可爱的问题，请您使用 %s 跟他说下面的话：“您真是太可爱了，恭喜获得年度最可爱用户称号”。", language);
        return callAiWithSimplePrompt(prompt);
    }

    //DemandCode为10B
    private String handleUnclearProductDemand(String language) {
        String prompt = String.format("你是AI机器人小青，你的用户并没有明确查询的是什么产品，请您提醒他。", language);
        return callAiWithSimplePrompt(prompt);
    }

    //DemandCode为12Z
    private String handleUnclearOrder(String language) {
        String prompt = String.format("你是AI机器人小青，你的用户并没有明确查询的是什么类型的订单，请您提醒他。", language);
        return callAiWithSimplePrompt(prompt);
    }

    /* ===================== */
    /* 以下函数为需要查询的函数 */
    /* ===================== */

    //DemandCode 为 10A
    private String handleProductSearch(String originalContent, String language) {
        try {
            // 步骤1: 请求 /product-categories 接口获取类别详情
            String categoryDetails = fetchProductCategories();
            System.out.println("Category Details: " + categoryDetails);

            // 步骤2: 请求 AI，获取用户查询的产品名称和类别名称
            String userQuery = callAiWithCustomPrompt(
                    originalContent,
                    categoryDetails,
                    "你是AI客服小青，用户的问题是：{content}。数据库中的产品共分为这些类别： {CategoryDetails}。请你按词拆分并判断用户想要查询的产品名称 {query}和 产品类别{category_name}。请只输出JSON，包含产品名称 {query}和 产品类别{category_name}，如果用户没有指定查询的类别，则不输出{category_name}。",
                    false // 不需要清理 JSON
            );

            System.out.println("AI Query Result: " + userQuery);

            // 提取 query 和 category_name
            JSONObject queryResult = new JSONObject(userQuery.replace("```json", "").replace("```", "").trim());
            String query = queryResult.getString("query");
            String categoryName = queryResult.optString("category_name", null);

            // 步骤3: 请求 /ai/product/search 接口
            String searchResult = fetchSearchResult(query, categoryName);
            System.out.println("Search Result: " + searchResult);

            // 步骤4: 请求 AI，生成最终的用户响应
            return callAiWithCustomPrompt(
                    searchResult, // searchResult 是 JSON 字符串
                    language,
                    "你是AI机器人小青，你的用户想要查询的产品信息如下：{SearchResult}（如果没有内容就是没有结果）。请您用一段简短的话用{language}语言将产品信息介绍给他（注意不要提及任何和编号有关的内容）。",
                    false // 不需要清理 JSON，直接保留原始格式
            );

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while handling product search";
        }
    }

    // DemandCode 为 11
    private String handleInventoryAlert(String language) {
        try {
            // 步骤1: 调用 AISearchController 中的 /ai/inventory/alert 接口
            ResponseEntity<Map<String, Object>> responseEntity = aiSearchController.getInventoryAlerts();
            Map<String, Object> alertResponse = responseEntity.getBody();

            // 步骤2: 格式化告警数据为 JSON 字符串
            JSONObject result = new JSONObject();
            if (alertResponse != null && "success".equals(alertResponse.get("status"))) {
                List<Map<String, String>> data = (List<Map<String, String>>) alertResponse.get("data");
                JSONArray jsonArray = new JSONArray(data);
                result.put("status", "success");
                result.put("data", jsonArray);
                result.put("total", data.size());
            } else {
                result.put("status", "error");
                result.put("message", "Failed to fetch inventory alert data.");
            }

            // 步骤3: 请求 AI，生成最终的用户响应
            return callAiWithCustomPrompt(
                    result.toString(), // 告警数据是 JSON 字符串
                    language,
                    "你是AI机器人小青，以下是库存告警的产品信息：{ResultData}（如果没有内容就是没有告警产品）。请您用一段简短的话用{language}语言将告警信息告诉他，请告诉用户产品库存不足(不需要说几件)，并给他建议。",
                    false // 不需要清理 JSON，直接保留原始格式
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while handling inventory alert";
        }
    }

    // DemandCode 为 12A
    private String handleLogisticsOrder(String language) {
        try {
            // 步骤1: 调用 /ai/orders/logistics 接口
            ResponseEntity<Map<String, Object>> responseEntity = aiSearchController.getAllLogisticsOrders();
            Map<String, Object> logisticsResponse = responseEntity.getBody();

            // 步骤2: 格式化物流订单数据为 JSON 字符串
            JSONObject result = new JSONObject();
            if (logisticsResponse != null && "success".equals(logisticsResponse.get("status"))) {
                List<LogisticsOrder> data = (List<LogisticsOrder>) logisticsResponse.get("data");
                JSONArray jsonArray = new JSONArray(data);
                result.put("status", "success");
                result.put("data", jsonArray);
                result.put("total", data.size());
            } else {
                result.put("status", "error");
                result.put("message", "Failed to fetch logistics order data.");
            }

            // 步骤3: 请求 AI，生成最终的用户响应
            return callAiWithCustomPrompt(
                    result.toString(), // 物流订单数据是 JSON 字符串
                    language,
                    "你是AI机器人小青，以下是所有进行中或已完成的订单信息：{ResultData}（如果没有内容就是没有物流订单）。请您用一段简短的话用{language}语言将物流订单信息介绍给他。其中purchase或sales的order的status为3为已完成，2为进行中，1为待签署，0为待成交；logistics-order的status为0为进行中，1为已完成，2为待签收。重点告诉用户进行中的和待签收的订单信息",
                    false // 不需要清理 JSON，直接保留原始格式
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while handling logistics order";
        }
    }

    //DemandCode 为12B
    private String handleSupplyOrder(String language) {
        try {
            // 步骤1: 调用 /ai/orders/supply 接口
            ResponseEntity<Map<String, Object>> responseEntity = aiSearchController.getAllPurchaseOrders();
            Map<String, Object> supplyResponse = responseEntity.getBody();

            // 步骤2: 格式化供应订单数据为 JSON 字符串
            JSONObject result = new JSONObject();
            if (supplyResponse != null && "success".equals(supplyResponse.get("status"))) {
                List<PurchaseOrder> data = (List<PurchaseOrder>) supplyResponse.get("data");
                JSONArray jsonArray = new JSONArray(data);
                result.put("status", "success");
                result.put("data", jsonArray);
                result.put("total", data.size());
            } else {
                result.put("status", "error");
                result.put("message", "Failed to fetch supply order data.");
            }

            // 步骤3: 请求 AI，生成最终的用户响应
            return callAiWithCustomPrompt(
                    result.toString(), // 供应订单数据是 JSON 字符串
                    language,
                    "你是AI机器人小青，以下是所有待成交的采购订单信息：{ResultData}（如果没有内容就是没有采购订单）。请您用一段简短的话用{language}语言将status为0(待接单的)的采购订单信息告诉他。",
                    false // 不需要清理 JSON，直接保留原始格式
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while handling supply order";
        }
    }

    //DemandCode 为 12C
    private String handleSalesOrder(String language) {
        try {
            // 步骤1: 调用 /ai/orders/purchase 接口
            ResponseEntity<Map<String, Object>> responseEntity = aiSearchController.getAllSalesOrders();
            Map<String, Object> purchaseResponse = responseEntity.getBody();

            // 步骤2: 格式化采购订单数据为 JSON 字符串
            JSONObject result = new JSONObject();
            if (purchaseResponse != null && "success".equals(purchaseResponse.get("status"))) {
                List<SalesOrder> data = (List<SalesOrder>) purchaseResponse.get("data");
                JSONArray jsonArray = new JSONArray(data);
                result.put("status", "success");
                result.put("data", jsonArray);
                result.put("total", data.size());
            } else {
                result.put("status", "error");
                result.put("message", "Failed to fetch purchase order data.");
            }

            // 步骤3：请求 AI，生成最终的用户响应
            return callAiWithCustomPrompt(
                    result.toString(), // 采购订单数据是 JSON 字符串
                    language,
                    "你是AI机器人小青，以下是所有待成交的销售订单信息：{ResultData}（如果没有内容就是没有销售订单）。请您用一段简短的话用{language}语言将status为0(待接单的)的采购订单信息告诉他。",
                    false // 不需要清理 JSON，直接保留原始格式
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while handling purchase order";
        }
    }

    //DemandCode 为 12Y
    private String handleAllOrders(String language) {
        try {
            // 步骤1: 调用 /ai/orders/supply 和 /ai/orders/sale 接口
            ResponseEntity<Map<String, Object>> supplyResponseEntity = aiSearchController.getAllPurchaseOrders();
            ResponseEntity<Map<String, Object>> saleResponseEntity = aiSearchController.getAllSalesOrders();
            Map<String, Object> supplyResponse = supplyResponseEntity.getBody();
            Map<String, Object> saleResponse = saleResponseEntity.getBody();

            // 步骤2: 格式化所有订单数据为 JSON 字符串
            JSONObject result = new JSONObject();
            JSONArray allOrders = new JSONArray();

            if (supplyResponse != null && "success".equals(supplyResponse.get("status"))) {
                List<PurchaseOrder> supplyData = (List<PurchaseOrder>) supplyResponse.get("data");
                for (PurchaseOrder order : supplyData) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "supply");
                    jsonObject.put("orderId", order.getPurchaseOrderId());
                    jsonObject.put("status", order.getStatus());
                    allOrders.put(jsonObject);
                }
            }

            if (saleResponse != null && "success".equals(saleResponse.get("status"))) {
                List<SalesOrder> saleData = (List<SalesOrder>) saleResponse.get("data");
                for (SalesOrder order : saleData) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "sale");
                    jsonObject.put("orderId", order.getSalesOrderId());
                    jsonObject.put("status", order.getStatus());
                    allOrders.put(jsonObject);
                }
            }

            result.put("status", "success");
            result.put("data", allOrders);
            result.put("total", allOrders.length());

            // 步骤3: 请求 AI，生成最终的用户响应
            return callAiWithCustomPrompt(
                result.toString(), // 所有订单数据是 JSON 字符串
                language,
                "你是AI机器人小青，以下是所有待成交的订单信息（包括供应和销售）：{ResultData}（如果没有内容就是没有订单）。请您用一段简短的话用{language}语言将所有信息告诉他。",
                false // 不需要清理 JSON，直接保留原始格式
            );
        } catch (Exception e) {
            e.printStackTrace();
        return "Error occurred while handling all orders";
        }
    }








    private String fetchProductCategories() {
        try {
            // 直接调用 ProductCategoryService 的方法
            List<ProductCategory> categories = productCategoryService.getAllProductCategories();

            // 将类别列表转换为 JSON 格式
            JSONArray jsonArray = new JSONArray();
            for (ProductCategory category : categories) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("categoryId", category.getCategoryId());
                jsonObject.put("categoryName", category.getCategoryName());
                jsonArray.put(jsonObject);
            }

            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while fetching product categories";
        }
    }

    private String fetchSearchResult(String query, String categoryName) {
        try {
            // 将类别名称映射为 category_id
            Integer categoryId = null;
            if (categoryName != null && !categoryName.isEmpty()) {
                categoryId = mapCategoryNameToId(categoryName);
            }

            // 调用 ProductService 的方法
            Page<Product> products = productService.getProducts(query, categoryId, PageRequest.of(0, 10)); // 默认第一页，每页10条数据

            // 将结果转换为 JSON 格式
            JSONArray jsonArray = new JSONArray();
            for (Product product : products.getContent()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", product.getName());
                jsonObject.put("description", product.getDescription());
                jsonObject.put("category", product.getCategory() != null ? product.getCategory().getCategoryName() : null);
                jsonObject.put("price", product.getPrice());
                jsonArray.put(jsonObject);
            }

            // 构造返回结果
            JSONObject result = new JSONObject();
            result.put("status", "success");
            result.put("data", jsonArray);
            result.put("total", products.getTotalElements());
            result.put("page", 1); // 默认第一页
            result.put("size", 10); // 默认每页10条数据

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while fetching search result";
        }
    }

    private Integer mapCategoryNameToId(String categoryName) {
        // 获取所有类别
        List<ProductCategory> categories = productCategoryService.getAllProductCategories();

        // 遍历查找匹配的类别
        for (ProductCategory category : categories) {
            if (categoryName.equalsIgnoreCase(category.getCategoryName())) {
                return category.getCategoryId();
            }
        }

        return null;
    }

    /**
     * 获取库存告警数据
     */
    private String fetchInventoryAlertData() {
        try {
            // 调用 ProductService 的方法获取库存告警产品
            List<Product> lowStockProducts = productService.getLowStockProducts();

            // 将告警产品转换为 JSON 格式
            JSONArray jsonArray = new JSONArray();
            for (Product product : lowStockProducts) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", product.getName());
                jsonObject.put("category", product.getCategory() != null ? product.getCategory().getCategoryName() : null);
                jsonArray.put(jsonObject);
            }

            // 构造返回结果
            JSONObject result = new JSONObject();
            result.put("status", "success");
            result.put("data", jsonArray);
            result.put("total", lowStockProducts.size());

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while fetching inventory alert data";
        }
    }





    private String callAiWithCustomPrompt(String parameter1, String parameter2, String promptTemplate,  boolean isJsonCleanRequired) {
        try {
            String cleanedAdditionalData = parameter1; // 默认直接使用 additionalData

            // 如果需要清理 JSON 数据
            if (isJsonCleanRequired) {
                // 直接保留 additionalData 的原始格式，不需要解析为 JSONObject
                cleanedAdditionalData = parameter1.replaceAll("\"", "\\\""); // 仅转义双引号，保留 \r\n
            }

            // 替换提示词中的占位符
            String userPrompt = promptTemplate
                    .replace("{content}", parameter1)

                    .replace("{SearchResult}", cleanedAdditionalData)
                    .replace("{ResultData}", cleanedAdditionalData)

                    .replace("{language}", parameter2)
                    .replace("{CategoryDetails}", parameter2);

            // 构建请求体
            String requestBody = String.format("""
        {
            "model": "deepseek-ai/DeepSeek-V2.5",
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
            System.out.println("Request Body Sent to AI API:");
            System.out.println(requestBody); // 打印完整的请求体

            // 发送请求并获取响应
            HttpResponse<String> response = Unirest.post(API_URL)
                    .header("Authorization", "Bearer " + TOKEN)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .asString();

            // 打印响应（调试用）
            System.out.println("AI Response: " + response.getBody());

            // 解析响应并返回内容
            JSONObject jsonResponse = new JSONObject(response.getBody());
            if (jsonResponse.has("choices")) {
                JSONObject choice = jsonResponse.getJSONArray("choices").getJSONObject(0);
                return choice.getJSONObject("message").getString("content");
            } else {
                // 如果没有 choices 字段，返回错误信息
                return "Error: Invalid AI response format. Expected 'choices' field.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while calling AI API with custom prompt";
        }
    }




    private String callAiWithSimplePrompt(String prompt) {
        try {
            // 构建请求体
            String requestBody = String.format("""
            {
            "model": "deepseek-ai/DeepSeek-V2.5",
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
            """, prompt.replace("\"", "\\\"")); // 转义双引号

            // 发送请求并获取响应
            HttpResponse<String> response = Unirest.post(API_URL)
                    .header("Authorization", "Bearer " + TOKEN)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .asString();

            // 打印响应（调试用）
            System.out.println("AI Response: " + response.getBody());

            // 解析响应并返回内容
            JSONObject jsonResponse = new JSONObject(response.getBody());
            if (jsonResponse.has("choices")) {
                JSONObject choice = jsonResponse.getJSONArray("choices").getJSONObject(0);
                return choice.getJSONObject("message").getString("content");
            } else {
                // 如果没有 choices 字段，返回错误信息
                return "Error: Invalid AI response format. Expected 'choices' field.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while calling AI API with simple prompt";
        }
    }
}
