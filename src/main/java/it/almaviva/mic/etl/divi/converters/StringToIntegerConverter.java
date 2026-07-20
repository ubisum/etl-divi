package it.almaviva.mic.etl.divi.converters;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import it.almaviva.mic.etl.divi.exceptions.DiviETLException;

public class StringToIntegerConverter implements Converter<String, Integer> 
{
	private static final Logger logger = LoggerFactory.getLogger(StringToIntegerConverter.class);
	
	/* funzione custom di conversione String -> Integer, da utilizzare 
	 * durante la trasformazione di un DTO in entita' */
    @Override
    public Integer convert(MappingContext<String, Integer> context) {
        String source = context.getSource();

        /* nessuna conversione se il dato sorgente non e' presente */
        if (StringUtils.isBlank(source)) 
            return null;
        

        try 
        {
            return Integer.parseInt(source);
        } 
        
        catch (NumberFormatException e) 
        {
           logger.error("Si e' verificato un'errore durante la conversione da stringa ad intero", e);
           throw new DiviETLException("Si e' verificato un'errore durante la conversione da stringa ad intero", 
        		                       HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
