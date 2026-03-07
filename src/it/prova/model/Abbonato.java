package it.prova.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Abbonato {
	private Long id;
	private String nome;
	private String cognome;
	private Integer importoMensile;
	private LocalDate dataDiNascita;
	private LocalDate dataStipula;
	private LocalDate dataCessazione;

	public Abbonato() {
	}

	public Abbonato(String nome, String cognome, Integer importoMensile, LocalDate dataDiNascita, LocalDate dataStipula, LocalDate dataCessazione) {
		this.nome 				= nome;
		this.cognome 			= cognome;
		this.importoMensile 	= importoMensile;
		this.dataDiNascita 		= dataDiNascita;
		this.dataStipula 		= dataStipula;
		this.dataCessazione 	= dataCessazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Integer getImportoMensile() {
		return importoMensile;
	}

	public void setImportoMensile(Integer importoMensile) {
		this.importoMensile = importoMensile;
	}

	public LocalDate getDataDiNascita() {
		return dataDiNascita;
	}

	public void setDataDiNascita(LocalDate dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}

	public LocalDate getDataStipula() {
		return dataStipula;
	}

	public void setDataStipula(LocalDate dataStipula) {
		this.dataStipula = dataStipula;
	}

	public LocalDate getDataCessazione() {
		return dataCessazione;
	}

	public void setDataCessazione(LocalDate dataCessazione) {
		this.dataCessazione = dataCessazione;
	}

	@Override
	public String toString()
	{
		String dataDiNascitaString  = dataDiNascita  != null ? DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataDiNascita)  : "N.D.";
		String dataStipulaString 	= dataStipula 	 != null ? DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataStipula)    : "N.D.";
		String dataCessazioneString = dataCessazione != null ? DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataCessazione) : "N.D.";

		return "Abbonato [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", importoMensile="
				+ importoMensile + ", dataDiNascita=" + dataDiNascitaString + ", dataStipula=" + dataStipulaString
				+ ", dataCessazione=" + dataCessazioneString + "]";
	}

}
