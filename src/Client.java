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


            // Esse (String) é porque ele está convertendo o que vem do readObject para o formato String
            //String resposta = (String) entrada.readObject();
            //System.out.println("Resposta do servidor: " + resposta);
            
            Scanner scanner = new Scanner(System.in);
            System.out.println("1. Login");
            System.out.println("2. Cadastrar");
            System.out.println("3. Sair");
            int opcao1 = Integer.parseInt(scanner.nextLine());

            boolean ativo = false;

            switch (opcao1) {
                case 1:
                    System.out.print("Digite seu nome: ");
                    nome = scanner.nextLine();

                    System.out.print("Digite sua senha: ");
                    password = scanner.nextLine();

                    User user = new User(nome, password);
                    saida.writeObject("LOGIN");
                    saida.writeObject(user);
                    System.out.print("Enviando objeto ao servidor");
                    String respostaLogin = (String) entrada.readObject();

                    if ("OK".equals(respostaLogin)) {
                        ativo = true;
                        System.out.print("Resposta do servidor");
                    } else {
                        System.err.println("Usuario nao cadastrado. Cadastrar? [Y/N]: ");
                            String resposta = scanner.nextLine();
                            ativo = true;
                            if (resposta == "Y"){
                                saida.writeObject("REGISTER");
                            } else {
                                saida.writeObject("EXIT");
                            }
                    }
                    
                    break;
                case 2:
                    System.out.print("Digite seu nome: ");
                    nome = scanner.nextLine();

                    System.out.print("Digite sua senha: ");
                    password = scanner.nextLine();

                    User novoUsuario = new User(nome, password);
                    saida.writeObject("REGISTER"); 
                    saida.writeObject(novoUsuario);

                    String respostaCadastro = (String) entrada.readObject();
                    
                    if ("OK".equals(respostaCadastro)) {
                        ativo = true;
                        System.err.println("Usuario cadastrado");
                    }
                    break;
                case 3:
                    saida.writeObject("EXIT");
                    socket.close();
                    System.out.println("Conexão encerrada.");
                    return;
                default:
                    throw new AssertionError();
            }

            while (ativo) {
                System.out.println("\n--- Menu ---");
                System.out.println("1. Listar arquivos");
                System.out.println("2. Baixar arquivo");
                System.out.println("3. Enviar arquivo");
                System.out.println("4. Sair");
                System.out.print("Escolha: ");
                int opcao2 = Integer.parseInt(scanner.nextLine());

                switch (opcao2) {
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