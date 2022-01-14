package com.students.community.controller;

import com.fasterxml.jackson.databind.util.EnumResolver;
import com.students.community.service.Aservice;
import com.students.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AController {
    @Autowired
    private Aservice aservice;
    @RequestMapping("/hello")
    @ResponseBody
    public String say(){
        return "Hello";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return aservice.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        //获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration=request.getHeaderNames();
        while(enumeration.hasMoreElements()){
            String name=enumeration.nextElement();
            String value=request.getHeader(name);
            System.out.println(name+":"+value);
        }
        System.out.println(request.getParameter("code"));

        //返回相应数据
        response.setContentType("text/html;charset=utf-8");
        try (PrintWriter writer=response.getWriter();){
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // GET请求

    // student?current=1&limit=20

    @RequestMapping(path="/students",method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name="current",required = false,defaultValue = "1") int current,
            @RequestParam(name="limit",required = false,defaultValue = "10")int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some Students";
    }


    // student/123
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id")int id){
        System.out.println(id);
        return "a Student";
    }


    //POST请求
    @RequestMapping(path="/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,String age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //响应html数据
    @RequestMapping(path="/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mav=new ModelAndView();
        mav.addObject("name","张三");
        mav.addObject("age","30");
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path="/school",method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","武大");
        model.addAttribute("age","80");
        return "/demo/view";
    }

    //响应JSON数据（异步请求）
    //Java对象 -> JSON字符串 -> JS对象
    @RequestMapping(path="/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object>emp=new HashMap<>();
        emp.put("name","张三");
        emp.put("age","23");
        emp.put("salary","8000");
        return emp;
    }

    @RequestMapping(path="/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        List<Map<String,Object>>emps=new ArrayList<>();
        Map<String,Object>emp=new HashMap<>();
        emp.put("name","张三");
        emp.put("age","23");
        emp.put("salary","8000");
        emps.add(emp);

        emp=new HashMap<>();
        emp.put("name","李四");
        emp.put("age","24");
        emp.put("salary","2000");
        emps.add(emp);
        return emps;
    }

    // cookie示例
    @RequestMapping(path = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        // 创建cookie
        Cookie cookie=new Cookie("code", CommunityUtil.generateUUID());
        // 设置cookie生效范围
        cookie.setPath("/community/alpha");
        // 设置cookie生存时间
        cookie.setMaxAge(60*10);
        // 发送cookie
        response.addCookie(cookie);

        return "setCookie";
    }

    @RequestMapping(path = "/cookie/get",method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code){
        System.out.println(code);
        return "get cookie";
    }

    // session示例
    @RequestMapping(path = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("id",1);
        session.setAttribute("name","Test");
        return "set session";
    }

    @RequestMapping(path = "/session/get",method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }
}
