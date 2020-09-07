package pokemon.pl.pokemon.services;

import org.springframework.stereotype.Service;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Token;
import pokemon.pl.pokemon.repositories.AppUserRepo;
import pokemon.pl.pokemon.repositories.TokenRepo;

@Service
public class TokenService {
    private AppUserRepo appUserRepo;

    private TokenRepo tokenRepo;

    public TokenService(AppUserRepo appUserRepo, TokenRepo tokenRepo) {
        this.appUserRepo = appUserRepo;
        this.tokenRepo = tokenRepo;
    }
    public void validateAppUser(String value){
        Token byValue = tokenRepo.findByValue(value);
        AppUser appUser = byValue.getAppUser();
        appUser.setEnabled(true);
        appUserRepo.save(appUser);
    }
}
