package com.mynt.calculator.common;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the base result.
 * 
 * @date 2021-11-28
 * @author James Zhou
 * @email zmxxx1314@163.com
 *
 */
@Data
@NoArgsConstructor
public class ResultDto<T> implements Serializable {

	private static final long serialVersionUID = -2597498104427034195L;

	/**
	 * result content
	 */
	private T data = null;

	/**
	 * this is the message content for the result which should be shown to user
	 */
	private String message = null;

	/**
	 * HTTP status code
	 */
	private Integer statusCode;

	public ResultDto(Integer statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public ResultDto(Integer statusCode, T data, String message) {
		this.statusCode = statusCode;
		this.data = data;
		this.message = message;
	}

	public ResultDto(Integer statusCode, T data) {
		this.statusCode = statusCode;
		this.data = data;
	}
}
