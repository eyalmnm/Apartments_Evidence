package md.starlab.apartmentsevidenceapp.questionnaire.persistant.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.QuestionEntity;


public class QuestionsConverter {

    @TypeConverter
    public String convertToString(ArrayList<QuestionEntity> entities) {
        Gson gson = new Gson();
        String json = gson.toJson(entities);
        return json;
    }

    @TypeConverter
    public ArrayList<QuestionEntity> convertToEntities(String str) {
        Type listType = new TypeToken<ArrayList<QuestionEntity>>() {
        }.getType();
        return new Gson().fromJson(str, listType);
    }
}
