package energy.au.rest.client.consumerest.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;

import energy.au.rest.client.consumerest.model.deserialize.MusicFestival;
import energy.au.rest.client.consumerest.model.serialize.Band;
import energy.au.rest.client.consumerest.util.DataCreator;
import jdk.internal.jline.internal.Log;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RecordLabelCreatorTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(RecordLabelCreatorTest.class);
	
	
	@Autowired
	private RecordLabelCreator creator;
	
	@Autowired
	private JSONMapper mapper;
	
	private List<MusicFestival> musicFestivals;
	
	@Before
    public void setUp() {
		musicFestivals = DataCreator.getMusicFestivalList();
    }

	@Test
	public void testCreateRecordLabelStructure() {
		 Map<String, List<Band>> recordLabels = creator.createRecordLabelStructure(musicFestivals);
		
		 Assert.isTrue(recordLabels.containsKey("Pacific Records1"), () -> "Pacific Records1 must be present at the top as key.");
		 List<Band> pacificRecBands= recordLabels.get("Pacific Records1");
		 List<Band> womenRecBands = recordLabels.get("Fourth Woman Records1");
		 Band jillBlackTest = new Band();
		 jillBlackTest.setBandName("Jill Black-1");
		 
		 Assert.isTrue(womenRecBands.contains(jillBlackTest), () -> "Fourth Woman Records1 record label must contain Jill Black-1 band.");
		 Band jillBlackReal= womenRecBands.get(womenRecBands.indexOf(jillBlackTest));
		
		
		Assert.isTrue(pacificRecBands.size()==3, () -> "Number of bands for Pacific Records label must be 3");
		Assert.isTrue(jillBlackReal.getMusicFestivals().size()==2, () -> "Number of music festivals for Jill black must be 2");
	}
}
