package energy.au.rest.client.consumerest.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;


import energy.au.rest.client.consumerest.model.deserialize.MusicFestival;
import energy.au.rest.client.consumerest.model.serialize.Band;

/**
 * This class will create structure for Record label from MusicFestival structure
 * At the top level, it will show the band record label, below that it will list out all bands under their management, and below that it will display which festivals 
 * they've attended, if any. All entries will be sorted alphabetically.
 * @author Rachana Sane
 *
 */
@Service
public class RecordLabelCreator {

	private static final Logger LOG = LoggerFactory.getLogger(RecordLabelCreator.class);
	

	@Autowired
	private JSONMapper mapper;

	public Map<String, List<Band>> createRecordLabelStructure(final List<MusicFestival> musicFestivals) {

		/*Map<String, List<Band>> sortedRecordLabels = new TreeMap<String,List<Band>>(
				new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}

				});*/
		Map<String, List<Band>> sortedRecordLabels = new HashMap<>();

		for (MusicFestival festival : musicFestivals) {
			final String musicFestivalName = festival.getName();
			final List<energy.au.rest.client.consumerest.model.deserialize.Band> bands = festival.getBands();
			if(bands==null || bands.isEmpty()) {
				continue;
			}

			for (energy.au.rest.client.consumerest.model.deserialize.Band band : bands) {
				final String bandName = band.getName();
				final String recordLabel = band.getRecordLabel();
				if (recordLabel == null) {
					continue;
				}

				if (sortedRecordLabels.containsKey(recordLabel)) {
					if (bandName == null) {
						continue;
					}
					List<Band> existingBands = sortedRecordLabels.get(recordLabel);
					final Band tempBand = new Band();
					tempBand.setBandName(bandName);
					final int index = existingBands.indexOf(tempBand);
					if (index >= 0) {					
						addMusicFestivalToBand(musicFestivalName, existingBands.get(index));						
					} else {
						addNewBand(musicFestivalName, bandName, existingBands);
					}

				} else {				
					sortedRecordLabels.put(recordLabel, createNewBandList(musicFestivalName, bandName));
				}
			}
		}

		LOG.info("finished serialization process -------------------------------->");
		try {
			mapper.java2JSON(sortedRecordLabels);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return sortBandsFromRecordLabels(sortedRecordLabels);

	}

	private List<Band> createNewBandList(String musicFestivalName, String bandName) {
		List<Band> newBands = new ArrayList();
		Set<String> sortedFestivalNames = new TreeSet<>(new Comparator<String>() {

			@Override
			public int compare(String val1, String val2) {
				return val1.compareTo(val2);
			}

		});
		if (musicFestivalName != null) {
			sortedFestivalNames.add(musicFestivalName);
		}
		if (bandName != null) {
			newBands.add(new Band(bandName,	sortedFestivalNames));
		}
		return newBands;
	}

	private void addMusicFestivalToBand(String musicFestivalName,
			Band inListBand) {
		Set<String> existingMusicFestivals = inListBand.getMusicFestivals();
		if (musicFestivalName != null && !existingMusicFestivals.contains(musicFestivalName)) {
			existingMusicFestivals.add(musicFestivalName);
		}
	}

	private void addNewBand(String musicFestivalName, String bandName,
			List<Band> existingBands) {
		Set<String> sortedMusicFestivalNames = new TreeSet<String>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}

		});
		if (musicFestivalName != null) {
			sortedMusicFestivalNames.add(musicFestivalName);
		}
		existingBands.add(new Band(bandName,
				sortedMusicFestivalNames));
	}

	private Map<String, List<Band>> sortBandsFromRecordLabels(
			Map<String, List<Band>> recordLabels) {
		if (recordLabels.size() == 0) {
			return Collections.EMPTY_MAP;
		}

		Map<String, List<Band>> sortedbands = recordLabels
				.entrySet().stream().peek(a-> LOG.info("Stream at object :"+a.getKey()))
				.collect(Collectors.toMap(keyMapper -> keyMapper.getKey(),
						valueMapper -> valueMapper.getValue().stream()
								.sorted(Comparator
										.comparing(Band::getBandName))
								.collect(Collectors.toList())));
		
		Map<String, List<Band>> sortedRecordLabels = sortedbands.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		LOG.info("soring of bands successfully complete");
		try {
			mapper.java2JSON(sortedRecordLabels);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return sortedRecordLabels;

	}

}
