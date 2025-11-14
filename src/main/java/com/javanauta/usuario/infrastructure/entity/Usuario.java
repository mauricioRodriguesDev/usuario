package com.javanauta.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "usuario")

public class Usuario implements UserDetails {

/*Codigo para criar um ID unico gerado automaticamente*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100)
    private String nome;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "senha")
    private String senha;

/*Codigos para fazer referencia as tabelas telefone e endereco, dizendo que um Usuario pode ter varios (OneToMany)
 ederecos e telefones cadastrados referenciando seu id unico.
Cascade.Type.ALL para quando apagar um usuario, apagar todos os enderecos e telefones cadastrados em seu id */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private List<Telefone> telefones;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id",referencedColumnName = "id")
    private List<Endereco> enderecos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {

        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    /*Para cada classe entity deve haver uma interface Repository estendendo o JpaRepository ou MongoRepository para fazer
* seus metodos CRUD no banco de dados*/


}
