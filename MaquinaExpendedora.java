import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.IOException;

public class MaquinaExpendedora implements Carrito {

	 	ArrayList<Producto> productos = new ArrayList<>();
	    ArrayList<String> ventasDelDia = new ArrayList<>();
	    double total = 0;

	    public MaquinaExpendedora() {
	        
	        productos.add(new Alimento("1", "Gansitos", 10.5, 10));
	        productos.add(new Alimento("2", "Ruffles", 8.75, 10));
	        productos.add(new Bebida("3", "Coca-Cola", 15, 20));
	        productos.add(new Bebida("4", "Pepsi", 14, 20));
	    }

	    public void mostrarProductos() {
	        System.out.println("Productos disponibles:");
	        System.out.println("Código\tDescripción\tPrecio\tTipo\tInventario");
	        for (Producto p : productos) {
	            System.out.println(p.codigo + "\t" + p.descripcion + "\t" + p.precio + "\t" + p.tipo() + "\t" + p.inventario);
	        }
	    }

	    public void agregarProducto(String codigo, int cantidad) {
	        for (Producto p : productos) {
	            if (p.codigo.equals(codigo)) {
	            	//cambio y mejora del control de la cantidad de alimentos y manejo de inventario (se mejora el ciclo y ayudamos a la optimizacion, logica y entendimineto)
	            	if (p instanceof Alimento && p.inventario + cantidad > 10) {
	                    System.out.println("El contenedor de alimentos está lleno.");
	                    return;
	                } else if (p instanceof Bebida && p.inventario + cantidad > 20) {
	                    System.out.println("El contenedor de bebidas está lleno.");
	                    return;
	                }
	                p.inventario += cantidad;
	                System.out.println("Se han agregado " + cantidad + " unidades de " + p.descripcion + " al inventario.");
	                return;
	            }
	        }
	        System.out.println("Producto no encontrado.");
	    }

	    public void agregarProducto(Producto producto) {
	        if (producto.inventario > 0) {
	            total += producto.precio;
	            producto.inventario--;
	            System.out.println("Se ha agregado " + producto.descripcion + " al carrito.");
	        } else {
	            System.out.println("No hay inventario disponible para " + producto.descripcion);
	        }
	    }
	    
	    

	    //agregar un ciclo while para manejar el pago y el cambio, ademas agregamos el control de excepciones
	    public void finalizarCompra() {
	        if (total == 0) {
	            System.out.println("No hay productos en el carrito.");
	            return;
	        }

	        System.out.println("\nTotal a pagar: " + total);
	        Scanner scanner = new Scanner(System.in);
	        double pago = 0;
	        boolean pagoValido = false;
	        while (!pagoValido) {
	            try {
	                System.out.print("Ingrese la cantidad con la que paga: ");
	                pago = scanner.nextDouble();
	                if (pago < total) {
	                    System.out.println("El pago es insuficiente.");
	                } else {
	                    pagoValido = true;
	                }
	            } catch (Exception e) {
	                System.out.println("Entrada inválida. Ingrese un número válido.");
	                scanner.next();
	            }
	        }

	        double cambio = pago - total;
	        System.out.println("Cambio a devolver: " + cambio);
	        // Generar comprobante
	        generarComprobante();
	        // Registrar venta del día
	        registrarVenta();
	        // Reiniciar carrito y total
	        total = 0;
	    }
	    
	    

	    private void generarComprobante() {
	        try {
	            FileWriter writer = new FileWriter("comprobante.txt");
	            writer.write("Productos comprados:\n");
	            for (Producto p : productos) {
	                if (p.inventario < 10) { //en caso que se comprara el producto
	                    writer.write(p.descripcion + "\n");
	                }
	            }
	            writer.write("Total: " + total);
	            writer.close();
	            System.out.println("Se ha generado el comprobante.");
	        } catch (IOException e) {
	            System.out.println("Error al generar el comprobante.");
	        }
	    }
	    

	    private void registrarVenta() {
	        // Obtener la fecha y hora actual
	        Date fechaHoraActual = new Date();
	        // Formatear la fecha y hora actual
	        SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	        String fechaHora = formatoFechaHora.format(fechaHoraActual);

	        // Crear la cadena de venta del día
	        StringBuilder venta = new StringBuilder();
	        venta.append("Fecha y hora: ").append(fechaHora).append("\n");
	        venta.append("Productos comprados:\n");
	        for (Producto p : productos) {
	            if (p.inventario < 10) {
	                venta.append(p.descripcion).append("\n");
	            }
	        }
	        venta.append("Total: ").append(total).append("\n");

	        // Agregar la venta del día a la lista de ventas
	        ventasDelDia.add(venta.toString());
	        // Escribir ventas del día en el archivo e implementacion de una excepcion
	        try {
	            FileWriter writer = new FileWriter("Ventas_del_dia.txt", true);
	            writer.write(venta.toString());
	            writer.close();
	        } catch (IOException e) {
	            System.out.println("Error al registrar la venta del día.");
	        }
	    }

	    public void mostrarVentasDelDia() {

	        if (ventasDelDia.isEmpty()) {
	            System.out.println("No hay ventas registradas hoy.");
	            return;
	        }

	        System.out.println("Ventas del día:");
	        for (String venta : ventasDelDia) {
	            System.out.println(venta);
	        }
	    }
	    
	    
}
