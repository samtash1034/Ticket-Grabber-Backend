package com.project.orm.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class UserModel {

    private String userId;

    private String email;

    private String name;

    private String password;

    private Date birthday;

    private String createUser;

    private Timestamp createTime;

    private String updateUser;

    private Timestamp updateTime;
}
