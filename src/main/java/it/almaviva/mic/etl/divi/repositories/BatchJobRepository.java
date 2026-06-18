package it.almaviva.mic.etl.divi.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.almaviva.mic.etl.divi.entities.BatchJob;

public interface BatchJobRepository extends JpaRepository<BatchJob, BigDecimal> 
{
	Optional<BatchJob> findTopByOrderByAvvioTsDesc();
}
