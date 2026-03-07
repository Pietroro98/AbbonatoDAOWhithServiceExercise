package it.prova.dao.abbonato;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.dao.AbstractMySQLDAO;
import it.prova.model.Abbonato;

import static it.prova.utils.UtilsClass.setNullableLocalDate;

public class AbbonatoDAOImpl extends AbstractMySQLDAO implements AbbonatoDAO {

	@Override
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Abbonato> list() throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Abbonato> result = new ArrayList<Abbonato>();

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from abbonato")) {

			while (rs.next()) {
				Abbonato abbonatoTemp = new Abbonato();
				abbonatoTemp.setNome(rs.getString("nome"));
				abbonatoTemp.setCognome(rs.getString("cognome"));
				abbonatoTemp.setImportoMensile(rs.getInt("importo_mensile"));
				abbonatoTemp.setDataDiNascita(rs.getDate("data_di_nascita")  != null ? rs.getDate("data_di_nascita").toLocalDate() : null);
				abbonatoTemp.setDataStipula(rs.getDate("data_stipula") 		 != null ? rs.getDate("data_stipula").toLocalDate()    : null);
				abbonatoTemp.setDataCessazione(rs.getDate("data_cessazione") != null ? rs.getDate("data_cessazione").toLocalDate() : null);
				abbonatoTemp.setId(rs.getLong("id"));

				result.add(abbonatoTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Abbonato get(Long idInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Abbonato result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from abbonato where id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
				{
					result = new Abbonato();
					result.setNome(rs.getString("nome"));
					result.setCognome(rs.getString("cognome"));
					result.setImportoMensile(rs.getInt("importo_mensile"));
					result.setDataDiNascita(rs.getDate("data_di_nascita") 	!= null ? rs.getDate("data_di_nascita").toLocalDate() 	: null);
					result.setDataStipula(rs.getDate("data_stipula") 		!= null ? rs.getDate("data_stipula").toLocalDate() 		: null);
					result.setDataCessazione(rs.getDate("data_cessazione") 	!= null ? rs.getDate("data_cessazione").toLocalDate() 	: null);

					result.setId(rs.getLong("id"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Abbonato abbonatoInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (abbonatoInput == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"insert into abbonato(nome, cognome, importo_mensile, data_di_nascita,data_stipula, data_cessazione) values(?,?,?,?,?,?);")) {
			ps.setString(1, abbonatoInput.getNome());
			ps.setString(2, abbonatoInput.getCognome());
			ps.setInt(	 3, abbonatoInput.getImportoMensile());
			setNullableLocalDate(ps, 4, abbonatoInput.getDataDiNascita());
			setNullableLocalDate(ps, 5, abbonatoInput.getDataStipula());
			setNullableLocalDate(ps, 6, abbonatoInput.getDataCessazione());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Abbonato abbonatoInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (abbonatoInput == null || abbonatoInput.getId() == null || abbonatoInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
					"UPDATE abbonato SET nome=?,cognome=?, importo_mensile=?, data_di_nascita=?,data_stipula=?, data_cessazione=? where id=?;")) {
			ps.setString(1, abbonatoInput.getNome());
			ps.setString(2, abbonatoInput.getCognome());
			ps.setInt(	 3, abbonatoInput.getImportoMensile());
			setNullableLocalDate(ps, 4, abbonatoInput.getDataDiNascita());
			setNullableLocalDate(ps, 5, abbonatoInput.getDataStipula());
			setNullableLocalDate(ps, 6, abbonatoInput.getDataCessazione());
			ps.setLong(	 7, abbonatoInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Long idDaRimuovere) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idDaRimuovere == null || idDaRimuovere < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("delete from abbonato where id=?")) {
			ps.setLong(1, idDaRimuovere);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
}
