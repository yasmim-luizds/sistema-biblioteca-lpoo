package Pessoas;
import sistema.*;

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
    
    //Aprova um empréstimo pendente
    public void aprovarEmprestimo(Emprestimo e) {
        if (!e.getStatus().equals("PENDENTE")) {
            System.out.println("Empréstimo já está aprovado ou devolvido.");
            return;
        }

        e.aprovar();
        e.getLivro().setDisponivel(false);
        e.getUsuario().incrementarEmprestimos();
    }

    //Registra devolução e atualiza status e disponibilidade
    public void registrarDevolucao(Emprestimo e) {
        if (!e.getStatus().equals("APROVADO")) {
            System.out.println("Empréstimo não está aprovado.");
            return;
        }

        e.devolver(LocalDate.now());
        e.getUsuario().reduzirEmprestimos();
        e.getLivro().setDisponivel(true);
    }

    //Cadastra um novo livro no sistema
    public void cadastrarLivro(SistemaBiblioteca sistema, Livro novo) {
        sistema.getLivros().add(novo);
        sistema.salvarLivros();
    }

    //Remove um livro caso não esteja emprestado
    public void removerLivro(SistemaBiblioteca sistema, Livro livro) {
        if (!livro.isDisponivel()) {
            System.out.println("Livro não pode ser removido: está emprestado.");
            return;
        }

        sistema.getLivros().remove(livro);
        sistema.salvarLivros();
    }

    //Atualiza informações de um livro
    public void atualizarLivro(Livro livro, String titulo, String autor) {
        if (titulo != null) {
            livro.setTitulo(titulo);
        }
        if (autor != null) {
            livro.setAutor(autor);
        }
    }
    
}