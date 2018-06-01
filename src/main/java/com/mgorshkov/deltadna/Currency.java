package com.mgorshkov.deltadna;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mgorshkov.deltadna.dao.Coins;
import com.mgorshkov.deltadna.dao.model.Machine;
import com.mgorshkov.deltadna.dao.model.Register;
import com.mgorshkov.deltadna.dao.service.InventoryDAO;
import com.mgorshkov.deltadna.dao.service.MachineDAO;
import com.mgorshkov.deltadna.dao.service.RegisterDAO;
import sun.security.provider.certpath.OCSPResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

/**
 * Currency endpoint deals with money handling which an average user can perform on a vending machine.
 */
@Path("currency")
public class Currency {

    /**
     * Allows an average user to insert coins recognized by us into a vending machine
     * @param machineId - valid vending machine
     * @param coin - valid coin
     * @return An error is returned if the machine or coin is invalid. Returns a message if we cannot add more change
     *          due to a full register. Otherwise, we return the value of the coin added in cents.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{machine}/insertChange/{coin}")
    public Response addCoin(@PathParam("machine") int machineId, @PathParam("coin") int coin) {
        MachineDAO machineDAO = new MachineDAO();
        Machine machine = machineDAO.getMachineByID(machineId);

        if(machine == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Coins insertedCoin;

        if(coin == Coins.NICKEL.getValue()){
            insertedCoin = Coins.NICKEL;
        }else if(coin == Coins.DIME.getValue()){
            insertedCoin = Coins.DIME;
        }else if(coin == Coins.QUARTER.getValue()){
            insertedCoin = Coins.QUARTER;
        }else if(coin == Coins.DOLLAR.getValue()){
            insertedCoin = Coins.DOLLAR;
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        RegisterDAO registerDAO = new RegisterDAO();
        List<Register> registerList = registerDAO.getRegistersByMachineId(machineId);

        String returnString = "";

        for(Register r : registerList){
            if(coin == r.getCurrencyValue()){
                if(r.getCurrentNumber() < r.getMaxNumber()){
                    returnString = "Added "+coin+" cents to machine.";
                    registerDAO.addChange(machineId, insertedCoin);
                    machineDAO.insertCoins(machineId, insertedCoin);
                }else{
                    returnString = "Cannot add "+coin+" cents to machine since the register is full.";
                }
            }
        }

        Gson gson = new Gson();
        JsonObject reponseObject = new JsonObject();
        reponseObject.addProperty("Response", returnString);

        return Response.ok(gson.toJson(reponseObject)).build();
    }

    /**
     * Allows an average user to check how much change is currently inserted into the machine (in cents).
     * @param machineId - valid machine
     * @return An error if the machine is invalid. Otherwise we return the value in cents current inserted into machine.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{machine}/currentlyInserted/")
    public Response showCurrencyInserted(@PathParam("machine") int machineId) {
        MachineDAO machineDAO = new MachineDAO();
        Machine machine = machineDAO.getMachineByID(machineId);

        if(machine == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Gson gson = new Gson();
        JsonObject insertedValueObject = new JsonObject();
        insertedValueObject.addProperty("Inserted Money Value (Cents)", machine.getInsertedValue());
        return Response.ok(gson.toJson(insertedValueObject)).build();
    }

    /**
     * An average user can request to return the change given that they have change inserted. The coins aren't necessarily
     * what they've inserted but the same value of coins will be returned.
     * @param machineId - Valid machine
     * @return An error if the machine isn't recognized. Otherwise, the value of coins inserted is returned.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{machine}/returnChange/")
    public Response returnAllChange(@PathParam("machine") int machineId) {
        MachineDAO machineDAO = new MachineDAO();
        Machine machine = machineDAO.getMachineByID(machineId);

        if(machine == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        RegisterDAO registerDAO = new RegisterDAO();
        String response = registerDAO.dispenseChange(machine);

        Gson gson = new Gson();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("Returned", response);

        machineDAO.flushInsertedCoins(machineId);

        return Response.ok(gson.toJson(responseObject)).build();
    }
}
