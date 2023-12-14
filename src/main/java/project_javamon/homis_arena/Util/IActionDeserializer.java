package project_javamon.homis_arena.Util;


import com.google.gson.*;
import project_javamon.homis_arena.Game.Actions.Attack;
import project_javamon.homis_arena.Game.Actions.IAction;

import java.lang.reflect.Type;

public class IActionDeserializer implements JsonDeserializer<IAction> {
    @Override
    public IAction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Gson().fromJson(json, Attack.class);
    }
}
