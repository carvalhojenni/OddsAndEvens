import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    
    private static final Scanner scannerC = new Scanner(System.in);
    private static BufferedWriter saida;
    
    private static void transmissaoManipulador(String msg) throws IOException {
        saida.write(msg);
        saida.newLine();
        saida.flush();
    }

    private static int validarModoJogo(int valorMin, int valorMax, String entradaInvalida) {
        int entrada = scannerC.nextInt();
        while (entrada < valorMin || entrada > valorMax) {
            System.out.print(entradaInvalida);
            entrada = scannerC.nextInt();
        }
        return entrada;
    }

    public static void main(String[] args) throws IOException {

        int porta = 7777, roundAtual =0;
        String Jogador;
        String Adversario;
        boolean jogadorPar;
        boolean adversarioPar;
        boolean modoCONTRAPC;
        boolean pgtjogarDnv; 
        int numeroJogador, numeroAdversario;
        int jogadorPontuacao = 0;
        int adversarioPontuacao = 0;
        BufferedReader entrada;
        
        System.out.print("Digite seu nome: ");
        Jogador = scannerC.nextLine();

        Socket socket = new Socket("127.0.0.1", porta);

        
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        saida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        transmissaoManipulador(Jogador);
        System.out.println(entrada.readLine());

        System.out.println("\n*************** BEM VINDO AO JOGO DE PAR OU IMPAR ***************\n");
        System.out.printf("\nDesenvolvido por FERNANDA AMORIM || RA: 125111370917\n");
        System.out.printf("\nDesenvolvido por JENNIFER CARVALHO || RA: 125111371396\n");
        System.out.printf("\nDesenvolvido por GABRIEL MONTEFUSCO || RA: 125111362412\n");
        System.out.printf("\nDesenvolvido por DIEGO ABNER || RA: 1252225462\n");
        System.out.printf("\nDesenvolvido por GABRIELA GODOY || RA: 125111370068\n");
        System.out.printf("\nDesenvolvido por LARYSSA BEATRIZ || RA: 125111348034\n");

        System.out.println("\n*************** BEM VINDO AO JOGO DE PAR OU IMPAR ***************\n 1 - Contra PC \n 2 - PvP");

        
        System.out.println("Por favor, defina um Modo de Jogo: ");
        modoCONTRAPC = validarModoJogo(1, 2, "Modo Jogo escolhido invalido! Insira 1 (Contra PC) ou 2 (PvP)") == 1;
        transmissaoManipulador(String.valueOf(modoCONTRAPC));

        
        System.out.println("Aguardando um adversario...");
        Adversario = entrada.readLine(); 
        System.out.println("Seu adversario é "+ Adversario); 

        do{
            
            System.out.println("\n ROUND " + ++roundAtual);
            do{
                
                System.out.println("Escolha [1] IMPAR ou [2] PAR");
                jogadorPar = validarModoJogo(1, 2, "\n Escolha invalido!\n Insira uma escolha valida! [1] (PAR) ou [2] (IMPAR)" ) ==2;
                transmissaoManipulador(String.valueOf(jogadorPar));
                System.out.println("Aguardando " + Adversario + " fazer sua escolha");

                
                adversarioPar = Boolean.parseBoolean(entrada.readLine());
                if(adversarioPar){
                    System.out.println(Adversario + " escolheu ser PAR..");
                } else {
                    System.out.println(Adversario + " escolheu ser IMPAR..");
                }

            } while(jogadorPar == adversarioPar);

            
            System.out.println("Agora, escolha um numero entre 0 e 5");
            numeroJogador = validarModoJogo(0, 5 , "\nNumero invalido!\n Insira um numero entre 0 e 5: ");
            transmissaoManipulador(String.valueOf(numeroJogador));
            System.out.println("Aguardando " + Adversario + " inserir um numero..");


            
            numeroAdversario = Integer.parseInt(entrada.readLine());
            System.out.println(Adversario + " inseriu o numero: " + numeroAdversario); 

            
            if(jogadorPar == ((numeroAdversario + numeroJogador) % 2 == 0)){
                System.out.println("Voce ganhou! :)");
                jogadorPontuacao++;

            }else{
                System.out.println("Voce perdeu! :(");
                adversarioPontuacao++;
            }

            System.out.println("\n**** Placar de Pontuacao ****\n");
            System.out.println("Você: "+ jogadorPontuacao);
            System.out.println(Adversario + ": "+ adversarioPontuacao);

            System.out.println("\nVocê quer jogar novamente?\n [1] SIM ou [2] NÃO");
            transmissaoManipulador(String.valueOf(scannerC.nextInt() == 1));
            System.out.println("Aguardando " + Adversario + " fazer sua escolha");
            
            pgtjogarDnv = Boolean.parseBoolean(entrada.readLine());

        } while (pgtjogarDnv);
    }
    
}