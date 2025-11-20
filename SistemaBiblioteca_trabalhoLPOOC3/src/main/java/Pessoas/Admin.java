package Pessoas;
import java.time.LocalDate;
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
    
    // Aprova um empréstimo pendente: atualiza status, livro e contador do usuário
    public void aprovarEmprestimo(Emprestimo e) {
        if (!"PENDENTE".equals(e.getStatus())) {
            System.out.println("Empréstimo já está aprovado/recusado/devolvido.");
            return;
        }
        e.aprovar(); // seta status APROVADO e marca livro indisponível
        // incrementa contador do usuário associado ao empréstimo
        if (e.getUsuario() != null) {
            e.getUsuario().incrementarEmprestimos();
        }
    }

    // Registra devolução e atualiza status e disponibilidade
    public void registrarDevolucao(Emprestimo e) {
        if (!"APROVADO".equals(e.getStatus())) {
            System.out.println("Empréstimo não está em estado APROVADO.");
            return;
        }
        e.registrarDevolucao(LocalDate.now()); // seta data e status DEVOLVIDO
        if (e.getUsuario() != null) {
            e.getUsuario().reduzirEmprestimos();
        }
        // livro volta a estar disponível dentro de registrarDevolucao, mas garantir:
        if (e.getLivro() != null) {
            e.getLivro().setDisponivel(true);
        }
    }

    // Cadastra novo livro usando o sistema
    public void cadastrarLivro(SistemaBiblioteca sistema, Livro novo) {
        sistema.adicionarLivro(novo);
    }

    // Remove um livro caso não esteja emprestado
    public void removerLivro(SistemaBiblioteca sistema, int isbn) {
        Livro livro = sistema.buscarLivroPorIsbn(isbn);
        if (livro == null) {
            System.out.println("Livro não encontrado.");
            return;
        }
        if (!livro.isDisponivel()) {
            System.out.println("Livro não pode ser removido: está emprestado.");
            return;
        }
        sistema.excluirLivro(isbn);
    }

    // Atualiza informações de um livro (título/autor)
    public void atualizarLivro(Livro livro, String titulo, String autor) {
        if (livro == null) {
            return;
        }
        if (titulo != null && !titulo.isBlank()) {
            livro.setTitulo(titulo);
        }
        if (autor != null && !autor.isBlank()) {
            livro.setAutor(autor);
        }
    }
    
}