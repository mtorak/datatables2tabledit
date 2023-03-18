package com.dt2te.poc.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IpRelationsModel implements Serializable {

  private Integer id;

  private DocFieldModel ipField;
  private DocFieldModel portField;
  private DocFieldModel startDateField;
  private DocFieldModel startTimeField;

  private DocFieldModel endDateField;
  private DocFieldModel endTimeField;

}
