package com.acproject.AC0322;

import com.acproject.AC0322.Objects.CheckoutInfo;
import com.acproject.AC0322.Objects.RentalAgreement;
import com.acproject.AC0322.Objects.Tool;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Ac0322ApplicationTests {

	@Autowired
	private Ac0322Application application;

	private MockMvc mockMvc;

	private ObjectMapper mapper;

	@BeforeEach
	public void init() {
		mockMvc = standaloneSetup(application).build();
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Test
	void contextLoads() {
		assertThat(application).isNotNull();
	}

	@Test
	public void invalidCasesReturnBadRequestWithExpectedBody() throws Exception {
		for (Map.Entry<CheckoutInfo, String> testCase : getInvalidTestCasesWithExpectedResponse().entrySet()) {

			CheckoutInfo checkoutInfo = testCase.getKey();
			String expectedResponse = testCase.getValue();

			final String jsonContent = mapper.writeValueAsString(checkoutInfo);

			RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rentTool")
					.content(jsonContent)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON);;

			mockMvc.perform(requestBuilder)
					.andExpect(status().isBadRequest())
					.andExpect(content().string(expectedResponse));
		}
	}

	@Test
	public void validCheckoutRequestsReturnExpectedResult() throws Exception {
		for (Map.Entry<CheckoutInfo, RentalAgreement> testCase : getValidCheckoutInfoWithExpectedRentalAgreement().entrySet()) {

			CheckoutInfo checkoutInfo = testCase.getKey();
			RentalAgreement expectedRental = testCase.getValue();

			final String jsonContent = mapper.writeValueAsString(checkoutInfo);

			RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rentTool")
					.content(jsonContent)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON);;

			mockMvc.perform(requestBuilder)
					.andExpect(status().isOk())
					.andExpect(content().string(expectedRental.getConsoleMessage()));
		}
	}

	private HashMap<CheckoutInfo, String> getInvalidTestCasesWithExpectedResponse() {
		HashMap<CheckoutInfo, String> testCases = new HashMap<>();

		CheckoutInfo testCase1 = new CheckoutInfo("JAKR", 5, 101, LocalDate.of(2015, Month.SEPTEMBER, 3));
		String response1 = RentalProcessor.DISCOUNT_PERCENT_ERROR_MESSAGE;
		testCases.put(testCase1, response1);

		CheckoutInfo testCase2 = new CheckoutInfo("INVALID", 5, 0, LocalDate.of(2015, Month.SEPTEMBER, 3));
		String response2 = RentalProcessor.getToolCodeErrorMessage("INVALID");
		testCases.put(testCase2, response2);

		CheckoutInfo testCase3 = new CheckoutInfo("JAKR", -12, 0, LocalDate.of(2015, Month.SEPTEMBER, 3));
		String response3 = RentalProcessor.RENTAL_DAY_ERROR_MESSAGE;
		testCases.put(testCase3, response3);

		return testCases;
	}

	private HashMap<CheckoutInfo, RentalAgreement> getValidCheckoutInfoWithExpectedRentalAgreement() {
		HashMap<CheckoutInfo, RentalAgreement> testCases = new HashMap<>();

		CheckoutInfo testCase1 = new CheckoutInfo("LADW", 3, 10, LocalDate.of(2020, Month.JULY, 2));
		RentalAgreement agreement1 = new RentalAgreement(testCase1, LocalDate.of(2020, Month.JULY, 5), 2, Tool.LADW);
		testCases.put(testCase1, agreement1);

		CheckoutInfo testCase2 = new CheckoutInfo("CHNS", 5, 25, LocalDate.of(2015, Month.JULY, 2));
		RentalAgreement agreement2 = new RentalAgreement(testCase2, LocalDate.of(2015, Month.JULY, 7), 3, Tool.CHNS);
		testCases.put(testCase2, agreement2);

		CheckoutInfo testCase3 = new CheckoutInfo("JAKD", 6, 0, LocalDate.of(2015, Month.SEPTEMBER, 3));
		RentalAgreement agreement3 = new RentalAgreement(testCase3, LocalDate.of(2015, Month.SEPTEMBER, 9), 3, Tool.JAKD);
		testCases.put(testCase3, agreement3);

		CheckoutInfo testCase4 = new CheckoutInfo("JAKR", 9, 0, LocalDate.of(2015, Month.JULY, 2));
		RentalAgreement agreement4 = new RentalAgreement(testCase4, LocalDate.of(2015, Month.JULY, 11), 6, Tool.JAKR);
		testCases.put(testCase4, agreement4);

		CheckoutInfo testCase5 = new CheckoutInfo("JAKR", 4, 50, LocalDate.of(2020, Month.JULY, 2));
		RentalAgreement agreement5 = new RentalAgreement(testCase5, LocalDate.of(2020, Month.JULY, 6), 1, Tool.JAKR);
		testCases.put(testCase5, agreement5);

		return testCases;
	}

}
