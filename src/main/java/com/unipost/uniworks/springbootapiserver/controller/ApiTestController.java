package com.unipost.uniworks.springbootapiserver.controller;

import com.unipost.uniworks.springbootapiserver.service.ApiTestService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiTestController {

    @Autowired
    private ApiTestService apiTestService;

    @PostMapping("/api/decrypt")
    public ResponseEntity<byte[]> decryptFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
        // 실제 복호화 로직은 나중에 추가
        // 현재는 첫 번째 파일을 그대로 반환하도록 예시

        MultipartFile file = files.get(0);
        byte[] data = file.getBytes(); // 복호화 후 바이트 배열로 교체 예정

        String decodedFileName = "decoded_" + file.getOriginalFilename();
        String encodedFileName = URLEncoder.encode(decodedFileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encodedFileName)
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(data);
    }

    @GetMapping("/test-otp")
    public Map<String, String> testOtp(@RequestParam Map<String, String> param) {
        String secret = param.get("secret");
        String inputOtp = param.get("otp");
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean isCodeValid = gAuth.authorize(secret, Integer.parseInt(inputOtp));

        Map<String, String> response = new HashMap<>();
        if (isCodeValid) {
            response.put("MSGT", "success");
            response.put("MSG", "OTP 성공");
        } else {
            response.put("MSGT", "fail");
            response.put("MSG", "OTP 실패");
        }

        return response;
    }

    @GetMapping("/test-call")
    public Map<String, String> testCall(@RequestParam(defaultValue = "1") int count) {
        for (int i = 0; i < count; i++) {
            apiTestService.callMoinApi(i + 1);
        }

        Map<String, String> response = new HashMap<>();
        response.put("MSGT", "success");
        response.put("MSG", count + "건 호출 성공");
        return response;
    }

}
