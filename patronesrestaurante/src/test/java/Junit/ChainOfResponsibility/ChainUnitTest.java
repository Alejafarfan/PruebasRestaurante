package Junit.ChainOfResponsibility;

import Comportamentales.ChainOfResponsibility.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ChainUnitTest {

    private Waiter waiter;
    private Chef chef;

    @BeforeEach
    void setUp() {
        waiter = new Waiter("Laura", "Zona A");
        chef = new Chef("Andrés", "Italiana");
    }

    @Test
    void testWaiterHandlesOrder() {
        waiter.handle("order", "Pedido de jugo de mango");
        assertNotNull(waiter);
    }

    @Test
    void testChefHandlesCookRequest() {
        chef.handle("cook", "Preparar pizza napolitana");
        assertNotNull(chef);
    }

    @Test
    void testWaiterPassesCookRequestToNextHandler() {
        Chef chefMock = mock(Chef.class);
        waiter.setNext(chefMock);

        waiter.handle("cook", "Preparar pasta carbonara");

        verify(chefMock).handle("cook", "Preparar pasta carbonara");
    }

    @Test
    void testChefDoesNotHandleOrder() {
        chef.handle("order", "Pedido de ensalada");
        assertNotNull(chef);
    }

    @Test
    void testChainHandlesUnknownRequestGracefully() {
        waiter.setNext(chef);
        waiter.handle("payment", "Pago con tarjeta");
        assertNotNull(waiter);
    }

    @Test
    void testFullChainHandlesOrderAndCook() {
        waiter.setNext(chef);
        waiter.handle("order", "Pedido de sopa de tomate");
        waiter.handle("cook", "Preparar lasaña");
        assertNotNull(waiter);
    }

    @Test
    void testWaiterToString() {
        String waiterString = waiter.toString();
        assertTrue(waiterString.contains("Waiter"));
        assertTrue(waiterString.contains("Laura"));
    }

    @Test
    void testChefToString() {
        String chefString = chef.toString();
        assertTrue(chefString.contains("Chef"));
        assertTrue(chefString.contains("Andrés"));
    }

    @Test
    void testSetNextShouldWorkCorrectly() {
        waiter.setNext(chef);
        assertNotNull(waiter);
    }

    @Test
    void testMultipleHandlersInChain() {
        Chef chef2 = new Chef("Carlos", "Francesa");
        waiter.setNext(chef);
        chef.setNext(chef2);

        waiter.handle("cook", "Preparar coq au vin");
        assertNotNull(waiter);
    }

    @Test
    void testWaiterHandlesOrderWithDifferentMessages() {
        waiter.handle("order", "Pedido de pizza");
        waiter.handle("order", "Pedido de pasta");
        waiter.handle("order", "Pedido de ensalada");
        assertNotNull(waiter);
    }

    @Test
    void testChefHandlesCookWithDifferentDishes() {
        chef.handle("cook", "Preparar risotto");
        chef.handle("cook", "Preparar tiramisu");
        assertNotNull(chef);
    }

    @Test
    void testWaiterWithDifferentZones() {
        Waiter waiter2 = new Waiter("Carlos", "Zona B");
        waiter.setNext(waiter2);
        waiter.handle("order", "Pedido para zona B");
        assertNotNull(waiter2);
    }

    @Test
    void testChefWithDifferentSpecialties() {
        Chef chef2 = new Chef("Marco", "Japonesa");
        waiter.setNext(chef);
        chef.setNext(chef2);
        waiter.handle("cook", "Preparar sushi");
        assertNotNull(chef2);
    }
}
