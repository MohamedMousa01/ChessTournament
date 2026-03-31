package com.prova.chess.service;

import com.prova.chess.model.Torneo;
import com.prova.chess.repository.torneo.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TorneoServiceImpl {

    @Autowired
    private TorneoRepository torneoRepository;

    public List<Torneo> listAllElements() {

        return (List<Torneo>) torneoRepository.findAll();
    }



}
