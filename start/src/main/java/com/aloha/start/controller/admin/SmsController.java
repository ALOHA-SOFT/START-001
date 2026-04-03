package com.aloha.start.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/sms")
public class SmsController {

    @GetMapping("")
    public String sms() {
        return "page/admin/sms/index";
    }
}
