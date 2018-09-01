## Author 
(Andrew Deng)[www.zhiyueinfo.com]

## Overview

在本节里，我将使用Spring Batch 框架从 CSV 中读取信息，经过简单处理，写入到XML 文件里。

## 输入的report.csv文件

```
   1001,"Sanjay"        ,"Learn Java"  ,06/05/2007
   1002,"Abdul S"       ,"Learn MySQL" ,19/04/2007
   1003,"Krishna Kasyap","Learn JavaFX",06/07/2017
```

## 输出的文件tutorials.xml

```$xslt
<?xml version="1.0" encoding="UTF-8"?>
<tutorials>
  <tutorial tutorial_id="1001">
    <submission_date>06/05/2007</submission_date>
    <tutorial_author>Sanjay</tutorial_author>
    <tutorial_title>Learn Java</tutorial_title>
  </tutorial>
  <tutorial tutorial_id="1002">
    <submission_date>19/04/2007</submission_date>
    <tutorial_author>Abdul S</tutorial_author>
    <tutorial_title>Learn MySQL</tutorial_title>
  </tutorial>
  <tutorial tutorial_id="1003">
    <submission_date>06/07/2017</submission_date>
    <tutorial_author>Krishna Kasyap</tutorial_author>
    <tutorial_title>Learn JavaFX</tutorial_title>
  </tutorial>
</tutorials>
```

## Reader

`Reader`，这里我们使用 `FlatFileItemReader` 。

该`bean`定义在_jobConfig.xml_里。



```
<bean id="cvsFileItemReader" class="org.springframework.batch.item.file.FlatFileItemReader">
<property name="resource" value="report.csv"/>
    … …
</bean>
```

## Writer

`Writer`我们使用`StaxEventItemWriter`，同样定义在_jobConfig.xml_里：

```
<bean id="xmlItemWriter" class="org.springframework.batch.item.xml.StaxEventItemWriter">
    … …
</bean>
```

## Processor

定义在 _jobConfig.xml_ 里。

```
<bean id="itemProcessor" class="com.adnrew.course.springbatch.CustomItemProcessor"/>
```

在这个例子中，这个`processor`只是简单的打印了两条信息。

```
public class CustomItemProcessor implements ItemProcessor<Tutorial, Tutorial> {


  @Override
  public Tutorial process(Tutorial item) throws Exception {
    System.out.println("Processing..." + item);

    System.out.println("Hello");
    return item;
  }
}  
```

## Job的定义

Job的定义。把`Reader`，`Processor`，`Writer` 串联起来。
这里只有一个`Step`.

```$xslt
   <batch:job id = "helloWorldJob">    
      <batch:step id = "step1"> 
         <batch:tasklet> 
            <batch:chunk reader = "cvsFileItemReader" writer = "xmlItemWriter" 
               processor = "itemProcessor" commit-interval = "10"> 
            </batch:chunk> 
         </batch:tasklet> 
      </batch:step> 
   </batch:job>  
```

## Tutorial.java

实体类，使用注解的形式把实体类和`XML`的tag对应起来

```$xslt
import javax.xml.bind.annotation.XmlAttribute; 
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlRootElement;  

@XmlRootElement(name = "tutorial") 
public class Tutorial {  
   private int tutorial_id; 
   private String tutorial_author; 
   private String tutorial_title;
   private String submission_date;  
 
   @XmlAttribute(name = "tutorial_id") 
   public int getTutorial_id() { 
      return tutorial_id; 
   }  
 
   public void setTutorial_id(int tutorial_id) { 
      this.tutorial_id = tutorial_id; 
   }  
 
   @XmlElement(name = "tutorial_author") 
   public String getTutorial_author() { 
      return tutorial_author; 
   }  
   public void setTutorial_author(String tutorial_author) { 
      this.tutorial_author = tutorial_author; 
   }  
      
   @XmlElement(name = "tutorial_title") 
   public String getTutorial_title() { 
      return tutorial_title; 
   }  
   
   public void setTutorial_title(String tutorial_title) { 
      this.tutorial_title = tutorial_title; 
   }  
   
   @XmlElement(name = "submission_date") 
   public String getSubmission_date() { 
      return submission_date; 
   }  
   
   public void setSubmission_date(String submission_date) { 
      this.submission_date = submission_date; 
   } 
   
   @Override 
   public String toString() { 
      return "  [Tutorial id=" + tutorial_id + ", 
         Tutorial Author=" + tutorial_author  + ", 
         Tutorial Title=" + tutorial_title + ", 
         Submission Date=" + submission_date + "]"; 
   } 
}  
```

## 主程序入口 

```$xslt
@SpringBootApplication
public class SpringbatchApplication {

  public static void main(String[] args)
      throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {


    String[] springConfig = {"jobConfig.xml"};

    // Creating the application context object
    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

    // Creating the job launcher
    JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");

    // Creating the job
    Job job = (Job) context.getBean("helloWorldJob");

    // Executing the JOB
    JobExecution execution = jobLauncher.run(job, new JobParameters());
    System.out.println("Exit Status : " + execution.getStatus());

    SpringApplication.run(SpringbatchApplication.class, args);

  }
}

```

## 运行结果

```$xslt
...
20:02:25.884 [restartedMain] DEBUG org.springframework.batch.core.job.flow.support.SimpleFlow - Completed state=helloWorldJob.end3 with status=COMPLETED
20:02:25.884 [restartedMain] DEBUG org.springframework.batch.core.job.AbstractJob - Job execution complete: JobExecution: id=1, version=1, startTime=Fri Aug 31 20:02:25 CST 2018, endTime=null, lastUpdated=Fri Aug 31 20:02:25 CST 2018, status=COMPLETED, exitStatus=exitCode=COMPLETED;exitDescription=, job=[JobInstance: id=1, version=0, Job=[helloWorldJob]], jobParameters=[{}]
20:02:25.884 [restartedMain] DEBUG org.springframework.batch.support.transaction.ResourcelessTransactionManager - Creating new transaction with name [org.springframework.batch.core.repository.support.SimpleJobRepository.update]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
20:02:25.884 [restartedMain] DEBUG org.springframework.jdbc.core.JdbcTemplate - Executing prepared SQL query
20:02:25.884 [restartedMain] DEBUG org.springframework.jdbc.core.JdbcTemplate - Executing prepared SQL statement [SELECT VERSION FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID=?]
20:02:25.884 [restartedMain] DEBUG org.springframework.jdbc.datasource.DataSourceUtils - Fetching JDBC Connection from DataSource
20:02:25.884 [restartedMain] DEBUG org.springframework.jdbc.datasource.DriverManagerDataSource - Creating new JDBC DriverManager Connection to [jdbc:mysql://localhost:3307/details]
20:02:25.889 [restartedMain] DEBUG org.springframework.jdbc.datasource.DataSourceUtils - Registering transaction synchronization for JDBC Connection
20:02:25.890 [restartedMain] DEBUG org.springframework.jdbc.core.JdbcTemplate - Executing prepared SQL query
20:02:25.890 [restartedMain] DEBUG org.springframework.jdbc.core.JdbcTemplate - Executing prepared SQL statement [SELECT COUNT(*) FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID = ?]
20:02:25.891 [restartedMain] DEBUG org.springframework.jdbc.core.JdbcTemplate - Executing prepared SQL update
20:02:25.891 [restartedMain] DEBUG org.springframework.jdbc.core.JdbcTemplate - Executing prepared SQL statement [UPDATE BATCH_JOB_EXECUTION set START_TIME = ?, END_TIME = ?,  STATUS = ?, EXIT_CODE = ?, EXIT_MESSAGE = ?, VERSION = ?, CREATE_TIME = ?, LAST_UPDATED = ? where JOB_EXECUTION_ID = ? and VERSION = ?]
20:02:25.892 [restartedMain] DEBUG org.springframework.jdbc.core.JdbcTemplate - SQL update affected 1 rows
20:02:25.892 [restartedMain] DEBUG org.springframework.jdbc.datasource.DataSourceUtils - Returning JDBC Connection to DataSource
20:02:25.892 [restartedMain] DEBUG org.springframework.batch.support.transaction.ResourcelessTransactionManager - Initiating transaction commit
20:02:25.892 [restartedMain] DEBUG org.springframework.batch.support.transaction.ResourcelessTransactionManager - Committing resourceless transaction on [org.springframework.batch.support.transaction.ResourcelessTransactionManager$ResourcelessTransaction@1b8eaa16]
20:02:25.892 [restartedMain] INFO org.springframework.batch.core.launch.support.SimpleJobLauncher - Job: [FlowJob: [name=helloWorldJob]] completed with the following parameters: [{}] and the following status: [COMPLETED]
Exit Status : COMPLETED
...
```