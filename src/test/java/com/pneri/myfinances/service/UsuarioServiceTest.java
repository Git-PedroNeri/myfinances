package com.pneri.myfinances.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pneri.myfinances.exceptions.UsuarioBussinessException;
import com.pneri.myfinances.exceptions.UsuarioErrorAuthentication;
import com.pneri.myfinances.model.entity.Usuario;
import com.pneri.myfinances.model.repositories.UsuarioRepository;
import com.pneri.myfinances.services.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	@SpyBean
	UsuarioServiceImpl serviceSpy;
	@MockBean
	UsuarioRepository usuarioRepositoryMock;

	@Test(expected = Test.None.class) // Espera que não retorne nenhuma excpetion
	public void mustSaveAUser() {
		// Set
		Mockito.doNothing().when(serviceSpy).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(10L).nome("usuario").email("email@email.com").senha("senha").build();
		Mockito.when(usuarioRepositoryMock.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		// Action
		Usuario usuarioSalvo = serviceSpy.cadastrar(new Usuario());

		// Asserts

		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("usuario");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}

	@Test(expected = UsuarioBussinessException.class)
	public void naoDeveSalvarOUsuarioComEmailJaCadastrado() {
		// Set
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(UsuarioBussinessException.class).when(serviceSpy).validarEmail(email);
		// Action
		serviceSpy.cadastrar(usuario);
		Mockito.verify(usuarioRepositoryMock, Mockito.never()).save(usuario);
	}

	/**
	 * Test.None espera que nao retorne nenhuma exception
	 */
	@Test(expected = Test.None.class)
	public void mustValidateEmail() {
		// Set
		Mockito.when(usuarioRepositoryMock.existsByEmail(Mockito.anyString())).thenReturn(false);
		// Action
		serviceSpy.validarEmail("email@email.com.br");
		// Asserts
	}

	@Test(expected = UsuarioBussinessException.class)
	public void mustReturnExceptioWhenFindEmailSaved() {
		// Set
		Mockito.when(usuarioRepositoryMock.existsByEmail(Mockito.anyString())).thenReturn(true);
		// Action
		serviceSpy.validarEmail("email@gmail.com");

	}

	@Test(expected = Test.None.class)
	public void mustAuthenticateAUserWithSuccess() {

		// cenário ou Set
		String email = "userEmail@email.com";
		String senha = "senha";

		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		Mockito.when(usuarioRepositoryMock.findByEmail(email)).thenReturn(Optional.of(usuario));

		Usuario result = serviceSpy.autenticar(email, senha);

		Assertions.assertThat(result).isNotNull();

	}

	@Test
	public void mustThrowsExceptionWhenNotFoundAUser() {
		// Set
		Mockito.when(usuarioRepositoryMock.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		// Action
		Throwable exception = Assertions.catchThrowable(() -> serviceSpy.autenticar("email", "senha"));
		// Asserts
		Assertions.assertThat(exception).isInstanceOf(UsuarioErrorAuthentication.class)
				.hasMessage("Usuario não encontrado :(");

	}

	@Test
	public void mustThrowExceptionWhenPasswordIncorrect() {
		// Set
		String senha = "correctPassword";

		Usuario usuario = Usuario.builder().email("email@gmail.com").nome("Pedro").senha(senha).build();
		OngoingStubbing<Optional<Usuario>> stubbing = Mockito
				.when(usuarioRepositoryMock.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

		// Action
		Throwable exception = Assertions
				.catchThrowable(() -> serviceSpy.autenticar("email@gmail.com", "incorrectPassword"));

		Assertions.assertThat(exception).isInstanceOf(UsuarioErrorAuthentication.class).hasMessage("Senha inválida :(");

	}

}
