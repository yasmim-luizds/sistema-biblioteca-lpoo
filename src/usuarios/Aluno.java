package usuarios;

public class Aluno extends Usuario {

    private int matricula;
    private String curso;
    private int periodo;

    public Aluno(String nome, String email, String username, String senha,
            int matricula, String curso, int periodo) {
        super(nome, email, username, senha);
        this.matricula = matricula;
        this.curso = curso;
        this.periodo = periodo;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    @Override
    public boolean autenticar(String senha) {
        return this.senha != null && this.senha.equals(senha);
    }

    @Override
    public int getPrazoEmprestimoDias() {
        return 7;
    }

    @Override
    public double getMultaPorDia() {
        return 2.0;
    }

    @Override
    public boolean podeEmprestar() {
        return getQtdeEmprestimosAtivos() < 2;
    }

    @Override
    public String toString() {
        return "Aluno{"
                + "nome='" + nome + '\''
                + ", matricula=" + matricula
                + ", curso='" + curso + '\''
                + ", periodo=" + periodo
                + ", login='" + username + '\''
                + '}';
    }
}
