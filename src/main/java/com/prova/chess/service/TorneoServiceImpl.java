package com.prova.chess.service;

import com.prova.chess.model.Torneo;
import com.prova.chess.model.Utente;
import com.prova.chess.repository.torneo.TorneoRepository;
import com.prova.chess.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TorneoServiceImpl implements TorneoService {

    @Autowired
    private TorneoRepository torneoRepository;

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




}
