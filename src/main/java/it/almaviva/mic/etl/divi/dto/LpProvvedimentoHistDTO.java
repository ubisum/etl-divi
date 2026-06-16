package it.almaviva.mic.etl.divi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LpProvvedimentoHistDTO 
{
    private String id_atto;
    private String tipologia_prov;
    private String data_prov;
    private String id_ecp_hist;
}