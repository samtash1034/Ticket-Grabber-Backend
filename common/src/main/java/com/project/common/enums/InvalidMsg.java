package com.project.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 預期不應該出現的錯誤訊息
 */
@Getter
@AllArgsConstructor
public enum InvalidMsg {

    /**
     * 使用者狀態錯誤
     */
    ERROR1("使用者狀態錯誤"),
    /**
     * 帳戶無法修改
     */
    ERROR2("帳戶無法修改"),
    /**
     * 查無啟用權杖
     */
    ERROR3("查無啟用權杖"),
    /**
     * 查無重設密碼權杖
     */
    ERROR4("查無重設密碼權杖"),
    /**
     * 查無多因子驗證權杖
     */
    ERROR5("查無多因子驗證權杖"),
    /**
     * 權杖解析失敗
     */
    ERROR6("權杖解析失敗"),
    /**
     * 匯入資料不可超過一千筆
     */
    ERROR7("匯入資料不可超過一千筆"),

    /**
     * 網域名稱錯誤
     */
    ERROR8("網域名稱錯誤"),

    /**
     * ERP資料無法解析
     */
    ERROR9("ERP資料無法解析"),

    /**
     * ERP採購單資料版次異常
     */
    ERROR10("ERP採購單資料版次異常"),

    /**
     * ERP回傳之HttpStatus異常
     */
    ERROR11("ERP回傳之HttpStatus異常"),
    /**
     * 資料異常(不該是null)
     */
    ERROR12("資料異常"),
    /**
     * 資料異常(不該是null)
     */
    ERROR13("無法執行未啟用排程"),

    /**
	 * 傳入權限已禁用
	 */
	ERROR609("傳入權限已禁用"),
    ERROR501("此排程已停用，無法重複停用"),
    ERROR502("此排程已啟用，無法重複啟用");


    private final String msg;

}
