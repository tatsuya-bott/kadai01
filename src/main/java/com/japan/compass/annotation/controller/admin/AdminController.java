package com.japan.compass.annotation.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    /*
     * 管理ログイン画面
     */
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    /*
     * 管理TOP画面
     */
    @GetMapping("/top")
    public String top() {
        return "admin/top";
    }
}
