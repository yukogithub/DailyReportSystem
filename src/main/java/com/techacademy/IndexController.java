package com.techacademy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;
import com.techacademy.service.UserDetail;

@Controller
public class IndexController {

    @Autowired
    private ReportRepository reportRepository;

    @GetMapping("/")
    public String getIndex(@AuthenticationPrincipal UserDetail userDetail, @ModelAttribute Report report, Model model) {

        // ログインユーザー名をModelに登録
        Employee employee = userDetail.getUser();
        model.addAttribute("employeeName", employee.getName());

        // ログインユーザーに関連するレポートを検索
        List<Report> reportlist = reportRepository.findByEmployee(employee);
        // モデルにレポートを追加
        model.addAttribute("reportlist", reportlist);

        // 件数をカウントしてModelに登録
        int reportCount = reportlist.size();
        model.addAttribute("reportCount", reportCount);

        // report/list.htmlに画面遷移
        return "index";
    }

}
