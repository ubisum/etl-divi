package it.almaviva.mic.etl.divi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.almaviva.mic.etl.divi.dto.EsitoDTO;

@RestController
@RequestMapping("/etl/divi") 
@Tag(name = "Caricamento dati DIVI", description = "Set di servizi per l'avvio delle procedure di caricamento ed il loro monitoraggio")
public class EtlDiviController 
{
	private static final Logger logger = LoggerFactory.getLogger(EtlDiviController.class);
	
	@GetMapping("/test")
	@Operation(
	        summary = "Test di connessione",
	        description = "Restituisce una semplice risposta di conferma dell'attivita' dell'applicazione"
	    )
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Utente trovato")
	    })
	public ResponseEntity<EsitoDTO> testConnessione()
	{
		logger.info("Inizio procedura");
		
		EsitoDTO esito = new EsitoDTO();
		esito.setCodice(200);
		esito.setMessaggio("Tutto a posto");
		
		logger.debug("Creazione esito");
		logger.error("Fine esecuzione");
		return ResponseEntity.ok().body(esito);
	}
}
