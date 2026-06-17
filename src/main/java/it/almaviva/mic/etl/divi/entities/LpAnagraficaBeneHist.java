package it.almaviva.mic.etl.divi.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.*;

@Entity
@Data
@Table(name = "lp_anagrafica_bene_hist")
class LpAnagraficaBeneHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bene_hist")
    private Long idBeneHist;

    private String nctn;
    private String nctr;
    private String ncts;
    private Integer categoria_id;
    private Integer tipologia_id;
    private String denominazione;
    private String condiz_giuridica;
    private String destinaz_uso;
    private String hash_payload;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
    private Boolean is_current;
    private Long batch_id;
}