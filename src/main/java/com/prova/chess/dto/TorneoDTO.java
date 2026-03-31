package com.prova.chess.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prova.chess.model.Stato;
import com.prova.chess.model.Torneo;
import com.prova.chess.model.Utente;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TorneoDTO {

    private Long id;

    @NotBlank(message = "{denominazione.notblank}")
    private String denominazione;

    private LocalDate dataCreazione;
    private Stato stato;

    @Min(value = 0, message = "L'ELO minimo non può essere negativo")
    private Integer eloMinimo;

    @Min(value = 0, message = "La quota di iscrizione non può essere negativo")
    private Double quotaIscrizione;

    @Min(value = 2, message = "Il numero minimo di giocatori non può essere negativo")
    private Integer maxGiocatori;

    private Utente utenteCreazione;

    private Set<UtenteDTO> partecipanti;

    public TorneoDTO() {
    }

    public TorneoDTO(Long id, String denominazione, LocalDate dataCreazione, Stato stato,
                     Integer eloMinimo, Double quotaIscrizione, Integer maxGiocatori, Utente utenteCreazione) {
        this.id = id;
        this.denominazione = denominazione;
        this.dataCreazione = dataCreazione;
        this.stato = stato;
        this.eloMinimo = eloMinimo;
        this.quotaIscrizione = quotaIscrizione;
        this.maxGiocatori = maxGiocatori;
        this.utenteCreazione = utenteCreazione;
    }

    // --- Getters e Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDenominazione() { return denominazione; }
    public void setDenominazione(String denominazione) { this.denominazione = denominazione; }

    public LocalDate getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDate dataCreazione) { this.dataCreazione = dataCreazione; }

    public Stato getStato() { return stato; }
    public void setStato(Stato stato) { this.stato = stato; }

    public Integer getEloMinimo() { return eloMinimo; }
    public void setEloMinimo(Integer eloMinimo) { this.eloMinimo = eloMinimo; }

    public Double getQuotaIscrizione() { return quotaIscrizione; }
    public void setQuotaIscrizione(Double quotaIscrizione) { this.quotaIscrizione = quotaIscrizione; }

    public Integer getMaxGiocatori() { return maxGiocatori; }
    public void setMaxGiocatori(Integer maxGiocatori) { this.maxGiocatori = maxGiocatori; }

    public Utente getUtenteCreazioneId() { return utenteCreazione; }
    public void setUtenteCreazioneId(Utente utenteCreazione) { this.utenteCreazione = utenteCreazione; }

    public Set<UtenteDTO> getPartecipanti() { return partecipanti; }
    public void setPartecipanti(Set<UtenteDTO> partecipanti) { this.partecipanti = partecipanti; }

    // --- Metodi di Conversione ---

    public static TorneoDTO buildTorneoDTOFromModel(Torneo torneoModel, boolean includePartecipanti) {
        TorneoDTO result = new TorneoDTO(
                torneoModel.getId(),
                torneoModel.getDenominazione(),
                torneoModel.getDataCreazione(),
                torneoModel.getStato(),
                torneoModel.getEloMinimo(),
                torneoModel.getQuotaIscrizione(),
                torneoModel.getMaxGiocatori(),
                torneoModel.getUtenteCreazione()
        );

        if (includePartecipanti && torneoModel.getPartecipanti() != null) {
            result.setPartecipanti(torneoModel.getPartecipanti().stream()
                    .map(UtenteDTO::buildUtenteDTOFromModel)
                    .collect(Collectors.toSet()));
        }

        return result;
    }

    public Torneo buildTorneoModel() {
        Torneo result = new Torneo();
        result.setId(this.id);
        result.setDenominazione(this.denominazione);
        result.setDataCreazione(this.dataCreazione);
        result.setStato(this.stato);
        result.setEloMinimo(this.eloMinimo);
        result.setQuotaIscrizione(this.quotaIscrizione);
        result.setMaxGiocatori(this.maxGiocatori);
        result.setUtenteCreazione(this.utenteCreazione);
        // I partecipanti vengono solitamente gestiti tramite logica di business nel Service
        return result;
    }
}