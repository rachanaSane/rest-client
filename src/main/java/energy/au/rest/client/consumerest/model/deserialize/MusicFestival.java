package energy.au.rest.client.consumerest.model.deserialize;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicFestival {
	
	private String name;
	private List<Band> bands;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Band> getBands() {
		return bands;
	}
	public void setBands(List<Band> bands) {
		this.bands = bands;
	}
	@Override
	public String toString() {
		return "MusicFestival [name=" + name + ", bands=" + bands + "]";
	}
	
	

}
