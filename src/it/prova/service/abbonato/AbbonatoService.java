package it.prova.service.abbonato;

import java.time.LocalDate;
import java.util.List;

import it.prova.dao.abbonato.AbbonatoDAO;
import it.prova.model.Abbonato;

public interface AbbonatoService {

	// questo mi serve per injection
	public void setAbbonatoDao(AbbonatoDAO abbonatoDAO);

	public List<Abbonato> listAll() throws Exception;

	public Abbonato findById(Long idInput) throws Exception;

	public int aggiorna(Abbonato input) throws Exception;

	public int inserisciNuovo(Abbonato input) throws Exception;

	public int rimuovi(Long idDaRimuovere) throws Exception;

	public Abbonato cercaAttivoChePagaDiPiu(LocalDate dataRiferimento) throws Exception;

	public int contaAttiviInIntervallo(LocalDate dataInizio, LocalDate dataFine) throws Exception;

	public List<Abbonato> elencoDistintoSubentratiUltimiSeiMesi(LocalDate dataRiferimento) throws Exception;

	public List<Abbonato> elencoByCognomeOverEtaConDisdettaDopoAnno(String cognome, Integer etaMinima, Integer annoLimite) throws Exception;

	public List<Abbonato> elencoSituazioniAnomale() throws Exception;

}
