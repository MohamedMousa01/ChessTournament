package com.prova.chess.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prova.chess.model.Stato;
import com.prova.chess.model.Utente;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UtenteDTO {

	private Long id;

	@NotBlank(message = "{nome.notblank}")
	private String nome;

	@NotBlank(message = "{cognome.notblank}")
	private String cognome;

	@NotBlank(message = "{username.notblank}")
	private String username;

	@NotBlank(message = "{password.notblank}")
	private String password; // Solitamente null in uscita per sicurezza

	private LocalDate dataRegistrazione;
	private Stato stato;

	@Min(value = 0, message = "L'ELO minimo non può essere negativo")
	private Integer eloRating;

	private Double montePremi;
	private Set<String> ruoli; // Usiamo i codici dei ruoli (es. ROLE_ADMIN)

	private Long torneoId;
	private String denominazioneTorneo;

	public UtenteDTO() {
	}

	public UtenteDTO(Long id, String nome, String cognome, String username, String password,
					 LocalDate dataRegistrazione, Stato stato, Integer eloRating, Double montePremi) {
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.dataRegistrazione = dataRegistrazione;
		this.stato = stato;
		this.eloRating = eloRating;
		this.montePremi = montePremi;


	}



	// --- Getters e Setters ---

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getCognome() { return cognome; }
	public void setCognome(String cognome) { this.cognome = cognome; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public LocalDate getDataRegistrazione() { return dataRegistrazione; }
	public void setDataRegistrazione(LocalDate dataRegistrazione) { this.dataRegistrazione = dataRegistrazione; }

	public Stato getStato() { return stato; }
	public void setStato(Stato stato) { this.stato = stato; }

	public Integer getEloRating() { return eloRating; }
	public void setEloRating(Integer eloRating) { this.eloRating = eloRating; }

	public Double getMontePremi() { return montePremi; }
	public void setMontePremi(Double montePremi) { this.montePremi = montePremi; }

	public Set<String> getRuoli() { return ruoli; }
	public void setRuoli(Set<String> ruoli) { this.ruoli = ruoli; }

	public Long getTorneoId() {return torneoId;}
	public void setTorneoId(Long torneoId) {this.torneoId = torneoId;}

	public String getDenominazioneTorneo() {
		return denominazioneTorneo;
	}

	public void setDenominazioneTorneo(String denominazioneTorneo) {
		this.denominazioneTorneo = denominazioneTorneo;
	}

	// --- Metodi di Conversione ---

	public static UtenteDTO buildUtenteDTOFromModel(Utente utenteModel) {
		UtenteDTO result = new UtenteDTO(
				utenteModel.getId(),
				utenteModel.getNome(),
				utenteModel.getCognome(),
				utenteModel.getUsername(),
				null, // Non inviamo mai la password nel DTO in uscita
				utenteModel.getDataRegistrazione(),
				utenteModel.getStato(),
				utenteModel.getEloRating(),
				utenteModel.getMontePremi()
		);

		if (utenteModel.getTorneo() != null) {
			result.setTorneoId(utenteModel.getTorneo().getId());
			result.setDenominazioneTorneo(utenteModel.getTorneo().getDenominazione());
		}

		if (utenteModel.getRuoli() != null) {
			result.setRuoli(utenteModel.getRuoli().stream()
					.map(ruolo -> ruolo.getCodice())
					.collect(Collectors.toSet()));
		}

		return result;
	}

	public Utente buildUtenteModel(boolean includeId) {
		Utente result = new Utente();
		if (includeId) {
			result.setId(this.id);
		}
		result.setNome(this.nome);
		result.setCognome(this.cognome);
		result.setUsername(this.username);
		result.setPassword(this.password);
		result.setDataRegistrazione(this.dataRegistrazione);
		result.setStato(this.stato);
		result.setEloRating(this.eloRating);
		result.setMontePremi(this.montePremi);
		// I ruoli e i tornei vengono solitamente gestiti nel Service
		// per evitare problemi di persistenza/fetch
		return result;
	}


	public Utente buildUtenteModelNoPass(boolean includeId) {
		Utente result = new Utente();
		if (includeId) {
			result.setId(this.id);
		}
		result.setNome(this.nome);
		result.setCognome(this.cognome);
		result.setUsername(this.username);
		result.setPassword(this.password);
		result.setDataRegistrazione(this.dataRegistrazione);
		result.setStato(this.stato);
		result.setEloRating(this.eloRating);
		result.setMontePremi(this.montePremi);
		// I ruoli e i tornei vengono solitamente gestiti nel Service
		// per evitare problemi di persistenza/fetch
		return result;
	}

}