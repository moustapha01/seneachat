package com.signaretech.seneachat.service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface IMailService {

    public void sendMail(String to, String subject, String body) throws MessagingException;

}
