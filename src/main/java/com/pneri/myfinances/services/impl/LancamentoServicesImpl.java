package com.pneri.myfinances.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pneri.myfinances.exceptions.LancamentoBussinessException;
import com.pneri.myfinances.exceptions.UsuarioBussinessException;
import com.pneri.myfinances.model.entity.Lancamento;
import com.pneri.myfinances.model.enums.StatusLancamento;
import com.pneri.myfinances.model.enums.TipoLancamento;
import com.pneri.myfinances.model.repositories.LancamentoRepository;
import com.pneri.myfinances.services.LancamentoService;

@Service
public class LancamentoServicesImpl implements LancamentoService {

	LancamentoRepository repository;

	public LancamentoServicesImpl(LancamentoRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validarLancamento(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validarLancamento(lancamento);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {

		ExampleMatcher matcherContains = ExampleMatcher.matching().withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING);

		Example<Lancamento> lancamentoFiltrado = Example.of(lancamentoFiltro, matcherContains);

		return repository.findAll(lancamentoFiltrado);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);

	}

	@Override
	public void validarLancamento(Lancamento lancamento) {

		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new LancamentoBussinessException("Informe uma Descricao Válida");
		}

		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new LancamentoBussinessException("Informe um Mês Válido");
		}

		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new LancamentoBussinessException("Informe um Ano Válido");
		}

		if (lancamento.getUsuario().getId() == null) {
			throw new UsuarioBussinessException("Informe um Usuario Existente");

		}

		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {

			throw new LancamentoBussinessException("Informe um Valor Válido");
		}

		if (lancamento.getTipo() == null) {
			throw new LancamentoBussinessException("Informe um Tipo de lancamento Válido");

		}

	}

	@Override
	public Optional<Lancamento> findLancamentoById(Long id) {
		return repository.findById(id);
	}

	public BigDecimal validarValor(BigDecimal val) {
		if (val == null) {
			return BigDecimal.ZERO;
		} else {
			return val;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoByUsuario(Long id) {
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
		receitas = validarValor(receitas);
		despesas = validarValor(despesas);
		return receitas.subtract(despesas);
	}

}
