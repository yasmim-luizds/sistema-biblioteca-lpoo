package Pessoas;

public class Aluno extends Usuario {
    private int matricula;
    private String curso;

    public Aluno(int matricula, String curso, String nome, String email, String username, String senha) {
        super(nome, email, username, senha);
        this.matricula = matricula;
        this.curso = curso;
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
    
    @Override
    public boolean autenticar(String senha) {
	return this.senha != null && this.senha.equals(senha);
    }

    @Override
    public int getPrazoEmprestimoDias() {
        return 7;
    }     // Aluno: 7 dias

    @Override
    public double getMultaPorDia() {
        return 2.0;
    }
    
    @Override
    public boolean podeEmprestar() {
        return getQtdeEmprestimosAtivos() < 2;
    }
    
}
