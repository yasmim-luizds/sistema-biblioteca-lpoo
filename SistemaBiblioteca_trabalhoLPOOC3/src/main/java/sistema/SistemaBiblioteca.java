package Sistema;

import java.util.*; //importa vários util
import java.time.LocalDate; // importa a data local
import Pessoas.*; //importa todas as classes do package usuarios

public class SistemaBiblioteca {
    private List<Livro> livros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();

    private static final String ARQ_LIVROS = "livros.txt";
    private static final String ARQ_USUARIOS = "usuarios.txt";
    private static final String ARQ_EMPRESTIMOS = "emprestimos.txt";

    public SistemaBiblioteca() {
        this.livros = Arquivo.carregarLivros(ARQ_LIVROS);
        this.usuarios = Arquivo.carregarUsuarios(ARQ_USUARIOS);
        this.emprestimos = Arquivo.carregarEmprestimos(ARQ_EMPRESTIMOS, livros, usuarios);
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

    // solicitar empréstimo: verifica limite via polimorfismo e cria PENDENTE ou aprova se solicitante for Admin
    public void solicitarEmprestimo(Usuario solicitante, int isbn) throws Exception {
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

        // se Admin (operador), aprova direto
        if (solicitante instanceof Admin) {
            e.aprovar();
            solicitante.incrementarEmprestimos();
            livro.setDisponivel(false);
        }

        emprestimos.add(e);
        Arquivo.gravarEmprestimos(ARQ_EMPRESTIMOS, emprestimos);
        Arquivo.alterarDisponibilidadeLivro(ARQ_LIVROS, isbn, livro.isDisponivel());
    }

    // aprovar um empréstimo pendente (feito por um Admin)
    public boolean aprovarEmprestimo(Emprestimo emprestimo, Admin admin) throws Exception {
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
        emprestimo.getUsuario().reduzirEmprestimos();
        Arquivo.gravarEmprestimos(ARQ_EMPRESTIMOS, emprestimos);
        Arquivo.gravarLivros(ARQ_LIVROS, livros);
        return true;
    }

    public List<Livro> listarLivros() {
        return Collections.unmodifiableList(livros);
    }

    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Emprestimo> listarEmprestimos() {
        return Collections.unmodifiableList(emprestimos);
    }

    public Emprestimo buscarEmprestimoPorUsuarioISBN(Usuario u, int isbn) {
        for (Emprestimo e : emprestimos) {
            if (e.getUsuario().equals(u) && e.getLivro().getIsbn() == isbn && !"DEVOLVIDO".equals(e.getStatus())) {
                return e;
            }
        }
        return null;
    }

    // expor listas para Admin usar (remover/cadastrar)
    public List<Livro> getLivros() {
        return livros;
    }
    
}
