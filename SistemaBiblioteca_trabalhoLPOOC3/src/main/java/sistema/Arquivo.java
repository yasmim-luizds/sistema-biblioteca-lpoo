package sistema;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import Pessoas.Usuario;
import Pessoas.Aluno;
import Pessoas.Professor;

public class Arquivo {

    // grava livros no arquivo: isbn;titulo;autor;disponivel
    public static void gravarLivros(String arquivo, List<Livro> livros) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            for (Livro l : livros) {
                bw.write(l.getIsbn() + ";" + l.getTitulo() + ";" + l.getAutor() + ";" + l.isDisponivel());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro gravarLivros: " + e.getMessage());
        }
    }

    public static List<Livro> carregarLivros(String arquivo) {
        List<Livro> livros = new ArrayList<>();
        File f = new File(arquivo);
        if (!f.exists()) {
            return livros;
        }
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine().trim();
                if (linha.isEmpty()) {
                    continue;
                }
                String[] p = linha.split(";");
                if (p.length >= 4) {
                    int isbn = Integer.parseInt(p[0]);
                    String titulo = p[1];
                    String autor = p[2];
                    boolean disponivel = Boolean.parseBoolean(p[3]);
                    livros.add(new Livro(titulo, autor, isbn, disponivel));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro carregarLivros: " + e.getMessage());
        }
        return livros;
    }

    // grava usuarios (ALUNO/PROFESSOR) — formato livre, mantenha coerente com seu carregador
    public static void gravarUsuarios(String arquivo, List<Usuario> usuarios) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            for (Usuario u : usuarios) {
                if (u instanceof Aluno) {
                    Aluno a = (Aluno) u;
                    // formato: ALUNO;login;senha;nome;email;matricula;curso;periodo
                    bw.write("ALUNO;" + a.getLogin() + ";" + a.getSenha() + ";" + a.getNome() + ";" + a.getEmail()
                            + ";" + a.getMatricula() + ";" + a.getCurso() + ";" + a.getPeriodo());
                } else if (u instanceof Professor) {
                    Professor p = (Professor) u;
                    bw.write("PROFESSOR;" + p.getLogin() + ";" + p.getSenha() + ";" + p.getNome() + ";" + p.getEmail()
                            + ";" + p.getMatricula());
                } else {
                    // genérico (não esperado)
                    bw.write("USER;" + u.getLogin() + ";" + u.getSenha() + ";" + u.getNome() + ";" + u.getEmail());
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro gravarUsuarios: " + e.getMessage());
        }
    }

    public static List<Usuario> carregarUsuarios(String arquivo) {
        List<Usuario> usuarios = new ArrayList<>();
        File f = new File(arquivo);
        if (!f.exists()) {
            return usuarios;
        }
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine().trim();
                if (linha.isEmpty()) {
                    continue;
                }
                String[] p = linha.split(";");
                String tipo = p[0];
                if ("ALUNO".equalsIgnoreCase(tipo) && p.length >= 8) {
                    String login = p[1], senha = p[2], nome = p[3], email = p[4];
                    int matricula = Integer.parseInt(p[5]);
                    String curso = p[6];
                    int periodo = Integer.parseInt(p[7]);
                    usuarios.add(new Aluno(nome, Integer.MIN_VALUE, 'U', login, senha, matricula, curso, periodo));
                    // note: idade/sexo preenchidos com placeholder se seu construtor exigir — ajuste conforme seu construtor real
                } else if ("PROFESSOR".equalsIgnoreCase(tipo) && p.length >= 6) {
                    String login = p[1], senha = p[2], nome = p[3], email = p[4];
                    int matricula = Integer.parseInt(p[5]);
                    usuarios.add(new Professor(nome, email, login, senha, matricula));
                } else if ("BIBLIOTECARIO".equalsIgnoreCase(tipo) && p.length >= 6) {
                    String login = p[1], senha = p[2], nome = p[3], email = p[4];
                    int codigo = Integer.parseInt(p[5]);
                    usuarios.add(new Admin(nome, email, login, senha)); // Admin usado como "bibliotecario"
                }
            }
        } catch (IOException e) {
            System.err.println("Erro carregarUsuarios: " + e.getMessage());
        }
        return usuarios;
    }

    // emprestimos: isbn;loginUsuario;dataEmprestimo;dataDevolucao;status
    public static void gravarEmprestimos(String arquivo, List<Emprestimo> emprestimos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            for (Emprestimo e : emprestimos) {
                String dataDev = e.getDataDevolucao() != null ? e.getDataDevolucao().toString() : "";
                String login = e.getUsuario() != null ? e.getUsuario().getLogin() : "";
                bw.write(e.getLivro().getIsbn() + ";" + login + ";" + e.getDataEmprestimo() + ";" + dataDev + ";" + e.getStatus());
                bw.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Erro gravarEmprestimos: " + ex.getMessage());
        }
    }

    public static List<Emprestimo> carregarEmprestimos(String arquivo, List<Livro> livros, List<Usuario> usuarios) {
        List<Emprestimo> emprestimos = new ArrayList<>();
        File f = new File(arquivo);
        if (!f.exists()) {
            return emprestimos;
        }
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine().trim();
                if (linha.isEmpty()) {
                    continue;
                }
                String[] p = linha.split(";");
                if (p.length >= 4) {
                    int isbn = Integer.parseInt(p[0]);
                    String login = p[1];
                    LocalDate dataEmp = LocalDate.parse(p[2]);
                    String dataDevStr = p[3];
                    String status = p.length > 4 ? p[4] : "PENDENTE";

                    Livro livro = null;
                    for (Livro l : livros) {
                        if (l.getIsbn() == isbn) {
                            livro = l;
                            break;
                        }
                    }

                    Usuario usuario = null;
                    for (Usuario u : usuarios) {
                        if (u.getLogin().equals(login)) {
                            usuario = u;
                            break;
                        }
                    }

                    if (livro != null && usuario != null) {
                        Emprestimo e = new Emprestimo(livro, usuario, dataEmp);
                        if (dataDevStr != null && !dataDevStr.isEmpty()) {
                            e.registrarDevolucao(LocalDate.parse(dataDevStr));
                        }
                        if ("APROVADO".equalsIgnoreCase(status)) {
                            e.aprovar();
                        }
                        if ("RECUSADO".equalsIgnoreCase(status)) {
                            e.recusar();
                        }
                        emprestimos.add(e);
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Erro carregarEmprestimos: " + ex.getMessage());
        }
        return emprestimos;
    }

    // altera disponibilidade no arquivo de livros
    public static void alterarDisponibilidadeLivro(String arquivo, int isbn, boolean novaDisponibilidade) {
        List<Livro> livros = carregarLivros(arquivo);
        for (Livro livro : livros) {
            if (livro.getIsbn() == isbn) {
                livro.setDisponivel(novaDisponibilidade);
                break;
            }
        }
        gravarLivros(arquivo, livros);
    }

}
