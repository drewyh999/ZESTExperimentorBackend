package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import com.zest.zestexperimentorbackend.persists.entities.cacheobjects.AnswerStateCache;
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
public class PilotService extends ExperimentService{

    private static final Log log = LogFactory.getLog(PilotService.class);

    public PilotService(QuestionService questionService, ScheduleService scheduleService, TesteeService testeeService, CacheService cacheService) {
        super(questionService, scheduleService, testeeService, cacheService);
    }

    public List<BaseQuestion> runPilot(HttpSession session, List<Answer> answerList){
        if(session.isNew()){
            String question_id = setUp(session, Schedule.ScheduleType.PILOT);
            session.setAttribute("stop_count",0);
            var selectedQuestion =  questionService.getById(question_id);
            //Starts with a question that has infinite exposure time
            ((CodeEvaluation)selectedQuestion).setExposureTime(-1);
            return List.of(selectedQuestion);
        }
        else{
            return this.continueAnswering(session,answerList);
        }
    }


    /**
     * Starts from 1 minutes and decrease, since in the continuing answering part, the question index
     *          * should be more than 0, so as the requirement stated, we should start from  infinite exposure time
     *          * and then 1 min of exposure time then decrease 10 seconds each time, so we only need to decrease
     *          * 60000ms which is 1 min by the distance between current question index and index 1,
     */
    private BaseQuestion setQuestionExposureTimeByQuestionIndex(BaseQuestion selectedQuestion, int currentQuestionIndex){
        long newExposureTime;
        if(currentQuestionIndex != 0)
            newExposureTime = Math.max(60000 - (currentQuestionIndex - 1) * 10000L,100);
        else
            newExposureTime = -1;
        //Set the new exposure time
        ((CodeEvaluation)(selectedQuestion)).setExposureTime(newExposureTime);

        return selectedQuestion;
    }

    public List<BaseQuestion> continueAnswering(HttpSession session, List<Answer> answerList){
        //Continue the answering
        AnswerStateCache answerStateCache = cacheService.getById(session.getId());
        log.info("current cache info" + answerStateCache.toString());
        Schedule schedule = scheduleService.getById(answerStateCache.getScheduleId());
        List<ScheduleModule> currentModuleList = schedule.getScheduleModuleList();
        Testee testee = testeeService.getById(answerStateCache.getTesteeId());
        int currentQuestionIndex = answerStateCache.getQuestionIndex();
        int currentModuleIndex = answerStateCache.getModuleIndex();
        ScheduleModule current_schedule_module = currentModuleList.get(currentModuleIndex);

        //If the answer list is empty then the user is refreshing the page or comeback from closing a tab
        if(answerList.size() == 0){
            var selectedQuestion =getQuestionsByCacheInfo(answerStateCache,
                    currentQuestionIndex,currentModuleIndex);
            if(current_schedule_module.getModuleType() == ScheduleModule.ModuleType.CODE)
                return List.of(setQuestionExposureTimeByQuestionIndex(selectedQuestion.get(0),currentQuestionIndex));
            else if(current_schedule_module.getModuleType() == ScheduleModule.ModuleType.DEMO)
                return selectedQuestion;
        }

        for(var answer : answerList){
            testee.getAnswerMap().put(answer.getQuestionID(),answer);
        }
        testeeService.saveOne(testee);

        //If the current answer is null(Cannot tell) then we start to count consecutive、
        int stop_count = -1;
        if(session.getAttribute("stop_count") != null){
            stop_count = (int)session.getAttribute("stop_count");
        }
        //TODO make the stopping string as config
        if(answerList.get(0).getAnswerText().equals("Cannot tell")){
            stop_count ++;
            session.setAttribute("stop_count",stop_count);
        }
        else{
            session.setAttribute("stop_count",0);
        }
        if(stop_count >= ((EarlyStoppingSchedule)schedule).getStoppingCount()){
            session.removeAttribute("stop_count");
            return getQuestionsByCacheInfo(answerStateCache,0,currentModuleIndex + 1);
        }


        // Return next question and next question index if the experiment is not completed

        //Check if current module is demographic module if it is, then we need to move on to next module
        if(current_schedule_module.getModuleType() == ScheduleModule.ModuleType.DEMO){
            currentModuleIndex ++;
            currentQuestionIndex = 0;
        }

        //If current module is not finished, continue on current module
        if(currentQuestionIndex < current_schedule_module.getQuestionIdList().size() - 1 &&
                currentModuleIndex < currentModuleList.size() - 1){
            var selectedQuestion = getQuestionsByCacheInfo(answerStateCache,
                    currentQuestionIndex + 1,currentModuleIndex);

            //If we are in the Timed question module(which is the first module), we need to decrease the exposure time by 10 seconds till 1 second
            // every time
            if(currentModuleIndex == 0){
                selectedQuestion = List.of(setQuestionExposureTimeByQuestionIndex(selectedQuestion.get(0),
                        currentQuestionIndex + 1));
            }
            return selectedQuestion;
        }
        //If current module finished, continue on next module
        else if(currentModuleIndex < currentModuleList.size() - 1){
            return getQuestionsByCacheInfo(answerStateCache,0,currentModuleIndex + 1);
        }
        // Return null if the whole experiment is over and mark the testee as the completed one
        else{
            testee.setFinished(true);
            testeeService.saveOne(testee);
            cacheService.deleteById(session.getId());
            session.invalidate();
            return null;
        }
    }

}
