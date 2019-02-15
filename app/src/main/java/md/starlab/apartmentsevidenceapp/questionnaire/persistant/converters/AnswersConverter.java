package md.starlab.apartmentsevidenceapp.questionnaire.persistant.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.AnswerEntity;


public class AnswersConverter {

    @TypeConverter
    public String convertToString(ArrayList<AnswerEntity> entities) {
        Gson gson = new Gson();
        String json = gson.toJson(entities);
        return json;
    }

    @TypeConverter
    public ArrayList<AnswerEntity> convertToEntities(String str) {
        Type listType = new TypeToken<ArrayList<AnswerEntity>>() {
        }.getType();
        return new Gson().fromJson(str, listType);
    }
}
