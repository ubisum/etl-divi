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

@Entity
@Data
@Table(name = "lp_tr_bene_dc_hist")
public class LpTrBeneDcHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bd_hist")
    private BigDecimal idBdHist;

    @ManyToOne
    @JoinColumn(name = "id_bene_hist", nullable = false)
    private LpAnagraficaBeneHist bene;

    @ManyToOne
    @JoinColumn(name = "id_dc_hist", nullable = false)
    private LpDatoCatastaleHist datoCatastale;

    private String hash_link;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
    private Boolean is_current;

    @ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}