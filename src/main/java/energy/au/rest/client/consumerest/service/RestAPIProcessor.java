package energy.au.rest.client.consumerest.service;

import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import energy.au.rest.client.consumerest.ConsumeRestApplication;

import energy.au.rest.client.consumerest.model.deserialize.MusicFestival;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
	public RestAPIProcessor() {
		this.webClient = WebClient.builder().baseUrl(ENERY_AUSTRALIA_BASE_URL)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON)
				.defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT).filter(logRequest()).build();

	}

	public void getMusicFestivals() {

		Flux<MusicFestival> fest = webClient.get().uri("/api/v1/festivals").exchange()
				.flatMapMany(clientResponse -> clientResponse.bodyToFlux(MusicFestival.class));

		List<MusicFestival> musicFestivals = new ArrayList();

		//fest.log().subscribe(new Subscriber<MusicFestival>() {
		fest.subscribe(new Subscriber<MusicFestival>() {
			@Override
			public void onSubscribe(Subscription s) {

				log.info("inside onsubscribe method");
				s.request(Long.MAX_VALUE);

			}

			@Override
			public void onNext(MusicFestival festival) {

				log.info("Music festival added");
				musicFestivals.add(festival);

			}

			@Override
			public void onError(Throwable t) {
				if(t instanceof DecodingException) {
					log.error("\n"+SERVICE_NOT_READY);					
				}else if (t instanceof UnsupportedMediaTypeException) {
					log.error("\n"+SERVICE_NOT_READY);				
		        }else  if (t instanceof RestClientException) {
		        	log.error("\n"+SERVICE_NOT_READY);		      
		        }else {
		        	log.error("some error occurred **: " + t.getMessage());
		        }
			}

			@Override
			public void onComplete() {
				log.info("Data received successfully from Music Festival service");
				try {
					mapper.java2JSON(musicFestivals);
					recordLabelCreator.createRecordLabelStructure(musicFestivals);					
				} catch (JsonProcessingException e) {
					log.error("Error occurred while trying to parse Java object to JSON.",e);					
				}
			}
		});

	}


	private ExchangeFilterFunction logRequest() {
		return (clientRequest, next) -> {
			log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
			return next.exchange(clientRequest);
		};
	}

}
