import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
public class Client {
    public static String nome;
    public static String password;
    public static void main(String[] args) {
        try {
            // Conecta ao servidor no localhost na porta 12345
            Socket socket = new Socket("127.0.0.1", 12345);
            System.out.println("Conectado ao servidor!");

            // Cria ObjectOutputStream para enviar objetos ao servidor
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            // Cria ObjectInputStream para receber objetos do servidor
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

            
            Scanner scanner = new Scanner(System.in);

            System.out.print("Digite seu nome: ");
            nome = scanner.nextLine();

            System.out.print("Digite sua senha: ");
            password = scanner.nextLine();

            User user = new User(nome, password);
            saida.writeObject(user);


            // Esse (String) é porque ele está convertendo o que vem do readObject para o formato String
            String resposta = (String) entrada.readObject();
            System.out.println("Resposta do servidor: " + resposta);


            boolean ativo = true;
            while (ativo) {
                System.out.println("\n--- Menu ---");
                System.out.println("1. Listar arquivos");
                System.out.println("2. Baixar arquivo");
                System.out.println("3. Enviar arquivo");
                System.out.println("4. Sair");
                System.out.print("Escolha: ");
                int opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        Manager.listarArquivos(nome);
                        break;
                    case 2:
                        Manager.baixarArquivos(saida, entrada);
                        break;
                    case 3:
                        Manager.enviarArquivo(saida);
                        break;

                    case 4:
                        saida.writeObject("EXIT");
                        ativo = false;
                        break;

                    default:
                        System.out.println("Opção inválida!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 
}