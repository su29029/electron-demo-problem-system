package com.su29029.server.service;

import java.util.*;

import javax.servlet.http.HttpSession;

import com.su29029.server.bean.Message;
import com.su29029.server.bean.OtherProblem;
import com.su29029.server.bean.SelectProblem;
import com.su29029.server.bean.User;
import com.su29029.server.bean.UserSelectedProblemInformation;
import com.su29029.server.dao.ProblemDao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
@Repository
public class ProblemService {
    @Autowired
    private ProblemDao problemDao;
    private List<Integer> nextProblemId;
    private List<Integer> previousProblemId;
    private HashMap<Integer,String> problemAnswer;
    private String currentUser;
    private String language;

    public Message prepareProblem(UserSelectedProblemInformation userSelectedProblemInformation){
        nextProblemId = new ArrayList<Integer>();
        previousProblemId = new ArrayList<Integer>();
        problemAnswer = new HashMap<Integer, String>();
        currentUser = userSelectedProblemInformation.getUser().getUsername();
        language = userSelectedProblemInformation.getLanguage();
        if (!language.equals("C")) return new Message("Not Support", 400);
        System.out.println("user:" + currentUser + " is ready.");
        for(int i = 0; i < userSelectedProblemInformation.getProblemNumber();){
            int id = new Random().nextInt(20);
            if (nextProblemId.indexOf(id) == -1 && id != 0){
                nextProblemId.add(id);
                i++;
            }
        }
        System.out.println(nextProblemId.toString());
        return new Message("OK", 200);
    }

    public Object getProblemById(int flag){
        if (flag == 1){
            previousProblemId.add(nextProblemId.get(nextProblemId.size() - 1));
            nextProblemId.remove(nextProblemId.size() - 1);
        } else {
            nextProblemId.add(previousProblemId.get(previousProblemId.size() - 1));
            previousProblemId.remove(previousProblemId.size() - 1);
        }
        int current = previousProblemId.get(previousProblemId.size() - 1);
        System.out.println("current:" + current);
        String chart = "problem_language_" + language;
        
        String problemType = problemDao.checkProblemType(chart,current);
        
        if (problemType.equals("select") || problemType.equals("multi")){
            SelectProblem problem = new SelectProblem();
            problem = problemDao.getSelectProblemById(chart, current);
            problemAnswer.put(current, problem.getAnswer());
            problem.setAnswer("");
            return problem;
        } else {
            OtherProblem problem = new OtherProblem();
            problem = problemDao.getOtherProblemById(chart, current);
            problemAnswer.put(current, problem.getAnswer());
            problem.setAnswer("");
            return problem;
        }
    }

    public User submit(List<String> answer, HttpSession session){
        // 将答案反向，随后根据previousProblemId数组从开始到末尾的id值对应的在problemAnswer中正确答案与获得到的答案进行对比。
        
        int correctNumber = 0;
        System.out.println(answer.toString());
        for(int i = 0; i < answer.size(); i++){
            if (answer.get(i) == null) continue;
            if (answer.get(i).equals(problemAnswer.get(previousProblemId.get(i)))){
                correctNumber++;
            }
        }
        problemDao.updateUserScore(correctNumber * previousProblemId.size(), currentUser);
        return new User(null,null,correctNumber);
    }
}
