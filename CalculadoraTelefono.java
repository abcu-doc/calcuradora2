import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculadoraTelefono extends JFrame implements ActionListener {

    private JTextField display; 
    private JLabel operationDisplay; 
    private double num1 = 0, num2 = 0, resultado = 0;
    private String operador = "";
    private boolean nuevaEntrada = false;

    public CalculadoraTelefono() {
        setTitle("Calculadora de Teléfono");
        setSize(350, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        // Pantalla superior para mostrar la operación
        operationDisplay = new JLabel(" ");
        operationDisplay.setFont(new Font("Arial", Font.PLAIN, 28));
        operationDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
        operationDisplay.setForeground(Color.GRAY);
        add(operationDisplay, BorderLayout.NORTH);

        // Pantalla principal (resultado)
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.PLAIN, 50));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.CENTER);

        // Panel de botones
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 10, 10));
        panel.setBackground(Color.BLACK);

        String[] botones = {
            "C", "←", "/", "*",
            "7", "8", "9", "-",
            "4", "5", "6", "+",
            "1", "2", "3", 
            "±", "0", ".", "="
        };

        for (String texto : botones) {
            JButton boton = new JButton(texto);
            boton.setFont(new Font("Arial", Font.PLAIN, 24));
            boton.setFocusPainted(false);
            boton.setBackground(texto.matches("[0-9]") ? new Color(51, 51, 51) : new Color(255, 165, 0)); 
            boton.setForeground(Color.WHITE);
            if (texto.equals("C") || texto.equals("←")) {
                boton.setBackground(new Color(200, 0, 0));
            }
            if (texto.equals("=")) {
                boton.setBackground(new Color(0, 122, 255));
            }
            boton.addActionListener(this);
            panel.add(boton);
        }

        add(panel, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        // Manejo de números y punto decimal
        if (comando.matches("[0-9.]")) {
            if (nuevaEntrada || display.getText().equals("0")) {
                display.setText(comando.equals(".") ? "0." : comando);
                nuevaEntrada = false;
            } else {
                if (comando.equals(".") && display.getText().contains(".")) return;
                display.setText(display.getText() + comando);
            }
            operationDisplay.setText(operationDisplay.getText() + comando);
        }
        // Manejo de operadores
        else if (comando.matches("[/*\\-+]")) {
            if (!operador.isEmpty() && !nuevaEntrada) {
                calcularResultado();
            }
            num1 = Double.parseDouble(display.getText());
            operador = comando;
            nuevaEntrada = true;
            operationDisplay.setText(operationDisplay.getText() + " " + operador + " ");
        }
        // Calcular resultado
        else if (comando.equals("=")) {
            if (!operador.isEmpty()) {
                calcularResultado();
                operador = "";
            }
        }
        // Limpiar pantalla
        else if (comando.equals("C")) {
            display.setText("0");
            operationDisplay.setText(" ");
            num1 = num2 = resultado = 0;
            operador = "";
            nuevaEntrada = false;
        }
        // Borrar último número
        else if (comando.equals("←")) {
            if (nuevaEntrada) {
                return; 
            }
            String textoActual = display.getText();
            if (textoActual.length() > 1) {
                display.setText(textoActual.substring(0, textoActual.length() - 1));
            } else {
                display.setText("0");
            }
            operationDisplay.setText(operationDisplay.getText().substring(0, operationDisplay.getText().length() - 1));
        }
        // Cambio de signo
        else if (comando.equals("±")) {
            double valor = Double.parseDouble(display.getText());
            valor *= -1;
            display.setText(String.valueOf(valor));
        }
    }

    private void calcularResultado() {
        try {
            num2 = Double.parseDouble(display.getText());
    
            // Validación para evitar división por cero
            if (operador.equals("/") && num2 == 0) {
                display.setText("Error");
                operationDisplay.setText(formatearNumero(num1) + " " + operador + " " + formatearNumero(num2) + " = Error");
                return;
            }
    
            switch (operador) {
                case "+": resultado = num1 + num2; break;
                case "-": resultado = num1 - num2; break;
                case "*": resultado = num1 * num2; break;
                case "/": resultado = num1 / num2; break;
            }
    
            // Formatear los números antes de mostrarlos
            String resultadoFinal = formatearNumero(resultado);
            display.setText(resultadoFinal);
            operationDisplay.setText(formatearNumero(num1) + " " + operador + " " + formatearNumero(num2) + " = " + resultadoFinal);
            
            num1 = resultado;
            nuevaEntrada = true;
        } catch (Exception ex) {
            display.setText("Error");
        }
    }
    
    // Método para quitar decimales innecesarios
    private String formatearNumero(double num) {
        return (num % 1 == 0) ? String.valueOf((int) num) : String.valueOf(num);
    }
    
    public static void main(String[] args) {
        new CalculadoraTelefono();
    }
}