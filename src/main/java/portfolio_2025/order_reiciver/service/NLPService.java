package portfolio_2025.order_reiciver.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NLPService {

    private static final Logger logger = LoggerFactory.getLogger(NLPService.class);
    private final RestTemplate restTemplate;
    
    // Context for Llama, guiding it to parse restaurant orders
    private String context = "You are a restaurant order parser. Parse the given order text and extract menu items, quantities, selected options, and additional notes. Return the result in a structured JSON format.";
    
    public NLPService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    // Method to parse order text using the Llama model
    public String parseOrderText(String orderText) {
        if (orderText == null || orderText.isBlank()) {
            return "Error: Order text is empty.";
        }
        
        String userPrompt = "Parse the following order text and return structured details in JSON format:\n\n" + orderText;
        
        logger.info("parseOrderText called with text: {}", orderText);
        
        String llamaEndpoint = "http://localhost:11434/api/chat";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama3");
        
        List<Map<String, String>> messages = new ArrayList<>();
        // System message to provide context
        messages.add(Map.of("role", "system", "content", context));
        // User message with the order text
        messages.add(Map.of("role", "user", "content", userPrompt));
        
        requestBody.put("messages", messages);
        requestBody.put("stream", false);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        
        try {
            logger.info("Sending request to Llama: {}", requestBody);
            ResponseEntity<Map> response = restTemplate.postForEntity(llamaEndpoint, requestEntity, Map.class);
            Map<String, Object> responseBody = response.getBody();
            
            logger.info("Received response from Llama: {}", responseBody);
            
            if (responseBody != null && responseBody.containsKey("message")) {
                Map<String, String> message = (Map<String, String>) responseBody.get("message");
                return message.get("content");
            } else {
                return "Error: AI response format unexpected.";
            }
        } catch (Exception e) {
            logger.error("Error communicating with Llama", e);
            return "Error communicating with AI: " + e.getMessage();
        }
    }
}
