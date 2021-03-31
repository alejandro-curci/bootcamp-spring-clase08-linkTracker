package com.spring.linktracker.services;

import com.spring.linktracker.dtos.LinkDTO;
import com.spring.linktracker.dtos.ResponseDTO;
import com.spring.linktracker.exceptionHandlers.IDLinkException;
import com.spring.linktracker.exceptionHandlers.InvalidLinkException;
import com.spring.linktracker.exceptionHandlers.InvalidPasswordException;

public interface LinkService {

    public ResponseDTO createLink(LinkDTO linkDto, String pass) throws InvalidLinkException;
    public String getRedirection(String id, String pass) throws IDLinkException, InvalidPasswordException;
    public ResponseDTO getData(String id) throws IDLinkException;
    public ResponseDTO invalidate(String id) throws IDLinkException;
}
