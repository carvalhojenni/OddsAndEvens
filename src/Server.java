import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
        public static void main(String[] args) {
            System.out.println("Carregando o servidor...");
            final int porta = 7777;

            try {
                //reservar porta p/ servidor
                ServerSocket serverSocket = new ServerSocket(porta);
                System.out.println("O Servidor esta disponvel! ||  Porta: "+porta);

                while (!serverSocket.isClosed()){
                    Socket socketClient = serverSocket.accept();

                    new Thread(new ManipuladorConexao(socketClient)).start();
                }
            } catch (IOException e) {
                
            }
        }
}