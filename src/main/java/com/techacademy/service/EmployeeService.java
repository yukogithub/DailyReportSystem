package com.techacademy.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.entity.Authentication;
import com.techacademy.entity.Employee;
import com.techacademy.repository.AuthenticationRepository;
import com.techacademy.repository.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AuthenticationRepository authenticationRepository;

    public EmployeeService(EmployeeRepository repository, AuthenticationRepository authenticationRepository) {
        this.employeeRepository = repository;
        this.authenticationRepository = authenticationRepository;
    }


    /** 全件を検索して返す */
    public List<Employee> getEmployeeList(){
        // リポジトリのfindAllメソッドを呼び出す
        return employeeRepository.findAll();
    }

    /** 1件検索して返す*/
    public Employee getEmployee(Integer id){
        // リポジトリのfindAllメソッドを呼び出す
        return employeeRepository.findById(id).get();
    }

    /** Employeeの登録を行う*/
    public Employee saveEmployee(Employee employee) {
        Authentication auth = employee.getAuthentication(); // EmployeeインスタンスからAuthenticationインスタンスを取得
        auth.setEmployee(employee); // AuthenticationインスタンスにEmployeeインスタンスを設定
//        auth.setPassword(passwordEncoder.encode(employee.getAuthentication().getPassword()));  // Authenticationインスタンスのパスワードをハッシュ化

        return employeeRepository.save(employee);
    }

    @Transactional
    public Authentication saveAuthentication(Authentication authentication) {
        return authenticationRepository.save(authentication);
    }

    public int getEmployeeCount() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.size();
    }

}
