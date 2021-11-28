package com.mynt.calculator.service;

/**
 * Calculator service interface.
 * 
 * @date 2021-11-28
 * @author James Zhou
 * @email zmxxx1314@163.com
 *
 */
public interface CalculatorService {

	/**
	 * Freight calculate method.
	 * 
	 * @param weight
	 * @param height
	 * @param width
	 * @param length
	 * @param voucherCode
	 * @return Freight value.
	 */
	public Double freightCalculator(Double weight, Double height, Double width, Double length, String voucherCode);
}
