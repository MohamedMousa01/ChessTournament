package com.prova.chess.service;


import com.prova.chess.model.Torneo;
import com.prova.chess.model.Utente;

import java.util.List;

public interface TorneoService {

    List<Torneo> listAllElements(Utente utenteLoggato);

}
