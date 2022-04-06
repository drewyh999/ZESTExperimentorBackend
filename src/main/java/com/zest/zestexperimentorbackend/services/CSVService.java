package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.exceptions.BaseNotFoundExeption;
import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
        servletResponse.addHeader("Content-Disposition","attachment; filename=" + "experiment-result-export"
                +localtimestring + ".csv");

        //If nobody had taken the test yet. we return 404
        if(!testeeService.ifAnyParticipants(mode)){
            servletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //Create list of all questions which will be used as headers of the csv file
        List<String> questionIdList = new ArrayList<>();
        scheduleService.getAll().get(0).getScheduleModuleList().forEach(scheduleModule -> questionIdList.addAll(scheduleModule.getQuestionIdList()));
        List<BaseQuestion> questionList = questionService.getByIdList(questionIdList);

        //Get alias of the problem and use them as the csv file header
        List<String> csvHeaderList = new ArrayList<>();
        csvHeaderList.add("id");
        csvHeaderList.add("TestGroup");
        for(var question: questionList){
            csvHeaderList.add(question.getAlias());
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
                    for (var entry : testee.getAnswerMap().entrySet()) {
                        if(entry.getValue() != null) {
                            record.add(entry.getValue().getAnswerText());
                            //Only print those question with time requirement
                            if (entry.getValue().getTimeSpent() != null)
                                record.add(entry.getValue().getTimeSpent().toString());
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
