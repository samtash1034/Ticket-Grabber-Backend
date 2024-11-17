
package com.project.user.response.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRes {

    @Schema(description = "使用者卡稱")
    private String userName;

    @Schema(description = "權杖")
    private String token;

}
