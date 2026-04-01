package com.prova.chess.web.api;


import com.prova.chess.dto.TorneoDTO;
import com.prova.chess.model.Torneo;
import com.prova.chess.model.Utente;
import com.prova.chess.repository.utente.UtenteRepository;
import com.prova.chess.service.TorneoService;
import com.prova.chess.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/tornei")
public class TorneoController {

    @Autowired
    TorneoService torneoService;

    @Autowired
    UtenteRepository utenteRepository;

    @GetMapping
    public ResponseEntity<List<Torneo>> getAll(Authentication authentication){

        String username = authentication.getName();

        Utente utenteCorrente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato nel database"));

        List<Torneo> tornei = torneoService.listAllElements(utenteCorrente);
        return ResponseEntity.ok(tornei);
    }




}
