package it.almaviva.mic.etl.divi.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "batch_job_dettaglio")
public class BatchJobDettaglio 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_det")
	private BigDecimal idDet;
	
	@Column(name = "raw_id")
	private String rowId;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "esito")
	private String esito;
	
	@Column(name = "error_message")
	private String errorMessage;
	
	@Column(name = "processed_ts")
	private LocalDateTime processedTs;
	
	@ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}
