DROP PROCEDURE IF EXISTS sp_test_cursore_hash;

DELIMITER $$

CREATE PROCEDURE sp_test_cursore_hash()
BEGIN

    DECLARE done BOOLEAN DEFAULT FALSE;

    DECLARE v_source_id INT;
    DECLARE v_hash VARCHAR(64);

    DECLARE cur CURSOR FOR
        SELECT
            bene_source_id,
            bene_hash
        FROM DIVI_STAGING;

    DECLARE CONTINUE HANDLER FOR NOT FOUND
        SET done = TRUE;


    OPEN cur;

    read_loop: LOOP

        FETCH cur INTO
            v_source_id,
            v_hash;

        IF done THEN
            LEAVE read_loop;
        END IF;

        INSERT INTO debug_sp_elabora_divi
        (
            source_id,
            hash_valore,
            punto
        )
        VALUES
        (
            v_source_id,
            v_hash,
            'procedura test'
        );

    END LOOP;

    CLOSE cur;

END$$

DELIMITER ;