package it.almaviva.mic.etl.divi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EsitoDTO 
{
	private Integer codice;
	private String messaggio;
	private Object content;
	
}
