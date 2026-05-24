package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.CabecalhoCardapioPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.CardapioPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.IRecuperaListaCardapiosUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.IRecuperarCardapioAtualUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.IRecuperarCardapioUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@RestController
@RequestMapping("/cardapio")
public class CardapioController {
    private IRecuperarCardapioUC recuperaCardapioUC;
    private IRecuperaListaCardapiosUC recuperaListaCardapioUC;
    private IRecuperarCardapioAtualUC recuperaCardapioAtualUC;

    public CardapioController(IRecuperarCardapioUC recuperaCardapioUC,
                              IRecuperaListaCardapiosUC recuperaListaCardapioUC,
                              IRecuperarCardapioAtualUC recuperaCardapioAtualUC) {
        this.recuperaCardapioUC = recuperaCardapioUC;
        this.recuperaListaCardapioUC = recuperaListaCardapioUC;
        this.recuperaCardapioAtualUC = recuperaCardapioAtualUC;
    }

    @GetMapping("/atual")
    @CrossOrigin("*")
    public CardapioPresenter recuperaCardapioAtual(){
        CardapioResponse cardapioResponse = recuperaCardapioAtualUC.run();
        return mappingToPresenter(cardapioResponse);
    }

    
    @GetMapping("/{id}")
    @CrossOrigin("*")
    public CardapioPresenter recuperaCardapio(@PathVariable(value="id")long id){
        CardapioResponse cardapioResponse = recuperaCardapioUC.run(id);
        return mappingToPresenter(cardapioResponse);
    }

    @GetMapping("/lista")
    @CrossOrigin("*")
    public List<CabecalhoCardapioPresenter> recuperaListaCardapios(){
         return recuperaListaCardapioUC.run().cabecalhos().stream()
            .map(cabCar -> new CabecalhoCardapioPresenter(cabCar.id(),cabCar.titulo()))
            .toList();
    }

    private CardapioPresenter mappingToPresenter(CardapioResponse cardapioResponse) {
        Set<Long> conjIdSugestoes = new HashSet<>(cardapioResponse.getSugestoesDoChef().stream()
            .map(Produto::getId)
            .toList());
        CardapioPresenter cardapioPresenter = new CardapioPresenter(cardapioResponse.getCardapio().getCabecalhoCardapio().titulo());
        for(Produto produto : cardapioResponse.getCardapio().getProdutos()){
            boolean sugestao = conjIdSugestoes.contains(produto.getId());
            cardapioPresenter.insereItem(produto.getId(), produto.getDescricao(), produto.getPreco(), sugestao);
        }
        return cardapioPresenter;
    }
}
