package com.prova.chess.repository.ruolo;

import com.prova.chess.model.Ruolo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RuoloRepository extends CrudRepository<Ruolo, Long> {
	Ruolo findByDescrizioneAndCodice(String descrizione, String codice);

	Optional<Ruolo> findByCodice(String codice);
}
