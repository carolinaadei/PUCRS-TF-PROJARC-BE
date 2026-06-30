package com.bcopstein.deliveryservice.dto;

import java.io.Serializable;

public record DeliveryMessage(long pedidoId, String enderecoEntrega) implements Serializable {}
