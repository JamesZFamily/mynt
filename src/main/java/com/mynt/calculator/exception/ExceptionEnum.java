package com.mynt.calculator.exception;

/**
 * Exception Code Enum
 * 
 * @date 2021-11-28
 * @author James Zhou
 * @email zmxxx1314@163.com
 *
 */
public enum ExceptionEnum {

	HEIGHT_REQUIRED("SE0001", "Required Double parameter 'height' is not present."),
	WIDTH_REQUIRED("SE0002", "Required Double parameter 'width' is not present."),
	LENGTH_REQUIRED("SE0003", "Required Double parameter 'length' is not present."),

	HEIGHT_NOT_POSITIVE("SE0004", "Parameter 'height' cannot be negative."),
	WIDTH_NOT_POSITIVE("SE0005", "Parameter 'width' cannot be negative."),
	LENGTH_NOT_POSITIVE("SE0006", "Parameter 'length' cannot be negative."),

	VOUCHER_CODE_INVALID("SE0007", "Failed to apply the voucher code, voucher code = {0}, reason = {1}."),
	OVER_WEIGHT("SE0008", "Weight cannot exceeds {0}kg."), UNKNOWN_EXCEPTION("UE0001", "Unknown error.");

	private String errorCode;

	private String errorMessage;

	private ExceptionEnum(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
