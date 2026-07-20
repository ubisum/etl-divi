package it.almaviva.mic.etl.divi.dto.json;

import lombok.Data;

@Data
public class Link 
{
	private String title;
	private String type;
	private String rel;
	private String href;
}
