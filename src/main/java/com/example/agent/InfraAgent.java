package com.example.agent; // Define o "endereço" da classe dentro do projeto

import java.io.File; // ferramenta para manipulação de arquivos
import java.io.FileWriter; // ferramenta para manipulação de arquivos
import java.io.IOException; // ferramenta para manipulação de arquivos
import java.net.InetSocketAddress; // ajuda a definir um endereço IP e porta
import java.net.Socket; //ferramenta para tentar conexões de rede
import java.time.LocalDateTime; // usado para registrar data e hora


//Classe principal e metodo main

//define o nome da classe principal
public class InfraAgent { 
    // define por padrão, intervalo de 30 seg
    public static final int DEFAULT_CHECK_INTERVAL_SECONDS = 30;
    //define por padrão a porta para monitorar: 8080
    public static final int DEFAULT_PORT = 8080;
    //define o nome padrão do arquivo de texto onde mostra as info
    public static final String DEFAULT_LOG_FILE = "health.log";

    //lê as variáveis de ambiente
    public static final int CHECK_INTERVAL_SECONDS = 
        Integer.parseInt(System.getenv().getOrDefault("CHECK_INTERVAL_SECONDS", String.valueOf(DEFAULT_CHECK_INTERVAL_SECONDS)));

    public static final int CHECK_PORT =
        Integer.parseInt(System.getenv().getOrDefault("CHECK_PORT", String.valueOf(DEFAULT_PORT)));

     public static final String LOG_FILE_PATH =
        System.getenv().getOrDefault("LOG_FILE_PATH", String.valueOf(DEFAULT_LOG_FILE));


    public static void main(String[] args){
        System.out.println("Infra Agent iniciado...");
        System.out.println("Intervalo de check: " + CHECK_INTERVAL_SECONDS + " segundos.");
        System.out.println("Porta monitorada: " + CHECK_PORT);
        System.out.println("Arquivo de log: " + LOG_FILE_PATH);
        //criação do looping inifito, para que o programa não pare de rodar
        while (true) {
            try{
                //cria a função que faz a verificação
                performHealthCheck(); 
                // faz com que o programa "hiberne"
                Thread.sleep(CHECK_INTERVAL_SECONDS * 1000l);
            } catch (InterruptedException e){ // caso force o programa a parar bruscamente
                System.out.println("Agente interrompido. Encerrando...");
                Thread.currentThread().interrupt(); //faz com que não force a thread a parar imediatamente
                break;
            } 
        }
    }
    private static void performHealthCheck(){
        // faz a checagem do espaço em disco
        long bytesLivres = new File("/").getUsableSpace();
        // para que seja realizado a conversão em MegaBytes
        long mbLivres = bytesLivres / (1024 * 1024);
        // chama uma função, tenta se conetcar na porta configurada, o tempo máximo é 1,5 seg
        boolean portOpen = isPortOpen("localhost", CHECK_PORT, 1500);
        // criar uma mensagem contendo cabecalho com as informações de hora, espaço e status da porta
        String log = "=======================================================================\n" + "Infra Agent Check: " + LocalDateTime.now() + "\n" + "Free disk space: " + mbLivres + "MB\n" + CHECK_PORT + " open: " + portOpen + "\n";
        // chama a função para salvar o texto em um arquivo mencionado com nome abaixo
        writeLog(LOG_FILE_PATH, log);

        //Escreve no SDOUT
        System.out.print(log);
    }

   // metodo criado para que seja feito o teste de rede
   private static boolean isPortOpen(String host, int port, int timeout){
        // tenta criar uma "tomada" de conexão de rede
        try (Socket socket = new Socket()) {
            //tenta se conectar ao endereço e portas informados
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e){
            return false;
        }
   }
   // metodo para função escrita de arquivo
   private static void writeLog(String fileName, String content) {
        //abre op arquivo health.log, acrescentando texto no final do arquivo em vez de apagar o que existia
        try (FileWriter fw = new FileWriter(fileName, true)){
            // escreve efetivamente a mensagem no log do arquivo
            fw.write(content);
        } catch (IOException e ){
            e.printStackTrace();
        }
   }

}
