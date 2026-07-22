package it.almaviva.mic.etl.divi.cron;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.divi.dto.json.DiviResponse;
import it.almaviva.mic.etl.divi.services.AnagraficaBeneService;
import it.almaviva.mic.etl.divi.services.BatchJobService;
import it.almaviva.mic.etl.divi.services.DiviService;
import it.almaviva.mic.etl.divi.utils.DiviETLUtils;
import it.almaviva.mic.etl.divi.web.DiviClient;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiviScheduler {

    private final DiviClient diviClient;
    private final AnagraficaBeneService anagraficaService;
    private final BatchJobService batchService;
    private final DiviService diviService;
    private static final String SORT_DATA_ULTIMO_AGGIORNAMENTO_ASC = "data_ultimo_aggiornamento asc";
    private static final Logger logger = LoggerFactory.getLogger(DiviScheduler.class);
    
    @Value("${divi.request.mode}")
    private String requestMode;
    
    @Value("${divi.json.file}")
    private Boolean readFromJson;

    @Scheduled(cron = "${divi.cron}")
    public void execute() {
    	logger.info("Avvio procedura automatica di ricerca dati da DIVI...");
    	
    	if(StringUtils.isBlank(requestMode))
    	{
    		logger.info("Modalita' di richiesta dati non presente. Il processo verra' terminato.");
    	}
    	
    	else if(requestMode.trim().toLowerCase().equals("rest"))
    	{
    		logger.info("Avvio procedura di richiesta dati con modalita' REST...");
    		
    		/* lista delle risposte ricevute */
    		List<DiviResponse> datiRicevuti = new ArrayList<>(); 
    		
    		try
    		{
    			DiviResponse response = null;
    			if(this.readFromJson != null && this.readFromJson)
    			{
    				logger.info("Richiesta lettura JSON da file");
    				response = DiviETLUtils.readJson("example.json", DiviResponse.class);
    				
    				datiRicevuti.add(response);
    			}
    				
    			
    			else
    			{
    				logger.info("Preparazione invocazione servizio REST...");
    				
    				/* mappa dei parametri per la chiamata al servizio */
        			Map<String, String> mappaParametri = new HashMap<>();
        			
        			/* ricerca ultima data di aggiornamento */
        			LocalDateTime ultimaData = anagraficaService.findMostRecentUpdate();
        			if(ultimaData != null)
        				mappaParametri.put("CQL_FILTER", DiviETLUtils.formatDateTimeForWFS(ultimaData, false));
        			
        			/* prima chiamata al servizio */
        			mappaParametri.put("STARTINDEX", "0");
        			mappaParametri.put("SORTBY", SORT_DATA_ULTIMO_AGGIORNAMENTO_ASC);
            		response = diviClient.getDivi(mappaParametri);
            		
            		/* variabili di controllo del flusso */
            		int numeroRecordRicevuti = response.getNumberReturned();
            		int numeroRecordTotali = response.getNumberMatched();
            		
            		logger.info("Effettuata prima chiamata al servizio.\nRecord restituiti: {}\nRecord totali da recuperare: {}",
            				    numeroRecordRicevuti,
            				    numeroRecordTotali);
            		
            		/* primo inserimento in lista */
            		if(numeroRecordRicevuti > 0)
            			datiRicevuti.add(response);
            		
            		else
            		{
            			logger.info("La procedura non ha ricevuto nessun record e verra' terminata");
            			return;
            		}
            		
            		while(numeroRecordRicevuti < numeroRecordTotali)
            		{
            			mappaParametri.put("STARTINDEX", String.valueOf(numeroRecordRicevuti));
            			response = diviClient.getDivi(mappaParametri);
            			numeroRecordRicevuti += response.getNumberReturned();
            			
            			datiRicevuti.add(response);
            			
            		}
    			}
    			
        		logger.info("Creazione batch job...");
        		BigDecimal idBatch = batchService.insertBatchJob(null, "JSON");
                
                logger.info("Terminata procedura automatica di ricerca dati da DIVI...");
                diviService.insertDiviData(datiRicevuti, idBatch);
    		}
    		
    		catch (Throwable ex)
    		{
    			logger.info("Si e' verificata un'eccezione durante l'esecuzione della procedura", ex);
    		}    		
    	}

    	else if (requestMode.trim().toLowerCase().equals("ftp"))
    	{
    		
    		/* lettura dati da FTP */
    	}
    	
    	else
    	{
    		logger.info("Modalita' di richiesta dati {} non valida. Il processo verra' terminato.", requestMode);
    		return;
    	}

    }
}
