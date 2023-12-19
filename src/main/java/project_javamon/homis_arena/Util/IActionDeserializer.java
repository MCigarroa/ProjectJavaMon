package project_javamon.homis_arena.Util;


import com.google.gson.*;
import project_javamon.homis_arena.Game.Actions.Attack;
import project_javamon.homis_arena.Game.Actions.Draw;
import project_javamon.homis_arena.Game.Actions.IAction;

import java.lang.reflect.Type;

public class IActionDeserializer implements JsonDeserializer<IAction> {
    @Override
    public IAction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("damage")) {
            return context.deserialize(jsonObject, Attack.class);
        }

        if (jsonObject.has("name") && jsonObject.has("amount")) {
            return context.deserialize(jsonObject, Draw.class);
        }
        return null;
    }
}
