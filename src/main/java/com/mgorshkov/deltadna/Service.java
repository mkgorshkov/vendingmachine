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
 * Service endpoint allows maintenance on a vending machine.
 */
@Path("service")
public class Service {

    /**
     * Allows a superuser to restock products in the vending machine.
     * @param machineId - Valid machine
     * @return An error if the machine is invalid. Otherwise, restocks all of the products (sets the current number
     *          for each product to maxNumber)
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{machine}/restockProducts/")
    public Response showCurrencyInserted(@PathParam("machine") int machineId) {
        MachineDAO machineDAO = new MachineDAO();
        Machine machine = machineDAO.getMachineByID(machineId);

        if(machine == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        InventoryDAO inventoryDAO = new InventoryDAO();
        List<Inventory> inventoryList = inventoryDAO.getInventoryByMachineId(machineId);

        String toReturn = "";

        for(Inventory i : inventoryList){
            if(i.getCurrentNumber() < i.getMaxNumber()){
                toReturn += "Added "+(i.getMaxNumber()-i.getCurrentNumber())+ " items for item '"+i.getProduct().getName()+"'. ";
                inventoryDAO.replenishInventory(i);
            }
        }

        Gson gson = new Gson();
        JsonObject insertedValueObject = new JsonObject();
        if(toReturn.length() > 1){
            insertedValueObject.addProperty("Replenished Inventory", toReturn);
        }else{
            insertedValueObject.addProperty("Replenished Inventory", "All items in machine were full.");
        }
        return Response.ok(gson.toJson(insertedValueObject)).build();
    }

    /**
     * Allows a superuser to restock change in the vending machine.
     * @param machineId - Valid machine
     * @return An error if the machine is invalid. Otherwise, restocks all of the coins (sets the current number
     *          for each coin to maxNumber)
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{machine}/restockChange/")
    public Response returnAllChange(@PathParam("machine") int machineId) {
        MachineDAO machineDAO = new MachineDAO();
        Machine machine = machineDAO.getMachineByID(machineId);

        if(machine == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        RegisterDAO registerDAO = new RegisterDAO();
        List<Register> registerList = registerDAO.getRegistersByMachineId(machineId);

        String toReturn = "";

        for(Register r : registerList){
            if(r.getCurrentNumber() < r.getMaxNumber()){
                toReturn += "Added "+(r.getMaxNumber()-r.getCurrentNumber())+ " coins for '"+r.getCurrencyValue()+"'. ";
                registerDAO.replenishChange(r);
            }
        }

        Gson gson = new Gson();
        JsonObject insertedValueObject = new JsonObject();
        if(toReturn.length() > 1){
            insertedValueObject.addProperty("Replenished Register", toReturn);
        }else{
            insertedValueObject.addProperty("Replenished Register", "All registers in machine were full.");
        }

        return Response.ok(gson.toJson(insertedValueObject)).build();
    }

    /**
     * Allows a superuser to check how much change is in the vending machine by coin.
     * @param machineId - Valid machine
     * @return An error if the machine is invalid. Otherwise, returns current number of each coin.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{machine}/availableChange")
    public Response checkAvailableChange(@PathParam("machine") int machineId) {
        MachineDAO machineDAO = new MachineDAO();
        Machine machine = machineDAO.getMachineByID(machineId);

        if(machine == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        RegisterDAO registerDAO = new RegisterDAO();
        List<Register> registerList = registerDAO.getRegistersByMachineId(machineId);

        Gson gson = new Gson();
        JsonArray reponseObject = new JsonArray();

        for(Register r : registerList){
            JsonObject specificRegister = new JsonObject();
            specificRegister.addProperty("Currency Name", r.getCurrencyName());
            specificRegister.addProperty("Number of Coins", r.getCurrentNumber());
            specificRegister.addProperty("Max Number of Coins", r.getMaxNumber());

            reponseObject.add(specificRegister);
        }

        return Response.ok(gson.toJson(reponseObject)).build();
    }

    /**
     * Allows a superuser to check how much stock is in the vending machine by product.
     * @param machineId - Valid machine
     * @return An error if the machine is invalid. Otherwise, returns current number of each product.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{machine}/availableStock")
    public Response checkAvailableStock(@PathParam("machine") int machineId) {
        MachineDAO machineDAO = new MachineDAO();
        Machine machine = machineDAO.getMachineByID(machineId);

        if(machine == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        InventoryDAO inventoryDAO = new InventoryDAO();
        List<Inventory> inventoryList = inventoryDAO.getInventoryByMachineId(machineId);

        Gson gson = new Gson();
        JsonArray reponseObject = new JsonArray();

        for(Inventory i : inventoryList){
            JsonObject specificInventory = new JsonObject();
            specificInventory.addProperty("Product Name", i.getProduct().getName());
            specificInventory.addProperty("Current Number", i.getCurrentNumber());
            specificInventory.addProperty("Max Number", i.getMaxNumber());

            reponseObject.add(specificInventory);
        }

        return Response.ok(gson.toJson(reponseObject)).build();
    }
}
