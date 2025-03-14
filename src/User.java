import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    String nome;
    String password;

    public User(String nome, String password) {
        this.nome = nome;
        this.password = password;
    }

        // Getters para acessar os valores após desserialização
    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }
}
