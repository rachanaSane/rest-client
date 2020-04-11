package energy.au.rest.client.consumerest.model.deserialize;
import java.util.ArrayList;
import java.util.List;


public class MusicFestivalList {
	
	private List<MusicFestival> festivals;
	
	public MusicFestivalList() {
		festivals = new ArrayList<>();
    }

	public List<MusicFestival> getFestivals() {
		return festivals;
	}

	public void setFestivals(List<MusicFestival> festivals) {
		this.festivals = festivals;
	}

	@Override
	public String toString() {
		return "MusicFestivalList [festivals=" + festivals + "]";
	}
	
	
	
	
	

}
