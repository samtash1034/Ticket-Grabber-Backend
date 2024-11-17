package com.project.common.exception;

import com.project.common.enums.CommonCode;

/**
 * 1. Exception.message 主要存於 DB 與 file log 中，用作 debug 使用 <br/>
 * 2. 使用者收到訊息，利用 {@link CommonCode} 中的訊息回傳 <br/>
 * 3. 若 CommonCode 中有 %s 訊息格式置換，則透過 AOP 統一置換 <br/>
 */
public class BaseException extends RuntimeException {

	private final CommonCode code;
	private final Object[] args;

	public BaseException(CommonCode code, Object... args) {
		super();
		this.code = code;
		this.args = args;
	}

	public CommonCode getCode() {
		return code;
	}

	public Object[] getArgs() {
		return args;
	}

}
