package com.project.user.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterReq {

//    @NotBlank
//    @Pattern(regexp = RegexUtil.EMAIL_REGEX)
    @Schema(description = "帳號")
    private String email;

//    @NotBlank
//    @Pattern(regexp = RegexUtil.PASSWORD_REGEX)
    @Schema(description = "密碼")
    private String password;

//    @NotBlank
//    @Size(max = 30)
    @Schema(description = "姓名")
    private String name;

//    @NotNull
    @Schema(description = "生日")
    private Long birthday;
}
