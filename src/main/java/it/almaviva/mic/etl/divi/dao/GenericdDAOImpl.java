package it.almaviva.mic.etl.divi.dao;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import it.almaviva.mic.etl.divi.converters.DiviConverter;
import it.almaviva.mic.etl.divi.dto.BatchJobDTO;
import it.almaviva.mic.etl.divi.dto.json.Feature;
import it.almaviva.mic.etl.divi.dto.json.Property;
import it.almaviva.mic.etl.divi.entities.BatchJob;
import it.almaviva.mic.etl.divi.enums.DiviEsitoBatchJob;
import it.almaviva.mic.etl.divi.exceptions.DiviETLException;
import it.almaviva.mic.etl.divi.repositories.BatchJobRepository;
import it.almaviva.mic.etl.divi.utils.DiviETLConsts;
import it.almaviva.mic.etl.divi.utils.DiviETLUtils;
import it.almaviva.mic.etl.divi.utils.HashingUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;


@Component
public class GenericdDAOImpl implements GenericDAO 
{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
    private DataSource dataSource;
	
	@Autowired
	BatchJobRepository batchRepository;
	
	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	private String batchSize;
	
	private static final Logger logger = LoggerFactory.getLogger(GenericdDAOImpl.class);
	
	@Override
	public BatchJobDTO findUltimoBatchJobAttivo() 
	{
		logger.info("Ricerca dell'ultimo batch attivo...");
		
		Optional<BatchJob> ultimoBatchAttivo = batchRepository.findTopByOrderByAvvioTsDesc();
		if(ultimoBatchAttivo.isEmpty())
		{
			logger.info("Nessun batch trovato");
			return null;
		}
		
		logger.info("Trovato batch job con identificativo {} nello stato {}, iniziato con timestamp {}. Timestamp di terminazione: {}",
				    ultimoBatchAttivo.get().getBatchId(),
				    ultimoBatchAttivo.get().getEsito() != null ? ultimoBatchAttivo.get().getEsito() : "Non terminato",
				    DiviETLUtils.formatDateTime(ultimoBatchAttivo.get().getAvvioTs()),
				    ultimoBatchAttivo.get().getFineTs() != null ? DiviETLUtils.formatDateTime(ultimoBatchAttivo.get().getFineTs()) : "");
		
		return DiviConverter.convertBatchJobFromEntity(ultimoBatchAttivo.get());
	}
	
	@Override
	public void inserisciDettagliBatchJob(Map<Integer, List<String>> errori, BigDecimal idJob, String filename) 
	{
		logger.info("Inserimento dettagli del job con identificativo {}", idJob);
		
		if(errori == null || errori.size() == 0)
		{
			logger.info("Nessun errore presente, non verranno inseriti dettagli");
			return;
		}
		
		try
		{
			logger.info("Ricerca delle informazioni relative al batch job...");
			
			Optional<BatchJob> job = batchRepository.findById(idJob);
			if(job.isEmpty())
				throw new DiviETLException("Nessun job presente con l'ID segnalato", HttpStatus.INTERNAL_SERVER_ERROR);
			
			/* definizione del prepared statement */
			String sql = "INSERT INTO asset_mgmt.batch_job_dettaglio "
					   + "(batch_id, raw_id, file_name, esito, error_message, processed_ts) "
					   + "VALUES(?, ?, ?, ?, ?, ?)";
			
			/* connessione */
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			/* creazione prepared statement */
			PreparedStatement ps = connection.prepareStatement(sql);
			
			/* popolamento */
			popolamentoDettagli(ps, errori, idJob, LocalDateTime.now(), filename);
			//ps.addBatch();
			
			/* esecuzione */
			ps.executeBatch();
			
		}
		
		catch(DiviETLException mee)
		{
			/* si rilancia l'eccezione verso il controller */
			throw new DiviETLException(mee.getMessage(), mee.getStatus());
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificata un'eccezione durante l'aggiornamento del job", ex);
			throw new DiviETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@Override
	public Integer eseguiStoredProcedureContaRecord(String procedure) 
	{
		logger.info("Accesso alla funzione di esecuzione delle stored procedure");
		if(StringUtils.isBlank(procedure))
		{
			logger.info("Nome della storeed procedure fornita e' pari a NULL");
			throw new DiviETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Richiesta esecuzione stored procedure {}", procedure);
		
		try
		{
			/* creazione stored procedure */
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery(procedure);
			
			/* registrazione del parametro di output */
			query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.OUT);
			
			/* esecuzione */
			query.execute();
			
			/* restituzione numero righe inserite */
			return (Integer)query.getOutputParameterValue(1);

		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errroe durante l'esecuzione della procedure {}", procedure, ex);
			throw new DiviETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	
	@Override
	public void eseguiStoredProcedure(String procedure) 
	{
		logger.info("Accesso alla funzione di esecuzione delle stored procedure");
		if(StringUtils.isBlank(procedure))
		{
			logger.info("Nome della storeed procedure fornita e' pari a NULL");
			throw new DiviETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.info("Richiesta esecuzione stored procedure {}", procedure);
		
		try
		{
			entityManager.createNativeQuery("CALL " + procedure + "()").executeUpdate();
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errroe durante l'esecuzione della procedure {}", procedure, ex);
			throw new DiviETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}

	@Override
	public BigDecimal insertBatchJob(String fonte, String tipoCarico) 
	{
		logger.info("Inserimento job {}...", fonte);
		try
		{
			logger.info("Creazione dell'entita'...");
			BatchJob job = new BatchJob();
			job.setFonte(fonte);
			job.setTipoCarico(tipoCarico);
			job.setAvvioTs(LocalDateTime.now());
			
			logger.info("Inserimento sul database...");
			BatchJob insertedRow =  batchRepository.save(job);
			
			logger.info("Inserito job con identificativo {}", insertedRow.getBatchId());
			
			return insertedRow.getBatchId();
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificato un errore durante l'inserimento del job {}", fonte, ex);
			throw new DiviETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		
	}

	@Override
	public void updateBatchJob(BigDecimal idJob, DiviEsitoBatchJob esito) 
	{
		logger.info("Aggiornamento job con identificativo {}...", idJob);
		
		try
		{
			if(idJob == null)
			{
				logger.info("Identificativo del job non valido");
				throw new DiviETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			logger.info("Ricerca del job sul database...");
			Optional<BatchJob> job = batchRepository.findById(idJob);
			
			if(job.isEmpty())
			{
				logger.info("Impossibile trovare un job con l'identificativo specificato");
				throw new DiviETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			logger.info("Preparazione all'aggiornamento del job...");
			job.get().setEsito(esito.getEsito());
			job.get().setFineTs(LocalDateTime.now());
			
			logger.info("Salvataggio in corso...");
			batchRepository.save(job.get());
			
			logger.info("Salvataggio job completatao");
		}
		
		catch(DiviETLException mee)
		{
			logger.info("Si e' verificata un'eccezione durante l'aggiornamento del job", mee);
			
			/* si rilancia l'eccezione verso il controller */
			throw new DiviETLException(mee.getMessage(), mee.getStatus());
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificata un'eccezione durante l'aggiornamento del job", ex);
			throw new DiviETLException("Si e' verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* metodo di popolamento della insert degli indirizzi */
	private void popolamentoDettagli(PreparedStatement ps, Map<Integer, List<String>> validationErrors, BigDecimal idBatch, LocalDateTime ldt, String filename) throws SQLException
	{
		/* grandezza batch */
		Integer maxNumRecords = null;
		if(StringUtils.isBlank(batchSize))
		{
			logger.info("Nessuna property indicante la misura del batch trovata. Si imposta la grandezza massima di default a 1000");
			maxNumRecords = Integer.valueOf(1000);
		}
		
		else
			maxNumRecords = Integer.valueOf(batchSize);
		
		/* iterazione sugli errori */
		int counter = 0;
		for(Integer row : validationErrors.keySet())
		{
			/* elenco violazioni */
			String violations = String.join(" - ", validationErrors.get(row));
			
			/* riempimento */
			ps.setBigDecimal(1, idBatch);
			ps.setString(2, row.toString());
			ps.setString(3, filename);
			ps.setString(4, "KO");
			ps.setString(5, violations);
			ps.setObject(6, ldt);
			
			ps.addBatch();
			
			/* controllo del raggiungimento del numero massimo di elementi per batch */
			if(++counter % Integer.valueOf(maxNumRecords) == 0)
				ps.executeBatch();
			
		}
	}

	@Override
	public void insertDiviData(List<Feature> listaFeature, BigDecimal idBatch) 
	{
		logger.info("Inserimento dati da DIVI...");
		
		if(CollectionUtils.isEmpty(listaFeature))
		{
			logger.info("Nessun record da inserire trovato");
			return;
		}
		
		/* grandezza batch */
		Integer maxNumRecords = null;
		if(StringUtils.isBlank(batchSize))
		{
			logger.info("Nessuna property indicante la misura del batch trovata. Si imposta la grandezza massima di default a 1000");
			maxNumRecords = Integer.valueOf(1000);
		}
		
		else
			maxNumRecords = Integer.valueOf(batchSize);
		
		try
		{
			logger.info("Sono presenti {} record da inserire nella tabella di staging", listaFeature.size());
			
			logger.info("Creazione connessione verso il DB...");
			Session session = entityManager.unwrap(Session.class);
			Connection conn = session.doReturningWork(c -> c);
			
			logger.info("Lettura del codice SQL per la creazione della tabella temporanea...");
			String sqlTabellaTemporanea = DiviETLUtils.readContentFromFile(DiviETLConsts.DIVI_CREATE_STAGING);
			if(StringUtils.isEmpty(sqlTabellaTemporanea))
			{
				logger.info("Impossibile leggere il codice per la creazione della tabella di staging");
				throw new DiviETLException("Impossibile leggere codice per la creazione della tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			logger.info("Rimozione della tabella temporanea (se presente)...");
			Statement createStagingStmt = conn.createStatement();
			createStagingStmt.execute("DROP TEMPORARY TABLE IF EXISTS DIVI_STAGING");
//			createStagingStmt.execute("DROP TABLE IF EXISTS DIVI_STAGING");
			
			logger.info("Creazione tabella temporanea...");
			createStagingStmt.executeUpdate(sqlTabellaTemporanea);
			
			logger.info("Lettura codice SQL per l'inserimento dei record nella tabella di staging...");
			String sqlInserimentoRecord = DiviETLUtils.readContentFromFile(DiviETLConsts.DIVI_INSERT_STAGING);
			if(StringUtils.isEmpty(sqlInserimentoRecord))
			{
				logger.info("Impossibile leggere il codice per l'inserimento dei record nella tabella di staging");
				throw new DiviETLException("Impossibile leggere il codice per l'inserimento dei record nella tabella di staging", 
						                    HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			/* creazione del prepared statement */
			PreparedStatement inserimentoRecordPs = conn.prepareStatement(sqlInserimentoRecord);
			
			/* contatore dei record */
			int counter = 0;
			
			for(Feature feature : listaFeature)
			{
				if(feature.getProperties() != null)
				{
					/* creazione dell'i-simo record sulla tabella temporanea */
					popolamentoRecordDivi(inserimentoRecordPs, feature.getProperties(), idBatch);
					
					/* aggiunta al batch */
					inserimentoRecordPs.addBatch();
					
					/* controllo del raggiungimento del numero massimo di elementi per batch */
					if(++counter % Integer.valueOf(batchSize) == 0)
						inserimentoRecordPs.executeBatch();
				}
				
			}
			
			/* esecuzione del batch, se non avvenuto nel ciclo */
			inserimentoRecordPs.executeBatch();
			
			logger.info("Inserimento terminato");
			
			/* verifica del numero dei record effettivamente scritti */
			logger.info("Verifica dei record effettivamente scritti sulla tabella temporanea...");
			
			String sqlCountRecords = "SELECT COUNT(*) FROM DIVI_STAGING";
			Statement countRecords = conn.createStatement();
			
			ResultSet result = countRecords.executeQuery(sqlCountRecords);
			Integer numeroRecordScritti = result.next() ? result.getInt(1) : 0;
			
			logger.info("Record effettivamente inseriti sulla tabella di staging: {}", numeroRecordScritti);
			
			logger.info("Terminato inserimento indirizzi in tabella di staging");
		}
		
		catch(Throwable ex)
		{
			logger.info("Si e' verificata un'eccezione", ex);
		}
	}
	
	private void popolamentoRecordDivi(PreparedStatement ps, Property property, BigDecimal idBatch) throws SQLException, NoSuchAlgorithmException
	{
		/* iterazione sui record */
		Integer counter = 1;
		
		/* funzioni di inserimento dati */
		counter = insertDatiBene(ps, property, counter);
		counter = insertDatiCatasto(ps, property, counter);
		counter = insertDatiEnte(ps, property, counter);
		counter = insertDatiLocalita(ps, property, counter);
		counter = insertDatiProvvedimento(ps, property, counter);
		
		/* inserimento ID del batch */
		ps.setBigDecimal(counter++, idBatch);
	}
	
	private Integer insertDatiProvvedimento(PreparedStatement ps, Property property, Integer counter)throws SQLException, NoSuchAlgorithmException
	{
		/* INSERIMENTO PARAMETRI DEL PROVVEDIMENTO  ---------------------------------------------------------------------------- */
		StringBuilder prov_sb = new StringBuilder();
		
		/* id atto */
		if(property.getProvId() != null)
		{
			prov_sb.append(property.getProvId() + "|");
			ps.setString(counter++, property.getProvId().toString());
		}
			
		else
		{
			prov_sb.append("NULL" + "|");
			ps.setNull(counter++, java.sql.Types.VARCHAR);
		}
		
		/* tipo */
		if(property.getProvTipoTutela() != null)
			prov_sb.append(property.getProvTipoTutela() + "|");
		
		else
			prov_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getProvTipoTutela());
		
		/* data */
		if(property.getProvData() != null)
		{
			prov_sb.append(property.getProvData().replace("Z", ""));
			LocalDate ld = LocalDate.parse(property.getProvData().replace("Z", ""));
			
			ps.setObject(counter++, ld);
		}
		
		else
		{
			prov_sb.append("NULL");
			ps.setNull(counter++, java.sql.Types.DATE);
		}
		
		/* hash */
		ps.setString(counter++, HashingUtils.getHashingCode(prov_sb.toString()));
		
		return counter;
			
		
	}
	
	private Integer insertDatiLocalita(PreparedStatement ps, Property property, Integer counter)throws SQLException, NoSuchAlgorithmException 
	{
		/* INSERIMENTO PARAMETRI DELLA LOCALITA' ---------------------------------------------------------------------------- */
		StringBuilder localita_sb = new StringBuilder();
		
		/* regione */
		if(property.getIndiRegione() != null)
			localita_sb.append(property.getIndiRegione() + "|");
		
		else
			localita_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getIndiRegione());
		
		/* provincia */
		if(property.getIndiProvincia() != null)
			localita_sb.append(property.getIndiProvincia() + "|");
		
		else
			localita_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getIndiProvincia());
		
		/* comune */
		if(property.getIndiComune() != null)
			localita_sb.append(property.getIndiComune() + "|");
		
		else
			localita_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getIndiComune());
		
		/* indirizzo */
		if(property.getIndiOdonimo() != null)
			localita_sb.append(property.getIndiOdonimo() + "|");
		
		else
			localita_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getIndiOdonimo());
		
		/* civico */
		if(property.getIndiCivico() != null)
			localita_sb.append(property.getIndiCivico() + "|");
		
		else
			localita_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getIndiCivico());
		
		/* latitudine */
		if(property.getPosizioneX() != null)
		{
			localita_sb.append(property.getPosizioneX() + "|");
			ps.setBigDecimal(counter++, property.getPosizioneX());
		}
		
		else
		{
			localita_sb.append("NULL" + "|");
			ps.setNull(counter++, Types.DECIMAL);
		}
		
		/* longitudine */
		if(property.getPosizioneY() != null)
		{
			localita_sb.append(property.getPosizioneY());
			ps.setBigDecimal(counter++, property.getPosizioneY());
		}
		
		else
		{
			localita_sb.append("NULL");
			ps.setNull(counter++, Types.DECIMAL);
		}
		
		/* hash */
		ps.setString(counter++, HashingUtils.getHashingCode(localita_sb.toString()));
		
		return counter;
	}

	private Integer insertDatiBene(PreparedStatement ps, Property property, Integer counter)throws SQLException, NoSuchAlgorithmException 
	{
		/* INSERIMENTO PARAMETRI DEL BENE ---------------------------------------------------------------------------- */
		StringBuilder bene_sb = new StringBuilder();
		
		/* source ID */
		if(property.getBeneId() != null)
		{
			bene_sb.append(property.getBeneId().toString() + "|");
			ps.setInt(counter++, property.getBeneId());
		}
		
		else
		{
			bene_sb.append("NULL" + "|");
			ps.setNull(counter++, java.sql.Types.INTEGER);
		}
		
		
		/* classe */
//		if(property.getBeneClasse() != null)
//			bene_sb.append(property.getBeneClasse() + "|");
//		
//		else
//			bene_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getBeneClasse());
		
		/* tipo */
//		if(property.getBeneTipo() != null)
//			bene_sb.append(property.getBeneTipo() + "|");
//		
//		else
//			bene_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getBeneTipo());
		
		/* denominazione */
//		if(property.getBeneDenominazione() != null)
//			bene_sb.append(property.getBeneDenominazione() + "|");
//		
//		else
//			bene_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getBeneDenominazione());
		
		/* data ultimo aggiornamento */
		if(property.getDataUltimoAggiornamento() != null)
		{
			bene_sb.append(property.getDataUltimoAggiornamento());
			Instant instant = Instant.parse(property.getDataUltimoAggiornamento());
			ps.setTimestamp(counter++, Timestamp.from(instant), Calendar.getInstance(TimeZone.getTimeZone("UTC")));
		}
		
		else
		{
			bene_sb.append("NULL");
			ps.setTimestamp(counter++, null);
		}
		
		/* hash */
		ps.setString(counter++, HashingUtils.getHashingCode(bene_sb.toString()));
		
		return counter;
	}
	
	private Integer insertDatiCatasto(PreparedStatement ps, Property property, Integer counter)throws SQLException, NoSuchAlgorithmException 
	{
		/* INSERIMENTO PARAMETRI DEL CATASTO ---------------------------------------------------------------------------- */
		StringBuilder catasto_sb = new StringBuilder();
		
		/* tipo catasto */
		if(property.getImmoTipoImmobile() != null)
			catasto_sb.append(property.getImmoTipoImmobile() + "|");
		
		else
			catasto_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getImmoTipoImmobile());
		
		/* codice catastale */
		if(property.getImmoCodiceCatastaleComune() != null)
			catasto_sb.append(property.getImmoCodiceCatastaleComune() + "|");
		
		else
			catasto_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getImmoCodiceCatastaleComune());
		
		/* sezione censuaria */
		if(property.getImmoSezioneCensuaria() != null)
			catasto_sb.append(property.getImmoSezioneCensuaria() + "|");
		
		else
			catasto_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getImmoSezioneCensuaria());
		
		/* sezione urbana */
		if(property.getImmoSezioneUrbana() != null)
			catasto_sb.append(property.getImmoSezioneUrbana() + "|");
		
		else
			catasto_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getImmoSezioneUrbana());
		
		/* foglio */
		if(property.getImmoNumeroFoglio() != null)
			catasto_sb.append(property.getImmoNumeroFoglio() + "|");
		
		else
			catasto_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getImmoNumeroFoglio());
		
		/* particella */
		if(property.getImmoParticella() != null)
			catasto_sb.append(property.getImmoParticella() + "|");
		
		else
			catasto_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getImmoParticella());
		
		/* subalterno */
		if(property.getImmoSubalterno() != null)
			catasto_sb.append(property.getImmoSubalterno() + "|");
		
		else
			catasto_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getImmoSubalterno());
		
		/* tipo immobile */
		if(property.getImmoCatasto() != null)
			catasto_sb.append(property.getImmoCatasto());
		
		else
			catasto_sb.append("NULL");
		
		ps.setString(counter++, property.getImmoCatasto());
		
		/* hash */
		ps.setString(counter++, HashingUtils.getHashingCode(catasto_sb.toString()));
		
		return counter;
	}
	
	private Integer insertDatiEnte(PreparedStatement ps, Property property, Integer counter)throws SQLException, NoSuchAlgorithmException
	{
		/* INSERIMENTO PARAMETRI DELL'ENTE ---------------------------------------------------------------------------- */
		StringBuilder ente_sb = new StringBuilder();
		
		/* nome esteso */
		if(property.getEnteNomeEsteso() != null)
			ente_sb.append(property.getEnteNomeEsteso() + "|");
		
		else
			ente_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getEnteNomeEsteso());
		
		/* sigla */
		if(property.getEnteNomeBreve() != null)
			ente_sb.append(property.getEnteNomeBreve() + "|");
		
		else
			ente_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getEnteNomeBreve());
		
		/* cf */
		if(property.getEnteCf() != null)
			ente_sb.append(property.getEnteCf() + "|");
		
		else
			ente_sb.append("NULL" + "|");
		
		ps.setString(counter++, property.getEnteCf());
		
		/* partita IVA */
		if(property.getEntePIva() != null)
			ente_sb.append(property.getEntePIva());
		
		else
			ente_sb.append("NULL");
		
		ps.setString(counter++, property.getEntePIva());
		
		/*hash */
		ps.setString(counter++, HashingUtils.getHashingCode(ente_sb.toString()));
		
		return counter;
		
	}
}
