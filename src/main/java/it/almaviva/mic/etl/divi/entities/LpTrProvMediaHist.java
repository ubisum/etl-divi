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
@Table(name = "lp_tr_prov_media_hist")
class LpTrProvMediaHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pm_hist")
    private BigDecimal idPmHist;

    @ManyToOne
    @JoinColumn(name = "id_prov_hist", nullable = false)
    private LpProvvedimentoHist provvedimento;

    @ManyToOne
    @JoinColumn(name = "id_media_hist", nullable = false)
    private LpMultimediaHist multimedia;

    private String hash_link;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
    private Boolean is_current;

    @ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}
