package it.almaviva.mic.etl.divi.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import it.almaviva.mic.etl.divi.dto.BatchJobDTO;
import it.almaviva.mic.etl.divi.enums.DiviEsitoBatchJob;

public interface GenericDAO 
{
	public void eseguiStoredProcedure(String procedure);
	public Integer eseguiStoredProcedureContaRecord(String procedure);
	public BigDecimal insertBatchJob(String fonte, String tipoCarico);
	public void updateBatchJob(BigDecimal idJob, DiviEsitoBatchJob esito);
	public void inserisciDettagliBatchJob(Map<Integer, List<String>> errori, BigDecimal idJob, String filename);
	public BatchJobDTO findUltimoBatchJobAttivo();
}
