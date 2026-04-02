package com.prova.chess.service;

import com.prova.chess.dto.TorneoDTO;
import com.prova.chess.dto.UtenteDTO;
import com.prova.chess.model.Stato;
import com.prova.chess.model.Torneo;
import com.prova.chess.model.Utente;
import com.prova.chess.repository.torneo.TorneoRepository;
import com.prova.chess.repository.utente.UtenteRepository;
import com.prova.chess.security.SecurityUtil;
import com.prova.chess.security.dto.ResponseBusta;
import com.prova.chess.web.api.exception.ElementoNonTrovatoException;
import com.prova.chess.web.api.exception.TorneoCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class TorneoServiceImpl implements TorneoService {

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private SecurityUtil securityUtil;

    public List<Torneo> listAllElements(Utente utenteLoggato) {
        if(utenteLoggato == null)
            throw new RuntimeException("Errore nel recupero dell'utene Logatto");

        if(SecurityUtil.isOrganizer()){
            return torneoRepository.findByUtenteCreazione(utenteLoggato);
        }
        return (List<Torneo>) torneoRepository.findAll();
    }


    public Torneo caricaSingoloTorneo(Long id){
        return torneoRepository.findById(id).orElse(null);
    }


    public List<Torneo> trovaMatching(TorneoDTO torneoDTO){
        String denominazione = torneoDTO.getDenominazione();
        Integer eloMinimo = torneoDTO.getEloMinimo();
        return torneoRepository.findByDenominazioneAndElominimo(denominazione, eloMinimo);
    }

    @Transactional
    public Torneo inserisciNuovo(Torneo torneoInstance){

        Long id = securityUtil.getCurrentUserId();
        Utente utente = utenteRepository.findById(id).orElseThrow();
        if(utente.isPlayer())
            throw new TorneoCreationException("Non è possibile aggiungere il torneo, sei un player");

        torneoInstance.setUtenteCreazione(utente);
        torneoInstance.setDataCreazione(LocalDate.now());
        torneoInstance.setStato(Stato.ATTIVO);
        return torneoRepository.save(torneoInstance);
    }

    @Transactional
    public Torneo aggiorna(Torneo torneoInstance){

        Set<Utente> partecipanti = torneoInstance.getPartecipanti();
        if(!partecipanti.isEmpty())
            throw new TorneoCreationException("Torneo con partecipanti associati, impossibile fare la modifica");

        Torneo torneo = torneoRepository.findById(torneoInstance.getId()).orElse(null);
        if (torneo == null)
            throw new ElementoNonTrovatoException("Elemento non trovato");

        torneo.setDenominazione(torneoInstance.getDenominazione());
        torneo.setEloMinimo(torneoInstance.getEloMinimo());
        torneo.setQuotaIscrizione(torneoInstance.getQuotaIscrizione());
        torneo.setMaxGiocatori(torneoInstance.getMaxGiocatori());
        return torneoRepository.save(torneo);
    }

    @Transactional
    public TorneoDTO disattiva(Long id){
        Torneo torneo = torneoRepository.findById(id).orElseThrow(() -> new RuntimeException("Utente con ID " + id + " non trovato"));

        torneo.setStato(Stato.DISABILITATO);
        Torneo torneoAggiornato = torneoRepository.save(torneo);

        return TorneoDTO.buildTorneoDTOFromModel(torneoAggiornato, true);

    }


}
