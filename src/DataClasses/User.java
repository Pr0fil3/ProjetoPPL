package DataClasses;

import java.io.Serializable;

/**
 * Classe User
 */
public class User implements Serializable {

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

    public User(String nome, String password, boolean privilegios) {
        this(0, nome, password, privilegios);
    }

    public User(int id, String password) {
        this(id, "", password, false);
    }

    public User(String nome, String password) {
        this(nome, password, false);
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

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPrivilegios() {
        return privilegios;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
