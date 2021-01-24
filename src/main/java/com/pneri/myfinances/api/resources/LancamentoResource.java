package com.pneri.myfinances.api.resources;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pneri.myfinances.api.dto.LancamentoDTO;
import com.pneri.myfinances.exceptions.LancamentoBussinessException;
import com.pneri.myfinances.model.entity.Lancamento;
import com.pneri.myfinances.model.entity.Usuario;
import com.pneri.myfinances.model.enums.StatusLancamento;
import com.pneri.myfinances.model.enums.TipoLancamento;
import com.pneri.myfinances.services.LancamentoService;
import com.pneri.myfinances.services.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor // retira a necessidade de um construtor explicito e necessario inserir nas
							// interfaces final
public class LancamentoResource {

	private final UsuarioService usuarioService;

	private final LancamentoService lancamentoService;

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {

		try {
			Lancamento entity = converter(dto);
			entity = lancamentoService.salvar(entity);
			return new ResponseEntity(entity, HttpStatus.CREATED);
		} catch (LancamentoBussinessException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {

		return lancamentoService.findLancamentoById(id).map(entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (LancamentoBussinessException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lancamento nao encontrado na base de dados", HttpStatus.BAD_REQUEST));

	}

	@DeleteMapping("/{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {

		return lancamentoService.findLancamentoById(id).map(entity -> {
			lancamentoService.deletar(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Lancamento nao encontrado na base de dados", HttpStatus.BAD_REQUEST));

	}

	/**
	 * Outra forma de passar os paramentros
	 * 
	 * @param params
	 * @return
	 * @GetMapping
	 * 
	 *             public ResponseEntity buscar(@RequestParam
	 *             java.util.Map<String,String>params) {
	 * 
	 *             }
	 */

	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano, @RequestParam("usuario") Long idUsuario) {

		Lancamento filtro = new Lancamento();

		filtro.setDescricao(descricao);
		filtro.setAno(ano);
		filtro.setMes(mes);

		Optional<Usuario> usuario = usuarioService.findUsuarioById(idUsuario);

		if (!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Nao foi possivel realizar a consulta");
		} else {
			filtro.setUsuario(usuario.get());
		}

		List<Lancamento> lancamentos = lancamentoService.buscar(filtro);

		return ResponseEntity.ok(lancamentos);

	}

	private Lancamento converter(LancamentoDTO dto) {

		Lancamento lancamento = new Lancamento();

		lancamento.setId(dto.getId());
		lancamento.setAno(dto.getAno());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());

		Usuario usuario = usuarioService.findUsuarioById(dto.getUsuario()).orElseThrow(
				() -> new LancamentoBussinessException("Usuario nao encontrado para o id informado " + dto.getId()));

		lancamento.setUsuario(usuario);
		lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));

		return lancamento;
	}

}
