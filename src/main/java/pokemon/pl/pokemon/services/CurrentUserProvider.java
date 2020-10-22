package pokemon.pl.pokemon.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pokemon.pl.pokemon.model.AppUser;

@Component
public class CurrentUserProvider {
    public AppUser getCurrentUser() {
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}