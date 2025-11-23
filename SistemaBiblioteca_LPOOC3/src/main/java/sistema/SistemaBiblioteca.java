package sistema;

import java.time.LocalDate;
import java.util.ArrayList;
import usuarios.*;

public class SistemaBiblioteca {

    private ArrayList<Livro> livros;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Emprestimo> emprestimos;

    private static final String ARQ_LIVROS = "livros.txt";
    private static final String ARQ_USUARIOS = "usuarios.txt";
    private static final String ARQ_EMPRESTIMOS = "emprestimos.txt";

    public SistemaBiblioteca() {
        this.livros = new ArrayList<>(Arquivo.carregarLivros(ARQ_LIVROS));
        this.usuarios = new ArrayList<>(Arquivo.carregarUsuarios(ARQ_USUARIOS));
        this.emprestimos = new ArrayList<>(Arquivo.carregarEmprestimos(ARQ_EMPRESTIMOS, livros, usuarios));
    }

    public void salvarTudo() {
        Arquivo.gravarLivros(ARQ_LIVROS, livros);
        Arquivo.gravarUsuarios(ARQ_USUARIOS, usuarios);
        Arquivo.gravarEmprestimos(ARQ_EMPRESTIMOS, emprestimos);
    }

    public Usuario validarLogin(String login, String senha) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(login) && u.autenticar(senha)) {
                return u;
            }
        }
        return null;
    }

    public void adicionarLivro(Livro livro) {
        livros.add(livro);
        Arquivo.gravarLivros(ARQ_LIVROS, livros);
    }

    public void excluirLivro(int isbn) {
        livros.removeIf(l -> l.getIsbn() == isbn);
        Arquivo.gravarLivros(ARQ_LIVROS, livros);
    }

    public void adicionarUsuario(Usuario u) {
        usuarios.add(u);
        Arquivo.gravarUsuarios(ARQ_USUARIOS, usuarios);
    }

    public Livro buscarLivroPorIsbn(int isbn) {
        for (Livro l : livros) {
            if (l.getIsbn() == isbn) {
                return l;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorLogin(String login) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }
        return null;
    }

    public Emprestimo solicitarEmprestimo(Usuario solicitante, int isbn) throws Exception {
        Livro livro = buscarLivroPorIsbn(isbn);
        if (livro == null) {
            throw new Exception("Livro não encontrado (ISBN " + isbn + ").");
        }
        if (!livro.isDisponivel()) {
            throw new Exception("Livro indisponível no momento.");
        }

        if (!solicitante.podeEmprestar()) {
            throw new Exception("O usuário " + solicitante.getNome() + " atingiu o limite de empréstimos.");
        }

        Emprestimo e = new Emprestimo(livro, solicitante, LocalDate.now());
        emprestimos.add(e);
        Arquivo.gravarEmprestimos(ARQ_EMPRESTIMOS, emprestimos);
        return e;
    }

    public boolean aprovarEmprestimo(Emprestimo emprestimo) throws Exception {
        if (!emprestimos.contains(emprestimo)) {
            throw new Exception("Empréstimo não encontrado");
        }
        if (!"PENDENTE".equals(emprestimo.getStatus())) {
            throw new Exception("Só é possível aprovar empréstimos em PENDENTE");
        }
        emprestimo.aprovar();
        emprestimo.getUsuario().incrementarEmprestimos();
        Arquivo.gravarEmprestimos(ARQ_EMPRESTIMOS, emprestimos);
        Arquivo.alterarDisponibilidadeLivro(ARQ_LIVROS, emprestimo.getLivro().getIsbn(), false);
        return true;
    }

    public boolean registrarDevolucao(Emprestimo emprestimo) throws Exception {
        if (!emprestimos.contains(emprestimo)) {
            throw new Exception("Empréstimo não encontrado");
        }
        if ("DEVOLVIDO".equals(emprestimo.getStatus())) {
            throw new Exception("Já devolvido");
        }
        emprestimo.registrarDevolucao(LocalDate.now());
        emprestimo.getUsuario().decrementarEmprestimos();
        Arquivo.gravarEmprestimos(ARQ_EMPRESTIMOS, emprestimos);
        Arquivo.gravarLivros(ARQ_LIVROS, livros);
        return true;
    }

    public ArrayList<Livro> listarLivros() {
        return livros;
    }

    public ArrayList<Usuario> listarUsuarios() {
        return usuarios;
    }

    public ArrayList<Emprestimo> listarEmprestimos() {
        return emprestimos;
    }

    public Emprestimo buscarEmprestimoPorUsuarioISBN(Usuario u, int isbn) {
        for (Emprestimo e : emprestimos) {
            if (e.getUsuario().equals(u)
                    && e.getLivro().getIsbn() == isbn
                    && !"DEVOLVIDO".equals(e.getStatus())) {
                return e;
            }
        }
        return null;
    }
}
