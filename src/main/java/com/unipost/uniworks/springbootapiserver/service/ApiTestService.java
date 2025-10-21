package com.unipost.uniworks.springbootapiserver.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApiTestService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void callMoinApi(int count) {
//        String url = "http://43.203.66.218:8080/TEST/returnMoinData_i.do";
        String url = "http://localhost:8080/TEST/returnMoinData_i.do";

        // 샘플 JSON 데이터 구성
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("seq", count);
        requestData.put("userId", "test");
        requestData.put("status", "success");

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 Entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            System.out.println("응답 상태코드: " + response.getStatusCode());
            System.out.println("응답 본문: " + response.getBody());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}