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

CREATE TABLE smartschoolingdev.users (
	user_type varchar(100) NOT NULL,
	user_role varchar(100) NOT NULL,
	user_description varchar(100) NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (user_type)
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

CREATE TABLE smartschoolingdev.students_fees_collections_details (
	collection_id varchar(100) NOT NULL,
	stud_id varchar(100) NOT NULL,
	academic_id varchar(100) NOT NULL,
	fee_id varchar(100) NOT NULL,
	amount DOUBLE NOT NULL,
	collection_date DATETIME NOT NULL,
	account_id VARCHAR(100) NOT NULL,
	last_update_time DATETIME NOT NULL,
	last_user varchar(100) NOT NULL,
	CONSTRAINT students_fees_collections_details_pk PRIMARY KEY (collection_id),
	CONSTRAINT students_fees_collections_details_fk FOREIGN KEY (stud_id) REFERENCES smartschoolingdev.student_details(stud_id),
	CONSTRAINT students_fees_collections_details_fk_1 FOREIGN KEY (academic_id) REFERENCES smartschoolingdev.academic_details(academic_id),
	CONSTRAINT students_fees_collections_details_fk_2 FOREIGN KEY (fee_id) REFERENCES smartschoolingdev.fee_types(fee_id),
	CONSTRAINT students_fees_collections_details_fk_3 FOREIGN KEY (account_id) REFERENCES smartschoolingdev.accounts(account_id)
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
