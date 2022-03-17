package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.cacheobjects.AnswerStateCache;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.schedules.ScheduleModule;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
public class ExperimentService {
    final QuestionService questionService;

    final ScheduleService scheduleService;

    final TesteeService testeeService;

    final CacheService cacheService;

    public ExperimentService(QuestionService questionService, ScheduleService scheduleService, TesteeService testeeService, CacheService cacheService) {
        this.questionService = questionService;
        this.scheduleService = scheduleService;
        this.testeeService = testeeService;
        this.cacheService = cacheService;
    }

    public BaseQuestion runExperiment(HttpSession session, String ans,Long time){
        // Schedule -> List of Modules in the schedule -> List of question id in the module
        if(session.isNew()){
            String question_id = setUp(session, Schedule.ScheduleType.EXPERIMENT);
            return questionService.getById(question_id);
        }
        else{
            return continueAnswering(session,ans,time);
        }
    }

    Testee setUpTestee(Testee testee, Schedule assigned_schedule){
        for(int i = 0;i < assigned_schedule.getScheduleModuleList().size();i ++){
            for(int j = 0;j < assigned_schedule.getScheduleModuleList().get(i).getQuestionIdList().size();j ++){
                String init_id = assigned_schedule.getScheduleModuleList().get(i).getQuestionIdList().get(j);
                testee.getAnswerMap().put(init_id,null);
                testee.getTimeMap().put(init_id,null);
            }
        }
        return testee;
    }

    //Set up the first session and return ID of the first question
    String setUp(HttpSession session, Schedule.ScheduleType type){
        //Assign the new testee to a random test group(each schedule could represent a test group)
        List<Schedule> schedule_list = scheduleService.getByType(type);
        Random random = new Random(session.getCreationTime());
        int group_index = 0;
        if(schedule_list.size() != 1) {
            group_index = random.nextInt(schedule_list.size() - 1);
        }
        Schedule assigned_schedule = schedule_list.get(group_index);

        //create a new testee
        String type_string = type.toString().toLowerCase(Locale.ROOT);
        Testee testee = new Testee(type_string +"-"+ assigned_schedule.getTestGroup());

        //Initialize the answer map with null
        testee = setUpTestee(testee,assigned_schedule);
        Testee saved_testee = testeeService.saveOne(testee);

        //Create new cache object to store in redis
        AnswerStateCache answerStateCache = new AnswerStateCache(session.getId(),0,0
                ,saved_testee.getId(),assigned_schedule.getId());

        cacheService.saveOne(answerStateCache);

        return assigned_schedule.getScheduleModuleList().get(0).getQuestionIdList().get(0);
    }

    BaseQuestion continueAnswering(HttpSession session, String ans,Long timeSpent){
        //Continue the answering
        AnswerStateCache answerStateCache = cacheService.getById(session.getId());
        Schedule schedule = scheduleService.getById(answerStateCache.getScheduleId());
        List<ScheduleModule> current_module_list = schedule.getScheduleModuleList();
        Testee testee = testeeService.getById(answerStateCache.getTesteeId());
        int current_question_index = answerStateCache.getQuestionIndex();
        int current_module_index = answerStateCache.getModuleIndex();
        ScheduleModule current_schedule_module = current_module_list.get(current_module_index);

        // Save the current answer and time it takes
        String current_question_id = current_schedule_module.getQuestionIdList().get(current_question_index);
        testee.getAnswerMap().put(current_question_id,ans);
        testee.getTimeMap().put(current_question_id,timeSpent);
        testeeService.saveOne(testee);

        // Return next question and next question index if the experiment is not completed

        //If current module is not finished, continue on current module
        if(current_question_index < current_schedule_module.getQuestionIdList().size() - 1){
            answerStateCache.setQuestionIndex(current_question_index + 1);
            answerStateCache.setModuleIndex(current_module_index);
            cacheService.saveOne(answerStateCache);
            return questionService.getById(current_schedule_module.getQuestionIdList()
                    .get(current_question_index + 1));
        }
        //If current module finished, continue on next module
        else if(current_module_index < current_module_list.size() - 1){
            answerStateCache.setQuestionIndex(0);
            answerStateCache.setModuleIndex(current_module_index + 1);
            cacheService.saveOne(answerStateCache);
            return questionService.getById(schedule.getScheduleModuleList().get(current_module_index + 1)
                    .getQuestionIdList().get(0));
        }
        // Return null if the whole experiment is over
        else{
            cacheService.deleteById(session.getId());
            session.invalidate();
            return null;
        }
    }
}
