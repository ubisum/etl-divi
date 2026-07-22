package it.almaviva.mic.etl.divi.services;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.divi.dao.AnagraficaBeneDAO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AnagraficaBeneServiceImpl implements AnagraficaBeneService
{
	 private final AnagraficaBeneDAO anagraficaDAO;
	 private static final Logger logger = LoggerFactory.getLogger(AnagraficaBeneServiceImpl.class);
	
	@Override
	public LocalDateTime findMostRecentUpdate() 
	{
		logger.info("Servizio di ricerva della data dell'ultimo aggiornamento...");
		
		return anagraficaDAO.findMostRecentUpdate();
	}

}
