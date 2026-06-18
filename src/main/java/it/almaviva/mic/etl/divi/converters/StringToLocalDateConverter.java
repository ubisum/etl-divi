package it.almaviva.mic.etl.divi.converters;

import java.time.LocalDate;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import it.almaviva.mic.etl.utils.MicdlEtlUtils;

public class StringToLocalDateConverter implements Converter<String, LocalDate>  
{

	@Override
	public LocalDate convert(MappingContext<String, LocalDate> context) 
	{
		/* nessuna conversione se il dato sorgente non e' presente */
		if(context.getSource() == null)
			return null;
		
		else
			return MicdlEtlUtils.convertDateFromString(context.getSource());
	}

}
