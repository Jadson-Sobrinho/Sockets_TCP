import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
            String mensagemRecebida = (String) entrada.readObject();
            // Exibe a mensagem recebida do cliente na tela
            System.out.println("Mensagem recebida do cliente: " + mensagemRecebida);

            // Monta a resposta que será enviado para o servidor
            String resposta = "Servidor recebeu: " + mensagemRecebida;
            // Escreve os dados na saída
            saida.writeObject(resposta);

            // Fecha os recursos
            entrada.close();
            saida.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
