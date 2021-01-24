package com.pneri.myfinances.services;

import java.util.List;

import com.pneri.myfinances.model.entity.Lancamento;
import com.pneri.myfinances.model.enums.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento);

	Lancamento atualizar(Lancamento lanacamento);

	void deletar(Lancamento lancamento);

	List<Lancamento> buscar(Lancamento lancamentoFiltro);

	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validarLancamento(Lancamento lancamento);

}
