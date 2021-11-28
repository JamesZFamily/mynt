package com.mynt.calculator.common;

import java.math.BigDecimal;

import com.mynt.calculator.exception.ExceptionEnum;
import com.mynt.calculator.exception.GeneralException;

/**
 * Base service.
 * 
 * @date 2021-11-28
 * @author James Zhou
 * @email zmxxx1314@163.com
 *
 */
public abstract class BaseService {

	/**
	 * For calculator the freight. Current formula is v1 * v2 - discount
	 * 
	 * @param v1       Weight or volume or factor.
	 * @param v2       Weight or volume or factor.
	 * @param discount Discount value.
	 * @return The freight.
	 */
	protected Double calculator(Double v1, Double v2, Double discount) {
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		BigDecimal disountVal = BigDecimal.valueOf(discount);

		return b1.multiply(b2).subtract(disountVal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * Check the param is null or not.
	 * 
	 * @param param
	 * @param exceptionEnum
	 */
	protected void checkParameter(Object param, ExceptionEnum exceptionEnum) {
		if (param == null) {
			throw new GeneralException(exceptionEnum);
		}
	}

	/**
	 * Check the parameter is positive or not.
	 * 
	 * @param param
	 * @param exceptionEnum
	 */
	protected void positiveValidation(Double param, ExceptionEnum exceptionEnum) {
		if (param <= 0) {
			throw new GeneralException(exceptionEnum);
		}
	}
}
