package usuarios;

public abstract class Pessoa {
    protected String nome;
    protected int idade;
    protected char sexo;
    protected String login;
    protected String senha;

    public Pessoa(String nome, int idade, char sexo, String login, String senha) {
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.login = login;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }
    public void setIdade(int idade) {
        this.idade = idade;
    }

    public char getSexo() {
        return sexo;
    }
    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public abstract boolean autenticar(String senha);

}
