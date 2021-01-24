package com.pneri.myfinances.model.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pneri.myfinances.model.entity.Usuario;

/*
 * 
 * @SpringBootTest-> Substituido pelo DataJpaTest
 * @ExtendWith(SpringExtension.class)-> usar com JUnit5
 * @AutoConfigureTestDatabase -> Utilizado pois se nao sobrescreve as configuracoes setadas no profiles de test
 * 
 * 
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	TestEntityManager em;

	public static Usuario createUser() {
		return Usuario.builder().nome("user").email("userEmail@email.com").senha("123").build();
	}

	@Test
	public void mustCheckExistsEmail() {

		// Set ou Cenario
		Usuario usuario = createUser();
		em.persist(usuario);
		// Action ou metodo
		Boolean result = usuarioRepository.existsByEmail("userEmail@email.com");
		// Asserts ou Verificacao
		Assertions.assertThat(result).isTrue();

	}

	@Test
	void mustReturnFalseWhenNotExistsEmail() {
		// Set
		// Acao--Meu metodo
		Boolean result = usuarioRepository.existsByEmail("emailDiferente@email.com");
		// Verificacao
		Assertions.assertThat(result).isFalse();

	}

	@Test
	public void mustSaveUser() {

		// set
		Usuario usuario = createUser();

		// action
		Usuario userSaved = usuarioRepository.save(usuario);
		// asserts
		Assertions.assertThat(userSaved.getId()).isNotNull();
	}

	@Test
	public void mustFindUserByEmail() {

		// Set
		Usuario usuario = createUser();
		em.persist(usuario);

		// Action
		Optional<Usuario> userWithEmail = usuarioRepository.findByEmail("userEmail@email.com");

		// Asserts
		Assertions.assertThat(userWithEmail.isPresent()).isTrue();
	}

	@Test
	public void mustReturnEmptyIfNotFoundAEmail() {

		Optional<Usuario> userWithEmail = usuarioRepository.findByEmail("userEmail@email.com");
		Assertions.assertThat(userWithEmail.isPresent()).isFalse();

	}
}
