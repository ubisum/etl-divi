package it.almaviva.mic.etl.divi.dao;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.divi.repositories.AnagraficaBeneRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AnagraficaBeneDAOImpl implements AnagraficaBeneDAO 
{
	 private static final Logger logger = LoggerFactory.getLogger(AnagraficaBeneDAOImpl.class);
	 private final AnagraficaBeneRepository anagraficaRepository;
	 
	@Override
	public LocalDateTime findMostRecentUpdate() 
	{
		logger.info("Ricerca ultima data aggiornamento (se presente)");
		
		LocalDateTime ultimaData = anagraficaRepository.findMaxDataUltimoAggiornamento();
		return ultimaData;
	}

}
