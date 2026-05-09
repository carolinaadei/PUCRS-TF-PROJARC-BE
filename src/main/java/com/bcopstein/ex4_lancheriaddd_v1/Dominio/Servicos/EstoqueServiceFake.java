package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class EstoqueServiceFake implements IEstoqueService {
    
    @Override
    public boolean verificaDisponibilidade(List<ItemPedido> itens) {
        // O serviço de estoque pode ser um "fake" que responde sempre que o estoque é suficiente
        return true;
}

    @Override
    public void baixarEstoque(List<ItemPedido> itens) {
        // Não faz nada, pois é um serviço fake
    }

    @Override
    public void devolverEstoque(List<ItemPedido> itens) {
        // Não faz nada, pois é um serviço fake
    }
}