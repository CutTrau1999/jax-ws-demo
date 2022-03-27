package com.example.jax_ws.resource;

import com.example.jax_ws.entity.ShoppingCart;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ShoppingCartResource {
    Response get(int userId);

    Response add(int userId, int productId, int quantity);


    Response update(int userId, int productId, int quantity);


    Response remove(int userId, int productId);


    Response clear(int userId);
}
