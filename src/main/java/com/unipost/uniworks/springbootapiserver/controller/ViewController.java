package com.unipost.uniworks.springbootapiserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/show-page")
    public String showPage(Model model) {
        // 템플릿에 전달할 데이터 추가z
        model.addAttribute("title", "Welcome to Mustache");
        model.addAttribute("message", "This is a sample page rendered using Mustache.");

        // resources/templates/sample.mustache 파일을 렌더링
        return "test1";
    }
}
