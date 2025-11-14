package e2e;

import Comportamentales.Command.controlador.CommandController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandE2ETest {

    @Test
    void commandFullFlow_registerAndPay() {
        CommandController controller = new CommandController();

        // register an order via controller
        controller.clienteSolicita("registrar", "E2E Pizza");
        assertTrue(controller.getSystem().getActiveOrders().contains("E2E Pizza"));

        // process payment (Client.requestAction uses a fixed amount of 20000)
        controller.clienteSolicita("pagar", "");
        assertTrue(controller.getSystem().getTotalSales() >= 20000);
    }
}