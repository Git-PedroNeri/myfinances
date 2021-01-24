package com.pneri.myfinances.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.pneri.myfinances.model.entity.Lancamento;
import com.pneri.myfinances.model.enums.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento);

	Lancamento atualizar(Lancamento lanacamento);

	void deletar(Lancamento lancamento);

	List<Lancamento> buscar(Lancamento lancamentoFiltro);

	void atualizarStatus(Lancamento lancamento, StatusLancamento status);

	void validarLancamento(Lancamento lancamento);

	Optional<Lancamento> findLancamentoById(Long id);

	BigDecimal obterSaldoByUsuario(Long id);

}
