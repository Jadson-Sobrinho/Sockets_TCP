import java.util.HashMap;
import java.util.Map;

public class Manager {
    private Map<Integer, User> mapaLogin;

    // Construtor
    public Manager() {
        this.mapaLogin = new HashMap<>();
    }

    // Adicionar ou atualizar dados associados a um ID
    public void adicionarOuAtualizarDados(int id, User login) {
        mapaLogin.put(id, login);
        System.out.println("Dados adicionados/atualizados para ID: " + id);
    }

    // Consultar dados por ID
    public User consultarDados(int id) {
        return mapaLogin.get(id);
    }

    // Remover dados por ID
    public void removerDados(int id) {
        if (mapaLogin.remove(id) != null) {
            System.out.println("Dados removidos para ID: " + id);
        } else {
            System.out.println("Nenhum dado encontrado para ID: " + id);
        }
    }

    // Exibir todos os dados
    public void exibirTodosDados() {
        System.out.println("Dados armazenados:");
        for (Map.Entry<Integer, User> entry : mapaLogin.entrySet()) {
            System.out.println("ID: " + entry.getKey() + " -> " + entry.getValue());
        }
    }
}