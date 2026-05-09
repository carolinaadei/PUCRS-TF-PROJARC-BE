package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import java.util.List;

public interface IEstoqueService {
        boolean verificaDisponibilidade(List<ItemPedido> itens);
        void baixarEstoque(List<ItemPedido> itens);
        void devolverEstoque(List<ItemPedido> itens);    
}
