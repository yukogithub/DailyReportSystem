package com.techacademy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name="report")
public class Report {

    /** リソース内での連番。主キー。自動生成 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 日報の日付。null不許可 */
    @Column(nullable = false)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportDate;

    /** タイトル。255文字。null不許可 */
    @Column(length = 255, nullable = false)
    @NotEmpty
    @Length(max=20)
    private String title;

    /** 内容。null不許可 */
    @Column(nullable = false)
    @Type(type="text")
    @NotEmpty
    private String content;

    /** 従業員テーブルのID */
    @ManyToOne
    @JoinColumn(name="employee_id", referencedColumnName="id", nullable = false)
    private Employee employee;

    /** 登録日時。null不許可 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時。null不許可 */
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
