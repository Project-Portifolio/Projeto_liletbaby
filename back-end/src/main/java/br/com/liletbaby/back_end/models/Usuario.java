package br.com.liletbaby.back_end.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Usuario class.
 *
 * @author Wender Couto
 * @author João Abreu
 * @since 0.0.1-SNAPSHOT
 */

@Data
@Entity
@Table(name = "Usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String role;

    @Column(nullable = true)
    private String nameCompleto;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String cpf_cnpj;

    @OneToOne
    private Carrinho cartId;

    public Usuario (){
    }

    public Usuario(int i) {
    }

    public Usuario (String username, String password){
        this.name = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNameCompleto() {
        return nameCompleto;
    }

    public void setNameCompleto(String nameCompleto) {
        this.nameCompleto = nameCompleto;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Carrinho getCartId() {
        return cartId;
    }

    public void setCartId(Carrinho cartId) {
        this.cartId = cartId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}