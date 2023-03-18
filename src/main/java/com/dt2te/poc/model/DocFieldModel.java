package com.dt2te.poc.model;

import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocFieldModel implements Serializable {

  private Integer id;
  private Integer fieldTypeId;
  private String fieldTypeText;
  private Integer docId;
  private Integer docContentId;

  private String value;
  private Timestamp addTime;
  private Boolean mustChange;

}
