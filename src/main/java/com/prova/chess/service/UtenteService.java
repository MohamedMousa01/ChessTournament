package com.prova.chess.service;

import com.prova.chess.dto.ImportoDTO;
import com.prova.chess.dto.UtenteDTO;
import com.prova.chess.model.Utente;

import java.util.List;

public interface UtenteService {

	public List<Utente> listAllUtenti();

	public Utente caricaSingoloUtente(Long id);

	public Utente caricaSingoloUtenteConRuoli(Long id);

	public Utente aggiorna(Utente utenteInstance);

	public Utente inserisciNuovo(Utente utenteInstance);

	public void rimuovi(Long idToRemove);

	public UtenteDTO disattiva(Long id);


	public Utente findByUsername(String username);

	public Utente ricarica(Double importo);


	public Utente iscriviti(Long id);
}
