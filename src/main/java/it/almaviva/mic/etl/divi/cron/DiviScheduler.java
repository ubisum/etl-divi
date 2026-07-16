package it.almaviva.mic.etl.divi.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.divi.dto.json.DiviResponse;
import it.almaviva.mic.etl.divi.web.DiviClient;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiviScheduler {

    private final DiviClient diviClient;
    private static final Logger logger = LoggerFactory.getLogger(DiviScheduler.class);

    @Scheduled(cron = "${divi.cron}")
    public void execute() {
    	logger.info("Avvio procedura automatica di ricerca dati da DIVI...");

        DiviResponse response = diviClient.getDivi();
        
        logger.info("Avvio procedura automatica di ricerca dati da DIVI...");

    }
}
