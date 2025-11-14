package Junit.FactoryMethod.modelo;

import Creacionales.FactoryMethod.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderFactoryUnitTest {

    private KitchenService kitchen;

    @BeforeEach
    void setUp() {
        kitchen = Mockito.mock(KitchenService.class);
    }

    @Test
    void dineInOrder_prepare_delegatesToKitchen() {
        DineInOrderCreator creator = new DineInOrderCreator(kitchen);

        Order order = creator.createOrder("Mesa 1");
        order.addItem("Arroz", 10.0);
        order.prepare();

        verify(kitchen, times(1)).prepareOrder(order);
        assertEquals(10.0, order.getPrice(), 0.001);
        assertEquals("DINE_IN", order.getType());
    }

    @Test
    void takeOutOrder_prepare_delegatesToKitchen() {
        TakeAwayOrderCreator creator = new TakeAwayOrderCreator(kitchen, null);

        Order order = creator.createOrder("Cliente 1");
        order.addItem("Pizza", 15.0);
        order.prepare();

        verify(kitchen, times(1)).prepareOrder(order);
        assertEquals(15.0, order.getPrice(), 0.001);
        assertEquals("TAKEAWAY", order.getType());
    }

    @Test
    void deliveryOrder_prepare_delegatesToKitchen() {
        DeliveryOrderCreator creator = new DeliveryOrderCreator(kitchen, null);

        Order order = creator.createOrder("Calle Principal 123");
        order.addItem("Hamburguesa", 12.0);
        order.prepare();

        verify(kitchen, times(1)).prepareOrder(order);
        assertEquals(12.0, order.getPrice(), 0.001);
        assertEquals("DELIVERY", order.getType());
    }

    @Test
    void order_multipleItems_calculatesTotalPrice() {
        DineInOrderCreator creator = new DineInOrderCreator(kitchen);

        Order order = creator.createOrder("Mesa 2");
        order.addItem("Sopa", 5.0);
        order.addItem("Pollo", 15.0);
        order.addItem("Postre", 8.0);

        assertEquals(28.0, order.getPrice(), 0.001);
    }

    @Test
    void dineInOrder_multipleItems() {
        DineInOrderCreator creator = new DineInOrderCreator(kitchen);
        Order order = creator.createOrder("Mesa 5");

        order.addItem("Entrada", 8.0);
        order.addItem("Plato Principal", 20.0);
        order.addItem("Bebida", 3.0);

        assertEquals(31.0, order.getPrice(), 0.001);
        assertEquals("DINE_IN", order.getType());
    }

    @Test
    void takeAwayOrder_multipleItems() {
        TakeAwayOrderCreator creator = new TakeAwayOrderCreator(kitchen, null);
        Order order = creator.createOrder("Cliente A");

        order.addItem("Hamburguesa", 10.0);
        order.addItem("Papas Fritas", 4.0);
        order.addItem("Refresco", 2.0);

        assertEquals(16.0, order.getPrice(), 0.001);
        assertEquals("TAKEAWAY", order.getType());
    }

    @Test
    void deliveryOrder_multipleItems() {
        DeliveryOrderCreator creator = new DeliveryOrderCreator(kitchen, null);
        Order order = creator.createOrder("Dirección 123");

        order.addItem("Pizza Grande", 20.0);
        order.addItem("Adicional", 5.0);

        assertEquals(25.0, order.getPrice(), 0.001);
        assertEquals("DELIVERY", order.getType());
    }

    @Test
    void dineInOrder_singleItem() {
        DineInOrderCreator creator = new DineInOrderCreator(kitchen);
        Order order = creator.createOrder("Mesa 10");

        order.addItem("Café", 3.0);

        assertEquals(3.0, order.getPrice(), 0.001);
        assertEquals("DINE_IN", order.getType());
    }

    @Test
    void takeAwayOrder_singleItem() {
        TakeAwayOrderCreator creator = new TakeAwayOrderCreator(kitchen, null);
        Order order = creator.createOrder("Cliente B");

        order.addItem("Sandwich", 7.0);

        assertEquals(7.0, order.getPrice(), 0.001);
        assertEquals("TAKEAWAY", order.getType());
    }

    @Test
    void deliveryOrder_singleItem() {
        DeliveryOrderCreator creator = new DeliveryOrderCreator(kitchen, null);
        Order order = creator.createOrder("Dirección 456");

        order.addItem("Tacos", 10.0);

        assertEquals(10.0, order.getPrice(), 0.001);
        assertEquals("DELIVERY", order.getType());
    }

    @Test
    void multipleOrders_allPrepared() {
        DineInOrderCreator dineInCreator = new DineInOrderCreator(kitchen);
        TakeAwayOrderCreator takeAwayCreator = new TakeAwayOrderCreator(kitchen, null);
        DeliveryOrderCreator deliveryCreator = new DeliveryOrderCreator(kitchen, null);

        Order order1 = dineInCreator.createOrder("Mesa 1");
        Order order2 = takeAwayCreator.createOrder("Cliente 1");
        Order order3 = deliveryCreator.createOrder("Dirección 1");

        order1.addItem("Item 1", 10.0);
        order2.addItem("Item 2", 15.0);
        order3.addItem("Item 3", 20.0);

        order1.prepare();
        order2.prepare();
        order3.prepare();

        verify(kitchen, times(3)).prepareOrder(any());
    }

    @Test
    void order_zeroPrice() {
        DineInOrderCreator creator = new DineInOrderCreator(kitchen);
        Order order = creator.createOrder("Mesa 3");

        assertEquals(0.0, order.getPrice(), 0.001);
    }

    @Test
    void order_addMultipleItemsSequentially() {
        TakeAwayOrderCreator creator = new TakeAwayOrderCreator(kitchen, null);
        Order order = creator.createOrder("Cliente C");

        double[] prices = {5.0, 8.0, 3.0, 12.0};
        for (double price : prices) {
            order.addItem("Item", price);
        }

        assertEquals(28.0, order.getPrice(), 0.001);
    }

    @Test
    void creatorShouldCreateCorrectOrderType() {
        DineInOrderCreator dineCreator = new DineInOrderCreator(kitchen);
        Order dineOrder = dineCreator.createOrder("Mesa");
        assertEquals("DINE_IN", dineOrder.getType());

        TakeAwayOrderCreator takeCreator = new TakeAwayOrderCreator(kitchen, null);
        Order takeOrder = takeCreator.createOrder("Client");
        assertEquals("TAKEAWAY", takeOrder.getType());

        DeliveryOrderCreator deliveryCreator = new DeliveryOrderCreator(kitchen, null);
        Order deliveryOrder = deliveryCreator.createOrder("Address");
        assertEquals("DELIVERY", deliveryOrder.getType());
    }

    @Test
    void dineInOrder_deliver_printsCorrectly() {
        DineInOrderCreator creator = new DineInOrderCreator(kitchen);
        Order order = creator.createOrder("Mesa 7");
        order.addItem("Pescado", 20.0);
        assertDoesNotThrow(order::deliver);
    }

    @Test
    void takeAwayOrder_deliver_printsPickupCode() {
        TakeAwayOrderCreator creator = new TakeAwayOrderCreator(kitchen, "ABC123");
        Order order = creator.createOrder("Cliente X");
        order.addItem("Tacos", 8.0);
        assertDoesNotThrow(order::deliver);
    }

    @Test
    void deliveryOrder_deliver_printsAddress() {
        DeliveryOrderCreator creator = new DeliveryOrderCreator(kitchen, "Calle 456");
        Order order = creator.createOrder("Usuario Y");
        order.addItem("Burger", 12.0);
        assertDoesNotThrow(order::deliver);
    }

    @Test
    void dineInOrder_toString_containsCorrectInfo() {
        DineInOrderCreator creator = new DineInOrderCreator(kitchen);
        Order order = creator.createOrder("Mesa 3");
        order.addItem("Sopa", 5.0);
        order.addItem("Carne", 18.0);
        String str = order.toString();
        assertTrue(str.contains("DineInOrder"));
        assertTrue(str.contains("Mesa 3"));
        assertTrue(str.contains("23.0"));
    }

    @Test
    void takeAwayOrder_toString_containsPickupCode() {
        TakeAwayOrderCreator creator = new TakeAwayOrderCreator(kitchen, "XYZ789");
        Order order = creator.createOrder("Cliente Z");
        order.addItem("Pizza", 15.0);
        String str = order.toString();
        assertTrue(str.contains("TakeAwayOrder"));
        assertTrue(str.contains("XYZ789"));
    }

    @Test
    void deliveryOrder_toString_containsAddress() {
        DeliveryOrderCreator creator = new DeliveryOrderCreator(kitchen, "Av. Principal 999");
        Order order = creator.createOrder("Cliente W");
        order.addItem("Pollo", 16.0);
        String str = order.toString();
        assertTrue(str.contains("DeliveryOrder"));
        assertTrue(str.contains("Av. Principal 999"));
    }

    @Test
    void takeAwayOrder_getName_includesPickupCode() {
        TakeAwayOrderCreator creator = new TakeAwayOrderCreator(kitchen, "PICKUP5");
        Order order = creator.createOrder("John Doe");
        String name = order.getName();
        assertTrue(name.contains("John Doe"));
        assertTrue(name.contains("pickup"));
        assertTrue(name.contains("PICKUP5"));
    }

    @Test
    void deliveryOrder_getName_includesAddress() {
        DeliveryOrderCreator creator = new DeliveryOrderCreator(kitchen, "Calle Azul 100");
        Order order = creator.createOrder("Jane Smith");
        String name = order.getName();
        assertTrue(name.contains("Jane Smith"));
        assertTrue(name.contains("delivery to"));
        assertTrue(name.contains("Calle Azul 100"));
    }

    @Test
    void order_getItems_returnsImmutableCopy() {
        DineInOrderCreator creator = new DineInOrderCreator(kitchen);
        Order order = creator.createOrder("Mesa 11");
        order.addItem("Item1", 10.0);
        List<String> items1 = order.getItems();
        order.addItem("Item2", 5.0);
        List<String> items2 = order.getItems();
        assertEquals(1, items1.size());
        assertEquals(2, items2.size());
    }

    @Test
    void multipleOrders_independentTotals() {
        DineInOrderCreator dineCreator = new DineInOrderCreator(kitchen);
        TakeAwayOrderCreator takeCreator = new TakeAwayOrderCreator(kitchen, "CODE1");
        DeliveryOrderCreator deliveryCreator = new DeliveryOrderCreator(kitchen, "Addr1");

        Order order1 = dineCreator.createOrder("Mesa 1");
        Order order2 = takeCreator.createOrder("Client 1");
        Order order3 = deliveryCreator.createOrder("User 1");

        order1.addItem("Plato A", 15.0);
        order1.addItem("Bebida A", 2.0);

        order2.addItem("Plato B", 20.0);

        order3.addItem("Plato C", 25.0);
        order3.addItem("Bebida C", 3.0);
        order3.addItem("Extra C", 5.0);

        assertEquals(17.0, order1.getPrice(), 0.001);
        assertEquals(20.0, order2.getPrice(), 0.001);
        assertEquals(33.0, order3.getPrice(), 0.001);
    }

    


}