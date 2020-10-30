package pokemon.pl.pokemon.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.repositories.AppUserRepo;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {
    @Mock
    AppUserRepo appUserRepo;

    AppUser appUser;
    UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userDetailsService = new UserDetailsServiceImpl(appUserRepo);
        appUser = PrepareData.prepareAppUser();
    }

    @Test
    void loadUserByUsername_Should_Return_AppUser_If_UserName_Was_Found() {
//        given
        String userName = appUser.getUsername();
        when(appUserRepo.findByUsername(userName)).thenReturn(appUser);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//        then
        assertThat(userDetails.getUsername(), equalTo(appUser.getUsername()));
    }

    @Test
    void loadUserByUsername_Should_Ask_AppUserRepo_OneTime() {
//        given
        userDetailsService.loadUserByUsername(anyString());
//        then
        verify(appUserRepo).findByUsername(anyString());
    }

}