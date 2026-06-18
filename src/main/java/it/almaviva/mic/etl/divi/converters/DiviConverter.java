package it.almaviva.mic.etl.divi.converters;

import org.modelmapper.ModelMapper;

import it.almaviva.mic.etl.divi.dto.BatchJobDTO;
import it.almaviva.mic.etl.divi.entities.BatchJob;

public class DiviConverter 
{
	public static BatchJobDTO convertBatchJobFromEntity(BatchJob job)
	{
		/* creazione del mapper */
		ModelMapper modelMapper = new ModelMapper();
		
		/* aggiunta dei convertitori necessari */
		modelMapper.addConverter(new LocalDateTimeToStringConverter());
		
		/* conversione */
		return modelMapper.map(job, BatchJobDTO.class);
	}
	

}
