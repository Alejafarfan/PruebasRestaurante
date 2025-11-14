package e2e;

import Comportamentales.Command.vista.CommandConsole;
import Creacionales.FactoryMethod.vista.FactoryConsole;
import Creacionales.Singleton.vista.InventoryConsole;
import Creacionales.Builder.vista.BuilderConsole;
import Estructurales.Adapter.vista.AdapterConsole;
import Comportamentales.Observer.vista.ObserverConsole;
import Comportamentales.State.vista.StateConsole;
import Estructurales.Proxy.vista.ProxyConsole;
import Comportamentales.ChainOfResponsibility.vista.ChainConsole;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class ConsoleMainE2ETest {

    private InputStream sysInBackup = System.in;

    @Test
    void adapterConsoleShouldExitQuickly() {
        String input = "4\n"; // choose exit
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    assertDoesNotThrow(() -> AdapterConsole.main(new String[]{}));
    System.setIn(sysInBackup);
    }

    @Test
    void commandConsoleShouldExitQuickly() {
        String input = "5\n"; // choose exit
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    assertDoesNotThrow(() -> CommandConsole.main(new String[]{}));
    System.setIn(sysInBackup);
    }

    @Test
    void factoryConsoleShouldExitQuickly() {
        String input = "4\n"; // exit
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    assertDoesNotThrow(() -> FactoryConsole.main(new String[]{}));
    System.setIn(sysInBackup);
    }

    @Test
    void inventoryConsoleShouldExitQuickly() {
        String input = "4\n"; // exit
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    assertDoesNotThrow(() -> InventoryConsole.main(new String[]{}));
    System.setIn(sysInBackup);
    }

    @Test
    void builderConsoleShouldExitQuickly() {
        String input = "4\n"; // exit
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    assertDoesNotThrow(() -> BuilderConsole.main(new String[]{}));
    System.setIn(sysInBackup);
    }

    @Test
    void observerConsoleShouldExitQuickly() {
        String input = "4\n"; // exit
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    assertDoesNotThrow(() -> ObserverConsole.main(new String[]{}));
    System.setIn(sysInBackup);
    }

    @Test
    void stateConsoleShouldRunAndExit() {
        // id, description, several ENTER to advance states, then 'no' to stop
        String input = "1\nTestDesc\n\n\n\n\nno\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    assertDoesNotThrow(() -> StateConsole.main(new String[]{}));
    System.setIn(sysInBackup);
    }

    @Test
    void proxyConsoleAndChainConsoleShouldRun() {
        // These mains don't read input
    assertDoesNotThrow(() -> ProxyConsole.main(new String[]{}));
    assertDoesNotThrow(() -> ChainConsole.main(new String[]{}));
    }
}
