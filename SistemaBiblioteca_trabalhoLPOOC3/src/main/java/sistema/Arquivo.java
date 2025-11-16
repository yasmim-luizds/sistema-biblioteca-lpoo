package sistema;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import Pessoas.Aluno;
import Pessoas.Bibliotecario;

public class Arquivo {

    // Método para gravar livros no arquivo
    public static void gravarLivrosEmArquivo(String arquivo, List<Livro> livros) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Livro livro : livros) {
                // Gravando os livros no formato: titulo;autor;isbn;disponivel
                writer.write(livro.getTitulo() + ";" + livro.getAutor() + ";" + livro.getIsbn() + ";" + livro.isDisponivel());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao gravar livros no arquivo: " + e.getMessage());
        }
    }

    // Método para carregar os livros do arquivo
    public static List<Livro> carregarLivros(String arquivo) {
        List<Livro> livros = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(arquivo))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(";"); // Separando os campos por ponto e vírgula

                // Certificando-se de que temos todos os dados necessários
                if (partes.length == 4) {
                    String titulo = partes[0];
                    String autor = partes[1];
                    int isbn = Integer.parseInt(partes[2]);
                    boolean disponivel = Boolean.parseBoolean(partes[3]);

                    // Adicionando o livro à lista
                    livros.add(new Livro(titulo, autor, isbn, disponivel));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar livros: " + e.getMessage());
        }
        return livros;
    }

    // Método para gravar empréstimos no arquivo
    public static void gravarEmprestimosEmArquivo(String arquivo, List<Emprestimo> emprestimos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Emprestimo emprestimo : emprestimos) {
                // Gravando o empréstimo no formato: livro_isbn;pessoa_nome;data_emprestimo
                writer.write(emprestimo.getLivro().getIsbn() + ";"
                        + emprestimo.getAluno().getNome() + ";"
                        + emprestimo.getDataEmprestimo());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao gravar empréstimos no arquivo: " + e.getMessage());
        }
    }

    // Método para remover livro do arquivo
    public static void removerLivroDoArquivo(String arquivo, int isbn) {
        List<Livro> livros = carregarLivros(arquivo); // Carrega todos os livros
        livros.removeIf(livro -> livro.getIsbn() == isbn); // Remove o livro com o ISBN especificado

        // Regrava a lista de livros atualizada no arquivo
        gravarLivrosEmArquivo(arquivo, livros);
    }

    // Método para carregar empréstimos do arquivo
    public static List<Emprestimo> carregarEmprestimos(String arquivo, List<Livro> livros, List<Aluno> alunos) {
        List<Emprestimo> emprestimos = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(arquivo))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(";"); // Separando os campos por ponto e vírgula

                // Certificando-se de que temos todos os dados necessários
                if (partes.length == 3) {
                    int isbn = Integer.parseInt(partes[0]);
                    String nomePessoa = partes[1];
                    String dataEmprestimoStr = partes[2];
                    LocalDate dataEmprestimo = LocalDate.parse(dataEmprestimoStr); // Convertendo string para LocalDate

                    // Procurando o livro pelo ISBN diretamente na lista de livros
                    Livro livro = null;
                    for (Livro l : livros) {
                        if (l.getIsbn() == isbn) {
                            livro = l;
                            break;
                        }
                    }

                    // Procurando a pessoa na lista de pessoas
                    Aluno aluno = null;
                    for (Aluno a : alunos) {
                        if (a.getNome().equals(nomePessoa)) {
                            aluno = a;
                            break;
                        }
                    }

                    // Adicionando o empréstimo à lista, se o livro e a pessoa existirem
                    if (livro != null && aluno != null) {
                        emprestimos.add(new Emprestimo(livro, aluno, dataEmprestimo, null));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar empréstimos: " + e.getMessage());
        }
        return emprestimos;
    }

    // Método para alterar a disponibilidade de um livro no arquivo
    public static void alterarDisponibilidadeLivro(String arquivo, int isbn, boolean novaDisponibilidade) {
        // Carregar todos os livros do arquivo
        List<Livro> livros = carregarLivros(arquivo);

        // Localizar o livro pelo ISBN e alterar a disponibilidade
        for (Livro livro : livros) {
            if (livro.getIsbn() == isbn) {
                livro.setDisponivel(novaDisponibilidade);
                break; // Sai do laço assim que o livro for encontrado
            }
        }

        // Gravar a lista atualizada no arquivo
        gravarLivrosEmArquivo(arquivo, livros);

        System.out.println("Disponibilidade do livro com ISBN " + isbn + " foi alterada para " + novaDisponibilidade);
    }

    // Método para gravar alunos no arquivo
    public static void gravarAlunosEmArquivo(String arquivo, List<Aluno> alunos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Aluno aluno : alunos) {
                // Gravando o aluno no formato: nome;idade;sexo;login;senha;curso;periodo;matricula
                writer.write(aluno.getNome() + ";"
                        + aluno.getIdade() + ";"
                        + aluno.getSexo() + ";"
                        + aluno.getLogin() + ";"
                        + aluno.getSenha() + ";"
                        + aluno.getCurso() + ";"
                        + aluno.getPeriodo() + ";"
                        + aluno.getMatricula());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao gravar alunos no arquivo: " + e.getMessage());
        }
    }

    // Método para carregar os alunos do arquivo
    public static List<Aluno> carregarAlunos(String arquivo) {
        List<Aluno> alunos = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(arquivo))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(";"); // Separando os campos por ponto e vírgula

                // Certificando-se de que temos todos os dados necessários
                if (partes.length == 8) { // 8 campos: nome, idade, sexo, login, senha, curso, periodo, matricula
                    String nome = partes[0];
                    int idade = Integer.parseInt(partes[1]);
                    char sexo = partes[2].charAt(0);
                    String login = partes[3];
                    String senha = partes[4];
                    String curso = partes[5];
                    int periodo = Integer.parseInt(partes[6]);
                    int matricula = Integer.parseInt(partes[7]);

                    // Adicionando o aluno à lista
                    alunos.add(new Aluno(nome, idade, sexo, login, senha, matricula, curso, periodo));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar alunos: " + e.getMessage());
        }
        return alunos;
    }

    // Método para gravar bibliotecários no arquivo
    public static void gravarBibliotecariosEmArquivo(String arquivo, List<Bibliotecario> bibliotecarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Bibliotecario bibliotecario : bibliotecarios) {
                // Gravando o bibliotecário no formato: nome;idade;sexo;login;senha;codigo
                writer.write(bibliotecario.getNome() + ";"
                        + bibliotecario.getIdade() + ";"
                        + bibliotecario.getSexo() + ";"
                        + bibliotecario.getLogin() + ";"
                        + bibliotecario.getSenha() + ";"
                        + bibliotecario.getCodigo());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao gravar bibliotecários no arquivo: " + e.getMessage());
        }
    }

    // Método para carregar bibliotecários do arquivo
    public static List<Bibliotecario> carregarBibliotecarios(String arquivo) {
        List<Bibliotecario> bibliotecarios = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(arquivo))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(";"); // Separando os campos por ponto e vírgula

                // Certificando-se de que temos todos os dados necessários
                if (partes.length == 6) { // 6 campos: nome, idade, sexo, login, senha, código (int)
                    String nome = partes[0];
                    int idade = Integer.parseInt(partes[1]);
                    char sexo = partes[2].charAt(0);  // Considerando que o sexo é uma única letra (M/F)
                    String login = partes[3];
                    String senha = partes[4];
                    int codigo = Integer.parseInt(partes[5]); // Agora tratando o código como int

                    // Adicionando o bibliotecário à lista com o construtor adequado
                    bibliotecarios.add(new Bibliotecario(nome, idade, sexo, login, senha, codigo));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar bibliotecários: " + e.getMessage());
        }
        return bibliotecarios;
    }

}
