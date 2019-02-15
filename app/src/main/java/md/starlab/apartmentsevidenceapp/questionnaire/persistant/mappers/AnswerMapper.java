package md.starlab.apartmentsevidenceapp.questionnaire.persistant.mappers;

import md.starlab.apartmentsevidenceapp.questionnaire.model.AnswerModel;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.AnswerEntity;

public class AnswerMapper {

    public static final AnswerModel convertToModel(AnswerEntity entity) {
        return new AnswerModel(entity.getId(), entity.getTitle());
    }

    public static final AnswerEntity convertToEntity(AnswerModel model) {
        return new AnswerEntity(model.key, model.value);
    }
}
