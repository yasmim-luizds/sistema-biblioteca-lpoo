package sistema;

import java.util.*; //importa vários util
import java.time.LocalDate; // importa a data local
import usuarios.*; //importa todas as classes do package usuarios

public class SistemaBiblioteca {
	private List<Livro> livros;
	private List<Aluno> alunos;
	private List<Bibliotecario> bibliotecarios;
	private List<Emprestimo> emprestimos;

	private static final String ARQUIVO_LIVROS = "livros.txt";
	private static final String ARQUIVO_ALUNOS = "alunos.txt";
	private static final String ARQUIVO_BIBLIOTECARIOS = "bibliotecarios.txt";
	private static final String ARQUIVO_EMPRESTIMOS = "emprestimos.txt";

	// metodo para retornar uma lista combinada de alunos + bibliotecarios
	private List<Pessoa> getTodasPessoas() {
		List<Pessoa> todasPessoas = new ArrayList<>();
		todasPessoas.addAll(alunos);
		todasPessoas.addAll(bibliotecarios);
		return todasPessoas;
	}

	// construtor
	public SistemaBiblioteca() { // construtor que instancia listas
		this.livros = Arquivo.carregarLivros(ARQUIVO_LIVROS);
		this.alunos = Arquivo.carregarAlunos(ARQUIVO_ALUNOS);
		this.bibliotecarios = Arquivo.carregarBibliotecarios(ARQUIVO_BIBLIOTECARIOS);
		this.emprestimos = Arquivo.carregarEmprestimos(ARQUIVO_EMPRESTIMOS, livros, alunos);
	}

	// método para validar o login
	public Pessoa validarLogin(String login, String senha) {
		for (Pessoa pessoa : getTodasPessoas()) {
			if (pessoa.getLogin().equals(login) && pessoa.getSenha().equals(senha)) {
				return pessoa;
			}
		}
		return null; // não validou
	}

	// Método para adicionar um novo livro
	public void adicionarLivro(Livro livro) {
		livros.add(livro);
		Arquivo.gravarLivrosEmArquivo(ARQUIVO_LIVROS, livros);
	}

	// Método para adicionar novos alunos
	public void adicionarAluno(Aluno aluno) {
		alunos.add(aluno);
		Arquivo.gravarAlunosEmArquivo(ARQUIVO_ALUNOS, alunos);
	}

	// Método para adicionar novos bibliotecarios
	public void adicionarBibliotecario(Bibliotecario bibliotecario) {
		bibliotecarios.add(bibliotecario);
		Arquivo.gravarBibliotecariosEmArquivo(ARQUIVO_BIBLIOTECARIOS, bibliotecarios);
	}

	// Método para registrar um novo empréstimo
	public void registrarEmprestimo(Aluno aluno, Livro livro) {
		// Verificar se o livro está disponível
		if (!livro.isDisponivel()) {
			System.out.println("Erro: O livro já está emprestado.");
			return;
		}

		// Criar um novo empréstimo
		Emprestimo novoEmprestimo = new Emprestimo(livro, aluno, LocalDate.now(), null);

		// Atualizar a disponibilidade do livro para false
		livro.setDisponivel(false);

		// Adicionar o empréstimo à lista de empréstimos
		emprestimos.add(novoEmprestimo);

		// Gravar as alterações no arquivo de empréstimos
		Arquivo.gravarEmprestimosEmArquivo(ARQUIVO_EMPRESTIMOS, emprestimos);

		// Atualizar a disponibilidade do livro no arquivo
		Arquivo.alterarDisponibilidadeLivro(ARQUIVO_LIVROS, livro.getIsbn(), false);

		System.out.println("Empréstimo registrado com sucesso!");
	}


	public Emprestimo buscarEmprestimo(Aluno aluno, Livro livro) {
	    for (Emprestimo emprestimo : emprestimos) {
	        // Comparar diretamente os atributos chave
	        if (emprestimo.getAluno().getMatricula() == aluno.getMatricula() && 
	            emprestimo.getLivro().getIsbn() == livro.getIsbn() &&
	            emprestimo.getDataDevolucao() == null) {
	            return emprestimo;
	        }
	    }
	    return null; // não encontrado
	}



	// Verifica a disponibilidade de um livro pelo ISBN
	public void verificarDisponibilidade(int isbnConsulta) {
		Livro livro = buscarLivroPorIsbn(isbnConsulta); 

		if (livro == null) {
			System.out.println("Livro com ISBN " + isbnConsulta + " não encontrado.");
			return;
		}

		if (livro.isDisponivel()) {
			System.out.println("O livro '" + livro.getTitulo() + "' está disponível.");
		} else {
			System.out.println("O livro '" + livro.getTitulo() + "' não está disponível.");
		}
	}

	// faz a busca de um livro para realizar emprestimo
	public Livro buscarLivroPorIsbn(int isbnEmprestimoOuReserva) {
		for (Livro livro : livros) {
			if (livro.getIsbn() == isbnEmprestimoOuReserva) {
				return livro;
			}
		}
		return null;
	}

	// Método para registrar a devolução de algum livro
	public void registrarDevolucao(Aluno aluno, Livro livro) {
		Emprestimo emprestimoEncontrado = buscarEmprestimo(aluno, livro);
		if (emprestimoEncontrado != null) {
			if (emprestimoEncontrado.getDataDevolucao() == null) {
				emprestimoEncontrado.registrarDevolucao(LocalDate.now());
				livro.setDisponivel(true);
				Arquivo.gravarEmprestimosEmArquivo(ARQUIVO_EMPRESTIMOS, emprestimos);
				Arquivo.gravarLivrosEmArquivo(ARQUIVO_LIVROS, livros); // Atualizar o estado dos livros
				System.out.println("Devolução registrada com sucesso!");
			} else {
				System.out.println("Erro: Este empréstimo já foi devolvido.");
			}
		} else {
			System.out.println("Erro: Empréstimo não encontrado ou já devolvido.");
		}
	}

	// Método para listar todos os livros
	public List<Livro> listarLivros() {
		livros = Arquivo.carregarLivros("livros.txt"); // Recarrega os livros do arquivo
		return livros;
	}

	// Método para listar empréstimos
	public List<Emprestimo> listarEmprestimos() {
		return emprestimos;
	}

	// Método para listar alunos cadastrados
	public List<Aluno> listarAlunos() {
		return alunos;
	}

	// Método para listar bibliotecários cadastrados
	public List<Bibliotecario> listarBibliotecarios() {
		return bibliotecarios;
	}

	// método para excluir um livro pelo isbn
	public void excluirLivro(int isbn) {
		Livro livroParaRemover = null;
		for (Livro livro : livros) {
			if (livro.getIsbn() == isbn) {
				livroParaRemover = livro;
				break;
			}

		}
		if (livroParaRemover != null) {
			livros.remove(livroParaRemover);
			Arquivo.gravarLivrosEmArquivo(ARQUIVO_LIVROS, livros);
			System.out.println("Livro removido com sucesso;");

		} else {
			System.err.println("Livro não encontrado.");
		}
	}

}