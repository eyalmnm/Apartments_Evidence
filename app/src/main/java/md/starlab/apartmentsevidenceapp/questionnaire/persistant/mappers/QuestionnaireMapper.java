package md.starlab.apartmentsevidenceapp.questionnaire.persistant.mappers;

import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.questionnaire.model.QuestionModel;
import md.starlab.apartmentsevidenceapp.questionnaire.model.QuestionnaireModel;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.QuestionEntity;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.QuestionnaireEntity;

public class QuestionnaireMapper {

    public static final QuestionnaireModel convertToModel(QuestionnaireEntity entity) {
        return new QuestionnaireModel(entity.getId(), entity.getTitle(),
                getQuestionModels(entity.getQuestions()));
    }

    private static ArrayList<QuestionModel> getQuestionModels(ArrayList<QuestionEntity> entities) {
        ArrayList<QuestionModel> models = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            models.add(QuestionMapper.convertToModel(entities.get(i)));
        }
        return models;
    }

    public static final QuestionnaireEntity convertToEntity(QuestionnaireModel model) {
        ArrayList<QuestionEntity> questions = getQuestionEntities(model.questions);
        return new QuestionnaireEntity(model.id, model.title, questions);
    }

    private static ArrayList<QuestionEntity> getQuestionEntities(ArrayList<QuestionModel> models) {
        ArrayList<QuestionEntity> entities = new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            entities.add(QuestionMapper.convertToEntity(models.get(i)));
        }
        return entities;
    }
}
