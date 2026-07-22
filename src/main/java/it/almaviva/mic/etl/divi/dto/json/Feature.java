package it.almaviva.mic.etl.divi.dto.json;

import java.util.List;

import lombok.Data;

@Data
public class Feature 
{
    private String type;
    private String id;
    private Geometry geometry;
    private String geometry_name;
    private Property properties;
    private List<Double> bbox;
}
