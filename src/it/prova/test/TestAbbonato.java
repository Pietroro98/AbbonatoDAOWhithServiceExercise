package it.prova.test;

import it.prova.model.Abbonato;
import it.prova.service.MyServiceFactory;
import it.prova.service.abbonato.AbbonatoService;
import it.prova.utils.UtilsClass;

import java.time.LocalDate;
import java.util.List;

public class TestAbbonato {

	public static void main(String[] args) {

		// parlo direttamente con il service
		AbbonatoService abbonatoService = MyServiceFactory.getAbbonatoServiceImpl();

		try
		{
			// testInserimentoNuovoAbbonato(abbonatoService);
			System.out.println("In tabella ci sono " + abbonatoService.listAll().size() + " elementi.");

			// System.out.println("Test update abbonato");
			// testUpdateAbbonato(abbonatoService);

			// testRimozioneAbbonato(abbonatoService);
			System.out.println("In tabella ci sono " + abbonatoService.listAll().size() + " elementi.");

			// System.out.println("TEST ATTIVO CHE PAGA DI PIU");
			// testAttivoChePagaDiPiu(abbonatoService);

			// System.out.println("TEST CONTEGGIO ATTIVI IN INTEVALLO DI DATE");
			 // testContaAttiviInIntervallo(abbonatoService);

			// System.out.println("TEST SUBENTRATI IN ULTIMI MESI");
			// testSubentratiUltimiSeiMesi(abbonatoService);

			// System.out.println("TEST di abbonati con un certo cognome, over60 che hanno disdetto dopo il 2020");
			// testByCognomeOver60DisdettiDopo2020(abbonatoService);

			System.out.println("In tabella ci sono " + abbonatoService.listAll().size() + " elementi.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void testInserimentoNuovoAbbonato(AbbonatoService abbonatoService) throws Exception
	{
		System.out.println(".......testInserimentoNuovoAbbonato inizio.............");
		Abbonato newAbbonatoInstance = UtilsClass.creaAbbonato("Pietro","Romano",300,"10-06-1998","03-06-2026","03-06-2026",null);
		if (abbonatoService.inserisciNuovo(newAbbonatoInstance) != 1)
			throw new RuntimeException("testInserimentoNuovoAbbonato FAILED ");

		System.out.println(".......testInserimentoNuovoAbbonato PASSED.............");
	}

	private static void testUpdateAbbonato(AbbonatoService abbonatoService) throws Exception {
		System.out.println(".......testUpdateAbbonato inizio.............");

		// inserisco i dati che poi modifico
		if (abbonatoService.inserisciNuovo(
				UtilsClass.creaAbbonato(
						      "Matteo",
						    "Gialli",
						300,
						 "11-08-2009",
						   "11-08-2030",
						"03-06-2026",null)) != 1)
			throw new RuntimeException("testUpdateAbbonato: inserimento preliminare FAILED");

		List<Abbonato> recuperoUltimoInserito = abbonatoService.listAll();
		if (recuperoUltimoInserito == null || recuperoUltimoInserito.isEmpty())
			throw new RuntimeException("testUpdateAbbonato: nessun record disponibile dopo inserimento");

		// mi metto da parte l'id su cui lavorare per il test
		Long idMatteo = null;
		idMatteo = recuperoUltimoInserito.getLast().getId();
		if (idMatteo == null)
			throw new RuntimeException("testUpdateAbbonato: impossibile individuare id record inserito");

		String nuovoCognome = "Vivaldi";
		Abbonato toBeUpdated = abbonatoService.findById(idMatteo);
		if (toBeUpdated == null)
			throw new RuntimeException("testUpdateAbbonato: record da aggiornare non trovato");

		toBeUpdated.setCognome(nuovoCognome);
		if (abbonatoService.aggiorna(toBeUpdated) != 1)
			throw new RuntimeException("testUpdateAbbonato FAILED ");

		System.out.println(".......testUpdateAbbonato PASSED.............");
	}

	private static void testRimozioneAbbonato(AbbonatoService abbonatoService) throws Exception {
		System.out.println(".......testRimozioneAbbonato inizio.............");

		//creo un nuovo abbonato che poi andro a rimuovere per la prova
		if (abbonatoService.inserisciNuovo(
				UtilsClass.creaAbbonato(
						"Nicola",
						"Neri",
						40,
						"11-10-2002",
						"11-10-2030",
						"03-06-2026",null)) != 1)
			throw new RuntimeException("testUpdateAbbonato: inserimento preliminare FAILED");

		List<Abbonato> interoContenutoTabella = abbonatoService.listAll();
		if (interoContenutoTabella.isEmpty() || interoContenutoTabella.get(0) == null)
			throw new Exception("Non ho nulla da rimuovere");

		Long idNicola = interoContenutoTabella.getLast().getId();
		// ricarico per sicurezza con l'id individuato
		Abbonato toBeRemoved = abbonatoService.findById(idNicola);
		if (abbonatoService.rimuovi(toBeRemoved.getId()) != 1)
			throw new RuntimeException("testRimozioneAbbonato FAILED ");

		System.out.println(".......testRimozioneAbbonato PASSED.............");
	}

	private static void testAttivoChePagaDiPiu(AbbonatoService abbonatoService) throws Exception {
		System.out.println(".......testAttivoChePagaDiPiu inizio.............");
		Abbonato result = abbonatoService.cercaAttivoChePagaDiPiu(LocalDate.now());
		if (result == null)
			throw new RuntimeException("testAttivoChePagaDiPiu FAILED: nessun abbonato attivo trovato");
		System.out.println("Top attivo: " + result);
		System.out.println(".......testAttivoChePagaDiPiu PASSED.............");
	}

	private static void testContaAttiviInIntervallo(AbbonatoService abbonatoService) throws Exception {
		System.out.println(".......testContaAttiviInIntervallo inizio.............");
		LocalDate inizio = LocalDate.now().minusMonths(1);
		LocalDate fine = LocalDate.now().plusMonths(1);
		int count = abbonatoService.contaAttiviInIntervallo(inizio, fine);
		if (count < 0)
			throw new RuntimeException("testContaAttiviInIntervallo FAILED: count negativo");
		System.out.println("Conteggio abbonati attivi tra " + inizio + " e " + fine + " sono in totale: " + count);
		System.out.println(".......testContaAttiviInIntervallo PASSED.............");
	}

	private static void testSubentratiUltimiSeiMesi(AbbonatoService abbonatoService) throws Exception {
		System.out.println(".......testSubentratiUltimiSeiMesi inizio.............");
		List<Abbonato> result = abbonatoService.elencoDistintoSubentratiUltimiSeiMesi(LocalDate.now());
		if (result == null)
			throw new RuntimeException("testSubentratiUltimiSeiMesi FAILED: lista null");
		System.out.println("Subentrati ultimi 6 mesi: " + result.size());
		System.out.println("Lista: " + result);
		System.out.println(".......testSubentratiUltimiSeiMesi PASSED.............");
	}

	private static void testByCognomeOver60DisdettiDopo2020(AbbonatoService abbonatoService) throws Exception {
		System.out.println(".......testByCognomeOver60DisdettiDopo2020 inizio.............");
		List<Abbonato> result = abbonatoService.elencoByCognomeOver60ConDisdettaDopo("Rossi", LocalDate.of(2020, 12, 31));
		if (result == null)
			throw new RuntimeException("testByCognomeOver60DisdettiDopo2020 FAILED: lista null");
		System.out.println("Trovati: " + result.size());
		System.out.println(".......testByCognomeOver60DisdettiDopo2020 PASSED.............");
	}
}
