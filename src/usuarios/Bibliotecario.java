package usuarios;

public class Bibliotecario extends Pessoa {
    protected int codigoFuncionario;

    public Bibliotecario(String nome, int idade, char sexo, String login, String senha, int codigoFuncionario) {
        super(nome, idade, sexo, login, senha);
        this.codigoFuncionario = codigoFuncionario;
    }


    public int getCodigoFuncionario() {
        return codigoFuncionario;
    }
    public void setCodigoFuncionario(int codigoFuncionario) {
        this.codigoFuncionario = codigoFuncionario;
    }


    @Override
    public boolean autenticar(String senha) {
        return this.senha != null && this.senha.equals(senha);
    }

 


}
