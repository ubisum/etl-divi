package it.almaviva.mic.etl.divi.cron;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.divi.dto.json.DiviResponse;
import it.almaviva.mic.etl.divi.web.DiviClient;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiviScheduler {

    private final DiviClient diviClient;
    private static final String SORT_DATA_ULTIMO_AGGIORNAMENTO_ASC = "data_ultimo_aggiornamento asc";
    private static final Logger logger = LoggerFactory.getLogger(DiviScheduler.class);
    
    @Value("${divi.request.mode}")
    private String requestMode;

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
    			/* prima chiamata al servizio */
        		DiviResponse response = diviClient.getDivi(0, SORT_DATA_ULTIMO_AGGIORNAMENTO_ASC);
        		
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
        			response = diviClient.getDivi(numeroRecordRicevuti, SORT_DATA_ULTIMO_AGGIORNAMENTO_ASC);
        			numeroRecordRicevuti += response.getNumberReturned();
        			
        			datiRicevuti.add(response);
        			
        		}
        		
                
                logger.info("Terminata procedura automatica di ricerca dati da DIVI...");
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
