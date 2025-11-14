package Junit.Proxy;

import Estructurales.Proxy.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ProxyUnitTest {

    private Kitchen kitchen;
    private Waiter waiter;
    private Client client;

    @BeforeEach
    void setUp() {
        kitchen = new Kitchen("Andrés", 1, "Pasta");
        waiter = new Waiter("Carlos", kitchen);
        client = new Client("Lucía", waiter);
    }

    @Test
    void waiterDebeTomarYEntregarPedido() {
        waiter.takeOrder("Spaghetti");
        assertEquals("Spaghetti", waiter.getCurrentOrder());
    }

    @Test
    void clientDebeSolicitarPlato() {
        client.requestDish("Tiramisú");
        assertEquals("Lucía", client.getName());
    }

    @Test
    void waiterDebeActualizarPedidoActual() {
        waiter.takeOrder("César");
        assertEquals("César", waiter.getCurrentOrder());
        waiter.takeOrder("Griega");
        assertEquals("Griega", waiter.getCurrentOrder());
    }

    @Test
    void kitchenDebePrepararPlatoCorrectamente() {
        String dish = kitchen.prepareDish("Bife");
        assertNotNull(dish);
        assertTrue(dish.contains("Bife"));
    }

    @Test
    void waiterSinPedidoDebeRetornarNull() {
        Waiter newWaiter = new Waiter("Sofía", kitchen);
        assertNull(newWaiter.getCurrentOrder());
    }

    @Test
    void waiterShouldStoreDifferentOrders() {
        waiter.takeOrder("Orden 1");
        assertEquals("Orden 1", waiter.getCurrentOrder());
        
        waiter.takeOrder("Orden 2");
        assertEquals("Orden 2", waiter.getCurrentOrder());
        
        waiter.takeOrder("Orden 3");
        assertEquals("Orden 3", waiter.getCurrentOrder());
    }

    @Test
    void clientShouldHaveName() {
        assertEquals("Lucía", client.getName());
    }

    @Test
    void kitchenShouldPrepareMultipleDishes() {
        String dish1 = kitchen.prepareDish("Pizza");
        String dish2 = kitchen.prepareDish("Pasta");
        String dish3 = kitchen.prepareDish("Ensalada");

        assertNotNull(dish1);
        assertNotNull(dish2);
        assertNotNull(dish3);
    }

    @Test
    void multipleWaitersWithSameKitchen() {
        Waiter waiter2 = new Waiter("Pedro", kitchen);
        waiter.takeOrder("Plato A");
        waiter2.takeOrder("Plato B");

        assertEquals("Plato A", waiter.getCurrentOrder());
        assertEquals("Plato B", waiter2.getCurrentOrder());
    }

    @Test
    void multipleClientsWithDifferentWaiters() {
        Client client2 = new Client("Juan", waiter);
        client.requestDish("Plato 1");
        client2.requestDish("Plato 2");

        assertEquals("Lucía", client.getName());
        assertEquals("Juan", client2.getName());
    }

    @Test
    void kitchenPrepareAndServe() {
        String prepared = kitchen.prepareDish("Arroz con Pollo");
        assertNotNull(prepared);
        assertTrue(prepared.contains("Arroz con Pollo"));
    }

    @Test
    void waiterTakesOrderAndClientRequests() {
        client.requestDish("Sushi");
        waiter.takeOrder("Sushi");
        assertEquals("Sushi", waiter.getCurrentOrder());
    }

    @Test
    void kitchenSpecialty() {
        Kitchen kitchenEspecial = new Kitchen("Marco", 2, "Japonesa");
        Waiter waiterEspecial = new Waiter("Ana", kitchenEspecial);

        waiterEspecial.takeOrder("Sushi Premium");
        assertEquals("Sushi Premium", waiterEspecial.getCurrentOrder());

        String dish = kitchenEspecial.prepareDish("Sushi Premium");
        assertNotNull(dish);
    }

    @Test
    void clientAndWaiterInteraction() {
        client.requestDish("Postre");
        waiter.takeOrder("Postre");
        
        assertEquals("Postre", waiter.getCurrentOrder());
        assertEquals("Lucía", client.getName());
    }

    @Test
    void multipleOrdersSequence() {
        waiter.takeOrder("Entrada");
        assertEquals("Entrada", waiter.getCurrentOrder());
        
        waiter.takeOrder("Plato Principal");
        assertEquals("Plato Principal", waiter.getCurrentOrder());
        
        waiter.takeOrder("Postre");
        assertEquals("Postre", waiter.getCurrentOrder());
    }

    @Test
    void kitchenPreparationMultipleTimes() {
        for (int i = 0; i < 5; i++) {
            String dish = kitchen.prepareDish("Plato " + i);
            assertNotNull(dish);
            assertTrue(dish.contains("Plato " + i));
        }
    }
}
