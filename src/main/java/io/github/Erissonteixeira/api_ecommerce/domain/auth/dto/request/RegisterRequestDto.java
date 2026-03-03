package io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegisterRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Whatsapp é obrigatório")
    @Pattern(
            regexp = "^(\\d{2})\\s(\\d{4,5})-(\\d{4})$",
            message = "Whatsapp inválido (ex.: 99 99999-9999 ou 99 9999-9999)"
    )
    private String whatsapp;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "Senha é obrigatória")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$",
            message = "Senha deve ter no mínimo 8 caracteres, 1 letra maiúscula e 1 caractere especial"
    )
    private String senha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}