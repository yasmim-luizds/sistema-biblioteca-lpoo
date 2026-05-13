package usuarios;

public class Professor extends Usuario {

    private int matricula;

    public Professor(String nome, String email, String username, String senha,
            int matricula) {
        super(nome, email, username, senha);
        this.matricula = matricula;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    @Override
    public boolean autenticar(String senha) {
        return this.senha != null && this.senha.equals(senha);
    }

    @Override
    public int getPrazoEmprestimoDias() {
        return 14;
    }

    @Override
    public double getMultaPorDia() {
        return 1.0;
    }

    @Override
    public boolean podeEmprestar() {
        return getQtdeEmprestimosAtivos() < 5;
    }

    @Override
    public String toString() {
        return "Professor{"
                + "nome='" + nome + '\''
                + ", matricula=" + matricula
                + ", login='" + username + '\''
                + '}';
    }
}
