package it.almaviva.mic.etl.divi.enums;

public enum DiviEsitoBatchJob 
{
	ESITO_KO("KO"),
	ESITO_OK("OK");
	
	private String esito;

	private DiviEsitoBatchJob(String esito) 
	{
		this.esito = esito;
	}

	public String getEsito() 
	{
		return esito;
	}
	
}
