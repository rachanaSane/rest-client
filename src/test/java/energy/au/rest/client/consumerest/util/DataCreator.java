package energy.au.rest.client.consumerest.util;

import java.util.ArrayList;
import java.util.List;

import energy.au.rest.client.consumerest.model.deserialize.Band;
import energy.au.rest.client.consumerest.model.deserialize.MusicFestival;

public class DataCreator {
	
	public static List<MusicFestival> getMusicFestivalList(){
		List<MusicFestival> musicFestivals = new ArrayList<>();
		MusicFestival lolpalooza= new MusicFestival();
		lolpalooza.setName("LOL-palooza-1");
		List<Band> paloozaBands = new ArrayList<>();
		paloozaBands.add(new Band("Jill Black-1","Fourth Woman Records1"));
		paloozaBands.add(new Band("Winter Primates",""));
		paloozaBands.add(new Band("Frank Jupiter","Pacific Records1"));
		paloozaBands.add(new Band("Werewolf Weekday","Pacific Records1"));
		lolpalooza.setBands(paloozaBands);
		
		musicFestivals.add(lolpalooza);
		
		
		MusicFestival smallNIn= new MusicFestival();
		smallNIn.setName("Small Night In-1");
		List<Band> smallNInBands = new ArrayList<>();
		smallNInBands.add(new Band("Jill Black-1","Fourth Woman Records1"));
		smallNInBands.add(new Band("Wild Antelope","Marner Sis. Recording"));
		smallNInBands.add(new Band("AFrank Jupiter2","Pacific Records1"));
		smallNInBands.add(new Band("Yanke East","MEDIOCRE Music"));
		smallNIn.setBands(smallNInBands);
		
		musicFestivals.add(smallNIn);
		
		return musicFestivals;
	}

}
