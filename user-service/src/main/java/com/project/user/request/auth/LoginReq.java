
package com.project.user.request.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginReq {

    @Schema(description = "帳號")
    private String email;

    @Schema(description = "密碼【")
    private String password;

}
