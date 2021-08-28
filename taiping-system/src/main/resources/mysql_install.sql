-- taiping mysql 初始化脚本

-- 创建数据库
drop database if exists taiping;
CREATE  DATABASE  taiping;
CREATE DATABASE IF NOT EXISTS taiping default charset utf8 COLLATE utf8_general_ci;
USE taiping;



-- 建表


SET FOREIGN_KEY_CHECKS=0;


-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_BLOB_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of QRTZ_CALENDARS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_FIRED_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------
INSERT INTO `QRTZ_LOCKS` VALUES ('taiping-schedle', 'STATE_ACCESS');
INSERT INTO `QRTZ_LOCKS` VALUES ('taiping-schedle', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------
INSERT INTO `QRTZ_SCHEDULER_STATE` VALUES ('taiping-schedle', 'lx-PC1571105971365', '1571133789737', '7500');

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_SIMPLE_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_SIMPROP_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_TRIGGERS
-- ----------------------------


-- ----------------------------
-- Table structure for t_annex
-- ----------------------------
DROP TABLE IF EXISTS `t_annex`;
CREATE TABLE `t_annex` (
  `annex_id` varchar(50) NOT NULL,
  `annex_name` varchar(50) DEFAULT NULL COMMENT '附件名称',
  `manage_id` varchar(50) DEFAULT NULL COMMENT '关联id',
  `mode` varchar(10) DEFAULT NULL COMMENT '所属模块',
  `annex_path` varchar(255) DEFAULT NULL COMMENT '附件文件下载路径',
  `upload_time` bigint(20) DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`annex_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_change
-- ----------------------------
DROP TABLE IF EXISTS `t_change`;
CREATE TABLE `t_change` (
  `change_id` varchar(50) NOT NULL COMMENT 'ID主键',
  `change_code` varchar(32) DEFAULT NULL COMMENT '变更单号',
  `change_name` varchar(32) DEFAULT NULL COMMENT '项目名称',
  `change_type` varchar(32) DEFAULT NULL COMMENT '变更单类型',
  `people` int(11) DEFAULT NULL COMMENT '人数',
  `workload` int(11) DEFAULT NULL COMMENT '设备U数',
  `start_date` bigint(20) DEFAULT NULL COMMENT '开始日期',
  `end_date` bigint(20) DEFAULT NULL COMMENT '结束日期',
  `handover_people` varchar(255) DEFAULT NULL COMMENT '历史交接人',
  `project_status` varchar(255) DEFAULT NULL COMMENT '项目状态',
  PRIMARY KEY (`change_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_change_people
-- ----------------------------
DROP TABLE IF EXISTS `t_change_people`;
CREATE TABLE `t_change_people` (
  `id` varchar(50) NOT NULL COMMENT 'ID主键',
  `change_id` varchar(50) DEFAULT NULL COMMENT '变更单ID主键',
  `workload` decimal(11,6) DEFAULT NULL COMMENT '单人U数',
  `people` varchar(32) DEFAULT NULL COMMENT '交接人',
  `start_date` bigint(20) DEFAULT NULL COMMENT '开始日期',
  `end_date` bigint(20) DEFAULT NULL COMMENT '结束日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_change_plan
-- ----------------------------
DROP TABLE IF EXISTS `t_change_plan`;
CREATE TABLE `t_change_plan` (
  `change_id` varchar(50) NOT NULL COMMENT '主键ID',
  `report_person` varchar(32) DEFAULT NULL COMMENT '项目提交人',
  `project_name` varchar(32) DEFAULT NULL COMMENT '项目名称',
  `device_type` int(11) DEFAULT NULL COMMENT '设备型号（U）',
  `device_number` int(11) DEFAULT NULL COMMENT '设备数量',
  `expect_start_time` bigint(20) DEFAULT NULL COMMENT '机房预计上架日期',
  `expect_end_time` bigint(20) DEFAULT NULL COMMENT '机房预计完成日期',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_deleted` varchar(1) DEFAULT '0',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`change_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_daily_workload
-- ----------------------------
DROP TABLE IF EXISTS `t_daily_workload`;
CREATE TABLE `t_daily_workload` (
  `workload_id` varchar(50) NOT NULL COMMENT '主键ID',
  `change_date` bigint(20) DEFAULT NULL COMMENT '日期',
  `project_code` varchar(50) DEFAULT NULL COMMENT 'DMC单号',
  `project_name` varchar(50) DEFAULT NULL COMMENT '项目名称',
  `total_workload` int(11) DEFAULT NULL COMMENT '项目总工作量',
  `daily_workload` decimal(10,6) DEFAULT NULL COMMENT '当日完成工作量',
  `work_status` varchar(1) DEFAULT NULL COMMENT '资源状态',
  `work_status_value` decimal(10,6) DEFAULT NULL COMMENT '资源状态值',
  `project_status` varchar(50) DEFAULT NULL COMMENT '项目状态',
  PRIMARY KEY (`workload_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_flash_off
-- ----------------------------
DROP TABLE IF EXISTS `t_flash_off`;
CREATE TABLE `t_flash_off` (
  `id` varchar(50) NOT NULL COMMENT 'id',
  `off_type` varchar(2) DEFAULT NULL COMMENT '闪断类型',
  `off_date` bigint(20) DEFAULT NULL COMMENT '日期',
  `cause` varchar(50) DEFAULT NULL COMMENT '原因(停水原因/停电原因)',
  `influence` varchar(50) DEFAULT NULL COMMENT '造成影响',
  `start_time` varchar(20) DEFAULT NULL COMMENT '停水时间/停电时间',
  `end_time` varchar(20) DEFAULT NULL COMMENT '来水时间/来电时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_flash_off_trend
-- ----------------------------
DROP TABLE IF EXISTS `t_flash_off_trend`;
CREATE TABLE `t_flash_off_trend` (
  `trend_id` varchar(50) NOT NULL COMMENT '主键',
  `trend_date` bigint(20) DEFAULT NULL COMMENT '统计日期',
  `off_type` varchar(10) DEFAULT NULL COMMENT '闪断类型',
  `off_number` int(11) DEFAULT NULL COMMENT '闪断次数',
  `report_date` bigint(20) DEFAULT NULL COMMENT '分析月份',
  PRIMARY KEY (`trend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_healthy_param
-- ----------------------------
DROP TABLE IF EXISTS `t_healthy_param`;
CREATE TABLE `t_healthy_param` (
  `param_id` varchar(50) NOT NULL COMMENT '参数ID',
  `param_type` varchar(2) DEFAULT NULL COMMENT '参数类别',
  `param_value` varchar(255) DEFAULT NULL COMMENT '参数值',
  `standard_value` varchar(255) DEFAULT NULL COMMENT '参数标准值',
  PRIMARY KEY (`param_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_inspection_task
-- ----------------------------
DROP TABLE IF EXISTS `t_inspection_task`;
CREATE TABLE `t_inspection_task` (
  `inspection_id` varchar(50) NOT NULL COMMENT 'id',
  `inspection_code` varchar(50) DEFAULT NULL COMMENT '巡检工单号',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `status` varchar(20) DEFAULT NULL COMMENT '状态',
  `task_name` varchar(20) DEFAULT NULL COMMENT '任务名称',
  `location` varchar(50) DEFAULT NULL COMMENT '区域位置',
  `frequency` varchar(20) DEFAULT NULL COMMENT '频率',
  `inspection_group` varchar(20) DEFAULT NULL COMMENT '巡检组',
  `executor` varchar(20) DEFAULT NULL COMMENT '执行人',
  `plan_start_time` varchar(50) DEFAULT NULL COMMENT '计划开始时间',
  `plan_end_time` varchar(50) DEFAULT NULL COMMENT '计划结束时间',
  `allow_lead_time` varchar(50) DEFAULT NULL COMMENT '允许提前时间',
  `allow_delay_time` varchar(50) DEFAULT NULL COMMENT '允许延迟时间',
  `plan_use_time` varchar(10) DEFAULT NULL COMMENT '计划用时',
  `actual_use_time` varchar(10) DEFAULT NULL COMMENT '实际用时',
  `actual_start_time` varchar(50) DEFAULT NULL COMMENT '实际开始时间',
  `actual_end_time` bigint(50) DEFAULT NULL COMMENT '实际结束时间',
  PRIMARY KEY (`inspection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_manage_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_manage_activity`;
CREATE TABLE `t_manage_activity` (
  `manage_id` varchar(50) NOT NULL COMMENT '主键id',
  `manage_type` varchar(1) DEFAULT NULL COMMENT '类型',
  `source_name` varchar(50) DEFAULT NULL COMMENT '来源对象名称',
  `source_code` varchar(50) DEFAULT NULL COMMENT '来源对象code',
  `source_mode` varchar(50) DEFAULT NULL COMMENT '来源模块',
  `cause` varchar(255) DEFAULT NULL COMMENT '产生原因',
  `create_date` bigint(20) DEFAULT NULL COMMENT '产生日期',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `responsible_id` varchar(50) DEFAULT NULL COMMENT '负责人ID',
  `responsible_person` varchar(20) DEFAULT NULL COMMENT '负责人',
  `completion_time` bigint(20) DEFAULT NULL COMMENT '预计完成时间',
  `is_remind` varchar(1) DEFAULT NULL COMMENT '是否提醒',
  `activity_type` varchar(10) DEFAULT NULL COMMENT '运维管理活动类型',
  `solve_instruction` varchar(255) DEFAULT NULL COMMENT '处理说明',
  `advise` varchar(255) DEFAULT NULL COMMENT '建议',
  `completion_status` varchar(1) DEFAULT NULL COMMENT '完成状态',
  `reviewer_id` varchar(50) DEFAULT NULL COMMENT '复核人id',
  `reviewer` varchar(20) DEFAULT NULL COMMENT '复核人',
  `approval_status` varchar(1) DEFAULT NULL COMMENT '复核状态',
  `review_instruction` varchar(255) DEFAULT NULL COMMENT '复核说明',
  `is_reduce` varchar(1) DEFAULT NULL COMMENT '是否可以裁剪',
  PRIMARY KEY (`manage_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_month_workload
-- ----------------------------
DROP TABLE IF EXISTS `t_month_workload`;
CREATE TABLE `t_month_workload` (
  `workload_id` varchar(50) NOT NULL COMMENT '主键ID',
  `value_type` varchar(1) DEFAULT NULL COMMENT '类型（预测或实际',
  `workload_date` bigint(20) DEFAULT NULL COMMENT '日期',
  `workload_type` varchar(1) DEFAULT NULL COMMENT '负荷类型',
  `workload_percentage` varchar(10) DEFAULT NULL COMMENT '负荷比',
  `month_percentage` varchar(10) DEFAULT NULL COMMENT '环比',
  `year_percentage` varchar(10) DEFAULT NULL COMMENT '同比',
  `manage_id` varchar(50) DEFAULT NULL COMMENT '关联运维管理活动ID',
  `source_name` varchar(50) DEFAULT NULL COMMENT '来源对象名称',
  `source_code` varchar(50) DEFAULT NULL COMMENT '来源对象code',
  `cause` varchar(255) DEFAULT NULL COMMENT '产生原因',
  `solve_instruction` varchar(255) DEFAULT NULL COMMENT '处理说明',
  `activity_type` varchar(10) DEFAULT NULL COMMENT '运维管理活动类型',
  `report_type` varchar(1) DEFAULT '' COMMENT '报告类型',
  PRIMARY KEY (`workload_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_param_manage
-- ----------------------------
DROP TABLE IF EXISTS `t_param_manage`;
CREATE TABLE `t_param_manage` (
  `param_id` varchar(50) NOT NULL COMMENT '主键ID',
  `mode` varchar(10) DEFAULT NULL COMMENT '模块',
  `param_type` varchar(10) DEFAULT NULL COMMENT '类型',
  `near_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`param_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;


-- ----------------------------
-- Records of t_param_manage
-- ----------------------------
INSERT INTO `t_param_manage` VALUES ('1', '1001', '0', null);
INSERT INTO `t_param_manage` VALUES ('2', '1002', '0', null);
INSERT INTO `t_param_manage` VALUES ('3', '1003', '0', null);
INSERT INTO `t_param_manage` VALUES ('4', '1004', '0', null);
INSERT INTO `t_param_manage` VALUES ('5', '1006', '0', null);

-- ----------------------------
-- Table structure for t_performance
-- ----------------------------
DROP TABLE IF EXISTS `t_performance`;
CREATE TABLE `t_performance` (
  `performance_id` varchar(50) NOT NULL COMMENT '主键id',
  `person_name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `work_date` bigint(20) DEFAULT NULL COMMENT '月份',
  `day_number` int(11) DEFAULT NULL COMMENT '上班天数（常白班）',
  `day_night_number` int(11) DEFAULT NULL COMMENT '上班天数（倒班）',
  `inspection_number` int(11) DEFAULT NULL COMMENT '巡检次数',
  `workload` decimal(10,6) DEFAULT NULL COMMENT '上架工作量（U）',
  `work_efficiency` decimal(10,6) DEFAULT NULL COMMENT '上架效率（U/小时）',
  PRIMARY KEY (`performance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_problem_statistics
-- ----------------------------
DROP TABLE IF EXISTS `t_problem_statistics`;
CREATE TABLE `t_problem_statistics` (
  `statistics_id` varchar(50) NOT NULL COMMENT '主键',
  `statistics_type` varchar(10) DEFAULT NULL COMMENT '统计类别',
  `value_type` varchar(255) DEFAULT NULL COMMENT '值',
  `value_number` int(11) DEFAULT NULL COMMENT '数量',
  `month_percentage` varchar(10) DEFAULT NULL COMMENT '环比',
  `year_percentage` varchar(10) DEFAULT NULL COMMENT '同比',
  `value_date` bigint(20) DEFAULT NULL COMMENT ' 统计日期',
  `instruction` varchar(255) DEFAULT NULL COMMENT '同比环比报告',
  `manage_id` varchar(50) DEFAULT NULL COMMENT '关联运维管理活动ID',
  `source_name` varchar(50) DEFAULT NULL COMMENT '来源对象名称',
  `source_code` varchar(50) DEFAULT NULL COMMENT '来源对象code',
  `cause` varchar(50) DEFAULT NULL COMMENT ' 产生原因',
  `solve_instruction` varchar(255) DEFAULT NULL COMMENT '处理说明',
  `activity_type` varchar(10) DEFAULT NULL COMMENT '运维管理活动类型',
  `report_type` varchar(10) DEFAULT NULL COMMENT '报告类型',
  PRIMARY KEY (`statistics_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_schedule_info
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_info`;
CREATE TABLE `t_schedule_info` (
  `schedule_id` varchar(50) NOT NULL COMMENT 'id',
  `schedule_date` bigint(20) DEFAULT NULL COMMENT '排班日期',
  `schedule_type` varchar(1) DEFAULT NULL COMMENT '班次类别',
  `schedule_name` varchar(10) DEFAULT NULL COMMENT '班次名称',
  `start_time` varchar(10) DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(10) DEFAULT NULL COMMENT '结束时间',
  `duty_officer` varchar(10) DEFAULT NULL COMMENT '值班人员',
  `special_description` varchar(255) DEFAULT NULL COMMENT '特殊情况说明',
  PRIMARY KEY (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_trouble_ticket
-- ----------------------------
DROP TABLE IF EXISTS `t_trouble_ticket`;
CREATE TABLE `t_trouble_ticket` (
  `ticket_id` varchar(50) NOT NULL COMMENT 'id',
  `ticket_code` varchar(50) DEFAULT NULL COMMENT '故障单号',
  `report_person` varchar(10) DEFAULT NULL COMMENT '报障人',
  `trouble_source` varchar(50) DEFAULT NULL COMMENT '故障来源',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `create_person` varchar(10) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `trouble_time` bigint(20) DEFAULT NULL COMMENT '故障发生时间',
  `trouble_time_month` bigint(20) DEFAULT NULL COMMENT '故障发生时间月份开始时间',
  `trouble_location` varchar(255) DEFAULT NULL COMMENT '故障发生地点',
  `trouble_type` varchar(50) DEFAULT NULL COMMENT '故障分类',
  `top_type` varchar(50) DEFAULT NULL COMMENT '故障一级分类',
  `secondary_type` varchar(50) DEFAULT NULL COMMENT '故障二级分类',
  `trouble_name` varchar(50) DEFAULT NULL COMMENT '故障名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `influence` varchar(10) DEFAULT NULL COMMENT '影响度',
  `urgency` varchar(10) DEFAULT NULL COMMENT '紧急度',
  `trouble_level` varchar(10) DEFAULT NULL COMMENT '故障级别',
  `handle_description` varchar(255) DEFAULT NULL COMMENT '故障处理描述',
  `solve_description` varchar(255) DEFAULT NULL COMMENT '故障解决描述',
  `handle_group` varchar(20) DEFAULT NULL COMMENT '处理组',
  `handle_person` varchar(10) DEFAULT NULL COMMENT '处理人',
  `trouble_cause` varchar(50) DEFAULT NULL COMMENT '故障原因定位',
  `solve_person` varchar(10) DEFAULT NULL COMMENT '解决人',
  `solve_time` varchar(50) DEFAULT NULL COMMENT '解决时间',
  `close_type` varchar(10) DEFAULT NULL COMMENT '关闭类型',
  `close_person` varchar(10) DEFAULT NULL COMMENT '关闭人',
  `close_time` varchar(50) DEFAULT NULL COMMENT '关闭时间',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  `interrupt_duration` decimal(10,4) DEFAULT NULL COMMENT '造成服务中断时长',
  `value_type` varchar(1) DEFAULT '0' COMMENT '类型1新增0导入',
  PRIMARY KEY (`ticket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_trouble_trend
-- ----------------------------
DROP TABLE IF EXISTS `t_trouble_trend`;
CREATE TABLE `t_trouble_trend` (
  `trend_id` varchar(50) NOT NULL COMMENT '主键',
  `trend_date` bigint(20) DEFAULT NULL COMMENT '统计日期',
  `top_type` varchar(10) DEFAULT NULL COMMENT '故障一级分类',
  `very_low_number` int(11) DEFAULT NULL COMMENT '极低次数',
  `low_number` int(11) DEFAULT NULL COMMENT '低次数',
  `moderate_number` int(11) DEFAULT NULL COMMENT '中次数',
  `highe_number` int(11) DEFAULT NULL COMMENT '高次数',
  `very_highe_number` int(11) DEFAULT NULL COMMENT '极高次数',
  `report_date` bigint(20) DEFAULT NULL COMMENT '分析月份',
  PRIMARY KEY (`trend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_trouble_type_statistics
-- ----------------------------
DROP TABLE IF EXISTS `t_trouble_type_statistics`;
CREATE TABLE `t_trouble_type_statistics` (
  `statistics_id` varchar(50) NOT NULL COMMENT '主键',
  `statistics_date` bigint(20) DEFAULT NULL COMMENT ' 统计日期',
  `top_type` varchar(10) DEFAULT NULL COMMENT '故障一级分类',
  `secondary_type` varchar(10) DEFAULT NULL COMMENT '故障二级分类',
  `total_number` int(11) DEFAULT NULL COMMENT '次数',
  `month_percentage` varchar(10) DEFAULT NULL COMMENT '环比',
  `year_percentage` varchar(10) DEFAULT NULL COMMENT '同比',
  `very_low_number` int(11) DEFAULT NULL COMMENT '极低次数',
  `very_low_month` varchar(10) DEFAULT NULL COMMENT '极低环比',
  `very_low_year` varchar(10) DEFAULT NULL COMMENT '极低同比',
  `low_number` int(11) DEFAULT NULL COMMENT '低次数',
  `low_month` varchar(10) DEFAULT NULL COMMENT '低环比',
  `low_year` varchar(10) DEFAULT NULL COMMENT '低同比',
  `moderate_number` int(11) DEFAULT NULL COMMENT '中次数',
  `moderate_month` varchar(10) DEFAULT NULL COMMENT '中环比',
  `moderate_year` varchar(10) DEFAULT NULL COMMENT '中同比',
  `high_number` int(11) DEFAULT NULL COMMENT '高次数',
  `high_month` varchar(10) DEFAULT NULL COMMENT '高环比',
  `high_year` varchar(10) DEFAULT NULL COMMENT '高同比',
  `very_high_number` int(11) DEFAULT NULL COMMENT '极高次数',
  `very_high_month` varchar(10) DEFAULT NULL COMMENT '极高环比',
  `very_high_year` varchar(10) DEFAULT NULL COMMENT '极高同比',
  `value_year` varchar(10) DEFAULT NULL COMMENT '年',
  `value_month` varchar(10) DEFAULT NULL COMMENT '月',
  `manage_id` varchar(50) DEFAULT NULL COMMENT '关联运维管理活动ID',
  `source_name` varchar(50) DEFAULT NULL COMMENT '来源对象名称',
  `source_code` varchar(50) DEFAULT NULL COMMENT '来源对象code',
  `cause` varchar(50) DEFAULT NULL COMMENT ' 产生原因',
  `solve_instruction` varchar(255) DEFAULT NULL COMMENT '处理说明',
  `activity_type` varchar(10) DEFAULT NULL COMMENT '运维管理活动类型',
  `report_type` varchar(10) DEFAULT NULL COMMENT '报告类型',
  PRIMARY KEY (`statistics_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_workload_statistics
-- ----------------------------
DROP TABLE IF EXISTS `t_workload_statistics`;
CREATE TABLE `t_workload_statistics` (
  `workload_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '主键id',
  `workload_date` bigint(20) DEFAULT NULL COMMENT '日期',
  `total_workload` int(11) DEFAULT NULL COMMENT '总上架U数',
  `free_percentage` decimal(10,6) DEFAULT NULL COMMENT '空闲负荷比',
  `normal_percentage` decimal(10,6) DEFAULT NULL COMMENT '正常负荷比',
  `full_percentage` decimal(10,6) DEFAULT NULL COMMENT '满负荷负荷比',
  PRIMARY KEY (`workload_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for t_base_cabinet
-- ----------------------------
DROP TABLE IF EXISTS `t_base_cabinet`;
CREATE TABLE `t_base_cabinet`  (
  `cabinet_base_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机柜基础数据id',
  `floor` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '楼层',
  `cabinet_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机柜名称',
  `device_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `cabinet_unique_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机柜唯一标识名',
  `rated_power` decimal(10, 2) NULL DEFAULT NULL COMMENT '设计负荷',
  `array_cabinet_design_load` decimal(10, 2) NULL DEFAULT NULL COMMENT '列头柜设计总负荷',
  `array_cabinet_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列头柜名称',
  `floor_unique_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '楼层唯一标识',
  `cabinet_column` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机柜列',
  `design_space_capacity` int(11) NULL DEFAULT NULL COMMENT '设计u数',
  `electric_reserve` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否是配电保留 0 不是 1 是',
  `data_collection_time` bigint(20) NULL DEFAULT NULL COMMENT '数据采集时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`cabinet_base_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基础机柜表' ROW_FORMAT = Dynamic;







-- ----------------------------
-- Table structure for t_cabinet
-- ----------------------------
DROP TABLE IF EXISTS `t_cabinet`;
CREATE TABLE `t_cabinet`  (
  `cabinet_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机柜id',
  `cabinet_unique_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机柜唯一标识名',
  `floor` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '楼层',
  `cabinet_column` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机柜列',
  `cabinet_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '柜名',
  `cabinet_location` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备位置',
  `design_space_capacity` int(11) NULL DEFAULT NULL COMMENT '设计设备空间容量',
  `used_space_capacity` int(11) NULL DEFAULT NULL COMMENT '使用的空间容量',
  `unused_space_capacity` int(11) NULL DEFAULT NULL COMMENT '剩余空间容量',
  `used_space_capacity_percent` decimal(10, 2) NULL DEFAULT NULL COMMENT '已使用的空间容量占比',
  `rated_power` decimal(10, 2) NULL DEFAULT NULL COMMENT '额定功率',
  `used_rated_power` decimal(10, 2) NULL DEFAULT NULL COMMENT '已用额定功率',
  `used_rated_percent` decimal(10, 2) NULL DEFAULT NULL COMMENT '额定已用功率百分比',
  `used_actual_power` decimal(10, 2) NULL DEFAULT NULL COMMENT '已用实际功率',
  `used_actual_percent` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际已用百分比',
  `month` int(10) NULL DEFAULT NULL COMMENT '月份',
  `year` int(10) NULL DEFAULT NULL COMMENT '年份',
  `data_collection_time` bigint(20) NULL DEFAULT NULL COMMENT '数据采集时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`cabinet_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '机柜信息表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `t_capacity_threshold_related_view_info`;
CREATE TABLE `t_capacity_threshold_related_view_info`  (
  `threshold_related_view_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '阈值关联id',
  `threshold_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值code',
  `view_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型 1 显示图数据 2 趋势数据',
  `data_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据code',
  `data_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据名称',
  `value` decimal(10, 2) NULL DEFAULT NULL COMMENT '值',
  `data_module` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据模块',
  `module_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块类型',
  `year` int(10) NULL DEFAULT NULL COMMENT '年份',
  `month` int(10) NULL DEFAULT NULL COMMENT '月份',
  `data_time` bigint(20) NULL DEFAULT NULL COMMENT '数据时间',
  `is_deleted` int(2) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  PRIMARY KEY (`threshold_related_view_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '空间容量关联显示图表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `t_capacity_threshold_info`;
CREATE TABLE `t_capacity_threshold_info`  (
  `threshold_info_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '阈值id',
  `threshold_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值名称',
  `threshold_value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值的值',
  `threshold_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值编码',
  `year_over_year_percent` decimal(10, 2) NULL DEFAULT NULL COMMENT '同比比率',
  `threshold_data` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '具体阈值的对象',
  `ring_growth` decimal(10, 2) NULL DEFAULT NULL COMMENT '环比比率',
  `used_cable` bigint(20) NULL DEFAULT NULL COMMENT '已用线缆数量  (综合布线特有字段)',
  `all_cable` bigint(20) NULL DEFAULT NULL COMMENT '线缆总数量 (综合布线特有字段)',
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值类型  1 楼层  2 机柜 3 功能区 4 模块 5 ups 6 机柜列 7 机柜（PDU） 8 路由类型',
  `module` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块  1 空间容量 2 电力容量 3 综合布线',
  `generic_cabling_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '综合布线类型 (综合布线特有字段)',
  `advice` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '建议',
  `month` int(10) NULL DEFAULT NULL COMMENT '月份',
  `year` int(10) NULL DEFAULT NULL COMMENT '年份',
  `activity_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运维管理活动类型',
  `solve_instruction` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理说明',
  `data_collection_time` bigint(20) NULL DEFAULT NULL COMMENT '数据采集时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`threshold_info_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '容量阈值信息表' ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for t_capacity_threshold_related_info
-- ----------------------------
DROP TABLE IF EXISTS `t_capacity_threshold_related_info`;
CREATE TABLE `t_capacity_threshold_related_info`  (
  `threshold_related_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '阈值关联id',
  `threshold_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '阈值编码',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型  1 推荐数据  2 显示关联数据',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `value` decimal(10, 2) NULL DEFAULT NULL COMMENT '值',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  PRIMARY KEY (`threshold_related_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '容量阈值关联信息表' ROW_FORMAT = Dynamic;



DROP TABLE IF EXISTS `t_connect_rack`;
CREATE TABLE `t_connect_rack`  (
  `connect_rack_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配线架id',
  `connect_rack_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配线架code',
  `eic_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'eic编号',
  `serial_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '序列号',
  `connect_rack_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配线架名称',
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配线架描述',
  `manufacture` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '厂商名称',
  `logo` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `serial_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系列名称',
  `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '型号',
  `status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `location` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '位置',
  `project` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属项目',
  `department` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `owner` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属者',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '重量',
  `used_weight` int(11) NULL DEFAULT NULL COMMENT 'u位高度',
  `asset_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产编号',
  `racking_time` bigint(20) NULL DEFAULT NULL COMMENT '上架时间',
  `data_collection_time` bigint(20) NULL DEFAULT NULL COMMENT '数据采集时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`connect_rack_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '配线架表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `t_generic_cabling`;
CREATE TABLE `t_generic_cabling`  (
  `generic_cabling_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '综合布线id',
  `cable_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '线缆类型',
  `generic_cabling_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '综合布线类型',
  `connect_rack_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本端配线架编号',
  `cabinet_unique_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机柜唯一标识名',
  `used_weight` int(11) NULL DEFAULT NULL COMMENT '本端u数',
  `location` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本端配线架位置',
  `port` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本端端口',
  `opposite_end_cabinet_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对端机柜唯一标识',
  `opposite_end_rack_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对端配线架编号',
  `opposite_used_weight` int(11) NULL DEFAULT NULL COMMENT '对端u数',
  `opposite_location` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对端配线架位置',
  `opposite_end_port` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对端端口',
  `extent` int(10) NULL DEFAULT NULL COMMENT '长度',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `month` int(10) NULL DEFAULT NULL COMMENT '月份',
  `year` int(10) NULL DEFAULT NULL COMMENT '年份',
  `data_collection_time` bigint(20) NULL DEFAULT NULL COMMENT '数据采集时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`generic_cabling_id`) USING BTREE,
  INDEX `year`(`year`) USING BTREE,
  INDEX `month`(`month`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;



DROP TABLE IF EXISTS `t_it_energy_current`;
CREATE TABLE `t_it_energy_current`  (
  `it_energy_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'it能耗电流id',
  `data_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据名称',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `all_electric_meter` decimal(10, 2) NULL DEFAULT NULL COMMENT '累计电量数',
  `growth_electric_meter` decimal(10, 2) NULL DEFAULT NULL COMMENT '电量增长电量',
  `module` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `year` int(10) NULL DEFAULT NULL COMMENT '年份',
  `month` int(10) NULL DEFAULT NULL COMMENT '月份',
  `data_collection_time` bigint(20) NULL DEFAULT NULL COMMENT '数据收集时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`it_energy_id`) USING BTREE,
  INDEX `year`(`year`) USING BTREE,
  INDEX `month`(`month`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'IT能耗电流表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_menu_info
-- ----------------------------
DROP TABLE IF EXISTS `t_menu_info`;
CREATE TABLE `t_menu_info` (
  `menu_id` varchar(10) NOT NULL COMMENT '菜单id',
  `menu_name` varchar(100) NOT NULL COMMENT '菜单名称 ',
  `menu_href` varchar(100) DEFAULT NULL COMMENT '菜单指向（菜单请求路径）',
  `parent_menu_id` varchar(50) DEFAULT NULL COMMENT '父级菜单编码',
  `menu_level` int(11) NOT NULL COMMENT '菜单级别',
  `menu_sort` int(11) NOT NULL COMMENT '菜单排序',
  `menu_code` varchar(20) DEFAULT NULL COMMENT '菜单code码',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_menu_info
-- ----------------------------
INSERT INTO `t_menu_info` VALUES ('0', '个人工作台', '', null, '1', '0', '01');
INSERT INTO `t_menu_info` VALUES ('1', '用户管理', '', null, '1', '1', '02');
INSERT INTO `t_menu_info` VALUES ('10', '容量分析', null, null, '1', '10', '03');
INSERT INTO `t_menu_info` VALUES ('100', '维护保养计划配置表', '/business/system-setup/maintenance-setting', '83', '3', '100', '0120605');
INSERT INTO `t_menu_info` VALUES ('101', '下拉选单', '/business/system-setup/dropdownbox-setting', '65', '2', '101', '01207');
INSERT INTO `t_menu_info` VALUES ('102', '采购项目配置', null, '65', '2', '102', '01208');
INSERT INTO `t_menu_info` VALUES ('103', '采购项目配置表 ', '/business/system-setup/purchase-item-setting', '102', '3', '103', '0120801');
INSERT INTO `t_menu_info` VALUES ('104', '新增采购项目配置', '/business/system-setup/purchase-item-add', '102', '3', '104', '0120802');
INSERT INTO `t_menu_info` VALUES ('105', '修改采购项目配置', '/business/system-setup/purchase-item-update', '102', '3', '105', '0120803');
INSERT INTO `t_menu_info` VALUES ('106', '删除采购项目配置', null, '102', '3', '106', '0120804');
INSERT INTO `t_menu_info` VALUES ('11', '容量数据采集', '', '10', '2', '11', '0301');
INSERT INTO `t_menu_info` VALUES ('12', '容量分析报告', '/business/volumetric-analysis/spatial-capacity', '10', '2', '12', '0302');
INSERT INTO `t_menu_info` VALUES ('13', '问题分析', null, null, '1', '13', '04');
INSERT INTO `t_menu_info` VALUES ('14', '问题数据采集', '', '13', '2', '14', '0401');
INSERT INTO `t_menu_info` VALUES ('15', '问题分析报告', '/business/problem-analysis/problem-analysis-report', '13', '2', '15', '0402');
INSERT INTO `t_menu_info` VALUES ('16', '生产力分析', null, null, '1', '16', '05');
INSERT INTO `t_menu_info` VALUES ('17', '生产力数据采集', '', '16', '2', '17', '0501');
INSERT INTO `t_menu_info` VALUES ('18', '生产力分析报告', '/business/productivity-analysis/productivity-analysis-report', '16', '2', '18', '0502');
INSERT INTO `t_menu_info` VALUES ('19', '运行情况', null, null, '1', '19', '06');
INSERT INTO `t_menu_info` VALUES ('2', '用户列表', '/business/user/user-list', '1', '2', '2', '0201');
INSERT INTO `t_menu_info` VALUES ('20', '运行情况数据采集', '/business/operation-analysis/operation-data-collection', '19', '2', '20', '0601');
INSERT INTO `t_menu_info` VALUES ('21', '运行情况分析报告', '/business/operation-analysis/operation-analysis-report', '19', '2', '21', '0602');
INSERT INTO `t_menu_info` VALUES ('22', '健康卡', '/business/operation-analysis/operation-health-card', '19', '2', '22', '0603');
INSERT INTO `t_menu_info` VALUES ('23', '维护保养计划', null, null, '1', '23', '07');
INSERT INTO `t_menu_info` VALUES ('24', '预算及采购', null, null, '1', '24', '08');
INSERT INTO `t_menu_info` VALUES ('25', '预算', '', '24', '2', '25', '0801');
INSERT INTO `t_menu_info` VALUES ('26', '预算录入', '/business/pre-selection-and-procurement-analysis/add-budget-plan', '25', '3', '26', '080101');
INSERT INTO `t_menu_info` VALUES ('27', '修改预算项', '/business/pre-selection-and-procurement-analysis/budget-update', '25', '3', '27', '080102');
INSERT INTO `t_menu_info` VALUES ('28', '删除预算项', null, '25', '3', '28', '080103');
INSERT INTO `t_menu_info` VALUES ('29', '采购', '', '24', '2', '29', '0802');
INSERT INTO `t_menu_info` VALUES ('3', '新建用户', '/business/user/user-add', '2', '3', '3', '020101');
INSERT INTO `t_menu_info` VALUES ('30', '新建采购项目', '/business/pre-selection-and-procurement-analysis/add-purchase-plan', '29', '3', '30', '080201');
INSERT INTO `t_menu_info` VALUES ('31', '修改采购项目', '/business/pre-selection-and-procurement-analysis/purchase-update', '29', '3', '31', '080202');
INSERT INTO `t_menu_info` VALUES ('32', '删除采购项目', null, '29', '3', '32', '080203');
INSERT INTO `t_menu_info` VALUES ('33', '决策管理', '/business/decision-management', null, '1', '33', '09');
INSERT INTO `t_menu_info` VALUES ('34', '风控分析', null, null, '1', '34', '010');
INSERT INTO `t_menu_info` VALUES ('35', '风控数据采集', '', '34', '2', '35', '01001');
INSERT INTO `t_menu_info` VALUES ('36', '风险项新增', '/business/risk-control-analysis/risk-add', '35', '3', '36', '0100101');
INSERT INTO `t_menu_info` VALUES ('37', '修改风险项', '/business/risk-control-analysis/risk-update', '35', '3', '37', '0100102');
INSERT INTO `t_menu_info` VALUES ('38', '删除风险项', null, '35', '3', '38', '0100103');
INSERT INTO `t_menu_info` VALUES ('39', '风控分析报告', '/business/risk-control-analysis/risk-analysis-report', '34', '2', '39', '01003');
INSERT INTO `t_menu_info` VALUES ('4', '修改用户', '/business/user/user-update', '2', '3', '4', '020102');
INSERT INTO `t_menu_info` VALUES ('40', '风控分析', '', '34', '2', '40', '01002');
INSERT INTO `t_menu_info` VALUES ('41', '修改风控分析数据', '/business/risk-control-analysis/risk-analysis-update', '40', '3', '41', '0100201');
INSERT INTO `t_menu_info` VALUES ('42', '风险分值修改', '/business/risk-control-analysis/risk-score', '35', '3', '42', '0100104');
INSERT INTO `t_menu_info` VALUES ('43', '新增问题项', '/business/problem-analysis/problem-add', '14', '3', '43', '040101');
INSERT INTO `t_menu_info` VALUES ('44', '修改问题项', '/business/problem-analysis/problem-update', '14', '3', '44', '040102');
INSERT INTO `t_menu_info` VALUES ('45', '删除问题项', null, '14', '3', '45', '040103');
INSERT INTO `t_menu_info` VALUES ('46', '生产力数据新增', '/business/productivity-analysis/productivity-add', '16', '3', '46', '050101');
INSERT INTO `t_menu_info` VALUES ('47', '生产力数据修改', '/business/productivity-analysis/productivity-update', '16', '3', '47', '050102');
INSERT INTO `t_menu_info` VALUES ('48', '生产力数据删除', null, '16', '3', '48', '050103');
INSERT INTO `t_menu_info` VALUES ('49', '基础信息', '', '23', '2', '49', '0701');
INSERT INTO `t_menu_info` VALUES ('5', '删除用户', '', '2', '3', '5', '020103');
INSERT INTO `t_menu_info` VALUES ('50', '新增基础信息', '/business/maintenance-plan/basis-plan-add', '49', '3', '50', '070101');
INSERT INTO `t_menu_info` VALUES ('51', '修改基础信息', '/business/maintenance-plan/basis-plan-update', '49', '3', '51', '070102');
INSERT INTO `t_menu_info` VALUES ('52', '删除基本信息', null, '49', '3', '52', '070103');
INSERT INTO `t_menu_info` VALUES ('53', '关联维保合同', '', '23', '2', '53', '0702');
INSERT INTO `t_menu_info` VALUES ('54', '新增维保合同', '/business/maintenance-plan/contract-plan-add', '53', '3', '54', '070201');
INSERT INTO `t_menu_info` VALUES ('55', '修改维保合同', '/business/maintenance-plan/contract-plan-update', '53', '3', '55', '070202');
INSERT INTO `t_menu_info` VALUES ('56', '删除维保合同', null, '53', '3', '56', '070203');
INSERT INTO `t_menu_info` VALUES ('57', '维护保养计划', '', '23', '2', '57', '0703');
INSERT INTO `t_menu_info` VALUES ('58', '新增维保计划', '/business/maintenance-plan/maintenance-plan-add', '57', '3', '58', '070301');
INSERT INTO `t_menu_info` VALUES ('59', '修改维保计划', '/business/maintenance-plan/maintenance-plan-update', '57', '3', '59', '070302');
INSERT INTO `t_menu_info` VALUES ('6', '用户组列表', '/business/user/user-group-list', '1', '2', '6', '0202');
INSERT INTO `t_menu_info` VALUES ('60', '删除维保计划', null, '57', '3', '60', '070303');
INSERT INTO `t_menu_info` VALUES ('61', '运维管理活动', '', null, '1', '61', '011');
INSERT INTO `t_menu_info` VALUES ('62', '运维管理活动表', '/business/operation-maintenance/operation-maintenance-plan-list', '61', '2', '62', '01101');
INSERT INTO `t_menu_info` VALUES ('63', '新增运维管理活动', '/business/operation-maintenance/operation-maintenance-plan-add', '61', '2', '63', '01102');
INSERT INTO `t_menu_info` VALUES ('64', '修改运维管理活动', '/business/operation-maintenance/operation-maintenance-plan-update', '61', '2', '64', '01103');
INSERT INTO `t_menu_info` VALUES ('65', '系统设置', null, null, '1', '65', '012');
INSERT INTO `t_menu_info` VALUES ('66', '阈值设置', '/business/system-setup/threshild-setting', '65', '2', '66', '01201');
INSERT INTO `t_menu_info` VALUES ('67', '下拉框维护', '/business/system-setup/dropdownbox-mainten', '65', '2', '67', '01202');
INSERT INTO `t_menu_info` VALUES ('68', '个人工作台列表1', '/business/personal-work-list/one', '01', '2', '68', '0101');
INSERT INTO `t_menu_info` VALUES ('69', '个人工作台列表2', '/business/personal-work-list/two', '01', '2', '69', '0102');
INSERT INTO `t_menu_info` VALUES ('7', '新建用户组', '/business/user/user-group-add', '6', '3', '7', '020201');
INSERT INTO `t_menu_info` VALUES ('70', '个人工作台列表3', '/business/personal-work-list/three', '01', '2', '70', '0103');
INSERT INTO `t_menu_info` VALUES ('71', 'IT能耗负荷总配置', '/business/system-setup/itEnergyLoad-setting', '65', '2', '71', '01203');
INSERT INTO `t_menu_info` VALUES ('72', '问题分析TOPN配置', '/business/system-setup/problemTop-setting', '65', '2', '72', '01204');
INSERT INTO `t_menu_info` VALUES ('73', '生产力分析参数配置', '/business/system-setup/productivityParam-setting', '65', '2', '73', '01205');
INSERT INTO `t_menu_info` VALUES ('74', '查看用户详情', '/business/user/user-details', '2', '3', '74', '020104');
INSERT INTO `t_menu_info` VALUES ('75', '查看用户组详情', '/business/user/user-group-details', '6', '3', '75', '020204');
INSERT INTO `t_menu_info` VALUES ('76', '查看风险项详情', '/business/risk-control-analysis/risk-details', '35', '3', '76', '0100105');
INSERT INTO `t_menu_info` VALUES ('77', '能耗分析', null, null, '1', '77', '013');
INSERT INTO `t_menu_info` VALUES ('78', '能耗数据采集', '', '77', '2', '78', '01301');
INSERT INTO `t_menu_info` VALUES ('79', '能耗分析报告', '/business/energy-consumption-analysis/energy-analysis-report', '77', '2', '79', '01302');
INSERT INTO `t_menu_info` VALUES ('8', '修改用户组', '/business/user/user-group-update', '6', '3', '8', '020202');
INSERT INTO `t_menu_info` VALUES ('80', '风控分析附件删除', null, '35', '3', '80', '0100106');
INSERT INTO `t_menu_info` VALUES ('81', '维保计划附件删除', null, '57', '3', '81', '070304');
INSERT INTO `t_menu_info` VALUES ('82', '运维管理活动附件删除', null, '61', '2', '82', '01104');
INSERT INTO `t_menu_info` VALUES ('83', '维护保养计划配置', '', '65', '2', '83', '01206');
INSERT INTO `t_menu_info` VALUES ('84', '新增维护保养计划', '/business/system-setup/maintenance-add', '83', '3', '84', '0120601');
INSERT INTO `t_menu_info` VALUES ('85', '修改维护保养计划', '/business/system-setup/maintenance-update', '83', '3', '85', '0120602');
INSERT INTO `t_menu_info` VALUES ('86', '删除维护保养计划', null, '83', '3', '86', '0120603');
INSERT INTO `t_menu_info` VALUES ('87', '暂停/启动维护保养计划', null, '83', '3', '87', '0120604');
INSERT INTO `t_menu_info` VALUES ('88', '维护保养计划附件删除', null, '57', '3', '88', '070304');
INSERT INTO `t_menu_info` VALUES ('89', '容量数据采集表', '/business/volumetric-analysis/data-Collection', '11', '3', '89', '030101');
INSERT INTO `t_menu_info` VALUES ('9', '删除用户组', null, '6', '3', '9', '020203');
INSERT INTO `t_menu_info` VALUES ('90', '问题数据采集表', '/business/problem-analysis/problem-data-collection', '14', '3', '90', '040104');
INSERT INTO `t_menu_info` VALUES ('91', '生产力数据采集表', '/business/productivity-analysis/productivity-data-collection', '17', '3', '91', '050104');
INSERT INTO `t_menu_info` VALUES ('92', '能耗数据采集表', '/business/energy-consumption-analysis/data-collection', '78', '3', '92', '0130101');
INSERT INTO `t_menu_info` VALUES ('93', '基础信息表', '/business/maintenance-plan/basis-plan-grid', '49', '3', '93', '070104');
INSERT INTO `t_menu_info` VALUES ('94', '关联维保合同表', '/business/maintenance-plan/contract-plan-grid', '54', '3', '94', '070204');
INSERT INTO `t_menu_info` VALUES ('95', '维护保养计划表', '/business/maintenance-plan/maintenance-plan-grid', '57', '3', '95', '070305');
INSERT INTO `t_menu_info` VALUES ('96', '预算表', '/business/pre-selection-and-procurement-analysis/budget-plan', '25', '3', '96', '080102');
INSERT INTO `t_menu_info` VALUES ('97', '采购表', '/business/pre-selection-and-procurement-analysis/purchase-plan', '29', '3', '97', '080202');
INSERT INTO `t_menu_info` VALUES ('98', '风控数据采集表', '/business/risk-control-analysis/risk-data-collection', '35', '3', '98', '0100107');
INSERT INTO `t_menu_info` VALUES ('99', '风控分析表', '/business/risk-control-analysis/risk-analysis', '40', '3', '99', '0100202');



-- ----------------------------
-- Table structure for t_user_group
-- ----------------------------
DROP TABLE IF EXISTS `t_user_group`;
CREATE TABLE `t_user_group` (
  `user_group_id` varchar(50) NOT NULL COMMENT '用户组id',
  `user_group_name` varchar(100) NOT NULL COMMENT '用户组名称',
  `is_default_group` int(1) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL COMMENT '用户组描述',
  `remark` varchar(200) DEFAULT NULL COMMENT '用户组描述',
  `is_deleted` int(1) DEFAULT '0' COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间(时间戳)',
  `update_user` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间(时间戳)',
  PRIMARY KEY (`user_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_group
-- ----------------------------
INSERT INTO `t_user_group` VALUES ('629d1abc9310491d9bdebc521365c09d', '超级管理员', '1', '超级管理员组', '此用户组拥有超级管理员菜单权限', '0', 'admin', '1570849249860', 'admin', '1572255143711');

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info` (
  `user_id` varchar(50) NOT NULL COMMENT '用户id',
  `user_name` varchar(100) NOT NULL COMMENT '用户名',
  `account_name` varchar(100) NOT NULL COMMENT '登录账户名',
  `user_real_name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `user_number` varchar(100) NOT NULL COMMENT '员工编号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `user_group_id` varchar(50) DEFAULT NULL,
  `phone_number` varchar(50) DEFAULT NULL COMMENT '电话号码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱地址',
  `is_super_user` int(1) DEFAULT NULL,
  `is_deleted` int(1) DEFAULT '0' COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间(时间戳)',
  `update_user` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间(时间戳)',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_info
-- ----------------------------

INSERT INTO `t_user_info` VALUES ('c298d5ffe72a4c0b88a1aff24331e490', 'admin', 'admin', '超级管理员', 'wh0000001', 'admin123', '629d1abc9310491d9bdebc521365c09d', '123123123', '123123', '1', '0', null, null, null, '1573113961635');

-- ----------------------------
-- Table structure for t_user_group_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_user_group_permission`;
CREATE TABLE `t_user_group_permission` (
  `permission_id` varchar(50) NOT NULL COMMENT '用户组菜单权限id',
  `user_group_id` varchar(50) NOT NULL COMMENT '用户组id',
  `menu_id` varchar(50) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_group_permission
-- ----------------------------
INSERT INTO `t_user_group_permission` VALUES ('019ac832311e4d509ae143fb536a619a', '629d1abc9310491d9bdebc521365c09d', '21');
INSERT INTO `t_user_group_permission` VALUES ('03cb2dbccb3e4c998bc5234ddaf5816c', '629d1abc9310491d9bdebc521365c09d', '11');
INSERT INTO `t_user_group_permission` VALUES ('0889434747c640f0bb786ac097149575', '629d1abc9310491d9bdebc521365c09d', '99');
INSERT INTO `t_user_group_permission` VALUES ('094505a847404bdca2b0a4f3c0524761', '629d1abc9310491d9bdebc521365c09d', '8');
INSERT INTO `t_user_group_permission` VALUES ('0ad454ac25164a9b82d91ccc84115257', '629d1abc9310491d9bdebc521365c09d', '73');
INSERT INTO `t_user_group_permission` VALUES ('0bcef2e370b34e02a3fda5306b7af01c', '629d1abc9310491d9bdebc521365c09d', '74');
INSERT INTO `t_user_group_permission` VALUES ('0c739cde48964b688881c16a29432777', '629d1abc9310491d9bdebc521365c09d', '47');
INSERT INTO `t_user_group_permission` VALUES ('0d3f981930ae47938988cde784afeecd', '629d1abc9310491d9bdebc521365c09d', '6');
INSERT INTO `t_user_group_permission` VALUES ('0f364bc0b07a4992883a3738a965e642', '629d1abc9310491d9bdebc521365c09d', '75');
INSERT INTO `t_user_group_permission` VALUES ('1791e78a69c548b086e45c5243cfea66', '629d1abc9310491d9bdebc521365c09d', '80');
INSERT INTO `t_user_group_permission` VALUES ('1e94012776f1469ebe17b5b30e4cbfaa', '629d1abc9310491d9bdebc521365c09d', '85');
INSERT INTO `t_user_group_permission` VALUES ('2188c72bc7bc42efac59304de4f6efe4', '629d1abc9310491d9bdebc521365c09d', '26');
INSERT INTO `t_user_group_permission` VALUES ('2259ba49d5834348a07b6b7532cffa0e', '629d1abc9310491d9bdebc521365c09d', '9');
INSERT INTO `t_user_group_permission` VALUES ('24852b37f27e46249ca44d0b1a2c153c', '629d1abc9310491d9bdebc521365c09d', '12');
INSERT INTO `t_user_group_permission` VALUES ('25111341be2a4b5eafb0af4d9600f65d', '629d1abc9310491d9bdebc521365c09d', '71');
INSERT INTO `t_user_group_permission` VALUES ('25713f012b7f4883947571def062d683', '629d1abc9310491d9bdebc521365c09d', '59');
INSERT INTO `t_user_group_permission` VALUES ('2a733aae05de4466b0a2dce9feb49dd0', '629d1abc9310491d9bdebc521365c09d', '77');
INSERT INTO `t_user_group_permission` VALUES ('2fb38a782e364ea3af9e9e673cb33706', '629d1abc9310491d9bdebc521365c09d', '81');
INSERT INTO `t_user_group_permission` VALUES ('338fd454d4e740209c0d96ee186cadec', '629d1abc9310491d9bdebc521365c09d', '84');
INSERT INTO `t_user_group_permission` VALUES ('339d3c66613d408fbe05b3aa08e1c285', '629d1abc9310491d9bdebc521365c09d', '25');
INSERT INTO `t_user_group_permission` VALUES ('353ee565777d43a2b6c34e356d70d687', '629d1abc9310491d9bdebc521365c09d', '66');
INSERT INTO `t_user_group_permission` VALUES ('35d4a72121634b029108e6659a1dc278', '629d1abc9310491d9bdebc521365c09d', '49');
INSERT INTO `t_user_group_permission` VALUES ('3a492711cb4e48e9bf4bbaeeb9863254', '629d1abc9310491d9bdebc521365c09d', '61');
INSERT INTO `t_user_group_permission` VALUES ('3a9d7615bfde48dc808a06570942e81e', '629d1abc9310491d9bdebc521365c09d', '13');
INSERT INTO `t_user_group_permission` VALUES ('3d4bb0acc40b48c48d828ac30c75bc7e', '629d1abc9310491d9bdebc521365c09d', '98');
INSERT INTO `t_user_group_permission` VALUES ('4081fc294ff144ea9c2ee48005167d95', '629d1abc9310491d9bdebc521365c09d', '104');
INSERT INTO `t_user_group_permission` VALUES ('41a1b700f1114454a685c93fab70b716', '629d1abc9310491d9bdebc521365c09d', '37');
INSERT INTO `t_user_group_permission` VALUES ('4716291e445b4dd6950fa6cd8d50c89c', '629d1abc9310491d9bdebc521365c09d', '56');
INSERT INTO `t_user_group_permission` VALUES ('483acba09c654a0b98290b2552331633', '629d1abc9310491d9bdebc521365c09d', '86');
INSERT INTO `t_user_group_permission` VALUES ('50f9734bba4d4b229757a8ceb3ff20e6', '629d1abc9310491d9bdebc521365c09d', '3');
INSERT INTO `t_user_group_permission` VALUES ('5119a53200b745d08f18735e651b5919', '629d1abc9310491d9bdebc521365c09d', '24');
INSERT INTO `t_user_group_permission` VALUES ('533f17098ae54e69a03ad7dee767980d', '629d1abc9310491d9bdebc521365c09d', '30');
INSERT INTO `t_user_group_permission` VALUES ('592c1c3f18d942589a649c714c20a975', '629d1abc9310491d9bdebc521365c09d', '42');
INSERT INTO `t_user_group_permission` VALUES ('5bdde6c5abb24e72a06f7601171cd10e', '629d1abc9310491d9bdebc521365c09d', '101');
INSERT INTO `t_user_group_permission` VALUES ('5de5f9b8a742483d8e6ca4d906746ae2', '629d1abc9310491d9bdebc521365c09d', '17');
INSERT INTO `t_user_group_permission` VALUES ('5f4e716599674f4db58d59a43912ec86', '629d1abc9310491d9bdebc521365c09d', '2');
INSERT INTO `t_user_group_permission` VALUES ('6028cdd0851643a4b9386caa83f62864', '629d1abc9310491d9bdebc521365c09d', '78');
INSERT INTO `t_user_group_permission` VALUES ('65fb18587a154064a7661bd51e56a061', '629d1abc9310491d9bdebc521365c09d', '15');
INSERT INTO `t_user_group_permission` VALUES ('666d8cc677414c71b267487edd727af4', '629d1abc9310491d9bdebc521365c09d', '65');
INSERT INTO `t_user_group_permission` VALUES ('6832d73b4b6d4ce8a850f8233751494a', '629d1abc9310491d9bdebc521365c09d', '67');
INSERT INTO `t_user_group_permission` VALUES ('689a65cf757447dd8d7cada91cd3dde4', '629d1abc9310491d9bdebc521365c09d', '60');
INSERT INTO `t_user_group_permission` VALUES ('69ac9524800547feba044c7fae509b54', '629d1abc9310491d9bdebc521365c09d', '93');
INSERT INTO `t_user_group_permission` VALUES ('6a6be1e909a448d7a3367c656c4e01cf', '629d1abc9310491d9bdebc521365c09d', '88');
INSERT INTO `t_user_group_permission` VALUES ('6d9d496d22a744309093859872fa53d7', '629d1abc9310491d9bdebc521365c09d', '57');
INSERT INTO `t_user_group_permission` VALUES ('6e4dbe4192ae4a6cb96642dd02161d39', '629d1abc9310491d9bdebc521365c09d', '36');
INSERT INTO `t_user_group_permission` VALUES ('6e758f17b92f4696a03fadbdfeb103f1', '629d1abc9310491d9bdebc521365c09d', '31');
INSERT INTO `t_user_group_permission` VALUES ('721df6d56eb840c0a8adf45fbd4b6f70', '629d1abc9310491d9bdebc521365c09d', '40');
INSERT INTO `t_user_group_permission` VALUES ('779acb541b714a8da4144307f2d7b59d', '629d1abc9310491d9bdebc521365c09d', '91');
INSERT INTO `t_user_group_permission` VALUES ('7fd49e3b7bac4fb594abeba9f5b12723', '629d1abc9310491d9bdebc521365c09d', '4');
INSERT INTO `t_user_group_permission` VALUES ('7ff45095b49f4a1ab7fc92bc0baa1c5e', '629d1abc9310491d9bdebc521365c09d', '34');
INSERT INTO `t_user_group_permission` VALUES ('8117b7e79891431e82672494426e4d7e', '629d1abc9310491d9bdebc521365c09d', '35');
INSERT INTO `t_user_group_permission` VALUES ('84dd32ed60bc44ee89370ef568c06a7e', '629d1abc9310491d9bdebc521365c09d', '44');
INSERT INTO `t_user_group_permission` VALUES ('87794e26f2c7492ebe156596f7209bdb', '629d1abc9310491d9bdebc521365c09d', '82');
INSERT INTO `t_user_group_permission` VALUES ('87b88f04391b43c1a5ce4a253681a3b7', '629d1abc9310491d9bdebc521365c09d', '79');
INSERT INTO `t_user_group_permission` VALUES ('8a26b5024bff4219974de48ee51c036a', '629d1abc9310491d9bdebc521365c09d', '54');
INSERT INTO `t_user_group_permission` VALUES ('8b77c4aa933c463588e3e067d516d49d', '629d1abc9310491d9bdebc521365c09d', '33');
INSERT INTO `t_user_group_permission` VALUES ('8cab06fbff344381bdced781e4655c03', '629d1abc9310491d9bdebc521365c09d', '14');
INSERT INTO `t_user_group_permission` VALUES ('8e6c1ce4fc8244dd856e7faa54b36a71', '629d1abc9310491d9bdebc521365c09d', '48');
INSERT INTO `t_user_group_permission` VALUES ('90b99ff9d2694560a27bd092d858d382', '629d1abc9310491d9bdebc521365c09d', '1');
INSERT INTO `t_user_group_permission` VALUES ('9189d9a3e11e4071bab507a7a27a5d1b', '629d1abc9310491d9bdebc521365c09d', '27');
INSERT INTO `t_user_group_permission` VALUES ('947ecf64d04b4928aa5f91ff3f78309b', '629d1abc9310491d9bdebc521365c09d', '105');
INSERT INTO `t_user_group_permission` VALUES ('9aacfdf62ff74110aa49c9f42c2e8b8e', '629d1abc9310491d9bdebc521365c09d', '19');
INSERT INTO `t_user_group_permission` VALUES ('9d8f678e02954cb39c281f712d2827b2', '629d1abc9310491d9bdebc521365c09d', '58');
INSERT INTO `t_user_group_permission` VALUES ('9df2eec4158144eb9367b10d23eea589', '629d1abc9310491d9bdebc521365c09d', '50');
INSERT INTO `t_user_group_permission` VALUES ('a701ed1e1628495480d5be2431d1b505', '629d1abc9310491d9bdebc521365c09d', '55');
INSERT INTO `t_user_group_permission` VALUES ('ad499d0868dc4e61baf2e6673abf4dcc', '629d1abc9310491d9bdebc521365c09d', '90');
INSERT INTO `t_user_group_permission` VALUES ('ae3dd8172ba8450681f8f04bc54bd848', '629d1abc9310491d9bdebc521365c09d', '96');
INSERT INTO `t_user_group_permission` VALUES ('afc65fcbc5e44d979be22de46f94cb86', '629d1abc9310491d9bdebc521365c09d', '63');
INSERT INTO `t_user_group_permission` VALUES ('b15182b3ac8b462bbe60d7e5ff428c9e', '629d1abc9310491d9bdebc521365c09d', '39');
INSERT INTO `t_user_group_permission` VALUES ('b2e64381bd524f369e79222e8822a669', '629d1abc9310491d9bdebc521365c09d', '62');
INSERT INTO `t_user_group_permission` VALUES ('b5f247977b744545bca63671967b25ef', '629d1abc9310491d9bdebc521365c09d', '95');
INSERT INTO `t_user_group_permission` VALUES ('bfd5b5edec4b4276a0809521f529256d', '629d1abc9310491d9bdebc521365c09d', '76');
INSERT INTO `t_user_group_permission` VALUES ('c2be2f034a6f4fd99348fca1959fcb8c', '629d1abc9310491d9bdebc521365c09d', '51');
INSERT INTO `t_user_group_permission` VALUES ('c4b3bdf02949408e9967be26717b1a85', '629d1abc9310491d9bdebc521365c09d', '103');
INSERT INTO `t_user_group_permission` VALUES ('c70c6ac0e6f04edc98b2499ab7b085af', '629d1abc9310491d9bdebc521365c09d', '38');
INSERT INTO `t_user_group_permission` VALUES ('c714a0f07bd44a4f8bdcba8c3acdd655', '629d1abc9310491d9bdebc521365c09d', '83');
INSERT INTO `t_user_group_permission` VALUES ('cacd92834bbc4d0e87a18add68a33d0e', '629d1abc9310491d9bdebc521365c09d', '46');
INSERT INTO `t_user_group_permission` VALUES ('cdcd35b844764e34addbb3b77351a05a', '629d1abc9310491d9bdebc521365c09d', '45');
INSERT INTO `t_user_group_permission` VALUES ('ce9e3bf3b2494f638afd507b1e217e54', '629d1abc9310491d9bdebc521365c09d', '72');
INSERT INTO `t_user_group_permission` VALUES ('cf22b3c19d0f4d3db79bf245dc5a3865', '629d1abc9310491d9bdebc521365c09d', '16');
INSERT INTO `t_user_group_permission` VALUES ('d0ace182f7ea40d0b2d1fb15e6377a4d', '629d1abc9310491d9bdebc521365c09d', '64');
INSERT INTO `t_user_group_permission` VALUES ('d2a45543d0f54855ad2fb47b10dedda4', '629d1abc9310491d9bdebc521365c09d', '43');
INSERT INTO `t_user_group_permission` VALUES ('db0d31faff4d45299069e91cc6dcce6e', '629d1abc9310491d9bdebc521365c09d', '18');
INSERT INTO `t_user_group_permission` VALUES ('df3e48bef04f4a5bb3e72c4641d7c3e4', '629d1abc9310491d9bdebc521365c09d', '87');
INSERT INTO `t_user_group_permission` VALUES ('e29ea73a3a7348eb83c382e84b7fde4f', '629d1abc9310491d9bdebc521365c09d', '5');
INSERT INTO `t_user_group_permission` VALUES ('e2ead20e393a45a2ba31b4f9e8143341', '629d1abc9310491d9bdebc521365c09d', '10');
INSERT INTO `t_user_group_permission` VALUES ('e37e21be017543d3985383cecbc14242', '629d1abc9310491d9bdebc521365c09d', '100');
INSERT INTO `t_user_group_permission` VALUES ('e3b8c1cfa89f4e6aaef21afcadfde9ca', '629d1abc9310491d9bdebc521365c09d', '89');
INSERT INTO `t_user_group_permission` VALUES ('ea591fd2e0ee43bcb3d59fb259ad1999', '629d1abc9310491d9bdebc521365c09d', '92');
INSERT INTO `t_user_group_permission` VALUES ('eb009156ea354cecb3dbf52de91e15f3', '629d1abc9310491d9bdebc521365c09d', '29');
INSERT INTO `t_user_group_permission` VALUES ('eeb6d3b249d04bd0804a4f56d99fa292', '629d1abc9310491d9bdebc521365c09d', '22');
INSERT INTO `t_user_group_permission` VALUES ('eef2d0098cea4dde98668dc2f4fba003', '629d1abc9310491d9bdebc521365c09d', '7');
INSERT INTO `t_user_group_permission` VALUES ('efee1430259b4a158dd8217ed21c71c4', '629d1abc9310491d9bdebc521365c09d', '97');
INSERT INTO `t_user_group_permission` VALUES ('f0666e5131cc41cf863b53284ffbbca1', '629d1abc9310491d9bdebc521365c09d', '23');
INSERT INTO `t_user_group_permission` VALUES ('f4038385cce94af49ed32fb0cbcf60c3', '629d1abc9310491d9bdebc521365c09d', '20');
INSERT INTO `t_user_group_permission` VALUES ('f9feed33b5af41c595cb1bad1d49a4a8', '629d1abc9310491d9bdebc521365c09d', '41');
INSERT INTO `t_user_group_permission` VALUES ('fa3c9d33bdf843428a87b5aa8595de6f', '629d1abc9310491d9bdebc521365c09d', '94');
INSERT INTO `t_user_group_permission` VALUES ('faebc12372664cae9c40e98da2452482', '629d1abc9310491d9bdebc521365c09d', '102');
INSERT INTO `t_user_group_permission` VALUES ('ff26e346a7404845b2eb7b5732d7866d', '629d1abc9310491d9bdebc521365c09d', '52');
-- ----------------------------
-- Table structure for t_risk_item
-- ----------------------------
DROP TABLE IF EXISTS `t_risk_item`;
CREATE TABLE `t_risk_item` (
  `risk_item_id` varchar(50) NOT NULL DEFAULT '' COMMENT '风险项id',
  `risk_item_name` varchar(100) DEFAULT NULL COMMENT '风险项名称',
  `found_date` bigint(20) DEFAULT NULL COMMENT '发现日期(时间戳)',
  `location` varchar(100) DEFAULT NULL COMMENT '位置/区域',
  `refer_system` int(2) DEFAULT NULL COMMENT '涉及系统',
  `child_system` varchar(100) DEFAULT NULL COMMENT '子系统',
  `refer_view` int(2) DEFAULT NULL COMMENT '涉及页面',
  `problem_source` int(2) DEFAULT NULL COMMENT '问题来源',
  `problem_describe` varchar(255) DEFAULT NULL COMMENT '问题描述',
  `risk_level` int(2) DEFAULT NULL COMMENT '风险等级',
  `risk_type` int(2) DEFAULT NULL COMMENT '风险类型',
  `risk_describe` varchar(255) DEFAULT NULL COMMENT '风险描述',
  `serial_effect_score` double(5,1) DEFAULT NULL COMMENT '连续性影响分值',
  `high_use_effect_score` double(5,1) DEFAULT NULL COMMENT '高可用影响分值',
  `system_level_score` double(5,1) DEFAULT NULL COMMENT '系统级别分值',
  `risk_happen_prob_score` double(5,1) DEFAULT NULL COMMENT '风险发生概率',
  `risk_score` double(5,1) DEFAULT NULL COMMENT '风险分值',
  `response_plan` varchar(100) DEFAULT NULL COMMENT '应对方案',
  `process_progress` int(2) DEFAULT NULL COMMENT '处理进度',
  `progress_update_description` varchar(255) DEFAULT NULL COMMENT '进度更新说明',
  `resolve_status` int(2) DEFAULT NULL COMMENT '解决状态',
  `plan_resolution_time` bigint(20) DEFAULT NULL COMMENT '计划解决时间(时间戳)',
  `actual_resolution_time` bigint(20) DEFAULT NULL COMMENT '实际解决时间(时间戳)',
  `timeout_time` int(10) DEFAULT NULL COMMENT '超时时间(单位:天)',
  `refer_annex_name` varchar(200) DEFAULT NULL COMMENT '参考附件名',
  `track_user` varchar(50) DEFAULT NULL COMMENT '追踪负责人',
  `check_user` varchar(50) DEFAULT NULL COMMENT '复检人',
  `check_time` bigint(20) DEFAULT NULL COMMENT '复检时间',
  `check_result` int(2) DEFAULT NULL COMMENT '复检结果',
  `is_manually_added` int(1) DEFAULT NULL COMMENT '是否为手动新增(0:是；1:否)',
  `activity_id` varchar(50) DEFAULT NULL COMMENT '相关运维管理活动id',
  `is_deleted` int(1) DEFAULT '0' COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间(时间戳)',
  `update_user` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间(时间戳)',
  `risk_found_year` int(10) DEFAULT NULL COMMENT '风险发生年份',
  `risk_found_month` int(10) DEFAULT NULL COMMENT '风险发生月份',
  `update_reason` varchar(255) DEFAULT NULL COMMENT '修改原因',
  PRIMARY KEY (`risk_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_risk_current_analysis
-- ----------------------------
DROP TABLE IF EXISTS `t_risk_current_analysis`;
CREATE TABLE `t_risk_current_analysis` (
  `id` varchar(50) NOT NULL COMMENT '主键id',
  `risk_item_id` varchar(50) DEFAULT NULL,
  `risk_item_name` varchar(100) DEFAULT NULL COMMENT '风险项名称',
  `timeout_time` int(10) DEFAULT NULL COMMENT '超时时间（单位：月）',
  `risk_level` int(1) DEFAULT NULL COMMENT '风险等级',
  `process_instruction` varchar(255) DEFAULT NULL COMMENT '处理说明',
  `activity_type` varchar(10) DEFAULT NULL COMMENT '运维管理活动类型',
  `threshold` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_risk_trend_analysis
-- ----------------------------
DROP TABLE IF EXISTS `t_risk_trend_analysis`;
CREATE TABLE `t_risk_trend_analysis` (
  `id` varchar(50) NOT NULL COMMENT '主键id',
  `risk_type` varchar(32) DEFAULT NULL COMMENT '风险类型',
  `advanced_risk_number` int(10) DEFAULT '0' COMMENT '高等级风险个数',
  `advanced_risk_year_over_year` decimal(10,2) DEFAULT NULL COMMENT '高等级风险同比',
  `advanced_risk_ring_growth` decimal(10,2) DEFAULT NULL COMMENT '高等级风险环比',
  `intermediate_risk_number` int(10) DEFAULT '0' COMMENT '中等级风险个数',
  `intermediate_risk_year_over_year` decimal(10,2) DEFAULT NULL COMMENT '中等级风险同比',
  `intermediate_risk_ring_growth` decimal(10,2) DEFAULT NULL COMMENT '中等级风险环比',
  `low_risk_number` int(10) DEFAULT '0' COMMENT '低等级风险个数',
  `low_risk_year_over_year` decimal(10,2) DEFAULT NULL COMMENT '低等级风险同比',
  `low_risk_ring_growth` decimal(10,2) DEFAULT NULL COMMENT '低等级风险环比',
  `risk_found_time` bigint(20) DEFAULT NULL COMMENT '风险发生时间（时间戳，精确到月）',
  `process_instruction` varchar(255) DEFAULT NULL COMMENT '处理说明',
  `activity_type` varchar(10) DEFAULT NULL COMMENT '运维管理活动类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_risk_level_trend
-- ----------------------------
DROP TABLE IF EXISTS `t_risk_level_trend`;
CREATE TABLE `t_risk_level_trend` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '主键id',
  `risk_level` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '风险等级',
  `risk_found_time` bigint(20) DEFAULT NULL COMMENT '风险发生时间（时间戳，精确到月）',
  `risk_count` int(10) DEFAULT NULL COMMENT '数量',
  `add_number` int(10) DEFAULT NULL COMMENT '增加个数',
  `ring_growth` decimal(10,0) DEFAULT NULL COMMENT '环比值',
  `process_instruction` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '处理说明',
  `activity_type` varchar(10) DEFAULT NULL COMMENT '运维管理活动类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_risk_current_analysis_report
-- ----------------------------
DROP TABLE IF EXISTS `t_risk_current_analysis_report`;
CREATE TABLE `t_risk_current_analysis_report` (
  `id` varchar(50) NOT NULL COMMENT '主键id',
  `risk_item_id` varchar(50) DEFAULT NULL,
  `risk_item_name` varchar(100) DEFAULT NULL COMMENT '风险项名称',
  `timeout_time` int(10) DEFAULT NULL COMMENT '超时时间（单位：月）',
  `risk_level` int(1) DEFAULT NULL COMMENT '风险等级',
  `process_instruction` varchar(255) DEFAULT NULL COMMENT '处理说明',
  `activity_type` varchar(10) DEFAULT NULL COMMENT '运维管理活动类型',
  `generation_time` bigint(20) DEFAULT NULL COMMENT '生成时间',
  `threshold` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_risk_trend_analysis_report
-- ----------------------------
DROP TABLE IF EXISTS `t_risk_trend_analysis_report`;
CREATE TABLE `t_risk_trend_analysis_report` (
  `id` varchar(50) NOT NULL COMMENT '主键id',
  `risk_type` varchar(32) DEFAULT NULL COMMENT '风险类型',
  `advanced_risk_number` int(10) DEFAULT '0' COMMENT '高等级风险个数',
  `advanced_risk_year_over_year` decimal(10,2) DEFAULT NULL COMMENT '高等级风险同比',
  `advanced_risk_ring_growth` decimal(10,2) DEFAULT NULL COMMENT '高等级风险环比',
  `intermediate_risk_number` int(10) DEFAULT '0' COMMENT '中等级风险个数',
  `intermediate_risk_year_over_year` decimal(10,2) DEFAULT NULL COMMENT '中等级风险同比',
  `intermediate_risk_ring_growth` decimal(10,2) DEFAULT NULL COMMENT '中等级风险环比',
  `low_risk_number` int(10) DEFAULT '0' COMMENT '低等级风险个数',
  `low_risk_year_over_year` decimal(10,2) DEFAULT NULL COMMENT '低等级风险同比',
  `low_risk_ring_growth` decimal(10,2) DEFAULT NULL COMMENT '低等级风险环比',
  `risk_found_time` bigint(20) DEFAULT NULL COMMENT '风险发生时间（时间戳，精确到月）',
  `process_instruction` varchar(255) DEFAULT NULL COMMENT '处理说明',
  `activity_type` varchar(10) DEFAULT NULL COMMENT '运维管理活动类型',
  `generation_time` bigint(20) DEFAULT NULL COMMENT '生成时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_risk_level_trend_report
-- ----------------------------
DROP TABLE IF EXISTS `t_risk_level_trend_report`;
CREATE TABLE `t_risk_level_trend_report` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '主键id',
  `risk_level` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '风险等级',
  `risk_found_time` bigint(20) DEFAULT NULL COMMENT '风险发生时间（时间戳，精确到月）',
  `risk_count` int(10) DEFAULT NULL COMMENT '数量',
  `add_number` int(10) DEFAULT NULL COMMENT '增加个数',
  `ring_growth` decimal(10,0) DEFAULT NULL COMMENT '环比值',
  `process_instruction` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '处理说明',
  `activity_type` varchar(10) DEFAULT NULL COMMENT '运维管理活动类型',
  `generation_time` bigint(20) DEFAULT NULL COMMENT '生成时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




-- ----------------------------
-- Table structure for t_power_energy_item
-- ----------------------------
DROP TABLE IF EXISTS `t_power_energy_item`;
CREATE TABLE `t_power_energy_item`  (
  `power_energy_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '动力能耗项编号',
  `data_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据名称',
  `electric_meter` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电量仪（度）',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `all_electric_meter` decimal(10, 2) NULL DEFAULT NULL COMMENT '累计电量数',
  `growth_electric_meter` decimal(10, 2) NULL DEFAULT NULL COMMENT '电量增长电量',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型 1 ups 2 变压器',
  `year` int(10) NULL DEFAULT NULL COMMENT '年',
  `month` int(10) NULL DEFAULT NULL COMMENT '月',
  `route` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由',
  `is_heat_item` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否是暖通分项',
  `data_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据code',
  `data_collection_time` bigint(20) NULL DEFAULT NULL COMMENT '数据收集时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`power_energy_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '动力能耗各分项表' ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for t_electric_instrument
-- ----------------------------
DROP TABLE IF EXISTS `t_electric_instrument`;
CREATE TABLE `t_electric_instrument`  (
  `electric_instrument_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电量仪表id',
  `data_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据名称',
  `electric_meter` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电量仪（度）',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `all_electric_meter` decimal(10, 2) NULL DEFAULT NULL COMMENT '累计电量数',
  `growth_electric_meter` decimal(10, 2) NULL DEFAULT NULL COMMENT '增长值',
  `year` int(10) NULL DEFAULT NULL COMMENT '年份',
  `month` int(10) NULL DEFAULT NULL COMMENT '月份',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `data_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据code',
  `data_collection_time` bigint(20) NULL DEFAULT NULL COMMENT '数据收集时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`electric_instrument_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电量仪表数据表' ROW_FORMAT = Dynamic;



DROP TABLE IF EXISTS `t_energy_analyze_info`;
CREATE TABLE `t_energy_analyze_info`  (
  `threshold_info_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '阈值id',
  `threshold_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值名称',
  `threshold_value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值的值',
  `threshold_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值编码',
  `year_over_year_percent` decimal(10, 2) NULL DEFAULT NULL COMMENT '同比比率',
  `threshold_data` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '具体阈值的对象',
  `ring_growth` decimal(10, 2) NULL DEFAULT NULL COMMENT '环比比率',
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值类型  1 冷机  2 水泵 3 冷塔 4 精密空调 5 it能耗 6 动力能耗   7 pue值',
  `module` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块  1 暖通分项 2 数据机房 3 pue值 ',
  `advice` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '建议',
  `month` int(10) NULL DEFAULT NULL COMMENT '月份',
  `year` int(10) NULL DEFAULT NULL COMMENT '年份',
  `is_child` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否是子项',
  `parent_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父数据code',
  `year_over_year_number` decimal(10, 2) NULL DEFAULT NULL COMMENT '同比数量',
  `ring_growth_number` decimal(10, 2) NULL DEFAULT NULL COMMENT '环比数量',
  `activity_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运维管理活动类型',
  `manage_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `solve_instruction` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理说明',
  `analyze_year` int(10) NULL DEFAULT NULL COMMENT '分析年份',
  `analyze_month` int(10) NULL DEFAULT NULL COMMENT '分析月份',
  `analyze_time` bigint(20) NULL DEFAULT NULL COMMENT '分析时间',
  `data_collection_time` bigint(20) NULL DEFAULT NULL COMMENT '数据采集时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`threshold_info_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '能耗分析表' ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for t_energy_analyze_related_view_info
-- ----------------------------
DROP TABLE IF EXISTS `t_energy_analyze_related_view_info`;
CREATE TABLE `t_energy_analyze_related_view_info`  (
  `threshold_related_view_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '阈值关联id',
  `threshold_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值code',
  `view_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型 1 显示图数据 2 趋势数据',
  `data_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据code',
  `data_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据名称',
  `value` decimal(10, 2) NULL DEFAULT NULL COMMENT '值',
  `data_module` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据模块',
  `module_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块类型',
  `year` int(10) NULL DEFAULT NULL COMMENT '年份',
  `month` int(10) NULL DEFAULT NULL COMMENT '月份',
  `analyze_year` int(10) NULL DEFAULT NULL COMMENT '分析年份',
  `analyze_month` int(10) NULL DEFAULT NULL COMMENT '分析月份',
  `analyze_time` bigint(20) NULL DEFAULT NULL COMMENT '分析时间',
  `data_time` bigint(20) NULL DEFAULT NULL COMMENT '数据时间',
  `is_deleted` int(2) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  PRIMARY KEY (`threshold_related_view_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '能耗分析图像显示表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_energy_threshold_info
-- ----------------------------
DROP TABLE IF EXISTS `t_energy_threshold_info`;
CREATE TABLE `t_energy_threshold_info`  (
  `threshold_info_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '阈值id',
  `threshold_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值名称',
  `threshold_value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值的值',
  `threshold_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值编码',
  `year_over_year_percent` decimal(10, 2) NULL DEFAULT NULL COMMENT '同比比率',
  `threshold_data` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '具体阈值的对象',
  `ring_growth` decimal(10, 2) NULL DEFAULT NULL COMMENT '环比比率',
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值类型  1 冷机  2 水泵 3 冷塔 4 精密空调 5 it能耗 6 动力能耗   7 pue值',
  `module` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块  1 暖通分项 2 数据机房 3 pue值 ',
  `advice` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '建议',
  `month` int(10) NULL DEFAULT NULL COMMENT '月份',
  `year` int(10) NULL DEFAULT NULL COMMENT '年份',
  `is_child` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否是子项 0 否 1 是',
  `parent_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父节点code',
  `year_over_year_number` decimal(10, 2) NULL DEFAULT NULL COMMENT '同比数量',
  `ring_growth_number` decimal(10, 2) NULL DEFAULT NULL COMMENT '环比数量',
  `data_collection_time` bigint(20) NULL DEFAULT NULL COMMENT '数据采集时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`threshold_info_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '能耗阈值表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `t_energy_threshold_related_view_info`;
CREATE TABLE `t_energy_threshold_related_view_info`  (
  `threshold_related_view_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '阈值关联id',
  `threshold_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阈值code',
  `view_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型 1 显示图数据 2 趋势数据',
  `data_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据code',
  `data_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据名称',
  `value` decimal(10, 2) NULL DEFAULT NULL COMMENT '值',
  `data_module` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据模块',
  `module_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块类型',
  `year` int(10) NULL DEFAULT NULL COMMENT '年份',
  `month` int(10) NULL DEFAULT NULL COMMENT '月份',
  `data_time` bigint(20) NULL DEFAULT NULL COMMENT '数据时间',
  `is_deleted` int(2) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  PRIMARY KEY (`threshold_related_view_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '能耗阈值图像显示' ROW_FORMAT = Dynamic;



DROP TABLE IF EXISTS `t_budget` CASCADE
;

DROP TABLE IF EXISTS `t_budget_purchase` CASCADE
;

DROP TABLE IF EXISTS `t_budget_purchase_item` CASCADE
;

DROP TABLE IF EXISTS `t_budget_purchase_plan` CASCADE
;

DROP TABLE IF EXISTS `t_budget_purchase_tl` CASCADE
;

/* Create Tables */

CREATE TABLE `t_budget`
(
  `tid` VARCHAR(50) NOT NULL COMMENT '主键',
  `code` VARCHAR(32) NOT NULL COMMENT '预算代码',
  `name` VARCHAR(60) NOT NULL COMMENT '预算名称',
  `type` VARCHAR(20) NOT NULL COMMENT '预算类别',
  `cost_center` VARCHAR(20) NOT NULL COMMENT '成本中心',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '预算金额',
  `classify` VARCHAR(20) NOT NULL COMMENT '预算分类',
  `kind` VARCHAR(20) NOT NULL COMMENT '预算种类',
  `budget_year` VARCHAR(10) NOT NULL COMMENT '预算年份',
  `cost_type` VARCHAR(50) NOT NULL COMMENT '费用类型',
  `cost_class` VARCHAR(50) NOT NULL COMMENT '费用分类',
  `create_user` VARCHAR(50) NULL COMMENT '创建用户',
  `update_user` VARCHAR(50) NULL COMMENT '更新用户',
  `create_time` DATETIME(0) NULL COMMENT '创建时间',
  `update_time` DATETIME(0) NULL COMMENT '更新时间',
  CONSTRAINT `PK_t_budget` PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预算表'
;

CREATE TABLE `t_budget_purchase`
(
  `tid` VARCHAR(50) NOT NULL COMMENT '主键',
  `budget_id` VARCHAR(50) NOT NULL COMMENT '预算ID',
  `pro_name` VARCHAR(60) NOT NULL COMMENT '采购项目名称',
  `pro_remark` VARCHAR(100) NOT NULL COMMENT '采购方式描述',
  `purchase_winning` VARCHAR(200) NOT NULL COMMENT '中标厂家',
  `pro_leader` VARCHAR(50) NOT NULL COMMENT '项目负责人',
  `budget_amount` DECIMAL(10,2) NOT NULL COMMENT '预算金额',
  `deal_amount` DECIMAL(10,2) NOT NULL COMMENT '成交金额',
  `payment_method` VARCHAR(50) NOT NULL COMMENT '付款方法',
  `payment_plan` VARCHAR(100) NOT NULL COMMENT '付款计划',
  `payment_warn_date` DATETIME(0) NOT NULL COMMENT '付款预警',
  `invoice_submit` DATETIME(0) NOT NULL COMMENT '发票最终提交日',
  `purchase_year` VARCHAR(10) NOT NULL COMMENT '采购年份',
  `status` VARCHAR(50) NULL COMMENT '状态',
  `create_user` VARCHAR(50) NULL COMMENT '创建人',
  `create_date` DATETIME(0) NULL COMMENT '创建时间',
  `update_user` VARCHAR(50) NULL COMMENT '修改人',
  `update_date` DATETIME(0) NULL COMMENT '修改时间',
  CONSTRAINT `PK_t_purchase` PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预算采购表'
;

CREATE TABLE `t_budget_purchase_item`
(
  `tid` VARCHAR(50) NOT NULL COMMENT '主键',
  `purchase_id` VARCHAR(50) NOT NULL COMMENT '采购ID',
  `template_id` VARCHAR(50) NOT NULL COMMENT '模板ID',
  `plan_start_date` DATE NULL COMMENT '审批计划开始日期',
  `plan_end_date` DATE NULL COMMENT '审批计划结束日期',
  `act_start_date` DATE NULL COMMENT '审批实际开始日期',
  `act_end_date` DATE NULL COMMENT '审批实际结束日期',
  `status` VARCHAR(50) NULL COMMENT '状态',
  `remark` VARCHAR(100) NULL COMMENT '状态描述',
  `create_user` VARCHAR(50) NULL COMMENT '创建人',
  `create_date` DATETIME(0) NULL COMMENT '创建时间',
  `update_user` VARCHAR(50) NULL COMMENT '修改人',
  `update_date` DATETIME(0) NULL COMMENT '修改时间',
  CONSTRAINT `PK_t_budget_purchase_item` PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预算采购审批项表'
;

CREATE TABLE `t_budget_purchase_plan`
(
  `tid` VARCHAR(50) NOT NULL COMMENT '主键',
  `purchase_id` VARCHAR(50) NOT NULL COMMENT '采购ID',
  `payment_ratio` DECIMAL(10,2) NOT NULL COMMENT '付款比例',
  `payment_amount` DECIMAL(10,2) NOT NULL COMMENT '付款金额',
  `payment_year` VARCHAR(10) NOT NULL COMMENT '付款年份',
  CONSTRAINT `PK_t_budget_purchase_item` PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预算采购计划'
;

CREATE TABLE `t_budget_purchase_tl`
(
  `tid` VARCHAR(50) NOT NULL,
  `name` VARCHAR(50) NOT NULL COMMENT '名称',
  `is_delete` INT NOT NULL COMMENT '是否删除',
  `is_order` INT NOT NULL COMMENT '排序号',
  `check_flag` INT NOT NULL COMMENT '是否选中',
  CONSTRAINT `PK_t_budget_purchase_tl` PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购审批模板表'
;

-- ----------------------------
-- Records of t_budget_purchase_tl
-- ----------------------------
INSERT INTO `t_budget_purchase_tl` VALUES ('0rbYCJdUsiMwwtoTW5D', '招标或者询价采购（将依据流程规划部进度更新本阶段进度）', '0', '8', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('1qHC9vpphBRPf58J6v6', '需求单领导邮件审批', '0', '4', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('4AMkliYLSmglESrj2hJ', '采购项目启动', '0', '1', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('8bNS3qWaevEdGPFrEIE', '验收', '0', '16', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('8hRfhSiGSazU41nyhMJ', '验收', '0', '18', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('9oSwoWakxXjs3UR3PVZ', '合同签订（将依据太寿或者流程规划部进度更新本阶段进度）', '0', '14', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('bgcqKp5nbLslHegJnsg', '第三次次付款', '0', '21', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('bSVM8TFJPt0l7ut94og', '终验付款/第二次付款', '0', '19', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('bzKw1ZtkR2yaRGHB4YY', '厂商回标或者询价答复（将依据流程规划部进度更新本阶段进度）', '0', '9', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('CVHxA8WVBre8nrcLBqL', '太寿会签（将依据太寿进度更新本阶段进度）', '0', '3', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('fw9ms0UxT3K3w0wGcoD', '采购平台需求单审批（将依据流程规划部进度更新本阶段进度）', '0', '5', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('H4nCtxErHSMiWIO7hsj', '开标（将依据流程规划部进度更新本阶段进度）', '0', '10', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('Li5vkcEfKVapquohG0u', '合同法务审核（将依据法务进度更新本阶段进度）', '0', '12', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('lT2OVNambUzZ8lwgoyi', '设备到货', '0', '15', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('PGe7Lb5NwxZHqKJtggP', '金科签报', '0', '2', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('pz8UHeJWWINOpWmEipp', '合同行政审批（将依据行政进度更新本阶段进度）', '0', '13', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('SfFdC3ihpWVzv3LkiyO', '编写邀标文件', '0', '7', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('TY6cS8bVuoAfG7K8mX8', '中标结果通知（将依据流程规划部进度更新本阶段进度）', '0', '11', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('X5zNX1Vfx3EMZ5Uhylw', '流程规划部或集采办立项（将依据流程规划部进度更新本阶段进度）', '0', '6', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('YyXAtekMlEk6V3IrhB8', '验收', '0', '20', '1');
INSERT INTO `t_budget_purchase_tl` VALUES ('YZ3Tgo5lUQq16mSp7l5', '初验付款/第一次付款', '0', '17', '1');


-- ----------------------------
-- Table structure for t_system_setting
-- ----------------------------
DROP TABLE IF EXISTS `t_system_setting`;
CREATE TABLE `t_system_setting` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '主键id ',
  `code` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT 'code 码',
  `code_name` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT 'codeName',
  `parent_code` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '父code',
  `value` double(10,2) DEFAULT NULL,
  `check_status` tinyint(10) DEFAULT '1' COMMENT '是否选中',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统设置Code 对应表';

-- ----------------------------
-- Records of t_system_setting
-- ----------------------------
INSERT INTO `t_system_setting` VALUES ('09lyzRO2i08ygzyggOP', '5010', '风险超时时间', '50', null, '1');
INSERT INTO `t_system_setting` VALUES ('0ti2P0Vpj3RhPF5M2Zh', '1020', '楼层空间占比', '10', null, '1');
INSERT INTO `t_system_setting` VALUES ('1HfWYWlvcIxjPo9IUyq', '402040', '四级阀值', '4020', '70', '1');
INSERT INTO `t_system_setting` VALUES ('2T5OwvV37IUjcQsxJe0', '103040', '四级阀值', '1030', '70', '1');
INSERT INTO `t_system_setting` VALUES ('3BMYE3qIrYNYUKX85iM', '60', '预算及采购', 'root', null, '1');
INSERT INTO `t_system_setting` VALUES ('3G44t3m9IC722dZiA6o', '501030', '三级阀值', '5010', '3', '1');
INSERT INTO `t_system_setting` VALUES ('3NqNmOGka6CmzsRofEf', '4030', '设计负荷占比', '40', null, '1');
INSERT INTO `t_system_setting` VALUES ('3U13nCP9KZY2VBeVZCU', '502010', '低等级风险阀值', '5020', '25', '1');
INSERT INTO `t_system_setting` VALUES ('42l2YtH3c1t9sxvhGBU', '403050', '五级阀值', '4030', '90', '1');
INSERT INTO `t_system_setting` VALUES ('52l2htH3c1t9sxvhGBU', '402050', '五级阀值', '4020', '90', '1');
INSERT INTO `t_system_setting` VALUES ('52l2YtH3c1t9sxvCGBG', '101050', '五级阀值', '1010', '90', '1');
INSERT INTO `t_system_setting` VALUES ('52l2YtH3c1t9sxvgGBU', '103050', '五级阀值', '1030', '90', '1');
INSERT INTO `t_system_setting` VALUES ('52l2YtH3c1t9sxvhGBU', '401050', '五级阀值', '4010', '2', '1');
INSERT INTO `t_system_setting` VALUES ('52l2YtH3c3t9sxvCGBU', '102050', '五级阀值', '1020', '90', '1');
INSERT INTO `t_system_setting` VALUES ('52l2YtH3hthsxjvhGBU', '404050', '五级阀值', '4040', '90', '0');
INSERT INTO `t_system_setting` VALUES ('8jGY5Ph0j0XYvlnOKh5', '501010', '一级阀值', '5010', '1', '1');
INSERT INTO `t_system_setting` VALUES ('9ige6CT5k3we89FuN5w', '404010', '一级阀值', '4040', '80', '1');
INSERT INTO `t_system_setting` VALUES ('A8wwvSZ1NCKpQaVbh2T', '404040', '四级阀值', '4040', '70', '1');
INSERT INTO `t_system_setting` VALUES ('bA81gu5vlGiPuXRXEnu', '103020', '二级阀值', '1030', '30', '1');
INSERT INTO `t_system_setting` VALUES ('Dj63bFbEj9Uq1sI4BeH', '102030', '三级阀值', '1020', '50', '1');
INSERT INTO `t_system_setting` VALUES ('EG75OIAm68mwaTrXlfU', '402010', '一级阀值', '4020', '0', '0');
INSERT INTO `t_system_setting` VALUES ('eIPQ25Ms5b3uYd72jR9', '404030', '三级阀值', '4040', '50', '1');
INSERT INTO `t_system_setting` VALUES ('Ghp8aXpzJKAtRs8o81b', '101030', '三级阀值', '1010', '50', '1');
INSERT INTO `t_system_setting` VALUES ('gU6EZ7vIld8yLoLaZVp', '103030', '三级阀值', '1030', '50', '1');
INSERT INTO `t_system_setting` VALUES ('H4Q2I1UQ8PHa7KMp71Y', '501050', '五级阀值', '5010', '5', '1');
INSERT INTO `t_system_setting` VALUES ('ilmPNynwDCDqXRWYC9w', '102010', '一级阀值', '1020', '0', '1');
INSERT INTO `t_system_setting` VALUES ('k5lod2lQpcJB5ivcQiS', '102020', '二级阀值', '1020', '30', '1');
INSERT INTO `t_system_setting` VALUES ('KLb2IhyZaqWhv7g7fMi', '4010', '用电负荷增长率', '40', null, '1');
INSERT INTO `t_system_setting` VALUES ('kPHfKxizpx491jzPwuQ', '404020', '二级阀值', '4040', '30', '1');
INSERT INTO `t_system_setting` VALUES ('kudUVU9VdPczdhyeog1', '109010', '模块一变压器负荷总容量', '1090', '2000', '1');
INSERT INTO `t_system_setting` VALUES ('kudUVU9VdPczdhyeog2', '109020', '模块一ups负荷总容量', '1090', '2000', '1');
INSERT INTO `t_system_setting` VALUES ('kudUVU9VdPczdhyeog3', '109030', '模块二变压器负荷总容量', '1090', '2000', '1');
INSERT INTO `t_system_setting` VALUES ('kudUVU9VdPczdhyeog4', '109040', '模块二ups负荷总容量', '1090', '2000', '1');
INSERT INTO `t_system_setting` VALUES ('kudUVU9VdPczdhyeogM', '101010', '一级阀值', '1010', '20', '0');
INSERT INTO `t_system_setting` VALUES ('LNnm6U3aHnWOvgbg66W', '403040', '四级阀值', '4030', '70', '0');
INSERT INTO `t_system_setting` VALUES ('LqWshMg2iceBWidVlUR', '403010', '一级阀值', '4030', '0', '1');
INSERT INTO `t_system_setting` VALUES ('o1B6W0Wq8TJECXbww6t', '5020', '风险等级', '50', null, '1');
INSERT INTO `t_system_setting` VALUES ('OLwxgUANqnkf9aSzZo6', '4040', 'ups负荷占比', '40', null, '1');
INSERT INTO `t_system_setting` VALUES ('P7yqRDylhtNyJHup2dQ', '401040', '四级阀值', '4010', '70', '1');
INSERT INTO `t_system_setting` VALUES ('PFnWhhoDSmav7SxSGqD', '401020', '二级阀值', '4010', '30', '1');
INSERT INTO `t_system_setting` VALUES ('PPhUOxpHST3x5ioAU0T', '501040', '四级阀值', '5010', '4', '1');
INSERT INTO `t_system_setting` VALUES ('Qf4zntYUn1t4s6Rfz62', '401010', '一级阀值', '4010', '10', '0');
INSERT INTO `t_system_setting` VALUES ('QuKuFaF4Qa9BerD0TbJ', '101040', '四级阀值', '1010', '70', '1');
INSERT INTO `t_system_setting` VALUES ('Rye6YUFIJGqcQamkeeF', '502020', '高等级风险阀值', '5020', '75', '1');
INSERT INTO `t_system_setting` VALUES ('SLvODwKZiXwnKiDSR71', '50', '风控', 'root', null, '1');
INSERT INTO `t_system_setting` VALUES ('sqw6fyRYT37dIgQHmme', '402020', '二级阀值', '4020', '30', '1');
INSERT INTO `t_system_setting` VALUES ('T2lDehgWdjXvBuIgk0z', '10', '容量', 'root', null, '1');
INSERT INTO `t_system_setting` VALUES ('V1QA7FfEqHxaXjP6Gij', '402030', '三级阀值', '4020', '50', '1');
INSERT INTO `t_system_setting` VALUES ('Wd5GTMYMyFDXkQVJBRn', '403020', '二级阀值', '4030', '30', '1');
INSERT INTO `t_system_setting` VALUES ('wh98gySTM8OMqFEcGMd', '403030', '三级阀值', '4030', '50', '0');
INSERT INTO `t_system_setting` VALUES ('WndIC2AUPbqpZf4JGJI', '1010', '功能区空间占比', '10', null, '1');
INSERT INTO `t_system_setting` VALUES ('WvdFO8tYNlpZ4Hx8usa', '1040', '模块电力占比', '10', null, '1');
INSERT INTO `t_system_setting` VALUES ('WvdFO8tYNlpZ4Hx8usb', '1050', 'ups电力占比', '10', null, '1');
INSERT INTO `t_system_setting` VALUES ('WvdFO8tYNlpZ4Hx8usc', '1060', '列头柜电力占比', '10', null, '1');
INSERT INTO `t_system_setting` VALUES ('WvdFO8tYNlpZ4Hx8usd', '1070', 'pdu电力占比', '10', null, '1');
INSERT INTO `t_system_setting` VALUES ('WvdFO8tYNlpZ4Hx8use', '1080', '综合布线已使用端口占比', '10', null, '1');
INSERT INTO `t_system_setting` VALUES ('WvdFO8tYNlpZ4Hx8usg', '1090', 'It 能耗负荷总量配置', '10', null, '1');
INSERT INTO `t_system_setting` VALUES ('WvdFO8tYNlpZ4Hx8usL', '1030', '机柜空间占比', '10', null, '1');
INSERT INTO `t_system_setting` VALUES ('WYD5HDrkYJcJCZJxYoV', '101020', '二级阀值', '1010', '20', '0');
INSERT INTO `t_system_setting` VALUES ('x1uRAsiUdKo5EBYNgH0', '103010', '一级阀值', '1030', '0', '1');
INSERT INTO `t_system_setting` VALUES ('xfeFGJuUfuMswjmIS7f', '501020', '二级阀值', '5010', '2', '1');
INSERT INTO `t_system_setting` VALUES ('xHQbdQbeBBU6XpvFiy9', '4020', 'PUE值', '40', null, '1');
INSERT INTO `t_system_setting` VALUES ('YjpYAZ7fzE4gYOSrcaW', '102040', '四级阀值', '1020', '70', '1');
INSERT INTO `t_system_setting` VALUES ('Zc7gWgd40hxLWI3mDxV', '401030', '三级阀值', '4010', '50', '1');
INSERT INTO `t_system_setting` VALUES ('y7cUiZLuqnL3oGQqJct', '20', '问题', 'root', null, '1');
INSERT INTO `t_system_setting` VALUES ('52l2YTT3c1t9sxvCGBU', '2010', '故障分类', '20', null, '1');
INSERT INTO `t_system_setting` VALUES ('52l2YtH3c1t9sxvFFBU', '201001', 'TOPN配置', '2010', '3.00', '1');
INSERT INTO `t_system_setting` VALUES ('DooloM2LY28LqOP04n7', '70', '生产力', 'root', null, '1');
INSERT INTO `t_system_setting` VALUES ('nOiYiUUXtX1ovYidfHx', '7010', '人力资源', '70', null, '1');
INSERT INTO `t_system_setting` VALUES ('yBcox64uj6vXAEocYmt', '701001', '团队负荷资源状态上限', '7010', '1.00', '1');
INSERT INTO `t_system_setting` VALUES ('52l2YtH3c1t9sxvCGBU', '701002', '团队负荷资源状态下限', '7010', '0.70', '1');
INSERT INTO `t_system_setting` VALUES ('52l2YtH3c1t9sxvCGBB', '701003', '最多上架人数', '7010', '2.00', '1');
INSERT INTO `t_system_setting` VALUES ('C6PtNgT4kE5QZuNkkcq', '8010', '健康卡衡量参数配置', '80', null, '1');
INSERT INTO `t_system_setting` VALUES ('fENA6jKlsGdjxOS9rE3', '801003', '空间容量正常最大使用率', '8010', '80.00', '1');
INSERT INTO `t_system_setting` VALUES ('hYQBreB2j12KpLP3ZYY', '801002', '消防蓄水池可供冷却系统正常最小补水量', '8010', '100.00', '1');
INSERT INTO `t_system_setting` VALUES ('jlhJBms1kInu463jVu5', '801006', '预算与采购正常最小执行进度', '8010', '80.00', '1');
INSERT INTO `t_system_setting` VALUES ('lwvhsBSjN8AwI59tfNA', '801004', '电力容量正常最大使用率', '8010', '80.00', '1');
INSERT INTO `t_system_setting` VALUES ('uuiwWkifw3NmHqXXPLX', '801001', '暖通系统水冷机组运行时长正常最大差值', '8010', '500.00', '1');
INSERT INTO `t_system_setting` VALUES ('Y9Z4X0JVtcmz8iHm2h0', '801005', '能耗PUE正常最大值', '8010', '1.90', '1');
INSERT INTO `t_system_setting` VALUES ('ZDvdK5rfRepCI7Chjjm', '80', '运行情况分析', 'root', null, '1');
INSERT INTO `t_system_setting` (`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('qfgPFO0Thj0zbqteQ1ti', '601040', '总体预算分析截止月份', '6010', '30.00', '1');
INSERT INTO `t_system_setting` (`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('qzPFh0Tgj0zbqteQ1ti', '601030', '总体预算采购执行百分比', '6010', '20.00', '1');
INSERT INTO `t_system_setting` (`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('qzPFh0Thj0zbqteQ1ti', '601020', '单项预算分析截止月份', '6010', '7.00', '1');
INSERT INTO `t_system_setting` (`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('qzPFO0Thj0zbqteQ1ti', '601010', '单项预算采购执行百分比', '6010', '30.00', '1');
INSERT INTO `t_system_setting` (`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('qzPFO0Txj0zbqteQ1ti', '6010', '执行百分比', '60', '30.00', '1');
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('11dFO8tYNlpZ4Hx8usa', '104010', '一级阀值', '1040', 0, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('22dFO8tYNlpZ4Hx8usa', '104020', '二级阀值', '1040', 30, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('33dFO8tYNlpZ4Hx8usa', '104030', '三级阀值', '1040', 50, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('44dFO8tYNlpZ4Hx8usa', '104040', '四级阀值', '1040', 70, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('55dFO8tYNlpZ4Hx8usa', '104050', '五级阀值', '1040', 90, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('11dFO8tYNlpZ4Hx8usb', '105010', '一级阀值', '1050', 0, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('22dFO8tYNlpZ4Hx8usb', '105020', '二级阀值', '1050', 30, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('33dFO8tYNlpZ4Hx8usb', '105030', '三级阀值', '1050', 50, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('44dFO8tYNlpZ4Hx8usb', '105040', '四级阀值', '1050', 70, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('55dFO8tYNlpZ4Hx8usb', '105050', '五级阀值', '1050', 90, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('11dFO8tYNlpZ4Hx8usc', '106010', '一级阀值', '1060', 0, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('22dFO8tYNlpZ4Hx8usc', '106020', '二级阀值', '1060', 30, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('33dFO8tYNlpZ4Hx8usc', '106030', '三级阀值', '1060', 50, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('44dFO8tYNlpZ4Hx8usc', '106040', '四级阀值', '1060', 70, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('55dFO8tYNlpZ4Hx8usc', '106050', '五级阀值', '1060', 90, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('11dFO8tYNlpZ4Hx8usd', '107010', '一级阀值', '1070', 0, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('22dFO8tYNlpZ4Hx8usd', '107020', '二级阀值', '1070', 30, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('33dFO8tYNlpZ4Hx8usd', '107030', '三级阀值', '1070', 50, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('44dFO8tYNlpZ4Hx8usd', '107040', '四级阀值', '1070', 70, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('55dFO8tYNlpZ4Hx8usd', '107050', '五级阀值', '1070', 90, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('11dFO8tYNlpZ4Hx8use', '108010', '一级阀值', '1080', 0, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('22dFO8tYNlpZ4Hx8use', '108020', '二级阀值', '1080', 30, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('33dFO8tYNlpZ4Hx8use', '108030', '三级阀值', '1080', 50, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('44dFO8tYNlpZ4Hx8use', '108040', '四级阀值', '1080', 70, 1);
INSERT INTO `t_system_setting`(`id`, `code`, `code_name`, `parent_code`, `value`, `check_status`) VALUES ('55dFO8tYNlpZ4Hx8use', '108050', '五级阀值', '1080', 90, 1);




DROP TABLE IF EXISTS `t_system_input`;
CREATE TABLE `t_system_input` (
  `id` varchar(20) CHARACTER SET utf8 NOT NULL,
  `code` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT 'code 编码',
  `code_name` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '编码名称',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0/1 ',
  `parent_code` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '父类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `t_system_input` VALUES ('0dOLS8etDu0itO3wlaP', '104020', '物业', '0', '1040');
INSERT INTO `t_system_input` VALUES ('0zmSPJ7lF5v614RM8sx', '101040', '23栋机房楼二三层', '0', '1010');
INSERT INTO `t_system_input` VALUES ('16QRNQVeP7M003jkCF1', '202016', '冷却水化学水处理器', '0', '2020');
INSERT INTO `t_system_input` VALUES ('19rz3GcAzGl5LO6lsox', '203010', '冷水机组', '0', '2030');
INSERT INTO `t_system_input` VALUES ('2pBGshI38VHda3zDlb8', '202022', '水源热泵', '0', '2020');
INSERT INTO `t_system_input` VALUES ('3330fr3KN006tsLOBE2', '202019', '加湿器', '0', '2020');
INSERT INTO `t_system_input` VALUES ('3FDg53rXV7OtrX50rgr', '102030', '电气', '0', '1020');
INSERT INTO `t_system_input` VALUES ('4CuDmb8kkWyrDS16RJs', '201020', '加湿器', '0', '2010');
INSERT INTO `t_system_input` VALUES ('4dXNc9118Ibi0yXucU6', '103040', '加湿器', '0', '1030');
INSERT INTO `t_system_input` VALUES ('4vpIS38h91ctJKEyLAa', '1020', '涉及系统', '0', '10');
INSERT INTO `t_system_input` VALUES ('592IVUdCKfWev2IzOyp', '202015', '冷水机组', '0', '2020');
INSERT INTO `t_system_input` VALUES ('6LGOgYVUrN365ckz223', '104010', '金科', '0', '1040');
INSERT INTO `t_system_input` VALUES ('6VjYmaGp2rL1E6Fdv7o', '202023', 'VRV空调', '0', '2020');
INSERT INTO `t_system_input` VALUES ('76JZPmOTt59v5uenb24', '102040', '弱电', '0', '1020');
INSERT INTO `t_system_input` VALUES ('7x1RPCnl9GolJlm5Km4', '105010', '日常巡检', '0', '1050');
INSERT INTO `t_system_input` VALUES ('7xy9FbTs41swpaAzeby', '302030', '常规类', '0', '3020');
INSERT INTO `t_system_input` VALUES ('8IXMGFmzgO3xiuO5iG7', '102020', '装饰', '0', '1020');
INSERT INTO `t_system_input` VALUES ('8JjU8lYc75FrvHfICRq', '203012', '压缩机 ', '0', '2030');
INSERT INTO `t_system_input` VALUES ('9PG0FJsga9K2iizJUm3', '201060', '水源热泵', '0', '2010');
INSERT INTO `t_system_input` VALUES ('9pp8Mde9zjp2cXJohp7', '106030', '日常运维', '0', '1060');
INSERT INTO `t_system_input` VALUES ('aeFscLVaHk1jPM5XZeE', '202020', '新风机', '0', '2020');
INSERT INTO `t_system_input` VALUES ('aG1jXPffXSqsKRS5ET7', '101020', '能源楼', '0', '1010');
INSERT INTO `t_system_input` VALUES ('AIDVuEc6hlJlhRxKOvv', '3010', '费用类型', '0', '30');
INSERT INTO `t_system_input` VALUES ('ajeYoGbr52VNj9a9YHh', '201040', '新风机', '0', '2010');
INSERT INTO `t_system_input` VALUES ('aUSc3pFBFgDQLaaLWBa', '105020', '风险专项排查', '0', '1050');
INSERT INTO `t_system_input` VALUES ('azEUc7h7hHP7ySNOJNY', '202017', '自动排气定压补水装置', '0', '2020');
INSERT INTO `t_system_input` VALUES ('bcf0GFvmHST6rYCdYGS', '103010', '除湿机', '0', '1030');
INSERT INTO `t_system_input` VALUES ('bXUKtlOjyr2LRBnfRWP', '301020', '资本性', '0', '3010');
INSERT INTO `t_system_input` VALUES ('c0MqZ4PlsBZD3K8yr9i', '106020', '配电系统', '0', '1060');
INSERT INTO `t_system_input` VALUES ('CkizpZZTAEJtXiKzqmS', '202014', '冷却塔', '0', '2020');
INSERT INTO `t_system_input` VALUES ('csFT10GPfnDuJMGfkEB', '202013', '板式热交换器 ', '0', '2020');
INSERT INTO `t_system_input` VALUES ('CZImoVTxhACFpeVrS4p', '203014', '布水器', '0', '2030');
INSERT INTO `t_system_input` VALUES ('d0j41QdFacg3KmKWUsH', '203019', '过滤网', '0', '2030');
INSERT INTO `t_system_input` VALUES ('dVokfWT5rSjowIFeG1X', '3020', '费用分类', '0', '30');
INSERT INTO `t_system_input` VALUES ('DWNUIb73T3hgZYSJTfq', '10', '风控管理', '0', 'root');
INSERT INTO `t_system_input` VALUES ('DXCA1amgFzRv1CZHjfy', '30', '预算与采购', '0', 'root');
INSERT INTO `t_system_input` VALUES ('E39DZI2kbF7gnlMW4oZ', '20', '维护保养计划', '0', 'root');
INSERT INTO `t_system_input` VALUES ('e7NwXl0OZftBOBPmvfB', '203021', '制冷循环系统', '0', '2030');
INSERT INTO `t_system_input` VALUES ('eDYbuTTjmjPxOPJeTl4', '103030', '装饰', '0', '1030');
INSERT INTO `t_system_input` VALUES ('eQn67IvwrrTcZ5wk9Fq', '106010', '暖通系统', '0', '1060');
INSERT INTO `t_system_input` VALUES ('feff6AUkVwCgbrCI5hE', '203024', '化学过滤系统', '0', '2030');
INSERT INTO `t_system_input` VALUES ('FicIpDww6ybtOUUkOVZ', '1050', '问题来源', '0', '10');
INSERT INTO `t_system_input` VALUES ('FsKslxjP7BiosHBDsJO', '203023', '湿膜', '0', '2030');
INSERT INTO `t_system_input` VALUES ('GQjiHOYq37HTe9g0mz9', '202010', '蓄冷罐', '0', '2020');
INSERT INTO `t_system_input` VALUES ('gqvDYlbH4jqI3wnlIHP', '102050', '流程管理', '0', '1020');
INSERT INTO `t_system_input` VALUES ('h9qi4reHA0E6fqElBKB', '203015', '淋水填料', '0', '2030');
INSERT INTO `t_system_input` VALUES ('iC7fJLw3dlNitoVzdhG', '203011', '蒸发器、冷凝器', '0', '2030');
INSERT INTO `t_system_input` VALUES ('IRPZfQXSaDiq9wfBPj2', '302010', '单列', '0', '3020');
INSERT INTO `t_system_input` VALUES ('JCwF2o3tipgu6aiGBEf', '203020', '保温', '0', '2030');
INSERT INTO `t_system_input` VALUES ('LFopbapFdInvOjANhyM', '103020', 'UPS及其电池', '0', '1030');
INSERT INTO `t_system_input` VALUES ('lJcOhdBqyCKVmwtF6Ty', '102010', '暖通', '0', '1020');
INSERT INTO `t_system_input` VALUES ('Lkw1nm2Wyr79G5kxrvj', '203013', '冷水盘', '0', '2030');
INSERT INTO `t_system_input` VALUES ('LvYKFGqhC37SNg9ZnSE', '201030', '给排水', '0', '2010');
INSERT INTO `t_system_input` VALUES ('LxGSCv5lNbp6w6KbDBx', '202021', '给排水', '0', '2020');
INSERT INTO `t_system_input` VALUES ('mmcDch6IpAZg8hGBLeb', '102060', '消防', '0', '1020');
INSERT INTO `t_system_input` VALUES ('mSBNl48OA2KBfcIQ6PJ', '2010', '子系统', '0', '20');
INSERT INTO `t_system_input` VALUES ('MzrRc6boUdMyyAfNWOb', '202011', '冷却水泵/冷冻水泵 ', '0', '2020');
INSERT INTO `t_system_input` VALUES ('n47TAxtre2dR3rwoX3T', '203016', '电气控制', '0', '2030');
INSERT INTO `t_system_input` VALUES ('O8T1YAbTdCwPuTPF4ZM', '101030', '能源楼二层', '0', '1010');
INSERT INTO `t_system_input` VALUES ('OrAFd0Ep1CEohAGOxbF', '201050', 'VRV空调', '0', '2010');
INSERT INTO `t_system_input` VALUES ('OrXmBhEatYYiIX8YWO1', '1040', '涉及界面', '0', '10');
INSERT INTO `t_system_input` VALUES ('ovCyasLdAtNG0JY872v', '101010', '23栋精密空调间', '0', '1010');
INSERT INTO `t_system_input` VALUES ('PINheHIg8nr6AOjAzjm', '2020', '设备', '0', '20');
INSERT INTO `t_system_input` VALUES ('PVDvoYMkVXfqn0Z3JHf', '203018', '风机', '0', '2030');
INSERT INTO `t_system_input` VALUES ('Q0QNStuOEP1lUkAwmhn', '203025', '热湿处理系统', '0', '2030');
INSERT INTO `t_system_input` VALUES ('Q1JlEoNQDawzEwxP3Eg', '201070', '通风', '0', '2010');
INSERT INTO `t_system_input` VALUES ('SQacf1nfmGl665uUB8Z', '1060', '风险类型', '0', '10');
INSERT INTO `t_system_input` VALUES ('sqe5xbxjEPYy25swcBD', '1010', '位置/区域', '0', '10');
INSERT INTO `t_system_input` VALUES ('stwTGf0aH5btHilR3ki', '301010', '费用性', '0', '3010');
INSERT INTO `t_system_input` VALUES ('t02TwhulDALWGfaufpI', '202018', '精密空调', '0', '2020');
INSERT INTO `t_system_input` VALUES ('T7V3GCEH56CxqpLyZwr', '203017', '制冷剂', '0', '2030');
INSERT INTO `t_system_input` VALUES ('TCj5GAybNsCoHwWgQXy', '2030', '部件', '0', '20');
INSERT INTO `t_system_input` VALUES ('tv90qtEANGKYr01j8vI', '203022', '水系统', '0', '2030');
INSERT INTO `t_system_input` VALUES ('v0n1kXICBnYFUb8FVZR', '201010', '水冷系统', '0', '2010');
INSERT INTO `t_system_input` VALUES ('wrGPLrtOnqgCkHuINHU', '1030', '子系统', '0', '10');
INSERT INTO `t_system_input` VALUES ('x2WWEj3upIFt68BB3EC', '302020', '尾款', '0', '3020');
INSERT INTO `t_system_input` VALUES ('yvRCEg8guNgxRW26RRL', '202012', '冷却水/冷冻水旁流水处理器 ', '0', '2020');


DROP TABLE IF EXISTS `t_asset_basic_info`;
CREATE TABLE `t_asset_basic_info` (
  `id` varchar(50) NOT NULL COMMENT '主键id',
  `asset_number` varchar(50) DEFAULT NULL COMMENT '资产编号',
  `asset_name` varchar(100) DEFAULT NULL COMMENT '资产名称',
  `asset_type` varchar(50) DEFAULT NULL COMMENT '资产分类',
  `asset_status` varchar(10) DEFAULT NULL COMMENT '资产状态',
  `vendor_type` varchar(100) DEFAULT NULL COMMENT '厂商分类',
  `serial_number` varchar(50) DEFAULT NULL COMMENT '序列号',
  `asset_description` varchar(100) DEFAULT NULL COMMENT '资产描述',
  `asset_location` varchar(100) DEFAULT NULL COMMENT '资产位置',
  `subordinate_item` varchar(50) DEFAULT NULL COMMENT '所属项目',
  `rated_power` decimal(10,2) DEFAULT NULL COMMENT '额定功率',
  `subordinate_dept` varchar(50) DEFAULT NULL COMMENT '所属部门',
  `owner` varchar(50) DEFAULT NULL COMMENT '所有者',
  `owner_phone` varchar(50) DEFAULT NULL COMMENT '所有者电话',
  `owner_email` varchar(50) DEFAULT NULL COMMENT '所有者邮箱',
  `racking_time` bigint(20) DEFAULT NULL COMMENT '上架时间(时间戳)',
  `is_deleted` int(1) DEFAULT '0' COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间(时间戳)',
  `update_user` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间(时间戳)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_asset_maintenance_contract
-- ----------------------------
DROP TABLE IF EXISTS `t_asset_maintenance_contract`;
CREATE TABLE `t_asset_maintenance_contract` (
  `contract_id` varchar(50) NOT NULL COMMENT '合同id',
  `contract_number` varchar(50) DEFAULT NULL COMMENT '合同编号',
  `contract_name` varchar(100) DEFAULT NULL COMMENT '合同名称',
  `cost_center` varchar(50) DEFAULT NULL COMMENT '成本中心',
  `contract_type` varchar(10) DEFAULT NULL COMMENT '合同类型',
  `maintenance_start_time` bigint(20) DEFAULT NULL COMMENT '维保起始时间',
  `maintenance_end_time` bigint(20) DEFAULT NULL COMMENT '维保结束时间',
  `maintenance_type` varchar(50) DEFAULT NULL COMMENT '维保类型',
  `maintenance_service_mode` varchar(50) DEFAULT NULL COMMENT '维保服务方式',
  `maintenance_factory` varchar(100) DEFAULT NULL COMMENT '维保厂家',
  `underwriting_type` varchar(50) DEFAULT NULL COMMENT '承保类型',
  `maintenance_contact` varchar(50) DEFAULT NULL COMMENT '维保联系人',
  `maintenance_contact_phone` varchar(50) DEFAULT NULL COMMENT '维保联系电话',
  `warranty_reminder` varchar(50) DEFAULT NULL COMMENT '过保提醒',
  `warranty_reminder_person` varchar(50) DEFAULT NULL COMMENT '过保提醒人',
  `warranty_reminder_phone` varchar(50) DEFAULT NULL COMMENT '过保提醒人电话',
  `warranty_reminder_email` varchar(50) DEFAULT NULL COMMENT '过保提醒人邮箱',
  `asset_number` varchar(50) DEFAULT NULL COMMENT '维保资产编号',
  `is_deleted` int(1) DEFAULT '0' COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间(时间戳)',
  `update_user` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间(时间戳)',
  PRIMARY KEY (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_maintenance_plan`;
CREATE TABLE `t_maintenance_plan` (
  `maintenance_plan_id` varchar(50) NOT NULL COMMENT '维保计划id',
  `child_system` varchar(100) DEFAULT NULL COMMENT '子系统',
  `device` varchar(50) DEFAULT NULL COMMENT '设备',
  `period` int(10) DEFAULT NULL COMMENT '周期(枚举:周、月、季、年)',
  `component` varchar(50) DEFAULT NULL COMMENT '部件',
  `maintenance_plan_name` varchar(100) DEFAULT NULL COMMENT '维护保养项目名称',
  `is_once_plan` int(1) DEFAULT NULL COMMENT '是否为一次性计划(0:否；1:是)',
  `maintenance_start_time` bigint(20) DEFAULT NULL COMMENT '维保项目开始执行时间',
  `execute_month` int(10) DEFAULT NULL COMMENT '执行月数(每季度/半年度/年度第xx月)',
  `execute_week` int(10) DEFAULT NULL COMMENT '执行周数(每月第xx周)',
  `delay_time` int(10) DEFAULT NULL COMMENT '延期时长(延期xx倍周期)',
  `status` int(1) DEFAULT NULL COMMENT '状态(正常、暂停、延期)',
  `suspend_time` bigint(20) DEFAULT NULL COMMENT '暂停时间(时间戳)',
  `is_manually_added` int(1) DEFAULT NULL COMMENT '是否为手动新增(0:是；1:否)',
  `activity_id` varchar(50) DEFAULT NULL COMMENT '相关运维管理活动id',
  `maintenance_user` varchar(50) DEFAULT NULL COMMENT '维保项目负责人',
  `is_deleted` int(1) DEFAULT '0' COMMENT '0 未删除 1 已删除',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间(时间戳)',
  `update_user` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间(时间戳)',
  `pre_reminder_time` int(10) DEFAULT NULL COMMENT '预提醒时间(单位:天)',
  PRIMARY KEY (`maintenance_plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `t_plan_execute_situation`;
CREATE TABLE `t_plan_execute_situation` (
  `id` varchar(50) NOT NULL,
  `maintenance_plan_id` varchar(50) NOT NULL,
  `plan_execute_time` bigint(20) DEFAULT NULL COMMENT '计划执行时间(时间戳)',
  `execute_status` int(10) DEFAULT NULL COMMENT '执行状态(枚举)',
  `actual_execute_time` bigint(20) DEFAULT NULL COMMENT '实际执行时间(时间戳)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
