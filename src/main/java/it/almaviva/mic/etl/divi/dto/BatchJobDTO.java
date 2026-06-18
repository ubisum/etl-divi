package it.almaviva.mic.etl.divi.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BatchJobDTO 
{
	private BigDecimal batchId;
	private String fonte;
	private String tipoCarico;
	private String avvioTs;
	private String fineTs;
	private String esito;
}
