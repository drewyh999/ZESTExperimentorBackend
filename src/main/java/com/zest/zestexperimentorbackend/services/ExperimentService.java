package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import com.zest.zestexperimentorbackend.cache.AnswerStateCache;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.schedules.ScheduleModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

// TODO implement strategy pattern to adapt different approach used by pilot and experiment

@Service
public class ExperimentService {
    final QuestionService questionService;

    final ScheduleService scheduleService;

    final TesteeService testeeService;

    final CacheService cacheService;

    final InvitationService invitationService;

    static final Log log = LogFactory.getLog(ExperimentService.class);

    public ExperimentService(QuestionService questionService,
                             ScheduleService scheduleService,
                             TesteeService testeeService,
                             CacheService cacheService,
                             InvitationService invitationService) {
        this.questionService = questionService;
        this.scheduleService = scheduleService;
        this.testeeService = testeeService;
        this.cacheService = cacheService;
        this.invitationService = invitationService;
    }

    public List<BaseQuestion> runExperiment(HttpSession session, List<Answer> answerList, String invitationId) {
        // Schedule -> List of Modules in the schedule -> List of question id in the module
        if (session.isNew()) {
            var questionIdList = setUp(session, Schedule.ScheduleType.EXPERIMENT, invitationId);
            log.info("New participant in experiment mode");
            return questionService.getByIdList(questionIdList);
        } else {
            return continueAnswering(session, answerList);
        }
    }

    /**
     * Initialize the testee objects with all the questionIDs and null
     *
     * @param testee            the testee to be setup
     * @param assignedSchedule the schedule that this testee is assigned to
     * @return the testee that has been initialized
     */
    Testee setUpTestee(Testee testee, Schedule assignedSchedule) {
        for (int i = 0; i < assignedSchedule.getScheduleModuleList().size(); i++) {
            for (int j = 0; j < assignedSchedule.getScheduleModuleList().get(i).getQuestionIdList().size(); j++) {
                String initId = assignedSchedule.getScheduleModuleList().get(i).getQuestionIdList().get(j);
                testee.getAnswerMap().put(initId, null);
            }
        }
        return testee;
    }


    /**
     * Get one question or a list of questions base on the question index/ module index given and the current
     * answer state cache. Note that the requested question/questions should not only rely on the requested question
     * index
     * and module index but also on the relative position between the requested question and the current state
     *
     * @param answerStateCache the cache object indicating the current situation
     * @param questionIndex    the question index of the requested question
     * @param moduleIndex      the module index of the requested question
     * @return the requested question/questions
     */
    List<BaseQuestion> getQuestionsByCacheInfo(AnswerStateCache answerStateCache, int questionIndex, int moduleIndex) {
        int currentModuleIndex = answerStateCache.getModuleIndex();
        Schedule schedule = scheduleService.getById(answerStateCache.getScheduleId());

        //If we are moving on to the different module, we should update the current question ID list including
        // shuffling it
        if (moduleIndex != currentModuleIndex) {
            log.info("Participant " + answerStateCache.getTesteeId() + "Entering next module");
            List<String> newModuleQuestionIdList = schedule.getScheduleModuleList().get(
                    moduleIndex).getQuestionIdList();
            Collections.shuffle(newModuleQuestionIdList, new Random(System.currentTimeMillis()));
            var newModuleQuestionList = questionService.getByIdList(newModuleQuestionIdList);
            answerStateCache.setCurrentModuleQuestionList(newModuleQuestionList);
        }

        //Update current question index and the module index
        answerStateCache.setQuestionIndex(questionIndex);
        answerStateCache.setModuleIndex(moduleIndex);

        //Save the cached states
        cacheService.saveOne(answerStateCache);

        //We should return all the question if the current module is a demographic question module
        if (schedule.getScheduleModuleList().get(
                answerStateCache.getModuleIndex()).getModuleType() == ScheduleModule.ModuleType.DEMO) {
            return answerStateCache.getCurrentModuleQuestionList();
        }
        //Or return single item when dealing with other type of module
        else {
            return List.of(answerStateCache.getCurrentModuleQuestionList().get(questionIndex));
        }
    }

    //Set up the first session and return ID of the first question
    List<String> setUp(HttpSession session, Schedule.ScheduleType type, String invitationId) {
        //Assign the new testee to a random test group(each schedule could represent a test group)
        List<Schedule> scheduleList = scheduleService.getByType(type);
        Random random = new Random(session.getCreationTime());
        int groupIndex = 0;
        if (scheduleList.size() != 1) {
            groupIndex = random.nextInt(scheduleList.size() - 1);
        }
        Schedule assignedSchedule = scheduleList.get(groupIndex);

        //create a new testee
        String scheduleTypeString = type.toString().toLowerCase(Locale.ROOT);
        // Get the invitation source of this testee
        var invitation = invitationService.getById(invitationId);
        Testee testee = new Testee(scheduleTypeString + "-" + assignedSchedule.getTestGroup(), invitation.getSource());


        //Initialize the answer map with null
        testee = setUpTestee(testee, assignedSchedule);
        Testee savedTestee = testeeService.saveOne(testee);

        log.info("Incoming new participant: session ID " + session.getId() + "     Testee ID: " + savedTestee.getId());


        //Initialize the module and shuffle the question order so that random access could be achieved
        List<String> moduleQuestionIDList = assignedSchedule.getScheduleModuleList().get(0).getQuestionIdList();
        Collections.shuffle(moduleQuestionIDList, random);
        var moduleQuestionList = questionService.getByIdList(moduleQuestionIDList);

        //Create new cache object to store in redis
        AnswerStateCache answerStateCache = new AnswerStateCache(session.getId(), 0, 0
                , savedTestee.getId(), assignedSchedule.getId(), moduleQuestionList);

        cacheService.saveOne(answerStateCache);

        // If the first assigned schedule module is a code module, return the first question, otherwise return all
        // questions within the module
        var firstModule = assignedSchedule.getScheduleModuleList().get(0);
        if (firstModule.getModuleType() == ScheduleModule.ModuleType.CODE) {
            return List.of(firstModule.getQuestionIdList().get(0));
        } else {
            return firstModule.getQuestionIdList();
        }
    }

    List<BaseQuestion> continueAnswering(HttpSession session, List<Answer> answerList) {
        //Continue the answering
        AnswerStateCache answerStateCache = cacheService.getById(session.getId());
        log.info(" continue incoming participants" + answerStateCache.toString());
        Schedule schedule = scheduleService.getById(answerStateCache.getScheduleId());
        List<ScheduleModule> currentModuleList = schedule.getScheduleModuleList();
        Testee testee = testeeService.getById(answerStateCache.getTesteeId());
        int currentQuestionIndex = answerStateCache.getQuestionIndex();
        int currentModuleIndex = answerStateCache.getModuleIndex();
        ScheduleModule currentScheduleModule = currentModuleList.get(currentModuleIndex);

        //If no answer is provided, then the user is refreshing or coming back after closing the tab in the browser
        if (answerList.isEmpty()) {
            return getQuestionsByCacheInfo(answerStateCache, currentQuestionIndex, currentModuleIndex);
        }

        // Save the current answer and time it takes
        for (var answer : answerList) {
            testee.getAnswerMap().put(answer.getQuestionID(), answer);
        }
        testeeService.saveOne(testee);

        // Return next question and next question index if the experiment is not completed

        //If current module is not finished, continue on current module
        if (currentQuestionIndex < currentScheduleModule.getQuestionIdList().size() - 1 &&
                currentModuleIndex < currentModuleList.size() - 1) {
            return getQuestionsByCacheInfo(answerStateCache, currentQuestionIndex + 1, currentModuleIndex);
        }
        //If current module finished, continue on next module
        else if (currentModuleIndex < currentModuleList.size() - 1) {
            return getQuestionsByCacheInfo(answerStateCache, 0, currentModuleIndex + 1);
        }
        // Return null if the whole experiment is over and mark the testee as the one that finished the test
        else {
            testee.setFinished(true);
            testeeService.saveOne(testee);
            cacheService.deleteById(session.getId());
            session.invalidate();
            log.info("Participant:" + testee.getId() + " had finished the test");
            return null;
        }
    }
}
