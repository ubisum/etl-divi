package it.almaviva.mic.etl.divi.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import it.almaviva.mic.etl.divi.entities.BatchJobDettaglio;

public interface BatchJobDettaglioRepository extends JpaRepository<BatchJobDettaglio, BigDecimal> 
{

}
