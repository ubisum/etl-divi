package it.almaviva.mic.etl.divi.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
@Table(name = "lp_ente_competente_hist")
class LpEnteCompetenteHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ecp_hist")
    private BigDecimal idEcpHist;

    private Integer source_id;
    private String codice_ecp;
    private String denominazione;
    private String sigla;
    private String codice_fiscale;
    private String partita_iva;
    private String hash_payload;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
    private Boolean is_current;
    
    @ManyToOne
	@JoinColumn(name = "enteCompetente", nullable = false)
    List<LpProvvedimentoHist> listaProvevdimenti;
    
    @ManyToOne
	@JoinColumn(name = "batch_id", nullable = false)
	private BatchJob batchJob;
}