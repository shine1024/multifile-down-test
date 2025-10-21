package com.unipost.uniworks.springbootapiserver.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Controller
public class ViewController {

    @GetMapping("/dragAndDropTest")
    public String dragAndDropTest(Model model) {
        return "dragAndDropTest";
    }

    @GetMapping("/show-api-test")
    public String showApiTest(Model model) {
        model.addAttribute("title", "Welcome to API TEST");
        model.addAttribute("message", "This is a sample page rendered using Mustache.");

        // resources/templates/sample.mustache 파일을 렌더링
        return "test3";
    }

    @GetMapping("/show-page")
    public String showPage(Model model) throws IOException, WriterException {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials(); // OTP Secret Key 생성

        String secret = key.getKey();
        System.out.println("secret: " + secret);

        String issuer = "MyService";  // 서비스명
        String account = "test";      // 사용자 ID


        // ✅ 직접 otpauth:// URL 생성 (RFC 표준)
        String otpAuthURL = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                issuer, account, secret, issuer
        );

        System.out.println("otpAuthURL: " + otpAuthURL);

        // ✅ ZXing QR 코드 생성
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream pngOutput = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutput);

        byte[] qrImage = pngOutput.toByteArray();

        // ✅ Base64 인코딩 (HTML <img> 태그에서 바로 사용 가능)
        String base64Qr = Base64.getEncoder().encodeToString(qrImage);
        String base64Image = "data:image/png;base64," + base64Qr;

        // ✅ 뷰로 전달
        model.addAttribute("title", "Welcome to Mustache");
        model.addAttribute("message", "OTP QR 코드 등록 테스트");
        model.addAttribute("secret", secret);
        model.addAttribute("base64Image", base64Image);

        // resources/templates/sample.mustache 파일을 렌더링
        return "test1";
    }

    @GetMapping("/show-test")
    public String showTest(Model model) {
        // 템플릿에 전달할 데이터 추가z
        model.addAttribute("title", "Welcome to Mustache");
        model.addAttribute("message", "This is a sample page rendered using Mustache.");

        return "test2";
    }

}


