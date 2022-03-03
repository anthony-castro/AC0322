package com.acproject.AC0322;

import com.acproject.AC0322.Objects.CheckoutInfo;
import com.acproject.AC0322.Objects.RentalAgreement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class Ac0322Application {

	public static void main(String[] args) {
		SpringApplication.run(Ac0322Application.class, args);
	}

	@PostMapping(
			value = "/rentTool",
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<String> rentTool(@RequestBody CheckoutInfo checkoutInfo) {
		String errors = RentalProcessor.checkForInvalidCheckoutInfo(checkoutInfo);
		if (StringUtils.hasLength(errors)) {
			return ResponseEntity.badRequest().body(errors);
		}

		RentalAgreement rentalAgreement = RentalProcessor.processRentalOrder(checkoutInfo);
		return ResponseEntity.ok(rentalAgreement.getConsoleMessage());
	}

}
