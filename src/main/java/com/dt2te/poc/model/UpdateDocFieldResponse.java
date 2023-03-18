package com.dt2te.poc.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UpdateDocFieldResponse implements Serializable {

  private boolean success;

}
