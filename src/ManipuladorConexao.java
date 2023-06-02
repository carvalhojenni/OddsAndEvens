import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ManipuladorConexao implements Runnable {
    private static ArrayList<ManipuladorConexao> lista_de_conexoes = new ArrayList<>();
    private BufferedReader entrada;
    private BufferedWriter saida;
    private String nomeJogador;
    private int numeroAleatorioPar = 10;
    private boolean escolhaPar, pgtjogarDnv, jogarDnvRecebido, numeroAleatorioRecebidoPar, escolhaParRecebido;
    private ManipuladorConexao adversarioManipulador;
    private Socket socketClient;

    public ManipuladorConexao(Socket socketClient) {
        try {
            this.socketClient = socketClient;
            entrada = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            saida = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));

        } catch (IOException manipulador) {
            manipulador.getMessage();
            fecharTudo();
           
        }
    }

    private void transmissaoManipulador(String msg) throws IOException {
        saida.write(msg);
        saida.newLine();
        saida.flush();
    }

    private void fecharTudo() {
        try {
            System.out.println(nomeJogador + " saiu do jogo :(");
            lista_de_conexoes.remove(this);
            entrada.close();
            saida.close();
            socketClient.close();
        } catch (Exception fechaTudo) {
            fechaTudo.getMessage();
        }
    }

    @Override
    public void run() {
        try {
            nomeJogador = entrada.readLine();
            transmissaoManipulador("Jogador conectado");
            System.out.println(nomeJogador + " entrou no jogo!");

            String nomeAdversario;
            boolean modoPVP = !Boolean.parseBoolean(entrada.readLine());

            if(modoPVP){
                lista_de_conexoes.add(this);
                int indice = lista_de_conexoes.indexOf(this);

                while(true){
                    System.out.println(" ");
                    
                    if(lista_de_conexoes.size() % 2 == 0){
                        adversarioManipulador = indice % 2 == 0 ? lista_de_conexoes.get(indice + 1) : lista_de_conexoes.get(indice - 1);
                        break;
                    }
                } nomeAdversario = adversarioManipulador.nomeJogador;
            } else {
                nomeAdversario = "PC";
            } transmissaoManipulador(nomeAdversario);

            do {
                if(!modoPVP){
                    escolhaPar = Boolean.parseBoolean(entrada.readLine()); 
                    transmissaoManipulador(String.valueOf(!escolhaPar)); 

                    numeroAleatorioPar = Integer.parseInt(entrada.readLine()); 
                    transmissaoManipulador(String.valueOf(new Random().nextInt(6))); 

                    pgtjogarDnv = Boolean.parseBoolean(entrada.readLine());
                    transmissaoManipulador(String.valueOf(pgtjogarDnv));
                } else {
                    escolhaParRecebido = false;
                    numeroAleatorioRecebidoPar = false;
                    jogarDnvRecebido = false;

                    do {
                        escolhaPar = Boolean.parseBoolean(entrada.readLine());
                        escolhaParRecebido = true;

                        while(true) {
                            System.out.println(" ");
                            if(adversarioManipulador.escolhaParRecebido){
                                transmissaoManipulador(String.valueOf(adversarioManipulador.escolhaPar));
                                break;

                            }
                        } escolhaParRecebido = false;
                    } while (escolhaPar == adversarioManipulador.escolhaPar);
                    
                    
                    numeroAleatorioPar = Integer.parseInt(entrada.readLine());
                    numeroAleatorioRecebidoPar = true;

                    while(true){
                        System.out.println(" ");
                        if(adversarioManipulador.numeroAleatorioRecebidoPar){
                            transmissaoManipulador(String.valueOf(adversarioManipulador.numeroAleatorioPar));
                            break;
                        }
                    }

                    pgtjogarDnv = Boolean.parseBoolean(entrada.readLine());
                    jogarDnvRecebido = true;

                    while(true){
                        System.out.println("");
                        if(adversarioManipulador.jogarDnvRecebido){
                            if(!adversarioManipulador.pgtjogarDnv) pgtjogarDnv = false;
                            break;
                        }
                    }
                    transmissaoManipulador(String.valueOf(pgtjogarDnv));
                } 
            }   while(pgtjogarDnv);
                fecharTudo();

        } catch (IOException manipulador) {
            fecharTudo();
            manipulador.getMessage();
        }
    }

}
