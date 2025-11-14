package e2e;

import Comportamentales.ChainOfResponsibility.controlador.ChainController;
import Comportamentales.ChainOfResponsibility.modelo.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChainE2ETest {

    @Test
    void chainFullFlow_clientRequests() {
        ChainController controller = new ChainController();
        controller.ejecutarFlujo();

        Client client = controller.getClient();
        assertNotNull(client);
    }
}