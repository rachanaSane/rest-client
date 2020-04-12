package energy.au.rest.client.consumerest.model.deserialize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Band {
	private String name;
	private String recordLabel;
	
	public Band() {
		
	}
	
	public Band(String name, String recordLabel) {
		super();
		this.name = name;
		this.recordLabel = recordLabel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRecordLabel() {
		return recordLabel;
	}
	public void setRecordLabel(String recordLabel) {
		this.recordLabel = recordLabel;
	}
	@Override
	public String toString() {
		return "Band [name=" + name + ", recordLabel=" + recordLabel + "]";
	}
	
	
	

}
