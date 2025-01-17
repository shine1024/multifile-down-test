package com.unipost.uniworks.springbootapiserver.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class FileHandleController {

    @GetMapping("/file-info")
    public Map<String, String> getFileInfo() {
        // 응답 데이터 생성
        Map<String, String> response = new HashMap<>();
        response.put("fileName", "example.txt");
        response.put("fileSize", "155KB");
        response.put("status", "available");

        // JSON 형식으로 응답
        return response;
    }

    @GetMapping("/download-file")
    public ResponseEntity<Resource> downloadFile() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(baos)) {
            for (int i = 1; i <= 5; i++) {
                String fileName = "static/test" + i + ".txt";
                ClassPathResource resource = new ClassPathResource(fileName);

                if (resource.exists()) {
                    try (InputStream inputStream = resource.getInputStream()) {
                        ZipEntry zipEntry = new ZipEntry("test" + i + ".txt");
                        zipOut.putNextEntry(zipEntry);

                        // Copy file content to ZIP output
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            zipOut.write(buffer, 0, length);
                        }
                        zipOut.closeEntry();
                    }
                }
            }

            // Finalize ZIP content
            zipOut.finish();

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=files.zip");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            // Return ZIP as InputStreamResource
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(baos.size())
                    .body(new InputStreamResource(new ByteArrayInputStream(baos.toByteArray())));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/download-file-response")
    public void downloadFile(HttpServletResponse response, @RequestParam(required = false) String count) throws IOException {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=files_" + count + ".zip");

        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            for (int i = 1; i <= 5; i++) {
                String fileName = "static/test" + i + ".txt";
                ClassPathResource resource = new ClassPathResource(fileName);

                if (resource.exists()) {
                    ZipEntry zipEntry = new ZipEntry("test" + i + ".txt");
                    zipOut.putNextEntry(zipEntry);

                    try (InputStream inputStream = resource.getInputStream()) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            zipOut.write(buffer, 0, length);
                        }
                    }
                    zipOut.closeEntry();
                }
            }
            zipOut.finish();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
