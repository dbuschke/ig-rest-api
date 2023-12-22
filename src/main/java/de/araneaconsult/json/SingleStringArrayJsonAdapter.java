package de.araneaconsult.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Converts JSON into a String Array. Can be used if you get a String
 * if the attribute has only one value or an Array if the attribute has multiple values. 
 */
public class SingleStringArrayJsonAdapter implements JsonSerializer<String[]>, JsonDeserializer<String[]> {

	@Override
	public JsonElement serialize(String[] array, Type t, JsonSerializationContext jsc) {
		if (array.length == 1) {
			return jsc.serialize(array[0]);
		} else {
			return jsc.serialize(array);
		}
	}

	@Override
	public String[] deserialize(JsonElement json, Type t, JsonDeserializationContext jdc)
			throws JsonParseException {
		if (json.isJsonArray()) {
			return  jdc.deserialize(json, t);
		} else {
			String[] ret = new String[1];
			ret[0] = jdc.deserialize(json, String.class);
			return ret;
		}
	}
}
