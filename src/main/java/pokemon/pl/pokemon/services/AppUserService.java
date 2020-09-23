package pokemon.pl.pokemon.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Token;
import pokemon.pl.pokemon.repositories.AppUserRepo;
import pokemon.pl.pokemon.repositories.TokenRepo;

import javax.mail.MessagingException;
import java.util.UUID;

@Service
public class AppUserService {

    private TokenRepo tokenRepo;
    private MailService mailService;
    private AppUserRepo appUserRepo;
    private PasswordEncoder passwordEncoder;
    private String url = "http://localhost:8080/token?value=";


    public AppUserService(TokenRepo tokenRepo, MailService mailService, AppUserRepo appUserRepo,
                          PasswordEncoder passwordEncoder) {
        this.tokenRepo = tokenRepo;
        this.mailService = mailService;
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRole("ROLE_USER");
        appUserRepo.save(appUser);
        sendToken(appUser);
    }

    private void sendToken(AppUser appUser) {
        String tokenValue = UUID.randomUUID().toString();

        Token token = new Token();   // new Token(tokenValue,appUser)
        token.setValue(tokenValue);
        token.setAppUser(appUser);
        tokenRepo.save(token);

        sendMail(appUser, tokenValue);
    }

    private void sendMail(AppUser appUser, String tokenValue) {
        try {
            mailService.sendMail(appUser.getUsername(), "Potwierdzaj to!", url+tokenValue, false);
        } catch (MessagingException e) {
            e.printStackTrace(); /*testuj tylko mailservice jako mock*/
        }
    }

    public AppUser getCurrentUser() {
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
