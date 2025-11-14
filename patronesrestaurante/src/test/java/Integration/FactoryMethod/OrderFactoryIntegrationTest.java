package Integration.FactoryMethod;

import Creacionales.FactoryMethod.controlador.OrderFactoryController;
import Creacionales.FactoryMethod.modelo.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class OrderFactoryIntegrationTest {

    @Test
    void placeOrder_flowWorksAndTotalsAreCorrect() {
        KitchenService kitchen = order -> {
            // Simulate order preparation
        };
        OrderCreator creator = new DineInOrderCreator(kitchen);
        OrderFactoryController controller = new OrderFactoryController(creator);

        Order order = controller.placeOrder("Mesa 5");

        assertNotNull(order);
        assertEquals("DINE_IN", order.getType());
        assertEquals(2, order.getItems().size());
        assertEquals(25.0, order.getPrice(), 0.001);
    }

    @Test
    void placeOrder_takeaway_triggersKitchenAndHasPositiveTotals() {
        AtomicBoolean kitchenCalled = new AtomicBoolean(false);
        KitchenService kitchen = order -> {
            kitchenCalled.set(true);
        };

        OrderCreator creator = new TakeAwayOrderCreator(kitchen, null);
        OrderFactoryController controller = new OrderFactoryController(creator);

        Order order = controller.placeOrder("Para llevar");

        assertNotNull(order);
        assertTrue(kitchenCalled.get(), "Kitchen should be invoked for takeaway orders");
        assertEquals("TAKEAWAY", order.getType());
        assertTrue(order.getItems().size() >= 1, "Takeaway order should contain at least one item");
        assertTrue(order.getPrice() > 0.0, "Takeaway order price should be positive");
    }

    @Test
    void placeOrder_delivery_and_dineIn_areIndependentAndHaveCorrectTypes() {
        AtomicBoolean kitchenDineInCalled = new AtomicBoolean(false);
        AtomicBoolean kitchenDeliveryCalled = new AtomicBoolean(false);

        KitchenService dineInKitchen = order -> kitchenDineInCalled.set(true);
        KitchenService deliveryKitchen = order -> kitchenDeliveryCalled.set(true);

        OrderCreator dineInCreator = new DineInOrderCreator(dineInKitchen);
        OrderCreator deliveryCreator = new DeliveryOrderCreator(deliveryKitchen, null);

        OrderFactoryController dineInController = new OrderFactoryController(dineInCreator);
        OrderFactoryController deliveryController = new OrderFactoryController(deliveryCreator);

        Order dineInOrder = dineInController.placeOrder("Mesa 1");
        Order deliveryOrder = deliveryController.placeOrder("Calle Falsa 123");

        assertNotNull(dineInOrder);
        assertNotNull(deliveryOrder);
        assertTrue(kitchenDineInCalled.get(), "Dine-in kitchen should be invoked");
        assertTrue(kitchenDeliveryCalled.get(), "Delivery kitchen should be invoked");
        assertEquals("DINE_IN", dineInOrder.getType());
        assertEquals("DELIVERY", deliveryOrder.getType());
        assertNotSame(dineInOrder, deliveryOrder, "Orders from different creators should be distinct");
        assertTrue(dineInOrder.getItems().size() >= 1);
        assertTrue(deliveryOrder.getItems().size() >= 1);
        assertTrue(dineInOrder.getPrice() > 0.0);
        assertTrue(deliveryOrder.getPrice() > 0.0);
    }

    @Test
    void placeOrder_dineIn_withMultipleItems_andDeliver() {
        KitchenService kitchen = order -> System.out.println("Kitchen prepared: " + order.getName());
        OrderCreator creator = new DineInOrderCreator(kitchen);
        OrderFactoryController controller = new OrderFactoryController(creator);

        Order order = controller.placeOrder("Mesa Premium");
        order.prepare();
        order.deliver();

        assertEquals("DINE_IN", order.getType());
        assertTrue(order.getPrice() > 0.0);
        assertEquals(2, order.getItems().size());
    }

    @Test
    void placeOrder_delivery_withAddressAndDeliver() {
        KitchenService kitchen = order -> System.out.println("Preparing delivery: " + order.getName());
        OrderCreator creator = new DeliveryOrderCreator(kitchen, "Av. Reforma 500");
        OrderFactoryController controller = new OrderFactoryController(creator);

        Order order = controller.placeOrder("Cliente Delivery");
        order.prepare();
        order.deliver();

        assertEquals("DELIVERY", order.getType());
        assertEquals(2, order.getItems().size());
        assertTrue(order.getPrice() > 0.0);
        assertTrue(order.getName().contains("Av. Reforma 500"));
    }

    @Test
    void placeOrder_takeAway_withPickupCodeAndDeliver() {
        KitchenService kitchen = order -> System.out.println("Preparing takeaway: " + order.getName());
        OrderCreator creator = new TakeAwayOrderCreator(kitchen, "RETIRO99");
        OrderFactoryController controller = new OrderFactoryController(creator);

        Order order = controller.placeOrder("Cliente Retiro");
        order.prepare();
        order.deliver();

        assertEquals("TAKEAWAY", order.getType());
        assertEquals(2, order.getItems().size());
        assertTrue(order.getPrice() > 0.0);
        assertTrue(order.getName().contains("RETIRO99"));
    }

    @Test
    void controllerCanChangeCreators_DynamicallyAndProcessOrders() {
        KitchenService kitchen = order -> { /* no-op */ };
        OrderFactoryController controller = new OrderFactoryController(new DineInOrderCreator(kitchen));

        // First order: dine-in
        Order order1 = controller.placeOrder("Mesa 1");
        assertEquals("DINE_IN", order1.getType());
        assertEquals(2, order1.getItems().size());

        // Change creator to delivery
        OrderCreator deliveryCreator = new DeliveryOrderCreator(kitchen, "Calle Nueva");
        controller = new OrderFactoryController(deliveryCreator);

        Order order2 = controller.placeOrder("Cliente 2");
        assertEquals("DELIVERY", order2.getType());
        assertEquals(2, order2.getItems().size());

        // Both should have different types
        assertNotEquals(order1.getType(), order2.getType());
    }

    @Test
    void kitchen_receivesCorrectNumberOfCallsAcrossMultipleOrders() {
        AtomicInteger kitchenCallCount = new AtomicInteger(0);
        KitchenService kitchen = order -> kitchenCallCount.incrementAndGet();

        OrderCreator creator = new DineInOrderCreator(kitchen);
        OrderFactoryController controller = new OrderFactoryController(creator);

        // Controller.placeOrder() calls prepare() automatically, so kitchen is called once per placeOrder
        controller.placeOrder("Mesa 1");  // Kitchen called here
        controller.placeOrder("Mesa 2");  // Kitchen called here
        controller.placeOrder("Mesa 3");  // Kitchen called here

        // Since placeOrder() already calls prepare(), we expect 3 calls (one per placeOrder)
        assertEquals(3, kitchenCallCount.get(), "Kitchen should be called once per controller.placeOrder()");
    }

    @Test
    void ordersOfDifferentTypes_areCreatedWithCorrectDefaults() {
        KitchenService kitchen = order -> { /* no-op */ };

        DineInOrder dineInOrder = (DineInOrder) new DineInOrderCreator(kitchen).createOrder("Table1");
        TakeAwayOrder takeAwayOrder = (TakeAwayOrder) new TakeAwayOrderCreator(kitchen, "CODE1").createOrder("Client1");
        DeliveryOrder deliveryOrder = (DeliveryOrder) new DeliveryOrderCreator(kitchen, "Address1").createOrder("User1");

        assertNotNull(dineInOrder);
        assertNotNull(takeAwayOrder);
        assertNotNull(deliveryOrder);

        assertEquals("DINE_IN", dineInOrder.getType());
        assertEquals("TAKEAWAY", takeAwayOrder.getType());
        assertEquals("DELIVERY", deliveryOrder.getType());

        assertEquals(0.0, dineInOrder.getPrice(), 0.001);
        assertEquals(0.0, takeAwayOrder.getPrice(), 0.001);
        assertEquals(0.0, deliveryOrder.getPrice(), 0.001);
    }
}