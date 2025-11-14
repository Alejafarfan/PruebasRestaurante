package e2e;

import Creacionales.FactoryMethod.controlador.OrderFactoryController;
import Creacionales.FactoryMethod.modelo.DineInOrderCreator;
import Creacionales.FactoryMethod.modelo.KitchenService;
import Creacionales.FactoryMethod.modelo.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryE2ETest {

    @Test
    void factoryFullFlow_placeAndPrepare() {
        KitchenService kitchen = order -> System.out.println("Preparing: " + order.getName());
        OrderFactoryController controller = new OrderFactoryController(new DineInOrderCreator(kitchen));

        Order order = controller.placeOrder("E2E Cliente");
        assertEquals("DINE_IN", order.getType());
        assertEquals(25.0, order.getPrice(), 0.001);
    }
}