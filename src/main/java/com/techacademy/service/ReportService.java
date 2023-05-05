package com.techacademy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;
import com.techacademy.repository.EmployeeRepository;

@Service
public class ReportService {
//    private final EmployeeRepository employeeRepository;
    private final ReportRepository reportRepository;

    public ReportService(EmployeeRepository repository, ReportRepository reportRepository) {
//        this.employeeRepository = repository;
        this.reportRepository = reportRepository;
    }


    /** 全件を検索して返す */
    public List<Report> getReportList(){
        // リポジトリのfindAllメソッドを呼び出す
        return reportRepository.findAll();
    }

    /** 1件検索して返す*/
    public Report getReport(Integer id){
        // リポジトリのfindAllメソッドを呼び出す
        return reportRepository.findById(id).get();
    }

    /** Reportの登録を行う */
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }


    /** Reportの件数を数える*/
    public long countReports() {
        return reportRepository.count();
    }

}
