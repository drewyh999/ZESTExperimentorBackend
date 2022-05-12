package com.zest.zestexperimentorbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.cacheobjects.AnswerStateCache;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.questions.DemographicQuestion;
import com.zest.zestexperimentorbackend.persists.entities.questions.CodeEvaluation;
import com.zest.zestexperimentorbackend.persists.entities.schedules.EarlyStoppingSchedule;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.entities.schedules.ScheduleModule;
import com.zest.zestexperimentorbackend.persists.repositories.QuestionRepository;
import com.zest.zestexperimentorbackend.persists.repositories.ScheduleRepository;
import com.zest.zestexperimentorbackend.persists.repositories.TesteeRepository;
import com.zest.zestexperimentorbackend.services.TesteeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TesteeRepository testeeRepository;

    @Autowired
    TesteeService testeeService;

    private static final Log log = LogFactory.getLog(RepositoryTest.class);

    @Test
    public void repoCreate() throws Exception{
        scheduleRepository.deleteAll();
        Schedule schedule = new EarlyStoppingSchedule(2);
        schedule.setScheduleType(Schedule.ScheduleType.PILOT);
        schedule.setAlias("Test");
        schedule.setTestGroup("testgroup");
        List<String> demographicQuestionIdList = new ArrayList<>();
        demographicQuestionIdList.add("62263767c9feff2a21500b95");
        demographicQuestionIdList.add("622bcd4963bfe04aa02ff513");
        demographicQuestionIdList.add("622bcd4c63bfe04aa02ff514");
        List<String> codeQuestionIdList = new ArrayList<>();
        codeQuestionIdList.add("626715b106de306f38c727b0");
        codeQuestionIdList.add("626715b106de306f38c727b1");
        ScheduleModule module_1 = new ScheduleModule(codeQuestionIdList);
        module_1.setModuleType(ScheduleModule.ModuleType.CODE);
        ScheduleModule module_3 = new ScheduleModule(demographicQuestionIdList);
        module_3.setModuleType(ScheduleModule.ModuleType.DEMO);
        List<ScheduleModule> moduleList = new ArrayList<>();
        moduleList.add(module_1);
        moduleList.add(module_3);
        schedule.setScheduleModuleList(moduleList);
        scheduleRepository.save(schedule);
    }
    @Test
    public void shuffleList() throws Exception{
        List<String> questionIdList = new ArrayList<>();
        questionIdList.add("62263767c9feff2a21500b95");
        questionIdList.add("622bcd4963bfe04aa02ff513");
        questionIdList.add("622bcd4c63bfe04aa02ff514");
        log.info(questionIdList);
        AnswerStateCache answerStateCache = new AnswerStateCache();
        Collections.shuffle(questionIdList, new Random(System.currentTimeMillis()));
        log.info(questionIdList);

        answerStateCache.setCurrentModuleQuestionIDList(questionIdList);
        log.info(answerStateCache.getCurrentModuleQuestionIDList());

    }

    @Test
    public void repoSearch() throws Exception{
        testeeRepository.deleteAll();
//        questionRepository.deleteAll();
       // scheduleRepository.deleteAll();
    }

    @Test
    public void repoDelete() throws Exception{
        var amount = ((TesteeRepository)testeeRepository).findAllByFinishedIs(true).filter(t -> t.getTestGroup().contains("pilot")).count();
        log.info("The amount of the pilot finished is " + amount);
    }
    @Test
    public void polySerialization() throws Exception{
        var mapper = new ObjectMapper();
        String jsonString = "{\"Magnificent\":false,\"Significant\":false,\"Artificial\":true,\"Others\":true}";
        Map<String,Boolean> map = mapper.readValue(jsonString,Map.class);

        if(map.get("Significant")){
            log.info("The conversion works!!");
        }
        else{
            log.info("Indeed works");
        }

    }
    //More test on every entity
}
