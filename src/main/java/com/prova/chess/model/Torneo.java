package com.prova.chess.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "torneo")
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "denominazione")
    private String denominazione;

    @Column(name = "datacreazione")
    private LocalDate dataCreazione = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Stato stato;

    @Column(name = "elominimo")
    private Integer eloMinimo;

    @Column(name = "quotaiscrizione")
    private Double quotaIscrizione;

    @Column(name = "maxgiocatori")
    private Integer maxGiocatori;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "torneo")
    private Set<Utente> partecipanti = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_creazione_id") // Nome della colonna nel DB
    private Utente utenteCreazione;

    public Torneo(){}

    public Torneo(String denominazione, LocalDate dataCreazione, Stato stato, Integer eloMinimo, Double quotaIscrizione, Integer maxGiocatori, Set<Utente> partecipanti) {
        this.denominazione = denominazione;
        this.dataCreazione = dataCreazione;
        this.stato = stato;
        this.eloMinimo = eloMinimo;
        this.quotaIscrizione = quotaIscrizione;
        this.maxGiocatori = maxGiocatori;
        this.partecipanti = partecipanti;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Stato getStato() {
        return stato;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

    public Integer getEloMinimo() {
        return eloMinimo;
    }

    public void setEloMinimo(Integer eloMinimo) {
        this.eloMinimo = eloMinimo;
    }

    public Double getQuotaIscrizione() {
        return quotaIscrizione;
    }

    public void setQuotaIscrizione(Double quotaIscrizione) {
        this.quotaIscrizione = quotaIscrizione;
    }

    public Integer getMaxGiocatori() {
        return maxGiocatori;
    }

    public void setMaxGiocatori(Integer maxGiocatori) {
        this.maxGiocatori = maxGiocatori;
    }

    public Set<Utente> getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(Set<Utente> partecipanti) {
        this.partecipanti = partecipanti;
    }

    public Utente getUtenteCreazione() {
        return utenteCreazione;
    }

    public void setUtenteCreazione(Utente utenteCreazione) {
        this.utenteCreazione = utenteCreazione;
    }
}
