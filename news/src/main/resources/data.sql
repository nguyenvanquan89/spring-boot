insert into role(code,name,createdBy,createdDate,modifiedBy,modifiedDate) values('ADMIN','Quản trị','Quân','2023-09-18 23:18:44','Quân','2023-09-18 23:18:44');
insert into role(code,name,createdBy,createdDate,modifiedBy,modifiedDate) values('EDITOR','Người chỉnh sửa','Quân','2023-09-18 23:18:44','Quân','2023-09-18 23:18:44');
insert into role(code,name,createdBy,createdDate,modifiedBy,modifiedDate) values('AUTHOR','Tác giả','Quân','2023-09-18 23:18:44','Quân','2023-09-18 23:18:44');
insert into role(code,name,createdBy,createdDate,modifiedBy,modifiedDate) values('USER','Người dùng','Quân','2023-09-18 23:18:44','Quân','2023-09-18 23:18:44');


insert into user(userName,passWord,fullName,status,createdBy,createdDate,modifiedBy,modifiedDate)
values('admin@gmail.com','$2a$10$/RUbuT9KIqk6f8enaTQiLOXzhnUkiwEJRdtzdrMXXwU7dgnLKTCYG','Nguyễn Văn Quân',1,'Quân','2023-09-18 23:18:44','Quân','2023-09-18 23:18:44');
insert into user(userName,passWord,fullName,status,createdBy,createdDate,modifiedBy,modifiedDate)
values('nguyenvana@gmail.com','$2a$10$/RUbuT9KIqk6f8enaTQiLOXzhnUkiwEJRdtzdrMXXwU7dgnLKTCYG','Nguyễn Văn A',1,'Quân','2023-09-18 23:18:44','Quân','2023-09-18 23:18:44');
insert into user(userName,passWord,fullName,status,createdBy,createdDate,modifiedBy,modifiedDate)
values('nguyenvanb@gmail.com','$2a$10$/RUbuT9KIqk6f8enaTQiLOXzhnUkiwEJRdtzdrMXXwU7dgnLKTCYG','Nguyễn Văn B',1,'Quân','2023-09-18 23:18:44','Quân','2023-09-18 23:18:44');
insert into user(userName,passWord,fullName,status,createdBy,createdDate,modifiedBy,modifiedDate)
values('nguyenvanc@gmail.com','$2a$10$/RUbuT9KIqk6f8enaTQiLOXzhnUkiwEJRdtzdrMXXwU7dgnLKTCYG','Nguyễn Văn C',1,'Quân','2023-09-18 23:18:44','Quân','2023-09-18 23:18:44');

INSERT INTO user_role(userId,roleId) VALUES (1,1);
INSERT INTO user_role(userId,roleId) VALUES (2,2);
INSERT INTO user_role(userId,roleId) VALUES (3,3);
INSERT INTO user_role(userId,roleId) VALUES (4,4);

INSERT INTO category (createdBy, createdDate, modifiedBy, modifiedDate, code, name) VALUES ('Quân', '2023-10-07 14:54:13', 'Quân', '2023-10-07 15:54:13', 'the-thao', 'Thể Thao');
INSERT INTO category (createdBy, createdDate, modifiedBy, modifiedDate, code, name) VALUES ('Quân', '2023-10-07 14:54:13', 'Quân', '2023-10-07 15:54:13', 'chinh-tri', 'Chính Trị');
INSERT INTO category (createdBy, createdDate, modifiedBy, modifiedDate, code, name) VALUES ('Quân', '2023-10-07 14:54:13', 'Quân', '2023-10-07 15:54:13', 'thoi-su', 'Thời Sự');
INSERT INTO category (createdBy, createdDate, modifiedBy, modifiedDate, code, name) VALUES ('Quân', '2023-10-07 14:54:13', 'Quân', '2023-10-07 15:54:13', 'kinh-doanh', 'Kinh Doanh');
INSERT INTO category (createdBy, createdDate, modifiedBy, modifiedDate, code, name) VALUES ('Quân', '2023-10-07 14:54:13', 'Quân', '2023-10-07 15:54:13', 'giao-duc', 'Giáo Dục');
INSERT INTO category (createdBy, createdDate, modifiedBy, modifiedDate, code, name) VALUES ('Quân', '2023-10-07 14:54:13', 'Quân', '2023-10-07 15:54:13', 'bat-dong-san', 'Bất Động Sản');
INSERT INTO category (createdBy, createdDate, modifiedBy, modifiedDate, code, name) VALUES ('Quân', '2023-10-07 14:54:13', 'Quân', '2023-10-07 15:54:13', 'khoa-hoc', 'Khoa Học');
INSERT INTO category (createdBy, createdDate, modifiedBy, modifiedDate, code, name) VALUES ('Quân', '2023-10-07 14:54:13', 'Quân', '2023-10-07 15:54:13', 'giai-tri', 'Giải Trí');