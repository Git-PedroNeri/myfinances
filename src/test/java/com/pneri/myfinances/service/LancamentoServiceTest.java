package com.pneri.myfinances.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pneri.myfinances.exceptions.LancamentoBussinessException;
import com.pneri.myfinances.model.entity.Lancamento;
import com.pneri.myfinances.model.entity.Usuario;
import com.pneri.myfinances.model.enums.StatusLancamento;
import com.pneri.myfinances.model.repositories.LancamentoRepository;
import com.pneri.myfinances.model.repositories.LancamentoRepositoryTest;
import com.pneri.myfinances.services.impl.LancamentoServicesImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServicesImpl service;
	@MockBean
	LancamentoRepository repository;

	@Test
	public void deveSalvarUmLancamento() {

		Lancamento lancamentoASalvar = LancamentoRepositoryTest.lancamentoBuilder();
//		Nao lanca erro caso a classe lance um erro
		Mockito.doNothing().when(service).validarLancamento(lancamentoASalvar);

		Lancamento lancamentoSalvo = LancamentoRepositoryTest.lancamentoBuilder();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

		Lancamento lancamento = service.salvar(lancamentoASalvar);

		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);

	}

	@Test
	public void naoDeveLancarUmLancamentoQuandoHouverErroDeValidacao() {
		// Set
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.lancamentoBuilder();
		Mockito.doThrow(LancamentoBussinessException.class).when(service).validarLancamento(lancamentoASalvar);

		// Action && Asserts
		Assertions.catchThrowableOfType(() -> service.salvar(lancamentoASalvar), LancamentoBussinessException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}

	@Test
	public void deveAtualizarUmLancamento() {

		Lancamento lancamentoSalvo = LancamentoRepositoryTest.lancamentoBuilder();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

		Mockito.doNothing().when(service).validarLancamento(lancamentoSalvo);
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
//Actions
		service.atualizar(lancamentoSalvo);

//Asserts

		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);

	}

	@Test
	public void deveLancarErrorAoTentarLancarLancamentoQueAindaNaoFoiSalvo() {
		// Set
		Lancamento lancamento = LancamentoRepositoryTest.lancamentoBuilder();

		// Action && Asserts
		Assertions.catchThrowableOfType(() -> service.atualizar(lancamento), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamento);
	}

	@Test
	public void deveDeletarUmLancamento() {
		// Set
		Lancamento lancamento = LancamentoRepositoryTest.lancamentoBuilder();
		lancamento.setId(1L);

		// Action
		service.deletar(lancamento);

		// Asserts
		Mockito.verify(repository).delete(lancamento);

	}

	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {

		// Set
		Lancamento lancamento = LancamentoRepositoryTest.lancamentoBuilder();

		// Action
		Assertions.catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);

		// Asserts
		Mockito.verify(repository, Mockito.never()).delete(lancamento);

	}

	@Test
	public void deveFiltrarLancamentos() {
		Lancamento lancamento = LancamentoRepositoryTest.lancamentoBuilder();
		lancamento.setId(1L);

		List<Lancamento> lista = Arrays.asList(lancamento);

		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

		// Action
		List<Lancamento> resultado = service.buscar(lancamento);
//      Asserts
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);

	}

	@Test
	public void deveAtualizarStatusDoLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.lancamentoBuilder();
		lancamento.setId(1L);
		lancamento.setStatus(StatusLancamento.PENDENTE);

		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

//		Action

		service.atualizarStatus(lancamento, novoStatus);

//		Asserts
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);

	}

	@Test
	public void deveObterUmLancamentoPorId() {
		Long id = 1L;
		Lancamento lancamento = LancamentoRepositoryTest.lancamentoBuilder();
		lancamento.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));

		// Action
		Optional<Lancamento> resultado = service.findLancamentoById(id);

		// Asserts
		Assertions.assertThat(resultado.isPresent()).isTrue();

	}

	@Test
	public void deveRetornarVazioQuandoLancamentoNaoExiste() {

		// Set
		Long id = 1L;
		Lancamento lancamento = LancamentoRepositoryTest.lancamentoBuilder();
		lancamento.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

		// Action
		Optional<Lancamento> resultado = service.findLancamentoById(id);

		// Asserts
		Assertions.assertThat(resultado.isPresent()).isFalse();

	}

	@Test
	public void deveLancarErrosAoValidarUmLancamento() {

		Lancamento lancamento = new Lancamento();

		Throwable erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));

		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
				.hasMessage("Informe uma Descricao Válida");

		lancamento.setDescricao("");

		erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));

		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
				.hasMessage("Informe uma Descricao Válida");

		lancamento.setDescricao("Salario");

		erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));
		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
				.hasMessage("Informe um Mês Válido");

		lancamento.setAno(0);

		erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));
		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
				.hasMessage("Informe um Mês Válido");

		lancamento.setAno(13);

		erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));
		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
				.hasMessage("Informe um Mês Válido");

		lancamento.setMes(1);

		erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));
		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
				.hasMessage("Informe um Ano Válido");

		lancamento.setAno(202);

		erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));
		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
				.hasMessage("Informe um Ano Válido");

		lancamento.setAno(2020);

		erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));
//		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
//				.hasMessage("Informe um Usuario Existente");
//		
//		lancamento.setUsuario(new Usuario());
		
//		erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));
//		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
//				.hasMessage("Informe um Usuario Existente");
//
//		lancamento.getUsuario().setId(1L);

//		erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));
//		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
//				.hasMessage("Informe um Valor Válido");
//
//		lancamento.setValor(BigDecimal.ZERO);
//
//		erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));
//		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
//				.hasMessage("Informe um Valor Válido");
//
//		lancamento.setValor(BigDecimal.valueOf(1));
//
//		erro = Assertions.catchThrowable(() -> service.validarLancamento(lancamento));
//		Assertions.assertThat(erro).isInstanceOf(LancamentoBussinessException.class)
//				.hasMessage("Informe um Tipo de lancamento Válido");

	}

}
