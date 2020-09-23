package pokemon.pl.pokemon.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class IndexControllerTest {
    @Mock
    Model model;

    IndexController indexController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController();
    }

    @Test
    void sayHello() {
        String sayHello = indexController.getIndexPage();
        assertThat(sayHello, equalTo("index"));

    }

    @Test
    void layout() {
        String layout = indexController.getLayout();
        assertThat(layout, equalTo("layout"));
    }


    @Test
    void loginFailure() {
        String sayHello = indexController.getLoginFailure(model);
        assertThat(sayHello, equalTo("failure"));
        verify(model, times(1)).addAttribute(eq("failure"), anyString());
    }

    @Test
    void showNoCoach() {
        String noCoach = indexController.getNoCoach();
        assertThat(noCoach, equalTo("no-coach"));
    }

    @Test
    void showNoCard() {
        String noCard = indexController.getNoCard();
        assertThat(noCard, equalTo("no-card"));
    }
}