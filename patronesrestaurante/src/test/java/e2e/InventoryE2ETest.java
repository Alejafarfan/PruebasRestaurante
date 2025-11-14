package e2e;

import Creacionales.Singleton.controlador.InventoryController;
// no unused imports
// no unused imports
import Creacionales.Singleton.modelo.Kitchen;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryE2ETest {

    @Test
    void inventoryFullFlowAddAndRequestIngredient() {
        InventoryController controller = new InventoryController();
    Kitchen kitchen = new Kitchen("E2E Chef");

        // Add a new product and check it exists
        controller.addProduct("E2EProd", 30);
        assertTrue(controller.getInventoryStatus().stream()
                .anyMatch(p -> p.getNombre().equalsIgnoreCase("E2EProd")));

        // Request ingredient from kitchen and verify removal succeeds
        kitchen.requestIngredient("E2EProd", 10);
        var remaining = controller.getInventoryStatus().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase("E2EProd"))
                .findFirst()
                .map(p -> p.getCantidad())
                .orElse(-1);
        assertEquals(20, remaining);

        // Remove more than available should fail
        boolean removed = controller.removeProduct("E2EProd", 1000);
        assertFalse(removed);
    }
}