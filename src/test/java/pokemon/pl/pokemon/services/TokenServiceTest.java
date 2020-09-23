package pokemon.pl.pokemon.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Token;
import pokemon.pl.pokemon.repositories.AppUserRepo;
import pokemon.pl.pokemon.repositories.TokenRepo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    @Mock
    TokenRepo tokenRepo;
    @Mock
    AppUserRepo appUserRepo;

    Token token;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        token = prepareToken();
    }

    @Test
    void validateAppUser_Should_Set_Its_Value_Enabled_To_True_And_Ask_AppUserRepo_OneTime() {
//        given
        when(tokenRepo.findByValue(anyString())).thenReturn(token);
        TokenService tokenService = new TokenService(appUserRepo, tokenRepo);
        tokenService.validateAppUser(anyString());
//        when
        AppUser appUser = token.getAppUser();
//        then
        assertThat(appUser.isEnabled(), equalTo(true));
        verify(appUserRepo).save(appUser);
    }

    private Token prepareToken() {
        Token token = new Token();
        token.setAppUser(new AppUser(2L, "jannowak@gmail.com"));
        return token;
    }
}