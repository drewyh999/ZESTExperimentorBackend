package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.cacheobjects.AnswerStateCache;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PilotService(QuestionService questionService, ScheduleService scheduleService, TesteeService testeeService, CacheService cacheService) {
        super(questionService, scheduleService, testeeService, cacheService);
    }

    public BaseQuestion runPilot(HttpSession session, String ans, Long timeSpent){
        logger.info("Received time spent on the question" + timeSpent);
        if(session.isNew()){
            String question_id = setUp(session, Schedule.ScheduleType.PILOT);
            session.setAttribute("stop_count",0);
            return questionService.getById(question_id);
        }
        else{
            //TODO Exposure time Decreasing
            //Continue the answering
            AnswerStateCache answerStateCache = cacheService.getById(session.getId());
            Schedule schedule = scheduleService.getById(answerStateCache.getScheduleId());
            List<ScheduleModule> current_module_list = schedule.getScheduleModuleList();
            Testee testee = testeeService.getById(answerStateCache.getTesteeId());
            int current_question_index = answerStateCache.getQuestionIndex();
            int current_module_index = answerStateCache.getModuleIndex();
            ScheduleModule current_schedule_module = current_module_list.get(current_module_index);

            // Save the current answer
            String current_question_id = current_schedule_module.getQuestionIdList().get(current_question_index);
            testee.getAnswerMap().put(current_question_id,ans);
            testee.getTimeMap().put(current_question_id,timeSpent);
            testeeService.saveOne(testee);

            //If the current answer is null(Cannot tell) then we start to count consecutive、
            int stop_count = -1;
            if(session.getAttribute("stop_count") != null){
                stop_count = (int)session.getAttribute("stop_count");
            }
            //TODO make the stopping string as config
            if(ans.equals("Cannot tell")){
                stop_count ++;
                session.setAttribute("stop_count",stop_count);
            }
            else{
                session.setAttribute("stop_count",0);
            }
            if(stop_count >= ((EarlyStoppingSchedule)schedule).getStoppingCount()){
                answerStateCache.setQuestionIndex(0);
                answerStateCache.setModuleIndex(current_module_index + 1);
                cacheService.saveOne(answerStateCache);
                session.removeAttribute("stop_count");
                return questionService.getById(schedule.getScheduleModuleList().get(current_module_index + 1)
                        .getQuestionIdList().get(0));
            }


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
}
