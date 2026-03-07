package it.prova.dao.abbonato;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.prova.dao.AbstractMySQLDAO;
import it.prova.model.Abbonato;
import it.prova.utils.UtilsClass;

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
					result = UtilsClass.buildAbbonatoFromResultSet(rs);
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

	@Override
	public Abbonato findAbbonatoAttivoChePagaDiPiu(LocalDate dataRiferimento) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		if (dataRiferimento == null)
			throw new Exception("Valore di input non ammesso.");

		Abbonato result = null;
		try (PreparedStatement ps = connection.prepareStatement(
				"select * from abbonato where data_stipula <= ? and (data_cessazione is null or data_cessazione >= ?) order by importo_mensile desc, id desc limit 1")) {
			ps.setDate(1, Date.valueOf(dataRiferimento));
			ps.setDate(2, Date.valueOf(dataRiferimento));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					result = UtilsClass.buildAbbonatoFromResultSet(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int countAttiviInIntervallo(LocalDate dataInizio, LocalDate dataFine) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		if (dataInizio == null || dataFine == null || dataInizio.isAfter(dataFine))
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"select count(*) from abbonato where data_stipula <= ? and (data_cessazione is null or data_cessazione >= ?)")) {
			ps.setDate(1, Date.valueOf(dataFine));
			ps.setDate(2, Date.valueOf(dataInizio));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Abbonato> listDistinctSubentratiUltimiSeiMesi(LocalDate dataRiferimento) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		if (dataRiferimento == null)
			throw new Exception("Valore di input non ammesso.");

		List<Abbonato> result = new ArrayList<>();
		LocalDate seiMesiFa = dataRiferimento.minusMonths(6);

		try (PreparedStatement ps = connection.prepareStatement(
				"select a.* from abbonato a inner join (select nome, cognome, data_di_nascita, max(data_stipula) as max_stipula from abbonato where data_stipula between ? and ? group by nome, cognome, data_di_nascita) b on a.nome=b.nome and a.cognome=b.cognome and a.data_di_nascita=b.data_di_nascita and a.data_stipula=b.max_stipula order by a.cognome, a.nome, a.id desc")) {
			ps.setDate(1, Date.valueOf(seiMesiFa));
			ps.setDate(2, Date.valueOf(dataRiferimento));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					result.add(UtilsClass.buildAbbonatoFromResultSet(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return result;
	}

	@Override
	public List<Abbonato> listByCognomeOver60ConDisdettaDopo(String cognome, LocalDate dataLimite) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		if (cognome == null || cognome.trim().isEmpty() || dataLimite == null)
			throw new Exception("Valore di input non ammesso.");

		List<Abbonato> result = new ArrayList<>();
		LocalDate sogliaOver60 = LocalDate.now().minusYears(60);

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from abbonato where cognome=? and data_di_nascita is not null and data_di_nascita <= ? and data_cessazione is not null and data_cessazione > ? order by data_cessazione desc, id desc")) {
			ps.setString(1, cognome.trim());
			ps.setDate(2, Date.valueOf(sogliaOver60));
			ps.setDate(3, Date.valueOf(dataLimite));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					result.add(UtilsClass.buildAbbonatoFromResultSet(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return result;
	}

	@Override
	public List<Abbonato> listSituazioniAnomale() throws Exception {
		return null;
	}

}
