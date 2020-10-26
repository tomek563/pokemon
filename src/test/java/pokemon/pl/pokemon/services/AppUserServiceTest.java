package pokemon.pl.pokemon.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Token;
import pokemon.pl.pokemon.repositories.AppUserRepo;
import pokemon.pl.pokemon.repositories.TokenRepo;

import javax.mail.MessagingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppUserServiceTest {
    @Mock
    TokenRepo tokenRepo;
    @Mock
    AppUserRepo appUserRepo;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    MailService mailService;
    @Mock
    CurrentUserProvider currentUserProvider;
    @InjectMocks
    AppUserService appUserService;
    AppUser appUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        appUser = PrepareData.prepareAppUser();
    }

    @Test
    void addUser_Should_Set_Password_And_Role_To_User_And_ArgumentCapture_Token_Set_Appuser() throws MessagingException {
//        given
        String passwordBeforeTest = appUser.getPassword();
        String roleBeforeTest = appUser.getRole();
        when(passwordEncoder.encode(any())).thenReturn(anyString());
        appUserService.addUser(appUser);
        ArgumentCaptor<Token> argumentCaptor = ArgumentCaptor.forClass(Token.class);
//        then
        verify(appUserRepo).save(appUser);
        verify(mailService).sendMail(anyString(), anyString(), anyString(), anyBoolean());
        verify(tokenRepo).save(argumentCaptor.capture());
        assertThat(appUser.getPassword(), not(equalTo(passwordBeforeTest)));
        assertThat(appUser.getRole(), not(equalTo(roleBeforeTest)));

        Token token = argumentCaptor.getValue();
        assertThat(token.getAppUser(), equalTo(appUser));
    }
    @Test
    void getLoggedUserId_Should_Return_LoggedUserId() {
//        given
        when(currentUserProvider.getCurrentUser()).thenReturn(appUser);
        Long id = appUser.getId();
//        then
        assertThat(appUserService.getLoggedUserId(), equalTo(id));
    }


    @Test
    void isAppUserInAppUserRepo_Should_Return_True_If_There_Is_AppUser() {
//        given
        when(appUserRepo.findByUsername(anyString())).thenReturn(appUser);
//        then
        assertThat(appUserService.isAppUserInAppUserRepo(anyString()),equalTo(true));
    }
    @Test
    void isAppUserInAppUserRepo_Should_Return_False_If_There_Is_No_AppUser() {
//        given
        when(appUserRepo.findByUsername(anyString())).thenReturn(null);
//        then
        assertThat(appUserService.isAppUserInAppUserRepo(anyString()),equalTo(false));
    }
}