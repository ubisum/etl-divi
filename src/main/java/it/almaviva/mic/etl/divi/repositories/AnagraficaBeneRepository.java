package it.almaviva.mic.etl.divi.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.almaviva.mic.etl.divi.entities.LpAnagraficaBeneHist;

public interface AnagraficaBeneRepository extends JpaRepository<LpAnagraficaBeneHist, BigDecimal> 
{
	@Query("SELECT MAX(d.data_ultimo_aggiornamento) FROM LpAnagraficaBeneHist d")
    LocalDateTime findMaxDataUltimoAggiornamento();
}
