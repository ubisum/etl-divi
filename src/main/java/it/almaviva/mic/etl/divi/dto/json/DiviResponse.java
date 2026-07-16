package it.almaviva.mic.etl.divi.dto.json;

import java.util.List;

import lombok.Data;

@Data
public class DiviResponse 
{
	private String type;
    private List<Feature> features;
    private Integer totalFeatures;
    private Integer numberMatched;
    private Integer numberReturned;
    private String timeStamp;
    private Crs crs;
    private List<Double> bbox;
}
