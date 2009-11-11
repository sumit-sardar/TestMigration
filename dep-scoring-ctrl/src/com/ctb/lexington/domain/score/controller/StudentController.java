package com.ctb.lexington.domain.score.controller;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.data.StudentData;
import com.ctb.lexington.db.irsdata.IrsStudentDimData;
import com.ctb.lexington.db.mapper.IrsStudentDimMapper;
import com.ctb.lexington.exception.DataException;


public class StudentController {
    private Connection conn;
    private StudentData data;
    private IrsStudentDimMapper stuMapper;

    public StudentController(Connection conn, StudentData student){
        this.conn = conn;
        this.data = student;
    }

    public void run() throws SQLException, DataException{
        stuMapper = new IrsStudentDimMapper(conn);
        // create IRS beans from input data
        IrsStudentDimData newStudent = getIrsStudentBean(data);
        // check if student exists in IRS
            IrsStudentDimData student = stuMapper.findByStudentId(newStudent.getStudentid());
        if(student == null) {
            // insert new student
            try {
                stuMapper.insert(newStudent);
            } catch (SQLException sqe) {
                // contentious student inserts are common, try to handle here
                student = stuMapper.findByStudentId(newStudent.getStudentid());
                if(student == null) {
                    throw sqe;
                } else if(!newStudent.equals(student)) {
                    stuMapper.update(newStudent);
                }
            }
        } else {
            // student exists, check if needs update
            if(!newStudent.equals(student)) {
                // update student                
                stuMapper.update(newStudent);
            }
        }
    }
    
    private IrsStudentDimData getIrsStudentBean(StudentData data) {
        IrsStudentDimData student = new IrsStudentDimData();
        
        student.setStudentid(data.getOasStudentId());
        student.setFirstName(data.getFirstName());
        student.setMiddleName(data.getMiddleInitial());
        student.setLastName(data.getLastName());
        student.setBirthDate(data.getBirthDate());
        student.setReportStudentId(data.getStudentIdentifier1());       
        return student;
    }
}