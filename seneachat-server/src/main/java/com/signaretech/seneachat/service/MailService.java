package com.signaretech.seneachat.service;

import com.signaretech.seneachat.util.EmailProtocol;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


@Service
public class MailService implements IMailService {

    private int port = 587;
    private String host = "mail.seneachat.com";
    private String from = "info@seneachat.com";
    private boolean auth = true;
    private String username = "info@seneachat.com";
    private String password = "Mandela12";
    private EmailProtocol protocol = EmailProtocol.SMTP;
    private boolean debug = true;

    /* (non-Javadoc)
     * @see com.signaretech.senachat.service.IMailService#sendMail(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void sendMail(String to, String subject, String body) throws MessagingException{

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        switch (protocol) {
            case SMTPS:
                props.put("mail.smtp.ssl.enable", true);
                break;

            default:
                break;
        }

        Authenticator authenticator = null;
        if(auth){
            props.put("mail.smtp.auth", true);
            authenticator = new Authenticator(){
                private PasswordAuthentication pa = new PasswordAuthentication(username, password);

                @Override
                public PasswordAuthentication getPasswordAuthentication(){
                    return pa;
                }
            };

        }

        Session session = Session.getInstance(props, authenticator);
        session.setDebug(debug);
        MimeMessage message = new MimeMessage(session );

        message.setFrom(new InternetAddress(from));
        InternetAddress[] address = {new InternetAddress(to)};
        message.setRecipients(Message.RecipientType.TO, address);
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setText(body);
        Transport.send(message);
    }
}

