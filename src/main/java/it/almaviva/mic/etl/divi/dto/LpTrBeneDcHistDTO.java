package it.almaviva.mic.etl.divi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LpTrBeneDcHistDTO 
{
	private String id_bene_hist;
    private String id_dc_hist;
    private String hash_link;
}