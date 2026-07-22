package it.almaviva.mic.etl.divi.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.almaviva.mic.etl.divi.dao.GenericDAO;
import it.almaviva.mic.etl.divi.dto.BatchJobDTO;
import it.almaviva.mic.etl.divi.enums.DiviEsitoBatchJob;
import it.almaviva.mic.etl.divi.exceptions.DiviETLException;
import jakarta.transaction.Transactional;

@Service
public class BatchJobServiceImpl implements BatchJobService 
{
	@Autowired
	private GenericDAO genericDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(BatchJobServiceImpl.class);
	
	@Override
	@Transactional
	public BigDecimal insertBatchJob(String fonte, String tipoCarico) 
	{
		logger.info("Accesso alla funzione service per l'inserimento del batch job...");
		try
		{
			return genericDAO.insertBatchJob(fonte, tipoCarico);
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificata un'eccezione durante l'inserimento del batch job", ex);
			throw new DiviETLException(StringUtils.isNoneBlank(ex.getMessage()) ? ex.getMessage() : 
				                        "Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@Override
	public void updateBatchJob(BigDecimal idBatch, DiviEsitoBatchJob esito) 
	{
		logger.info("Accesso alla funzione service per l'aggiornamento del batch job...");
		try
		{
			genericDAO.updateBatchJob(idBatch, esito);
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificata un'eccezione durante l'aggiornamento del batch job", ex);
			throw new DiviETLException(StringUtils.isNoneBlank(ex.getMessage()) ? ex.getMessage() : 
				                        "Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@Override
	@Transactional
	public void inserisciDettagliBatchJob(Map<Integer, List<String>> errori, BigDecimal idJob, String filename) 
	{
		logger.info("Accesso alla funzione service per l'inserimento dei dettagli del batch job...");
		try
		{
			genericDAO.inserisciDettagliBatchJob(errori, idJob, filename);
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificata un'eccezione durante l'inserimento dei dettagli del batch job", ex);
			throw new DiviETLException(StringUtils.isNoneBlank(ex.getMessage()) ? ex.getMessage() : 
				                        "Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@Override
	public BatchJobDTO findUltimoBatchJobAttivo() 
	{
		return genericDAO.findUltimoBatchJobAttivo();
	}

}
