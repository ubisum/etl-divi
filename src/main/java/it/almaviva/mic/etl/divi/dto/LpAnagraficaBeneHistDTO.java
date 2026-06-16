package it.almaviva.mic.etl.divi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LpAnagraficaBeneHistDTO 
{
    private String nctn;
    private String nctr;
    private String ncts;
    private String categoria_id;
    private String tipologia_id;
    private String denominazione;
    private String condiz_giuridica;
    private String destinaz_uso;

}