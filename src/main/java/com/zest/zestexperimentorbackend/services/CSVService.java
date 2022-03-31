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

        //Deal with file name and content type
        servletResponse.addHeader("Content-Disposition","attachment; filename=" + "experiment-result-export"
                +localtimestring + ".csv");
        List<Testee> testeeList = testeeService.findByTestGroupContains(mode);

        //Create list of all questions which will be used as headers of the csv file
        List<String> questionIdList = new ArrayList<>();
        testeeList.get(0).getAnswerList().forEach(a -> questionIdList.add(a.getQuestionID()));
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
        for(Testee testee: testeeList){
            List<String> record = new ArrayList<>();
            record.add(testee.getId());
            record.add(testee.getTestGroup());
            for(var entry: testee.getAnswerList()){
                record.add(entry.getAnswerText());
                //Only print those question with time requirement
                if(entry.getTimeSpent() != null)
                    record.add(entry.getTimeSpent().toString());
            }
            csvPrinter.printRecord(record);
        }
    }
}
