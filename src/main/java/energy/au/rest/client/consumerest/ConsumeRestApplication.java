package energy.au.rest.client.consumerest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import energy.au.rest.client.consumerest.model.deserialize.Band;
import energy.au.rest.client.consumerest.model.deserialize.MusicFestival;
import energy.au.rest.client.consumerest.model.deserialize.MusicFestivalList;
import energy.au.rest.client.consumerest.model.serialize.RecordLabel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ConsumeRestApplication {

	private static final Logger log = LoggerFactory.getLogger(ConsumeRestApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(ConsumeRestApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		/*return args -> {
			restTemplate.getForObject(
					"http://eacodingtest.digital.energyaustralia.com.au/api/v1/festivals", MusicFestival.class);
			
		};*/
		
	/*	//// Working solution ...................................................................
		return args -> {
			final HttpHeaders headers = new HttpHeaders();
	        headers.set("Content-Type", "application/json");
	        
	      //Create a new HttpEntity
	        final HttpEntity<String> entity = new HttpEntity<String>(headers);
	        
	      ResponseEntity<MusicFestival[]> response =restTemplate.exchange("http://eacodingtest.digital.energyaustralia.com.au/api/v1/festivals", HttpMethod.GET, entity,MusicFestival[].class);        
	        
	        MusicFestival[] festivals= response.getBody();
	        
	        log.info("yeyyyy...got response......");
	        log.info("festivals size : " + festivals.length);
	        
		      for(int i=0;i< festivals.length;i++) {
		        	MusicFestival festival = festivals[i];
		        	log.info("**************************************************");
		        	log.info("MusicFestival -"+i+":"+festival);
		        }	
	        
         
	     
			};*/
		
		
		return args -> {
		WebClient webClient = WebClient.builder()
                .baseUrl("http://eacodingtest.digital.energyaustralia.com.au")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")                
                .filter(logRequest())
                .build();
		
		Flux<MusicFestival> fest=webClient.get()
         .uri("/api/v1/festivals")
         .exchange()
         .flatMapMany(clientResponse -> clientResponse.bodyToFlux(MusicFestival.class));
		
		
	
		
		
				
	/*	fest.subscribe(festival -> {
			try {
				receiveMusicFestivalList(festival);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		fest.doFinally(s -> log.info("finished!!!!"));*/
		List<MusicFestival> musicFestivals = new ArrayList();
		
		fest.log().subscribe(new Subscriber<MusicFestival>() {
		    @Override
		    public void onSubscribe(Subscription s) {
		     // s.request(Long.MAX_VALUE);
		    	log.info("inside onsubscribe method");
		    	s.request(Long.MAX_VALUE);
		    	//s.request(arg0);
		    }
		 
		    @Override
		    public void onNext(MusicFestival festival) {
		    //  elements.add(integer);
		    	log.info("Music festival added");
		    	musicFestivals.add(festival);
		    	
		    }
		 
		    @Override
		    public void onError(Throwable t) {
		    	log.error("some error occurred **: "+t.getMessage());
		    }
		 
		    @Override
		    public void onComplete() {
		    	log.info("Everything is finished");
		    	try {
					java2JSON(musicFestivals);
					deSerializeList(musicFestivals);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
	//	log.info("done receiving fest");
		
		};
		
		
		
		
		//--------------------------------------------------------------------------------------------------------------------------------------------------------------

        
        
      /*  //Execute the method writing your HttpEntity to the request
        ResponseEntity<Map> response = restTemplate.exchange("https://httpbin.org/user-agent", HttpMethod.GET, entity, Map.class);        
        System.out.println(response.getBody());*/
		/*return args -> {
		
		ResponseEntity<MusicFestival[]> response =
				  restTemplate.getForEntity(
				  "http://eacodingtest.digital.energyaustralia.com.au/api/v1/festivals",
				  MusicFestival[].class);
		   MusicFestival[] festivals = response.getBody();
		   
		   log.info("Got response -----------------> ");
		   
		   log.info("festivals size:"+festivals.length);
		};*/
		
		/*return args -> {
			
			MusicFestivalList response = restTemplate.getForObject(
					  "http://eacodingtest.digital.energyaustralia.com.au/api/v1/festivals",
					  MusicFestivalList.class);
					
			
			
			   
			   log.info("Got response -----------------> ");
			   log.info("MusicFestival List ---> "+response);
			   
			   
			};*/
	}
	
	private void receiveMusicFestivalList(MusicFestival festival) throws JsonProcessingException {
		java2JSON(festival);
		/*log.info("***************************************************");
		log.info("festival name :"+festival.getName());
		List<Band> bands =festival.getBands();
		for(Band band:bands) {
			log.info("band name :"+band.getName() +"                                 RecordLebel: "+band.getRecordLabel());
		}*/
	}
	
	 private ExchangeFilterFunction logRequest() {
	        return (clientRequest, next) -> {
	            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
	            clientRequest.headers()
	                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
	            return next.exchange(clientRequest);
	        };
	    }
	 
	 private void java2JSON(Object obj) throws JsonProcessingException {
		 ObjectMapper mapper = new ObjectMapper();
		 String value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		 log.info("JSON value :\n"+value);
	 }
	 
	 private void deSerializeList(List<MusicFestival> musicFestivals) {
		 log.info("starting to deserialize *********************************************************");
		 Map<String, List<energy.au.rest.client.consumerest.model.serialize.Band>> sortedRecordLabels = new TreeMap<String, List<energy.au.rest.client.consumerest.model.serialize.Band>>(
	                new Comparator<String>() {

	                    @Override
	                    public int compare(String o1, String o2) {
	                        return o1.compareTo(o2);
	                    }

	                });
		 
		 for(MusicFestival festival : musicFestivals) {
			 String musicFestivalName = festival.getName();
			List<Band> bands = festival.getBands();
			
			for(Band band : bands) {
				String bandName =band.getName();
				String recordLabel = band.getRecordLabel();
				if(recordLabel ==null)
					continue;
				
				if(sortedRecordLabels.containsKey(recordLabel)) {
					if(bandName == null)
						continue;
					List<energy.au.rest.client.consumerest.model.serialize.Band> existingBands = sortedRecordLabels.get(recordLabel);
					energy.au.rest.client.consumerest.model.serialize.Band tempBand = new energy.au.rest.client.consumerest.model.serialize.Band();
					tempBand.setBandName(bandName);
					int index = existingBands.indexOf(tempBand);
					if(index >=0) {
						energy.au.rest.client.consumerest.model.serialize.Band inListBand  =existingBands.get(index);
						Set<String> existingMusicFestivals = inListBand.getMusicFestivals();
						if(musicFestivalName!=null && !existingMusicFestivals.contains(musicFestivalName)){
							existingMusicFestivals.add(musicFestivalName);
						};
					}else {
						Set<String> sortedMusicFestivalNames = new TreeSet<String>(new Comparator<String>() {

		                    @Override
		                    public int compare(String o1, String o2) {
		                        return o1.compareTo(o2);
		                    }

		                });
						if(musicFestivalName!=null)
							sortedMusicFestivalNames.add(musicFestivalName);
						existingBands.add(new energy.au.rest.client.consumerest.model.serialize.Band(bandName,sortedMusicFestivalNames));
					}
					
				}else {
					List<energy.au.rest.client.consumerest.model.serialize.Band> newBands = new ArrayList();
					Set<String> sortedMusicFestivalNames = new TreeSet<String>(new Comparator<String>() {

	                    @Override
	                    public int compare(String o1, String o2) {
	                        return o2.compareTo(o1);
	                    }

	                });
					if(musicFestivalName!=null)
						sortedMusicFestivalNames.add(musicFestivalName);
					if(bandName !=null)
						newBands.add(new energy.au.rest.client.consumerest.model.serialize.Band(bandName,sortedMusicFestivalNames));
					sortedRecordLabels.put(recordLabel, newBands);
				}
			}
		 }
		
		 log.info("finished serialization process -------------------------------->");
		 sortBandsFromRecordLabels(sortedRecordLabels);
		/* try {
			java2JSON(sortedRecordLabels);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	 }
	 
	private void sortBandsFromRecordLabels(Map<String, List<energy.au.rest.client.consumerest.model.serialize.Band>> recordLabels) {
		if(recordLabels.size() == 0) {
			return;
		}
		
		Map<String, List<energy.au.rest.client.consumerest.model.serialize.Band>> sortedRecordLabels = 
				recordLabels.entrySet().stream().collect(Collectors.toMap(keyMapper -> keyMapper.getKey() , 
						valueMapper -> valueMapper.getValue().stream().sorted(Comparator.comparing(energy.au.rest.client.consumerest.model.serialize.Band::getBandName)).collect(Collectors.toList())));
				
	   log.info("soring of bands successfully complete");
	   try {
		java2JSON(sortedRecordLabels);
	} catch (JsonProcessingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		/*recordLabels.forEach((key,bandList) ->{
			List<energy.au.rest.client.consumerest.model.serialize.Band> sortedBands = bandList.stream()
					.sorted(Comparator.comparing(energy.au.rest.client.consumerest.model.serialize.Band::getBandName))
					.collect(Collectors.toList());
		});*/
		
	}

}
