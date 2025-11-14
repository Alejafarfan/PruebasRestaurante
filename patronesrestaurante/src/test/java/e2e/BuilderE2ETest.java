package e2e;

import Creacionales.Builder.controlador.BuilderController;
import Creacionales.Builder.modelo.Dish;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuilderE2ETest {

    @Test
    void builderFullFlow_dailyAndVegetarian() {
        BuilderController controller = new BuilderController();

        Dish daily = controller.buildDish("dia");
        assertNotNull(daily);

        Dish vegetarian = controller.buildDish("vegetariano");
        assertEquals("Menú Vegetariano", vegetarian.getName());
        assertTrue(vegetarian.getSideIngredients().contains("Arroz integral"));
    }
}