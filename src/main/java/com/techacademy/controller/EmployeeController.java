package com.techacademy.controller;

import java.time.LocalDateTime;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.techacademy.service.EmployeeService;

@Controller
@RequestMapping("employee")
public class EmployeeController {
    private final EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    /** 一覧画面を表示 */
    @GetMapping("/list")
    public String getList(Model model) {
        // 全件検索結果をModelに登録
        model.addAttribute("employeelist", service.getEmployeeList());
        // employee/list.htmlに画面遷移
        return "employee/list";
    }


    /** 詳細画面を表示 */
    @GetMapping("/detail/{id}/")
    public String getEmployee(@PathVariable("id") Integer id, Model model) {
        // Modelに登録
        model.addAttribute("employee", service.getEmployee(id));
        // employee詳細画面に遷移
        return "employee/detail";
    }

    /** Employee登録画面を表示 */
    @GetMapping("/register")
    public String getRegister(@ModelAttribute Employee employee) {
        // employee登録画面に遷移
        return "employee/register";
    }


    /** Employee登録処理 */
    @PostMapping("/register")
    public String postRegister(@Validated Employee employee, BindingResult res, Model model) {
        if(res.hasErrors()) {
            //エラーあり
            return getRegister(employee);
        }
        // Employee登録
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setDeleteFlag(0);
        Authentication authentication = employee.getAuthentication();
        authentication = service.saveAuthentication(authentication); //Authenticationを保存
        employee.setAuthentication(authentication);
//        authentication.setPassword(null); // 暗号化したパスワード
        service.saveEmployee(employee); //Employeeを保存

        // 一覧画面にリダイレクト
        return "redirect:/employee/list";
    }


    /** Employee更新画面を表示 */
    @GetMapping("/update/{id}")
    public String getEmployee2(@PathVariable("id") Integer id, Model model) {
        // Modelに登録
        model.addAttribute("employee", service.getEmployee(id));
        // employee更新画面に遷移
        return "employee/update";
    }


    /** Employee更新処理 */
    @PostMapping("/update/{id}")
    public String postEmployee(@PathVariable("id") Integer id, @Validated Employee employee, BindingResult res, Model model) {
//        if(res.hasErrors()) {
//            //エラーあり
//            return getEmployee2(id, model);
//        }
        // Employee更新
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setDeleteFlag(0);

        Authentication authentication = employee.getAuthentication();
        authentication = service.saveAuthentication(authentication);
        employee.setAuthentication(authentication);
//        authentication.setPassword(null); // 暗号化したパスワード
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

    /** レコード件数取得 */
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee/list")
    public String getEmployeeCount(Model model) {
        int count = employeeService.getEmployeeCount();
        model.addAttribute("count", count);
        return "employeeCount";
    }

}
