package it.prova.dao.abbonato;

import java.time.LocalDate;
import java.util.List;

import it.prova.dao.IBaseDAO;
import it.prova.model.Abbonato;

public interface AbbonatoDAO extends IBaseDAO<Abbonato> {

	Abbonato findAbbonatoAttivoChePagaDiPiu(LocalDate dataRiferimento) throws Exception;

	int countAttiviInIntervallo(LocalDate dataInizio, LocalDate dataFine) throws Exception;

	List<Abbonato> listDistinctSubentratiUltimiSeiMesi(LocalDate dataRiferimento) throws Exception;

	List<Abbonato> listByCognomeOver60ConDisdettaDopo(String cognome, LocalDate dataLimite) throws Exception;

	List<Abbonato> listSituazioniAnomale() throws Exception;
}
