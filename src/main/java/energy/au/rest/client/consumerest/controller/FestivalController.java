package energy.au.rest.client.consumerest.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import energy.au.rest.client.consumerest.model.serialize.Band;
import energy.au.rest.client.consumerest.service.RestAPIProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FestivalController {
	
	private static final Logger log = LoggerFactory.getLogger(RestAPIProcessor.class);

	
	@Autowired
	private RestAPIProcessor restProcessor;
	
	 @GetMapping("/api")
	    public String getRecordLabels() {
	        restProcessor.getMusicFestivals();
	        return "Check console for the result";
	    }
	 
	 @GetMapping("/rest")
	    public Mono<Map<String, List<Band>>> getRecordLabelsRest() {
		 log.info("inside getRecordLabelsRest ");
	       return restProcessor.getRecordLabel();
	       // return "Check console for the result";
	    }
	 
	 
	 @GetMapping("/error")
	    public String getError() {
		 log.info("inside getError ");
	       return "Please retry after few minues";
	       // return "Check console for the result";
	    }


}
