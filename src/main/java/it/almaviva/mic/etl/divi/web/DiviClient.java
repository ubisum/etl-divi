package it.almaviva.mic.etl.divi.web;


import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import it.almaviva.mic.etl.divi.dto.json.DiviResponse;
import it.almaviva.mic.etl.divi.exceptions.DiviETLException;

@Component
public class DiviClient 
{
    private final RestClient restClient;
    private final String url;
    private final String authorization;
    private final String elems;

    private static final Logger logger = LoggerFactory.getLogger(DiviClient.class);

    public DiviClient(RestClient restClient, 
    		          @Value("${divi.url}") String url, 
    		          @Value("${divi.authorization}") String authorization,
    		          @Value("${divi.request.elems}") String elems) 
    {
        this.restClient = restClient;
        this.url = url;
        this.authorization = authorization;
        this.elems = elems;
    }


    public DiviResponse getDivi(Map<String, String> params) 
    {
    	logger.info("Preparazione chiamata a servizio DIVI...");
    	ResponseEntity<DiviResponse> response = null;
    	try
    	{
    		  UriComponentsBuilder builder = UriComponentsBuilder
    		            .fromHttpUrl(url)
    		            .queryParam("SERVICE", "WFS")
	                    .queryParam("REQUEST", "GetFeature")
	                    .queryParam("VERSION", "2.0.0")
	                    .queryParam("OUTPUTFORMAT", "application/json")
	                    .queryParam("TYPENAMES", "divi:immobili_tutelati_04_1");
    		  
    		  /* aggiunta indici */
    		  for(String key : params.keySet())
    			  builder.queryParam(key, params.get(key));
    		  
    		  /* aggiunta numero di record richiesti */
    		  if(StringUtils.isNotBlank(this.elems) && Integer.parseInt(this.elems) > 0)
    			  builder.queryParam("COUNT", this.elems);
    		  
    		  logger.info("Tentativo di connessione all'URL {}", builder.build().toUriString());
    		
    		response = restClient.get().uri(builder.build().
                    encode().
                    toUri()).
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
    			throw new DiviETLException("Impossibile effettuare la chiamata all'URL previsto", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
    	catch(NumberFormatException nfe)
    	{
    		logger.info("Si e' verificata un'eccezione durante una conversione da stringa ad intero. "
    				  + " Verificare l'impostazione del numero di elementi richiesti", nfe);
    		throw new DiviETLException("Si e' verificata un'eccezione durante una conversione da stringa ad intero. "
    				                  + "Verificare l'impostazione del numero di elementi richiesti", 
    				                    HttpStatus.INTERNAL_SERVER_ERROR);
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