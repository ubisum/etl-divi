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
@Table(name = "lp_tr_bene_prov_hist")
class LpTrBeneProvHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bp_hist")
    private BigDecimal idBpHist;

    @ManyToOne
    @JoinColumn(name = "id_bene_hist", nullable = false)
    private LpAnagraficaBeneHist bene;

    @ManyToOne
    @JoinColumn(name = "id_prov_hist", nullable = false)
    private LpProvvedimentoHist provvedimento;

    private String hash_link;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
    private Boolean is_current;
    private BigDecimal batch_id;
}
