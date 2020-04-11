package energy.au.rest.client.consumerest.model.serialize;

import java.util.List;

public class Band {
   private String name;
   private List<String> musicFestivals;
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public List<String> getMusicFestivals() {
	return musicFestivals;
}
public void setMusicFestivals(List<String> musicFestivals) {
	this.musicFestivals = musicFestivals;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	return true;
}
   
   
}
