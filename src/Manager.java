import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;



public class Manager {
    static final Scanner scanner = new Scanner(System.in);
    public static String nome;
    public static String password;

    public static void listarArquivos(String nomePessoa){
        String basePath = "storage";
   
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
}

    public static void baixarArquivos(ObjectOutputStream saida, ObjectInputStream entrada){
       
       try {
            // 1) envia comando de download
            saida.writeObject("DOWNLOAD");

            // 2) pergunta qual arquivo baixar
            System.out.print("Digite o nome do arquivo (ex: documento.pdf): ");
            String nomeArquivoDownload = scanner.nextLine();
            saida.writeObject(nomeArquivoDownload);

            // 3) lê o status
            String statusDownload = (String) entrada.readObject();
            if ("OK".equals(statusDownload)) {
                // 4) lê o tamanho e os bytes
                int tamanho = entrada.readInt();
                byte[] dados = new byte[tamanho];
                entrada.readFully(dados);

                // 5) salva em disco (pasta downloads/)
                Path destino = Paths.get("downloads", nomeArquivoDownload);
                Files.createDirectories(destino.getParent());
                Files.write(destino, dados);

                System.out.println("Arquivo salvo em: " + destino.toAbsolutePath());
            } else {
                System.out.println(statusDownload);
            }
        } catch (IOException e) {
            System.err.println("Erro de comunicação: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Erro no formato dos dados recebidos.");
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }

    }

    public static void enviarArquivo(ObjectOutputStream saida) {
        Frame parentFrame = null;
        try {
            // 1) preparamos o frame invisível e o diálogo de seleção
            parentFrame = new Frame();
            parentFrame.setVisible(false);

            FileDialog fileDialog = new FileDialog(parentFrame, "Selecione um arquivo", FileDialog.LOAD);
            fileDialog.setFilenameFilter((dir, name) ->
                name.endsWith(".jpg") ||
                name.endsWith(".jpeg") ||
                name.endsWith(".pdf") ||
                name.endsWith(".txt")
            );
            fileDialog.setVisible(true);

            String nomeArquivo = fileDialog.getFile();
            String diretorio = fileDialog.getDirectory();

            if (nomeArquivo == null) {
                System.out.println("Nenhum arquivo foi selecionado.");
                return;
            }

            File arquivoSelecionado = new File(diretorio, nomeArquivo);
            System.out.println("Arquivo selecionado: " + arquivoSelecionado.getAbsolutePath());

            // 2) envia comando de upload e o nome do arquivo
            saida.writeObject("UPLOAD");
            saida.writeObject(nomeArquivo);

            // 3) lê todos os bytes do arquivo
            byte[] conteudo = Files.readAllBytes(arquivoSelecionado.toPath());

            // 4) envia o tamanho e os bytes
            saida.writeInt(conteudo.length);
            saida.write(conteudo);
            saida.flush();

            System.out.println("Arquivo enviado com sucesso: " + nomeArquivo);

        } catch (IOException ex) {
            System.err.println("Erro de I/O ao enviar arquivo: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // 5) garante que o frame seja sempre descartado
            if (parentFrame != null) {
                parentFrame.dispose();
            }
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