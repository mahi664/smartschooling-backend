package com.example.demo.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.bo.ClassDetaislBO;
import com.example.demo.bo.FeesDetailsBO;
import com.example.demo.bo.RouteDetailsBO;
import com.example.demo.bo.StudentDetailsBO;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;
import com.sun.xml.bind.v2.runtime.reflect.Lister;

import jdk.internal.joptsimple.internal.Strings;

@Service
public class StudentService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	public StudentDetailsBO addNewStudent(StudentDetailsBO studentDetailsBO) {
		// TODO Auto-generated method stub
		try {
			
//			String nextStudentId = getNextStudentId();
			int nextStudentId = getMaxStudentId() + 1;
			studentDetailsBO.setStudentId(Integer.toString(nextStudentId));
			boolean res = addStudentBasicDeails(studentDetailsBO);
			if(res && studentDetailsBO.getRouteDetailsBO()!=null && studentDetailsBO.isTransportOpted())
				res = addSudentsTransportDetails(studentDetailsBO.getStudentId(), studentDetailsBO.getRouteDetailsBO());
			if(res && studentDetailsBO.getStudentClassDetails()!=null)
				res = addStudentClassesDetails(studentDetailsBO.getStudentId(),studentDetailsBO.getStudentClassDetails());
			if(res && studentDetailsBO.getStudentFeeDetails()!=null)
				res = addStudentFeesDetails(studentDetailsBO.getStudentId(), studentDetailsBO.getStudentFeeDetails());
			if(!res) {
				System.out.println("Problem in adding student Details");
				return null;
			}
			studentDetailsBO.setStudentId(getFormattedStudentId(nextStudentId));
			return studentDetailsBO;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while adding new Student");
			throw e;
		}
	}

	private boolean addStudentFeesDetails(String studentId, Map<String, List<FeesDetailsBO>> studentFeeDetails) {
		// TODO Auto-generated method stub
		boolean res = true;
		for(String academicId : studentFeeDetails.keySet()) {
			res = res && addStudentFeesDetails(studentId, academicId, studentFeeDetails.get(academicId));
		}
		return res;
	}

	private boolean addStudentFeesDetails(String studentId, String academicId, List<FeesDetailsBO> feesDetailsBOs) {
		// TODO Auto-generated method stub
		String query = "INSERT INTO STUDENT_FEES_DETAILS VALUES(?,?,?,?,?)";
		int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, studentId);
				ps.setString(2, academicId);
				ps.setString(3, feesDetailsBOs.get(i).getFeeId());
				ps.setDate(4, DateUtils.getSqlDate(new Date()));
				ps.setString(5, "BASE");
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return feesDetailsBOs.size();
			}
		});
		return res.length<=0 ? false : true;
	}

	private boolean addStudentClassesDetails(String studentId, Map<String, List<ClassDetaislBO>> studentClassDetails) {
		// TODO Auto-generated method stub
		boolean res = true;
		for(String academicId : studentClassDetails.keySet()) {
			res = res && addStudentClassesDetails(studentId,academicId,studentClassDetails.get(academicId));
		}
		return res;
	}

	private boolean addStudentClassesDetails(String studentId, String academicId, List<ClassDetaislBO> classDetaislBOs) {
		// TODO Auto-generated method stub
		String query = "INSERT INTO STUDENT_CLASS_DETAILS VALUES(?,?,?,?,?)";
		int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, studentId);
				ps.setString(2, academicId);
				ps.setString(3, classDetaislBOs.get(i).getClassId());
				ps.setDate(4, DateUtils.getSqlDate(new Date()));
				ps.setString(5, "BASE");
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return classDetaislBOs.size();
			}
		});
		return res.length<=0 ? false : true;
	}

	private boolean addSudentsTransportDetails(String studentId, RouteDetailsBO routeDetailsBO) {
		// TODO Auto-generated method stub
		String query = "INSERT INTO STUDENT_TRANSPORT_DETAILS VALUES(?,?,?,?,?,?)";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, studentId);
				ps.setString(2, routeDetailsBO.getRouteId());
				ps.setDate(3, DateUtils.getSqlDate(new Date()));
				ps.setDate(4, DateUtils.getSqlDate(Constants.MAX_DATE));
				ps.setDate(5, DateUtils.getSqlDate(new Date()));
				ps.setString(6, "BASE");
			}
		});
		return res<=0 ? false : true;
	}

	private boolean addStudentBasicDeails(StudentDetailsBO studentDetailsBO) {
		// TODO Auto-generated method stub
		String query = "INSERT INTO STUDENT_DETAILS VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, studentDetailsBO.getStudentId());
				ps.setString(2, studentDetailsBO.getFirstName());
				ps.setString(3, studentDetailsBO.getMiddleName());
				ps.setString(4, studentDetailsBO.getLastName());
				ps.setDate(5, DateUtils.getSqlDate(studentDetailsBO.getBirthDate()));
				ps.setString(6, studentDetailsBO.getGender());
				ps.setString(7, studentDetailsBO.getAdharNumber());
				ps.setString(8, studentDetailsBO.getEmail());
				ps.setString(9, studentDetailsBO.getMobileNumber());
				ps.setString(10, studentDetailsBO.getAlternateMobile());
				ps.setString(11, studentDetailsBO.getAddress());
				ps.setString(12, studentDetailsBO.getReligion());
				ps.setString(13, studentDetailsBO.getCaste());
				ps.setString(14, studentDetailsBO.isTransportOpted() ? Constants.YES : Constants.NO);
				ps.setString(15, studentDetailsBO.getNationality());
			}
		});
		return res<=0 ? false : true;
	}

	private String getNextStudentId() {
		// TODO Auto-generated method stub
		int nextStudentId = getMaxStudentId() + 1;
		return getFormattedStudentId(nextStudentId);
	}

	private String getFormattedStudentId(int studentId) {
		String nextStudentIdString = Constants.STUDENT_ID_PREFIX;
		if(studentId<10)
			nextStudentIdString += "000"+studentId;
		else if(studentId>=10 && studentId<100)
			nextStudentIdString += "00"+studentId;
		else if(studentId>=100 && studentId<1000)
			nextStudentIdString += "0"+studentId;
		else
			nextStudentIdString += studentId;
		return nextStudentIdString;
	}

	private int getMaxStudentId() {
		String query = "SELECT COUNT(STUD_ID) as MAX_STUDENT_ID FROM STUDENT_DETAILS";
		int maxStudId = jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int maxStudId = 0;
				while(rs.next())
					maxStudId = rs.getInt("MAX_STUDENT_ID");
				return maxStudId;
			}
		});
		return maxStudId;
	}

	public List<StudentDetailsBO> getStudentDetails() {
		// TODO Auto-generated method stub
		Map<String, StudentDetailsBO> studentDetails = getStudentBasicDetails();
		if(studentDetails!=null && studentDetails.size()>0) {
			populateStudentAcademicDetails(studentDetails);
			populateStudentFeesDetails(studentDetails);
			populateStudentTransportDetails(studentDetails);
		}
		return new ArrayList<StudentDetailsBO>(studentDetails.values());
	}

	private void populateStudentTransportDetails(Map<String, StudentDetailsBO> studentDetails) {
		// TODO Auto-generated method stub
		String query = "select A.*, B.source,B.destination,B.distance " + 
				"from student_transport_details A, routes B " + 
				"where A.route_id = B.route_id";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				while(rs.next()) {
					String studentId = getFormattedStudentId(Integer.parseInt(rs.getString("STUD_ID")));
					StudentDetailsBO studentDetailsBO = studentDetails.get(studentId);
					if(studentDetailsBO.getRouteDetailsBO()==null)
						studentDetailsBO.setRouteDetailsBO(new RouteDetailsBO());
					RouteDetailsBO routeDetailsBO = studentDetailsBO.getRouteDetailsBO();
					routeDetailsBO.setRouteId(rs.getString("ROUTE_ID"));
					routeDetailsBO.setSource(rs.getString("SOURCE"));
					routeDetailsBO.setDestination(rs.getString("DESTINATION"));
					routeDetailsBO.setDistance(rs.getDouble("DISTANCE"));
				}
				return null;
			}
		});
	}

	private void populateStudentFeesDetails(Map<String, StudentDetailsBO> studentDetails) {
		
		String query = "select B.stud_id, B.academic_id, B.fee_id, C.fee_name, A.amount, A.class_id, A.route_id from fee_details A, student_fees_details B, fee_types C " + 
				"where A.fee_id = B.fee_id and A.fee_id=C.fee_id and B.fee_id=C.fee_id and (A.class_id in (select distinct(class_id) from student_class_details where stud_id=B.stud_id) " + 
				"or A.route_id in (select distinct(route_id) from student_transport_details where stud_id=B.stud_id) or (A.class_id= ? and A.route_id=?))";
		jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, Constants.BLANK_STRING);
				ps.setString(2, Constants.BLANK_STRING);
			}
		}, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()) {
					String studentId = getFormattedStudentId(Integer.parseInt(rs.getString("STUD_ID")));
					String academicId = rs.getString("ACADEMIC_ID");
					StudentDetailsBO studentDetailsBO = studentDetails.get(studentId);
					if(studentDetails!=null) {
						Map<String, List<FeesDetailsBO>> feesDetails = studentDetailsBO.getStudentFeeDetails();
						if(feesDetails==null)
							feesDetails = new HashMap<String, List<FeesDetailsBO>>();
						if(!feesDetails.containsKey(academicId))
							feesDetails.put(academicId, new ArrayList<FeesDetailsBO>());
						List<FeesDetailsBO> feesDetailsBOs = feesDetails.get(academicId);
						FeesDetailsBO feesDetailsBO = new FeesDetailsBO();
						feesDetailsBO.setFeeId(rs.getString("FEE_ID"));
						feesDetailsBO.setFeeName(rs.getString("FEE_NAME"));
						feesDetailsBO.setAmount(rs.getDouble("AMOUNT"));
						feesDetailsBO.setClassId(rs.getString("CLASS_ID"));
						feesDetailsBO.setRouteId(rs.getString("ROUTE_ID"));
						feesDetailsBOs.add(feesDetailsBO);
						studentDetailsBO.setStudentFeeDetails(feesDetails);
					}
				}
				
				return null;
			}
		});
	}

	private void populateStudentAcademicDetails(Map<String, StudentDetailsBO> studentDetails) {
		String query = "select A.*, B.class_name from student_class_details A, classes B " + 
				"where A.class_id = B.class_id";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				while(rs.next()) {
					String studentId = getFormattedStudentId(Integer.parseInt(rs.getString("STUD_ID")));
					StudentDetailsBO studentDetailsBO = studentDetails.get(studentId);
					if(studentDetailsBO!=null) {
						Map<String, List<ClassDetaislBO>> classDetails = studentDetailsBO.getStudentClassDetails();
						if(classDetails==null)
							classDetails = new HashMap<String, List<ClassDetaislBO>>();
						String academicId = rs.getString("ACADEMIC_ID");
						if(!classDetails.containsKey(academicId))
							classDetails.put(academicId, new ArrayList<ClassDetaislBO>());
						List<ClassDetaislBO> classes = classDetails.get(academicId);
						ClassDetaislBO classDetaislBO = new ClassDetaislBO();
						classDetaislBO.setClassId(rs.getString("CLASS_ID"));
						classDetaislBO.setClassName(rs.getString("CLASS_NAME"));
						classes.add(classDetaislBO);
						studentDetailsBO.setStudentClassDetails(classDetails);
					}
				}
				
				return null;
			}
		});
	}

	private Map<String, StudentDetailsBO> getStudentBasicDetails() {
		// TODO Auto-generated method stub
//		String query = "select A.* , B.route_id, C.source, C.destination, C.distance " + 
//				"from student_details A, student_transport_details B, routes C " + 
//				"where A.stud_id = B.stud_id and B.route_id = C.route_id";
		
		String query = "SELECT * FROM STUDENT_DETAILS";
		return jdbcTemplate.query(query, new ResultSetExtractor<Map<String, StudentDetailsBO>>() {

			@Override
			public Map<String, StudentDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				Map<String, StudentDetailsBO> studentDetails = new HashMap<String, StudentDetailsBO>();
				while(rs.next()) {
					String studentId = getFormattedStudentId(rs.getInt("STUD_ID"));
					if(!studentDetails.containsKey(studentId))
						studentDetails.put(studentId, new StudentDetailsBO());
					StudentDetailsBO studentDetailsBO = studentDetails.get(studentId);
					studentDetailsBO.setStudentId(studentId);
					studentDetailsBO.setFirstName(rs.getString("FIRST_NAME"));
					if(rs.getString("MIDDLE_NAME")!=null)
						studentDetailsBO.setMiddleName(rs.getString("MIDDLE_NAME"));
					else
						studentDetailsBO.setMiddleName(Constants.BLANK_STRING);
					studentDetailsBO.setLastName(rs.getString("LAST_NAME"));
					studentDetailsBO.setBirthDate(DateUtils.getDate(rs.getDate("BIRTH_DATE")));
					studentDetailsBO.setGender(rs.getString("GENDER"));
					studentDetailsBO.setAdharNumber(rs.getString("ADHAR"));
					studentDetailsBO.setEmail(rs.getString("EMAIL"));
					studentDetailsBO.setMobileNumber(rs.getString("MOBILE"));
					studentDetailsBO.setAlternateMobile(rs.getString("ALTERNATE_MOBILE"));
					studentDetailsBO.setAddress(rs.getString("ADDRESS"));
					studentDetailsBO.setReligion(rs.getString("RELIGION"));
					studentDetailsBO.setCaste(rs.getString("CASTE"));
					studentDetailsBO.setTransportOpted(Constants.YES.equals(rs.getString("TRANSPORT"))?true:false);
					studentDetailsBO.setNationality(rs.getString("NATIONALITY"));
					
//					if(studentDetailsBO.getRouteDetailsBO()==null)
//						studentDetailsBO.setRouteDetailsBO(new RouteDetailsBO());
//					RouteDetailsBO routeDetailsBO = studentDetailsBO.getRouteDetailsBO();
//					routeDetailsBO.setRouteId(rs.getString("ROUTE_ID"));
//					routeDetailsBO.setSource(rs.getString("SOURCE"));
//					routeDetailsBO.setDestination(rs.getString("DESTINATION"));
//					routeDetailsBO.setDistance(rs.getDouble("DISTANCE"));
				}
				return studentDetails;
			}
		});
	}

	@Transactional
	public StudentDetailsBO updateStudentDetails(StudentDetailsBO studentDetailsBO)
	{
		String studentid = studentDetailsBO.getStudentId();
		int studentID = reformatStudentID(studentid);
		System.out.println("Passed student id : " + studentid);
		System.out.println("filtered student id "+studentID);
		
		
		///
		try 
		{
			String sID = ""+studentID;
			boolean result = false;
			
			//To delete entries from 4 tables 
			
			result = deleteEntry_student_class_details(sID);
			if(result)
				System.out.println("Entry for student id "+sID+" has been deleted from the Student_Class_Details table.");
			else
				System.out.println("Entry does not exitst in the table for sID "+sID);
			
			result=deleteEntry_student_fees_details(sID);
			if(result)
				System.out.println("Entry for student id "+sID+" has been deleted from the Student_Fees_Details table.");
			else
				System.out.println("Entry does not exitst in the table for sID "+sID);
			
			result=deleteEntry_student_transport_details(sID);
			if(result)
				System.out.println("Entry for student id "+sID+" has been deleted from the Student_Transport_Details table.");
			else
				System.out.println("Entry does not exitst in the table for sID "+sID);
			
			result=deleteEntry_student_details(sID);
			if(result)
				System.out.println("Entry for student id "+sID+" has been deleted from the Student_Details table.");
			else
				System.out.println("Entry does not exitst in the table for sID "+sID);
			
			
			//The student has been deleted from all the tables
			//Now add it again in all tables using the same ID
	        		
			studentDetailsBO.setStudentId(sID);
			System.out.println("Set student IS as "+sID+" before adding.");
			result = addStudentBasicDeails(studentDetailsBO);
			if(result && studentDetailsBO.getRouteDetailsBO()!=null && studentDetailsBO.isTransportOpted())
				result = addSudentsTransportDetails(studentDetailsBO.getStudentId(), studentDetailsBO.getRouteDetailsBO());
			if(result && studentDetailsBO.getStudentClassDetails()!=null)
				result = addStudentClassesDetails(studentDetailsBO.getStudentId(),studentDetailsBO.getStudentClassDetails());
			if(result && studentDetailsBO.getStudentFeeDetails()!=null)
			{
				System.out.println("Fees details for : "+studentDetailsBO.getStudentId());
				System.out.println("Details are : " + studentDetailsBO.getStudentFeeDetails());
				result = addStudentFeesDetails(studentDetailsBO.getStudentId(), studentDetailsBO.getStudentFeeDetails());
			}
				
			if(!result) {
				System.out.println("Problem in adding student Details");
				return null;
			}
			
			
			
			studentDetailsBO.setStudentId(studentid);
			return studentDetailsBO;
		} 
		catch (Exception e) 
		{
			System.out.println("Error while Updating Student");
			e.getStackTrace();
			throw e;
		}
	}
	
	private int reformatStudentID(String studentid)
	{
		int sid=-1;
		
		try 
		{
			if(!StringUtils.isEmpty(studentid))
			{
				String actualID = studentid.substring(Constants.APPEND_CHARACTERS);
				sid = Integer.parseInt(actualID);
				return sid;
			}
			
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			System.out.println("Error while reformating student id.");
			throw e;
		}
		
		return sid;
	}
	
	private boolean deleteEntry_student_class_details(String stud_id)
	{
		String query = "DELETE FROM STUDENT_CLASS_DETAILS WHERE STUD_ID=?";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException 
				{
					ps.setString(1, stud_id);
				}
		});
		
		return res<=0 ? false : true;
	}
	
	private boolean deleteEntry_student_fees_details(String stud_id)
	{
		String query = "DELETE FROM STUDENT_FEES_DETAILS WHERE STUD_ID=?";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException 
				{
					ps.setString(1, stud_id);
				}
		});
		
		return res<=0 ? false : true;
	}
	private boolean deleteEntry_student_transport_details(String stud_id)
	{
		String query = "DELETE FROM STUDENT_TRANSPORT_DETAILS WHERE STUD_ID=?";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException 
				{
					ps.setString(1, stud_id);
				}
		});
		
		return res<=0 ? false : true;
	}
	private boolean deleteEntry_student_details(String stud_id)
	{
		String query = "DELETE FROM STUDENT_DETAILS WHERE STUD_ID=?";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException 
				{
					ps.setString(1, stud_id);
				}
		});
		
		return res<=0 ? false : true;
	}

}
