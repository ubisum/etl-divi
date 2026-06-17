package it.almaviva.mic.etl.divi.entities;

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
@Table(name = "lp_dato_catastale_hist")
@Data
class LpDatoCatastaleHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dc_hist")
    private Long idDcHist;

    private String tipo_catasto;
    private String cod_catastale_comune;
    private String sezione_censuaria;
    private String sezione_urbana;
    private String foglio;
    private String particella;
    private String subalterno;
    private String tipo_immobile;
    private String hash_payload;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
    private Boolean is_current;
    
    @ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}