package com.alanmrace.jimzmlparser;

import com.alanmrace.jimzmlparser.exceptions.Issue;
import com.alanmrace.jimzmlparser.parser.ParserListener;

import java.util.ArrayList;
import java.util.List;

public class ParserIssueHandler implements ParserListener {
    List<Issue> issueList = new ArrayList<Issue>();

    @Override
    public void issueFound(Issue exception) {
        System.out.println(exception);

        issueList.add(exception);
    }

    public List<Issue> getIssueList() {
        return issueList;
    }
}
