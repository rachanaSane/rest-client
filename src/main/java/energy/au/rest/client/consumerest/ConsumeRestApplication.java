package energy.au.rest.client.consumerest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import energy.au.rest.client.consumerest.service.RestAPIProcessor;
/**
 * 
 * @author Rachana Sane
 *
 */
@SpringBootApplication
public class ConsumeRestApplication {

	
	@Autowired
	private RestAPIProcessor restProcessor;
	
	public static void main(String[] args) {
		SpringApplication.run(ConsumeRestApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		
		return args ->{
			restProcessor.getMusicFestivals();
		};		
	
	}	
	
	
	 
}
