package com.maestrano.json;

import java.lang.reflect.Type;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class TimeZoneDeserializer implements JsonDeserializer<TimeZone> {
	public TimeZone deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return json == null ? null : TimeZone.getTimeZone(json.getAsString());
	}
}
