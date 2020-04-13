package energy.au.rest.client.consumerest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import energy.au.rest.client.consumerest.error.CustomException;
import energy.au.rest.client.consumerest.model.deserialize.MusicFestival;
import energy.au.rest.client.consumerest.model.serialize.Band;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This is main web client for Energy Australia GET API
 * @author Rachana Sane
 *
 */
@Service
public class RestAPIProcessor {
	private static final String ENERY_AUSTRALIA_BASE_URL = "http://eacodingtest.digital.energyaustralia.com.au";
	private static final String CONTENT_TYPE_JSON = "application/json";
	private static final String USER_AGENT = "Spring 5 WebClient";
	private static final String SERVICE_NOT_READY = "***** External Energy Australia Service is not ready yet. Please try again after few minutes.*****";

	private static final Logger log = LoggerFactory.getLogger(RestAPIProcessor.class);

	private final WebClient webClient;

	@Autowired
	private RecordLabelCreator recordLabelCreator;

	@Autowired
	private JSONMapper mapper;
	
	@Autowired
	private RestTemplate restTemplate;

	
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Autowired
	public RestAPIProcessor() {
		this.webClient = WebClient.builder().baseUrl(ENERY_AUSTRALIA_BASE_URL)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON)
				.defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT).filter(logRequest()).build();

	}

	public void getMusicFestivals() {

		Flux<MusicFestival> festivalAPI = webClient.get().uri("/api/v1/festivals").exchange()
				.flatMapMany(clientResponse -> clientResponse.bodyToFlux(MusicFestival.class));

		List<MusicFestival> musicFestivals = new ArrayList<MusicFestival>();

	//	festivalAPI.retry(t -> t instanceof Throwable);
		
	//	festivalAPI.retryWhen(whenFactory)
		
		festivalAPI.subscribe(new Subscriber<MusicFestival>() {
			@Override
			public void onSubscribe(Subscription s) {				
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(MusicFestival festival) {				
				musicFestivals.add(festival);
			}

			@Override
			public void onError(Throwable t) {
				if (t instanceof DecodingException) {
					log.error("\n" + SERVICE_NOT_READY);
				} else if (t instanceof UnsupportedMediaTypeException) {
					log.error("\n" + SERVICE_NOT_READY);
				} else if (t instanceof RestClientException) {
					log.error("\n" + SERVICE_NOT_READY);
				} else {
					log.error("some error occurred **: " + t.getMessage());
				}
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				log.info("will retry execution now..");
				getMusicFestivals();
			}

			@Override
			public void onComplete() {
				log.info("Data received successfully from Music Festival service.\n Response Received from Energy AU API :");
				try {
					mapper.java2JSON(musicFestivals);
					recordLabelCreator.createRecordLabelStructure(musicFestivals);
				} catch (JsonProcessingException e) {
					log.error("Error occurred while trying to parse Java object to JSON.", e);
				}
			}
		});
		
		//Mono<List<MusicFestival>> festivalsMono= festivalAPI.collectList();
		
		//log.info("############## After subscription #####################");

	}
	
	
	
	public void getRestRecordLabels() {
		log.info("inside restAPIProcessor......");
		final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        
      //Create a new HttpEntity
        final HttpEntity<String> entity = new HttpEntity<String>(headers);
        
      ResponseEntity<MusicFestival[]> response =restTemplate.exchange("http://eacodingtest.digital.energyaustralia.com.au/api/v1/festivals", 
    		  HttpMethod.GET, entity,MusicFestival[].class);        
        
        MusicFestival[] festivals= response.getBody();
        
        log.info("yeyyyy...got response......");
        
        try {
			mapper.java2JSON(festivals);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      /*  log.info("festivals size : " + festivals.length);
        
	      for(int i=0;i< festivals.length;i++) {
	        	MusicFestival festival = festivals[i];
	        	log.info("**************************************************");
	        	log.info("MusicFestival -"+i+":"+festival);
	        }	*/
	}
	
	public Mono<Map<String, List<Band>>> getRecordLabel(){
		/*Mono<List<MusicFestival>> festivalAPI = webClient.get().uri("/api/v1/festivals").exchange(
				).onErrorMap(mapper -> new CustomException("Please Retry again service is down.:"+mapper.getMessage())).log()
				.flatMapMany(clientResponse -> clientResponse.bodyToFlux(MusicFestival.class)).collectList();*/
		
		Mono<List<MusicFestival>> festivalAPI = webClient.get().uri("/api/v1/festivals").exchange(
				).doOnError(Exception.class , e -> log.error("Some error occurred 834832482346234872637@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"))
				.flatMapMany(clientResponse -> clientResponse.bodyToFlux(MusicFestival.class)).collectList();
		
		Mono<Map<String, List<Band>>> output=  festivalAPI.map(input -> recordLabelCreator.createRecordLabelStructure(input));
		
	//	output.subscribe();
		output.doOnSuccess(t -> log.info("completed successfully"));
		
		//festivalAPI.subscribe();
		return output;
	}

	private ExchangeFilterFunction logRequest() {
		return (clientRequest, next) -> {
			log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
			return next.exchange(clientRequest);
		};
	}

}
