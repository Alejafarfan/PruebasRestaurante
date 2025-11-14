package Junit.State;

import Comportamentales.State.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class StateUnitTest {

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido(1, "Pasta");
    }

    @Test
    void pedidoDebeCambiarDeEstado() {
        assertEquals("Nuevo", pedido.getEstadoNombre());

        pedido.avanzarEstado();
        assertEquals("Preparando", pedido.getEstadoNombre());
    }

    @Test
    void pedidoDebeLlegarHastaCerrado() {
        pedido.avanzarEstado(); // Preparando
        pedido.avanzarEstado(); // Listo para servir
        pedido.avanzarEstado(); // Entregado
        pedido.avanzarEstado(); // Cerrado

        assertEquals("Cerrado", pedido.getEstadoNombre());
    }

    @Test
    void pedidoDebeIniciarEnEstadoNuevo() {
        Pedido p = new Pedido(2, "Pizza");
        assertEquals("Nuevo", p.getEstadoNombre());
    }

    @Test
    void pedidoDebeAvanzarHastaListoParaServir() {
        pedido.avanzarEstado(); // Preparando
        pedido.avanzarEstado(); // Listo para servir
        
        assertEquals("Listo para servir", pedido.getEstadoNombre());
    }

    @Test 
    void pedidoDebeAvanzarHastaEntregado() {
        pedido.avanzarEstado(); // Preparando
        pedido.avanzarEstado(); // Listo para servir
        pedido.avanzarEstado(); // Entregado

        assertEquals("Entregado", pedido.getEstadoNombre());
    }

    @Test
    void verificarIdYDescripcionPedido() {
        Pedido p = new Pedido(5, "Tacos");
        assertEquals(5, p.getId());
        assertEquals("Tacos", p.getDescripcion());
    }

    @Test
    void pedidoDebeAvanzarDesdeNuevoAPreparando() {
        assertEquals("Nuevo", pedido.getEstadoNombre());
        pedido.avanzarEstado();
        assertEquals("Preparando", pedido.getEstadoNombre());
    }

    @Test
    void pedidoDebeAvanzarDesdePreparandoAListoParaServir() {
        pedido.avanzarEstado(); // Preparando
        pedido.avanzarEstado(); // Listo para servir

        assertEquals("Listo para servir", pedido.getEstadoNombre());
    }

    @Test
    void pedidoDebeAvanzarDesdeListoAEntregado() {
        pedido.avanzarEstado(); // Preparando
        pedido.avanzarEstado(); // Listo para servir
        pedido.avanzarEstado(); // Entregado

        assertEquals("Entregado", pedido.getEstadoNombre());
    }

    @Test
    void pedidoDebeAvanzarDesdeEntregadoACerrado() {
        pedido.avanzarEstado(); // Preparando
        pedido.avanzarEstado(); // Listo para servir
        pedido.avanzarEstado(); // Entregado
        pedido.avanzarEstado(); // Cerrado

        assertEquals("Cerrado", pedido.getEstadoNombre());
    }

    @Test
    void pedidoConDiferentesIds() {
        Pedido p1 = new Pedido(1, "Desc1");
        Pedido p2 = new Pedido(100, "Desc100");

        assertEquals(1, p1.getId());
        assertEquals(100, p2.getId());
        assertEquals("Desc1", p1.getDescripcion());
        assertEquals("Desc100", p2.getDescripcion());
    }

    @Test
    void pedidoToString() {
        Pedido p = new Pedido(10, "Hamburguesa");
        String str = p.toString();
        assertTrue(str.contains("10"));
        assertTrue(str.contains("Hamburguesa"));
    }

    @Test
    void multiplePedidosConEstadosIndependientes() {
        Pedido p1 = new Pedido(1, "Pizza");
        Pedido p2 = new Pedido(2, "Pasta");

        p1.avanzarEstado(); // p1: Preparando
        assertEquals("Preparando", p1.getEstadoNombre());
        assertEquals("Nuevo", p2.getEstadoNombre()); // p2 sigue en Nuevo

        p2.avanzarEstado();
        p2.avanzarEstado(); // p2: Listo para servir
        assertEquals("Preparando", p1.getEstadoNombre());
        assertEquals("Listo para servir", p2.getEstadoNombre());
    }

    @Test
    void pedidoDebeSetearEstadoCorrectamente() {
        EstadoPedido nuevoEstado = new EstadoPreparando();
        pedido.setEstado(nuevoEstado);
        assertEquals("Preparando", pedido.getEstadoNombre());
    }

    @Test
    void secuenciaCompletaDePedido() {
        assertEquals("Nuevo", pedido.getEstadoNombre());
        
        pedido.avanzarEstado();
        assertEquals("Preparando", pedido.getEstadoNombre());
        
        pedido.avanzarEstado();
        assertEquals("Listo para servir", pedido.getEstadoNombre());
        
        pedido.avanzarEstado();
        assertEquals("Entregado", pedido.getEstadoNombre());
        
        pedido.avanzarEstado();
        assertEquals("Cerrado", pedido.getEstadoNombre());
    }
}
