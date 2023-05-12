package com.techacademy.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.entity.Authentication;
import com.techacademy.entity.Employee;
import com.techacademy.repository.AuthenticationRepository;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("employee")
public class EmployeeController {
    private final EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /** 一覧画面を表示 */
    @GetMapping("/list")
    public String getList(@AuthenticationPrincipal UserDetail userDetail, Model model) {
        // 全件検索結果をModelに登録
        model.addAttribute("employeelist", service.getEmployeeList());
        // 件数をカウントしてModelに登録
        model.addAttribute("employeeCount", service.countEmployees());
        // ログインユーザー名をModelに登録
        Employee employee = userDetail.getUser();
        model.addAttribute("employeeName", employee.getName());
        // employee/list.htmlに画面遷移
        return "employee/list";
    }


    /** 詳細画面を表示 */
    @GetMapping("/detail/{id}/")
    public String getEmployee(@AuthenticationPrincipal UserDetail userDetail, @PathVariable("id") Integer id, Model model) {
        // Modelに登録
        model.addAttribute("employee", service.getEmployee(id));
        // ログインユーザー名をModelに登録
        Employee employee = userDetail.getUser();
        model.addAttribute("employeeName", employee.getName());
        // employee詳細画面に遷移
        return "employee/detail";
    }

    /** Employee登録画面を表示 */
    @GetMapping("/register")
    public String getRegister(@AuthenticationPrincipal UserDetail userDetail, @ModelAttribute Employee employee) {
        // employee登録画面に遷移
        return "employee/register";
    }


    /** Employee登録処理 */
    @PostMapping("/register")
    public String postRegister(@AuthenticationPrincipal UserDetail userDetail, @Validated Employee employee, BindingResult res, Model model) {
        if(res.hasErrors()) {
            //エラーあり
            return getRegister(userDetail, employee);
        }
        // Employee登録
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setDeleteFlag(0);
        Authentication authentication = employee.getAuthentication();

        // 新規登録時のみ、同一コードが存在しないか確認
        Optional<Authentication> existingAuthOpt = authenticationRepository.findByCode(authentication.getCode());
        if (existingAuthOpt.isPresent()) {
            // 既に同じコードが存在する場合はエラーを返す
            model.addAttribute("error", "既に存在するコードです。");
            return getRegister(userDetail, employee);
        }

        authentication = service.saveAuthentication(authentication); //Authenticationを保存
        employee.setAuthentication(authentication);
        authentication.setPassword(passwordEncoder.encode(authentication.getPassword()));// 暗号化したパスワード
        service.saveEmployee(employee); //Employeeを保存

        // 一覧画面にリダイレクト
        return "redirect:/employee/list";
    }


    /** Employee更新画面を表示 */
    @GetMapping("/update/{id}")
    public String getEmployeeUpdate(@AuthenticationPrincipal UserDetail userDetail, @PathVariable("id") Integer id, Model model) {
        // ログインユーザー名をModelに登録
        Employee employee = userDetail.getUser();
        model.addAttribute("employeeName", employee.getName());
        // Modelに登録
        model.addAttribute("employee", service.getEmployee(id));
        // employee更新画面に遷移
        return "employee/update";
    }


    /** Employee更新処理 */
    @PostMapping("/update/{id}")
    public String postEmployeeUpdate(@AuthenticationPrincipal UserDetail userDetail, @PathVariable("id") Integer id, @Validated Employee employee, BindingResult res, Model model) {
        if(res.hasErrors()) {
            //エラーあり
            return getEmployeeUpdate(userDetail, id, model);
        }
        // Employee更新情報のセット
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setDeleteFlag(0);

        // フォームから新しい認証情報を取得
        Authentication authentication = employee.getAuthentication();
        // 新しいパスワードが入力されているかチェック
        if(authentication.getPassword().equals("")) {
            // 新しいパスワードが入力されていない場合、現在のパスワードをハッシュ化しないで保存
            Employee dbemployee = service.getEmployee(id);
            authentication.setPassword(dbemployee.getAuthentication().getPassword());
        } else {
            // 新しいパスワードが入力されている場合、ハッシュ化して保存
            authentication.setPassword(passwordEncoder.encode(authentication.getPassword()));
        }

        authentication = service.saveAuthentication(authentication);
        employee.setAuthentication(authentication);
        service.saveEmployee(employee); //Employeeを保存

        // 一覧画面にリダイレクト
        return "redirect:/employee/list";
    }


    /** Employee削除処理 */
    @GetMapping("/delete/{id}")
    public String getEmployee3(@PathVariable("id") Integer id, Model model) {
        // Employeeオブジェクトを取得
        Employee employee = service.getEmployee(id);
        // Employee登録
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setDeleteFlag(1);
        service.saveEmployee(employee);
        // 一覧画面にリダイレクト
        return "redirect:/employee/list";
    }

}
