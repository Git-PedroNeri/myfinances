package com.pneri.myfinances.api.dto;

import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

	private String nome;
	@Email(message = "Email should be valid")
	private String email;
	private String senha;

}
