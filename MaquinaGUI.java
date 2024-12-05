import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MaquinaGUI {

    private MaquinaExpendedora maquina;
    private JFrame frame;
    private JTable tableProductos;
    private JTextArea areaVentas;
    private JLabel labelTotal;

    public MaquinaGUI() {
        maquina = new MaquinaExpendedora();
        initComponents();
    }

    private void initComponents() {
        frame = new JFrame("Maquina Expendedora");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Panel de productos disponibles
        JPanel panelProductos = new JPanel(new BorderLayout());
        panelProductos.setBorder(BorderFactory.createTitledBorder("Productos Disponibles"));

        tableProductos = new JTable();
        actualizarTablaProductos();
        JScrollPane scrollProductos = new JScrollPane(tableProductos);
        panelProductos.add(scrollProductos, BorderLayout.CENTER);

        // Panel de opciones
        JPanel panelOpciones = new JPanel(new GridLayout(5, 1, 10, 10));
        panelOpciones.setBorder(BorderFactory.createTitledBorder("Opciones"));

        JButton btnAgregarContenedor = new JButton("Agregar al Contenedor");
        JButton btnAgregarCarrito = new JButton("Agregar al Carrito");
        JButton btnFinalizarCompra = new JButton("Finalizar Compra");
        JButton btnMostrarVentas = new JButton("Mostrar Ventas del Día");

        btnAgregarContenedor.addActionListener(e -> agregarAlContenedor());
        btnAgregarCarrito.addActionListener(e -> agregarAlCarrito());
        btnFinalizarCompra.addActionListener(e -> finalizarCompra());
        btnMostrarVentas.addActionListener(e -> mostrarVentasDelDia());

        panelOpciones.add(btnAgregarContenedor);
        panelOpciones.add(btnAgregarCarrito);
        panelOpciones.add(btnFinalizarCompra);
        panelOpciones.add(btnMostrarVentas);

        labelTotal = new JLabel("Total: $0.00", JLabel.CENTER);
        labelTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelOpciones.add(labelTotal);

        // Panel de ventas
        JPanel panelVentas = new JPanel(new BorderLayout());
        panelVentas.setBorder(BorderFactory.createTitledBorder("Ventas del Día"));
        areaVentas = new JTextArea();
        areaVentas.setEditable(false);
        JScrollPane scrollVentas = new JScrollPane(areaVentas);
        panelVentas.add(scrollVentas, BorderLayout.CENTER);

        // Agregar todo al frame
        frame.add(panelProductos, BorderLayout.CENTER);
        frame.add(panelOpciones, BorderLayout.EAST);
        frame.add(panelVentas, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void actualizarTablaProductos() {
        String[] columnNames = {"Código", "Descripción", "Precio", "Tipo", "Inventario"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Producto p : maquina.productos) {
            model.addRow(new Object[]{p.codigo, p.descripcion, p.precio, p.tipo(), p.inventario});
        }

        tableProductos.setModel(model);
    }

    private void agregarAlContenedor() {
        String codigo = JOptionPane.showInputDialog(frame, "Ingrese el código del producto:");
        String cantidadStr = JOptionPane.showInputDialog(frame, "Ingrese la cantidad a agregar:");

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            maquina.agregarProducto(codigo, cantidad);
            actualizarTablaProductos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarAlCarrito() {
    	String codigo = JOptionPane.showInputDialog(frame, "Ingrese el código del producto:");
        Producto producto = null;
        for (Producto p : maquina.productos) {
            if (p.codigo.equals(codigo)) {
                producto = p;
                break;
            }
        }
        if (producto != null) {
            maquina.agregarProducto(producto);
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private void finalizarCompra() {
        double pago = 0;

        try {
            String pagoStr = JOptionPane.showInputDialog(frame, "Ingrese la cantidad con la que paga:");
            pago = Double.parseDouble(pagoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pago < maquina.total) {
            JOptionPane.showMessageDialog(frame, "El pago es insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            double cambio = pago - maquina.total;
            JOptionPane.showMessageDialog(frame, "Compra finalizada. Cambio: $" + cambio);
            maquina.finalizarCompra();
            labelTotal.setText("Total: $0.00");
            actualizarTablaProductos();
        }
    }

    private void mostrarVentasDelDia() {
        StringBuilder ventas = new StringBuilder();
        for (String venta : maquina.ventasDelDia) {
            ventas.append(venta).append("\n");
        }

        areaVentas.setText(ventas.toString());
    }

    public static void main(String[] args) {
        new MaquinaGUI();
    }
}
