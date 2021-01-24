package com.pneri.myfinances.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pneri.myfinances.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
