package usuarios;
//Classe para os estudantes, que herda os atributos da classe Pessoa
public class Aluno extends Pessoa {
	private int matricula;
	private String curso;
	private int periodo;

	public Aluno(String nome, int idade, char sexo, String login, String senha, int matricula, String curso,
			int periodo) {
		super(nome, idade, sexo, login, senha);
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
	public String toString() {
		return "Nome: "+getNome() + "; Idade: " + getIdade() + "; Sexo:" + getSexo() + "; Login:" + getLogin() + "; Senha: " + getSenha() + "; Matrícula: " + matricula + "; Curso: " + curso + "; Período: " + periodo;
	}


}
