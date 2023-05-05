package com.techacademy.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;


@Controller
@RequestMapping("report")
public class ReportController {
    private final ReportService service;

    @Autowired
    public ReportController(ReportService service) {
        this.service = service;
    }

//    @Autowired
//    private AuthenticationRepository authenticationRepository;


    /** 一覧画面を表示 */
    @GetMapping("/report_list")
    public String getList(@AuthenticationPrincipal UserDetail userDetail, @ModelAttribute Report report, Model model) {
        // 全件検索結果をModelに登録
        model.addAttribute("reportlist", service.getReportList());
        // 件数をカウントしてModelに登録
        model.addAttribute("reportCount", service.countReports());
        // ログインユーザー名をModelに登録
        Employee employee = userDetail.getUser();
        model.addAttribute("employeeName", employee.getName());
        // report/list.htmlに画面遷移
        return "report/report_list";
    }


    /** 詳細画面を表示 */
    @GetMapping("/report_detail/{id}/")
    public String getReport(@PathVariable("id") Integer id, Model model) {
        // Modelに登録
        model.addAttribute("report", service.getReport(id));
        // report詳細画面に遷移
        return "report/report_detail";
    }

    /** Report登録画面を表示 */
    @GetMapping("/report_register")
    public String getRegister(@AuthenticationPrincipal UserDetail userDetail, @ModelAttribute Report report, Model model) {
        Employee employee = userDetail.getUser();
        model.addAttribute("employeeName", employee.getName());
//        model.addAttribute("employeeId", employee.getId());

        // report登録画面に遷移
        return "report/report_register";
    }

    /** Report登録処理 */
    @PostMapping("/report_register")
    public String postRegister(@AuthenticationPrincipal UserDetail userDetail, @Validated Report report, BindingResult res, Model model) {
        if(res.hasErrors()) {
            //エラーあり
            return getRegister(userDetail, report, model);
        }
        // Report登録
//        report.setEmployeeId(employee.getId());
        report.setCreatedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());

        service.saveReport(report); //Reportを保存

        // 一覧画面にリダイレクト
        return "redirect:/report/report_list";
    }

    /** Report更新画面を表示 */
    @GetMapping("/report_update/{id}")
    public String getReport2(@PathVariable("id") Integer id, Model model) {
        // Modelに登録
        model.addAttribute("report", service.getReport(id));
        // report更新画面に遷移
        return "report/report_update";
    }


    /** Report更新処理 */
    @PostMapping("/report_update/{id}")
    public String postReport(@PathVariable("id") Integer id, @Validated Report report, BindingResult res, Model model) {
        if(res.hasErrors()) {
            //エラーあり
            return getReport2(id, model);
        }
        // Report更新
        report.setUpdatedAt(LocalDateTime.now());

        service.saveReport(report); //Reportを保存

        // 一覧画面にリダイレクト
        return "redirect:/report/report_list";
    }

}

