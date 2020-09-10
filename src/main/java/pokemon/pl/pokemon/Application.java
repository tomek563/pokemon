package pokemon.pl.pokemon;

// TODO: 26.07.2020 zrobic strone domowa
//z menu do ktoryc beda linki do poszczegolnych podstron otwieranie paczek, itd
//1) logowanie gdzie wpisujesz login i hasło przez controller ma byc przekazywane hasło do
// seerwisu on ma zwalidfowac czy jest ok
//jesli dane ok to przez repo ma stworzyc usera z loginem i hasłem i zapisac w bazie
//jesli sukces maa wyslac na html z napisem sukces a jesli nie to na stronke ze sie nie udało
// moze byc zrobione wyjatkiem

//stworz formularz dodawania trenera (jakies bajery) id usera takie samo jak trenera
//podstrona z wybieraniem kart - naciskasz przycisk i w ten sposob losujesz 5 kart dla trenera - i potem
//podstrona przeglądanie własnych kart
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.repositories.CoachRepo;

@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}