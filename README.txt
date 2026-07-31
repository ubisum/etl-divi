-----------------------------------------------------
-- STATO DELL'ARTE ----------------------------------
-----------------------------------------------------

Sono gia' state definitite le seguenti funzionalità:
1. Inserimento in application property degli attributi necessari al funzioanmento dell'applicazione, tra cui:
	- attributi di connessione al database
	- parte fissa dell'URL per i servizi DIVI e token di autorizzazione
	- numero di elementi richiesti ad ogni invocazione dei servizi DIVI
	- attributo di frequenza per l'attivazione dello scheduler
	- modalità di ricerca dei dati DIVI (può assumere i valori rest o ftp)
	- modalità di acquisizione del JSON (se impostato a true, legge i dati da JSON interno al progetto).
      Di default è impostato a true per il debug. All'atto del deploy, l'attributo va messo a false o eliminato totalmente.
2. Creazione dei DTO utili per il mapping del JSON in oggetti Java
3. Definizione della funzione di connessione al servizio DIVI
4. Definizione del service e dei DAO per la creazione ed il popolamento della tabella di staging, per il salvataggio delle informazioni relative al batch associato alla chiamata a DIVI

-----------------------------------------------------
-- TO DO LIST ---------------------------------------
-----------------------------------------------------

1. È necessario completare e testare la stored procedure sp_elabora_divi_staging, presente nella cartella resources/sql del progetto.
   Va aggiunta tutta la parte per l'aggiornamento delle tabelle:
		- dei dati catastali
		- dei beni
		- dei provvedimenti
		- degli indirizzi
		- degli enti
		- di correlazione tra i beni e le altre cartelle
    Attualmente è presente solo la parte di inserimento (se richiesto) dei dati su tipo/categoria bene e provincia. Questa parte dev'essere oggetto di test.
2. Se richiesto, va implementato il flusso (a partire dallo scheduler) che le legge le informazioni da cartella FTP
	