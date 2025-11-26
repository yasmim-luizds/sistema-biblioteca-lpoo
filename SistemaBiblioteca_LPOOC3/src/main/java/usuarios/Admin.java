package usuarios;
public class Admin {

    protected String nome;
    protected String email;
    protected String username;
    protected String senha;

    public Admin(String nome, String email, String username, String senha) {
        this.nome = nome;
        this.email = email;
        this.username = username;
        this.senha = senha;
    }
    
    public Admin() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
