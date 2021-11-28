package com.mynt.calculator.bean;

import java.util.Date;

import lombok.Data;

/**
 * 
 * @date 2021-11-28
 * @author James Zhou
 * @email zmxxx1314@163.com
 *
 */
@Data
public class VoucherResponse {

	/**
	 * Voucher code.
	 */
	private String code;

	/**
	 * Discount for the voucher.
	 */
	private Double discount;

	/**
	 * Expire date for the voucher.
	 */
	private Date expiry;

	/**
	 * Error message.
	 */
	private String error;
}
