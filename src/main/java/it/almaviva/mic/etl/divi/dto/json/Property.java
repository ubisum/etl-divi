package it.almaviva.mic.etl.divi.dto.json;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Property 
{
	@JsonProperty("immo_regione")
    private String immoRegione;

    @JsonProperty("immo_provincia")
    private String immoProvincia;

    @JsonProperty("immo_comune")
    private String immoComune;

    @JsonProperty("immo_catasto")
    private String immoCatasto;

    @JsonProperty("immo_codice_catastale_comune")
    private String immoCodiceCatastaleComune;

    @JsonProperty("immo_sezione_censuaria")
    private String immoSezioneCensuaria;

    @JsonProperty("immo_sezione_urbana")
    private String immoSezioneUrbana;

    @JsonProperty("immo_tipo_immobile")
    private String immoTipoImmobile;

    @JsonProperty("immo_numero_foglio")
    private String immoNumeroFoglio;

    @JsonProperty("immo_codice_allegato")
    private String immoCodiceAllegato;

    @JsonProperty("immo_codice_sviluppo")
    private String immoCodiceSviluppo;

    @JsonProperty("immo_particella")
    private String immoParticella;

    @JsonProperty("immo_subalterno")
    private String immoSubalterno;

    @JsonProperty("immo_identificazione")
    private String immoIdentificazione;

    @JsonProperty("prov_immo_tipo_tutela")
    private String provImmoTipoTutela;

    @JsonProperty("prov_immo_parte")
    private Boolean provImmoParte;

    @JsonProperty("indi_regione")
    private String indiRegione;

    @JsonProperty("indi_provincia")
    private String indiProvincia;

    @JsonProperty("indi_comune")
    private String indiComune;

    @JsonProperty("indi_odonimo")
    private String indiOdonimo;

    @JsonProperty("indi_civico")
    private String indiCivico;

    @JsonProperty("indi_snc")
    private Boolean indiSnc;

    @JsonProperty("indi_localita")
    private String indiLocalita;

    @JsonProperty("indi_provvedimento")
    private Boolean indiProvvedimento;

    @JsonProperty("indi_principale")
    private Boolean indiPrincipale;

    @JsonProperty("prov_indirizzi_originali_txt")
    private String provIndirizziOriginaliTxt;

    @JsonProperty("prov_indirizzi_attuali_txt")
    private String provIndirizziAttualiTxt;

    @JsonProperty("prov_indirizzi_json")
    private String provIndirizziJson;

    @JsonProperty("bene_altra_localizzazione")
    private String beneAltraLocalizzazione;

    @JsonProperty("posizione_x")
    private BigDecimal posizioneX;

    @JsonProperty("posizione_y")
    private BigDecimal posizioneY;

    @JsonProperty("posizione_origine")
    private String posizioneOrigine;

    @JsonProperty("bene_id")
    private Integer beneId;

    @JsonProperty("bene_classe")
    private String beneClasse;

    @JsonProperty("bene_tipo")
    private String beneTipo;

    @JsonProperty("bene_denominazione")
    private String beneDenominazione;

    @JsonProperty("prov_id")
    private Integer provId;

    @JsonProperty("prov_oggetto")
    private String provOggetto;

    @JsonProperty("prov_identificativi_catalografici")
    private String provIdentificativiCatalografici;

    @JsonProperty("prov_tipo_tutela")
    private String provTipoTutela;

    @JsonProperty("prov_tipo_strumento_tutela")
    private String provTipoStrumentoTutela;

    @JsonProperty("prov_legge")
    private String provLegge;

    @JsonProperty("prov_rif_normativo")
    private String provRifNormativo;

    @JsonProperty("prov_data")
    private String provData;

    @JsonProperty("ente_id")
    private Integer enteId;

    @JsonProperty("ente_nome_breve")
    private String enteNomeBreve;

    @JsonProperty("ente_nome_esteso")
    private String enteNomeEsteso;

    @JsonProperty("ente_cf")
    private String enteCf;

    @JsonProperty("ente_p_iva")
    private String entePIva;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("data_ultimo_aggiornamento")
    private String dataUltimoAggiornamento;

    @JsonProperty("beni_cert")
    private Integer beniCert;
}
