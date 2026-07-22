package it.almaviva.mic.etl.divi.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "batch_job")
public class BatchJob 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "batch_id")
	private BigDecimal batchId;
	
	@Column(name = "fonte")
	private String fonte;
	
	@Column(name = "tipo_carico")
	private String tipoCarico;
	
	@Column(name = "avvio_ts")
	private LocalDateTime avvioTs;
	
	@Column(name = "fine_ts")
	private LocalDateTime fineTs;
	
	@Column(name = "esito")
	private String esito;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LpAnagraficaBeneHist> listaAnagraficaBeneHist;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LpDatoCatastaleHist> listaDatoCatastaleHist;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LpEnteCompetenteHist> listaEntiCompetentiHist;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LpLocalizzazioneHist> listaLocalizzazioniHist;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LpMultimediaHist> listaMultimediaHist;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LpProvvedimentoHist> listaProvevdimentiHist;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LpTrBeneDcHist> listaTrBeniDcHist;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LpTrBeneLocHist> listaTrBeniLocHist;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LpTrBeneProvHist> listaTrBeniProvHist;
	
	@OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LpTrProvMediaHist> listaTrProvMediaHist;
	
}
