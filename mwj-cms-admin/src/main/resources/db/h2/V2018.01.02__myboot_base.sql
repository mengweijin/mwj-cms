SET FOREIGN_KEY_CHECKS=0;
-------------------------------
-- 和 MySQL 脚本的差别为：所有的 text 类型的列，都改为了 varchar，避免程序发生问题。
-------------------------------
-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS sys_config;
CREATE TABLE sys_config (
  id int(4) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  type varchar(64) DEFAULT NULL COMMENT '配置分类',
  name varchar(64) NOT NULL COMMENT '配置名称',
  key_name varchar(64) NOT NULL COMMENT '配置键值属性名称',
  value varchar(64) NOT NULL COMMENT '当前配置的值',
  create_by int(11) DEFAULT NULL COMMENT '创建者',
  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by 		int(11) DEFAULT NULL COMMENT '更新者',
  update_time 	datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY sys_config_key_name_index (key_name) USING BTREE
) COMMENT='系统配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO sys_config VALUES ('1', '用户管理', '用户重置密码时的默认密码', 'defaultUserResetPassword', '123456', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
INSERT INTO sys_config VALUES ('4', 'OCR', 'Windows下工具路径', 'ocrToolWindowsPath', 'D:/Program/Tesseract-OCR/tesseract.exe', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
INSERT INTO sys_config VALUES ('5', 'OCR', 'Linux下工具路径', 'ocrToolLinuxPath', '/usr/share/tesseract/tessdata/', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);


-- ----------------------------
-- Table structure for sys_interface
-- ----------------------------
DROP TABLE IF EXISTS sys_interface;
CREATE TABLE sys_interface (
  id int(4) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  name varchar(64) NOT NULL COMMENT '接口名称',
  key_code varchar(32) NOT NULL COMMENT '接口编码，标识键',
  type int(4) DEFAULT NULL COMMENT '接口类型参考枚举类InterfaceType',
  url VARCHAR(256) DEFAULT NULL COMMENT '请求url,如果是Web Service(cxf)接口类型，则为wsdl地址',
  name_space VARCHAR(128) DEFAULT NULL COMMENT '接口命名空间，可通过访问wsdl地址后的第一个标签中的targetNamespace属性的值获取到',
  method_name VARCHAR(128) DEFAULT NULL COMMENT '要调用的服务端的方法名称，通过wsdl文档获取',
  request_type varchar(8) DEFAULT NULL COMMENT 'HTTP接口时的请求类型（POST, GET）',
  update_by 		int(11) DEFAULT NULL COMMENT '更新者',
  update_time 	datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY sys_interface_key_code_index (key_code) USING BTREE
) COMMENT='接口管理表';

-- ----------------------------
-- Records of sys_interface
-- ----------------------------
INSERT INTO sys_interface VALUES ('1', '根据IP地址获取城市信息', 'ipInfoByTaoBao', '3', 'http://ip.taobao.com/service/getIpInfo.php', null, null, 'POST', '1', '2018-01-01 00:00:00', '根据IP地址获取城市信息的Web Service');
INSERT INTO sys_interface VALUES ('2', '根据城市获取天气信息', 'weather', '2', 'http://www.webxml.com.cn/WebServices/WeatherWebService.asmx', 'http://WebXml.com.cn/', 'getWeatherbyCityName', null, '1', '2018-01-01 00:00:00', '根据城市获取天气信息的Web Service');
INSERT INTO sys_interface VALUES ('3', '获取本机外网IP地址', 'publicIP', '3', 'https://ip.cn/', null, null, 'GET', '1', '2018-01-01 00:00:00', '获取本机外网IP地址');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS sys_file;
CREATE TABLE sys_file (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  group_id varchar(32) DEFAULT NULL COMMENT '组ID',
  storage_name varchar(255) DEFAULT NULL COMMENT '当前存储的文件名',
  original_name varchar(255) DEFAULT NULL COMMENT '原始的文件名',
  file_path varchar(255) DEFAULT NULL COMMENT '文件存放的绝对全路径',
  create_by int(11) DEFAULT NULL COMMENT '创建者',
  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id)
) COMMENT='系统文件存储表';

-- ----------------------------
-- Records of sys_file
-- ----------------------------

DROP TABLE IF EXISTS sys_job_type;
CREATE TABLE sys_job_type (
  id int(4) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  name varchar(64) NOT NULL COMMENT 'JOB类型名称',
  impl_class_name varchar(255) NOT NULL COMMENT '实现类类名',
  status int(4) DEFAULT 0 COMMENT '状态枚举类Status（0正常 1停用）',
  create_by int(11) DEFAULT NULL COMMENT '创建者',
  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by int(11) DEFAULT NULL COMMENT '更新者',
  update_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (id)
) COMMENT='调度Job类型表';

insert into sys_job_type values ('1', '示例Job', 'com.mwj.cms.framework.quartz.task.DemoTask', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- ----------------------------
-- Table structure for sys_job_cron
-- ----------------------------
DROP TABLE IF EXISTS sys_job_cron;
CREATE TABLE sys_job_cron (
  id int(4) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  name varchar(255) NOT NULL COMMENT 'cron表达式展示的名称',
  cron varchar(255) NOT NULL COMMENT 'cron表达式的值',
  order_num int(4) DEFAULT 0 COMMENT '展示的先后顺序',
  status int(4) DEFAULT 0 COMMENT '状态枚举类Status（0正常 1停用）',
  create_by int(11) DEFAULT NULL COMMENT '创建者',
  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY sys_job_cron_index (cron) USING BTREE
) COMMENT='Job执行Cron表达式';

-- ----------------------------
-- Records of sys_job_cron
-- ----------------------------
INSERT INTO sys_job_cron VALUES ('1', '每周周日04:00:00执行一次', '0 0 4 ? * 1 *', '1', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('2', '每周周一04:00:00执行一次', '0 0 4 ? * 2 *', '2', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('3', '每周周二04:00:00执行一次', '0 0 4 ? * 3 *', '3', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('4', '每周周三04:00:00执行一次', '0 0 4 ? * 4 *', '4', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('5', '每周周四04:00:00执行一次', '0 0 4 ? * 5 *', '5', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('6', '每周周五04:00:00执行一次', '0 0 4 ? * 6 *', '6', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('7', '每周周六04:00:00执行一次', '0 0 4 ? * 7 *', '7', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('8', '每周一到周五每天04:00:00执行一次', '0 0 4 ? * 2-6 *', '8', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('9', '每天04:00:00执行一次', '0 0 4 * * ? *', '9', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('10', '每天00:00:00, 12:00:00各执行一次', '0 0 0,12 * * ? *', '10', '1', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('11', '每小时整点执行一次', '0 0 * * * ? *', '11', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('12', '每三十分钟整点执行一次', '0 0,30 * * * ? *', '12', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('13', '每十五分钟整点执行一次', '0 0,15,30,45 * * * ? *', '13', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('14', '每十分钟整点执行一次', '0 0,10,20,30,40,50 * * * ? *', '14', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('15', '每五分钟整点执行一次', '0 0/5 * * * ? *', '15', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('16', '每分钟整点执行一次', '0 * * * * ? *', '16', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('17', '每月1日04:00:00执行一次', '0 0 4 1 * ? *', '17', '0', '1', '2018-01-01 00:00:00');
INSERT INTO sys_job_cron VALUES ('18', '每月最后一日04:00:00执行一次', '0 0 4 L * ? *', '18', '0', '1', '2018-01-01 00:00:00');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS sys_job;
CREATE TABLE sys_job (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  name varchar(64) DEFAULT NULL COMMENT '任务名称',
  cron varchar(256) NOT NULL COMMENT 'cron执行表达式',
  job_type_id int(4) NOT NULL COMMENT '调度Job类型ID',
  job_table_id int(11) DEFAULT NULL COMMENT '业务关联表Id',
  misfire_policy int(4) DEFAULT 0 COMMENT '计划执行错误策略枚举类（0立刻执行（默认） 1放弃执行 2全部执行）',
  status int(4) DEFAULT 0 COMMENT '状态枚举类JobSchedulerStatus（0草稿 1正常 2暂停 3结束）',
  create_by int(11) DEFAULT NULL COMMENT '创建者',
  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by int(11) DEFAULT NULL COMMENT '更新者',
  update_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (id)
) COMMENT='调度信息表';


-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS sys_job_log;
CREATE TABLE sys_job_log (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '调度任务日志ID',
  job_id int(11) NOT NULL COMMENT '任务ID',
  status int(4) DEFAULT 0 COMMENT '状态JobLogStatus（0执行中 1成功 2失败）',
  ip varchar(46) DEFAULT NULL COMMENT 'Job执行主机IP地址',
  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  end_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',
  error_msg varchar(255) DEFAULT NULL COMMENT '错误消息',
  PRIMARY KEY (id)
) COMMENT='调度任务日志表';


-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS sys_log;
CREATE TABLE sys_log (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  title varchar(255) DEFAULT NULL COMMENT '操作模块',
  url varchar(255) DEFAULT NULL COMMENT '请求url',
  req_param varchar(4096) COMMENT '请求参数 JSON格式',
  method varchar(128) DEFAULT NULL COMMENT '请求方法',
  type int(4) DEFAULT NULL COMMENT '日志类型枚举LogType',
  operator varchar(32) DEFAULT NULL COMMENT '操作人员',
  oper_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  oper_ip varchar(46) DEFAULT NULL COMMENT '操作IP地址',
  oper_location varchar(64) DEFAULT NULL COMMENT '操作地点',
  status int(4) DEFAULT 0 COMMENT '日志状态枚举类，ResultStatus, 0正常 1异常',
  error_msg varchar(255) DEFAULT NULL COMMENT '错误消息',
  PRIMARY KEY (id)
) COMMENT='系统操作日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS sys_login_log;
CREATE TABLE sys_login_log (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  login_name varchar(16) DEFAULT NULL COMMENT '登录账号',
  login_type int(4) DEFAULT NULL COMMENT '操作类型,枚举类LoginType',
  ip varchar(46) DEFAULT NULL COMMENT '登录/登出IP地址',
  login_location varchar(64) DEFAULT NULL COMMENT '登录地点',
  browser varchar(50) DEFAULT NULL COMMENT '浏览器类型',
  os varchar(50) DEFAULT NULL COMMENT '操作系统',
  status int(4) DEFAULT 0 COMMENT '登录状态，ResultStatus枚举类（0成功 1失败）',
  operate_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录/登出时间',
  msg varchar(255) DEFAULT NULL COMMENT '提示消息',
  PRIMARY KEY (id)
) COMMENT='系统访问记录表';

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------

drop table if exists sys_dept;
create table sys_dept (
  id int(11) not null auto_increment comment '部门id',
  parent_id int(11) default 0 comment '父部门id',
  name varchar(30) default null comment '部门名称',
  order_num int(4) default 0 comment '显示顺序',
  status int(4) default 0 comment '部门状态, 对应枚举类Status（0正常 1停用）',
  create_by int(11) default null comment '创建者',
  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_by int(11) default null comment '更新者',
  update_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  primary key (id)
) comment = '部门表';

insert into sys_dept values('1', '0', '孟伟进集团', '0', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');

insert into sys_dept values('2', '1', '系统维护部', '0', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_dept values('3', '1', '娱乐圈', '1', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_dept values('11', '1', '北京分公司', '2', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_dept values('12', '1', '上海分公司', '3', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');

insert into sys_dept values('31', '11', '研发部', '0', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_dept values('32', '11', '测试部', '1', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_dept values('33', '11', '运维部', '2', '1', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_dept values('34', '11', '市场部', '3', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_dept values('35', '11', '财务部', '4', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');

insert into sys_dept values('51', '12', '研发部', '0', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_dept values('52', '12', '测试部', '1', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_dept values('53', '12', '运维部', '2', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_dept values('54', '12', '市场部', '3', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_dept values('55', '12', '财务部', '4', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
  id int(4) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  name varchar(30) NOT NULL COMMENT '角色名称',
  sort int(4) NOT NULL COMMENT '显示顺序',
  status int(4) NOT NULL COMMENT '角色状态,对应枚举类Status（0正常 1停用）',
  create_by int(11) DEFAULT null COMMENT '创建者',
  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by int(11) DEFAULT null COMMENT '更新者',
  update_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  remark varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id)
) COMMENT='角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO sys_role VALUES ('1', '管理员', '1', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', '管理员');
INSERT INTO sys_role VALUES ('2', '体验用户', '2', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', '体验用户');
INSERT INTO sys_role VALUES ('3', '普通用户', '3', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', '普通用户');


-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  login_name varchar(64) NOT NULL COMMENT '登录账号',
  nick varchar(64) NOT NULL COMMENT '用户昵称',
  email varchar(64) DEFAULT NULL COMMENT '用户邮箱',
  phone_number varchar(11) DEFAULT NULL COMMENT '手机号码',
  sex int(4) DEFAULT 0 COMMENT '用户性别（0:男 male， 1:女 female）',
  password varchar(128) DEFAULT NULL COMMENT '密码',
  dept_id int(11) DEFAULT 1 COMMENT '部门id',
  status int(4) DEFAULT 0 COMMENT '帐号状态,对应枚举类Status（0正常 1停用）',
  create_by int(11) DEFAULT NULL COMMENT '创建者',
  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by int(11) DEFAULT NULL COMMENT '更新者',
  update_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY sys_user_login_name_index (login_name) USING BTREE
) COMMENT='用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO sys_user VALUES ('1', 'admin', '管理员', 'mengweijin.work@foxmail.com', '18700000000', '0', '$2a$10$1HRbro5LSuyx3QE4eq6NxuXpujE6xCB9Itb.eJkAucneuAogvl9YC', '2', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
INSERT INTO sys_user VALUES ('2', 'mengweijin', '孟伟进', 'mengweijin.work@foxmail.com', '18700000000', '0', '$2a$10$WrvOxzZi561waK9gbI.Wc.a/11xUoEtx.MJqrX8vyDhSZD5tcCNxu', '2', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
INSERT INTO sys_user VALUES ('3', 'huge', '胡歌', 'mengweijin.work@foxmail.com', '18700000000', '0', '$2a$10$ocLVsvlCCcZPbBAe0cjyFugPmaVk/2z2j3AElz0E/c95YoNXQktCC', '3', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
INSERT INTO sys_user VALUES ('4', 'huojianhua', '霍建华', 'mengweijin.work@foxmail.com', '18700000000', '0', '$2a$10$zW1q6OOyd5v.x1J6RdK8Ie3tXYLc2/vvTzWnwTKu4Tc5halmZFFc6', '3', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
INSERT INTO sys_user VALUES ('5', 'zhaoliying', '赵丽颖', 'mengweijin.work@foxmail.com', '18700000000', '1',  '$2a$10$VqqkkCZBi0cL61SnVj/Olu.oscLKi7oJHzVilyxHTeIufAAS7EsPW', '3', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
INSERT INTO sys_user VALUES ('6', 'guodegang', '郭德纲', 'mengweijin.work@foxmail.com', '18700000000', '0', '$2a$10$ZfHbZfl8UgABpKxgtYrmf.X8N/e7ZxN9G7w2DZ.mN8jY3qSRwCSga', '3', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
INSERT INTO sys_user VALUES ('7', 'liudehua', '刘德华', 'mengweijin.work@foxmail.com', '18700000000', '0', '$2a$10$CKo11gDAPlMzP7IDAzD/M.0HxXBr3yvBIMMbAc/tgLVLPsnhE6hve', '3', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
INSERT INTO sys_user VALUES ('8', 'qiaofeng', '乔峰', 'mengweijin.work@foxmail.com', '18700000000', '0', '$2a$10$CFyXqd7BRg2.3UszcwBoGObu6ZtUrIkbgY2CBusc0AFD.lAJqN0Mi', '3', '1', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
INSERT INTO sys_user VALUES ('9', 'yangguo', '杨过', 'mengweijin.work@foxmail.com', '18700000000', '0', '$2a$10$3qVwDKIUK/s3Jj8If3N8KO1.uccza6nQP3Uf3dHYRfhA6z1pQCbSO', '3', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
INSERT INTO sys_user VALUES ('10', 'xiaolongnv', '小龙女', 'mengweijin.work@foxmail.com', '18700000000', '1', '$2a$10$NgI28PYYtDIPjcDripOYv.iDtL8kaN1Z6/HO2a0OMuFqX0yr5VFkK', '3', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');

-- ----------------------------
-- Table structure for sys_user_role_rlt
-- ----------------------------
DROP TABLE IF EXISTS sys_user_role_rlt;
CREATE TABLE sys_user_role_rlt (
  user_id int(11) NOT NULL COMMENT '用户ID',
  role_id int(4) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (user_id, role_id)
) COMMENT='用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role_rlt
-- ----------------------------
insert into sys_user_role_rlt values('2', '2');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  id 			      int(11) 		  not null    auto_increment      comment '菜单ID',
  name 		      varchar(50) 	not null 				                comment '菜单名称',
  parent_id 		int(11) 		  default 0 			                comment '父菜单ID',
  order_num 		int(4) 			  default 0 			                comment '显示顺序',
  url 				  varchar(256) default 'javascript:;'		comment '请求地址',
  menu_type 		int(4)  			default 0            comment '菜单类型，对应枚举类MenuType（0目录 1菜单 2按钮）',
  status 			  int(4) 		  default 0 				                comment '菜单状态,对应枚举类Status（0正常 1停用）',
  permission 	  varchar(64) 	not null				            comment '权限标识,唯一性约束',
  icon 				  varchar(64) 	default null	                  comment '菜单图标',
  create_by     int(11)  default null                   comment '创建者',
  create_time 	datetime NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_by 		int(11) 	default null			              comment '更新者',
  update_time 	datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  remark 			  varchar(500) 	default null 				            comment '备注',
  primary key (id),
  UNIQUE KEY sys_menu_permission_index (permission) USING BTREE
) comment = '菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
insert into sys_menu values ('-1', 'SpringBoot监控接口', '0', '0', '/actuator', '1', '0', 'actuator', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
-- 水平菜单栏

insert into sys_menu values ('-2', '水平菜单栏', '0', '1', 'javascript:;', '1', '0', 'horizontal_menu_bar', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('-6', '数据源监控', '-2', '1', '/druid/index.html', '1', '0', 'druid', 'fas fa-database', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('-7', '接口文档', '-2', '2', '/swagger-ui.html', '1', '0', 'swagger', 'layui-icon layui-icon-read', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 一级菜单权限 侧边栏
insert into sys_menu values ('-3', '侧边菜单栏', '0', '1', 'javascript:;', '1', '0', 'side_bar_menu', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('1', '系统管理', '-3', '1', 'javascript:;', '0', '0', 'system_manager', 'fas fa-cog', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('2', '日志管理', '-3', '2', 'javascript:;', '0', '0', 'log_manager', 'fas fa-file-alt', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('3', '系统监控', '-3', '3', 'javascript:;', '0', '0', 'system_monitor', 'fas fa-video', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('4', '通知公告', '-3', '4', '/sys/notice/index', '1', '0', 'system_notice_index', 'fas fa-bell', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 二级菜单权限 侧边栏
insert into sys_menu values ('51', '部门管理', '1', '1', '/sys/dept/index', '1', '0', 'system_dept_index', 'fas fa-layer-group', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('52', '用户管理', '1', '2', '/sys/user/index', '1', '0', 'system_user_index', 'fas fa-user', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('53', '角色管理', '1', '3', '/sys/role/index', '1', '0', 'system_role_index', 'fas fa-user-tie', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('54', '菜单管理', '1', '4', '/sys/menu/index', '1', '0', 'system_menu_index', 'fa fa-bars', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('55', '配置管理', '1', '5', '/sys/config/index', '1', '0', 'system_config_index', 'fa fa-wrench', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('56', '接口管理', '1', '6', '/sys/interface/index', '1', '0', 'system_interface_index', 'fas fa-globe', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('57', '文件管理', '1', '6', '/sys/file/index', '1', '0', 'system_file_index', 'fas fa-folder', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

insert into sys_menu values ('61', '操作日志', '2', '1', '/sys/log/index', '1', '0', 'system_log_index', 'fas fa-file-alt', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('62', '登录日志', '2', '2', '/sys/login-log/index', '1', '0', 'system_loginLog_index', 'fas fa-file-alt', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

insert into sys_menu values ('65', '调度管理', '3', '1', '/sys/job/index', '1', '0', 'system_job_index', 'fas fa-clock', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('66', '在线用户', '3', '2', '/sys/user-online/index', '1', '0', 'system_userOnline_index', 'fas fa-user-clock', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 部门管理按钮权限
insert into sys_menu values ('101', '部门新增', '51', '1', null, '2', '0', 'system_dept_insert', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('102', '部门删除', '51', '2', null, '2', '0', 'system_dept_delete', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('103', '部门编辑', '51', '3', null, '2', '0', 'system_dept_update', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('104', '部门启用/停用转换', '51', '4', null, '2', '0', 'system_dept_switchStatus', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 用户管理按钮权限
insert into sys_menu values ('111', '用户新增', '52', '1', null, '2', '0', 'system_user_insert', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('112', '用户编辑', '52', '2', null, '2', '0', 'system_user_update', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('113', '重置密码', '52', '3', null, '2', '0', 'system_user_resetPassword', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('114', '用户启用/停用转换', '52', '4', null, '2', '0', 'system_user_switchStatus', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('115', '修改密码', '52', '5', null, '2', '0', 'system_user_updatePassword', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 角色管理按钮权限
insert into sys_menu values ('121', '角色新增', '53', '1', null, '2', '0', 'system_role_insert', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('122', '角色删除', '53', '2', null, '2', '0', 'system_role_delete', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('123', '角色编辑', '53', '3', null, '2', '0', 'system_role_update', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('124', '角色启用/停用转换', '53', '4', null, '2', '0', 'system_role_switchStatus', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('125', '角色授权', '53', '5', null, '2', '0', 'system_role_authorization', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 菜单权限管理 按钮权限
insert into sys_menu values ('131', '菜单权限编辑', '54', '1', null, '2', '0', 'system_menu_update', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('132', '菜单权限启用/停用转换', '54', '2', null, '2', '0', 'system_menu_switchStatus', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 配置管理按钮权限
insert into sys_menu values ('141', '配置编辑', '55', '1', null, '2', '0', 'system_config_update', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 接口管理按钮权限
insert into sys_menu values ('151', '接口编辑', '56', '1', null, '2', '0', 'system_interface_update', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 文件管理按钮权限
insert into sys_menu values ('161', '文件删除', '57', '1', null, '2', '0', 'system_file_delete', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 操作日志 按钮权限
insert into sys_menu values ('171', '操作日志批量删除', '61', '1', null, '2', '0', 'system_syslog_batchDelete', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 登录日志 按钮权限

-- 调度管理 按钮权限
insert into sys_menu values ('191', 'Job新增', '65', '1', null, '2', '0', 'system_job_insert', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('192', 'Job删除', '65', '2', null, '2', '0', 'system_job_delete', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('193', 'Job编辑', '65', '3', null, '2', '0', 'system_job_update', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('194', 'Job发布', '65', '4', null, '2', '0', 'system_job_release', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('195', 'Job暂停', '65', '5', null, '2', '0', 'system_job_pause', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('196', 'Job恢复', '65', '6', null, '2', '0', 'system_job_resume', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('197', 'Job结束', '65', '7', null, '2', '0', 'system_job_finish', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('198', 'Job执行日志', '65', '8', null, '2', '0', 'system_job_log', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 在线用户 按钮权限
insert into sys_menu values ('201', '强制下线', '66', '1', null, '2', '0', 'system_userOnline_invalidateSession', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- 通知公告 按钮权限
insert into sys_menu values ('211', '通知公告新增', '4', '1', null, '2', '0', 'system_notice_insert', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('212', '通知公告删除', '4', '2', null, '2', '0', 'system_notice_delete', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('213', '通知公告编辑', '4', '3', null, '2', '0', 'system_notice_update', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);
insert into sys_menu values ('214', '通知公告启用/停用转换', '4', '4', null, '2', '0', 'system_notice_switchStatus', null, '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00', null);

-- ----------------------------
-- Table structure for sys_role_menu_rlt
-- ----------------------------
drop table if exists sys_role_menu_rlt;
create table sys_role_menu_rlt (
  role_id 	int(11) not null comment '角色ID',
  menu_id 	int(11) not null comment '菜单ID',
  primary key(role_id, menu_id)
) comment = '角色和菜单关联表';

-- ----------------------------
-- Records of sys_role_menu_rlt
-- ----------------------------
INSERT INTO sys_role_menu_rlt VALUES ('1', '-7');
INSERT INTO sys_role_menu_rlt VALUES ('1', '-6');
INSERT INTO sys_role_menu_rlt VALUES ('1', '-3');
INSERT INTO sys_role_menu_rlt VALUES ('1', '-2');
INSERT INTO sys_role_menu_rlt VALUES ('1', '-1');
INSERT INTO sys_role_menu_rlt VALUES ('1', '1');
INSERT INTO sys_role_menu_rlt VALUES ('1', '2');
INSERT INTO sys_role_menu_rlt VALUES ('1', '3');
INSERT INTO sys_role_menu_rlt VALUES ('1', '4');
INSERT INTO sys_role_menu_rlt VALUES ('1', '51');
INSERT INTO sys_role_menu_rlt VALUES ('1', '52');
INSERT INTO sys_role_menu_rlt VALUES ('1', '53');
INSERT INTO sys_role_menu_rlt VALUES ('1', '54');
INSERT INTO sys_role_menu_rlt VALUES ('1', '55');
INSERT INTO sys_role_menu_rlt VALUES ('1', '56');
INSERT INTO sys_role_menu_rlt VALUES ('1', '57');
INSERT INTO sys_role_menu_rlt VALUES ('1', '61');
INSERT INTO sys_role_menu_rlt VALUES ('1', '62');
INSERT INTO sys_role_menu_rlt VALUES ('1', '65');
INSERT INTO sys_role_menu_rlt VALUES ('1', '66');
INSERT INTO sys_role_menu_rlt VALUES ('1', '101');
INSERT INTO sys_role_menu_rlt VALUES ('1', '102');
INSERT INTO sys_role_menu_rlt VALUES ('1', '103');
INSERT INTO sys_role_menu_rlt VALUES ('1', '104');
INSERT INTO sys_role_menu_rlt VALUES ('1', '111');
INSERT INTO sys_role_menu_rlt VALUES ('1', '112');
INSERT INTO sys_role_menu_rlt VALUES ('1', '113');
INSERT INTO sys_role_menu_rlt VALUES ('1', '114');
INSERT INTO sys_role_menu_rlt VALUES ('1', '115');
INSERT INTO sys_role_menu_rlt VALUES ('1', '121');
INSERT INTO sys_role_menu_rlt VALUES ('1', '122');
INSERT INTO sys_role_menu_rlt VALUES ('1', '123');
INSERT INTO sys_role_menu_rlt VALUES ('1', '124');
INSERT INTO sys_role_menu_rlt VALUES ('1', '125');
INSERT INTO sys_role_menu_rlt VALUES ('1', '131');
INSERT INTO sys_role_menu_rlt VALUES ('1', '132');
INSERT INTO sys_role_menu_rlt VALUES ('1', '141');
INSERT INTO sys_role_menu_rlt VALUES ('1', '151');
INSERT INTO sys_role_menu_rlt VALUES ('1', '161');
INSERT INTO sys_role_menu_rlt VALUES ('1', '171');
INSERT INTO sys_role_menu_rlt VALUES ('1', '191');
INSERT INTO sys_role_menu_rlt VALUES ('1', '192');
INSERT INTO sys_role_menu_rlt VALUES ('1', '193');
INSERT INTO sys_role_menu_rlt VALUES ('1', '194');
INSERT INTO sys_role_menu_rlt VALUES ('1', '195');
INSERT INTO sys_role_menu_rlt VALUES ('1', '196');
INSERT INTO sys_role_menu_rlt VALUES ('1', '197');
INSERT INTO sys_role_menu_rlt VALUES ('1', '198');
INSERT INTO sys_role_menu_rlt VALUES ('1', '201');
INSERT INTO sys_role_menu_rlt VALUES ('1', '211');
INSERT INTO sys_role_menu_rlt VALUES ('1', '212');
INSERT INTO sys_role_menu_rlt VALUES ('1', '213');
INSERT INTO sys_role_menu_rlt VALUES ('1', '214');
INSERT INTO sys_role_menu_rlt VALUES ('2', '-7');
INSERT INTO sys_role_menu_rlt VALUES ('2', '-6');
INSERT INTO sys_role_menu_rlt VALUES ('2', '-3');
INSERT INTO sys_role_menu_rlt VALUES ('2', '-2');
INSERT INTO sys_role_menu_rlt VALUES ('2', '-1');
INSERT INTO sys_role_menu_rlt VALUES ('2', '1');
INSERT INTO sys_role_menu_rlt VALUES ('2', '2');
INSERT INTO sys_role_menu_rlt VALUES ('2', '3');
INSERT INTO sys_role_menu_rlt VALUES ('2', '4');
INSERT INTO sys_role_menu_rlt VALUES ('2', '51');
INSERT INTO sys_role_menu_rlt VALUES ('2', '52');
INSERT INTO sys_role_menu_rlt VALUES ('2', '53');
INSERT INTO sys_role_menu_rlt VALUES ('2', '54');
INSERT INTO sys_role_menu_rlt VALUES ('2', '55');
INSERT INTO sys_role_menu_rlt VALUES ('2', '56');
INSERT INTO sys_role_menu_rlt VALUES ('2', '57');
INSERT INTO sys_role_menu_rlt VALUES ('2', '61');
INSERT INTO sys_role_menu_rlt VALUES ('2', '62');
INSERT INTO sys_role_menu_rlt VALUES ('2', '65');
INSERT INTO sys_role_menu_rlt VALUES ('2', '66');
INSERT INTO sys_role_menu_rlt VALUES ('2', '101');
INSERT INTO sys_role_menu_rlt VALUES ('2', '111');
INSERT INTO sys_role_menu_rlt VALUES ('2', '121');
INSERT INTO sys_role_menu_rlt VALUES ('2', '191');
INSERT INTO sys_role_menu_rlt VALUES ('2', '193');
INSERT INTO sys_role_menu_rlt VALUES ('2', '194');
INSERT INTO sys_role_menu_rlt VALUES ('2', '195');
INSERT INTO sys_role_menu_rlt VALUES ('2', '196');
INSERT INTO sys_role_menu_rlt VALUES ('2', '197');
INSERT INTO sys_role_menu_rlt VALUES ('2', '198');

-- ----------------------------
-- 通知公告表
-- ----------------------------
drop table if exists sys_notice;
create table sys_notice (
  id 		int(4) 		    not null auto_increment    comment 'ID',
  title 		varchar(50) 	not null 	comment '标题',
  content    varchar(4096)         comment '内容',
  status 			int(4) 		default 0 		comment '状态Status枚举（0正常 1关闭）',
  create_by     int(11)  default null                   comment '创建者',
  create_time 	datetime NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_by 		int(11) 	default null			              comment '更新者',
  update_time 	datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  primary key (id)
) comment = '通知公告表';

-- ----------------------------
-- 通知公告表 初始数据
-- ----------------------------
insert into sys_notice values ('1', 'mwj-cms初始版本发布', 'mwj-cms初始版本发布！', '0', '1', '2018-01-01 00:00:00', '1', '2018-01-01 00:00:00');
insert into sys_notice values ('2', '欢迎来到mwj-cms后台管理系统', '欢迎来到mwj-cms后台管理系统，您的到来让本站蓬荜生辉！', '0', '1', '2018-01-01 00:00:01', '1', '2018-01-01 00:00:00');
