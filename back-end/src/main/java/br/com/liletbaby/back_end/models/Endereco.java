package br.com.liletbaby.back_end.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Endereco class.
 *
 * @author Wender Couto
 * @since 0.0.1-SNAPSHOT
 */

@Data
@Entity
@Table(name = "Endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codEndereco;

    @ManyToOne
    private Usuario usuario;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private int numeroCasa;

    @Column(nullable = true)
    private String Complemento;

    @Column(nullable = false)
    private String Bairro;

    @Column(nullable = false)
    private int cep;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = true)
    private String pais_nacionalidade;

    @Column(nullable = true)
    private String phoneNumber;

    public Endereco() {

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(int numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getComplemento() {
        return Complemento;
    }

    public void setComplemento(String complemento) {
        Complemento = complemento;
    }

    public String getBairro() {
        return Bairro;
    }

    public void setBairro(String bairro) {
        Bairro = bairro;
    }

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais_nacionalidade() {
        return pais_nacionalidade;
    }

    public void setPais_nacionalidade(String pais_nacionalidade) {
        this.pais_nacionalidade = pais_nacionalidade;
    }
}