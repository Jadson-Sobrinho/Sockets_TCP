import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        try {
            // Cria um ServerSocket na porta 12345
            // Voce pode escolher outra, mas use maior que 1024
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor aguardando conexão...");

            // Aguarda um cliente se conectar
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado!");

            // Cria ObjectInputStream para receber objetos do cliente
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            // Cria ObjectOutputStream para enviar objetos para o cliente
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());

            // Recebe um objeto do cliente (neste caso, uma String)
           User mensagemRecebida = (User) entrada.readObject();

            System.out.println("Nome recebido: " + mensagemRecebida.getName());
            System.out.println("Senha recebida: " + mensagemRecebida.getPassword());

            User user = new User();

            if(user.userValidation(mensagemRecebida.getName(), mensagemRecebida.getPassword())){
                CreateDirectories(mensagemRecebida.getName());
            }
            else{
                user.userList.add(mensagemRecebida);
                saida.writeObject("Usuário não encontrado. Usuário adicionado.");
            }
            
            // Fecha os recursos
            entrada.close();
            saida.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void CreateDirectories(String name) {
        Scanner scanner = new Scanner(System.in);
        String basePath = "storage";
        String[] typeFile = {"PDF", "JPG", "TXT"}; 

        //Esse for vai rodar a quantidade de strings que estiverem no array typefile
        for (String type : typeFile) {
            Path caminho = Paths.get(basePath, name, type);
            try {
                Files.createDirectories(caminho);
                System.out.println("Diretório criado: " + caminho);
            } catch (IOException e) {
                System.err.println("Erro ao criar diretório: " + caminho);
                e.printStackTrace();
            }
        }


    }

}
