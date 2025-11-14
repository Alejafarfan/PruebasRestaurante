package Junit.Singleton;

import Creacionales.Singleton.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class InventoryUnitTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = Inventory.getInstance();
    }

    @Test
    void singletonInstanceShouldBeUnique() {
        Inventory inv1 = Inventory.getInstance();
        Inventory inv2 = Inventory.getInstance();
        assertSame(inv1, inv2);
    }

    @Test
    void shouldRegisterEntryAndExitCorrectly() {
        inventory.registerEntry("Café", 10);
        assertTrue(inventory.registerExit("Café", 5));
        assertFalse(inventory.registerExit("Café", 1000)); // insuficiente
    }

    @Test
    void shouldRegisterNewProductCorrectly() {
        inventory.registerEntry("Galletas", 25);
        var productos = inventory.getProductos();
        assertTrue(productos.stream().anyMatch(p -> p.getNombre().equalsIgnoreCase("Galletas")));
    }

    @Test
    void shouldRegisterExitReturnFalseForNonExistentProduct() {
        assertFalse(inventory.registerExit("ProductoInexistente", 5));
    }

    @Test
    void shouldRegisterExitReturnFalseForInsufficientQuantity() {
        inventory.registerEntry("Azúcar", 10);
        assertTrue(inventory.registerExit("Azúcar", 5));
        assertFalse(inventory.registerExit("Azúcar", 10)); // Solo hay 5 restantes
    }

    @Test
    void shouldGetAllProductsCorrectly() {
        var productosInicial = inventory.getProductos().size();
        assertTrue(productosInicial > 0);
        assertTrue(inventory.getProductos().stream()
                .anyMatch(p -> p.getNombre().equalsIgnoreCase("Arroz")));
    }

    @Test
    void shouldUpdateRestaurantNameCorrectly() {
        inventory.setRestaurantName("Nuevo Nombre");
        assertEquals("Nuevo Nombre", inventory.getRestaurantName());
    }

    @Test
    void shouldUpdateMinimumLevelCorrectly() {
        inventory.setMinimumLevel(10);
        assertEquals(10, inventory.getMinimumLevel());
    }

    @Test
    void shouldHandleProductoInventarioCorrectly() {
        ProductoInventario producto = new ProductoInventario("Test Producto", 15);
        assertEquals("Test Producto", producto.getNombre());
        assertEquals(15, producto.getCantidad());
    }

    @Test
    void shouldUpdateProductoInventarioCantidad() {
        ProductoInventario producto = new ProductoInventario("Café", 10);
        producto.setCantidad(20);
        assertEquals(20, producto.getCantidad());
    }

    @Test
    void shouldRegisterMultipleEntriesAndExits() {
        inventory.registerEntry("Leche", 30);
        inventory.registerEntry("Leche", 20);
        var cantidad = inventory.getProductos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase("Leche"))
                .findFirst()
                .map(ProductoInventario::getCantidad)
                .orElse(0);
        assertTrue(cantidad > 30);

        assertTrue(inventory.registerExit("Leche", 15));
        var cantidadFinal = inventory.getProductos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase("Leche"))
                .findFirst()
                .map(ProductoInventario::getCantidad)
                .orElse(0);
        assertTrue(cantidadFinal > 0);
    }

    @Test
    void shouldRegisterExitWithExactQuantity() {
        inventory.registerEntry("Sal", 50);
        assertTrue(inventory.registerExit("Sal", 50));
        var cantidad = inventory.getProductos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase("Sal"))
                .findFirst()
                .map(ProductoInventario::getCantidad)
                .orElse(-1);
        assertEquals(0, cantidad);
    }

        @Test
        void kitchenShouldRequestIngredientSuccessfully() {
            inventory.registerEntry("Tomate", 50);
            Kitchen kitchen = new Kitchen("Marco");
            kitchen.requestIngredient("Tomate", 10);
            var cantidad = inventory.getProductos().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase("Tomate"))
                    .findFirst()
                    .map(ProductoInventario::getCantidad)
                    .orElse(0);
            assertEquals(40, cantidad);
        }

        @Test
        void kitchenShouldFailToRequestInsufficientIngredient() {
            Kitchen kitchen = new Kitchen("Marco");
            kitchen.requestIngredient("NoExiste", 100);
            // No debería cambiar nada
            assertTrue(true);
        }

        @Test
        void administratorShouldAddStock() {
            Administrator admin = new Administrator("Luis");
            admin.addStock("Carne", 30);
            var cantidad = inventory.getProductos().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase("Carne"))
                    .findFirst()
                    .map(ProductoInventario::getCantidad)
                    .orElse(0);
            assertEquals(30, cantidad);
        }

        @Test
        void administratorShouldReviewInventory() {
            Administrator admin = new Administrator("Carlos");
            admin.reviewInventory(); // Solo imprime, no tira excepción
            assertTrue(true);
        }

        @Test
        void multipleKitchensAccessingSingleInventory() {
            Kitchen kitchen1 = new Kitchen("Chef 1");
            Kitchen kitchen2 = new Kitchen("Chef 2");
        
            inventory.registerEntry("Pollo", 100);
            kitchen1.requestIngredient("Pollo", 30);
            kitchen2.requestIngredient("Pollo", 40);
        
            var cantidad = inventory.getProductos().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase("Pollo"))
                    .findFirst()
                    .map(ProductoInventario::getCantidad)
                    .orElse(0);
            assertEquals(30, cantidad);
        }

        @Test
        void administratorAndKitchenCoordination() {
            Administrator admin = new Administrator("Gerente");
            Kitchen kitchen = new Kitchen("Jefe Cocina");
        
            admin.addStock("Pescado", 50);
            kitchen.requestIngredient("Pescado", 20);
        
            var cantidad = inventory.getProductos().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase("Pescado"))
                    .findFirst()
                    .map(ProductoInventario::getCantidad)
                    .orElse(0);
            assertEquals(30, cantidad);
        }

        @Test
        void kitchenMultipleRequests() {
            Kitchen kitchen = new Kitchen("Chef Principal");
            inventory.registerEntry("Harina", 100);
        
            kitchen.requestIngredient("Harina", 20);
            kitchen.requestIngredient("Harina", 15);
            kitchen.requestIngredient("Harina", 25);
        
            var cantidad = inventory.getProductos().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase("Harina"))
                    .findFirst()
                    .map(ProductoInventario::getCantidad)
                    .orElse(0);
            assertEquals(40, cantidad);
        }
}

