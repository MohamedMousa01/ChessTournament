package com.prova.chess.security;

import com.prova.chess.model.Utente;
import com.prova.chess.repository.utente.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    @Autowired
    UtenteRepository utenteRepository;

    public static boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public static boolean isOrganizer(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ORGANIZER"));
    }

    public static boolean isPlayer(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PLAYER"));
    }

    public static String getUsernameLoggato() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) return null;

        return authentication.getName();
    }

    public Long getCurrentUserId() {
        String username = getCurrentUsername();
        Utente utente = utenteRepository.findByUsername(username).orElse(null);
        if (utente != null)
            return utente.getId();
        else
            return null;
    }
}
