package com.mynt.calculator.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mynt.calculator.common.ResultDto;
import com.mynt.calculator.exception.GeneralException;
import com.mynt.calculator.service.CalculatorService;

import lombok.extern.slf4j.Slf4j;

/**
 * Freight calculator controller.
 * 
 * @date 2021-11-28
 * @author James Zhou
 * @email zmxxx1314@163.com
 *
 */
@Slf4j
@RestController
@RequestMapping("calculator")
public class CalculatorController {

	@Resource
	private CalculatorService calculatorService;

	/**
	 * Freight calculator API.
	 * 
	 * @param weight
	 * @param height
	 * @param width
	 * @param length
	 * @param voucherCode
	 * @return freight value.
	 */
	@GetMapping("/freight")
	public ResponseEntity<ResultDto<Double>> freightCalculator(@RequestParam Double weight,
			@RequestParam(required = false) Double height, @RequestParam(required = false) Double width,
			@RequestParam(required = false) Double length, @RequestParam(required = false) String voucherCode) {

		log.debug("Weight {},Height {}, Width {}, Length {}, Voucher code {}.", weight, height, width, length,
				voucherCode);

		ResultDto<Double> result = new ResultDto<>();
		result.setStatusCode(HttpStatus.OK.value());
		result.setData(calculatorService.freightCalculator(weight, height, width, length, voucherCode));
		return ResponseEntity.ok(result);
	}

	/**
	 * Exception handler for this controller.
	 * 
	 * @param ex Exception.
	 * @return Base result Dto.
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ Exception.class })
	public ResultDto<String> exceptionHandler(Exception ex) {

		if (ex instanceof GeneralException) {
			return new ResultDto<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		}
		return new ResultDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
	}
}
