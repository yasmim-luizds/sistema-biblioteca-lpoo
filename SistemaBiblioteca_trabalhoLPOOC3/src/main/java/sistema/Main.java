package sistema;

import Pessoas.Usuario;
import Pessoas.Aluno;
import java.util.*;

public class Main {

    static Scanner teclado = new Scanner(System.in);

    // Constantes para o login do bibliotecário
    private static final String BIBLIOTECARIO_LOGIN = "bibliotecarioadm";
    private static final String BIBLIOTECARIO_SENHA = "senhabibliotecaadm";

    public static void main(String[] args) {
        SistemaBiblioteca sistema = new SistemaBiblioteca();
        int opcao;

        while (true) {
            System.out.println("\n|------Bem-vindo à Biblioteca------|");
            System.out.println("|           1- Login               |");
            System.out.println("|           2- Sair                |");
            System.out.println("|----------------------------------|");
            System.out.println("Escolha uma opção: ");
            opcao = teclado.nextInt();
            teclado.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("\nDigite o login: ");
                    String login = teclado.nextLine();
                    System.out.println("Digite a senha: ");
                    String senha = teclado.nextLine();

                    // Primeiro acesso como bibliotecário
                    if (login.equals(BIBLIOTECARIO_LOGIN) && senha.equals(BIBLIOTECARIO_SENHA)) {
                        System.out.println("Acesso como Bibliotecário");
                        menuBibliotecario(sistema, null); //Tentar pedir o nome do bibliotecario ou algum outro dado
                    } else {
                        //valida como aluno
                        Usuario usuario = sistema.validarLogin(login, senha);
                        if (usuario instanceof Aluno) {
                            menuAluno(sistema, (Aluno) usuario);
                        } else if (usuario instanceof Bibliotecario) {
                            //valida como bibliotecario 
                            menuBibliotecario(sistema, (Bibliotecario) usuario);
                        } else {
                            System.out.println("Login inválido, tente novamente...");
                        }
                    }
                    break;
                case 2:
                    System.out.println("Saindo do sistema...");
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente...");
            }
        }
    }

    public static void menuAluno(SistemaBiblioteca sistema, Aluno aluno) {
        int opcao;

        while (true) {
            System.out.println("\n|------ Área do Aluno ------|");
            System.out.println("1- Ver livros");
            System.out.println("2- Ver empréstimos");
            System.out.println("3- Verificar disponibilidade dos livros");
            System.out.println("4- Sair");
            System.out.println("Escolha uma das opções: ");
            opcao = teclado.nextInt();
            teclado.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("Lista de livros: ");
                    for (Livro livro : sistema.listarLivros()) {
                        System.out.println(livro);
                    }
                    break;
                case 2:
                    System.out.println("\nMeus empréstimos: ");
                    for (Emprestimo emprestimo : sistema.listarEmprestimos()) {
                        if (emprestimo.getAluno().equals(aluno)) {
                            System.out.println(emprestimo);

                        }
                    }
                    break;
                case 3:
                    System.out.println("Digite o ISBN do livro que deseja consultar: ");
                    int isbnConsulta = teclado.nextInt();
                    teclado.nextLine();
                    sistema.verificarDisponibilidade(isbnConsulta);
                    break;
                case 4:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente...");
            }
        }
    }

    public static void menuBibliotecario(SistemaBiblioteca sistema, Bibliotecario bibliotecario) {
        int opcao;

        while (true) {
            System.out.println("\n|------ Área do Bibliotecário ------|");
            System.out.println("1- Cadastrar livro");
            System.out.println("2- Cadastrar aluno");
            System.out.println("3- Cadastrar bibliotecário");
            System.out.println("4- Ver alunos cadastrados");
            System.out.println("5- Ver livros cadastrados");
            System.out.println("6- Ver bibliotecários cadastrados");
            System.out.println("7- Registrar empréstimo");
            System.out.println("8- Registrar devolução");
            System.out.println("9- Ver empréstimos");
            System.out.println("10- Remover livro");
            System.out.println("11- Sair");
            System.out.println("Escolha uma das opções: ");
            opcao = teclado.nextInt();
            teclado.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("Digite o título do livro: ");
                    String titulo = teclado.nextLine();
                    System.out.println("Digite o autor do livro: ");
                    String autor = teclado.nextLine();
                    System.out.println("Digite o ISBN do livro: ");
                    int isbn = teclado.nextInt();
                    teclado.nextLine();

                    Livro novoLivro = new Livro(titulo, autor, isbn, true);
                    sistema.adicionarLivro(novoLivro);
                    System.out.println("Livro cadastrado com sucesso.");
                    break;

                case 2:
                    System.out.println("Digite o nome do aluno: ");
                    String nomeAluno = teclado.nextLine();
                    System.out.println("Digite a idade: ");
                    int idade = teclado.nextInt();
                    teclado.nextLine();
                    System.out.println("Digite o sexo (M/F): ");
                    char sexo = teclado.nextLine().charAt(0);
                    System.out.println("Digite o login: ");
                    String loginAluno = teclado.nextLine();
                    System.out.println("Digite a senha (6 primeiros num. do CPF): ");
                    String senhaAluno = teclado.nextLine();
                    System.out.println("Digite a matrícula: ");
                    int matricula = teclado.nextInt();
                    teclado.nextLine();
                    System.out.println("Digite o curso: ");
                    String curso = teclado.nextLine();
                    System.out.println("Digite o período: ");
                    int periodo = teclado.nextInt();
                    teclado.nextLine();

                    Aluno novoAluno = new Aluno(nomeAluno, idade, sexo, loginAluno, senhaAluno, matricula, curso, periodo);
                    sistema.adicionarAluno(novoAluno);
                    System.out.println("Aluno cadastrado com sucesso.");
                    break;

                case 3:
                    System.out.println("Digite o nome: ");
                    String nomeBib = teclado.nextLine();
                    System.out.println("Digite a idade: ");
                    int idadeBib = teclado.nextInt();
                    System.out.println("Digite o sexo (M/F): ");
                    char sexoBib = teclado.next().charAt(0);
                    teclado.nextLine();
                    System.out.println("Digite o nome de login: ");
                    String loginBib = teclado.nextLine();
                    System.out.println("Digite a senha: ");
                    String senhaBib = teclado.nextLine();
                    System.out.println("Digite o código de funcionário: ");
                    int codigo = teclado.nextInt();

                    Bibliotecario novoBib = new Bibliotecario(nomeBib, idadeBib, sexoBib, loginBib, senhaBib, codigo);
                    sistema.adicionarBibliotecario(novoBib);
                    System.out.println("Novo bibliotecário adicionado com sucesso.");
                    break;

                case 4:
                    System.out.println("Lista de alunos cadastrados:");
                    for (Aluno aluno : sistema.listarAlunos()) {
                        System.out.println(aluno);
                    }
                    break;

                case 5:
                    System.out.println("Lista de livros cadastrados:");
                    for (Livro livro : sistema.listarLivros()) {
                        System.out.println(livro);
                    }
                    break;

                case 6:
                    System.out.println("Lista de bibliotecários cadastrados:");
                    for (Bibliotecario bib : sistema.listarBibliotecarios()) {
                        System.out.println(bib);
                    }
                    break;

                case 7:
                    List<Aluno> alunos = Arquivo.carregarAlunos("alunos.txt");
                    List<Livro> livros = Arquivo.carregarLivros("livros.txt");
                    // Verificar se há alunos e livros disponíveis.
                    if (alunos.isEmpty() || livros.isEmpty()) {
                        System.out.println("Erro: Alunos ou livros não carregados corretamente.");
                        return;
                    }
                    // Solicita o login do aluno.
                    System.out.println("Digite o login do aluno:");
                    String loginDigitado = teclado.nextLine();

                    // Busca o aluno pelo login.
                    Aluno alunoEncontrado = null;
                    for (Aluno aluno : alunos) {
                        if (aluno.getLogin().equals(loginDigitado)) {
                            alunoEncontrado = aluno;
                            break;
                        }
                    }
                    if (alunoEncontrado == null) {
                        System.out.println("Erro: Aluno não encontrado.");
                        return;
                    }
                    // Solicita o ISBN do livro.
                    System.out.println("Digite o ISBN do livro:");
                    int isbnDigitado = teclado.nextInt();

                    // Busca o livro pelo ISBN.
                    Livro livroEncontrado = null;
                    for (Livro livro : livros) {
                        if (livro.getIsbn() == isbnDigitado) {
                            livroEncontrado = livro;
                            break;
                        }
                    }
                    if (livroEncontrado == null) {
                        System.out.println("Erro: Livro não encontrado.");
                        return;
                    }
                    sistema.registrarEmprestimo(alunoEncontrado, livroEncontrado);

                    break;
                case 8:
                    // Solicitar o login do aluno
                    System.out.println("Digite o login do aluno:");
                    String loginDigitadoDev = teclado.nextLine();
                    // Buscar o aluno correspondente
                    Aluno alunoEncontradoDev = null;
                    for (Aluno aluno : sistema.listarAlunos()) {
                        if (aluno.getLogin().equals(loginDigitadoDev)) {
                            alunoEncontradoDev = aluno;
                            break;
                        }
                    }
                    if (alunoEncontradoDev == null) {
                        System.out.println("Erro: Aluno não encontrado.");
                        break;
                    }

                    // Solicitar o ISBN do livro
                    System.out.println("Digite o ISBN do livro:");
                    int isbnDigitadoDev = teclado.nextInt();
                    teclado.nextLine(); // Limpa o buffer do teclado

                    // Buscar o livro correspondente
                    Livro livroEncontradoDev = sistema.buscarLivroPorIsbn(isbnDigitadoDev);
                    if (livroEncontradoDev == null) {
                        System.out.println("Erro: Livro não encontrado.");
                        break;
                    }

                    // Verificar e registrar a devolução
                    sistema.registrarDevolucao(alunoEncontradoDev, livroEncontradoDev);
                    break;

                case 9:
                    System.out.println("Lista de empréstimos:");
                    for (Emprestimo emprestimo : sistema.listarEmprestimos()) {
                        System.out.println(emprestimo);
                    }
                    break;

                case 10:
                    System.out.println("Digite o ISBN do livro a ser removido:");
                    int isbnRemover = teclado.nextInt();
                    teclado.nextLine();
                    sistema.excluirLivro(isbnRemover);
                    break;

                case 11:
                    System.out.println("Saindo do menu do bibliotecário...");
                    return;

                default:
                    System.out.println("Opção inválida, tente novamente...");
            }
        }
    }
}
