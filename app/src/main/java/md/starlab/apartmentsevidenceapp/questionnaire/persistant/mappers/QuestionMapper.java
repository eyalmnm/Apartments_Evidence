package md.starlab.apartmentsevidenceapp.questionnaire.persistant.mappers;

import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.questionnaire.model.AnswerModel;
import md.starlab.apartmentsevidenceapp.questionnaire.model.QuestionModel;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.AnswerEntity;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.QuestionEntity;

public class QuestionMapper {

    public static final QuestionModel convertToModel(QuestionEntity entity) {
        return new QuestionModel(entity.getId(), entity.getTitle(), entity.getType(),
                getAnswerModels(entity.getAnswers()));
    }

    private static ArrayList<AnswerModel> getAnswerModels(ArrayList<AnswerEntity> entities) {
        ArrayList<AnswerModel> models = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            models.add(AnswerMapper.convertToModel(entities.get(i)));
        }
        return models;
    }

    public static final QuestionEntity convertToEntity(QuestionModel model) {
        ArrayList<AnswerEntity> answers = getAnswerEntities(model.answers);
        return new QuestionEntity(model.id, model.title, model.type, answers);
    }

    private static ArrayList<AnswerEntity> getAnswerEntities(ArrayList<AnswerModel> models) {
        ArrayList<AnswerEntity> entities = new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            entities.add(AnswerMapper.convertToEntity(models.get(i)));
        }
        return entities;
    }
}
