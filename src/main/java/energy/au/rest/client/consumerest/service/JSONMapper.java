package energy.au.rest.client.consumerest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class provides utility to convert and print any java object to JSON structure
 * @author Rachana Sane
 *
 */

@Service
public class JSONMapper {
	private static final Logger log = LoggerFactory.getLogger(JSONMapper.class);
	
	
	 public void java2JSON(Object obj,String message) throws JsonProcessingException {
		 ObjectMapper mapper = new ObjectMapper();
		 String value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		 log.info("\n"+message+"\n"+value);
	 }

}
