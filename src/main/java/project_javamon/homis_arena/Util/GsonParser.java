package project_javamon.homis_arena.Util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project_javamon.homis_arena.Game.Actions.IAction;
import project_javamon.homis_arena.Game.Pokemon.Card;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

public class GsonParser {
    public GsonParser() {
    }
    public static ObservableList<Card> parseDeck(File file) {
        try {
            String json = getAllJsonString(file);

            // GSON MAGIC ==============================================
            Gson gson = new GsonBuilder().registerTypeAdapter(Card.class, new CardDeserializer()).create();
            Type listType = new TypeToken<ArrayList<Card>>(){}.getType();
            ArrayList<Card> arrayList = gson.fromJson(json, listType);
            // GSON MAGIC END ==========================================

            return FXCollections.observableArrayList(arrayList);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return FXCollections.observableArrayList();
        } catch (JsonSyntaxException e) {
            System.out.println("JSON is not properly formatted: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }
    public static ArrayList<IAction> getAllAttack(String file) {
        try {
            String json = getAllJsonString(new File(file));

            // GSON MAGIC ==============================================
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(IAction.class, new IActionDeserializer());
            Gson gson = builder.create();
            // GSON MAGIC END ==========================================

            Type actionListType = new TypeToken<ArrayList<IAction>>(){}.getType();
            ArrayList<IAction> actionList = gson.fromJson(json, actionListType);
            return actionList;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.out.println("JSON is not properly formatted: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public static ArrayList<IAction> getAllDraw(String file) {
        try {
            String json = getAllJsonString(new File(file));

            // GSON MAGIC ==============================================
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(IAction.class, new IActionDeserializer());
            Gson gson = builder.create();
            // GSON MAGIC END ==========================================

            Type actionListType = new TypeToken<ArrayList<IAction>>(){}.getType();
            ArrayList<IAction> actionList = gson.fromJson(json, actionListType);
            return actionList;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.out.println("JSON is not properly formatted: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private static String getAllJsonString(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("The file does not exist: " + file.getAbsolutePath());
        }
        try(Scanner fileScanner = new Scanner(file)) {  // use try-with-resources for proper resource handling
            StringBuilder jsonString = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                jsonString.append(fileScanner.nextLine());
            }
            return jsonString.toString();
        }
    }
}
