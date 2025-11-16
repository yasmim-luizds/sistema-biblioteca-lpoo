package sistema;
import java.time.LocalDate;
import Pessoas.Usuario;
import java.time.temporal.ChronoUnit;


public class Emprestimo { 
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao; // pode ser null
    private String status; // PENDENTE, APROVADO, RECUSADO, DEVOLVIDO

    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = null;
        this.status = "PENDENTE";
    }

    public Livro getLivro() {
	return livro;
    }
    public void setLivro(Livro livro) {
    	this.livro = livro;
    }

    public Usuario getUsuario() {
        return usuario;
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

    public String getStatus() {
        return status;
    }

    public void setStatusAprovado(String status) {
        this.status = "Aprovado";
    }
        
    public void setStatusRecusado(String status) {
        this.status = "Recusado";
    }
    
    public void aprovar() {
        this.status = "APROVADO";
        this.livro.setDisponivel(false);
    }

    public void recusar() {
        this.status = "RECUSADO";
    }

    public void registrarDevolucao(LocalDate data) {
        this.dataDevolucao = data;
        this.status = "DEVOLVIDO";
        this.livro.setDisponivel(true);
    }

    public LocalDate getDataVencimento() {
        // usa polimorfismo: pega o prazo do usuário
        int prazo = usuario.getPrazoEmprestimoDias();
        return dataEmprestimo.plusDays(prazo);
    }

    public boolean emAtraso() {
        if (status.equals("APROVADO") && dataDevolucao == null) {
            return LocalDate.now().isAfter(getDataVencimento());
        }
        return false;
    }

    public double calcularMulta() {
        if (!emAtraso()) {
            return 0.0;
        }
        long diasAtraso = ChronoUnit.DAYS.between(getDataVencimento(), LocalDate.now());
        return diasAtraso * usuario.getMultaPorDia();
    }
       
    @Override
    public String toString() {
        return livro.toString() + " | Usuario: " + usuario.getNome()
                + " | Emprestimo: " + dataEmprestimo
                + " | Vencimento: " + getDataVencimento()
                + " | Status: " + status
                + (dataDevolucao != null ? " | Devolução: " + dataDevolucao : "");
    }
    
} 