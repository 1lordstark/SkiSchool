package controllers.rest.binders;

import java.lang.reflect.Type;

import models.LessonType;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class LessonTypeDeserializer implements JsonDeserializer<LessonType> {

    @Override
    public LessonType deserialize(JsonElement json, Type typeOf, JsonDeserializationContext context) throws JsonParseException {
        LessonType lessonType;
        switch (json.getAsJsonPrimitive().getAsString().toLowerCase()) {
        case "individual":
            lessonType = LessonType.INDIVIDUAL;
            break;
        case "group":
            lessonType = LessonType.GROUP;
            break;
        case "kindergarten":
            lessonType = LessonType.KINDERGARTEN;
            break;
        default:
            throw new JsonParseException(String.format("Lesson type %s doesn't exist", json.getAsJsonPrimitive().getAsString()));
        }

        return lessonType;
    }

}
