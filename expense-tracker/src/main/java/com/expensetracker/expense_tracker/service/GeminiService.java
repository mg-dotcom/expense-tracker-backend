package com.expensetracker.expense_tracker.service;

import com.expensetracker.expense_tracker.model.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final String apiKey;
    private final WebClient webClient;
    //    ตัวส่ง HTTP request ออกไปหา Gemini API เหมือนที่ Postman ทำ แต่ทำใน code แทน

    public record GeminiRequest(List<Content> contents) {}
    public record Content(List<Part> parts) {}
    public record Part(String text) {}
    public record GeminiResponse(List<Candidate> candidates) {}
    public record Candidate(ContentResponse content) {}
    public record ContentResponse(List<PartResponse> parts) {}
    public record PartResponse(String text) {}

    // ต้องกำหนดค่าให้ครบทุกตัวที่เป็น final ในนี้
    public GeminiService(
            @Value("${gemini.api.key}") String apiKey,
            WebClient.Builder webClientBuilder
    ) {
        this.apiKey = apiKey;
        // สร้าง webClient จาก builder ที่ Spring ส่งมาให้
        this.webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public String analyzeExpenses(List<Expense> expenses){
        // สร้าง prompt ให้ Gemini
        String prompt = "วิเคราะห์รายจ่ายต่อไปนี้เป็นภาษาไทย และให้คำแนะนำ: "
                + expenses.toString();

        // สร้าง Object ตามโครงสร้าง
        GeminiRequest requestBody = new GeminiRequest(List.of(new Content(List.of(new Part(prompt)))));

        return webClient.post()
                .uri("/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GeminiResponse.class).map(response -> {
                    // เรียกผ่าน Method สวยๆ ได้เลย ไม่ต้อง Cast!
                    try {
                        return response.candidates().get(0)
                                .content().parts().get(0)
                                .text();
                    } catch (Exception e) {
                        return "วิเคราะห์ไม่สำเร็จ หรือรูปแบบข้อมูลไม่ถูกต้อง";
                    }
                })
                .block();
    }
}