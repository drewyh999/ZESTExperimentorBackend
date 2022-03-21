package com.zest.zestexperimentorbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.zestexperimentorbackend.persists.entities.questionchoices.QuestionChoice;
import com.zest.zestexperimentorbackend.persists.entities.questionchoices.MultipleTextChoice;
import com.zest.zestexperimentorbackend.persists.entities.questionmedias.QuestionMedia;
import com.zest.zestexperimentorbackend.persists.entities.questionmedias.TextMedia;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.questions.PlainQuestion;
import com.zest.zestexperimentorbackend.persists.entities.questions.TimedQuestion;
import com.zest.zestexperimentorbackend.persists.entities.schedules.EarlyStoppingSchedule;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.entities.schedules.ScheduleModule;
import com.zest.zestexperimentorbackend.persists.repositories.QuestionRepository;
import com.zest.zestexperimentorbackend.persists.repositories.ScheduleRepository;
import com.zest.zestexperimentorbackend.persists.repositories.TesteeRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TesteeRepository testeeRepository;

    private static final Log log = LogFactory.getLog(RepositoryTest.class);

    private BaseQuestion getQuestion(){
//        QuestionMedia media = new TextMedia("Who is the number one beauty in Milan?");
//        QuestionChoice choices = new MultipleTextChoice(Arrays.asList("A. Yuxi Liu","B. Yuanhao Zhu","C. Siqian Huang"));
//        return new PlainQuestion(media, BaseQuestion.QuestionType.MULTI_CHOICE,choices,"BeautyMilan");
        return null;
    }

    @Test
    public void repoCreate() throws Exception{
        scheduleRepository.deleteAll();
        Schedule schedule = new EarlyStoppingSchedule(2);
        schedule.setScheduleType(Schedule.ScheduleType.PILOT);
        schedule.setAlias("Test");
        schedule.setTestGroup("testgroup");
        List<String> questionIdList = new ArrayList<>();
        questionIdList.add("62263767c9feff2a21500b95");
        questionIdList.add("622bcd4963bfe04aa02ff513");
        questionIdList.add("622bcd4c63bfe04aa02ff514");
        ScheduleModule module_1 = new ScheduleModule(questionIdList);
        ScheduleModule module_2 = new ScheduleModule(questionIdList);
        ScheduleModule module_3 = new ScheduleModule(questionIdList);
        List<ScheduleModule> moduleList = new ArrayList<>();
        moduleList.add(module_1);
        moduleList.add(module_2);
        moduleList.add(module_3);
        schedule.setScheduleModuleList(moduleList);
        scheduleRepository.save(schedule);
    }

    @Test
    public void repoSearch() throws Exception{
        testeeRepository.deleteAll();
    }

    @Test
    public void repoDelete() throws Exception{
        questionRepository.deleteAll();
    }
    @Test
    public void polySerialization() throws Exception{
        var mapper = new ObjectMapper();
        BaseQuestion q = questionRepository.findById("622bcd4c63bfe04aa02ff514").orElseThrow();
        log.info(mapper.writeValueAsString(((TimedQuestion)q)));
    }
    //More test on every entity
}
