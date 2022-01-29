package com.example.demo.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bo.ClassDetaislBO;
import com.example.demo.bo.SubjectDetailsBO;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;

@Service
public class ClassesService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ClassDetaislBO addNewClass(ClassDetaislBO classDetaislBO) {
		try {			
			String query = "INSERT INTO CLASSES VALUES(?,?,?,?)";
			int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1, classDetaislBO.getClassId());
					ps.setString(2, classDetaislBO.getClassName());
					ps.setDate(3, DateUtils.getSqlDate(new Date()));
					ps.setString(4, "BASE");
				}
			});
			if(res<=0) {
				System.out.println("Problem in adding new class details");
				return null;
			}
			return classDetaislBO;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while adding new class details");
			e.printStackTrace();
		}
		return null;
	}

	public List<ClassDetaislBO> addNewClasses(List<ClassDetaislBO> classDetaislBOs) {
		try {
			
			String query = "INSERT INTO CLASSES VALUES(?,?,?,?)";
			int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1, classDetaislBOs.get(i).getClassId());
					ps.setString(2, classDetaislBOs.get(i).getClassName());
					ps.setDate(3, DateUtils.getSqlDate(new Date()));
					ps.setString(4, "BASE");
				}
				
				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return classDetaislBOs.size();
				}
			});
			
			if(res.length<=0) {
				System.out.print("Problem occured while inserting classes details");
				return null;
			}
			return classDetaislBOs;
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while adding new classes details");
			e.printStackTrace();
		}
		return null;
	}

	public List<ClassDetaislBO> getClassesDetails() {
		try {
			String query = "SELECT * FROM CLASSES";
			List<ClassDetaislBO> classDetaislBOs = jdbcTemplate.query(query, new ResultSetExtractor<List<ClassDetaislBO>>() {

				@Override
				public List<ClassDetaislBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
					// TODO Auto-generated method stub
					List<ClassDetaislBO> classes = new ArrayList<ClassDetaislBO>();
					while(rs.next()) {
						ClassDetaislBO classDetailsBO = new ClassDetaislBO();
						classDetailsBO.setClassId(rs.getString("CLASS_ID"));
						classDetailsBO.setClassName(rs.getString("CLASS_NAME"));
						classes.add(classDetailsBO);
					}
					return classes;
				}
			});
			if(classDetaislBOs==null) {
				System.out.println("Problem in fetching classes details");
				return null;
			}
			return classDetaislBOs;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while fetching classes details");
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public List<SubjectDetailsBO> addClassSubject(String classId, List<SubjectDetailsBO> subjectIds, boolean isUpdateOp){
		try {
			if(isUpdateOp) {
				deleteClassSubjects(classId, subjectIds);	
			}
			
			return addClassSubject(classId, subjectIds);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in adding class subject details");
			e.printStackTrace();
			throw e;
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public List<SubjectDetailsBO> addClassSubject(String classId, List<SubjectDetailsBO> subjectIds) {
		try {
			String query = "INSERT INTO CLASS_SUBJECT_DETAILS VALUES(?,?,?,?,?,?)";
			int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1, classId);
					ps.setString(2, subjectIds.get(i).getSubjectId());
					ps.setDate(3, DateUtils.getSqlDate(new Date()));
					ps.setDate(4, DateUtils.getSqlDate(Constants.MAX_DATE));
					ps.setDate(5, DateUtils.getSqlDate(new Date()));
					ps.setString(6, "BASE");
				}
				
				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return subjectIds.size();
				}
			});
			if(res.length<=0) {
				System.out.println("Problem while adding classes subject Map");
				return null;
			}
			return subjectIds;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while inserting classes subject map");
			e.printStackTrace();
			throw e;
		}
//		return null;
	}

	public List<SubjectDetailsBO> getClassesSubjects(String classId){
		String query = "SELECT A.SUB_ID, B.SUB_NAME FROM CLASS_SUBJECT_DETAILS A, SUBJECTS B "+
						"WHERE A.SUB_ID = B.SUB_ID AND A.CLASS_ID=?";
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, classId);
			}
		}, new ResultSetExtractor<List<SubjectDetailsBO>>() {

			@Override
			public List<SubjectDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<SubjectDetailsBO> classesSubjects = new ArrayList<>();
				while(rs.next()) {
					SubjectDetailsBO subjectDetailsBO = new SubjectDetailsBO();
					subjectDetailsBO.setSubjectId(rs.getString("SUB_ID"));
					subjectDetailsBO.setSubjectName(rs.getString("SUB_NAME"));
					
					classesSubjects.add(subjectDetailsBO);
				}
				
				return classesSubjects;
			}
		});
	}
	
	public List<SubjectDetailsBO> deleteClassSubjects(String classId, List<SubjectDetailsBO> subjectIds){
		String query = "DELETE FROM CLASS_SUBJECT_DETAILS WHERE CLASS_ID=?";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, classId);
			}
		});
		if(res<=0) {
			return null;
		}
		return subjectIds;
	}
}
