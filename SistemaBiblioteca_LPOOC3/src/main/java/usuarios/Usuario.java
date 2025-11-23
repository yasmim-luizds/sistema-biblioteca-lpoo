package usuarios;

public abstract class Usuario {

    protected String nome;
    protected String email;
    protected String username; // login
    protected String senha;
    private int qtdeEmprestimosAtivos;

    public Usuario(String nome, String email, String username, String senha) {
        this.nome = nome;
        this.email = email;
        this.username = username;
        this.senha = senha;
    }
    
    public String getLogin() {
        return username;
    }

    public void setLogin(String login) {
        this.username = login;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean autenticar(String senha) {
        return this.senha != null && this.senha.equals(senha);
    }

    public int getQtdeEmprestimosAtivos() {
        return qtdeEmprestimosAtivos;
    }

    public void incrementarEmprestimos() {
        qtdeEmprestimosAtivos++;
    }

    public void decrementarEmprestimos() {
        if (qtdeEmprestimosAtivos > 0) {
            qtdeEmprestimosAtivos--;
        }
    }

    // POLIMORFISMO
    public abstract boolean podeEmprestar();

    public abstract int getPrazoEmprestimoDias();

    public abstract double getMultaPorDia();
}
