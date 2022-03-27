package com.example.jax_ws.resource;

import com.example.jax_ws.entity.Cart;
import com.example.jax_ws.model.CartModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;


@Path("/cart")
public class CartResource {

    private CartModel cartModel;

    public CartResource() {
        this.cartModel = new CartModel();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        try {
            return Response.status(Response.Status.OK).entity(cartModel.findAll()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.OK).entity(new ArrayList<>()).build();
        }
    }



    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(Cart cart) {
        try {
            Cart savedCart = cartModel.save(cart);
            return Response.status(Response.Status.CREATED).entity(savedCart).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }



}

