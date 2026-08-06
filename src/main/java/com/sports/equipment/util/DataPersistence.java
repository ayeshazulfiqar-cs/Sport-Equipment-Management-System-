package com.sports.equipment.util;

import com.google.gson.*;
import com.sports.equipment.model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * DataPersistence handles loading and saving data to JSON files.
 */
public class DataPersistence {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.json";
    private static final String EQUIPMENT_FILE = DATA_DIR + "/equipment.json";
    private static final String REQUESTS_FILE = DATA_DIR + "/requests.json";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "/transactions.json";
    private static final String MAINTENANCE_FILE = DATA_DIR + "/maintenance.json";

    private Gson gson;

    public DataPersistence() {
        GsonBuilder builder = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(User.UserRole.class, new EnumSerializer<>())
                .registerTypeAdapter(Equipment.EquipmentStatus.class, new EnumSerializer<>())
                .registerTypeAdapter(EquipmentRequest.RequestStatus.class, new EnumSerializer<>())
                .registerTypeAdapter(Transaction.TransactionStatus.class, new EnumSerializer<>())
                .registerTypeAdapter(MaintenanceRecord.MaintenanceType.class, new EnumSerializer<>());
        this.gson = builder.create();
        initializeDirectories();
    }

    private void initializeDirectories() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Users
    public List<User> loadUsers() {
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            String json = readFile(USERS_FILE);
            Type listType = new com.google.gson.reflect.TypeToken<List<User>>() {}.getType();
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveUsers(List<User> users) {
        try {
            String json = gson.toJson(users);
            writeFile(USERS_FILE, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Equipment
    public List<Equipment> loadEquipment() {
        try {
            File file = new File(EQUIPMENT_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            String json = readFile(EQUIPMENT_FILE);
            Type listType = new com.google.gson.reflect.TypeToken<List<Equipment>>() {}.getType();
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveEquipment(List<Equipment> equipment) {
        try {
            String json = gson.toJson(equipment);
            writeFile(EQUIPMENT_FILE, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Requests
    public List<EquipmentRequest> loadRequests() {
        try {
            File file = new File(REQUESTS_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            String json = readFile(REQUESTS_FILE);
            Type listType = new com.google.gson.reflect.TypeToken<List<EquipmentRequest>>() {}.getType();
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveRequests(List<EquipmentRequest> requests) {
        try {
            String json = gson.toJson(requests);
            writeFile(REQUESTS_FILE, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Transactions
    public List<Transaction> loadTransactions() {
        try {
            File file = new File(TRANSACTIONS_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            String json = readFile(TRANSACTIONS_FILE);
            Type listType = new com.google.gson.reflect.TypeToken<List<Transaction>>() {}.getType();
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveTransactions(List<Transaction> transactions) {
        try {
            String json = gson.toJson(transactions);
            writeFile(TRANSACTIONS_FILE, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Maintenance Records
    public List<MaintenanceRecord> loadMaintenanceRecords() {
        try {
            File file = new File(MAINTENANCE_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            String json = readFile(MAINTENANCE_FILE);
            Type listType = new com.google.gson.reflect.TypeToken<List<MaintenanceRecord>>() {}.getType();
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveMaintenanceRecords(List<MaintenanceRecord> records) {
        try {
            String json = gson.toJson(records);
            writeFile(MAINTENANCE_FILE, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private void writeFile(String filename, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        }
    }

    // Custom Enum Serializer/Deserializer
    private static class EnumSerializer<E extends Enum<E>> implements JsonSerializer<E>, JsonDeserializer<E> {
        @Override
        public JsonElement serialize(E src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.name());
        }

        @Override
        public E deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String name = json.getAsString();
                @SuppressWarnings("unchecked")
                Class<E> enumClass = (Class<E>) typeOfT;
                return Enum.valueOf(enumClass, name);
            } catch (IllegalArgumentException e) {
                throw new JsonParseException(e);
            }
        }
    }
}
