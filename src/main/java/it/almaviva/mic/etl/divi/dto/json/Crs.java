package it.almaviva.mic.etl.divi.dto.json;

import lombok.Data;

@Data
public class Crs 
{
    private String type;
    private CrsProperty properties;
}