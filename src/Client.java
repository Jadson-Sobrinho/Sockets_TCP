import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
                System.out.println("1. Enviar arquivo");
                System.out.println("2. Baixar arquivo");
                System.out.println("3. Listar arquivos");
                System.out.println("4. Sair");
                System.out.print("Escolha: ");
                int opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        saida.writeObject("UPLOAD");
                        //Cria uma janela Frame invisível (necessária para o FileDialog)
                        Frame parentFrame = new Frame();
                        parentFrame.setVisible(false); // Não mostra o frame principal
                        //Cria o FileDialog no modo LOAD (para abrir arquivos)
                        FileDialog fileDialog = new FileDialog(parentFrame, "Selecione um arquivo", FileDialog.LOAD);
                        //filtrar por tipos de arquivo
                        fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".jpeg") || name.endsWith(".pdf") || name.endsWith(".txt"));
                        // Mostra a caixa de diálogo (bloqueante)
                        fileDialog.setVisible(true);
                        

                        String arquivo = fileDialog.getFile();
                        String diretorio = fileDialog.getDirectory();
                        
                        if (arquivo != null) {
                            File arquivoSelecionado = new File(diretorio, arquivo);
                            System.out.println("Arquivo selecionado: " + arquivoSelecionado.getAbsolutePath());
                            

                            processarArquivo(arquivoSelecionado);
                        } else {
                            System.out.println("Nenhum arquivo foi selecionado.");
                        }
                        
                        // Encerra a aplicação
                        parentFrame.dispose();
                        break;
                    
                    case 2:
                        String basePath = "storage";
                        String nomePessoa = nome;

                        String[] tipos = { "PDF", "TXT", "JPG" };

                        for (String tipo : tipos) {
                            File pasta = new File(basePath + "/" + nomePessoa + "/" + tipo);

                            System.out.println("Arquivos de " + tipo + " para " + nomePessoa + ":");

                            if (pasta.exists() && pasta.isDirectory()) {
                                File[] arquivos = pasta.listFiles();
                                if (arquivos != null && arquivos.length > 0) {
                                    for (File lista : arquivos) {
                                        if (lista.isFile()) {
                                            System.out.println(" - " + lista.getName());
                                        }
                                    }
                                } else {
                                    System.out.println(" (nenhum arquivo encontrado)");
                                }
                            } else {
                                System.out.println(" (pasta não existe)");
                            }

                            System.out.println();
                        }
                        break;

                    case 4:
                        saida.writeObject("EXIT");
                        ativo = false;
                        break;

                    default:
                        System.out.println("Opção inválida!");
                }
            }

            // Fecha os recursos
            entrada.close();
            saida.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processarArquivo(File arquivo) {

        String nomeArquivo = arquivo.getName().toLowerCase();

        File caminho = null;

        if (nomeArquivo.endsWith(".jpg") || nomeArquivo.endsWith(".jpeg")) {
            caminho = new File("storage/" + nome + "/JPG/" + arquivo.getName());

        } else if (nomeArquivo.endsWith(".pdf")) {
            caminho = new File("storage/" + nome + "/PDF/" + arquivo.getName());

        } else if (nomeArquivo.endsWith(".txt")) {
            caminho = new File("storage/" + nome + "/TXT/" + arquivo.getName());

        } else {
            System.out.println("Tipo de arquivo não suportado: " + nomeArquivo);
            return;
        }

        try {
            Files.copy(arquivo.toPath(), caminho.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo salvo com sucesso em: " + caminho.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}