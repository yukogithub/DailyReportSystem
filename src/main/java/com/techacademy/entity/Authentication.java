package com.techacademy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
@Entity
@Table(name="authentication")
public class Authentication {

    /** 権限用の列挙型 */
    public static enum Role {
        一般, 管理者
    }

    /** 主キー。社員番号。20桁。null不許可 */
    @Id
    @Column(length = 20, nullable = false)
    @NotEmpty
    @Length(max=20)
    private String code;

    /** パスワード。255桁。null不許可 */
    @Column(length = 255, nullable = false)
//    @NotEmpty
    @Length(max=255)
    private String password;

    /** 権限。10桁。null不許可 */
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    /** 従業員テーブルのID */
    @OneToOne
    @JoinColumn(name="employee_id", referencedColumnName="id")
    private Employee employee;
}
