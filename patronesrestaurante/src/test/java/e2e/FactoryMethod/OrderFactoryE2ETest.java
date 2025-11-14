package e2e.FactoryMethod;

import Creacionales.FactoryMethod.controlador.OrderFactoryController;
import Creacionales.FactoryMethod.modelo.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderFactoryE2ETest {

    @Test
    void fullFlow_createPrepareDeliver() {
        KitchenService kitchen = order -> System.out.println("Chef preparando: " + order.getName());
        OrderCreator creator = new DeliveryOrderCreator(kitchen, "Calle 123");
        OrderFactoryController controller = new OrderFactoryController(creator);

        Order order = controller.placeOrder("Cliente Juan");
        order.deliver();

        assertEquals("DELIVERY", order.getType());
        assertTrue(order.getPrice() > 0.0);
        assertFalse(order.getItems().isEmpty());
    }

    @Test
    void fullFlow_dineInWithPrepareAndDeliver() {
        KitchenService kitchen = order -> System.out.println("Chef está preparando en la cocina: " + order.getName());
        OrderCreator creator = new DineInOrderCreator(kitchen);
        OrderFactoryController controller = new OrderFactoryController(creator);

        Order order = controller.placeOrder("Cliente Mesa 5");
        order.prepare();
        order.deliver();

        assertEquals("DINE_IN", order.getType());
        assertTrue(order.getPrice() > 0.0);
        assertTrue(order.getItems().size() >= 1);
        assertTrue(order.getName().contains("Cliente Mesa 5"));
    }

    @Test
    void fullFlow_takeAwayWithCodeAndDeliver() {
        KitchenService kitchen = order -> System.out.println("Preparando para retiro: " + order.getName());
        OrderCreator creator = new TakeAwayOrderCreator(kitchen, "RETIRO123");
        OrderFactoryController controller = new OrderFactoryController(creator);

        Order order = controller.placeOrder("Cliente Retiro");
        order.prepare();
        order.deliver();

        assertEquals("TAKEAWAY", order.getType());
        assertTrue(order.getPrice() > 0.0);
        assertFalse(order.getItems().isEmpty());
        assertTrue(order.getName().contains("RETIRO123"));
    }

    @Test
    void fullFlow_multipleOrdersOfDifferentTypes() {
        KitchenService kitchen = order -> System.out.println("Preparando: " + order.getName());

        // Order 1: Dine-in
        OrderCreator creator1 = new DineInOrderCreator(kitchen);
        OrderFactoryController controller1 = new OrderFactoryController(creator1);
        Order order1 = controller1.placeOrder("Mesa 1");
        order1.prepare();
        order1.deliver();

        // Order 2: Delivery
        OrderCreator creator2 = new DeliveryOrderCreator(kitchen, "Calle A");
        OrderFactoryController controller2 = new OrderFactoryController(creator2);
        Order order2 = controller2.placeOrder("Cliente Entrega");
        order2.prepare();
        order2.deliver();

        // Order 3: Takeaway
        OrderCreator creator3 = new TakeAwayOrderCreator(kitchen, "CODE789");
        OrderFactoryController controller3 = new OrderFactoryController(creator3);
        Order order3 = controller3.placeOrder("Cliente Retiro");
        order3.prepare();
        order3.deliver();

        assertEquals("DINE_IN", order1.getType());
        assertEquals("DELIVERY", order2.getType());
        assertEquals("TAKEAWAY", order3.getType());

        assertTrue(order1.getPrice() > 0.0);
        assertTrue(order2.getPrice() > 0.0);
        assertTrue(order3.getPrice() > 0.0);
    }

    @Test
    void fullFlow_orderWithAdditionalItems() {
        KitchenService kitchen = order -> System.out.println("Preparando orden con múltiples items: " + order.getName());
        OrderCreator creator = new DineInOrderCreator(kitchen);
        OrderFactoryController controller = new OrderFactoryController(creator);

        Order order = controller.placeOrder("Mesa Especial");
        // Controller already added "Plato principal" (20.0) + "Bebida" (5.0) = 25.0
        // Now add more items
        order.addItem("Entrada", 8.0);
        order.addItem("Postre", 7.0);
        order.deliver();

        // Total: 25.0 (from controller) + 8.0 + 7.0 = 40.0
        assertEquals(40.0, order.getPrice(), 0.001);
        assertEquals(4, order.getItems().size());
    }

    @Test
    void fullFlow_deliveryOrderWithToStringAndGetters() {
        KitchenService kitchen = order -> System.out.println("Preparando: " + order.toString());
        OrderCreator creator = new DeliveryOrderCreator(kitchen, "Calle Principal 500");
        Order order = creator.createOrder("Cliente VIP");

        String orderStr = order.toString();
        assertTrue(orderStr.contains("DeliveryOrder"));
        assertTrue(orderStr.contains("Calle Principal 500"));

        String name = order.getName();
        assertTrue(name.contains("Cliente VIP"));
        assertTrue(name.contains("delivery to"));
        assertTrue(name.contains("Calle Principal 500"));
    }

    @Test
    void fullFlow_takeAwayOrderWithToStringAndGetters() {
        KitchenService kitchen = order -> System.out.println("Preparando: " + order.toString());
        OrderCreator creator = new TakeAwayOrderCreator(kitchen, "PICKUP456");
        Order order = creator.createOrder("Cliente Express");

        String orderStr = order.toString();
        assertTrue(orderStr.contains("TakeAwayOrder"));
        assertTrue(orderStr.contains("PICKUP456"));

        String name = order.getName();
        assertTrue(name.contains("Cliente Express"));
        assertTrue(name.contains("pickup"));
        assertTrue(name.contains("PICKUP456"));
    }
}

