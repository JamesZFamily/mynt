package com.mynt.calculator.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mynt.calculator.exception.GeneralException;
import com.mynt.calculator.service.CalculatorService;

/**
 * Unit test for the controller
 * 
 * @date 2021-11-28
 * @author James Zhou
 * @email zmxxx1314@163.com
 *
 */
@ExtendWith(SpringExtension.class)
public class CalculatorControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private CalculatorController controller;

	@Mock
	private CalculatorService calculatorService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	/**
	 * Unit test for the controller.
	 * 
	 * @param hasInvalidParam
	 * @throws Exception
	 */
	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void testFreightCalculator(boolean hasInvalidParam) throws Exception {

		if (hasInvalidParam) {
			doThrow(new GeneralException("Exception happened.")).when(calculatorService).freightCalculator(any(), any(),
					any(), any(), any());
			mockMvc.perform(get("/calculator/freight?weight=10")).andExpect(status().isBadRequest())
					.andDo(MockMvcResultHandlers.print());
		} else {
			doReturn(10.0).when(calculatorService).freightCalculator(any(), any(), any(), any(), any());
			mockMvc.perform(get("/calculator/freight?weight=10")).andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print());
		}

	}
}
