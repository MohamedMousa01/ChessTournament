package com.prova.chess.service;

import com.prova.chess.model.Stato;
import com.prova.chess.model.Utente;
import com.prova.chess.repository.utente.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Utente> listAllUtenti() {
		return (List<Utente>) repository.findAll();
	}

	public Utente caricaSingoloUtente(Long id) {
		return repository.findById(id).orElse(null);
	}

	public Utente caricaSingoloUtenteConRuoli(Long id) {
		return repository.findByIdConRuoli(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Utente utenteInstance) {
		// deve aggiornare solo nome, cognome, username, ruoli
		Utente utenteReloaded = repository.findById(utenteInstance.getId()).orElse(null);
		if (utenteReloaded == null)
			throw new RuntimeException("Elemento non trovato");
		utenteReloaded.setNome(utenteInstance.getNome());
		utenteReloaded.setCognome(utenteInstance.getCognome());
		utenteReloaded.setUsername(utenteInstance.getUsername());
		utenteReloaded.setRuoli(utenteInstance.getRuoli());
		repository.save(utenteReloaded);
	}

	@Transactional
	public void inserisciNuovo(Utente utenteInstance) {
		utenteInstance.setStato(Stato.ATTIVO);
		utenteInstance.setPassword(passwordEncoder.encode(utenteInstance.getPassword()));
		utenteInstance.setDataRegistrazione(LocalDate.now());
		repository.save(utenteInstance);
	}

	@Transactional
	public void rimuovi(Long idToRemove) {
		repository.deleteById(idToRemove);
		;
	}

	public List<Utente> findByExample(Utente example) {
		// TODO Da implementare
		return listAllUtenti();
	}

	public Utente eseguiAccesso(String username, String password) {
		return repository.findByUsernameAndPasswordAndStato(username, password, Stato.ATTIVO);
	}

	public Utente findByUsernameAndPassword(String username, String password) {
		return repository.findByUsernameAndPassword(username, password);
	}

//	@Transactional
//	public void changeUserAbilitation(Long utenteInstanceId) {
//		Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
//		if (utenteInstance == null)
//			throw new RuntimeException("Elemento non trovato.");
//
//		if (utenteInstance.getStato() == null || utenteInstance.getStato().equals(StatoUtente.CREATO))
//			utenteInstance.setStato(StatoUtente.ATTIVO);
//		else if (utenteInstance.getStato().equals(StatoUtente.ATTIVO))
//			utenteInstance.setStato(StatoUtente.DISABILITATO);
//		else if (utenteInstance.getStato().equals(StatoUtente.DISABILITATO))
//			utenteInstance.setStato(StatoUtente.ATTIVO);
//	}

	public Utente findByUsername(String username) {
		return repository.findByUsername(username).orElse(null);
	}

}
