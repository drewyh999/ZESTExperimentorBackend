package com.zest.zestexperimentorbackend;

import com.zest.zestexperimentorbackend.persists.entities.QuestionChoices.QuestionChoice;
import com.zest.zestexperimentorbackend.persists.entities.QuestionChoices.MultipleTextChoice;
import com.zest.zestexperimentorbackend.persists.entities.QuestionMedias.QuestionMedia;
import com.zest.zestexperimentorbackend.persists.entities.QuestionMedias.TextMedia;
import com.zest.zestexperimentorbackend.persists.entities.Questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.Questions.DemographicQuestion;
import com.zest.zestexperimentorbackend.persists.repositories.QuestionRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    private static final Log log = LogFactory.getLog(RepositoryTest.class);

    private BaseQuestion getQuestion(){
        //@TODO Maybe Randomly generate some test questions for CRUD tests
        QuestionMedia media = new TextMedia("Who is the number one beauty in Milan?");
        QuestionChoice choices = new MultipleTextChoice(Arrays.asList("A. Yuxi Liu","B. Yuanhao Zhu","C. Siqian Huang"));
        return new DemographicQuestion(media, BaseQuestion.QuestionType.MULTI_CHOICE,choices,"BeautyMilan");
    }

    @Test
    public void repoCreate() throws Exception{
        //questionRepository.deleteAll();
        BaseQuestion q = getQuestion();
        q.setId("62263767c9feff2a21500b95");
        q.setAlias("Changed Alias");
        BaseQuestion returned = questionRepository.save(q);

        assertEquals(q,returned);
        assertEquals(q,returned);
        assertEquals(q,returned);
        assertEquals(q,returned);
    }

    @Test
    public void repoSearch() throws Exception{
        questionRepository.findAll().forEach(q -> log.info(q.toString()));
    }
    //More test on every entity
}
