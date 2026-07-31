DROP PROCEDURE IF EXISTS sp_elabora_divi_staging;

DELIMITER $$

CREATE PROCEDURE sp_elabora_divi_staging()
BEGIN
    -- ----------------------------------------------------------------
    -- Variabili di controllo
    -- ----------------------------------------------------------------
    DECLARE done BOOLEAN DEFAULT FALSE;

    -- ----------------------------------------------------------------
    -- Variabili di appoggio DIVI_STAGING
    -- ----------------------------------------------------------------
    DECLARE v_bene_source_id INT;
    DECLARE v_bene_classe VARCHAR(200);
    DECLARE v_bene_tipo VARCHAR(200);
    DECLARE v_bene_denominazione VARCHAR(255);
    DECLARE v_bene_data_utimo_aggiornamento DATETIME(3);
    DECLARE v_bene_hash VARCHAR(64);
    DECLARE v_dc_tipo_catasto VARCHAR(1);
    DECLARE v_dc_codice_catastale VARCHAR(4);
    DECLARE v_dc_sezione_censuaria VARCHAR(3);
    DECLARE v_dc_sezione_urbana VARCHAR(3);
    DECLARE v_dc_foglio VARCHAR(5);
    DECLARE v_dc_particella VARCHAR(10);
    DECLARE v_dc_subalterno VARCHAR(6);
    DECLARE v_dc_tipo_immobile VARCHAR(50);
    DECLARE v_dc_hash VARCHAR(64);
    DECLARE v_ente_denominazione VARCHAR(255);
    DECLARE v_ente_sigla VARCHAR(30);
    DECLARE v_ente_cf VARCHAR(16);
    DECLARE v_ente_pi VARCHAR(11);
    DECLARE v_ente_hash VARCHAR(64);
    DECLARE v_loc_regione VARCHAR(50);
    DECLARE v_loc_provincia VARCHAR(50);
    DECLARE v_loc_comune VARCHAR(50);
    DECLARE v_loc_indirizzo VARCHAR(120);
    DECLARE v_loc_civico VARCHAR(20);
    DECLARE v_loc_lat DECIMAL(10,7);
    DECLARE v_loc_lon DECIMAL(10,7);
    DECLARE v_loc_hash VARCHAR(64);
    DECLARE v_prov_id_atto VARCHAR(60);
    DECLARE v_prov_tipo VARCHAR(60);
    DECLARE v_prov_data DATE;
    DECLARE v_prov_hash VARCHAR(64);
    DECLARE v_batch_id BIGINT;

    -- ----------------------------------------------------------------
    -- Variabili risultato elaborazione
    -- ----------------------------------------------------------------
    DECLARE v_id_classe INT DEFAULT NULL;
    DECLARE v_id_tipo INT DEFAULT NULL;
	DECLARE v_sigla_provincia VARCHAR(2) DEFAULT NULL;
	DECLARE v_nuovo_id INT DEFAULT NULL;
	
	DECLARE bene_id BIGINT DEFAULT NULL;
	DECLARE bene_hash VARCHAR(64) DEFAULT NULL;
	DECLARE bene_current INT DEFAULT 0;
	
	-- --------------------------------------------------------------
    -- Variabili di riferimento
    -- --------------------------------------------------------------
	DECLARE bene_ref INT DEFAULT NULL;
	DECLARE dc_ref INT DEFAULT NULL;
	DECLARE loc_ref INT DEFAULT NULL;
	DECLARE ente_ref INT DEFAULT NULL;
	DECLARE prov_ref INT DEFAULT NULL;
	
	DECLARE v_oggi_dt DATETIME;
	
    -- ----------------------------------------------------------------
    -- Dichiarazione cursore
    -- ----------------------------------------------------------------

     DECLARE cur CURSOR FOR
        SELECT
            bene_source_id,
            bene_classe,
            bene_tipo,
            bene_denominazione,
            bene_data_utimo_aggiornamento,
            bene_hash,
            dc_tipo_catasto,
            dc_codice_catastale,
            dc_sezione_censuaria,
            dc_sezione_urbana,
            dc_foglio,
            dc_particella,
            dc_subalterno,
            dc_tipo_immobile,
            dc_hash,
            ente_denominazione,
            ente_sigla,
            ente_cf,
            ente_pi,
            ente_hash,
            loc_regione,
            loc_provincia,
            loc_comune,
            loc_indirizzo,
            loc_civico,
            loc_lat,
            loc_lon,
            loc_hash,
            prov_id_atto,
            prov_tipo,
            prov_data,
            prov_hash,
            batch_id
        FROM DIVI_STAGING;


    -- ----------------------------------------------------------------
    -- Handler fine cursore
    -- ----------------------------------------------------------------
    DECLARE CONTINUE HANDLER FOR NOT FOUND
        SET done = TRUE;
		
	-- --------------------------------------------------------------
    -- Setting ariabili di utilita'
    -- --------------------------------------------------------------
	SET v_oggi_dt = NOW();
	
	INSERT
				INTO
				debug_sp_elabora_divi 
				(data_log,
				source_id,
				hash_valore,
				punto)
			    VALUES
				(CURRENT_TIMESTAMP, v_bene_source_id, v_bene_hash, 'init');

    -- ----------------------------------------------------------------
    -- Apertura cursore
    -- ----------------------------------------------------------------
    OPEN cur;

    read_loop: LOOP
        -- ----------------------------------------------------------------
        -- Lettura record corrente
        -- ----------------------------------------------------------------
        FETCH cur INTO
            v_bene_source_id,
            v_bene_classe,
            v_bene_tipo,
            v_bene_denominazione,
            v_bene_data_utimo_aggiornamento,
            v_bene_hash,
            v_dc_tipo_catasto,
            v_dc_codice_catastale,
            v_dc_sezione_censuaria,
            v_dc_sezione_urbana,
            v_dc_foglio,
            v_dc_particella,
            v_dc_subalterno,
            v_dc_tipo_immobile,
            v_dc_hash,
            v_ente_denominazione,
            v_ente_sigla,
            v_ente_cf,
            v_ente_pi,
            v_ente_hash,
            v_loc_regione,
            v_loc_provincia,
            v_loc_comune,
            v_loc_indirizzo,
            v_loc_civico,
            v_loc_lat,
            v_loc_lon,
            v_loc_hash,
            v_prov_id_atto,
            v_prov_tipo,
            v_prov_data,
            v_prov_hash,
            v_batch_id; 

        -- ----------------------------------------------------------------
        -- Recupero classe oppure creazione nuovo record
        -- ----------------------------------------------------------------
        SET v_id_classe = NULL;

		IF v_bene_classe IS NOT NULL THEN
			BEGIN
				DECLARE v_classe_trovata BOOLEAN DEFAULT TRUE;
				
				-- ---------------------------------------------------------------------------
				-- Handler locale: pilota la creazione del nuovo record (se necessario)
				-- ---------------------------------------------------------------------------
				DECLARE CONTINUE HANDLER FOR NOT FOUND
					SET v_classe_trovata = FALSE;
					
				INSERT
				INTO
				debug_sp_elabora_divi 
				(data_log,
				source_id,
				hash_valore,
				punto)
			    VALUES
				(CURRENT_TIMESTAMP, v_bene_source_id, v_bene_hash, 'bene n/a');

				SELECT id
				INTO v_id_classe
				FROM lkp_tipologia_bene
				WHERE 
					LOWER(label) COLLATE utf8mb4_unicode_ci = LOWER(v_bene_classe) COLLATE utf8mb4_unicode_ci
				LIMIT 1;

				IF NOT v_classe_trovata THEN
					SELECT COALESCE(MAX(id), 0) + 1
					INTO v_nuovo_id
					FROM lkp_tipologia_bene;

				INSERT INTO lkp_tipologia_bene
				(id, label)
				VALUES
				(v_nuovo_id, v_bene_classe);

				SET v_id_classe = v_nuovo_id;
				
				END IF;

			END;
		END IF;

		-- ----------------------------------------------------------------
        -- Recupero tipo oppure creazione nuovo record
        -- ----------------------------------------------------------------
        SET v_id_tipo = NULL;
		
		IF v_bene_tipo IS NOT NULL THEN
			BEGIN
				DECLARE v_tipo_trovato BOOLEAN DEFAULT TRUE;

				-- ---------------------------------------------------------------------------
				-- Handler locale: pilota la creazione del nuovo record (se necessario)
				-- ---------------------------------------------------------------------------
				DECLARE CONTINUE HANDLER FOR NOT FOUND
					SET v_tipo_trovato = FALSE;

				SELECT id
				INTO v_id_tipo
				FROM lkp_categoria_bene
				WHERE 
				LOWER(label) COLLATE utf8mb4_unicode_ci = LOWER(v_bene_tipo) COLLATE utf8mb4_unicode_ci
				LIMIT 1;

				IF NOT v_tipo_trovato THEN
					SELECT COALESCE(MAX(id), 0) + 1
					INTO v_nuovo_id
					FROM lkp_categoria_bene;
					
					INSERT INTO lkp_categoria_bene
					(id, label)
					VALUES
					(v_nuovo_id, v_bene_tipo);

					SET v_id_tipo = v_nuovo_id;
				END IF;

			END;
		END IF;
		
		-- ----------------------------------------------------------------
        -- Ricerca della sigla della provincia
        -- ----------------------------------------------------------------
	 	IF v_loc_provincia IS NOT NULL THEN 
			BEGIN
				DECLARE v_provincia_trovata BOOLEAN DEFAULT TRUE; -- serve soltanto per mettere almeno un'istruzione nell'handler
				
				-- ---------------------------------------------------------------------------
				-- Handler locale: pilota la creazione del nuovo record (se necessario)
				-- ---------------------------------------------------------------------------
				DECLARE CONTINUE HANDLER FOR NOT FOUND
					SET v_provincia_trovata = FALSE;
					
				SELECT sigla
				INTO v_sigla_provincia
				FROM province
				WHERE 
				LOWER(nome) COLLATE utf8mb4_unicode_ci = LOWER(v_loc_provincia) COLLATE utf8mb4_unicode_ci;
				
				
			END;
		END IF; 
		
	

    END LOOP;



    -- ----------------------------------------------------------------
    -- Chiusura cursore
    -- ----------------------------------------------------------------

    CLOSE cur;


END$$

DELIMITER ;