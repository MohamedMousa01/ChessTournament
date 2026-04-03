package com.prova.chess.web.api;


import com.prova.chess.dto.TorneoDTO;
import com.prova.chess.dto.UtenteDTO;
import com.prova.chess.model.Torneo;
import com.prova.chess.model.Utente;
import com.prova.chess.repository.utente.UtenteRepository;
import com.prova.chess.security.dto.ResponseBusta;
import com.prova.chess.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/tornei")
public class TorneoController {

    @Autowired
    TorneoService torneoService;

    @Autowired
    UtenteRepository utenteRepository;

    @GetMapping
    public ResponseEntity<ResponseBusta<List<TorneoDTO>>> getAll(Authentication authentication){

        String username = authentication.getName();

        Utente utenteCorrente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato nel database"));

        List<TorneoDTO> dtos = torneoService.listAllElements(utenteCorrente)
                .stream()
                .map(t -> TorneoDTO.buildTorneoDTOFromModel(t, false))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseBusta.success(200, dtos, "Lista di tornei caricati correttamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBusta<TorneoDTO>> getById(@PathVariable (required = true) Long id){

        Torneo torneoCaricato = torneoService.caricaSingoloTorneo(id);
        if(torneoCaricato == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseBusta.error(400, "I dati dell'utente sono mancanti"));

        TorneoDTO torneoDTO = TorneoDTO.buildTorneoDTOFromModel(torneoCaricato, true);
        return ResponseEntity.ok(ResponseBusta.success(200, torneoDTO, "Torneo trovato correttamente"));
    }


    @PostMapping
    public ResponseEntity<ResponseBusta<TorneoDTO>> createNew(@Valid @RequestBody TorneoDTO torneoInput){

        if(torneoService.trovaMatching(torneoInput) != null && !(torneoService.trovaMatching(torneoInput).isEmpty()))
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseBusta.error(400, "Il torneo sembra essere già presente"));

        Torneo torneoDaInserire = torneoService.inserisciNuovo(torneoInput.buildTorneoModel());
        TorneoDTO torneoDTO = TorneoDTO.buildTorneoDTOFromModel(torneoDaInserire, true);
        return  ResponseEntity.ok(ResponseBusta.success(200, torneoDTO, "Torneo creato con successp"));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseBusta<TorneoDTO>> update(@Valid @RequestBody TorneoDTO torneoInput, @PathVariable (required = true) Long id){
        Torneo torneoCaricato = torneoService.caricaSingoloTorneo(id);

        if(torneoCaricato == null || id == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseBusta.error(400, "Utente non trovato"));

        torneoInput.setId(id);
        Torneo torneo = torneoService.aggiorna(torneoInput.buildTorneoModel());
        TorneoDTO torneoDTO = TorneoDTO.buildTorneoDTOFromModel(torneo, true);
        return ResponseEntity.ok(ResponseBusta.success(200, torneoDTO, "Torneo modificato con successo"));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBusta<TorneoDTO>> delete(@PathVariable (required = true) Long id){
        try {
            TorneoDTO torneoDisattivato = torneoService.disattiva(id);
            return ResponseEntity.ok(
                    ResponseBusta.success(200, torneoDisattivato, "Torneo disattivato con successo")
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseBusta.error(404, e.getMessage()));
        }
    }

}
