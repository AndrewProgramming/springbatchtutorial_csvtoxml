package com.adnrew.course.springbatch;

import com.adnrew.course.springbatch.entity.Tutorial;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class ReportFieldSetMapper implements FieldSetMapper<Tutorial> {

  @Override
  public Tutorial mapFieldSet(FieldSet fieldSet) throws BindException {

    //Instantiating the report object
    Tutorial tutorial = new Tutorial();

    //Setting the fields
    tutorial.setTutorial_id(fieldSet.readInt(0));
    tutorial.setTutorial_author(fieldSet.readString(1));
    tutorial.setTutorial_title(fieldSet.readString(2));
    tutorial.setSubmission_date(fieldSet.readString(3));

    return tutorial;
  }
}