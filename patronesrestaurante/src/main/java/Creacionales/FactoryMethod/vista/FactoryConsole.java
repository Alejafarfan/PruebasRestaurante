package Creacionales.FactoryMethod.vista;

import Creacionales.FactoryMethod.controlador.OrderFactoryController;
import Creacionales.FactoryMethod.modelo.*;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;


public class FactoryConsole {

    public static void main(String[] args) {
        KitchenService kitchen = order -> System.out.println("Preparando pedido de: " + order.getName());

        try (Scanner sc = new Scanner(System.in)) {
            boolean salir = false;
            NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));

            while (!salir) {
                mostrarMenu();
                int opcion = leerOpcion(sc);

                switch (opcion) {
                    case 1, 2, 3 -> {
                        OrderCreator creator = crearOrderCreator(sc, opcion, kitchen);
                        if (creator == null) {
                            // Si por alguna razón no se creó el creador, volvemos al menú.
                            break;
                        }

                        OrderFactoryController controller = new OrderFactoryController(creator);
                        String cliente = pedirTexto(sc, "Ingrese el nombre del cliente: ");

                        try {
                            Order order = controller.placeOrder(cliente);
                            order.deliver();

                            System.out.println("\nOrden creada exitosamente:");
                            System.out.println(order);
                            System.out.println("Items: " + order.getItems());

                            // Formatear el total si el precio es un número
                            try {
                                double precio = order.getPrice();
                                System.out.println("Total: " + currency.format(precio));
                            } catch (Exception fe) {
                                // Si getPrice() no retorna un double o lanza error, mostrar raw
                                System.out.println("Total: $" + order.getPrice());
                            }
                        } catch (Exception e) {
                            System.out.println("Error al crear la orden: " + e.getMessage());
                        }

                        pausar(sc);
                    }
                    case 4 -> {
                        salir = true;
                        System.out.println("Saliendo del sistema de pedidos...");
                    }
                    default -> {
                        System.out.println("Opción inválida, intente nuevamente.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n=== Sistema de Pedidos (Factory Method) ===");
        System.out.println("1. Pedido en mesa");
        System.out.println("2. Pedido a domicilio");
        System.out.println("3. Pedido para llevar");
        System.out.println("4. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int leerOpcion(Scanner sc) {
        String input = sc.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1; // opción inválida
        }
    }

    private static OrderCreator crearOrderCreator(Scanner sc, int opcion, KitchenService kitchen) {
        switch (opcion) {
            case 1:
                return new DineInOrderCreator(kitchen);
            case 2: {
                String direccion = pedirTexto(sc, "Ingrese dirección de entrega: ");
                if (direccion.isBlank()) {
                    System.out.println("La dirección no puede estar vacía. Volviendo al menú.");
                    return null;
                }
                return new DeliveryOrderCreator(kitchen, direccion);
            }
            case 3: {
                String codigo = pedirTexto(sc, "Ingrese código de retiro: ");
                if (codigo.isBlank()) {
                    System.out.println("El código de retiro no puede estar vacío. Volviendo al menú.");
                    return null;
                }
                return new TakeAwayOrderCreator(kitchen, codigo);
            }
            default:
                return null;
        }
    }

    private static String pedirTexto(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static void pausar(Scanner sc) {
        System.out.println("\nPresione ENTER para volver al menú...");
        sc.nextLine();
    }
}

