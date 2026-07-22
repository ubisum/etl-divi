package it.almaviva.mic.etl.divi.services;

import java.math.BigDecimal;
import java.util.List;

import it.almaviva.mic.etl.divi.dto.json.DiviResponse;

public interface DiviService 
{
	public void insertDiviData(List<DiviResponse> listaRisposte, BigDecimal idBatch);
}
