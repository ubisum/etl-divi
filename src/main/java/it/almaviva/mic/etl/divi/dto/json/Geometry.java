package it.almaviva.mic.etl.divi.dto.json;

import java.util.List;

import lombok.Data;

@Data
public class Geometry 
{
    private String type;
    private List<Double> coordinates;
}