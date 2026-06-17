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
@Table(name = "lp_multimedia_hist")
class LpMultimediaHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_media_hist")
    private BigDecimal idMediaHist;

    private String url;
    private String titolo;
    private String descrizione;
    private String hash_payload;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
    private Boolean is_current;

    @ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}
