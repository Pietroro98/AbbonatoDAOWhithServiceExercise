package it.prova.service.abbonato;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.dao.Constants;
import it.prova.dao.abbonato.AbbonatoDAO;
import it.prova.model.Abbonato;

public class AbbonatoServiceImpl implements AbbonatoService {

	private AbbonatoDAO abbonatoDAO;

	public void setAbbonatoDao(AbbonatoDAO abbonatoDAO) {
		this.abbonatoDAO = abbonatoDAO;
	}

	@Override
	public List<Abbonato> listAll() throws Exception {
		List<Abbonato> result = new ArrayList<>();
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {

			// inietto la connection nel dao
			abbonatoDAO.setConnection(connection);

			// eseguo quello che realmente devo fare
			result = abbonatoDAO.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Abbonato findById(Long idInput) throws Exception {
		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Abbonato result = null;
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {

			// inietto la connection nel dao
			abbonatoDAO.setConnection(connection);

			// eseguo quello che realmente devo fare
			result = abbonatoDAO.get(idInput);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int aggiorna(Abbonato input) throws Exception {
		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {

			// inietto la connection nel dao
			abbonatoDAO.setConnection(connection);

			// eseguo quello che realmente devo fare
			result = abbonatoDAO.update(input);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int inserisciNuovo(Abbonato input) throws Exception {
		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {

			// inietto la connection nel dao
			abbonatoDAO.setConnection(connection);

			// eseguo quello che realmente devo fare
			result = abbonatoDAO.insert(input);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int rimuovi(Long idDaRimuovere) throws Exception {
		if (idDaRimuovere == null || idDaRimuovere < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {

			// inietto la connection nel dao
			abbonatoDAO.setConnection(connection);

			// eseguo quello che realmente devo fare
			result = abbonatoDAO.delete(idDaRimuovere);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

}
