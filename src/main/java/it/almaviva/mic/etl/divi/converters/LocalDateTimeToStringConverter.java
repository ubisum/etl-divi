package it.almaviva.mic.etl.divi.converters;

import java.time.LocalDateTime;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.almaviva.mic.etl.utils.MicdlEtlUtils;

public class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> 
{
	private static final Logger logger = LoggerFactory.getLogger(LocalDateTimeToStringConverter.class);

	/* funzione custom di conversione LocaldateTime -> String, da utilizzare 
	 * durante la trasformazione di un'entita' in DTO */
	@Override
	public String convert(MappingContext<LocalDateTime, String> context) 
	{
		/* nessuna conversione se il dato sorgente non e' presente */
		if(context.getSource() == null)
			return null;
		
		else
			return MicdlEtlUtils.formatDateTime(context.getSource());
		
	}
}
