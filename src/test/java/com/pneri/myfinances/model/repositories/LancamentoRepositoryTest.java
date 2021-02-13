package com.pneri.myfinances.model.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pneri.myfinances.model.entity.Lancamento;
import com.pneri.myfinances.model.enums.StatusLancamento;
import com.pneri.myfinances.model.enums.TipoLancamento;
import com.pneri.myfinances.services.LancamentoService;

import lombok.Builder;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;

	@Autowired
	TestEntityManager em;

	public static Lancamento lancamentoBuilder() {

		return Lancamento.builder().ano(2019).mes(1).descricao("lancamento qualquer").dataCadastro(LocalDate.now())
				.status(StatusLancamento.PENDENTE).tipo(TipoLancamento.RECEITA).valor(BigDecimal.valueOf(10)).build();
	}

	@Test
	public void deveSalvarUmLancamento() {

		// Set
		Lancamento lancamento = lancamentoBuilder();
		// Action
		repository.save(lancamento);
		// Asserts
		Assertions.assertThat(lancamento.getId()).isNotNull();

	}

	@Test
	public void deveDeletarUmLancamento() {
		// Set
		Lancamento lancamento = lancamentoBuilder();
		em.persist(lancamento);
		lancamento = em.find(Lancamento.class, lancamento.getId());

		// Action
		repository.delete(lancamento);

		// Asserts
		Lancamento lancamentoInexistente = em.find(Lancamento.class, lancamento.getId());
		Assertions.assertThat(lancamentoInexistente).isNull();

	}

	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criaEPersisteLancamento();

		lancamento.setAno(1900);
		lancamento.setMes(12);
		lancamento.setDescricao("Descricao Alterada");
		lancamento.setTipo(TipoLancamento.DESPESA);
		lancamento.setStatus(StatusLancamento.CANCELADO);

		repository.save(lancamento);
		Lancamento lancamentoAtualizado = em.find(Lancamento.class, lancamento.getId());

		Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(1900);
		Assertions.assertThat(lancamentoAtualizado.getMes()).isEqualTo(12);
		Assertions.assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Descricao Alterada");
		Assertions.assertThat(lancamentoAtualizado.getTipo()).isEqualTo(TipoLancamento.DESPESA);
		Assertions.assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);

	}

	private Lancamento criaEPersisteLancamento() {
		Lancamento lancamento = lancamentoBuilder();
		return em.persist(lancamento);
	}

	public void deveBuscarUmLancamentoPorId() {

		Lancamento lancamento = criaEPersisteLancamento();

		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());

		Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();

	}
}
