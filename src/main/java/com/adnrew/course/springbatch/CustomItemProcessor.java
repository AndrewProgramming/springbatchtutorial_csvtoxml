package com.adnrew.course.springbatch;

import com.adnrew.course.springbatch.entity.Tutorial;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Tutorial, Tutorial> {


  @Override
  public Tutorial process(Tutorial item) throws Exception {
    System.out.println("Processing..." + item);

    System.out.println("Hello");
    return item;
  }
}      