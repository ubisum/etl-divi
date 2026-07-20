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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "lp_anagrafica_bene_hist")
class LpAnagraficaBeneHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bene_hist")
    private BigDecimal idBeneHist;

    private Integer source_id;
    private String nctn;
    private String nctr;
    private String ncts;
    private BigDecimal id_ecp;
    
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private LkpCategoriaBene categoriaBene;
    
    @ManyToOne
    @JoinColumn(name = "tipologia_id", nullable = false)
    private LkpTipologiaBene tipologiaBene;
    
    @OneToMany(mappedBy = "bene", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LpTrBeneProvHist> listaBeniProv;
    
    @OneToMany(mappedBy = "bene", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LpTrBeneLocHist> listaBeniLoc;
    
    @OneToMany(mappedBy = "bene", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LpTrBeneDcHist> listaBeniDc;
    
    private String denominazione;
    private String condiz_giuridica;
    private String destinaz_uso;
    private String hash_payload;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
    private Boolean is_current;
    private LocalDateTime data_ultimo_aggiornamento;
    
    @ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}