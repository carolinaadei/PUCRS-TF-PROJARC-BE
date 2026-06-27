package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

public class FakeStockService implements IStockService {
    
    @Override
    public boolean verifyItem(ItemPedido item) {
        /*
        Verifiy the stock for given items and set the order status to "APPROVED" 
        if everything is okay.
        
        Returns:
            false: if there's no ingredients for the item.
            true: if all ingredients for the item are available.
        */
        System.out.println("[FAKE STOCK] Checking availability for: " + item);
        System.out.println("Status: OK!");
        return true;
    }

    @Override
    public void getStock(List<ItemPedido> itens) {
        /*
        Get all ingredients from the Stock.
        
        Returns:
            List<ItemPedido>: list with all ingredients available.
        */
        System.out.print(true);
    }
}