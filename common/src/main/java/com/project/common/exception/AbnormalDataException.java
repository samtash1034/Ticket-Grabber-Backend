package com.project.common.exception;

import com.project.common.enums.CommonCode;
import com.project.common.enums.InvalidMsg;

/**
 * 錯誤代碼: CommonCode.N40005 資料異常[%s]
 */
public class AbnormalDataException extends BaseException {

	/**
	 * 資料異常[%s]
	 * 
	 * @param message
	 */
	public AbnormalDataException(InvalidMsg message) {
		super(CommonCode.N40005, message.getMsg());
	}

}
