package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.UserBasicDetails;
import com.example.demo.exception.StaffException;
import com.example.demo.service.dto.FilterListRequestDto;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;

@Service
public class UserBasicDetailsRepository {

	private final Logger log = LoggerFactory.getLogger(UserBasicDetailsRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * @param userBasicDetails
	 * @throws StaffException
	 */
	public void addUserBasicDetails(UserBasicDetails userBasicDetails) throws StaffException {
		log.info("Inserting user basic details for user {}", userBasicDetails.getFirstName());
		String query = "insert into user_basic_details values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.info("Query: {}", query);
		try {
			jdbcTemplate.update(query, ps -> setUserBasicDetailsPreparedStatement(ps, userBasicDetails));
		} catch (Exception ex) {
			log.error("Error while inserting student basic details");
			throw new StaffException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}
	
	/**
	 * @param mobileNumber
	 * @return
	 * @throws StaffException
	 */
	public List<UserBasicDetails> getUserBasicDetailsByMobileNumber(String mobileNumber) throws StaffException {
		log.info("Getting user basic details for mobile number {}", mobileNumber);
		String query = "select * from user_basic_details where mobile = ?";
		log.info("Query {}", query);
		List<UserBasicDetails> userBasicDetails;
		try {
			userBasicDetails = jdbcTemplate.query(query,
					ps -> setUserBasicDetaislByMobilePreparedStatement(ps, mobileNumber),
					(rs, rowNum) -> getUserBasicDetailsByMobileRSMapper(rs));
		} catch (Exception ex) {
			log.error("Error while getting user basic details for mobile number {}", mobileNumber);
			throw new StaffException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return userBasicDetails;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxUserId() {
		log.info("Getting max user id");
		String query = "SELECT COUNT(user_id) as MAX_USER_ID FROM user_basic_details";
		int maxUserId = jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int maxUserId = 0;
				while (rs.next())
					maxUserId = rs.getInt("MAX_USER_ID");
				return maxUserId;
			}
		});
		return maxUserId;
	}
	
	public PageImpl<UserBasicDetails> getStaffDetails(String academicYear, PageRequest pageable,
			FilterListRequestDto filterRequestDto) throws StaffException {
		log.info("Fetching staff details list for academic year {}, page no {}, page size {}", academicYear,
				pageable.getPageNumber(), pageable.getPageSize());
		String query = "select * from user_basic_details where first_name != ? ";
		query += getFilterQuery(filterRequestDto) + " " + "limit ? offset ?";
		log.info("query {}", query);
		List<UserBasicDetails> userBasicDetailsList = null;
		try {
			userBasicDetailsList = jdbcTemplate.query(query,
					ps -> setStaffDetailsPreparedStatement(ps, academicYear, filterRequestDto, pageable),
					(rs, rowNum) -> getStaffDetailsRSMapper(rs));
		} catch (Exception ex) {
			log.error("Error while fetching staff list for page no {}, page size {}", academicYear,
					pageable.getPageNumber(), pageable.getPageSize());
			throw new StaffException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return new PageImpl<>(userBasicDetailsList, pageable,
				getStaffBasicDetailsCount(academicYear, filterRequestDto));
	}

	/**
	 * @param academicYear
	 * @param filterRequestDto
	 * @return
	 * @throws StaffException
	 */
	private int getStaffBasicDetailsCount(String academicYear, FilterListRequestDto filterRequestDto)
			throws StaffException {
		log.info("Fetching staff details count for academic year {}", academicYear);
		String query = "select count(*) as studentsCount from user_basic_details where first_name != ? ";
		query += getFilterQuery(filterRequestDto) + " ";
		log.info("query {}", query);
		int staffBasicDetailsCount = 0;
		try {
			staffBasicDetailsCount = jdbcTemplate.query(query,
					ps -> setStaffDetailsPreparedStatement(ps, academicYear, filterRequestDto, null),
					new ResultSetExtractor<Integer>() {

						@Override
						public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
							int count = 0;
							while (rs.next()) {
								count = rs.getInt("studentsCount");
							}
							return count;
						}
					});
		} catch (Exception ex) {
			log.error("Error while fetching staff count for academic year {}, page no {}, page size {}", academicYear);
			throw new StaffException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return staffBasicDetailsCount;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private UserBasicDetails getStaffDetailsRSMapper(ResultSet rs) throws SQLException {
		log.info("Mapping staff details result set to entity");
		return UserBasicDetails.builder().address(rs.getString("address")).adhar(rs.getString("adhar"))
				.alternateMobile(rs.getString("alternate_mobile"))
				.birthDate(DateUtils.getDate(rs.getDate("birth_date"))).caste(rs.getString("caste"))
				.email(rs.getString("email")).firstName(rs.getString("first_name")).gender(rs.getString("gender"))
				.lastName(rs.getString("last_name")).maritalStatus(rs.getString("marital_status"))
				.middleName(rs.getString("middle_name")).mobile(rs.getString("mobile"))
				.nationality(rs.getString("nationality")).religion(rs.getString("religion"))
				.userId(rs.getString("user_id")).build();
	}

	/**
	 * @param ps
	 * @param academicYear
	 * @param filterRequestDto
	 * @param pageable
	 * @throws SQLException
	 */
	private void setStaffDetailsPreparedStatement(PreparedStatement ps, String academicYear,
			FilterListRequestDto filterRequestDto, PageRequest pageable) throws SQLException {
		int indx = 1;
		ps.setString(indx++, Constants.ADMIN);
		if (filterRequestDto != null && filterRequestDto.getFilterDto() != null) {
			if (!CollectionUtils.isEmpty(filterRequestDto.getFilterDto().getCastes())) {
				for (String caste : filterRequestDto.getFilterDto().getCastes()) {
					ps.setString(indx++, caste);
				}
			}
			if (!CollectionUtils.isEmpty(filterRequestDto.getFilterDto().getReligions())) {
				for (String religion : filterRequestDto.getFilterDto().getReligions()) {
					ps.setString(indx++, religion);
				}
			}
			if (!StringUtils.isEmpty(filterRequestDto.getFilterDto().getGender())) {
				ps.setString(indx++, filterRequestDto.getFilterDto().getGender());
			}
			if (!StringUtils.isEmpty(filterRequestDto.getFilterDto().getMaritalStatus())) {
				ps.setString(indx++, filterRequestDto.getFilterDto().getMaritalStatus());
			}

			if (!StringUtils.isEmpty(filterRequestDto.getFilterDto().getQuickSearchText())) {
				ps.setString(indx++, "%" + filterRequestDto.getFilterDto().getQuickSearchText() + "%");
				ps.setString(indx++, "%" + filterRequestDto.getFilterDto().getQuickSearchText() + "%");
				ps.setString(indx++, "%" + filterRequestDto.getFilterDto().getQuickSearchText() + "%");
				ps.setString(indx++, "%" + filterRequestDto.getFilterDto().getQuickSearchText() + "%");
			}
		}
		if (pageable != null) {
			ps.setInt(indx++, pageable.getPageSize());
			ps.setLong(indx, pageable.getOffset());
		}
	}

	/**
	 * @param filterRequestDto
	 * @return
	 */
	private String getFilterQuery(FilterListRequestDto filterRequestDto) {
		log.info("Gettig filtered query for {}", filterRequestDto.toString());
		String filteredQuery = "";
		if (filterRequestDto != null && filterRequestDto.getFilterDto() != null) {
			if (!CollectionUtils.isEmpty(filterRequestDto.getFilterDto().getCastes())) {
				log.info("Filter for castes in {}", filterRequestDto.getFilterDto().getCastes().toString());
				filteredQuery += "and caste in (";
				for (int i = 0; i < filterRequestDto.getFilterDto().getCastes().size() - 1; i++) {
					filteredQuery += "?,";
				}
				filteredQuery += "?) ";
			}
			if (!CollectionUtils.isEmpty(filterRequestDto.getFilterDto().getReligions())) {
				log.info("Filter for religions in {}", filterRequestDto.getFilterDto().getReligions().toString());
				filteredQuery += "and religion in (";
				for (int i = 0; i < filterRequestDto.getFilterDto().getReligions().size() - 1; i++) {
					filteredQuery += "?,";
				}
				filteredQuery += "?) ";
			}
			if (!StringUtils.isEmpty(filterRequestDto.getFilterDto().getGender())) {
				log.info("Filter for gender {}", filterRequestDto.getFilterDto().getGender());
				filteredQuery += "and gender = ? ";
			}
			if (!StringUtils.isEmpty(filterRequestDto.getFilterDto().getMaritalStatus())) {
				log.info("Filter for marital status {}", filterRequestDto.getFilterDto().getMaritalStatus());
				filteredQuery += "and marital_status = ? ";
			}
			if (!StringUtils.isEmpty(filterRequestDto.getFilterDto().getQuickSearchText())) {
				log.info("Filter for quick search for {}", filterRequestDto.getFilterDto().getQuickSearchText());
				filteredQuery += "and (first_name like (?) or middle_name like(?) or last_name like(?) or"
						+ " address like(?)) ";
			}
		}
		if (CollectionUtils.isEmpty(filterRequestDto.getSortOrders())) {
			log.info("Setting order by reg no");
			filteredQuery += "order by user_id";
		} else {
			log.info("sorting on {}", filterRequestDto.getSortOrders().toString());
			filteredQuery += "order by ";
			int i = 0;
			for (; i < filterRequestDto.getSortOrders().size() - 1; i++) {
				filteredQuery += filterRequestDto.getSortOrders().get(i).getField() + ", ";
			}
			filteredQuery += filterRequestDto.getSortOrders().get(i).getField() + " ";
		}
		return filteredQuery;
	}

	/**
	 * @param ps
	 * @param userBasicDetails
	 * @throws SQLException
	 */
	private void setUserBasicDetailsPreparedStatement(PreparedStatement ps, UserBasicDetails userBasicDetails)
			throws SQLException {
		log.info("Setting prepared statement for user basic details");
		ps.setString(1, userBasicDetails.getUserId());
		ps.setString(2, userBasicDetails.getFirstName());
		ps.setString(3, userBasicDetails.getMiddleName());
		ps.setString(4, userBasicDetails.getLastName());
		ps.setString(5, userBasicDetails.getMobile());
		ps.setString(6, userBasicDetails.getEmail());
		ps.setString(7, userBasicDetails.getAddress());
		ps.setDate(8, DateUtils.getSqlDate(userBasicDetails.getBirthDate()));
		ps.setString(9, userBasicDetails.getMaritalStatus());
		ps.setString(10, userBasicDetails.getAdhar());
		ps.setString(11, userBasicDetails.getReligion());
		ps.setString(12, userBasicDetails.getCaste());
		ps.setString(13, userBasicDetails.getNationality());
		ps.setString(14, userBasicDetails.getGender());
		ps.setString(15, userBasicDetails.getAlternateMobile());
	}
	
	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private UserBasicDetails getUserBasicDetailsByMobileRSMapper(ResultSet rs) throws SQLException {
		log.info("Mapping result set for user basic details");
		return UserBasicDetails.builder().address(rs.getString("address")).adhar(rs.getString("adhar"))
				.alternateMobile(rs.getString("alternate_mobile"))
				.birthDate(DateUtils.getDate(rs.getDate("birth_date"))).caste(rs.getString("caste"))
				.email(rs.getString("email")).firstName(rs.getString("first_name")).gender(rs.getString("gender"))
				.lastName(rs.getString("last_name")).maritalStatus(rs.getString("marital_status"))
				.middleName(rs.getString("middle_name")).mobile(rs.getString("mobile"))
				.nationality(rs.getString("nationality")).religion(rs.getString("religion"))
				.userId(rs.getString("user_id")).build();
	}

	/**
	 * @param ps
	 * @param mobileNumber
	 * @throws SQLException
	 */
	private void setUserBasicDetaislByMobilePreparedStatement(PreparedStatement ps, String mobileNumber)
			throws SQLException {
		log.info("Setting prepared statement for user basic details by mobile number");
		ps.setString(1, mobileNumber);
	}
}
