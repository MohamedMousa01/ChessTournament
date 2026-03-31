package com.prova.chess.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utente")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "dataregistrazione")
    private LocalDate dataRegistrazione = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Stato stato;

    @Check(constraints = "elo_minimo >= 0")
    @Column(name = "elorating")
    private Integer eloRating = 0;

    @Column(name = "montepremi")
    private Double montePremi;

    @ManyToMany
    @JoinTable(name = "utente_ruolo", joinColumns = @JoinColumn(name = "utente_id", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "ruolo_id", referencedColumnName = "ID"))
    private Set<Ruolo> ruoli = new HashSet<>(0);


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id")
    private Torneo torneo;


    public Utente(){}

    public Utente(String nome, String cognome, String username, String password, LocalDate dataRegistrazione, Stato stato, Integer eloRating, Double montePremi, Set<Ruolo> ruoli) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.dataRegistrazione = dataRegistrazione;
        this.stato = stato;
        this.eloRating = eloRating;
        this.montePremi = montePremi;
        this.ruoli = ruoli;
    }

    public Utente(Long id, String nome, String cognome, String username, String password, LocalDate dataRegistrazione, Stato stato, Integer eloRating, Double montePremi, Set<Ruolo> ruoli, Torneo tornei) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.dataRegistrazione = dataRegistrazione;
        this.stato = stato;
        this.eloRating = eloRating;
        this.montePremi = montePremi;
        this.ruoli = ruoli;
        this.torneo = tornei;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(LocalDate dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public Stato getStato() {
        return stato;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

    public Integer getEloRating() {
        return eloRating;
    }

    public void setEloRating(Integer eloRating) {
        this.eloRating = eloRating;
    }

    public Double getMontePremi() {
        return montePremi;
    }

    public void setMontePremi(Double montePremi) {
        this.montePremi = montePremi;
    }

    public Set<Ruolo> getRuoli() {
        return ruoli;
    }

    public void setRuoli(Set<Ruolo> ruoli) {
        this.ruoli = ruoli;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public boolean isAdmin() {
        for (Ruolo ruoloItem : ruoli) {
            if (ruoloItem.getCodice().equals(Ruolo.ROLE_ADMIN))
                return true;
        }
        return false;
    }

    public boolean isAttivo() {
        return this.stato != null && this.stato.equals(Stato.ATTIVO);
    }

    public boolean isDisabilitato() {
        return this.stato != null && this.stato.equals(Stato.DISABILITATO);
    }


}
