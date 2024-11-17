package com.project.common.exception;

import com.project.common.enums.CommonCode;

/**
 * 錯誤代碼: CommonCode.N40002 請求參數格式錯誤[%s]
 */
public class RequestDataFormatException extends BaseException {

	/**
	 * 請求參數格式錯誤[%s]
	 */
	public RequestDataFormatException() {
		super(CommonCode.N40002);
	}

	/**
	 * 請求參數格式錯誤[%s]
	 * 
	 * @param message
	 */
	public RequestDataFormatException(String message) {
		super(CommonCode.N40002, message);
	}

}
