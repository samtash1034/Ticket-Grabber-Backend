package com.project.common.exception;

import com.project.common.enums.CommonCode;


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
