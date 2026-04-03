package com.prova.chess.service;

import com.prova.chess.dto.ImportoDTO;
import com.prova.chess.dto.UtenteDTO;
import com.prova.chess.model.Stato;
import com.prova.chess.model.Torneo;
import com.prova.chess.model.Utente;
import com.prova.chess.repository.torneo.TorneoRepository;
import com.prova.chess.repository.utente.UtenteRepository;
import com.prova.chess.security.SecurityUtil;
import com.prova.chess.web.api.exception.ElementoNonTrovatoException;
import com.prova.chess.web.api.exception.TorneoCreationException;
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
	private TorneoRepository torneoRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SecurityUtil securityUtil;

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
	public Utente aggiorna(Utente utenteInstance) {
		// deve aggiornare solo nome, cognome, username, ruoli
		Utente utenteReloaded = repository.findById(utenteInstance.getId()).orElse(null);
		if (utenteReloaded == null)
			throw new RuntimeException("Elemento non trovato");
		utenteReloaded.setNome(utenteInstance.getNome());
		utenteReloaded.setCognome(utenteInstance.getCognome());
		utenteReloaded.setUsername(utenteInstance.getUsername());
		utenteReloaded.setRuoli(utenteInstance.getRuoli());
		return repository.save(utenteReloaded);
	}

	@Transactional
	public Utente inserisciNuovo(Utente utenteInstance) {
		utenteInstance.setStato(Stato.ATTIVO);
		utenteInstance.setPassword(passwordEncoder.encode(utenteInstance.getPassword()));
		utenteInstance.setDataRegistrazione(LocalDate.now());
		return repository.save(utenteInstance);
	}

	@Transactional
	public void rimuovi(Long idToRemove) {
		repository.deleteById(idToRemove);
	}

	public UtenteDTO disattiva(Long id){
		Utente utente = repository.findById(id).orElseThrow(() -> new RuntimeException("Utente con ID " + id + " non trovato"));

		utente.setStato(Stato.DISABILITATO);
		Utente utenteAggiornato = repository.save(utente);

		return UtenteDTO.buildUtenteDTOFromModel(utenteAggiornato);
	}

	public Utente findByUsername(String username) {
		return repository.findByUsername(username).orElse(null);
	}


	@Transactional
	public Utente ricarica(Double importo){
		//Long id = securityUtil.getCurrentUserId();

		//Utente playerAttuale = repository.findById(id).orElseThrow(() -> new TorneoCreationException("Utente loggato non trovato nel database."));

		String username = securityUtil.getCurrentUsername();
		Utente utente = repository.findByUsername(username).orElseThrow(() -> new TorneoCreationException("Utente loggato non trovato nel database."));

		Double montePremiBefore = utente.getMontePremi();
		utente.setMontePremi(montePremiBefore + importo);

		return utente;
	}


	@Transactional
	public Utente iscriviti(Long id){

		String username = securityUtil.getCurrentUsername();
		Utente utente = repository.findByUsername(username).orElseThrow(() -> new TorneoCreationException("Utente loggato non trovato nel database."));

		if(utente.getTorneo() != null)
			throw  new TorneoCreationException("L'utente è già iscritto ad un torneo, non si può iscrivere ad un altro");

		Torneo torneoIscrizione = torneoRepository.findById(id).orElseThrow(() -> new TorneoCreationException("Torneo non trovato nel database."));
		Double quota = torneoIscrizione.getQuotaIscrizione();

		if(utente.getMontePremi() < quota || utente.getEloRating() < torneoIscrizione.getEloMinimo())
			throw new ElementoNonTrovatoException("Errore nel controllo dati. Montepremi isufficente o Elo troppo basso!");

		if(torneoIscrizione.getStato() != Stato.ATTIVO)
			throw new ElementoNonTrovatoException("Non ti puoi iscrivere ad un torneo non attivo!");

		utente.setTorneo(torneoIscrizione);
		return utente;
	}



}
