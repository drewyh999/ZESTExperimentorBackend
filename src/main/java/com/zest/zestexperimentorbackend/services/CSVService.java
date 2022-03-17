package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.exceptions.BaseNotFoundExeption;
import com.zest.zestexperimentorbackend.persists.entities.Testee;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CSVService {
    QuestionService questionService;

    TesteeService testeeService;

    public CSVService(QuestionService questionService, TesteeService testeeService) {
        this.questionService = questionService;
        this.testeeService = testeeService;
    }

    //Returns the path to the csv file on the server for download
    public void exportCSV(HttpServletResponse servletResponse, String mode) throws IOException, BaseNotFoundExeption {
        servletResponse.setContentType("text/csv");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String localtimestring = dateTimeFormatter.format(dateTime);

        servletResponse.addHeader("Content-Disposition","attachment; filename=" + "experiment-result-export"
                +localtimestring + ".csv");
        List<Testee> testeeList = testeeService.findByTestGroupContains(mode);
        List<BaseQuestion> questionList = questionService.getByIdList(testeeList.get(0).getAnswerMap().keySet());

        //Get alias of the problem
        List<String> alias_list = new ArrayList<>();
        alias_list.add("id");
        alias_list.add("TestGroup");
        for(var question: questionList){
            alias_list.add(question.getAlias());
            alias_list.add(question.getAlias() + "_time");
        }

        CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(), CSVFormat.DEFAULT);
        //Print headers
        csvPrinter.printRecord(alias_list);
        //TODO Get answer map printed as it should be
        //TODO Export reaction time to the csv file
        for(Testee testee: testeeList){
            List<String> record = new ArrayList<>();
            record.add(testee.getId());
            record.add(testee.getTestGroup());
            for(var entry: testee.getAnswerMap().entrySet()){
                record.add(entry.getValue());
                record.add(testee.getTimeMap().get(entry.getKey()).toString());
            }
            csvPrinter.printRecord(record);
        }
    }
}
