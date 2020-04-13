package energy.au.rest.client.consumerest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import energy.au.rest.client.consumerest.service.RestAPIProcessor;

@RestController
public class FestivalController {

	@Autowired
	private RestAPIProcessor restProcessor;

	@GetMapping("/api")
	public String getRecordLabels() {
		restProcessor.getRecordLabels();
		return "*****       Check console for the result  **************";
	}

}
