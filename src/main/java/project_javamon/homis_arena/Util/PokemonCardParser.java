package project_javamon.homis_arena.Util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class PokemonCardParser {
    public PokemonCardParser() {
    }
    public static ObservableList<Card> getAllAttributes(File file) {
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

    private static String getAllJsonString(File file) throws FileNotFoundException {

        if (!file.exists()) {
            throw new FileNotFoundException("The file does not exist: " + file.getAbsolutePath());
        }

        Scanner fileScanner = new Scanner(file);
        StringBuilder jsonString = new StringBuilder();

        while (fileScanner.hasNextLine()) {
            jsonString.append(fileScanner.nextLine());
        }

        fileScanner.close();
        return jsonString.toString();
    }
}
