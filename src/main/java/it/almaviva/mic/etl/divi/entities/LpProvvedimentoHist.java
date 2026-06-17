package it.almaviva.mic.etl.divi.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
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

@Entity
@Data
@Table(name = "lp_provvedimento_hist")
class LpProvvedimentoHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prov_hist")
    private BigDecimal idProvHist;

    private String id_atto;
    private String tipologia_prov;
    private LocalDate data_prov;

    @ManyToOne
    @JoinColumn(name = "id_ecp_hist", nullable = false)
    private LpEnteCompetenteHist enteCompetente;

    private String hash_payload;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
    private Boolean is_current;

    @ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}