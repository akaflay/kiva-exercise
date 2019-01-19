package kiva.exercise.com.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtils {
	static ObjectMapper mapper = new ObjectMapper();

	public static ArrayNode createArrayNode() {
		return mapper.createArrayNode();
	}

	public static ObjectNode createObjectNode() {
		return mapper.createObjectNode();
	}

	/**
	 *
	 * This function is used to convert the Object ot the Json String.
	 *
	 * @param obj Object to be converted to the JSON String.
	 * @return Json String of the object passed.
	 *
	 */
	public static String toJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Log Exception
			return null;
		}
	}

	/**
	 *
	 * This function is used to convert the JSON String to the Object.
	 *
	 * @param jsonString The JSON string representing the Object.
	 * @param cls The Class of the Object to convert the JSON string to.
	 * @return Object converted of class cls form JSON string
	 *
	 */
	public static <T> T fromJson(String jsonString,  Class<T> cls) {
		try {
			return mapper.readValue(jsonString, cls);
		} catch (Exception e) {
			// TODO Log Exception
			return null;
		}
	}

}
