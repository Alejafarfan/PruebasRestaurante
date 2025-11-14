package e2e.FactoryMethod;

import Creacionales.FactoryMethod.controlador.OrderFactoryController;
import Creacionales.FactoryMethod.modelo.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryConsoleE2ETest {

    @Test
    void factoryConsole_dineInCreatesOrder() {
        KitchenService kitchen = order -> System.out.println("Preparando: " + order.getName());
        OrderCreator creator = new DineInOrderCreator(kitchen);
        OrderFactoryController controller = new OrderFactoryController(creator);
        
        Order order = controller.placeOrder("Mesa DineIn");
        
        assertEquals("DINE_IN", order.getType());
        assertEquals(2, order.getItems().size());
        assertEquals(25.0, order.getPrice(), 0.001);
    }

    @Test
    void factoryConsole_deliveryCreatesOrder() {
        KitchenService kitchen = order -> System.out.println("Preparando entrega: " + order.getName());
        OrderCreator creator = new DeliveryOrderCreator(kitchen, "Calle Principal 100");
        OrderFactoryController controller = new OrderFactoryController(creator);
        
        Order order = controller.placeOrder("Cliente Entrega");
        order.deliver();
        
        assertEquals("DELIVERY", order.getType());
        assertEquals(2, order.getItems().size());
        assertEquals(25.0, order.getPrice(), 0.001);
    }

    @Test
    void factoryConsole_takeAwayCreatesOrder() {
        KitchenService kitchen = order -> System.out.println("Preparando retiro: " + order.getName());
        OrderCreator creator = new TakeAwayOrderCreator(kitchen, "CODIGO123");
        OrderFactoryController controller = new OrderFactoryController(creator);
        
        Order order = controller.placeOrder("Cliente Retiro");
        order.deliver();
        
        assertEquals("TAKEAWAY", order.getType());
        assertEquals(2, order.getItems().size());
        assertEquals(25.0, order.getPrice(), 0.001);
    }
}
