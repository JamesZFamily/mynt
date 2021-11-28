package com.mynt.calculator.service.impl;

import java.text.MessageFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.mynt.calculator.bean.VoucherResponse;
import com.mynt.calculator.common.BaseService;
import com.mynt.calculator.exception.ExceptionEnum;
import com.mynt.calculator.exception.GeneralException;
import com.mynt.calculator.service.CalculatorService;

import lombok.extern.slf4j.Slf4j;

/**
 * Calculator service for calculate the freight
 * 
 * @date 2021-11-28
 * @author James Zhou
 * @email zmxxx1314@163.com
 *
 */
@Slf4j
@Service
public class CalculatorServiceImpl extends BaseService implements CalculatorService {

	@Value("${voucher.service.url:https://mynt-exam.mocklab.io/voucher/{0}?key=apikey}")
	private String voucherServiceURL;

	@Value("${voucher.service.check.expiry:false}")
	private boolean checkExpiryDate;

	@Value("${calculator.reject.weight:50}")
	private Double rejectWeight;

	@Value("${calculator.heavy.weight:10}")
	private Double heavyWeight;
	@Value("${calculator.heavy.factor:20}")
	private Double heavyFactor;

	@Value("${calculator.small.volume:1500}")
	private Double smallVolume;
	@Value("${calculator.small.factor:0.03}")
	private Double smallFactor;

	@Value("${calculator.medium.volume:2500}")
	private Double mediumVolume;
	@Value("${calculator.medium.factor:0.04}")
	private Double mediumFactor;

	@Value("${calculator.large.factor:0.05}")
	private Double largeFactor;

	@Resource
	private RestTemplate restTemplate;

	/**
	 * Freight calculate method.
	 */
	@Override
	public Double freightCalculator(Double weight, Double height, Double width, Double length, String voucherCode) {

		log.debug("Weight {},Height {}, Width {}, Length {}, Voucher code {}.", weight, height, width, length,
				voucherCode);

		if (weight > rejectWeight) {
			throw new GeneralException(ExceptionEnum.OVER_WEIGHT, rejectWeight);
		} else if (weight > heavyWeight) {
			return calculator(heavyFactor, weight, applyVoucher(voucherCode));
		} else {
			validation(height, width, length);

			Double volume = height * width * length;
			if (volume < smallVolume) {
				return calculator(smallFactor, volume, applyVoucher(voucherCode));
			} else if (volume < mediumVolume) {
				return calculator(mediumFactor, volume, applyVoucher(voucherCode));
			} else {
				return calculator(largeFactor, volume, applyVoucher(voucherCode));
			}
		}
	}

	/**
	 * Parameter validation method, will throw the runtime exception if invalid.
	 * 
	 * @param height
	 * @param width
	 * @param length
	 */
	private void validation(Double height, Double width, Double length) {

		checkParameter(height, ExceptionEnum.HEIGHT_REQUIRED);
		checkParameter(width, ExceptionEnum.WIDTH_REQUIRED);
		checkParameter(length, ExceptionEnum.LENGTH_REQUIRED);

		positiveValidation(height, ExceptionEnum.HEIGHT_NOT_POSITIVE);
		positiveValidation(width, ExceptionEnum.WIDTH_NOT_POSITIVE);
		positiveValidation(length, ExceptionEnum.LENGTH_NOT_POSITIVE);
	}

	/**
	 * Apply voucher, get the discount from the voucher service.
	 * 
	 * @param voucherCode
	 * @return Discount.
	 */
	private Double applyVoucher(String voucherCode) {
		Double discount = 0.0;
		if (!StringUtils.isEmpty(voucherCode)) {

			String voucherURL = MessageFormat.format(voucherServiceURL, voucherCode);
			log.debug(voucherURL);

			ResponseEntity<VoucherResponse> entity = restTemplate.getForEntity(voucherURL, VoucherResponse.class);
			log.debug(entity.toString());

			VoucherResponse voucherDetails = entity.getBody();
			if (voucherDetails != null) {
				if (!StringUtils.isEmpty(voucherDetails.getError())) {
					throw new GeneralException(ExceptionEnum.VOUCHER_CODE_INVALID, voucherCode,
							voucherDetails.getError());
				} else if (!checkExpiryDate || voucherDetails.getExpiry().after(new Date())) {
					discount = voucherDetails.getDiscount();
				}
			}
		}
		return discount;
	}

}
