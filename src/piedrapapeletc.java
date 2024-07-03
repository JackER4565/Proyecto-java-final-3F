import java.util.Scanner;

public class piedrapapeletc {
    public static void main(String[] args) {
        String[] maquina = new String[] { "Piedra", "Papel", "Tijera" };
        int puntosMaq = 0;
        int puntosJug = 0;
        int empates = 0;
        
        try (Scanner leer = new Scanner(System.in)) {
            while (puntosJug < 10 && puntosMaq < 10) {

                System.out.println("Ingrese piedra papel o tijera.");
                String usuario = leer.nextLine();

                int Aleatorio = (int) Math.round(Math.random() * 2);
                System.out.println(Aleatorio);
                usuario = usuario.toLowerCase();
                switch (usuario) {
                    case "piedra":
                        if (maquina[Aleatorio].equalsIgnoreCase("Piedra")){
                            System.out.println("Empate la maquina eligio: " + maquina[Aleatorio]);
                            empates++;
                            }
                        if (maquina[Aleatorio].equalsIgnoreCase("Papel")){
                            System.out.println("Perdiste la maquina eligio: " + maquina[Aleatorio]);
                            puntosMaq ++;
                        }
                        if (maquina[Aleatorio].equalsIgnoreCase("Tijera")){
                            System.out.println("Ganaste la maquina eligio: " + maquina[Aleatorio]);
                            puntosJug ++;
                        }
                        break;
                    case "papel":
                        if (maquina[Aleatorio].equalsIgnoreCase("Piedra")){
                            System.out.println("Ganaste la maquina eligio: " + maquina[Aleatorio]);
                            puntosJug ++;
                        }
                        if (maquina[Aleatorio].equalsIgnoreCase("Papel")){
                            System.out.println("Empate la maquina eligio: " + maquina[Aleatorio]);
                            empates++;
                        }
                        if (maquina[Aleatorio].equalsIgnoreCase("Tijera")){
                            System.out.println("Perdiste la maquina eligio: " + maquina[Aleatorio]);
                            puntosMaq ++;
                        }
                        break;

                    case "tijera":
                        if (maquina[Aleatorio].equalsIgnoreCase("Piedra")){
                            System.out.println("Perdiste la maquina eligio: " + maquina[Aleatorio]);
                            puntosMaq++;
                        }
                        if (maquina[Aleatorio].equalsIgnoreCase("Papel")){
                            System.out.println("Ganaste la maquina eligio: " + maquina[Aleatorio]);
                            puntosJug++;
                        }
                        if (maquina[Aleatorio].equalsIgnoreCase("Tijera")){
                            System.out.println("Empate la maquina eligio: " + maquina[Aleatorio]);
                            empates++;
                        }
                        break;

                    default:
                        System.out.println("?");
                }
                System.out.println("Puntos Maquina: " + puntosMaq);
                System.out.println("Puntos Jugador: " + puntosJug);
                System.out.println("Empates: " + empates);
                System.out.println("Press enter to continue.....");

                leer.nextLine();   
                System.out.print("\033[H\033[2J");  
                System.out.flush();  
            }
        }

    }
}
