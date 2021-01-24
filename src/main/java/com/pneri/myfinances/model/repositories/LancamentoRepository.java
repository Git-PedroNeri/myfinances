package com.pneri.myfinances.model.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pneri.myfinances.model.entity.Lancamento;
import com.pneri.myfinances.model.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	String SELECT_SALDO_BY_USER = "select sum(l.valor) from Lancamento l join l.usuario u "
			+ "where u.id=:idUsuario and l.tipo=:tipo group by u";

	@Query(value = SELECT_SALDO_BY_USER)
	BigDecimal obterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario") Long idUsuario,
			@Param("tipo") TipoLancamento tipo);
}
