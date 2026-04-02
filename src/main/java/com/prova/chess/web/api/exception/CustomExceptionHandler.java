package com.prova.chess.web.api.exception;

import com.prova.chess.security.dto.ResponseBusta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler  {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(
			MethodArgumentNotValidException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.BAD_REQUEST.value());

		// Get all errors
		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("errors", errors);

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}



	@ExceptionHandler(IdNotNullForInsertException.class)
	public ResponseEntity<Object> handleIdNotNullForInsertException(IdNotNullForInsertException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);

		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(TorneoCreationException.class)
	public ResponseEntity<ResponseBusta<String>> handleTorneoCreationException(TorneoCreationException ex, WebRequest request) {

		ResponseBusta<String> busta = ResponseBusta.error(403, ex.getMessage());
		return new ResponseEntity<>(busta, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(ElementoNonTrovatoException.class)
	public ResponseEntity<ResponseBusta<String>> handleElementoNonTrovatoException(ElementoNonTrovatoException ex, WebRequest request) {

		ResponseBusta<String> busta = ResponseBusta.error(404, ex.getMessage());
		return new ResponseEntity<>(busta, HttpStatus.FORBIDDEN);

	}

}
