package project_javamon.homis_arena.Util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import project_javamon.homis_arena.Game.Pokemon.Card;
import project_javamon.homis_arena.Game.Pokemon.EnergyCard;
import project_javamon.homis_arena.Game.Pokemon.PokemonCard;
import project_javamon.homis_arena.Game.Pokemon.TrainerCard;

import java.lang.reflect.Type;

class CardDeserializer implements JsonDeserializer<Card> {
    @Override
    public Card deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        // Helps GSON understand what class it is making dependent on content
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("hp") && jsonObject.has("weakness") && jsonObject.has("resistance")) {
            return context.deserialize(jsonObject, PokemonCard.class);
        } else if (jsonObject.has("type")) {
            return context.deserialize(jsonObject, EnergyCard.class);
        } else {
            return context.deserialize(jsonObject, TrainerCard.class);
        }
    }
}
