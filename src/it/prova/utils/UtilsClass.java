package it.prova.utils;

import it.prova.model.Abbonato;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UtilsClass {
    /**
     * Metodo che imposta un parametro date su ps,
     * convertendo un LocalDate oppure scrivendo null se il valore è assente.
     * @param ps
     * @param parameterIndex
     * @param value
     * @throws Exception
     */
    public static void setNullableLocalDate(PreparedStatement ps, int parameterIndex, LocalDate value) throws Exception {
        if (value == null) {
            ps.setNull(parameterIndex, Types.DATE);
            return;
        }
        ps.setDate(parameterIndex, Date.valueOf(value));
    }

    /**
     * Crea un Abbonato convertendo in modo dinamico le date da stringa.
     * Date vuote/null diventano null. Sono accettati anche "now" e "today".
     */
    public static Abbonato creaAbbonato(String nome, String cognome, Integer importoMensile,
                                        String dataDiNascita, String dataStipula, String dataCessazione,
                                        String patternDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                patternDate == null || patternDate.trim().isEmpty() ? "dd-MM-yyyy" : patternDate);

        return new Abbonato(
                nome,
                cognome,
                importoMensile,
                parseDateDinamica(dataDiNascita, formatter),
                parseDateDinamica(dataStipula, formatter),
                parseDateDinamica(dataCessazione, formatter)
        );
    }

    private static LocalDate parseDateDinamica(String input, DateTimeFormatter formatter) {
        if (input == null || input.trim().isEmpty())
            return null;

        String normalized = input.trim();
        if ("now".equalsIgnoreCase(normalized) || "today".equalsIgnoreCase(normalized))
            return LocalDate.now();

        return LocalDate.parse(normalized, formatter);
    }


    public static Abbonato buildAbbonatoFromResultSet(ResultSet rs) throws Exception {
        Abbonato result = new Abbonato();
        result.setNome(rs.getString("nome"));
        result.setCognome(rs.getString("cognome"));
        result.setImportoMensile(rs.getInt("importo_mensile"));
        result.setDataDiNascita(rs.getDate("data_di_nascita") 	!= null ? rs.getDate("data_di_nascita").toLocalDate() 	: null);
        result.setDataStipula(rs.getDate("data_stipula") 		!= null ? rs.getDate("data_stipula").toLocalDate() 		: null);
        result.setDataCessazione(rs.getDate("data_cessazione") 	!= null ? rs.getDate("data_cessazione").toLocalDate() 	: null);
        result.setId(rs.getLong("id"));
        return result;
    }
}
