package com.pneri.myfinances.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.pneri.myfinances.model.entity.Lancamento;
import com.pneri.myfinances.model.entity.Usuario;
import com.pneri.myfinances.model.enums.StatusLancamento;
import com.pneri.myfinances.model.enums.TipoLancamento;
import com.pneri.myfinances.model.repositories.LancamentoRepository;
import com.pneri.myfinances.model.repositories.UsuarioRepository;

import lombok.Builder;

/**
 * 
 * Classe para população inicial do banco de dados para teste
 * 
 * @author pedro.neri
 *
 */
@Configuration
@Profile("dev")
@Builder
public class LoadDataBaseTest implements CommandLineRunner {

	@Autowired
	private UsuarioRepository userRepósitory;
	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Override
	public void run(String... args) throws Exception {

		Usuario u1 = new Usuario(null, "Peter Pan", "peterpan@email.com", "admin");
		Usuario u2 = new Usuario(null, "Roboin Wood", "robin.wood@email.com", "admin");
		Usuario u3 = new Usuario(null, "Batman", "bruce.wayne@email.com", "admin");
		Usuario u4 = new Usuario(null, "Han Solo", "han.solo@email.com", "admin");
		Usuario u5 = new Usuario(null, "SkyWalwaker", "SkyWalwakern@email.com", "admin");
		userRepósitory.saveAll(Arrays.asList(u1, u2, u3, u4, u5));

		Lancamento l1 = new Lancamento(null, "Pagamento da Fatura do Cartão", 2021, 3, BigDecimal.valueOf(230.90),
				LocalDate.now(), TipoLancamento.DESPESA, StatusLancamento.EFETIVADO, u1);
		Lancamento l2 = new Lancamento(null, "Pagamento da Fatura do Cartão", 2021, 3, BigDecimal.valueOf(230.90),
				LocalDate.now(), TipoLancamento.DESPESA, StatusLancamento.EFETIVADO, u2);
		Lancamento l3 = new Lancamento(null, "Salário", 2021, 3, BigDecimal.valueOf(10000.0), LocalDate.now(),
				TipoLancamento.RECEITA, StatusLancamento.EFETIVADO, u2);
		Lancamento l4 = new Lancamento(null, "Salário", 2021, 3, BigDecimal.valueOf(10000.0), LocalDate.now(),
				TipoLancamento.RECEITA, StatusLancamento.EFETIVADO, u1);
		Lancamento l5 = new Lancamento(null, "Escola", 2021, 3, BigDecimal.valueOf(2330.90), LocalDate.now(),
				TipoLancamento.DESPESA, StatusLancamento.EFETIVADO, u3);
		Lancamento l6 = new Lancamento(null, "Escola", 2021, 3, BigDecimal.valueOf(2330.90), LocalDate.now(),
				TipoLancamento.DESPESA, StatusLancamento.EFETIVADO, u4);
		Lancamento l7 = new Lancamento(null, "Aluguel", 2021, 3, BigDecimal.valueOf(4390.90), LocalDate.now(),
				TipoLancamento.DESPESA, StatusLancamento.EFETIVADO, u5);

		lancamentoRepository.saveAll(Arrays.asList(l1, l2, l3, l4, l5, l6, l7));

	}

}
