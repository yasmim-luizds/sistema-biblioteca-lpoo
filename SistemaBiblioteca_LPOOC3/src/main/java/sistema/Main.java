package sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import usuarios.Admin;
import usuarios.Aluno;
import usuarios.Professor;
import usuarios.Usuario;

public class Main {

    private static final Scanner teclado = new Scanner(System.in);

    // Login fixo do admin/bibliotecário
    private static final String ADMIN_LOGIN = "bibliotecarioadm";
    private static final String ADMIN_SENHA = "senhabibliotecaadm";

    public static void main(String[] args) {
        SistemaBiblioteca sistema = new SistemaBiblioteca();

        while (true) {
            System.out.println("\n|------ Bem-vindo à Biblioteca ------|");
            System.out.println("| 1 - Login                          |");
            System.out.println("| 2 - Sair                           |");
            System.out.println("|------------------------------------|");
            System.out.print("Escolha uma opção: ");

            int opcao;
            try {
                opcao = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 ->
                    realizarLogin(sistema);
                case 2 -> {
                    System.out.println("Saindo do sistema...");
                    sistema.salvarTudo();
                    return;
                }
                default ->
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    // -------------------- LOGIN --------------------
    private static void realizarLogin(SistemaBiblioteca sistema) {
        System.out.print("\nLogin: ");
        String login = teclado.nextLine();
        System.out.print("Senha: ");
        String senha = teclado.nextLine();

        // 1) Testa login de Admin fixo
        if (login.equals(ADMIN_LOGIN) && senha.equals(ADMIN_SENHA)) {
            Admin admin = new Admin("Administrador", "admin@biblioteca.com",
                    ADMIN_LOGIN, ADMIN_SENHA);
            System.out.println("Login como ADMIN/Bibliotecário realizado com sucesso.");
            menuAdmin(sistema, admin);
            return;
        }

        // 2) Tenta autenticar como usuário (Aluno/Professor)
        Usuario usuario = sistema.validarLogin(login, senha);
        if (usuario == null) {
            System.out.println("Login ou senha inválidos.");
            return;
        }

        System.out.println("Login realizado como " + usuario.getClass().getSimpleName()
                + " - " + usuario.getNome());

        menuUsuario(sistema, usuario);
    }

    // -------------------- MENU ADMIN --------------------
    private static void menuAdmin(SistemaBiblioteca sistema, Admin admin) {
        while (true) {
            System.out.println("\n|------ Área do ADMIN / Bibliotecário ------|");
            System.out.println("1  - Cadastrar livro");
            System.out.println("2  - Cadastrar aluno");
            System.out.println("3  - Cadastrar professor");
            System.out.println("4  - Listar livros");
            System.out.println("5  - Listar usuários");
            System.out.println("6  - Listar empréstimos");
            System.out.println("7  - Aprovar empréstimo pendente");
            System.out.println("8  - Registrar devolução");
            System.out.println("9  - Remover livro");
            System.out.println("10 - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao;
            try {
                opcao = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 ->
                    cadastrarLivro(sistema, admin);
                case 2 ->
                    cadastrarAluno(sistema);
                case 3 ->
                    cadastrarProfessor(sistema);
                case 4 ->
                    listarLivros(sistema);
                case 5 ->
                    listarUsuarios(sistema);
                case 6 ->
                    listarEmprestimos(sistema);
                case 7 ->
                    aprovarEmprestimoPendente(sistema, admin);
                case 8 ->
                    registrarDevolucaoAdmin(sistema, admin);
                case 9 ->
                    removerLivro(sistema, admin);
                case 10 -> {
                    System.out.println("Saindo da área do ADMIN...");
                    sistema.salvarTudo();
                    return;
                }
                default ->
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    // --------- Funções do Admin ---------
    private static void cadastrarLivro(SistemaBiblioteca sistema, Admin admin) {
        System.out.print("Título do livro: ");
        String titulo = teclado.nextLine();

        System.out.print("Autor do livro: ");
        String autor = teclado.nextLine();

        System.out.print("ISBN do livro (apenas números): ");
        int isbn;
        try {
            isbn = Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ISBN inválido.");
            return;
        }

        Livro novo = new Livro(titulo, autor, isbn, true);
        admin.cadastrarLivro(sistema, novo);
        sistema.salvarTudo();
        System.out.println("Livro cadastrado com sucesso.");
    }

    private static void cadastrarAluno(SistemaBiblioteca sistema) {
        System.out.print("Nome do aluno: ");
        String nome = teclado.nextLine();

        System.out.print("E-mail: ");
        String email = teclado.nextLine();

        System.out.print("Login (username): ");
        String login = teclado.nextLine();

        System.out.print("Senha: ");
        String senha = teclado.nextLine();

        System.out.print("Matrícula: ");
        int matricula;
        try {
            matricula = Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Matrícula inválida.");
            return;
        }

        System.out.print("Curso: ");
        String curso = teclado.nextLine();

        System.out.print("Período: ");
        int periodo;
        try {
            periodo = Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Período inválido.");
            return;
        }

        Aluno aluno = new Aluno(nome, email, login, senha, matricula, curso, periodo);
        sistema.adicionarUsuario(aluno);
        sistema.salvarTudo();
        System.out.println("Aluno cadastrado com sucesso.");
    }

    private static void cadastrarProfessor(SistemaBiblioteca sistema) {
        System.out.print("Nome do professor: ");
        String nome = teclado.nextLine();

        System.out.print("E-mail: ");
        String email = teclado.nextLine();

        System.out.print("Login (username): ");
        String login = teclado.nextLine();

        System.out.print("Senha: ");
        String senha = teclado.nextLine();

        System.out.print("Matrícula: ");
        int matricula;
        try {
            matricula = Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Matrícula inválida.");
            return;
        }

        Professor professor = new Professor(nome, email, login, senha, matricula);
        sistema.adicionarUsuario(professor);
        sistema.salvarTudo();
        System.out.println("Professor cadastrado com sucesso.");
    }

    private static void listarLivros(SistemaBiblioteca sistema) {
        System.out.println("\n--- Livros cadastrados ---");
        for (Livro l : sistema.listarLivros()) {
            System.out.println(l);
        }
    }

    private static void listarUsuarios(SistemaBiblioteca sistema) {
        System.out.println("\n--- Usuários cadastrados ---");
        for (Usuario u : sistema.listarUsuarios()) {
            System.out.println(u);
        }
    }

    private static void listarEmprestimos(SistemaBiblioteca sistema) {
        System.out.println("\n--- Empréstimos ---");
        for (Emprestimo e : sistema.listarEmprestimos()) {
            System.out.println(e);
        }
    }

    private static void aprovarEmprestimoPendente(SistemaBiblioteca sistema, Admin admin) {
        List<Emprestimo> pendentes = new ArrayList<>();
        for (Emprestimo e : sistema.listarEmprestimos()) {
            if ("PENDENTE".equals(e.getStatus())) {
                pendentes.add(e);
            }
        }

        if (pendentes.isEmpty()) {
            System.out.println("Não há empréstimos pendentes.");
            return;
        }

        System.out.println("\n--- Empréstimos pendentes ---");
        for (int i = 0; i < pendentes.size(); i++) {
            System.out.println((i + 1) + " - " + pendentes.get(i));
        }

        System.out.print("Digite o número do empréstimo para aprovar: ");
        int escolha;
        try {
            escolha = Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
            return;
        }

        if (escolha < 1 || escolha > pendentes.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Emprestimo selecionado = pendentes.get(escolha - 1);
        // Usa métodos do Admin para mudar status e depois salva
        admin.aprovarEmprestimo(selecionado);
        sistema.salvarTudo();
        System.out.println("Empréstimo aprovado.");
    }

    private static void registrarDevolucaoAdmin(SistemaBiblioteca sistema, Admin admin) {
        List<Emprestimo> ativos = new ArrayList<>();
        for (Emprestimo e : sistema.listarEmprestimos()) {
            if ("APROVADO".equals(e.getStatus())) {
                ativos.add(e);
            }
        }

        if (ativos.isEmpty()) {
            System.out.println("Não há empréstimos em aberto (APROVADO).");
            return;
        }

        System.out.println("\n--- Empréstimos em aberto ---");
        for (int i = 0; i < ativos.size(); i++) {
            System.out.println((i + 1) + " - " + ativos.get(i));
        }

        System.out.print("Digite o número do empréstimo devolvido: ");
        int escolha;
        try {
            escolha = Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
            return;
        }

        if (escolha < 1 || escolha > ativos.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Emprestimo selecionado = ativos.get(escolha - 1);
        admin.registrarDevolucao(selecionado);
        sistema.salvarTudo();
        System.out.println("Devolução registrada com sucesso.");
    }

    private static void removerLivro(SistemaBiblioteca sistema, Admin admin) {
        listarLivros(sistema);
        System.out.print("Informe o ISBN do livro que deseja remover: ");
        int isbn;
        try {
            isbn = Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ISBN inválido.");
            return;
        }

        admin.removerLivro(sistema, isbn);
        sistema.salvarTudo();
    }

    // -------------------- MENU USUÁRIO (ALUNO/PROFESSOR) --------------------
    private static void menuUsuario(SistemaBiblioteca sistema, Usuario usuario) {
        while (true) {
            System.out.println("\n|------ Área do Usuário (" + usuario.getClass().getSimpleName() + ") ------|");
            System.out.println("1 - Listar livros");
            System.out.println("2 - Solicitar empréstimo");
            System.out.println("3 - Ver meus empréstimos");
            System.out.println("4 - Ver multas em aberto");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao;
            try {
                opcao = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 ->
                    listarLivros(sistema);
                case 2 ->
                    solicitarEmprestimoUsuario(sistema, usuario);
                case 3 ->
                    listarMeusEmprestimos(sistema, usuario);
                case 4 ->
                    verMultas(usuario, sistema);
                case 5 -> {
                    System.out.println("Saindo da área do usuário...");
                    sistema.salvarTudo();
                    return;
                }
                default ->
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    private static void solicitarEmprestimoUsuario(SistemaBiblioteca sistema, Usuario usuario) {
        System.out.print("Informe o ISBN do livro que deseja emprestar: ");
        int isbn;
        try {
            isbn = Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ISBN inválido.");
            return;
        }

        try {
            Emprestimo e = sistema.solicitarEmprestimo(usuario, isbn);
            System.out.println("Empréstimo solicitado com sucesso. Status atual: " + e.getStatus());
            sistema.salvarTudo();
        } catch (Exception ex) {
            System.out.println("Erro ao solicitar empréstimo: " + ex.getMessage());
        }
    }

    private static void listarMeusEmprestimos(SistemaBiblioteca sistema, Usuario usuario) {
        System.out.println("\n--- Meus empréstimos ---");
        for (Emprestimo e : sistema.listarEmprestimos()) {
            if (e.getUsuario().equals(usuario)) {
                System.out.println(e);
            }
        }
    }

    private static void verMultas(Usuario usuario, SistemaBiblioteca sistema) {
        double total = 0.0;

        System.out.println("\n--- Multas em aberto ---");
        for (Emprestimo e : sistema.listarEmprestimos()) {
            if (e.getUsuario().equals(usuario) && e.emAtraso()) {
                double multa = e.calcularMulta();
                total += multa;
                System.out.printf("%s -> Multa atual: R$ %.2f%n", e, multa);
            }
        }

        System.out.printf("Total de multas: R$ %.2f%n", total);
    }
}
