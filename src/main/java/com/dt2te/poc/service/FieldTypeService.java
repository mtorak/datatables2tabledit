package com.dt2te.poc.service;

import com.dt2te.poc.model.FieldTypes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FieldTypeService {

  public Map<Integer, String> getAvailableFieldTypes(){

    Map<Integer, String> fieldTypeMap = new HashMap<>();
    Arrays.stream(FieldTypes.values()).forEach(ft -> fieldTypeMap.put(ft.getFieldTypeId(), FieldTypes.valueOfId(ft.getFieldTypeId()).getLabel()));
    return fieldTypeMap;
  }

}
