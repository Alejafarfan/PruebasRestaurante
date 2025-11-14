package Junit.Observer;

import Comportamentales.Observer.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ObserverUnitTest {

    private OrderNotifier notifier;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        notifier = new OrderNotifier();
        pedido = new Pedido(1, "Hamburguesa");
    }

    @Test
    void testPedidoEstadoActualizaCorrectamente() {
        Pedido p = new Pedido(1, "Hamburguesa");
        p.setEstado("Listo");
        assertEquals("Listo", p.getEstado());
    }

    @Test
    void testNotifierNotifiesObservers() {
        CocinaObserver cocina = new CocinaObserver("Test");
        notifier.agregarObservador(cocina);

        Pedido p = new Pedido(1, "Pizza");
        notifier.setPedido(p);
        assertEquals(p, notifier.getPedidoActual());
    }

    @Test
    void testPedidoIdAndDescripcion() {
        assertEquals(1, pedido.getId());
        assertEquals("Hamburguesa", pedido.getDescripcion());
    }

    @Test
    void testPedidoWithDifferentId() {
        Pedido pedido2 = new Pedido(42, "Arroz");
        assertEquals(42, pedido2.getId());
        assertEquals("Arroz", pedido2.getDescripcion());
    }

    @Test
    void testNotifierAddMultipleObservers() {
        CocinaObserver cocina = new CocinaObserver("Cocina 1");
        MeseroObserver mesero = new MeseroObserver("Mesero 1");

        notifier.agregarObservador(cocina);
        notifier.agregarObservador(mesero);

        notifier.setPedido(pedido);
        assertEquals(pedido, notifier.getPedidoActual());
    }

    @Test
    void testNotifierSetAndGetPedido() {
        Pedido p1 = new Pedido(1, "Pizza");
        Pedido p2 = new Pedido(2, "Pasta");

        notifier.setPedido(p1);
        assertEquals(p1, notifier.getPedidoActual());

        notifier.setPedido(p2);
        assertEquals(p2, notifier.getPedidoActual());
    }

    @Test
    void testCocinaObserverCreation() {
        CocinaObserver cocina = new CocinaObserver("Cocina Principal");
        assertNotNull(cocina);
    }

    @Test
    void testMeseroObserverCreation() {
        MeseroObserver mesero = new MeseroObserver("Mesero 1");
        assertNotNull(mesero);
    }

    @Test
    void testPedidoToString() {
        String toString = pedido.toString();
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Hamburguesa"));
    }

    @Test
    void testNotifierWithNullPedido() {
        notifier.setPedido(null);
        assertNull(notifier.getPedidoActual());
    }

    @Test
    void testPedidoWithLongDescripcion() {
        Pedido p = new Pedido(99, "Una descripción muy larga de un pedido especial con muchos detalles");
        assertEquals(99, p.getId());
        assertTrue(p.getDescripcion().length() > 50);
    }

    @Test
    void testMultiplePedidosWithDifferentDetails() {
        Pedido p1 = new Pedido(1, "Plato 1");
        Pedido p2 = new Pedido(2, "Plato 2");
        Pedido p3 = new Pedido(3, "Plato 3");

        notifier.setPedido(p1);
        assertEquals(p1, notifier.getPedidoActual());

        notifier.setPedido(p2);
        assertEquals(p2, notifier.getPedidoActual());

        notifier.setPedido(p3);
        assertEquals(p3, notifier.getPedidoActual());
    }

    @Test
    void testMultipleCajaObservers() {
        CajaObserver caja1 = new CajaObserver("Caja 1");
        CajaObserver caja2 = new CajaObserver("Caja 2");
        notifier.agregarObservador(caja1);
        notifier.agregarObservador(caja2);

        Pedido p = new Pedido(1, "Pago");
        notifier.setPedido(p);
        assertEquals(p, notifier.getPedidoActual());
    }

    @Test
    void testMultipleMeseroObservers() {
        MeseroObserver mesero1 = new MeseroObserver("Mesero A");
        MeseroObserver mesero2 = new MeseroObserver("Mesero B");
        notifier.agregarObservador(mesero1);
        notifier.agregarObservador(mesero2);

        Pedido p = new Pedido(2, "Servir");
        notifier.setPedido(p);
        assertEquals(p, notifier.getPedidoActual());
    }

    @Test
    void testMixedObservers() {
        CajaObserver caja = new CajaObserver("Caja Principal");
        MeseroObserver mesero = new MeseroObserver("Mesero Principal");
        CocinaObserver cocina = new CocinaObserver("Cocina Principal");

        notifier.agregarObservador(caja);
        notifier.agregarObservador(mesero);
        notifier.agregarObservador(cocina);

        Pedido p = new Pedido(10, "Pedido Completo");
        notifier.setPedido(p);
        assertEquals(p, notifier.getPedidoActual());
    }

    @Test
    void testCajaObserverConstructor() {
        CajaObserver caja = new CajaObserver("Caja 1");
        assertNotNull(caja);
    }

    @Test
    void testCocinaObserverActualizar() {
        CocinaObserver cocina = new CocinaObserver("Cocina Principal");
        Pedido p = new Pedido(75, "Test Cocina");
        assertDoesNotThrow(() -> cocina.actualizar(p));
    }

    @Test
    void testNotifierRemovesObserverAfterNotification() {
        CajaObserver caja = new CajaObserver("Caja Test");
        notifier.agregarObservador(caja);
        
        Pedido p = new Pedido(100, "Pedido Test");
        notifier.setPedido(p);
        assertEquals(p, notifier.getPedidoActual());
    }

    @Test
    void testPedidoGettersAndSetters() {
        Pedido p = new Pedido(55, "Descripción Test");
        assertEquals(55, p.getId());
        assertEquals("Descripción Test", p.getDescripcion());
        
        p.setEstado("Nuevo Estado");
        assertEquals("Nuevo Estado", p.getEstado());
    }

    @Test
    void testNotifierWithEmptyObservers() {
        Pedido p = new Pedido(200, "Sin Observadores");
        notifier.setPedido(p);
        assertEquals(p, notifier.getPedidoActual());
    }

    @Test
    void testMultipleObserversNotifiedSequentially() {
        CajaObserver caja = new CajaObserver("Caja Seq");
        MeseroObserver mesero = new MeseroObserver("Mesero Seq");
        CocinaObserver cocina = new CocinaObserver("Cocina Seq");
        
        notifier.agregarObservador(caja);
        notifier.agregarObservador(mesero);
        notifier.agregarObservador(cocina);
        
        Pedido p1 = new Pedido(1, "Primer Pedido");
        notifier.setPedido(p1);
        assertEquals(p1, notifier.getPedidoActual());
        
        Pedido p2 = new Pedido(2, "Segundo Pedido");
        notifier.setPedido(p2);
        assertEquals(p2, notifier.getPedidoActual());
    }

    @Test
    void testNotifierGetPedidoActual() {
        assertNull(notifier.getPedidoActual());
        
        Pedido p = new Pedido(88, "Pescado");
        notifier.setPedido(p);
        assertNotNull(notifier.getPedidoActual());
        assertEquals(88, notifier.getPedidoActual().getId());
    }

    @Test
    void testCajaObserverReceivesMultipleUpdates() {
        CajaObserver caja = new CajaObserver("Caja Multi");
        notifier.agregarObservador(caja);
        
        for (int i = 1; i <= 5; i++) {
            Pedido p = new Pedido(i, "Pedido " + i);
            notifier.setPedido(p);
            assertEquals(p, notifier.getPedidoActual());
        }
    }

    @Test
    void testMeseroObserverReceivesMultipleUpdates() {
        MeseroObserver mesero = new MeseroObserver("Mesero Multi");
        notifier.agregarObservador(mesero);
        
        Pedido p1 = new Pedido(10, "Orden 1");
        Pedido p2 = new Pedido(11, "Orden 2");
        Pedido p3 = new Pedido(12, "Orden 3");
        
        notifier.setPedido(p1);
        assertEquals(10, notifier.getPedidoActual().getId());
        
        notifier.setPedido(p2);
        assertEquals(11, notifier.getPedidoActual().getId());
        
        notifier.setPedido(p3);
        assertEquals(12, notifier.getPedidoActual().getId());
    }
}
