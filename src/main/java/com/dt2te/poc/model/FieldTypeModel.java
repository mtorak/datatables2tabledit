package com.dt2te.poc.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldTypeModel implements Serializable {

  private Integer id;
  private String fieldType; // type name

}
