package sistema;
import java.time.LocalDate;

import usuarios.Aluno;


public class Emprestimo { 
	private Livro livro; 
	private Aluno aluno; 
	private LocalDate dataEmprestimo; 
	private LocalDate dataDevolucao; 

	public Emprestimo(Livro livro, Aluno aluno, LocalDate dataEmprestimo, LocalDate dataDevolucao) { 
		this.livro = livro; 
		this.aluno = aluno; 
		this.dataEmprestimo = dataEmprestimo; 
		this.dataDevolucao = null;  // Inicialmente, não há devolução 
	} 
	public void registrarDevolucao(LocalDate dataDevolucao) { 
		this.dataDevolucao = dataDevolucao; 
		livro.setDisponivel(true); // O livro volta a estar disponível 
	} 

	public boolean EmAtraso() { 
		if (dataDevolucao == null) { 
			return LocalDate.now().isAfter(dataEmprestimo.plusDays(7));//Prazo de devolução é 7 dias após a data de empréstimo (Se passar o prazo considera em atraso) 
		} 
		return false; 
	}


	public Livro getLivro() {
		return livro;
	}
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	public Aluno getAluno() {
		return aluno;
	}
	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}
	public void setDataEmprestimo(LocalDate dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}
	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}
	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	@Override 
	public String toString() { 
		return livro.toString() + " | Emprestado para: " + aluno.getNome() + " | Data Empréstimo: " + dataEmprestimo + " | Data Devolução: "+ dataEmprestimo.plusDays(7); 
	} 
} 