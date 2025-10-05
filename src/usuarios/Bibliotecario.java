package usuarios;
public class Bibliotecario extends Pessoa { 
	private int codigo; 

	public Bibliotecario(String nome, int idade, char sexo, String login, String senha, int codigo) {
		super(nome, idade, sexo, login, senha);
		this.codigo = codigo;
	}

	public int getCodigo() { 
		return codigo; 
	} 

	public void setCodigo(int codigo) { 
		this.codigo = codigo; 
	} 

	@Override 
	public String toString() { 
		return "Codigo do Funcionário: " + codigo + "; Nome: " + getNome() + "; Sexo: " + getSexo() + "; Idade:" + getIdade(); 
	} 
}

