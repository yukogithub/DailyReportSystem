package com.techacademy.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
@Entity
@Table(name="employee")
@Where(clause = "delete_flag = 0")
public class Employee {

    /** リソース内での連番。主キー。自動生成 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 名前。20桁。null不許可 */
    @Column(length = 20, nullable = false)
    @NotEmpty
    @Length(max=20)
    private String name;

    /** 削除フラグ。null不許可 */
    @Column(nullable = false)
    private Integer deleteFlag;

    /** 登録日時。null不許可 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時。null不許可 */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /** Authenticationテーブルとの関連 */
    @OneToOne(mappedBy="employee", cascade = CascadeType.ALL) // Employeeエンティティに対する操作（作成、更新、削除）がAuthenticationエンティティにも適用
    @Valid // Authentication オブジェクトに対してバリデーションを適用
    private Authentication authentication;

    /** Reportテーブルとの関連 */
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @Valid
    private List<Report> reports;
}
