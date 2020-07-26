package pokemon.pl.pokemon;

// TODO: 26.07.2020 zrobic strone domowa
//z menu do ktoryc beda linki do poszczegolnych podstron otwieranie paczek, itd
//1) logowanie gdzie wpisujesz login i hasło przez controller ma byc przekazywane hasło do seerwisu on ma zwalidfowac czy jest ok
//jesli dane ok to przez repo ma stworzyc usera z loginem i hasłem i zapisac w bazie
//jesli sukces maa wyslac na html z napisem sukces a jesli nie to na stronke ze sie nie udało moze byc zrobione wyjatkiem
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
