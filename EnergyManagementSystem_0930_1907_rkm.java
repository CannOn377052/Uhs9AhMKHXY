// 代码生成时间: 2025-09-30 19:07:14
import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnergyManagementSystem {

    private Map<String, EnergyData> energyDataMap = new HashMap<>();
    private static final Gson gson = new Gson();

    /**
     * Represents the energy data entity.
     */
    public static class EnergyData {
        private String id;
        private double consumption;
        private String unit;
        private long timestamp;

        // Constructor, getters, setters and toString methods are omitted for brevity.
    }

    public EnergyManagementSystem() {
        // Initialization of the energy management system.
        notFound((request, response) -> "This resource does not exist.\
");
    }

    /**
     * Adds a new energy data record to the system.
     * @param energyData The energy data to add.
     */
    public void addEnergyData(EnergyData energyData) {
        energyDataMap.put(energyData.getId(), energyData);
    }

    /**
     * Retrieves all energy data from the system.
     * @return A JSON string of all energy data.
     */
    public String getAllEnergyData() {
        return gson.toJson(energyDataMap.values());
    }

    /**
     * Retrieves a specific energy data record by ID.
     * @param id The ID of the energy data to retrieve.
     * @return A JSON string of the energy data if found, otherwise null.
     */
    public String getEnergyDataById(String id) {
        EnergyData energyData = energyDataMap.get(id);
        return energyData != null ? gson.toJson(energyData) : null;
    }

    /**
     * Updates an existing energy data record.
     * @param id The ID of the energy data to update.
     * @param updatedEnergyData The updated energy data.
     * @return The updated energy data as a JSON string if successful, otherwise null.
     */
    public String updateEnergyData(String id, EnergyData updatedEnergyData) {
        if (energyDataMap.containsKey(id)) {
            energyDataMap.put(id, updatedEnergyData);
            return gson.toJson(updatedEnergyData);
        }
        return null;
    }

    /**
     * Deletes an energy data record by ID.
     * @param id The ID of the energy data to delete.
     * @return True if the deletion was successful, otherwise false.
     */
    public boolean deleteEnergyData(String id) {
        if (energyDataMap.containsKey(id)) {
            energyDataMap.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Starts the Spark server and sets up the routes.
     */
    public void startServer() {
        port(4567); // Set the port on which the server will run.

        // POST route to add new energy data.
        post("/energy", "application/json", (request, response) -> {
            EnergyData energyData = gson.fromJson(request.body(), EnergyData.class);
            try {
                addEnergyData(energyData);
                response.status(201); // Created status.
                return gson.toJson(energyData);
            } catch (Exception e) {
                response.status(500); // Internal server error.
                return "Error adding energy data: " + e.getMessage();
            }
        });

        // GET route to retrieve all energy data.
        get("/energy", (request, response) -> getAllEnergyData());

        // GET route to retrieve energy data by ID.
        get("/energy/:id", (request, response) -> getEnergyDataById(request.params(":id\))));

        // PUT route to update existing energy data.
        put("/energy/:id", "application/json", (request, response) -> {
            EnergyData updatedEnergyData = gson.fromJson(request.body(), EnergyData.class);
            try {
                if (updateEnergyData(request.params(":id\)