package com.spring.linktracker.services;

import com.spring.linktracker.dtos.LinkDTO;
import com.spring.linktracker.dtos.LinkResponseDTO;
import com.spring.linktracker.dtos.ResponseDTO;
import com.spring.linktracker.exceptionHandlers.IDLinkException;
import com.spring.linktracker.exceptionHandlers.InvalidLinkException;
import com.spring.linktracker.exceptionHandlers.InvalidPasswordException;
import com.spring.linktracker.repositories.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LinkServiceImpl implements LinkService{
    @Autowired
    private LinkRepository repository;

    private boolean validateUrl(String url) {
        String regex = "((http|https)://)(www.)?[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)";
        Pattern p = Pattern.compile(regex);
        if (url == null) {
            return false;
        }
        Matcher m = p.matcher(url);
        return m.matches();
    }

    public ResponseDTO createLink(LinkDTO linkDto, String pass) throws InvalidLinkException {
        ResponseDTO response = new ResponseDTO();
        LinkResponseDTO linkResponseDTO = new LinkResponseDTO();
        if (validateUrl(linkDto.getUrl())) {
            repository.setCountID(repository.getCountID()+1);
            int newId = repository.getCountID();
            linkResponseDTO.setId(newId);
            linkResponseDTO.setUrl(linkDto.getUrl());
            linkResponseDTO.setPassword(pass);
            repository.getDatabase().put(newId, linkResponseDTO);
        } else {
            throw new InvalidLinkException("Link URL is not valid!");
        }
        response.setAction("Link Creation Action");
        response.setDescription("The link has been succesfully created! The ID is "+linkResponseDTO.getId());
        return response;
    }

    public String getRedirection(String id, String pass) throws IDLinkException, InvalidPasswordException {
        String redirectionLink = null;
        int idNum;
        try {
            idNum = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Link ID is not a number!");
        }
        Map<Integer, LinkResponseDTO> db = repository.getDatabase();
        if (!db.containsKey(idNum)) {
            throw new IDLinkException("Link ID does not exist!");
        } else if(!db.get(idNum).getPassword().equals(pass)) {
            throw new InvalidPasswordException("Wrong Password!");
        } else {
            redirectionLink = db.get(idNum).getUrl();
            db.get(idNum).setRedirectCount(db.get(idNum).getRedirectCount() + 1);
        }
        return redirectionLink;
    }

    public ResponseDTO getData(String id) throws IDLinkException {
        int idNum;
        ResponseDTO response = new ResponseDTO();
        try {
            idNum = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Link ID is not a number!");
        }
        Map<Integer, LinkResponseDTO> db = repository.getDatabase();
        if (!db.containsKey(idNum)) {
            throw new IDLinkException("Link does not exist!!");
        } else {
            response.setAction("Metrics about link with id: "+idNum);
            response.setDescription("Number of redirections: "+db.get(idNum).getRedirectCount());
        }
        return response;
    }

    public ResponseDTO invalidate(String id) throws IDLinkException {
        int idNum;
        ResponseDTO response = new ResponseDTO();
        try {
            idNum = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Link ID is not a number!");
        }
        Map<Integer, LinkResponseDTO> db = repository.getDatabase();
        if (!db.containsKey(idNum)) {
            throw new IDLinkException("Link ID does not exist!");
        } else {
            db.remove(idNum);
            response.setAction("Invalidate Link Action");
            response.setDescription("Link has been successfully removed");
        }
        return response;
    }
}
