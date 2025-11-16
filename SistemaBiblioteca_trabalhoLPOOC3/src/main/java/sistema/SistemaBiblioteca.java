package Sistema;

import java.util.*; //importa vários util
import java.time.LocalDate; // importa a data local
import Pessoas.*; //importa todas as classes do package usuarios

public class SistemaBiblioteca {
    private List<Livro> livros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();

    private static final String ARQ_LIVROS = "livros.csv";
    private static final String ARQ_USUARIOS = "usuarios.csv";
    private static final String ARQ_EMPRESTIMOS = "emprestimos.csv";

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

    // Usuário solicita empréstimo — se livro disponível cria PENDENTE; se solicitação por bibliotecário aprova imediatamente
    public void solicitarEmprestimo(Usuario solicitante, int isbn) throws LivroIndisponivelException {
        Livro livro = buscarLivroPorIsbn(isbn);

        if (livro == null) {
            throw new LivroIndisponivelException("Livro não encontrado (ISBN " + isbn + ").");
        }
        if (!livro.isDisponivel()) {
            throw new LivroIndisponivelException("Livro indisponível no momento.");
        }

        //NOVA REGRA: polimorfismo de limite
        if (!solicitante.podeEmprestar()) {
            throw new LivroIndisponivelException(
                    "O usuário " + solicitante.getNome() + " atingiu o limite de empréstimos simultâneos."
            );
        }

        // Cria o empréstimo em estado PENDENTE por padrão
        Emprestimo e = new Emprestimo(livro, solicitante, LocalDate.now());

        // Se for bibliotecário → aprova imediatamente
        if (solicitante instanceof Bibliotecario) {
            e.aprovar();
            solicitante.incrementarEmprestimos(); // confirma contagem
            livro.setDisponivel(false);
        } else {
            // Se for usuário comum → empréstimo continua pendente
            // ainda não marca o livro indisponível
            // nem incrementa contagem de empréstimos
        }

        emprestimos.add(e);
        Arquivo.gravarEmprestimos(ARQ_EMPRESTIMOS, emprestimos);

        // Atualiza disponibilidade do livro no arquivo:
        // Livro fica indisponível APENAS se o empréstimo foi aprovado.
        boolean deveFicarDisponivel = !e.getStatus().equals("APROVADO");
        Arquivo.alterarDisponibilidadeLivro(ARQ_LIVROS, isbn, deveFicarDisponivel);
    }

    // Bibliotecario aprova um empréstimo pendente
    public boolean aprovarEmprestimo(Emprestimo emprestimo, Bibliotecario bibliotecario) throws EmprestimoException {
        if (!emprestimos.contains(emprestimo)) {
            throw new EmprestimoException("Empréstimo não encontrado");
        }
        if (!"PENDENTE".equals(emprestimo.getStatus())) {
            throw new EmprestimoException("Só é possível aprovar empréstimos em PENDENTE");
        }
        emprestimo.aprovar();
        Arquivo.gravarEmprestimos(ARQ_EMPRESTIMOS, emprestimos);
        Arquivo.alterarDisponibilidadeLivro(ARQ_LIVROS, emprestimo.getLivro().getIsbn(), false);
        return true;
    }

    public boolean registrarDevolucao(Emprestimo emprestimo) throws EmprestimoException {
        if (!emprestimos.contains(emprestimo)) {
            throw new EmprestimoException("Empréstimo não encontrado");
        }
        if ("DEVOLVIDO".equals(emprestimo.getStatus())) {
            throw new EmprestimoException("Já devolvido");
        }
        emprestimo.registrarDevolucao(LocalDate.now());
        Arquivo.gravarEmprestimos(ARQ_EMPRESTIMOS, emprestimos);
        Arquivo.gravarLivros(ARQ_LIVROS, livros);
        return true;
    }

    // buscas e listagens úteis
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
    
}
