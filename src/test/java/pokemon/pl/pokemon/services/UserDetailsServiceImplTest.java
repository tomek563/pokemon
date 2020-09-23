package pokemon.pl.pokemon.services;

import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {
    //CZY TE TESTY MAJĄ SENS?
    @Test
    void loadUserByUsername_Should_Return_AppUser_If_UserName_Was_Found() {
//        given
        AppUserRepo appUserRepo = mock(AppUserRepo.class);
        String userName = "jannowak@gmail.com";
        when(appUserRepo.findByUsername(userName)).thenReturn(prepareAppUser());
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(appUserRepo);
        userDetailsService.loadUserByUsername(userName);
//        then
        assertThat(userName, equalTo(prepareAppUser().getUsername()));
    }

    @Test
    void loadUserByUsername_Should_Throw_UsernameNotFoundException_If_UserName_Was_Not_Found() {
//        given
        AppUserRepo appUserRepo = mock(AppUserRepo.class);
        when(appUserRepo.findByUsername(anyString())).thenThrow(UsernameNotFoundException.class);
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(appUserRepo);
//        then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(anyString()));
    }

    private AppUser prepareAppUser() {
        return new AppUser(2L, "jannowak@gmail.com");
    }
}