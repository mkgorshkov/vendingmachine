package com.mgorshkov.deltadna;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mgorshkov.deltadna.dao.Coins;
import com.mgorshkov.deltadna.dao.model.Inventory;
import com.mgorshkov.deltadna.dao.model.Machine;
import com.mgorshkov.deltadna.dao.model.Register;
import com.mgorshkov.deltadna.dao.service.InventoryDAO;
import com.mgorshkov.deltadna.dao.service.MachineDAO;
import com.mgorshkov.deltadna.dao.service.RegisterDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

/**
 * Items endpoint allows the purchasing of items from a vending machine.
 */
@Path("items")
public class Items {

    /**
     * Given a valid machine and a valid product, an average user can purchase a product from the machine given they've
     * input at the amount of change equal to the price of the product.
     * @param machineId - Valid machine
     * @param productId - Valid product
     * @return An error if the machine or product is invalid. Otherwise, we dispense the product and return the change if
     *          the correct amount of money is inserted and the product is available. Otherwise, we show a guiding message and
     *          allow the user to purchase something else.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{machine}/buy/{productid}")
    public Response addCoin(@PathParam("machine") int machineId, @PathParam("productid") int productId) {
        MachineDAO machineDAO = new MachineDAO();
        Machine machine = machineDAO.getMachineByID(machineId);

        if(machine == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        InventoryDAO inventoryDAO = new InventoryDAO();
        Inventory inventory = inventoryDAO.getInventoryByMachineIdAndProduct(machineId, productId);

        RegisterDAO registerDAO = new RegisterDAO();

        if(inventory == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String vendingDetails = "";
        String changeDetails = "";

        if(machine.getInsertedValue() >= inventory.getPrice()){
            if(inventory.getCurrentNumber() > 0){
                vendingDetails += "Dispensed item '"+inventory.getProduct().getName()+"'. ";
                inventoryDAO.sellItemFromInventory(inventory);
                machineDAO.sellItem(machineId, inventory.getPrice());

                changeDetails = registerDAO.dispenseChange(machine);
                machineDAO.flushInsertedCoins(machineId);
            }else{
                vendingDetails += "Could not dispense item '"+inventory.getProduct().getName()+"' since we're out of stock. ";
                changeDetails = "No change returned. Please make another selection. ";
            }
        }else{
            vendingDetails += "Not enough change is inserted to dispense item '"+inventory.getProduct().getName()+"'. ";
        }

        Gson gson = new Gson();
        JsonObject reponseObject = new JsonObject();
        reponseObject.addProperty("Vending", vendingDetails);
        reponseObject.addProperty("Returned", changeDetails);

        return Response.ok(gson.toJson(reponseObject)).build();
    }
}
