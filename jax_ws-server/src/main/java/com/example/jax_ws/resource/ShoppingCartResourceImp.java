package com.example.jax_ws.resource;

import com.example.jax_ws.entity.Product;
import com.example.jax_ws.entity.ShoppingCart;
import com.example.jax_ws.model.ProductModel;
import com.example.jax_ws.model.ShoppingCartModel;
import com.example.jax_ws.model.ShoppingCartModelImp;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/carts")
public class ShoppingCartResourceImp implements ShoppingCartResource {

    private ShoppingCartModel shoppingCartModel;
    private ProductModel productModel;

    public ShoppingCartResourceImp() {
        this.shoppingCartModel = new ShoppingCartModelImp();
        this.productModel = new ProductModel();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@HeaderParam("Authorization") int userId) {
        try {
            return Response.status(Response.Status.OK).entity(this.shoppingCartModel.get(userId)).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ShoppingCart()).build();
        }
    }

    @GET
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@HeaderParam("Authorization") int userId,
                        @QueryParam("productId") int productId,
                        @QueryParam("quantity") int quantity) {

        if (quantity <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Product product = null;
        try {
            product = this.productModel.findById(productId);
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (product == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        ShoppingCart shoppingCart = null;
        try {

            shoppingCart = this.shoppingCartModel.get(userId);
        } catch (SQLException e) {
            e.printStackTrace();

        }
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
        }

        shoppingCart.add(product, quantity);
        try {
            shoppingCart = this.shoppingCartModel.save(shoppingCart);
        } catch (SQLException e) {
            e.printStackTrace();
            shoppingCart = null;
        }
        if (shoppingCart == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ShoppingCart()).build();
        }
        return Response.status(Response.Status.CREATED).entity(shoppingCart).build();
    }

    @GET
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(@HeaderParam("Authorization") int userId,
                           @QueryParam("productId") int productId,
                           @QueryParam("quantity") int quantity) {
        return null;
    }

    @GET
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response remove(@HeaderParam("Authorization") int userId, @QueryParam("productId") int productId) {
        return null;
    }

    @GET
    @Path("/clear")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response clear(@HeaderParam("Authorization") int userId) {
        return null;
    }
}
