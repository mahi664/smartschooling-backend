CREATE TABLE smartschoolingdev.institute_det (
	institute_id varchar(100) NOT NULL,
	institute_name varchar(500) NOT NULL,
	foundation_date DATETIME NOT NULL,
	address varchar(500) NOT NULL,
	last_update_time DATETIME NOT null,
	last_user varchar(100) NOT null,
	CONSTRAINT institute_det_pk PRIMARY KEY (institute_id)
);

CREATE TABLE smartschoolingdev.institute_branch_det (
	branch_id varchar(100) NOT NULL,
	institute_id varchar(100) NOT NULL,
	branch_name varchar(200) NOT NULL,
	foundation_date DATETIME NOT NULL,
	address varchar(200) NOT NULL,
	last_update_time DATETIME NOT null,
	last_user varchar(100) NOT null,
	CONSTRAINT institute_branch_det_pk PRIMARY KEY (branch_id),
	CONSTRAINT institute_branch_det_fk FOREIGN KEY (institute_id) REFERENCES smartschoolingdev.institute_det(institute_id)
);

CREATE TABLE smartschoolingdev.classes (
	class_id varchar(100) NOT NULL,
	class_name varchar(100) NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT classes_pk PRIMARY KEY (class_id)
);

CREATE TABLE smartschoolingdev.branch_classes_details (
	branch_id varchar(100) NOT NULL,
	class_id varchar(100) NOT NULL,
	eff_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT branch_classes_details_pk PRIMARY KEY (branch_id,class_id,eff_date,end_date),
	CONSTRAINT branch_classes_details_fk FOREIGN KEY (branch_id) REFERENCES smartschoolingdev.institute_branch_det(branch_id),
	CONSTRAINT branch_classes_details_fk_1 FOREIGN KEY (class_id) REFERENCES smartschoolingdev.classes(class_id)
);

CREATE TABLE smartschoolingdev.subjects (
	sub_id varchar(100) NOT NULL,
	sub_name varchar(100) NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT null,
	CONSTRAINT subjects_pk PRIMARY KEY (sub_id)
);

CREATE TABLE smartschoolingdev.routes (
	route_id varchar(100) NOT NULL,
	source varchar(100) NOT NULL,
	destination varchar(100) NOT NULL,
	distance DECIMAL NOT NULL,
	CONSTRAINT routes_pk PRIMARY KEY (route_id)
);

ALTER TABLE smartschoolingdev.routes ADD last_update_time DATETIME NOT NULL;
ALTER TABLE smartschoolingdev.routes ADD last_user varchar(100) NOT NULL;
ALTER TABLE smartschoolingdev.routes ADD CONSTRAINT routes_un UNIQUE KEY (route_id,source,destination,distance);


CREATE TABLE smartschoolingdev.fee_types (
	fee_id varchar(100) NOT NULL,
	fee_name varchar(100) NOT NULL,
	fee_discription varchar(100) NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT fee_types_pk PRIMARY KEY (fee_id)
);


CREATE TABLE smartschoolingdev.fee_details (
	fee_id varchar(100) NOT NULL,
	class_id varchar(100),
	route_id varchar(100),
	amount DECIMAL NOT NULL,
	eff_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	CONSTRAINT fee_details_pk PRIMARY KEY (fee_id,class_id,route_id,amount,eff_date,end_date),
	CONSTRAINT fee_details_fk_2 FOREIGN KEY (fee_id) REFERENCES smartschoolingdev.fee_types(fee_id)
);

CREATE TABLE smartschoolingdev.academic_details (
	academic_id varchar(100) NOT NULL,
	academic_year varchar(100) NOT NULL,
	display_name varchar(100) NOT NULL,
	academic_start_date DATETIME NOT NULL,
	academic_end_date DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_update_user varchar(100) NOT NULL,
	CONSTRAINT academic_details_pk PRIMARY KEY (academic_id),
	CONSTRAINT academic_details_un UNIQUE KEY (academic_year,display_name,academic_id,academic_start_date,academic_end_date)
);

CREATE TABLE smartschoolingdev.class_subject_details (
	class_id varchar(100) NOT NULL,
	sub_id varchar(100) NOT NULL,
	eff_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT class_subject_details_pk PRIMARY KEY (class_id,sub_id,eff_date,end_date),
	CONSTRAINT class_subject_details_fk FOREIGN KEY (class_id) REFERENCES smartschoolingdev.classes(class_id),
	CONSTRAINT class_subject_details_fk_1 FOREIGN KEY (sub_id) REFERENCES smartschoolingdev.subjects(sub_id)
);

CREATE TABLE smartschoolingdev.student_details (
	stud_id varchar(100) NOT NULL,
	first_name varchar(100) NOT NULL,
	middle_name varchar(100) NULL,
	last_name varchar(100) NOT NULL,
	birth_date DATETIME NOT NULL,
	gender varchar(100) NULL,
	adhar varchar(100) NOT NULL,
	email varchar(100) NULL,
	mobile varchar(100) NOT NULL,
	alternate_mobile varchar(100) NULL,
	address varchar(500) NOT NULL,
	religion varchar(100) NULL,
	caste varchar(100) NULL,
	transport CHAR NOT NULL,
	nationality varchar(100) NULL,
	CONSTRAINT student_details_pk PRIMARY KEY (stud_id),
	CONSTRAINT student_details_un UNIQUE KEY (adhar)
);

CREATE TABLE smartschoolingdev.student_fees_details (
	stud_id varchar(100) NOT NULL,
	academic_id varchar(100) NOT NULL,
	fee_id varchar(100) NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT student_fees_details_pk PRIMARY KEY (stud_id,academic_id,fee_id),
	CONSTRAINT student_fees_details_fk FOREIGN KEY (stud_id) REFERENCES smartschoolingdev.student_details(stud_id),
	CONSTRAINT student_fees_details_fk_1 FOREIGN KEY (fee_id) REFERENCES smartschoolingdev.fee_types(fee_id),
	CONSTRAINT student_fees_details_fk_2 FOREIGN KEY (academic_id) REFERENCES smartschoolingdev.academic_details(academic_id)
);

CREATE TABLE smartschoolingdev.student_transport_details (
	stud_id varchar(100) NOT NULL,
	route_id varchar(100) NOT NULL,
	eff_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT student_transport_detaisl_pk PRIMARY KEY (stud_id,route_id,eff_date,end_date),
	CONSTRAINT student_transport_detaisl_fk FOREIGN KEY (stud_id) REFERENCES smartschoolingdev.student_details(stud_id),
	CONSTRAINT student_transport_detaisl_fk_1 FOREIGN KEY (route_id) REFERENCES smartschoolingdev.routes(route_id)
);

CREATE TABLE smartschoolingdev.student_class_details (
	stud_id varchar(100) NOT NULL,
	academic_id varchar(100) NOT NULL,
	class_id varchar(100) NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL
);
ALTER TABLE smartschoolingdev.student_class_details ADD CONSTRAINT student_class_details_pk PRIMARY KEY (stud_id,academic_id,class_id);
ALTER TABLE smartschoolingdev.student_class_details ADD CONSTRAINT student_class_details_fk FOREIGN KEY (class_id) REFERENCES smartschoolingdev.classes(class_id);
ALTER TABLE smartschoolingdev.student_class_details ADD CONSTRAINT student_class_details_fk_1 FOREIGN KEY (stud_id) REFERENCES smartschoolingdev.student_details(stud_id);
ALTER TABLE smartschoolingdev.student_class_details ADD CONSTRAINT student_class_details_fk_2 FOREIGN KEY (academic_id) REFERENCES smartschoolingdev.academic_details(academic_id);

CREATE TABLE smartschoolingdev.accounts (
	account_id varchar(100) NOT NULL,
	account_name varchar(100) NOT NULL,
	bank_name varchar(100) DEFAULT null NULL,
	bank_account_number varchar(100) DEFAULT null NULL,
	CONSTRAINT accounts_pk PRIMARY KEY (account_id)
);

CREATE TABLE smartschoolingdev.students_fees_collection_transaction (
	collection_id varchar(100) NOT NULL,
	stud_id varchar(100) NOT NULL,
	transaction_date DATETIME NOT NULL,
	account_id varchar(100) NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT students_fees_collection_transaction_pk PRIMARY KEY (collection_id),
	CONSTRAINT students_fees_collection_transaction_fk FOREIGN KEY (account_id) REFERENCES smartschoolingdev.accounts(account_id),
	CONSTRAINT students_fees_collection_transaction_fk_1 FOREIGN KEY (stud_id) REFERENCES smartschoolingdev.student_details(stud_id)
);

CREATE TABLE smartschoolingdev.students_fees_collection_transaction_details (
	trans_det_id varchar(100) NOT NULL,
	collection_id varchar(100) NOT NULL,
	fee_id varchar(100) NOT NULL,
	academic_id varchar(100) NOT NULL,
	amount DOUBLE NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT students_fees_collection_transaction_details_pk PRIMARY KEY (trans_det_id),
	CONSTRAINT students_fees_collection_transaction_details_fk FOREIGN KEY (collection_id) REFERENCES smartschoolingdev.students_fees_collection_transaction(collection_id),
	CONSTRAINT students_fees_collection_transaction_details_fk_1 FOREIGN KEY (fee_id) REFERENCES smartschoolingdev.fee_types(fee_id),
	CONSTRAINT students_fees_collection_transaction_details_fk_2 FOREIGN KEY (academic_id) REFERENCES smartschoolingdev.academic_details(academic_id)
);


CREATE TABLE smartschoolingdev.transactions (
	transaction_id varchar(100) NOT NULL,
	amount DOUBLE NOT NULL,
	transaction_date DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT transactions_pk PRIMARY KEY (transaction_id)
);

CREATE TABLE smartschoolingdev.ref_table_types (
	ref_table_type varchar(10) NOT NULL,
	ref_table_name varchar(100) NOT NULL,
	CONSTRAINT ref_table_types_pk PRIMARY KEY (ref_table_type)
);

CREATE TABLE smartschoolingdev.transaction_details (
	transaction_det_id varchar(100) NOT NULL,
	transaction_id varchar(100) NOT NULL,
	account_id varchar(100) NOT NULL,
	transaction_type CHAR NOT NULL,
	ref_id varchar(100) DEFAULT null NULL,
	ref_table_type VARCHAR(10) DEFAULT null NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT transaction_details_pk PRIMARY KEY (transaction_det_id),
	CONSTRAINT transaction_details_fk FOREIGN KEY (account_id) REFERENCES smartschoolingdev.accounts(account_id),
	CONSTRAINT transaction_details_fk_1 FOREIGN KEY (ref_table_type) REFERENCES smartschoolingdev.ref_table_types(ref_table_type)
);
ALTER TABLE smartschoolingdev.transaction_details ADD CONSTRAINT transaction_details_fk_2 FOREIGN KEY (transaction_id) REFERENCES smartschoolingdev.transactions(transaction_id);

CREATE TABLE smartschoolingdev.roles (
	role_id varchar(100) NOT NULL,
	role_name varchar(100) NOT NULL,
	role_description varchar(500) NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT roles_pk PRIMARY KEY (roll_id)
);

CREATE TABLE smartschoolingdev.leave_types (
	leave_id varchar(100) NOT NULL,
	leave_name varchar(100) NOT NULL,
	description varchar(500) NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT leave_types_pk PRIMARY KEY (leave_id)
);

CREATE TABLE smartschoolingdev.accrual_frequency (
	accrual_frequency varchar(100) NOT NULL,
	CONSTRAINT accrual_frequency_pk PRIMARY KEY (accrual_frequency)
);

CREATE TABLE smartschoolingdev.leave_accrual_details (
	leave_id varchar(100) NOT NULL,
	accrual_frequency varchar(100) NOT NULL,
	accrual_day INTEGER NOT NULL,
	amount DOUBLE NOT NULL,
	start_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT leave_accrual_details_pk PRIMARY KEY (leave_id,accrual_frequency,start_date,end_date),
	CONSTRAINT leave_accrual_details_fk FOREIGN KEY (leave_id) REFERENCES smartschoolingdev.leave_types(leave_id),
	CONSTRAINT leave_accrual_details_fk_1 FOREIGN KEY (accrual_frequency) REFERENCES smartschoolingdev.accrual_frequency(accrual_frequency)
);

CREATE TABLE smartschoolingdev.user_basic_details (
	user_id varchar(100) NOT NULL,
	first_name varchar(100) NOT NULL,
	middle_name varchar(100) NULL,
	last_name varchar(100) NOT NULL,
	mobile varchar(12) NOT NULL,
	email varchar(100) NULL,
	address varchar(500) NOT NULL,
	birth_date DATETIME NOT NULL,
	marital_status varchar(100) NOT NULL,
	adhar varchar(100) NULL,
	religion varchar(100) NULL,
	caste varchar(100) NULL,
	nationality varchar(100) NULL,
	CONSTRAINT user_basic_details_pk PRIMARY KEY (user_id)
);

CREATE TABLE smartschoolingdev.user_login_details (
	user_id varchar(100) NOT NULL,
	username varchar(200) NOT NULL,
	password varchar(1000) NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT user_login_details_pk PRIMARY KEY (user_id),
	CONSTRAINT user_login_details_fk FOREIGN KEY (user_id) REFERENCES smartschoolingdev.user_basic_details(user_id)
);

CREATE TABLE smartschoolingdev.user_role_mapping (
	user_id varchar(100) NOT NULL,
	role_id varchar(100) NOT NULL,
	eff_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT user_role_mapping_pk PRIMARY KEY (user_id,role_id,eff_date,end_date),
	CONSTRAINT user_role_mapping_fk FOREIGN KEY (user_id) REFERENCES smartschoolingdev.user_basic_details(user_id),
	CONSTRAINT user_role_mapping_fk_1 FOREIGN KEY (role_id) REFERENCES smartschoolingdev.roles(role_id)
);

CREATE TABLE smartschoolingdev.user_academic_details (
	user_id varchar(100) NOT NULL,
	academic_id varchar(100) NOT NULL,
	class_id varchar(100) NOT NULL,
	sub_id varchar(100) NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT user_academic_details_pk PRIMARY KEY (user_id,academic_id,sub_id,class_id),
	CONSTRAINT user_academic_details_fk FOREIGN KEY (academic_id) REFERENCES smartschoolingdev.academic_details(academic_id),
	CONSTRAINT user_academic_details_fk_1 FOREIGN KEY (class_id) REFERENCES smartschoolingdev.classes(class_id),
	CONSTRAINT user_academic_details_fk_2 FOREIGN KEY (user_id) REFERENCES smartschoolingdev.user_basic_details(user_id),
	CONSTRAINT user_academic_details_fk_3 FOREIGN KEY (sub_id) REFERENCES smartschoolingdev.subjects(sub_id)
);

CREATE TABLE smartschoolingdev.user_applicable_leaves (
	user_leave_rec_id varchar(100) NOT NULL,
	user_id varchar(100) NOT NULL,
	leave_id varchar(100) NOT NULL,
	eff_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT user_applicable_leaves_pk PRIMARY KEY (user_leave_rec_id),
	CONSTRAINT user_applicable_leaves_un UNIQUE KEY (user_id,leave_id,eff_date,end_date),
	CONSTRAINT user_applicable_leaves_fk FOREIGN KEY (user_id) REFERENCES smartschoolingdev.user_basic_details(user_id),
	CONSTRAINT user_applicable_leaves_fk_1 FOREIGN KEY (leave_id) REFERENCES smartschoolingdev.leave_types(leave_id)
);

CREATE TABLE smartschoolingdev.user_leave_accrual_details (
	id varchar(100) NOT NULL,
	user_leave_rec_id varchar(100) NOT NULL,
	academic_id varchar(100) NOT NULL,
	accrual_date DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	amount DOUBLE NOT NULL,
	CONSTRAINT user_leave_accrual_details_pk PRIMARY KEY (id),
	CONSTRAINT user_leave_accrual_details_fk FOREIGN KEY (user_leave_rec_id) REFERENCES smartschoolingdev.user_applicable_leaves(user_leave_rec_id),
	CONSTRAINT user_leave_accrual_details_fk_1 FOREIGN KEY (academic_id) REFERENCES smartschoolingdev.academic_details(academic_id)
);

CREATE TABLE smartschoolingdev.user_leaves_deduction_details (
	user_leave_rec_id varchar(100) NOT NULL,
	eff_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	half_day CHAR NOT NULL,
	amount DOUBLE NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT user_leaves_deduction_details_pk PRIMARY KEY (user_leave_rec_id,eff_date,end_date),
	CONSTRAINT user_leaves_deduction_details_fk FOREIGN KEY (user_leave_rec_id) REFERENCES smartschoolingdev.user_applicable_leaves(user_leave_rec_id)
);

CREATE TABLE smartschoolingdev.user_salary_details (
	user_id varchar(100) NOT NULL,
	amount DOUBLE NOT NULL,
	eff_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT user_salary_details_pk PRIMARY KEY (user_id,eff_date,end_date),
	CONSTRAINT user_salary_details_fk FOREIGN KEY (user_id) REFERENCES smartschoolingdev.user_basic_details(user_id)
);

CREATE TABLE smartschoolingdev.month_master (
	month_id INT NOT NULL,
	`month` varchar(100) NOT NULL,
	CONSTRAINT month_master_pk PRIMARY KEY (month_id)
);

CREATE TABLE smartschoolingdev.user_payroll_details (
	user_id varchar(100) NOT NULL,
	academic_id varchar(100) NOT NULL,
	payroll_date DATETIME NOT NULL,
	paid_days DOUBLE NOT NULL,
	unpaid_days DOUBLE NOT NULL,
	amount DOUBLE NOT NULL,
	payroll_locked CHAR NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	month_id INT NOT NULL,
	CONSTRAINT user_payroll_details_pk PRIMARY KEY (user_id,academic_id,payroll_date,month_id),
	CONSTRAINT user_payroll_details_fk FOREIGN KEY (academic_id) REFERENCES smartschoolingdev.academic_details(academic_id),
	CONSTRAINT user_payroll_details_fk_1 FOREIGN KEY (user_id) REFERENCES smartschoolingdev.user_basic_details(user_id),
	CONSTRAINT user_payroll_details_fk_2 FOREIGN KEY (month_id) REFERENCES smartschoolingdev.month_master(month_id)
);

CREATE TABLE smartschoolingdev.user_manager_mapping (
	user_id varchar(100) NOT NULL,
	reports_to varchar(100) NOT NULL,
	eff_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT user_manager_mapping_pk PRIMARY KEY (user_id,reports_to,eff_date,end_date),
	CONSTRAINT user_manager_mapping_fk FOREIGN KEY (user_id) REFERENCES smartschoolingdev.user_basic_details(user_id),
	CONSTRAINT user_manager_mapping_fk_1 FOREIGN KEY (reports_to) REFERENCES smartschoolingdev.user_basic_details(user_id)
);

CREATE TABLE smartschoolingdev.user_attendance (
	user_id varchar(100) NOT NULL,
	academic_id varchar(100) NOT NULL,
	attendance_date DATETIME NOT NULL,
	availability CHAR NOT NULL,
	check_in DATETIME NOT NULL,
	check_out DATETIME NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT user_attendance_pk PRIMARY KEY (user_id,attendance_date),
	CONSTRAINT user_attendance_fk FOREIGN KEY (user_id) REFERENCES smartschoolingdev.user_basic_details(user_id),
	CONSTRAINT user_attendance_fk_1 FOREIGN KEY (academic_id) REFERENCES smartschoolingdev.academic_details(academic_id)
);
