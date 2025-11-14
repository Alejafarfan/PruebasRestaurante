package Junit.Vista;

import Comportamentales.Command.vista.CommandConsole;
import Creacionales.FactoryMethod.vista.FactoryConsole;
import Estructurales.Adapter.vista.AdapterConsole;
import Creacionales.Builder.vista.BuilderConsole;
import Comportamentales.Observer.vista.ObserverConsole;
import Creacionales.Singleton.vista.InventoryConsole;
import Comportamentales.ChainOfResponsibility.vista.ChainConsole;
import Estructurales.Proxy.vista.ProxyConsole;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class VistaMainTests {

    private void withSystemIn(String input, Runnable runnable) {
        InputStream originalIn = System.in;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes())) {
            System.setIn(bais);
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    void commandConsoleShouldExitImmediately() {
        withSystemIn("5\n", () -> assertDoesNotThrow(() -> CommandConsole.main(new String[0])));
    }

    @Test
    void factoryConsoleShouldExitImmediately() {
        withSystemIn("4\n", () -> assertDoesNotThrow(() -> FactoryConsole.main(new String[0])));
    }

    @Test
    void adapterConsoleShouldExitImmediately() {
        withSystemIn("4\n", () -> assertDoesNotThrow(() -> AdapterConsole.main(new String[0])));
    }

    @Test
    void builderConsoleShouldExitImmediately() {
        withSystemIn("4\n", () -> assertDoesNotThrow(() -> BuilderConsole.main(new String[0])));
    }

    @Test
    void observerConsoleShouldExitImmediately() {
        withSystemIn("4\n", () -> assertDoesNotThrow(() -> ObserverConsole.main(new String[0])));
    }

    @Test
    void inventoryConsoleShouldExitImmediately() {
        withSystemIn("4\n", () -> assertDoesNotThrow(() -> InventoryConsole.main(new String[0])));
    }

    @Test
    void chainConsoleShouldRunFlow() {
        assertDoesNotThrow(() -> ChainConsole.main(new String[0]));
    }

    @Test
    void proxyConsoleShouldRunFlow() {
        assertDoesNotThrow(() -> ProxyConsole.main(new String[0]));
    }
}
