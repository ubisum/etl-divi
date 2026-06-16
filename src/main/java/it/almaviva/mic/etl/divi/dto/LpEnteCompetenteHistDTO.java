package it.almaviva.mic.etl.divi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LpEnteCompetenteHistDTO 
{
    private String codice_ecp;
    private String denominazione;
    private String sigla;
    private String codice_fiscale;
    private String partita_iva;
}