package it.almaviva.mic.etl.divi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LpLocalizzazioneHistDTO 
{
    private String regione;
    private String provincia;
    private String comune;
    private String indirizzo;
    private String civico;
    private String lat;
    private String lon;
}