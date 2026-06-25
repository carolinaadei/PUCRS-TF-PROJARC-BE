package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Mensageria;

import java.io.Serializable;

public record DeliveryMessage(long pedidoId, String enderecoEntrega) implements Serializable {}
