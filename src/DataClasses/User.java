package DataClasses;

/**
 * Created by -nikeiZprooo- on 28/05/2014.
 */
public class User {
    private int id;
    private String nome;
    private String password;
    private boolean privilegios;

    public User(int id, String nome, String password, boolean privilegios) {
        this.id = id;
        this.nome = nome;
        this.password = password;
        this.privilegios = privilegios;
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public boolean isPrivilegios() {
        return privilegios;
    }
}
