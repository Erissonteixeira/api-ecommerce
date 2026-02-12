package io.github.Erissonteixeira.api_ecommerce.domain.usuario.dto;

import java.time.LocalDateTime;

public class UsuarioResponseDto {

    private Long id;
    private String nome;
    private String email;
    private String whatsapp;
    private String cpf;
    private LocalDateTime criadoEm;

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getWhatsapp() { return whatsapp; }
    public String getCpf() { return cpf; }
    public LocalDateTime getCriadoEm() { return criadoEm; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
