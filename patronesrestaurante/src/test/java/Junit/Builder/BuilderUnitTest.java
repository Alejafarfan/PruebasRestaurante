package Junit.Builder;

import Creacionales.Builder.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class BuilderUnitTest {

    private ChefDirector chef;

    @BeforeEach
    void setUp() {
        chef = new ChefDirector();
    }

    @Test
    void vegetarianDishShouldBeBuiltCorrectly() {
        chef.setBuilder(new VegetarianDishBuilder());
        Dish dish = chef.constructDish();

        assertEquals("Menú Vegetariano", dish.getName());
        assertTrue(dish.getSideIngredients().contains("Arroz integral"));
        assertEquals("Tofu con verduras salteadas", dish.getMainIngredient());
    }

    @Test
    void vegetarianDishShouldHaveCorrectPrice() {
        chef.setBuilder(new VegetarianDishBuilder());
        Dish dish = chef.constructDish();
        assertEquals(18000, dish.getPrice(), 0.001);
    }

    @Test
    void vegetarianDishShouldHaveCorrectDrink() {
        chef.setBuilder(new VegetarianDishBuilder());
        Dish dish = chef.constructDish();
        assertEquals("Jugo natural de naranja", dish.getDrink());
    }

    @Test
    void vegetarianDishShouldHaveAllSideIngredients() {
        chef.setBuilder(new VegetarianDishBuilder());
        Dish dish = chef.constructDish();
        var sides = dish.getSideIngredients();
        assertEquals(2, sides.size());
        assertTrue(sides.contains("Ensalada fresca"));
    }

    @Test
    void gourmetDishShouldBeBuiltCorrectly() {
        chef.setBuilder(new GourmetDishBuilder());
        Dish dish = chef.constructDish();

        assertEquals("Menú Gourmet", dish.getName());
        assertEquals("Filete mignon con salsa de vino tinto", dish.getMainIngredient());
        assertTrue(dish.getSideIngredients().contains("Puré de papa trufado"));
    }

    @Test
    void gourmetDishShouldHaveCorrectPrice() {
        chef.setBuilder(new GourmetDishBuilder());
        Dish dish = chef.constructDish();
        assertEquals(32000, dish.getPrice(), 0.001);
    }

    @Test
    void gourmetDishShouldHaveCorrectDrink() {
        chef.setBuilder(new GourmetDishBuilder());
        Dish dish = chef.constructDish();
        assertEquals("Copa de vino tinto", dish.getDrink());
    }

    @Test
    void gourmetDishShouldHaveAllSideIngredients() {
        chef.setBuilder(new GourmetDishBuilder());
        Dish dish = chef.constructDish();
        var sides = dish.getSideIngredients();
        assertEquals(2, sides.size());
        assertTrue(sides.contains("Espárragos al vapor"));
    }

    @Test
    void dishShouldReturnIndependentSideIngredientsList() {
        chef.setBuilder(new VegetarianDishBuilder());
        Dish dish = chef.constructDish();
        var sides1 = dish.getSideIngredients();
        var sides2 = dish.getSideIngredients();
        assertNotSame(sides1, sides2);
    }

    @Test
    void dishToStringShouldContainAllData() {
        chef.setBuilder(new VegetarianDishBuilder());
        Dish dish = chef.constructDish();
        String representation = dish.toString();
        assertTrue(representation.contains("Menú Vegetariano"));
        assertTrue(representation.contains("Tofu"));
        assertTrue(representation.contains("18000"));
    }

    @Test
    void dishShouldBeInitializedCorrectly() {
        Dish dish = new Dish();
        assertNull(dish.getName());
        assertNull(dish.getMainIngredient());
        assertEquals(0, dish.getPrice(), 0.001);
        assertEquals(0, dish.getSideIngredients().size());
    }

    @Test
    void dishShouldAllowSettingAllAttributes() {
        Dish dish = new Dish();
        dish.setName("Mi Plato");
        dish.setMainIngredient("Arroz");
        dish.addSideIngredient("Frijoles");
        dish.addSideIngredient("Ensalada");
        dish.setDrink("Agua");
        dish.setPrice(12000);

        assertEquals("Mi Plato", dish.getName());
        assertEquals("Arroz", dish.getMainIngredient());
        assertEquals(2, dish.getSideIngredients().size());
        assertEquals("Agua", dish.getDrink());
        assertEquals(12000, dish.getPrice(), 0.001);
    }

    @Test
    void dishShouldAddMultipleSideIngredients() {
        Dish dish = new Dish();
        dish.addSideIngredient("Ingrediente 1");
        dish.addSideIngredient("Ingrediente 2");
        dish.addSideIngredient("Ingrediente 3");

        assertEquals(3, dish.getSideIngredients().size());
    }
    // Extended Builder tests for comprehensive coverage
    @Test
    void vegetarianDishBuilderIsBuilder() {
        VegetarianDishBuilder builder = new VegetarianDishBuilder();
        assertNotNull(builder);
    }

    @Test
    void gourmetDishBuilderIsBuilder() {
        GourmetDishBuilder builder = new GourmetDishBuilder();
        assertNotNull(builder);
    }

    @Test
    void dailyMenuBuilderIsBuilder() {
        DailyMenuBuilder builder = new DailyMenuBuilder();
        assertNotNull(builder);
    }

    @Test
    void chefDirectorWithVegetarian() {
        ChefDirector director = new ChefDirector();
        director.setBuilder(new VegetarianDishBuilder());
        Dish dish = director.constructDish();
        assertEquals("Menú Vegetariano", dish.getName());
    }

    @Test
    void chefDirectorWithGourmet() {
        ChefDirector director = new ChefDirector();
        director.setBuilder(new GourmetDishBuilder());
        Dish dish = director.constructDish();
        assertEquals("Menú Gourmet", dish.getName());
    }

    @Test
    void chefDirectorWithDaily() {
        ChefDirector director = new ChefDirector();
        director.setBuilder(new DailyMenuBuilder());
        Dish dish = director.constructDish();
        assertEquals("Menú del Día", dish.getName());
    }

    @Test
    void dishCopyConstructorOrIndependence() {
        chef.setBuilder(new VegetarianDishBuilder());
        Dish dish1 = chef.constructDish();
        
        chef.setBuilder(new GourmetDishBuilder());
        Dish dish2 = chef.constructDish();
        
        assertNotEquals(dish1.getName(), dish2.getName());
    }

    @Test
    void multipleVegetarianDishesAreIndependent() {
        VegetarianDishBuilder builder1 = new VegetarianDishBuilder();
        VegetarianDishBuilder builder2 = new VegetarianDishBuilder();
        builder1.createNewDish();
        builder1.buildName();
        builder1.buildMainIngredient();
        builder1.buildSideIngredients();
        builder1.buildDrink();
        builder1.buildPrice();
        
        builder2.createNewDish();
        builder2.buildName();
        builder2.buildMainIngredient();
        builder2.buildSideIngredients();
        builder2.buildDrink();
        builder2.buildPrice();
        
        Dish veg1 = builder1.getDish();
        Dish veg2 = builder2.getDish();
        
        assertNotSame(veg1, veg2);
        assertEquals(veg1.getName(), veg2.getName());
    }

    @Test
    void vegetarianDishBuilderConstructsValidDish() {
        VegetarianDishBuilder builder = new VegetarianDishBuilder();
        builder.createNewDish();
        builder.buildName();
        builder.buildMainIngredient();
        builder.buildSideIngredients();
        builder.buildDrink();
        builder.buildPrice();
        Dish dish = builder.getDish();
        
        assertNotNull(dish.getName());
        assertNotNull(dish.getMainIngredient());
        assertTrue(dish.getPrice() > 0);
        assertFalse(dish.getSideIngredients().isEmpty());
    }

    @Test
    void gourmetDishBuilderConstructsValidDish() {
        GourmetDishBuilder builder = new GourmetDishBuilder();
        builder.createNewDish();
        builder.buildName();
        builder.buildMainIngredient();
        builder.buildSideIngredients();
        builder.buildDrink();
        builder.buildPrice();
        Dish dish = builder.getDish();
        
        assertNotNull(dish.getName());
        assertNotNull(dish.getMainIngredient());
        assertTrue(dish.getPrice() > 0);
        assertFalse(dish.getSideIngredients().isEmpty());
    }

    @Test
    void dailyMenuBuilderConstructsValidDish() {
        DailyMenuBuilder builder = new DailyMenuBuilder();
        builder.createNewDish();
        builder.buildName();
        builder.buildMainIngredient();
        builder.buildSideIngredients();
        builder.buildDrink();
        builder.buildPrice();
        Dish dish = builder.getDish();
        
        assertNotNull(dish.getName());
        assertNotNull(dish.getMainIngredient());
        assertTrue(dish.getPrice() > 0);
        assertFalse(dish.getSideIngredients().isEmpty());
    }

    @Test
    void vegetarianDishHasMoreThanOneIngredient() {
        chef.setBuilder(new VegetarianDishBuilder());
        Dish dish = chef.constructDish();
        assertTrue(dish.getSideIngredients().size() >= 2);
    }

    @Test
    void gourmetDishHasMoreThanOneIngredient() {
        chef.setBuilder(new GourmetDishBuilder());
        Dish dish = chef.constructDish();
        assertTrue(dish.getSideIngredients().size() >= 2);
    }

    @Test
    void dailyMenuDishHasMultipleIngredients() {
        chef.setBuilder(new DailyMenuBuilder());
        Dish dish = chef.constructDish();
        assertTrue(dish.getSideIngredients().size() >= 2);
    }

    @Test
    void dishToStringIncludesPrice() {
        chef.setBuilder(new GourmetDishBuilder());
        Dish dish = chef.constructDish();
        assertTrue(dish.toString().contains("32000"));
    }

    @Test
    void dishToStringIncludesDrink() {
        chef.setBuilder(new VegetarianDishBuilder());
        Dish dish = chef.constructDish();
        assertTrue(dish.toString().contains("Jugo"));
    }

    @Test
    void dishAllGettersWork() {
        Dish dish = new Dish();
        dish.setName("Test");
        dish.setMainIngredient("Main");
        dish.setPrice(5000);
        dish.setDrink("Drink");
        dish.addSideIngredient("Side");
        
        assertEquals("Test", dish.getName());
        assertEquals("Main", dish.getMainIngredient());
        assertEquals(5000, dish.getPrice(), 0.001);
        assertEquals("Drink", dish.getDrink());
        assertEquals(1, dish.getSideIngredients().size());
    }

    @Test
    void vegetarianVsGourmetPrices() {
        chef.setBuilder(new VegetarianDishBuilder());
        Dish vegetarian = chef.constructDish();
        
        chef.setBuilder(new GourmetDishBuilder());
        Dish gourmet = chef.constructDish();
        
        assertTrue(gourmet.getPrice() > vegetarian.getPrice());
    }

    @Test
    void vegetarianDishNameIsConsistent() {
        for (int i = 0; i < 5; i++) {
            chef.setBuilder(new VegetarianDishBuilder());
            Dish dish = chef.constructDish();
            assertEquals("Menú Vegetariano", dish.getName());
        }
    }

    @Test
    void gourmetDishNameIsConsistent() {
        for (int i = 0; i < 5; i++) {
            chef.setBuilder(new GourmetDishBuilder());
            Dish dish = chef.constructDish();
            assertEquals("Menú Gourmet", dish.getName());
        }
    }

    @Test
    void dailyMenuDishNameIsConsistent() {
        for (int i = 0; i < 5; i++) {
            chef.setBuilder(new DailyMenuBuilder());
            Dish dish = chef.constructDish();
            assertEquals("Menú del Día", dish.getName());
        }
    }

    @Test
    void dishSideIngredientsReturnedConsistently() {
        Dish dish = new Dish();
        dish.addSideIngredient("Ingredient1");
        var list1 = dish.getSideIngredients();
        var list2 = dish.getSideIngredients();
        
        assertEquals(list1.size(), list2.size());
        assertTrue(list2.contains("Ingredient1"));
    }

    @Test
    void allBuildersConstructSpecificDishes() {
        VegetarianDishBuilder vBuilder = new VegetarianDishBuilder();
        vBuilder.createNewDish();
        vBuilder.buildName();
        vBuilder.buildMainIngredient();
        
        GourmetDishBuilder gBuilder = new GourmetDishBuilder();
        gBuilder.createNewDish();
        gBuilder.buildName();
        gBuilder.buildMainIngredient();
        
        DailyMenuBuilder dBuilder = new DailyMenuBuilder();
        dBuilder.createNewDish();
        dBuilder.buildName();
        dBuilder.buildMainIngredient();
        
        assertNotNull(vBuilder.getDish().getName());
        assertNotNull(gBuilder.getDish().getName());
        assertNotNull(dBuilder.getDish().getName());
    }

    @Test
    void dishBuilderStepsAreIndependent() {
        VegetarianDishBuilder builder = new VegetarianDishBuilder();
        builder.createNewDish();
        builder.buildName();
        Dish partialDish = builder.getDish();
        assertNotNull(partialDish.getName());
    }

    @Test
    void complexBuilderScenario() {
        chef.setBuilder(new VegetarianDishBuilder());
        Dish veg = chef.constructDish();
        assertEquals("Menú Vegetariano", veg.getName());
        assertEquals("Tofu con verduras salteadas", veg.getMainIngredient());
        assertEquals(18000, veg.getPrice(), 0.001);
        
        chef.setBuilder(new GourmetDishBuilder());
        Dish gourmet = chef.constructDish();
        assertEquals("Menú Gourmet", gourmet.getName());
        assertEquals("Filete mignon con salsa de vino tinto", gourmet.getMainIngredient());
        assertEquals(32000, gourmet.getPrice(), 0.001);
        
        chef.setBuilder(new DailyMenuBuilder());
        Dish daily = chef.constructDish();
        assertEquals("Menú del Día", daily.getName());
        assertEquals("Pechuga de pollo a la plancha", daily.getMainIngredient());
        assertEquals(15000, daily.getPrice(), 0.001);
    }
}

