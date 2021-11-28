package com.mynt.calculator.exception;

import java.text.MessageFormat;

import lombok.Getter;

/**
 * General exception for this project
 * 
 * @date 2021-11-28
 * @author James Zhou
 * @email zmxxx1314@163.com
 *
 */
@Getter
public class GeneralException extends RuntimeException {

	private static final long serialVersionUID = -8160714213956451416L;

	/**
	 * Error code.
	 */
	private final String errorCode;

	/**
	 * Error message.
	 */
	private final String errorMessage;

	/**
	 * Exceptuin enum.
	 */
	private final ExceptionEnum exceptionEnum;

	public GeneralException(ExceptionEnum exceptionEnum) {
		super(exceptionEnum.getErrorMessage());
		this.exceptionEnum = exceptionEnum;
		this.errorCode = exceptionEnum.getErrorCode();
		this.errorMessage = exceptionEnum.getErrorMessage();
	}

	public GeneralException(ExceptionEnum exceptionEnum, Object... arguments) {
		super(MessageFormat.format(exceptionEnum.getErrorMessage(), arguments));
		this.exceptionEnum = exceptionEnum;
		this.errorCode = exceptionEnum.getErrorCode();
		this.errorMessage = MessageFormat.format(exceptionEnum.getErrorMessage(), arguments);
	}

	public GeneralException(String pattern, Object... arguments) {
		super(pattern);
		errorCode = ExceptionEnum.UNKNOWN_EXCEPTION.getErrorCode();
		errorMessage = MessageFormat.format(pattern, arguments);
		exceptionEnum = ExceptionEnum.UNKNOWN_EXCEPTION;
	}

}
