package it.almaviva.mic.etl.divi.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "lkp_categoria_bene")
public class LkpCategoriaBene {

	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "label")
	private String label;
	
	@OneToMany(mappedBy = "categoria_id", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LpAnagraficaBeneHist> listaAnagrafiche;
	
}
