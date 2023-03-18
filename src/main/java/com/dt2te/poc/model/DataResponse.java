package com.dt2te.poc.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataResponse implements Serializable {

  private List<DocFieldModel> fields;
  private List<Map<Integer, String>> validFieldSelects;

  private List<IpRelationsModel> ipRelations;
  private List<Map<Integer, String>> validIpRelationsSeletcs;

  @Builder
  public DataResponse(boolean success, String msg, boolean hasWarning, List<DocFieldModel> fields, List<IpRelationsModel> ipRelations,
      List<Map<Integer, String>> validFieldSelects, List<Map<Integer, String>> validIpRelationsSeletcs) {
    this.fields = fields;
    this.ipRelations = ipRelations;
    this.validFieldSelects = validFieldSelects;
    this.validIpRelationsSeletcs = validIpRelationsSeletcs;
  }

}
