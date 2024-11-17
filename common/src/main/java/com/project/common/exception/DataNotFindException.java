package com.project.common.exception;

import com.project.common.enums.CommonCode;

/**
 * 用於資料異常時使用，預期應有資料，但是 查無資料 或是 查出資料值異常 <br/>
 * 錯誤代碼: CommonCode.N40003 無此資料[%s]
 */
public class DataNotFindException extends BaseException {

	/**
	 * 無此資料[%s]
	 */
	public DataNotFindException() {
		super(CommonCode.N40003);
	}

	/**
	 * 無此資料[%s]
	 * 
	 * @param message
	 */
	public DataNotFindException(String message) {
		super(CommonCode.N40003, message);
	}

}
