package com.contactura.contactura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactura.contactura.model.Contactura;
import com.contactura.contactura.model.MensagemRetorno;
import com.contactura.contactura.repository.ContacturaRepository;

@CrossOrigin
@RestController
@RequestMapping({"/contactura"})
public class ContacturaController {

	@Autowired
	private ContacturaRepository repository;
	
	//Lista todos os contatos - http://loalhost:8090/contactura
	@GetMapping
	public List<?> findAll() {
		return repository.findAll();
	}
	
	//Retorna um contato - http://loalhost:8090/contactura/10
	@GetMapping(value="{id}")
	public ResponseEntity<?> findById(@PathVariable long id) {
		return repository.findById(id)
				.map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}
	
	//Cria um novo contato - http://loalhost:8090/contactura/10
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Contactura create(@RequestBody Contactura contactura) {
		return repository.save(contactura);
	}
	
	//Deleta o contato - http://loalhost:8090/contactura/10
	@PutMapping(value = "{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@PathVariable long id, @RequestBody Contactura contato) {
		return repository.findById(id)
				.map(record -> {
					record.setName(contato.getName());
					record.setEmail(contato.getEmail());
					record.setPhone(contato.getPhone());
					Contactura update = repository.save(record);
					return ResponseEntity.ok().body(update);
				}).orElse(ResponseEntity.notFound().build());
	}
	
	//Deleta o contato - http://loalhost:8090/contactura/10
	@DeleteMapping(path = {"{id}"})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable long id) {
		MensagemRetorno retono = new MensagemRetorno("Deletado com sucesso!");
		return repository.findById(id)
					.map(record -> {
						repository.deleteById(id);
						return ResponseEntity.ok().body(retono);
					})
					.orElse(ResponseEntity.notFound().build());
	}
}
