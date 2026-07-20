package it.almaviva.mic.etl.divi.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import it.almaviva.mic.etl.divi.exceptions.DiviETLException;


public class DiviETLUtils 
{
	private static final Logger logger = LoggerFactory.getLogger(DiviETLUtils.class);
	
	public static String readContentFromFile(String filename)
	{
		logger.info("Lettura contenuto dal file {}...", filename);
		
		/* stringa di appoggio */
		 String createSql;
		 
		 try 
		 {
			 /* lettura risorsa */
			 InputStream is = DiviETLUtils.class.getClassLoader().getResourceAsStream(filename);
			 if(is == null)
			 {
				 logger.info("Impossibile trovare la risorsa {}", filename);
				 throw new DiviETLException("Impossibile trovate la risorsa " + filename, HttpStatus.INTERNAL_SERVER_ERROR);
			 }
			 
			 /* lettura file */
			 return new String(is.readAllBytes(), StandardCharsets.UTF_8);
		 }
		 
		 catch (Throwable ex) 
		{
			 logger.info("Si e' verificato un errore", ex);
			 throw new DiviETLException(ex instanceof DiviETLException ? ex.getMessage() : "Si e' verificato un errore interno", 
					                     HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public static String misurazioneTempoEsecuzione(long start, long end)
	{
		/* calcolo della durata in nanosecondi */
		long durationNs = end - start;
		
		/* conversione in millesecondi */
		long durationMs = durationNs / 1_000_000;

		/* calcolo di minuti, secondi e millisecondi rimanenti */
		long minutes = durationMs / 60000;
		long seconds = (durationMs % 60000) / 1000;
		long millis  = durationMs % 1000;

		/* formattazione */
		String formatted = String.format("%02d:%02d:%03d", minutes, seconds, millis);

		return formatted;

	}
	
	public static String formatDateTime(LocalDateTime ldt)
	{
		/* definizione del pattern */
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		
		/* conversione */
		return ldt.format(formatter);
	}
	
	public static LocalDate convertDateFromString(String date)
	{
		/* definizione del pattern */
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		
		return LocalDate.parse(date, formatter);
	}
}
