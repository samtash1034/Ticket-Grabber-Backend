package com.project.user.response.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserDetailRes {

    private Long id;

    private String email;

    private String name;

    private Date birthday;

    private Long createTime;

}
