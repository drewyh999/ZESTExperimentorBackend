package com.zest.zestexperimentorbackend;

import com.zest.zestexperimentorbackend.Entities.QuestionChoices.QuestionChoice;
import com.zest.zestexperimentorbackend.Entities.QuestionChoices.MultipleTextChoice;
import com.zest.zestexperimentorbackend.Entities.QuestionMedias.QuestionMedia;
import com.zest.zestexperimentorbackend.Entities.QuestionMedias.TextQuestionMedia;
import com.zest.zestexperimentorbackend.Entities.Questions.BaseQuestion;
import com.zest.zestexperimentorbackend.Entities.Questions.DemographicQuestion;
import com.zest.zestexperimentorbackend.Repositories.QuestionRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    private static final Log log = LogFactory.getLog(RepositoryTest.class);

    private BaseQuestion getQuestion(){
        //@TODO Maybe Randomly generate some test questions for CRUD tests
        QuestionMedia media = new TextQuestionMedia("Who is the number one beauty in Milan?");
        QuestionChoice choices = new MultipleTextChoice(Arrays.asList("A. Yuxi Liu","B. Yuanhao Zhu","C. Siqian Huang"));
        return new DemographicQuestion(media, BaseQuestion.QuestionType.MULTI_CHOICE,choices,"BeautyMilan");
    }

    @Test
    public void repoCreate() throws Exception{
        questionRepository.deleteAll();
        BaseQuestion q = getQuestion();
        BaseQuestion returned = questionRepository.save(q);
        assertEquals(q,returned);
    }

    @Test
    public void repoSearch() throws Exception{
        List<BaseQuestion> q = questionRepository.findAllByAliasContains("Beauty");
        for(BaseQuestion qe : q){
            log.info(qe.toString());
        }
    }

    //More test on every entity
}
