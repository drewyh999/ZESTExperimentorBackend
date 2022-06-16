package com.zest.zestexperimentorbackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.zestexperimentorbackend.exceptions.BaseNotFoundExeption;
import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.entities.schedules.Schedule;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class CSVService {
    QuestionService questionService;

    TesteeService testeeService;

    ScheduleService scheduleService;


    public CSVService(QuestionService questionService, TesteeService testeeService, ScheduleService scheduleService) {
        this.questionService = questionService;
        this.testeeService = testeeService;
        this.scheduleService = scheduleService;
    }

    //Returns the path to the csv file on the server for download
    public void exportCSV(HttpServletResponse servletResponse, String mode) throws IOException, BaseNotFoundExeption {
        servletResponse.setContentType("text/csv");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String localtimestring = dateTimeFormatter.format(dateTime);

        //Deal with file name and content type
        servletResponse.addHeader("Content-Disposition","attachment; filename=" + mode + "-result-export"
                +localtimestring + ".csv");

        //If nobody had taken the test yet. we return 404
        if(!testeeService.ifAnyParticipants(mode)){
            servletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //Create list of all questions which will be used as headers of the csv file
        List<String> questionIdList = new ArrayList<>();
        //Find the  corresponding schedules that
        scheduleService.getByType(Schedule.ScheduleType.valueOf(mode)).get(0).getScheduleModuleList()
                .forEach(scheduleModule -> questionIdList.addAll(scheduleModule.getQuestionIdList()));
        List<BaseQuestion> questionList = questionService.getByIdList(questionIdList);

        //Get alias of the problem and use them as the csv file header
        List<String> csvHeaderList = new ArrayList<>();
        csvHeaderList.add("id");
        csvHeaderList.add("TestGroup");
        for(var question: questionList){

            //For multiple choice questions, we should get all choices of the questions as the header
            if(question.getQuestionChoiceType() == BaseQuestion.QuestionChoiceType.MULTI_CHOICE){
                for(var choice :question.getQuestionChoices()){
                    csvHeaderList.add(question.getAlias() +"_"+ choice);
                }
            }
            else{
                csvHeaderList.add(question.getAlias());
            }
            csvHeaderList.add(question.getAlias() + "_time");
        }

        CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(), CSVFormat.DEFAULT);

        //Print headers
        csvPrinter.printRecord(csvHeaderList);

        //Write all results to the output file
        Stream<Testee> testeeStream = testeeService.getByTestGroupContains(mode);
        testeeStream.forEach(testee -> {
                    List<String> record = new ArrayList<>();
                    record.add(testee.getId());
                    record.add(testee.getTestGroup());
                    var answerMap = testee.getAnswerMap();
                    for (var question : questionList) {
                        var answer = answerMap.get(question.getId());

                        if(answer != null) {
                            if(question.getQuestionChoiceType() != BaseQuestion.QuestionChoiceType.MULTI_CHOICE){
                                record.add(answer.getAnswerText());
                            }
                            else{
                                var mapper = new ObjectMapper();
                                try {
                                    Map<String, Boolean> choiceMap = mapper.readValue(answer.getAnswerText(),Map.class);
                                    question.getQuestionChoices().forEach((choice) -> {
                                        boolean b = (choiceMap.get(choice)) ? record.add("1") : record.add("0");
                                    });
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }

                            }
                            var timeSpend = answerMap.get(question.getId()).getTimeSpent();
                            //Only print those question with time requirement
                            if (timeSpend != null) {
                                record.add(timeSpend.toString());
                            }
                        }
                        else{
                            record.add("");
                            record.add("");
                        }
                    }
            try {
                csvPrinter.printRecord(record);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
