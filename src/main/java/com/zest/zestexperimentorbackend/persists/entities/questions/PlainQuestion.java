package com.zest.zestexperimentorbackend.persists.entities.questions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.zest.zestexperimentorbackend.persists.entities.questionchoices.QuestionChoice;
import com.zest.zestexperimentorbackend.persists.entities.questionmedias.QuestionMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;


@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeName("PlainQuestion")
public class PlainQuestion extends BaseQuestion{

}
