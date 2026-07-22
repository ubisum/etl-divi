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
@Table(name = "lp_tr_bene_loc_hist")
public class LpTrBeneLocHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bl_hist")
    private BigDecimal idBlHist;

    @ManyToOne
    @JoinColumn(name = "id_bene_hist", nullable = false)
    private LpAnagraficaBeneHist bene;

    @ManyToOne
    @JoinColumn(name = "id_loc_hist", nullable = false)
    private LpLocalizzazioneHist localizzazione;

    private String hash_link;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
    private Boolean is_current;

    @ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}
