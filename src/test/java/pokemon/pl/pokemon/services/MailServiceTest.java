package pokemon.pl.pokemon.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.xmlunit.builder.Input;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailServiceTest {

    @Mock
    JavaMailSender javaMailSender;
    String to = "jannowak@gmail.com";
    String subject = "reply";
    String text = "it is a test generated message";
    boolean isHtmlContent = false;
    MimeMessage mimeMessage;

    @BeforeEach
    public void setUp() throws MessagingException {
        MockitoAnnotations.initMocks(this);
        mimeMessage = PrepareData.prepareMimeMessage();
    }

    @Test
    void sendMail_Should_Capture_ArgumentCaptor_With_Its_Correct_Properties() throws MessagingException {
//        given
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        MailService mailService = new MailService(javaMailSender);
//        when
        mailService.sendMail(to, subject, text, isHtmlContent);
        ArgumentCaptor<MimeMessage> argumentCaptor = ArgumentCaptor.forClass(MimeMessage.class);
//        then
        verify(javaMailSender).send(argumentCaptor.capture());
//        assertThat(argumentCaptor.getValue().getRecipients(Message.RecipientType.TO), equalTo(to)); JAK TO MOZNA ZAPISAC?
        assertThat(argumentCaptor.getValue().getAllHeaderLines().nextElement(), equalTo("To: " + to));
        assertThat(argumentCaptor.getValue().getSubject(), equalTo(subject));
    }

}