package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.schedules.EarlyStoppingSchedule;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.entities.schedules.ScheduleModule;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class PilotService extends ExperimentService{

    public PilotService(QuestionService questionService, ScheduleService scheduleService, TesteeService testeeService) {
        super(questionService, scheduleService, testeeService);
    }

    public BaseQuestion runPilot(HttpSession session, String ans){
        if(session.isNew()){
            String question_id = setUp(session, Schedule.ScheduleType.PILOT);
            session.setAttribute("stop_count",0);
            return questionService.findById(question_id);
        }
        else{
            //Continue the answering
            Schedule schedule = scheduleService.findById((String)session.getAttribute("schedule_id"));
            List<ScheduleModule> current_module_list = schedule.getScheduleModuleList();
            Testee testee = testeeService.findById((String)session.getAttribute("testee_id"));
            int current_question_index = (int)session.getAttribute("question_index");
            int current_module_index = (int)(session.getAttribute("module_index"));
            ScheduleModule current_schedule_module = current_module_list.get(current_module_index);

            // Save the current answer
            //If the current answer is null(Cannot tell) then we start to count consecutive、

            String current_question_id = current_schedule_module.getQuestionIdList().get(current_question_index);
            testee.getAnswerMap().put(current_question_id,ans);
            testeeService.saveOne(testee);
            int stop_count = -1;
            if(session.getAttribute("stop_count") != null){
                stop_count = (int)session.getAttribute("stop_count");
            }
            if(ans.equals("Cannot tell")){
                stop_count ++;
                session.setAttribute("stop_count",stop_count);
            }
            else{
                session.setAttribute("stop_count",0);
            }
            if(stop_count >= ((EarlyStoppingSchedule)schedule).getStoppingCount()){
                session.setAttribute("question_index",0);
                session.setAttribute("module_index",current_module_index + 1);
                session.removeAttribute("stop_count");
                return questionService.findById(schedule.getScheduleModuleList().get(current_module_index + 1)
                        .getQuestionIdList().get(0));
            }


            // Return next question and next question index if the experiment is not completed

            //If current module is not finished, continue on current module
            if(current_question_index < current_schedule_module.getQuestionIdList().size() - 1){
                session.setAttribute("question_index",current_question_index + 1);
                session.setAttribute("module_index",current_module_index);
                return questionService.findById(current_schedule_module.getQuestionIdList()
                        .get(current_question_index + 1));
            }
            //If current module finished, continue on next module
            else if(current_module_index < current_module_list.size() - 1){
                session.setAttribute("question_index",0);
                session.setAttribute("module_index",current_module_index + 1);
                return questionService.findById(schedule.getScheduleModuleList().get(current_module_index + 1)
                        .getQuestionIdList().get(0));
            }
            // Return null if the whole experiment is over
            else{
                session.invalidate();
                return null;
            }
        }
    }
}
