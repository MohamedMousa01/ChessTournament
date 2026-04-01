package com.prova.chess.security.auth;

import com.prova.chess.dto.UtenteDTO;
import com.prova.chess.model.Ruolo;
import com.prova.chess.model.Stato;
import com.prova.chess.model.Utente;
import com.prova.chess.repository.ruolo.RuoloRepository;
import com.prova.chess.repository.utente.UtenteRepository;
import com.prova.chess.security.JWTUtil;
import com.prova.chess.security.dto.UtenteAuthDTO;
import com.prova.chess.security.dto.UtenteRegisterDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	UtenteRepository utenteRepository;

	@Autowired
	RuoloRepository ruoloRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final JWTUtil jwtUtil;
	private final AuthenticationManager authManager;

	public AuthController(JWTUtil jwtUtil, AuthenticationManager authManager) {
		this.jwtUtil = jwtUtil;
		this.authManager = authManager;
	}

	@PostMapping("/login")
	public Map<String, Object> loginHandler(@RequestBody UtenteAuthDTO body) {
		try {
			// Creating the Authentication Token which will contain the credentials for
			// authenticating
			// This token is used as input to the authentication process
			UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
					body.getUsername(), body.getPassword());

			// Authenticating the Login Credentials
			authManager.authenticate(authInputToken);

			// Se siamo qui posso tranquillamente generare il JWT Token
			String token = jwtUtil.generateToken(body.getUsername());

			// Respond with the JWT
			return Collections.singletonMap("jwt-token", token);
		} catch (AuthenticationException authExc) {
			// Auhentication Failed
			throw new RuntimeException("Invalid Login Credentials");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerHandler(@RequestBody @Valid UtenteRegisterDTO body){

			if(utenteRepository.findByUsername(body.getUsername()).isPresent())
				return ResponseEntity
						.status(HttpStatus.CONFLICT) // Errore 409: Conflitto (ottimo per duplicati)
						.body("Errore: Lo username '" + body.getUsername() + "' è già in uso.");

			Ruolo ruolo_player = ruoloRepository.findByCodice(Ruolo.ROLE_PLAYER).orElseThrow(() -> new RuntimeException("Ruolo PLAYER non trovato"));

			Utente utenteRegistrato = new Utente();
			utenteRegistrato.setNome(body.getNome());
			utenteRegistrato.setCognome(body.getCognome());
			utenteRegistrato.setUsername(body.getUsername());
			utenteRegistrato.setPassword(passwordEncoder.encode(body.getPassword()));
			utenteRegistrato.setDataRegistrazione(LocalDate.now());
			utenteRegistrato.setEloRating(0);
			utenteRegistrato.setMontePremi(0.0);

			Set<Ruolo> ruoli = new HashSet<>();
			ruoli.add(ruolo_player);
			utenteRegistrato.setRuoli(ruoli);

			utenteRegistrato.setStato(Stato.ATTIVO);

			Utente salvato = utenteRepository.save(utenteRegistrato);

		return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
	}

}
