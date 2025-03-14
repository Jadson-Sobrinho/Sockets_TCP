import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    String password;
    

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(){

    }

        // Getters para acessar os valores após desserialização
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    List<User> userList;

    public boolean userValidation(String name, String password) {
        

        for(User users: userList) {
            if(users.getName().equals(name) && users.getPassword().equals(name)){
                return true;
            }
        }
        return false;

        
    }
}

