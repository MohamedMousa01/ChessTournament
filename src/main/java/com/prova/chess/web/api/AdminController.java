package com.prova.chess.web.api;

import com.prova.chess.model.Utente;
import com.prova.chess.repository.utente.UtenteRepository;
import com.prova.chess.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin/utenti")
public class AdminController {

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    UtenteService utenteService;

    @GetMapping
    public ResponseEntity<List<Utente>> getAll(){
        List<Utente> utenti = utenteService.listAllUtenti();
        return ResponseEntity.ok(utenti);
    }



}
