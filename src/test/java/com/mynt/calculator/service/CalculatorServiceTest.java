package com.mynt.calculator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.mynt.calculator.bean.VoucherResponse;
import com.mynt.calculator.exception.GeneralException;
import com.mynt.calculator.service.impl.CalculatorServiceImpl;

/**
 * Unit test for the calculator service
 * 
 * @date 2021-11-28
 * @author James Zhou
 * @email zmxxx1314@163.com
 *
 */
@ExtendWith(SpringExtension.class)
public class CalculatorServiceTest {

	@InjectMocks
	private CalculatorServiceImpl calculatorService;

	@Value("${voucher.service.url:https://mynt-exam.mocklab.io/voucher/{0}?key=apikey}")
	private String voucherServiceURL;

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

	@Mock
	private RestTemplate restTemplate;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

		ReflectionTestUtils.setField(calculatorService, "voucherServiceURL", voucherServiceURL);
		ReflectionTestUtils.setField(calculatorService, "rejectWeight", rejectWeight);
		ReflectionTestUtils.setField(calculatorService, "heavyWeight", heavyWeight);
		ReflectionTestUtils.setField(calculatorService, "heavyFactor", heavyFactor);
		ReflectionTestUtils.setField(calculatorService, "smallVolume", smallVolume);
		ReflectionTestUtils.setField(calculatorService, "smallFactor", smallFactor);
		ReflectionTestUtils.setField(calculatorService, "mediumVolume", mediumVolume);
		ReflectionTestUtils.setField(calculatorService, "mediumFactor", mediumFactor);
		ReflectionTestUtils.setField(calculatorService, "largeFactor", largeFactor);
	}

	@ParameterizedTest
	@CsvSource({ "51,,,,", // Over weight.
			"10,,,,", // Height is required.
			"10,5,,,", // Width is required.
			"10,5,5,,", // length is required.
			"10,-5,5,5,", // Height is negative.
			"10,5,-5,5,", // Width is negative.
			"10,5,5,-5,", // Length is negative.
			"10,5,5,5,skdlks", // Invalid voucher code.
	})
	void testFreightCalculatorWithException(Double weight, Double height, Double width, Double length,
			String voucherCode) {

		if (!StringUtils.isEmpty(voucherCode)) {
			VoucherResponse response = new VoucherResponse();
			response.setError("Invalid code.");
			ResponseEntity<VoucherResponse> entity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			doReturn(entity).when(restTemplate).getForEntity(any(String.class), any());
		}

		assertThrows(GeneralException.class,
				() -> calculatorService.freightCalculator(weight, height, width, length, voucherCode));
	}

	@ParameterizedTest
	@CsvSource({ "1000,50,,,,,,", // Heavy parcel. expected = weight * heavyFactor
			"30,5,10,10,10,,,", // Small parcel. expected = height * width * length * smallFactor
			"80,5,10,10,20,,,", // Medium parcel. expected = height * width * length * mediumFactor
			"150,5,10,10,30,,,", // Large parcel. expected = height * width * length * largeFactor
			"137.75,5,10,10,30,MYNT,12.25,false", // Large parcel with voucher code ignore expire date
			"150,5,10,10,30,MYNT,12.25,true", // Large parcel with voucher code check expire date
	})
	void testFreghtCalculator(Double expected, Double weight, Double height, Double width, Double length,
			String voucherCode, Double voucherDiscount, Boolean checkExpiry) {

		if (!StringUtils.isEmpty(voucherCode)) {

			VoucherResponse response = new VoucherResponse();
			response.setCode(voucherCode);
			response.setDiscount(voucherDiscount);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -10);
			response.setExpiry(cal.getTime());

			ReflectionTestUtils.setField(calculatorService, "checkExpiryDate", checkExpiry);

			ResponseEntity<VoucherResponse> entity = new ResponseEntity<>(response, HttpStatus.OK);
			doReturn(entity).when(restTemplate).getForEntity(any(String.class), any());
		}
		assertEquals(expected, calculatorService.freightCalculator(weight, height, width, length, voucherCode));
	}
}
