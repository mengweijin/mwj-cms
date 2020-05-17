quartz-2.2.3-distribution.tar.gz
该压缩包里包含quartz持久化任务到数据库中的表结构，需要根据不同的数据库选择需要的脚本提前根据脚本创建好表结构。
路径为：quartz-2.2.3\docs\dbTables

####################################################################################################################
## Quartz 实际并不关心你是在相同的还是不同的机器上运行节点。当集群是放置在不同的机器上时，通常称之为水平集群。
## 节点是跑在同一台机器是，称之为垂直集群。对于垂直集群，存在着单点故障的问题。这对高可用性的应用来说是个坏消息，
## 因为一旦机器崩溃了，所有的节点也就被有效的终止了。
## 当你运行水平集群时，时钟应当要同步，以免出现离奇且不可预知的行为。假如时钟没能够同步，Scheduler 实例将对其他节点的状态产生混乱。
## 有几种简单的方法来保证时钟何持同步，而且也没有理由不这么做。
## 最简单的同步计算机时钟的方式是使用某一个 Internet 时间服务器(Internet Time Server ITS)。
## 没什么会阻止你在相同环境中使用集群的和非集群的 Quartz 应用。唯一要注意的是这两个环境不要混用在相同的数据库表。
## 意思是非集群环境不要使用与集群应用相同的一套数据库表；否则将得到希奇古怪的结果，集群和非集群的 Job 都会遇到问题。
####################################################################################################################

表与字段说明：

quartz框架中T_TASK_TRIGGERS表 TRIGGER_STATE 字段显示任务的属性大概状态有这几种：
WAITING:等待
PAUSED:暂停
ACQUIRED:正常执行
BLOCKED：阻塞
ERROR：错误
COMPLETE: 完成
主要使用以上这几种状态控制调度各节点定时任务:

QRTZ_CALENDARS 以 Blob 类型存储 Quartz 的 Calendar 信息
QRTZ_CRON_TRIGGERS 存储 Cron Trigger，包括Cron表达式和时区信息
QRTZ_FIRED_TRIGGERS 存储与已触发的 Trigger 相关的状态信息，以及相联 Job的执行信息QRTZ_PAUSED_TRIGGER_GRPS 存储已暂停的 Trigger组的信息
QRTZ_SCHEDULER_STATE 存储少量的有关 Scheduler 的状态信息，和别的Scheduler实例(假如是用于一个集群中)
QRTZ_LOCKS 存储程序的悲观锁的信息(假如使用了悲观锁)
QRTZ_JOB_DETAILS 存储每一个已配置的 Job 的详细信息
QRTZ_JOB_LISTENERS 存储有关已配置的 JobListener的信息
QRTZ_SIMPLE_TRIGGERS存储简单的Trigger，包括重复次数，间隔，以及已触的次数
QRTZ_BLOG_TRIGGERS Trigger 作为 Blob 类型存储(用于 Quartz 用户用JDBC创建他们自己定制的 Trigger 类型，JobStore并不知道如何存储实例的时候)
QRTZ_TRIGGER_LISTENERS 存储已配置的 TriggerListener的信息
QRTZ_TRIGGERS 存储已配置的 Trigger 的信息

表qrtz_job_details:保存job详细信息,该表需要用户根据实际情况初始化
job_name:集群中job的名字,该名字用户自己可以随意定制,无强行要求
job_group:集群中job的所属组的名字,该名字用户自己随意定制,无强行要求
job_class_name:集群中个notejob实现类的完全包名,quartz就是根据这个路径到classpath找到该job类
is_durable:是否持久化,把该属性设置为1，quartz会把job持久化到数据库中
job_data:一个blob字段，存放持久化job对象

表qrtz_triggers: 保存trigger信息
trigger_name:trigger的名字,该名字用户自己可以随意定制,无强行要求
trigger_group:trigger所属组的名字,该名字用户自己随意定制,无强行要求
job_name:qrtz_job_details表job_name的外键
job_group:qrtz_job_details表job_group的外键
trigger_state:当前trigger状态，设置为ACQUIRED,如果设置为WAITING,则job不会触发
trigger_cron:触发器类型,使用cron表达式

表qrtz_cron_triggers:存储cron表达式表
trigger_name:qrtz_triggers表trigger_name的外键
trigger_group:qrtz_triggers表trigger_group的外键
cron_expression:cron表达式

表qrtz_scheduler_state:存储集群中note实例信息，quartz会定时读取该表的信息判断集群中每个实例的当前状态
instance_name:之前配置文件中org.quartz.scheduler.instanceId配置的名字，就会写入该字段，如果设置为AUTO,quartz会根据物理机名和当前时间产生一个名字
last_checkin_time:上次检查时间
checkin_interval:检查间隔时间






下面是各种不同triigger对应的不同misfire策略（摘自网络）：
CronTrigger

withMisfireHandlingInstructionDoNothing
——不触发立即执行
——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行

withMisfireHandlingInstructionIgnoreMisfires
——以错过的第一个频率时间立刻开始执行
——重做错过的所有频率周期后
——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行

withMisfireHandlingInstructionFireAndProceed(默认)
——以当前时间为触发频率立刻触发一次执行
——然后按照Cron频率依次执行


SimpleTrigger

withMisfireHandlingInstructionFireNow
——以当前时间为触发频率立即触发执行
——执行至FinalTIme的剩余周期次数
——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值

withMisfireHandlingInstructionIgnoreMisfires
——以错过的第一个频率时间立刻开始执行
——重做错过的所有频率周期
——当下一次触发频率发生时间大于当前时间以后，按照Interval的依次执行剩下的频率
——共执行RepeatCount+1次

withMisfireHandlingInstructionNextWithExistingCount
——不触发立即执行
——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
——以startTime为基准计算周期频率，并得到FinalTime
——即使中间出现pause，resume以后保持FinalTime时间不变


withMisfireHandlingInstructionNowWithExistingCount（默认）
——以当前时间为触发频率立即触发执行
——执行至FinalTIme的剩余周期次数
——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到
——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值

withMisfireHandlingInstructionNextWithRemainingCount
——不触发立即执行
——等待下次触发频率周期时刻，执行至FinalTime的剩余周期次数
——以startTime为基准计算周期频率，并得到FinalTime
——即使中间出现pause，resume以后保持FinalTime时间不变

withMisfireHandlingInstructionNowWithRemainingCount
——以当前时间为触发频率立即触发执行
——执行至FinalTIme的剩余周期次数
——以调度或恢复调度的时刻为基准的周期频率，FinalTime根据剩余次数和当前时间计算得到

——调整后的FinalTime会略大于根据starttime计算的到的FinalTime值

MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT
——此指令导致trigger忘记原始设置的starttime和repeat-count
——触发器的repeat-count将被设置为剩余的次数
——这样会导致后面无法获得原始设定的starttime和repeat-count值