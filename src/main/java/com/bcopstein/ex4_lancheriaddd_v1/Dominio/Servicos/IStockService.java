package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import java.util.List;

public interface IStockService {
        boolean verifyItem(ItemPedido item);
        void getStock(List<ItemPedido> itens);    
}
