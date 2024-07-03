import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("Segoe print", Font.BOLD, 18);
    final private JPanel buttonsJugar = new JPanel();
    final private JPanel MainPanel = new JPanel();
    final private JLabel lbJugador = new JLabel(new ImageIcon());
    final private JLabel lbwelcome = new JLabel("Bienvenido");
    final private JLabel lbMaquina = new JLabel(new ImageIcon());

    final private JLabel outVictorias = new JLabel("0"), outEmpates = new JLabel("0"), outDerrotas = new JLabel("0");
    int victorias = 0, derrotas = 0, empates = 0;

    public void initialize() throws IOException {
        
        // Labels de arriba
        JLabel lbVictorias = new JLabel("Victorias:");
        lbVictorias.setFont(mainFont);
        outVictorias.setFont(mainFont);

        JLabel lbDerrotas = new JLabel("Derrotas:");
        lbDerrotas.setFont(mainFont);
        outDerrotas.setFont(mainFont);

        JLabel lbEmpates = new JLabel("Empates:");
        lbEmpates.setFont(mainFont);
        outEmpates.setFont(mainFont);

        setNumbers();
        // Upper panel, donde dice victorias empates y derrotas
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new GridLayout(1, 6, 50, 50));
        upperPanel.setBounds(600, 350, 600, 300);
        upperPanel.setBackground(Color.WHITE);

        upperPanel.add(lbVictorias);
        upperPanel.add(outVictorias);
        upperPanel.add(lbEmpates);
        upperPanel.add(outEmpates);
        upperPanel.add(lbDerrotas);
        upperPanel.add(outDerrotas);

        // Welcome/outcome label
        lbwelcome.setFont(mainFont);
        lbwelcome.setHorizontalAlignment(JLabel.CENTER);
        lbwelcome.setVerticalAlignment(JLabel.CENTER);

        // Label del jugador
        lbJugador.setBackground(new Color(128, 128, 255));

        // Botones
        JButton btnTijera = new JButton("      Tijera      ");
        JButton btnPiedra = new JButton("      Piedra      ");
        JButton btnPapel = new JButton("      Papel      ");
        JButton btnReiniciar = new JButton("Reiniciar");
        Color viejoColor = btnPiedra.getBackground();
        btnReiniciar.setFont(mainFont);
        btnReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanDB();
                btnPapel.setBackground(viejoColor);
                btnTijera.setBackground(viejoColor);
                btnPiedra.setBackground(viejoColor);
                MainPanel.remove(lbJugador);
                MainPanel.add(buttonsJugar, BorderLayout.EAST);
                lbJugador.setIcon(null);
                lbMaquina.setIcon(null);
                lbwelcome.setText("Bienvenido");
                MainPanel.revalidate();
                MainPanel.repaint();
            }
        });
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(mainFont);
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(ABORT);
            }
        });

        btnPiedra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnPapel.setBackground(viejoColor);
                    btnTijera.setBackground(viejoColor);
                    btnPiedra.setBackground(new Color(181, 230, 29));
                    logicaJuego("Piedra");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnPapel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnPiedra.setBackground(viejoColor);
                    btnTijera.setBackground(viejoColor);
                    btnPapel.setBackground(new Color(181, 230, 29));
                    logicaJuego("Papel");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnTijera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnPapel.setBackground(viejoColor);
                    btnPiedra.setBackground(viejoColor);
                    btnTijera.setBackground(new Color(181, 230, 29));
                    logicaJuego("Tijera");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        buttonsJugar.setLayout(new GridLayout(3, 1, 100, 10));
        buttonsJugar.setBackground(new Color(128, 128, 255));
        buttonsJugar.setOpaque(true);
        buttonsJugar.add(btnPiedra);
        buttonsJugar.add(btnPapel);
        buttonsJugar.add(btnTijera);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(btnReiniciar);
        buttonsPanel.add(btnSalir);

        // Main panel
        MainPanel.setLayout(new BorderLayout());
        MainPanel.setBackground(new Color(128, 128, 255));
        MainPanel.add(upperPanel, BorderLayout.NORTH);
        MainPanel.add(lbwelcome, BorderLayout.CENTER);
        MainPanel.add(buttonsJugar, BorderLayout.EAST);
        MainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        MainPanel.add(lbMaquina, BorderLayout.WEST);
        add(MainPanel);

        setTitle("Piedra, papel o tijera");
        setSize(800, 600);
        setMinimumSize(new Dimension(300, 400));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private Connection connect() {
        String url = "jdbc:sqlite:./game.db"; // URL de la base de datos SQLite
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    private void cleanDB(){
        String query = "DELETE FROM results WHERE id;";
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.execute();
                outVictorias.setText("0");
                outEmpates.setText("0");
                outDerrotas.setText("0");
                victorias = 0;
                derrotas = 0;
                empates = 0;
        } catch (SQLException er) {
            System.out.println(er.getMessage());
        }
    }

    private void setNumbers() {
        String get = "SELECT\n" + //
                        "    COUNT(CASE WHEN result = 'Victoria' THEN 1 END) AS count_V,\n" + //
                        "    COUNT(CASE WHEN result = 'Derrota' THEN 1 END) AS count_D,\n" + //
                        "    COUNT(CASE WHEN result = 'Empate' THEN 1 END) AS count_E\n" + //
                        "FROM results;";
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(get)) {
                ResultSet resultSet = pstmt.executeQuery();
                while (resultSet.next()) {
                    outVictorias.setText(resultSet.getString(1));
                    outDerrotas.setText(resultSet.getString(2));
                    outEmpates.setText(resultSet.getString(3));
                }
               
        } catch (SQLException er) {
            System.out.println(er.getMessage());
        }
        
    }

    private void insertResult(String result) {
        String sql = "INSERT INTO results(result) VALUES(?)";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, result);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void logicaJuego(String jugadorEleccion) throws IOException {
        // Imágenes del jugador
        // BufferedImage tijeraJ = ImageIO.read(new File("src/images/tijerajug.png"));
        // BufferedImage papelJ = ImageIO.read(new File("src/images/papeljug.png"));
        // BufferedImage piedraJ = ImageIO.read(new File("src/images/piedrajug.png"));

        // Imágenes de la máquina
        BufferedImage piedraM = ImageIO.read(new File("src/images/piedramaquina.png"));
        BufferedImage papelM = ImageIO.read(new File("src/images/papelmaquina.png"));
        BufferedImage tijeraM = ImageIO.read(new File("src/images/tijeramaquina.png"));

        // Opciones de la máquina
        String[] opciones = { "Piedra", "Papel", "Tijera" };
        String maquinaEleccion = opciones[(int) (Math.random() * 3)];

        // Mostrar la elección del jugador
        // switch (jugadorEleccion) {
        //     case "Tijera":
        //         lbJugador.setIcon(new ImageIcon(tijeraJ));
        //         break;
        //     case "Piedra":
        //         lbJugador.setIcon(new ImageIcon(piedraJ));
        //         break;
        //     case "Papel":
        //         lbJugador.setIcon(new ImageIcon(papelJ));
        //         break;
        // }

        // Mostrar la elección de la máquina
        switch (maquinaEleccion) {
            case "Tijera":
                lbMaquina.setIcon(new ImageIcon(tijeraM));
                break;
            case "Piedra":
                lbMaquina.setIcon(new ImageIcon(piedraM));
                break;
            case "Papel":
                lbMaquina.setIcon(new ImageIcon(papelM));
                break;
        }

        empates = Integer.parseInt(outEmpates.getText());
        derrotas = Integer.parseInt(outDerrotas.getText());
        victorias = Integer.parseInt(outVictorias.getText());

        // Determinar el resultado
        String resultado = "Empate";
        if (jugadorEleccion.equals(maquinaEleccion)) {
            empates++;
            lbwelcome.setText("Empate");
            resultado = "Empate";
        } else if ((jugadorEleccion.equals("Piedra") && maquinaEleccion.equals("Tijera")) ||
                (jugadorEleccion.equals("Papel") && maquinaEleccion.equals("Piedra")) ||
                (jugadorEleccion.equals("Tijera") && maquinaEleccion.equals("Papel"))) {
            victorias++;
            lbwelcome.setText("¡Ganaste!");
            resultado = "Victoria";
        } else {
            derrotas++;
            lbwelcome.setText("Perdiste");
            resultado = "Derrota";
        }

        // Actualizar las etiquetas de puntos
        outVictorias.setText(String.valueOf(victorias));
        outEmpates.setText(String.valueOf(empates));
        outDerrotas.setText(String.valueOf(derrotas));

        insertResult(resultado);
    }

    private void createNewDatabase() {
        String url = "jdbc:sqlite:./game.db"; // URL de la base de datos SQLite

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {

                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("El nombre del driver es " + meta.getDriverName());
                System.out.println("Se ha creado una nueva base de datos.");

                // Crear la tabla de resultados si no existe
                String sql = "CREATE TABLE IF NOT EXISTS results (\n"
                        + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + " result TEXT NOT NULL\n"
                        + ");";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.execute();
                    System.out.println("Tabla 'results' creada o ya existente.");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        MainFrame myframe = new MainFrame();
        myframe.createNewDatabase(); // Crea la base de datos y la tabla si no existen
        myframe.initialize();
    }
}