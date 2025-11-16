package sistema;

public class Livro {
	private String titulo;
	private String autor;
	private int isbn; //codigo de barra do livro
	private boolean disponivel; // para controlar a disponibilidade do livro

	public Livro(String titulo, String autor, int isbn, boolean disponivel) {
		this.titulo = titulo;
		this.autor = autor;
		this.isbn = isbn;
		this.disponivel = disponivel;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

	public boolean isDisponivel() {
		return disponivel;
	}

	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}

	@Override
	public String toString() {
		return "Título: "+titulo + "; Autor: " + autor + "; ISBN: "+ isbn;
	}

}