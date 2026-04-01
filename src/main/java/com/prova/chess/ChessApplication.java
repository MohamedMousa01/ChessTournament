package com.prova.chess;

import com.prova.chess.model.Ruolo;
import com.prova.chess.model.Stato;
import com.prova.chess.model.Utente;
import com.prova.chess.repository.ruolo.RuoloRepository;
import com.prova.chess.repository.utente.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@Component
public class ChessApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ChessApplication.class, args);
	}

	@Autowired
	private UtenteRepository utenteRepository;

	@Autowired
	private RuoloRepository ruoloRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {

		// Crea i ruoli se non esistono
		creaRuoloSeNonEsiste(Ruolo.ROLE_ADMIN, "Amministratore");
		creaRuoloSeNonEsiste(Ruolo.ROLE_PLAYER, "Giocatore");
		creaRuoloSeNonEsiste(Ruolo.ROLE_ORGANIZER, "Organizzatore");

		// Crea l'admin se non esiste
		if (utenteRepository.findByUsername("admin").isEmpty()) {

			Ruolo ruoloAdmin = ruoloRepository.findByCodice(Ruolo.ROLE_ADMIN).get();

			Set<Ruolo> ruoli = new HashSet<>();
			ruoli.add(ruoloAdmin);

			Utente admin = new Utente();
			admin.setNome("Admin");
			admin.setCognome("Admin");
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setDataRegistrazione(LocalDate.now());
			admin.setStato(Stato.ATTIVO);
			admin.setEloRating(0);
			admin.setMontePremi(0.0);
			admin.setRuoli(ruoli);

			utenteRepository.save(admin);
			System.out.println(">>> Admin creato con successo!");
		} else {
			System.out.println(">>> Admin già presente, skip.");
		}
	}

	private void creaRuoloSeNonEsiste(String codice, String descrizione) {
		if (ruoloRepository.findByCodice(codice).isEmpty()) {
			Ruolo ruolo = new Ruolo();
			ruolo.setCodice(codice);
			ruolo.setDescrizione(descrizione);
			ruoloRepository.save(ruolo);
			System.out.println(">>> Ruolo " + codice + " creato.");
		}
	}
}

