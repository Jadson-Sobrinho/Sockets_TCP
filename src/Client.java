import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


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

            User user = new User();

            user = user.userInfo();

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
