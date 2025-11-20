package sistema;

import java.time.LocalDate;
import usuarios.Usuario;

public class Emprestimo { 
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao; 
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
        this.status = "APROVADO";
    }

    public void setStatusRecusado(String status) {
        this.status = "RECUSADO";
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

    // Data de vencimento = data do empréstimo + prazo do usuário 
    public LocalDate getDataVencimento() {
        int prazo = usuario.getPrazoEmprestimoDias();
        return dataEmprestimo.plusDays(prazo);
    }

    // Verifica se está em atraso: aprovado, não devolvido e hoje > vencimento
    public boolean emAtraso() {
        if ("APROVADO".equals(status) && dataDevolucao == null) {
            return LocalDate.now().isAfter(getDataVencimento());
        }
        return false;
    }

 
    public double calcularMulta() {
        if (!emAtraso()) {
            return 0.0;
        }

        LocalDate venc = getDataVencimento();
        LocalDate hoje = LocalDate.now();

        long diasAtraso = hoje.toEpochDay() - venc.toEpochDay();
        if (diasAtraso < 0) {
            diasAtraso = 0; 
        }

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
