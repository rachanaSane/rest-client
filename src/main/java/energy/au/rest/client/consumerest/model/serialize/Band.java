package energy.au.rest.client.consumerest.model.serialize;

import java.util.List;
import java.util.Set;

public class Band {
   private String bandName;
   private Set<String> musicFestivals;

   
   
   
public String getBandName() {
	return bandName;
}
public void setBandName(String bandName) {
	this.bandName = bandName;
}
public Set<String> getMusicFestivals() {
	return musicFestivals;
}
public void setMusicFestivals(Set<String> musicFestivals) {
	this.musicFestivals = musicFestivals;
}

public Band() {
	
}


  
public Band(String bandName, Set<String> musicFestivals) {
	super();
	this.bandName = bandName;
	this.musicFestivals = musicFestivals;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((bandName == null) ? 0 : bandName.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Band other = (Band) obj;
	if (bandName == null) {
		if (other.bandName != null)
			return false;
	} else if (!bandName.equals(other.bandName))
		return false;
	return true;
}



   
}
