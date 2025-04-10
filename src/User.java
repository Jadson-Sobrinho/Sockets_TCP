import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    String password;
    public static List<User> userList = new ArrayList<>();
    

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(){
            if (userList == null) {
            userList = new ArrayList<>();
        }
    }

    // Getters para acessar os valores após desserialização
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean userValidation(String name, String password) {
        

        for(User users: userList) {
            if(users.getName().equals(name) && users.getPassword().equals(password)){
                System.out.print("Usuario cadastrado");
                return true;
            }
        }
        System.out.print("Usuario não cadastrado");
        return false;

        
    }
}

