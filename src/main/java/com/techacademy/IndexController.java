package com.techacademy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
public class IndexController {

    @Autowired // Serviceオブジェクトを自動的に注入
    private ReportService service;

    @GetMapping("/")
    public String getIndex(@AuthenticationPrincipal UserDetail userDetail, @ModelAttribute Report report, Model model) {
        // 全件検索結果をModelに登録
        model.addAttribute("reportlist", service.getReportList());
        // 件数をカウントしてModelに登録
        model.addAttribute("reportCount", service.countReports());
        // ログインユーザー名をModelに登録
        Employee employee = userDetail.getUser();
        model.addAttribute("employeeName", employee.getName());
        // report/list.htmlに画面遷移
        return "index";
    }

}
