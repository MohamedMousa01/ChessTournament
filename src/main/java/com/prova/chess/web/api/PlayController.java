package com.prova.chess.web.api;

import com.prova.chess.dto.ImportoDTO;
import com.prova.chess.dto.TorneoDTO;
import com.prova.chess.dto.UtenteDTO;
import com.prova.chess.model.Torneo;
import com.prova.chess.model.Utente;
import com.prova.chess.security.SecurityUtil;
import com.prova.chess.security.dto.ResponseBusta;
import com.prova.chess.service.TorneoService;
import com.prova.chess.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/play")
public class PlayController {

    @Autowired
    UtenteService utenteService;
    @Autowired
    TorneoService torneoService;
    @Autowired
    SecurityUtil securityUtil;


    @PostMapping("/ricarica")
    public ResponseEntity<ResponseBusta<UtenteDTO>> ricaricaMontepremi(@RequestBody ImportoDTO importo){

        Utente utente = utenteService.ricarica(importo.getImporto());
        UtenteDTO player = UtenteDTO.buildUtenteDTOFromModel(utente);

        return ResponseEntity.ok(ResponseBusta.success(200, player, "Montepremi caricato correttamente"));
    }


    @GetMapping("/ricerca")
    public ResponseEntity<ResponseBusta<List<TorneoDTO>>> trovaTorneo(@RequestParam (required = false, defaultValue = "") String denominazione){

        List<Torneo> risultati = torneoService.ricercaCompatibili();
        List<TorneoDTO> torneoDTOS = torneoService.ricercaCompatibili().stream().map( t -> TorneoDTO.buildTorneoDTOFromModel(t, false)).collect(Collectors.toList());

        return ResponseEntity.ok(ResponseBusta.success(200, torneoDTOS, "Lista di tornei compatibili con l'elo"));
    }


    @PostMapping("/iscriviti/{id}")
    public ResponseEntity<ResponseBusta<UtenteDTO>> iscrvitiATorneo(@PathVariable (required = true) Long id){

        Utente utente = utenteService.iscriviti(id);
        UtenteDTO utenteIscritto = UtenteDTO.buildUtenteDTOFromModel(utente);

        return ResponseEntity.ok(ResponseBusta.success(200, utenteIscritto, "Utente iscritto con successo al torneo"));
    }


    @DeleteMapping("/abbandona")
    public ResponseEntity<ResponseBusta<TorneoDTO>> abbandonaTorneo(){

        TorneoDTO torneoDTO = torneoService.abbandonaTorneo();
        return ResponseEntity.ok(ResponseBusta.success(200, torneoDTO, "Utente rimosso correttamente dalla lista dei partecipanti"));
    }


    @GetMapping("/ultimo-torneo")
    public ResponseEntity<ResponseBusta<TorneoDTO>> ultimoTorneo(){

        TorneoDTO torneoDTO = torneoService.trovaTorneo();
        return ResponseEntity.ok(ResponseBusta.success(200, torneoDTO, "Trovato il torneo a cui sei associato"));
    }



}
