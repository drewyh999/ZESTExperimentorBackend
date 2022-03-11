package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.controllers.QuestionController;
import com.zest.zestexperimentorbackend.persists.entities.Questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.Schedules.Schedule;
import com.zest.zestexperimentorbackend.persists.entities.Testee;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;

@Service
public class ExperimentService {
    private final QuestionService questionService;

    private final ScheduleService scheduleService;

    private final TesteeService testeeService;

    private static final Log log = LogFactory.getLog(QuestionController.class);

    public ExperimentService(QuestionService questionService, ScheduleService scheduleService,TesteeService testeeService) {
        this.questionService = questionService;
        this.scheduleService = scheduleService;
        this.testeeService = testeeService;
    }

    public BaseQuestion runExperiment(HttpSession session,String ans){
        if(session.isNew()){
            //Assign the new testee to a random test group
            List<Schedule> schedule_list = scheduleService.getAll();
            Random random = new Random(session.getCreationTime());
            int group_index = random.nextInt(schedule_list.size() - 1);
            Schedule assigned_schedule = schedule_list.get(group_index);
            session.setAttribute("schedule_id",assigned_schedule.getId());
            session.setAttribute("question_index",0);

            //create a new testee
            Testee testee = new Testee(assigned_schedule.getTestGroup());
            Testee saved_testee = testeeService.saveOne(testee);
            session.setAttribute("testee_id",saved_testee.getId());

            return questionService.findById(assigned_schedule.getQuestionIdList().get(0));

        }
        else{
            //Continue the answering
            Schedule schedule = scheduleService.findById((String)session.getAttribute("schedule_id"));
            Testee testee = testeeService.findById((String)session.getAttribute("testee_id"));
            int current_question_index = (int)session.getAttribute("question_index");

            //Save the current answer
            testee.getAnswerMap().put(schedule.getQuestionIdList().get(current_question_index),ans);
            testeeService.saveOne(testee);

            //Return next question and next question index if the experiment is not completed
            if(current_question_index + 1 < schedule.getQuestionIdList().size()) {
                session.setAttribute("question_index", current_question_index + 1);
                return questionService.findById(schedule.getQuestionIdList().get(current_question_index + 1));
            }
            //Return null if the answer is over
            else{
                session.invalidate();
                return null;
            }
        }
    }
}
