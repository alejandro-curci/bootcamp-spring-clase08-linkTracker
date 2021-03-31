package com.spring.linktracker.controllers;

import com.spring.linktracker.dtos.ErrorDTO;
import com.spring.linktracker.dtos.LinkDTO;
import com.spring.linktracker.exceptionHandlers.IDLinkException;
import com.spring.linktracker.exceptionHandlers.InvalidLinkException;
import com.spring.linktracker.exceptionHandlers.InvalidPasswordException;
import com.spring.linktracker.services.LinkService;
import com.spring.linktracker.services.LinkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class LinkController {

    @Autowired
    private LinkService service;

    @PostMapping("/create")
    public ResponseEntity crearLink(@RequestBody LinkDTO link, @RequestHeader String pass) throws InvalidLinkException {
        return new ResponseEntity(service.createLink(link, pass), HttpStatus.OK);
    }

    // REDIRECTION 1
    @GetMapping("/redirect")
    public RedirectView redirect(@RequestParam String id, @RequestHeader String pass) throws IDLinkException, InvalidPasswordException {
        RedirectView redir = new RedirectView();
        String urlRedirection = service.getRedirection(id, pass);
        redir.setUrl(urlRedirection);
        return redir;
    }

    // REDIRECTION 2
    @GetMapping("/redirect-again")
    public ResponseEntity redirectToExternalUrl(@RequestParam String id, @RequestHeader String pass) throws IDLinkException, URISyntaxException, InvalidPasswordException {
        URI urlRedirection = new URI(service.getRedirection(id, pass));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(urlRedirection);
        return new ResponseEntity(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/metrics")
    public ResponseEntity getMetrics(@RequestParam String id) throws IDLinkException {
        return new ResponseEntity(service.getData(id), HttpStatus.OK);
    }

    @GetMapping("/invalidate")
    public ResponseEntity removeLink(@RequestParam String id) throws IDLinkException {
        return new ResponseEntity(service.invalidate(id), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity linkNoValido(Exception e) {
        ErrorDTO err = new ErrorDTO();
        err.setMessage(e.getMessage());
        return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
    }
}
