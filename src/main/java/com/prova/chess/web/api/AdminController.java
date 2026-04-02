package com.prova.chess.web.api;

import com.prova.chess.dto.TorneoDTO;
import com.prova.chess.dto.UtenteDTO;
import com.prova.chess.model.Ruolo;
import com.prova.chess.model.Utente;
import com.prova.chess.repository.utente.UtenteRepository;
import com.prova.chess.security.dto.ResponseBusta;
import com.prova.chess.security.dto.UtenteInfoJWTResponseDTO;
import com.prova.chess.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/utenti")
public class AdminController {

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    UtenteService utenteService;

    @GetMapping
    public ResponseEntity<ResponseBusta<List<UtenteDTO>>> getAll(){
        List<UtenteDTO> utenti = utenteService.listAllUtenti().stream().map(UtenteDTO::buildUtenteDTOFromModel)
                .toList();
        return ResponseEntity.ok(ResponseBusta.success(200, utenti, "Lista di utenti caricata correttamente"));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseBusta<UtenteInfoJWTResponseDTO>> getById(@PathVariable(required = true) Long id){
        if(id == null)
            throw new RuntimeException();

        Utente utenteLoggato = utenteService.caricaSingoloUtente(id);

        List<String> roles = utenteLoggato.getRuoli().stream()
                .map(Ruolo::getCodice)
                .toList();

        TorneoDTO torneoDTO = null;
        if (utenteLoggato.getTorneo() != null) {
            torneoDTO = TorneoDTO.buildTorneoDTOFromModel(utenteLoggato.getTorneo(), false);
        }

        UtenteInfoJWTResponseDTO payload = new UtenteInfoJWTResponseDTO(
                utenteLoggato.getNome(),
                utenteLoggato.getCognome(),
                utenteLoggato.getUsername(),
                utenteLoggato.getDataRegistrazione(),
                roles,
                torneoDTO,
                utenteLoggato.getEloRating(),
                utenteLoggato.getMontePremi()
        );

        return ResponseEntity.ok(ResponseBusta.success(200, payload,"Utente trovato correttamente"));
    }


    @PostMapping
    public ResponseEntity<ResponseBusta<UtenteDTO>> createNew(@Valid @RequestBody UtenteDTO utenteInput ){
        if (utenteInput == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseBusta.error(400, "I dati dell'utente sono mancanti"));
        }

        //devo fare il controllo dove non si puo inserire un utente con usernamen già in uso

        Utente utente = utenteService.inserisciNuovo(utenteInput.buildUtenteModel(false));
        UtenteDTO utenteDTO = UtenteDTO.buildUtenteDTOFromModel(utente);

        return ResponseEntity.ok(ResponseBusta.success(201, utenteDTO , "Utente inserito correttamente"));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseBusta<UtenteDTO>> update(@Valid @RequestBody UtenteDTO utenteInput,  @PathVariable(required = true) Long id){
        Utente utenteCaricato = utenteService.caricaSingoloUtente(id);
        if(utenteCaricato == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseBusta.error(400, "I dati dell'utente sono mancanti"));
        }

        utenteInput.setId(id);
        Utente utenteAggiornato = utenteService.aggiorna(utenteInput.buildUtenteModel(true));
        UtenteDTO result =  UtenteDTO.buildUtenteDTOFromModel(utenteAggiornato);
        return ResponseEntity.ok(ResponseBusta.success(200, result , "Utente aggiornato con successo"));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBusta<UtenteDTO>> delete( @PathVariable(required = true) Long id){

        try {
            UtenteDTO utenteDisattivato = utenteService.disattiva(id);

            return ResponseEntity.ok(
                    ResponseBusta.success(200, utenteDisattivato, "Utente disattivato con successo")
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseBusta.error(404, e.getMessage()));
        }

    }


}
