package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.exceptions.ServiceException;
import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import com.zest.zestexperimentorbackend.cache.AnswerStateCache;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.questions.CodeEvaluation;
import com.zest.zestexperimentorbackend.persists.entities.schedules.EarlyStoppingSchedule;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.entities.schedules.ScheduleModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class PilotService extends ExperimentService {

    private static final Log log = LogFactory.getLog(PilotService.class);


    private static final String STOPPINGSTRINGLITERAL = "Cannot tell";

    private static final String STOPCOUNTKEY = "stop_count";

    public PilotService(QuestionService questionService,
                        ScheduleService scheduleService,
                        TesteeService testeeService,
                        CacheService cacheService,
                        InvitationService invitationService) {
        super(questionService, scheduleService, testeeService, cacheService, invitationService);
    }

    public List<BaseQuestion> runPilot(HttpSession session,
                                       List<Answer> answerList,
                                       String invitationId) throws ServiceException {
        if (session.isNew()) {
            var questionIdList = setUp(session, Schedule.ScheduleType.PILOT, invitationId).get(0);
            //TODO Maybe should use a different answer cache object to store it?
            session.setAttribute(STOPCOUNTKEY, 0);
            var selectedQuestions = questionService.getById(questionIdList);

            if (!(selectedQuestions instanceof CodeEvaluation)) {
                throw new ServiceException("First module of the pilot schedule must " +
                        "be a code evaluation module and they must contains code evaluation questions");
            }

            //Starts with a question that has infinite exposure time
            ((CodeEvaluation) selectedQuestions).setExposureTime(-1);
            return List.of(selectedQuestions);
        } else {
            return this.continueAnswering(session, answerList);
        }
    }


    /**
     * Starts from 1 minutes and decrease, since in the continuing answering part, the question index
     * * should be more than 0, so as the requirement stated, we should start from  infinite exposure time
     * * and then 1 min of exposure time then decrease 10 seconds each time, so we only need to decrease
     * * 60000ms which is 1 min by the distance between current question index and index 1,
     */
    private BaseQuestion setQuestionExposureTimeByQuestionIndex(BaseQuestion selectedQuestion,
                                                                int currentQuestionIndex) {
        long newExposureTime;
        if (currentQuestionIndex != 0)
            newExposureTime = Math.max(60000 - (currentQuestionIndex - 1) * 10000L, 100);
        else
            newExposureTime = -1;
        //Set the new exposure time
        ((CodeEvaluation) (selectedQuestion)).setExposureTime(newExposureTime);

        return selectedQuestion;
    }

    @Override
    public List<BaseQuestion> continueAnswering(HttpSession session, List<Answer> answerList) {
        //Continue the answering
        AnswerStateCache answerStateCache = cacheService.getById(session.getId());
        log.info(" continue incoming participants " + answerStateCache.toString());
        Schedule schedule = scheduleService.getById(answerStateCache.getScheduleId());
        List<ScheduleModule> currentModuleList = schedule.getScheduleModuleList();
        Testee testee = testeeService.getById(answerStateCache.getTesteeId());
        int currentQuestionIndex = answerStateCache.getQuestionIndex();
        int currentModuleIndex = answerStateCache.getModuleIndex();
        ScheduleModule currentScheduleModule = currentModuleList.get(currentModuleIndex);

        //If the answer list is empty then the user is refreshing the page or comeback from closing a tab
        if (answerList.isEmpty()) {
            var selectedQuestion = getQuestionsByCacheInfo(answerStateCache,
                    currentQuestionIndex, currentModuleIndex);
            if (currentScheduleModule.getModuleType() == ScheduleModule.ModuleType.CODE)
                return List.of(setQuestionExposureTimeByQuestionIndex(selectedQuestion.get(0), currentQuestionIndex));
            else if (currentScheduleModule.getModuleType() == ScheduleModule.ModuleType.DEMO)
                return selectedQuestion;
        }

        for (var answer : answerList) {
            testee.getAnswerMap().put(answer.getQuestionID(), answer);
        }
        testeeService.saveOne(testee);

        //If the current answer is null(Cannot tell) then we start to count consecutive、
        int stopCount = -1;
        if (session.getAttribute(STOPCOUNTKEY) != null) {
            stopCount = (int) session.getAttribute(STOPCOUNTKEY);
        }
        //TODO make the stopping string as config
        if (answerList.get(0).getAnswerText().equals(STOPPINGSTRINGLITERAL)) {
            stopCount++;
            session.setAttribute(STOPCOUNTKEY, stopCount);
        } else {
            session.setAttribute(STOPCOUNTKEY, 0);
        }
        if (stopCount >= ((EarlyStoppingSchedule) schedule).getStoppingCount()) {
            session.removeAttribute(STOPCOUNTKEY);
            return getQuestionsByCacheInfo(answerStateCache, 0, currentModuleIndex + 1);
        }


        // Return next question and next question index if the experiment is not completed

        //Check if current module is demographic module if it is, then we need to move on to next module
        if (currentScheduleModule.getModuleType() == ScheduleModule.ModuleType.DEMO) {
            currentModuleIndex++;
            currentQuestionIndex = 0;
        }

        //If current module is not finished, continue on current module
        if (currentQuestionIndex < currentScheduleModule.getQuestionIdList().size() - 1 &&
                currentModuleIndex < currentModuleList.size() - 1 &&
                currentScheduleModule.getModuleType() != ScheduleModule.ModuleType.DEMO) {
            var selectedQuestion = getQuestionsByCacheInfo(answerStateCache,
                    currentQuestionIndex + 1, currentModuleIndex);

            //If we are in the Timed question module(which is the first module), we need to decrease the exposure
            // time by 10 seconds till 1 second
            // every time
            if (currentModuleIndex == 0) {
                selectedQuestion = List.of(setQuestionExposureTimeByQuestionIndex(selectedQuestion.get(0),
                        currentQuestionIndex + 1));
            }
            return selectedQuestion;
        }
        //If current module finished, continue on next module
        else if (currentModuleIndex < currentModuleList.size() - 1) {
            return getQuestionsByCacheInfo(answerStateCache, 0, currentModuleIndex + 1);
        }
        // Return null if the whole experiment is over and mark the testee as the completed one
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
