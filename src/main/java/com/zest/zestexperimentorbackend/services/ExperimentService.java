package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import com.zest.zestexperimentorbackend.persists.entities.cacheobjects.AnswerStateCache;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.schedules.ScheduleModule;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
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

    public List<BaseQuestion> runExperiment(HttpSession session, List<Answer> answerList){
        // Schedule -> List of Modules in the schedule -> List of question id in the module
        if(session.isNew()){
            String question_id = setUp(session, Schedule.ScheduleType.EXPERIMENT);
            return List.of(questionService.getById(question_id));
        }
        else{
            return continueAnswering(session,answerList);
        }
    }

    /**
     * Initialize the testee objects with all the questionIDs and null
     *
     * @param testee the testee to be setup
     * @param assigned_schedule the schedule that this testee is assigned to
     * @return the testee that has been initialized
     */
    Testee setUpTestee(Testee testee, Schedule assigned_schedule){
        for(int i = 0;i < assigned_schedule.getScheduleModuleList().size();i ++){
            for(int j = 0; j < assigned_schedule.getScheduleModuleList().get(i).getQuestionIdList().size(); j ++){
                String init_id = assigned_schedule.getScheduleModuleList().get(i).getQuestionIdList().get(j);
                testee.getAnswerMap().put(init_id,null);
            }
        }
        return testee;
    }


    /**
     * Get one question or a list of questions base on the question index/ module index given and the current
     * answer state cache. Note that the requested question/questions should not only rely on the requested question index
     * and module index but also on the relative position between the requested question and the current state
     * @param answerStateCache the cache object indicating the current situation
     * @param questionIndex the question index of the requested question
     * @param moduleIndex the module index of the requested question
     * @return the requested question/questions
     */
    List<BaseQuestion> getQuestionsByCacheInfo(AnswerStateCache answerStateCache, int questionIndex, int moduleIndex){
        int currentModuleIndex = answerStateCache.getModuleIndex();
        Schedule schedule = scheduleService.getById(answerStateCache.getScheduleId());

        //If we are moving on to the different module, we should update the current question ID list including shuffle it
        if(moduleIndex != currentModuleIndex){
            List<String> newModuleQuestionIdList = schedule.getScheduleModuleList().get(moduleIndex).getQuestionIdList();
            Collections.shuffle(newModuleQuestionIdList,new Random(System.currentTimeMillis()));
            answerStateCache.setCurrentModuleQuestionIDList(newModuleQuestionIdList);
        }

        //Update current question index and the module index
        answerStateCache.setQuestionIndex(questionIndex);
        answerStateCache.setModuleIndex(moduleIndex);

        //Save the cached states
        cacheService.saveOne(answerStateCache);

        //We should return all the question if the current module is a demographic question module
        if(schedule.getScheduleModuleList().get(answerStateCache.getModuleIndex()).getModuleType() == ScheduleModule.ModuleType.DEMO){
            return questionService.getByIdList(answerStateCache.getCurrentModuleQuestionIDList());
        }
        //Or return single item when dealing with other type of module
        else{
            return List.of(questionService.getById(answerStateCache.getCurrentModuleQuestionIDList().get(questionIndex)));
        }
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
        String ScheduleTypeString = type.toString().toLowerCase(Locale.ROOT);
        Testee testee = new Testee(ScheduleTypeString +"-"+ assigned_schedule.getTestGroup());

        //Initialize the answer map with null
        testee = setUpTestee(testee,assigned_schedule);
        Testee saved_testee = testeeService.saveOne(testee);

        //Initialize the module and shuffle the question order so that random access could be achieved
        List<String> ModuleQuestionIDList = assigned_schedule.getScheduleModuleList().get(0).getQuestionIdList();
        Collections.shuffle(ModuleQuestionIDList,random);

        //Create new cache object to store in redis
        AnswerStateCache answerStateCache = new AnswerStateCache(session.getId(),0,0
                ,saved_testee.getId(), assigned_schedule.getId(), ModuleQuestionIDList);

        cacheService.saveOne(answerStateCache);

        return assigned_schedule.getScheduleModuleList().get(0).getQuestionIdList().get(0);
    }

    List<BaseQuestion> continueAnswering(HttpSession session, List<Answer> answerList){
        //Continue the answering
        AnswerStateCache answerStateCache = cacheService.getById(session.getId());
        Schedule schedule = scheduleService.getById(answerStateCache.getScheduleId());
        List<ScheduleModule> current_module_list = schedule.getScheduleModuleList();
        Testee testee = testeeService.getById(answerStateCache.getTesteeId());
        int current_question_index = answerStateCache.getQuestionIndex();
        int current_module_index = answerStateCache.getModuleIndex();
        ScheduleModule current_schedule_module = current_module_list.get(current_module_index);

        //If no answer is provided, then the user is refreshing or coming back after closing the tab in the browser
        if(answerList.size() == 0){
            return getQuestionsByCacheInfo(answerStateCache,current_question_index,current_module_index);
        }

        // Save the current answer and time it takes
        for(var answer : answerList){
            testee.getAnswerMap().put(answer.getQuestionID(),answer);
        }
        testeeService.saveOne(testee);

        // Return next question and next question index if the experiment is not completed

        //If current module is not finished, continue on current module
        if(current_question_index < current_schedule_module.getQuestionIdList().size() - 1){
            return getQuestionsByCacheInfo(answerStateCache,current_question_index + 1,current_module_index);
        }
        //If current module finished, continue on next module
        else if(current_module_index < current_module_list.size() - 1){
            return getQuestionsByCacheInfo(answerStateCache,0,current_module_index + 1);
        }
        // Return null if the whole experiment is over and mark the testee as the one that finished the test
        else{
            testee.setFinished(true);
            testeeService.saveOne(testee);
            cacheService.deleteById(session.getId());
            session.invalidate();
            return null;
        }
    }
}
