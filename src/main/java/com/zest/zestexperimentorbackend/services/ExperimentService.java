package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.controllers.QuestionController;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.schedules.ScheduleModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;

@Service
public class ExperimentService {
    final QuestionService questionService;

    final ScheduleService scheduleService;

    final TesteeService testeeService;

    public ExperimentService(QuestionService questionService, ScheduleService scheduleService,TesteeService testeeService) {
        this.questionService = questionService;
        this.scheduleService = scheduleService;
        this.testeeService = testeeService;
    }

    public BaseQuestion runExperiment(HttpSession session,String ans){
        // Schedule -> List of Modules in the schedule -> List of question id in the module
        if(session.isNew()){
            String question_id = setUp(session, Schedule.ScheduleType.EXPERIMENT);
            return questionService.findById(question_id);
        }
        else{
            return continueAnswering(session,ans);
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

        //Initialize session information for continue answering
        Schedule assigned_schedule = schedule_list.get(group_index);
        session.setAttribute("schedule_id",assigned_schedule.getId());
        session.setAttribute("question_index",0);
        session.setAttribute("module_index",0);

        //create a new testee
        Testee testee = new Testee(assigned_schedule.getTestGroup());
        Testee saved_testee = testeeService.saveOne(testee);
        session.setAttribute("testee_id",saved_testee.getId());

        return assigned_schedule.getScheduleModuleList().get(0).getQuestionIdList().get(0);
    }

    BaseQuestion continueAnswering(HttpSession session, String ans){
        //Continue the answering
        Schedule schedule = scheduleService.findById((String)session.getAttribute("schedule_id"));
        List<ScheduleModule> current_module_list = schedule.getScheduleModuleList();
        Testee testee = testeeService.findById((String)session.getAttribute("testee_id"));
        int current_question_index = (int)session.getAttribute("question_index");
        int current_module_index = (int)(session.getAttribute("module_index"));
        ScheduleModule current_schedule_module = current_module_list.get(current_module_index);

        // Save the current answer
        String current_question_id = current_schedule_module.getQuestionIdList().get(current_question_index);
        testee.getAnswerMap().put(current_question_id,ans);
        testeeService.saveOne(testee);

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
