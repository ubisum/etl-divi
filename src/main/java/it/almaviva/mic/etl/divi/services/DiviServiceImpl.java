package it.almaviva.mic.etl.divi.services;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.divi.dao.GenericDAO;
import it.almaviva.mic.etl.divi.dto.json.DiviResponse;
import it.almaviva.mic.etl.divi.dto.json.Feature;
import it.almaviva.mic.etl.divi.utils.DiviETLConsts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiviServiceImpl implements DiviService 
{
	private final GenericDAO genericDAO;
	private static final Logger logger = LoggerFactory.getLogger(DiviServiceImpl.class);
	
	@Override
	@Transactional
	public void insertDiviData(List<DiviResponse> listaRisposte, BigDecimal idBatch) 
	{
		logger.info("Preparazione dei dati all'inserimento...");
		
		if(CollectionUtils.isEmpty(listaRisposte))
		{
			logger.info("Nessun dato trovato");
			return;
		}
		
		List<Feature> listaFeature = listaRisposte.stream().
				                     flatMap(res -> Optional.ofNullable(res.getFeatures()).
				                     orElse(Collections.emptyList()).stream()).toList();
		
		logger.info("Invio dati al DAO...");
		genericDAO.insertDiviData(listaFeature, idBatch);
		
		logger.info("Esecuzione stored procedure...");
		genericDAO.eseguiStoredProcedure(DiviETLConsts.DIVI_STORED_PROCEDURE);
	}

}
