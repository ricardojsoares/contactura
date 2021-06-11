package com.contactura.contactura.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactura.contactura.model.ContacturaUser;
import com.contactura.contactura.model.MensagemRetorno;
import com.contactura.contactura.repository.ContacturaUserRepository;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class ContacturaUserController {

	@Autowired
	private ContacturaUserRepository repository;
	
	@RequestMapping("/login")
	@GetMapping
	public ResponseEntity<?> login(HttpServletRequest request){			
		String token = request.getHeader("Authorization")
				.substring("Basic".length()).trim();

		return ResponseEntity.ok().body(new MensagemRetorno(token));
	}
	
	@GetMapping
	public List<?> findAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "{id}")
	public ResponseEntity<?> findById(@PathVariable long id) {
		return repository.findById(id)
				.map(user -> ResponseEntity.ok().body(user))
				.orElse(ResponseEntity.notFound().build());
		
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ContacturaUser create(@RequestBody ContacturaUser user) {
		user.setPassword(criptografarSenha(user.getPassword()));
		return repository.save(user);
	}
	
	@PutMapping(value = ("{id}"))
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@PathVariable long id, ContacturaUser user){
		return repository.findById(id)
				.map(record -> {
					record.setName(user.getName());
					record.setUsername(user.getUsername());
					record.setPassword(criptografarSenha(user.getPassword()));
					record.setAdmin(user.isAdmin());
					
					ContacturaUser update = repository.save(record);
					return ResponseEntity.ok().body(update);
				})
				.orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping(path = {"/{id}"})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable long id){
		MensagemRetorno retono = new MensagemRetorno("Usuário removido com sucesso!");
		return repository.findById(id)
				.map(user -> {
					repository.deleteById(id);
					return ResponseEntity.ok().body(retono);
				}).orElse(ResponseEntity.notFound().build());
	}
	
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder passwordEncode = new BCryptPasswordEncoder();
		return passwordEncode.encode(senha);
	}
}
