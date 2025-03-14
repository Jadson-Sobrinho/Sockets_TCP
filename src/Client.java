import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            // Conecta ao servidor no localhost na porta 12345
            Socket socket = new Socket("127.0.0.1", 12345);
            System.out.println("Conectado ao servidor!");

            // Cria ObjectOutputStream para enviar objetos ao servidor
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            // Cria ObjectInputStream para receber objetos do servidor
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

            // Cria o objeto scanner será utilizado para leitura do teclado
            Scanner scanner = new Scanner(System.in);


            System.out.print("Digite seu nome: ");
 
            String nome = scanner.nextLine();
            System.out.print("Digite sua senha: ");
   
            String password = scanner.nextLine();
    
            scanner.close();

            User user = new User(nome, password);

            // Envia a mensagem como objeto String
            saida.writeObject(user);

            // Esse (String) é porque ele está convertendo o que vem do readObject para o formato String
            String resposta = (String) entrada.readObject();
            System.out.println("Resposta do servidor: " + resposta);


            // Fecha os recursos
            entrada.close();
            saida.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
