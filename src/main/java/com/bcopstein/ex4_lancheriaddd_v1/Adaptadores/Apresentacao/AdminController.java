package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.IDefinirCardapioAtualUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.IDefinirPoliticaDescontoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.IListarPoliticasDescontoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.IRecuperaListaCardapiosUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CabecalhoCardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ConfiguracaoCardapio;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final IRecuperaListaCardapiosUC listaCardapiosUC;
    private final IDefinirCardapioAtualUC definirCardapioAtualUC;
    private final ConfiguracaoCardapio configuracaoCardapio;
    private final IListarPoliticasDescontoUC listarPoliticasUC;
    private final IDefinirPoliticaDescontoUC definirPoliticaUC;

    public AdminController(
            IRecuperaListaCardapiosUC listaCardapiosUC,
            IDefinirCardapioAtualUC definirCardapioAtualUC,
            ConfiguracaoCardapio configuracaoCardapio,
            IListarPoliticasDescontoUC listarPoliticasUC,
            IDefinirPoliticaDescontoUC definirPoliticaUC) {
        this.listaCardapiosUC = listaCardapiosUC;
        this.definirCardapioAtualUC = definirCardapioAtualUC;
        this.configuracaoCardapio = configuracaoCardapio;
        this.listarPoliticasUC = listarPoliticasUC;
        this.definirPoliticaUC = definirPoliticaUC;
    }

    // UC1 — listar cardápios disponíveis
    @GetMapping("/cardapio/lista")
    public CabecalhoCardapioResponse listarCardapios() {
        return listaCardapiosUC.run();
    }

    // UC2 — consultar e definir cardápio corrente
    @GetMapping("/cardapio/atual")
    public CardapioAtualResponse consultarCardapioAtual() {
        return new CardapioAtualResponse(configuracaoCardapio.getIdCardapioAtual());
    }

    @PutMapping("/cardapio/atual")
    public CardapioAtualResponse definirCardapioAtual(@RequestBody CardapioAtualRequest request) {
        long novoId = definirCardapioAtualUC.run(request.idCardapio());
        return new CardapioAtualResponse(novoId);
    }

    // UC3 — listar políticas de desconto disponíveis
    @GetMapping("/desconto/politicas")
    public List<String> listarPoliticas() {
        return listarPoliticasUC.run();
    }

    // UC4 — definir política de desconto corrente
    @PutMapping("/desconto/politica")
    public PoliticaDescontoResponse definirPolitica(@RequestBody PoliticaDescontoRequest request) {
        String codigo = definirPoliticaUC.run(request.codigo());
        return new PoliticaDescontoResponse(codigo);
    }

    public record CardapioAtualRequest(long idCardapio) {}
    public record CardapioAtualResponse(long idCardapioAtual) {}
    public record PoliticaDescontoRequest(String codigo) {}
    public record PoliticaDescontoResponse(String politicaCorrente) {}
}
