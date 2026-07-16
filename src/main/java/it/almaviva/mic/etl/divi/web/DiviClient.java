package it.almaviva.mic.etl.divi.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import it.almaviva.mic.etl.divi.dto.json.DiviResponse;
import it.almaviva.mic.etl.divi.exceptions.DivilETLException;

@Component
public class DiviClient 
{
    private final RestClient restClient;
    private final String url;
    private final String authorization;

    private static final Logger logger = LoggerFactory.getLogger(DiviClient.class);

    public DiviClient(RestClient restClient, @Value("${divi.url}") String url, @Value("${divi.authorization}") String authorization) 
    {
        this.restClient = restClient;
        this.url = url;
        this.authorization = authorization;
    }


    public DiviResponse getDivi() 
    {
    	logger.info("Preparazione chiamata a URL {}", url);
    	ResponseEntity<DiviResponse> response = null;
    	try
    	{
    		response = restClient.get().
                    uri(url).
                    header(HttpHeaders.AUTHORIZATION, "Basic " + authorization).
                    accept(MediaType.APPLICATION_JSON).
                    retrieve().
                    toEntity(DiviResponse.class);
    		
    		if(response != null && response.getStatusCode() != null && response.getStatusCode().is2xxSuccessful())
    		{
    			logger.info("Risposta ricevuta con successo");
    			return response.getBody();
    		}
    		
    		else
    			throw new DivilETLException("Impossibile effettuare la chiamata all'URL previsto", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
    	catch(Throwable ex)
    	{
    		logger.info("Si e' verificata un'eccezione durante la chiamata all'URL", ex);
    		if(response != null & response.getStatusCode() != null)
    			logger.info("Codice di errore ricevuto: {}", response.getStatusCode());
    		
    		return null;
    	}
    }
}