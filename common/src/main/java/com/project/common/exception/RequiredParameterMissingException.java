package com.project.common.exception;

import com.project.common.enums.CommonCode;

/**
 * 用於前端所傳資料異常時使用，預期應是必填欄位，卻檢查出缺失 <br/>
 * 錯誤代碼: CommonCode.N40001 缺少必填參數[%s]
 */
public class RequiredParameterMissingException extends BaseException {

	/**
	 * 缺少必填參數[%s]
	 */
	public RequiredParameterMissingException() {
		super(CommonCode.N40001);
	}

	/**
	 * 缺少必填參數[%s]
	 * 
	 * @param message
	 */
	public RequiredParameterMissingException(String message) {
		super(CommonCode.N40001, message);
	}

}
