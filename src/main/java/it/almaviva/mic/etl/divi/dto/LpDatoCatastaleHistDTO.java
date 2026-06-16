package it.almaviva.mic.etl.divi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LpDatoCatastaleHistDTO 
{

    private String tipo_catasto;
    private String cod_catastale_comune;
    private String sezione_censuaria;
    private String sezione_urbana;
    private String foglio;
    private String particella;
    private String subalterno;
    private String tipo_immobile;
}