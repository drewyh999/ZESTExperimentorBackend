package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import com.zest.zestexperimentorbackend.persists.entities.cacheobjects.AnswerStateCache;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.questions.CodeEvaluation;
import com.zest.zestexperimentorbackend.persists.entities.schedules.EarlyStoppingSchedule;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.entities.schedules.ScheduleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class PilotService extends ExperimentService{

    public PilotService(QuestionService questionService, ScheduleService scheduleService, TesteeService testeeService, CacheService cacheService) {
        super(questionService, scheduleService, testeeService, cacheService);
    }
    //TODO Change experiment use as well
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
            //Continue the answering
            AnswerStateCache answerStateCache = cacheService.getById(session.getId());
            Schedule schedule = scheduleService.getById(answerStateCache.getScheduleId());
            List<ScheduleModule> currentModuleList = schedule.getScheduleModuleList();
            Testee testee = testeeService.getById(answerStateCache.getTesteeId());
            int current_question_index = answerStateCache.getQuestionIndex();
            int current_module_index = answerStateCache.getModuleIndex();
            ScheduleModule current_schedule_module = currentModuleList.get(current_module_index);

            //If the answer list is empty then the user is refreshing the page or comeback from closing a tab
            if(answerList.size() == 0){
                long newExposureTime = current_question_index == 0 ? -1 :Math.max(60000 - current_question_index * 10000L,100);

                var selectedQuestion = questionService.getById(current_schedule_module.getQuestionIdList()
                        .get(current_question_index));

                ((CodeEvaluation)selectedQuestion).setExposureTime(newExposureTime);
                return List.of(selectedQuestion);
            }

            testee.getAnswerList().addAll(answerList);
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
                return getQuestionsByCacheInfo(answerStateCache,0,current_module_index + 1);
            }

            //TODO Shuffle the question id list when entering the next module

            // Return next question and next question index if the experiment is not completed

            //If current module is not finished, continue on current module
            if(current_question_index < current_schedule_module.getQuestionIdList().size() - 1){
                answerStateCache.setQuestionIndex(current_question_index + 1);
                answerStateCache.setModuleIndex(current_module_index);
                cacheService.saveOne(answerStateCache);
                var selectedQuestion = questionService.getById(current_schedule_module.getQuestionIdList()
                        .get(current_question_index + 1));
                //If we are in the Timed question module, we need to decrease the exposure time by 10 seconds till 1 second
                // every time
                if(current_module_index == 0){
                    /*
                    * Starts from 1 minutes and decrease, since in the continuing answering part, the question index
                    * should be more than 0, so as the requirement stated, we should start from  infinite exposure time
                    * and then 1 min of exposure time then decrease 10 seconds each time, so we only need to decrease
                    * 60000ms which is 1 min by the distance between current question index and index 1, */

                    long newExposureTime = Math.max(60000 - current_question_index * 10000L,100);

                    //Set the new exposure time
                    ((CodeEvaluation)selectedQuestion).setExposureTime(newExposureTime);
                }
                return List.of(selectedQuestion);
            }
            //If current module finished, continue on next module
            else if(current_module_index < currentModuleList.size() - 1){
                return getQuestionsByCacheInfo(answerStateCache,0,current_module_index + 1);
            }
            // Return null if the whole experiment is over
            else{
                cacheService.deleteById(session.getId());
                session.invalidate();
                return null;
            }
        }
    }


}
