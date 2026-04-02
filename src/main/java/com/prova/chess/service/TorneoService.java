package com.prova.chess.service;


import com.prova.chess.dto.TorneoDTO;
import com.prova.chess.model.Torneo;
import com.prova.chess.model.Utente;

import java.util.List;

public interface TorneoService {

    List<Torneo> listAllElements(Utente utenteLoggato);

    public Torneo caricaSingoloTorneo(Long id);

    public List<Torneo> trovaMatching(TorneoDTO torneoDTO);

    public Torneo inserisciNuovo(Torneo torneoInstance);

    public Torneo aggiorna(Torneo torneoInstance);

    public TorneoDTO disattiva(Long id);

}
