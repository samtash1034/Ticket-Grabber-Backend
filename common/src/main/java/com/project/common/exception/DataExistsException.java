package com.project.common.exception;


import com.project.common.enums.CommonCode;

/**
 * 用於資料異常時使用，預期不應有資料，但是卻能查到 <br/>
 * 錯誤代碼: CommonCode.N40004 資料已存在[%s]
 */
public class DataExistsException extends BaseException {

	/**
	 * 資料已存在[%s] 
	 */
	public DataExistsException() {
		super(CommonCode.N40004);
	}

	/**
	 * 資料已存在[%s] 
	 * 
	 * @param message
	 */
	public DataExistsException(String message) {
		super(CommonCode.N40004, message);
	}

}
