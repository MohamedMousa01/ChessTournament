package com.prova.chess.repository.torneo;

import com.prova.chess.model.Torneo;
import com.prova.chess.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface TorneoRepository extends CrudRepository<Torneo, Long>, JpaRepository<Torneo, Long> {

    @Query("select distinct t from Torneo t left join fetch t.partecipanti ")
    List<Torneo> findAllEager();

    @Query("from Torneo t left join fetch t.partecipanti where t.id=?1")
    Torneo findByIdEager(Long idTorneo);

    @Query("select distinct t from Torneo t left join fetch t.partecipanti where t.utenteCreazione = ?1 ")
    List<Torneo> findByUtenteCreazione(Utente utenteCreazione);

}
