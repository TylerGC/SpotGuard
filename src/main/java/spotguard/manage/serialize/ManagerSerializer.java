package spotguard.manage.serialize;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import spotguard.manage.Manager;

public class ManagerSerializer implements JsonSerializer<Manager> {

	@Override
	public JsonElement serialize(Manager manager, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		//result.add("users", new JsonPrimitive(Manager.userMap));
		return null;
	}

}
