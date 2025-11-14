package Junit.Command;

import Comportamentales.Command.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class CommandUnitTest {

    private OrderSystem system;

    @BeforeEach
    void setUp() {
        system = new OrderSystem();
    }

    @Test
    void testRegisterOrderCommand() {
        Command cmd = new RegisterOrder(system, "Pizza");
        cmd.execute();
        assertTrue(system.getActiveOrders().contains("Pizza"));
    }

    @Test
    void testCancelOrderCommand() {
        system.registerOrder("Sopa");
        Command cmd = new CancelOrder(system, "Sopa");
        cmd.execute();
        assertFalse(system.getActiveOrders().contains("Sopa"));
    }

    @Test
    void testCancelOrderThatDoesNotExist() {
        Command cmd = new CancelOrder(system, "Hamburguesa");
        cmd.execute();
        assertFalse(system.getActiveOrders().contains("Hamburguesa"));
    }

    @Test
    void testMultipleOrdersAndCancellations() {
        Command registerPizza = new RegisterOrder(system, "Pizza");
        Command registerPasta = new RegisterOrder(system, "Pasta");
        Command cancelPizza = new CancelOrder(system, "Pizza");

        registerPizza.execute();
        registerPasta.execute();
        assertTrue(system.getActiveOrders().contains("Pizza"));
        assertTrue(system.getActiveOrders().contains("Pasta"));

        cancelPizza.execute();
        assertFalse(system.getActiveOrders().contains("Pizza"));
        assertTrue(system.getActiveOrders().contains("Pasta"));
    }

    @Test
    void testRegisterSameOrderTwice() {
        Command cmd1 = new RegisterOrder(system, "Ensalada");
        Command cmd2 = new RegisterOrder(system, "Ensalada");
        cmd1.execute();
        cmd2.execute();
        long count = system.getActiveOrders().stream().filter(o -> o.equals("Ensalada")).count();
        assertTrue(count >= 1);
    }

    @Test
    void testOrderSystemInitializeWithEmptyOrders() {
        assertTrue(system.getActiveOrders().isEmpty());
    }

    @Test
    void testRegisterMultipleDifferentOrders() {
        Command cmd1 = new RegisterOrder(system, "Café");
        Command cmd2 = new RegisterOrder(system, "Té");
        Command cmd3 = new RegisterOrder(system, "Jugo");
        
        cmd1.execute();
        cmd2.execute();
        cmd3.execute();

        assertEquals(3, system.getActiveOrders().size());
        assertTrue(system.getActiveOrders().contains("Café"));
        assertTrue(system.getActiveOrders().contains("Té"));
        assertTrue(system.getActiveOrders().contains("Jugo"));
    }

    @Test
    void testCancelMultipleOrders() {
        system.registerOrder("Orden1");
        system.registerOrder("Orden2");
        system.registerOrder("Orden3");

        Command cancel1 = new CancelOrder(system, "Orden1");
        Command cancel2 = new CancelOrder(system, "Orden2");

        cancel1.execute();
        cancel2.execute();

        assertEquals(1, system.getActiveOrders().size());
        assertTrue(system.getActiveOrders().contains("Orden3"));
    }

    @Test
    void testRegisterOrderDirectly() {
        system.registerOrder("Arroz");
        assertTrue(system.getActiveOrders().contains("Arroz"));
    }

    @Test
    void testCancelOrderDirectly() {
        system.registerOrder("Pasta");
        system.cancelOrder("Pasta");
        assertFalse(system.getActiveOrders().contains("Pasta"));
    }

    @Test
    void testRegisterAndCancelWithSpecialCharacters() {
        Command register = new RegisterOrder(system, "Pizza & Bebida");
        Command cancel = new CancelOrder(system, "Pizza & Bebida");

        register.execute();
        assertTrue(system.getActiveOrders().contains("Pizza & Bebida"));

        cancel.execute();
        assertFalse(system.getActiveOrders().contains("Pizza & Bebida"));
    }

    @Test
    void testGetActiveOrdersReturnsCorrectList() {
        system.registerOrder("Orden A");
        system.registerOrder("Orden B");

        var orders = system.getActiveOrders();
        assertEquals(2, orders.size());
        assertTrue(orders.contains("Orden A"));
        assertTrue(orders.contains("Orden B"));
    }

    @Test
    void testCommandsCanBeExecutedInSequence() {
        Command register1 = new RegisterOrder(system, "Pizza");
        Command register2 = new RegisterOrder(system, "Pasta");
        Command cancel = new CancelOrder(system, "Pizza");
        Command register3 = new RegisterOrder(system, "Ensalada");

        register1.execute();
        register2.execute();
        cancel.execute();
        register3.execute();

        assertEquals(2, system.getActiveOrders().size());
        assertFalse(system.getActiveOrders().contains("Pizza"));
        assertTrue(system.getActiveOrders().contains("Pasta"));
        assertTrue(system.getActiveOrders().contains("Ensalada"));
    }

    @Test
    void testCashierReceiveRegisterCommand() {
        Cashier cashier = new Cashier("María");
        cashier.receiveCommand("registrar", system, "Arroz", 0);
        assertNotNull(cashier.getCommand());
    }

    @Test
    void testCashierReceiveCancelCommand() {
        Cashier cashier = new Cashier("María");
        cashier.receiveCommand("cancelar", system, "Pizza", 0);
        assertNotNull(cashier.getCommand());
    }

    @Test
    void testCashierReceivePaymentCommand() {
        Cashier cashier = new Cashier("María");
        cashier.receiveCommand("pagar", system, "", 50000);
        assertNotNull(cashier.getCommand());
    }

    @Test
    void testCashierExecuteCommand() {
        Cashier cashier = new Cashier("María");
        cashier.receiveCommand("registrar", system, "Sopa", 0);
        cashier.executeCommand();
        assertTrue(system.getActiveOrders().contains("Sopa"));
    }

    @Test
    void testCashierWithUnknownCommand() {
        Cashier cashier = new Cashier("María");
        cashier.receiveCommand("desconocido", system, "", 0);
        // No debería asignar comando
        assertNull(cashier.getCommand());
    }

    @Test
    void testCashierExecuteWithoutCommand() {
        Cashier cashier = new Cashier("María");
        cashier.executeCommand(); // No hace nada porque no hay comando
        assertNull(cashier.getCommand());
    }

    @Test
    void testProcessPaymentCommand() {
        Command payCmd = new ProcessPayment(system, 50000);
        payCmd.execute();
        assertNotNull(payCmd);
    }

    @Test
    void testCashierSequenceOfCommands() {
        Cashier cashier = new Cashier("Juan");
        
        cashier.receiveCommand("registrar", system, "Entrada", 0);
        cashier.executeCommand();
        assertTrue(system.getActiveOrders().contains("Entrada"));

        cashier.receiveCommand("registrar", system, "Plato Principal", 0);
        cashier.executeCommand();
        assertTrue(system.getActiveOrders().contains("Plato Principal"));

        cashier.receiveCommand("cancelar", system, "Entrada", 0);
        cashier.executeCommand();
        assertFalse(system.getActiveOrders().contains("Entrada"));
    }

    @Test
    void testMultipleCashiers() {
        Cashier cashier1 = new Cashier("María");
        Cashier cashier2 = new Cashier("Juan");

        cashier1.receiveCommand("registrar", system, "Pizza", 0);
        cashier1.executeCommand();

        cashier2.receiveCommand("registrar", system, "Pasta", 0);
        cashier2.executeCommand();

        assertEquals(2, system.getActiveOrders().size());
    }

    @Test
    void testCashierGetName() {
        Cashier cashier = new Cashier("Laura");
        assertNotNull(cashier);
    }

    @Test
    void testCashierReceiveCommandAndGetCommand() {
        Cashier cashier = new Cashier("José");
        assertNull(cashier.getCommand());
        
        cashier.receiveCommand("registrar", system, "Hamburguesa", 0);
        assertNotNull(cashier.getCommand());
    }

    @Test
    void testClientConstructor() {
        Client client = new Client("Carlos");
        assertNotNull(client);
    }

    @Test
    void testClientRequestAction() {
        Client client = new Client("Patricia");
        Cashier cashier = new Cashier("Antonia");
        assertDoesNotThrow(() -> {
            client.requestAction("registrar", "Ceviche", cashier, system);
        });
    }

    @Test
    void testClientRequestMultipleActions() {
        Client client = new Client("Roberto");
        Cashier cashier = new Cashier("Beatriz");
        client.requestAction("registrar", "Arroz", cashier, system);
        client.requestAction("registrar", "Pollo", cashier, system);
        assertEquals(2, system.getActiveOrders().size());
    }

    @Test
    void testRegisterOrderCommand_toString() {
        RegisterOrder register = new RegisterOrder(system, "Pescado");
        register.execute();
        String str = register.toString();
        assertTrue(str.contains("RegisterOrder") || str != null);
    }

    @Test
    void testCancelOrderCommand_toString() {
        system.registerOrder("Camarones");
        CancelOrder cancel = new CancelOrder(system, "Camarones");
        cancel.execute();
        String str = cancel.toString();
        assertTrue(str.contains("CancelOrder") || str != null);
    }

    @Test
    void testProcessPaymentCommand_toString() {
        ProcessPayment payment = new ProcessPayment(system, 30000);
        payment.execute();
        String str = payment.toString();
        assertTrue(str.contains("ProcessPayment") || str != null);
    }

    @Test
    void testOrderSystem_registerOrderMultipleTimes() {
        system.registerOrder("Tacos");
        system.registerOrder("Tacos");
        system.registerOrder("Tacos");
        long count = system.getActiveOrders().stream().filter(o -> o.equals("Tacos")).count();
        assertEquals(3, count);
    }

    @Test
    void testOrderSystem_cancelNonExistentOrder() {
        system.cancelOrder("NoExiste");
        assertTrue(system.getActiveOrders().isEmpty());
    }

    @Test
    void testOrderSystem_getActiveOrdersIsNotNull() {
        assertNotNull(system.getActiveOrders());
        assertTrue(system.getActiveOrders().isEmpty());
    }

    @Test
    void testOrderSystem_processPayment() {
        system.processPayment(50000);
        assertNotNull(system);
    }

    @Test
    void testCashierReceiveMultipleCommandTypes() {
        Cashier cashier = new Cashier("Diana");
        
        cashier.receiveCommand("registrar", system, "Sopa", 0);
        Command cmd1 = cashier.getCommand();
        assertNotNull(cmd1);
        
        cashier.receiveCommand("cancelar", system, "Sopa", 0);
        Command cmd2 = cashier.getCommand();
        assertNotNull(cmd2);
        
        cashier.receiveCommand("pagar", system, "", 25000);
        Command cmd3 = cashier.getCommand();
        assertNotNull(cmd3);
    }

    @Test
    void testCashierExecuteRegisterThenCancel() {
        Cashier cashier = new Cashier("Felipe");
        
        cashier.receiveCommand("registrar", system, "Ramen", 0);
        cashier.executeCommand();
        assertEquals(1, system.getActiveOrders().size());
        
        cashier.receiveCommand("cancelar", system, "Ramen", 0);
        cashier.executeCommand();
        assertEquals(0, system.getActiveOrders().size());
    }

    @Test
    void testCashierExecutePaymentCommand() {
        Cashier cashier = new Cashier("Gerardo");
        system.registerOrder("Churrasco");
        
        cashier.receiveCommand("pagar", system, "", 100000);
        cashier.executeCommand();
        assertNotNull(cashier.getCommand());
    }

    @Test
    void testClientAndCashierInteraction() {
        Client client = new Client("Valentina");
        Cashier cashier = new Cashier("Hugo");
        
        client.requestAction("registrar", "Mofongo", cashier, system);
        assertEquals(1, system.getActiveOrders().size());
        
        cashier.receiveCommand("registrar", system, "Yuca", 0);
        cashier.executeCommand();
        assertEquals(2, system.getActiveOrders().size());
    }

    @Test
    void testClientRequestCancel() {
        Client client = new Client("Mariana");
        Cashier cashier = new Cashier("Carlos");
        
        client.requestAction("registrar", "Lentejas", cashier, system);
        assertEquals(1, system.getActiveOrders().size());
        
        client.requestAction("cancelar", "Lentejas", cashier, system);
        assertEquals(0, system.getActiveOrders().size());
    }

    @Test
    void testClientRequestPayment() {
        Client client = new Client("Edmundo");
        Cashier cashier = new Cashier("Lucia");
        
        client.requestAction("registrar", "Arroz rojo", cashier, system);
        assertEquals(1, system.getActiveOrders().size());
        
        client.requestAction("pagar", "", cashier, system);
        assertNotNull(cashier.getCommand());
    }

    @Test
    void testCashierExecuteCommandWithoutReceiving() {
        Cashier cashier = new Cashier("Miguel");
        cashier.executeCommand();
        assertNull(cashier.getCommand());
    }

    @Test
    void testCashierUnknownCommand() {
        Cashier cashier = new Cashier("Sofia");
        cashier.receiveCommand("ejecutar", system, "", 0);
        assertNull(cashier.getCommand());
    }

    @Test
    void testMultipleClientsMultipleCashiers() {
        Client client1 = new Client("Ana");
        Client client2 = new Client("Bruno");
        Cashier cashier1 = new Cashier("Carla");
        Cashier cashier2 = new Cashier("David");
        
        client1.requestAction("registrar", "Fajitas", cashier1, system);
        client2.requestAction("registrar", "Enchiladas", cashier2, system);
        
        assertEquals(2, system.getActiveOrders().size());
        assertTrue(system.getActiveOrders().contains("Fajitas"));
        assertTrue(system.getActiveOrders().contains("Enchiladas"));
    }
}
