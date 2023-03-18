package com.dt2te.poc.controller;

import com.dt2te.poc.model.DataResponse;
import com.dt2te.poc.model.UpdateDocFieldResponse;
import com.dt2te.poc.model.UpdateIpRelationsResponse;
import com.dt2te.poc.service.FieldTypeService;
import com.dt2te.poc.service.Step3TempService;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/step3")
public class Step3RestController {

  private final FieldTypeService fieldTypeService;
  private final Step3TempService step3TempService;

  private static final String ACTION_EDIT = "edit";
  private static final String ACTION_DELETE = "delete";

  public Step3RestController(FieldTypeService fieldTypeService, Step3TempService step3TempService) {
    this.fieldTypeService = fieldTypeService;
    this.step3TempService = step3TempService;
  }

  // doc fields
  @GetMapping(value = "/docuscanDocFieldResult", produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse docuscanDocFieldResult() {
    log.info("Getting docuscan DocField results from db..");

    Map<Integer, String> availableFieldTypes = fieldTypeService.getAvailableFieldTypes();

    return DataResponse.builder()
        .fields(step3TempService.getDocFieldsForJob())
        .validFieldSelects(Stream.of(availableFieldTypes).collect(Collectors.toList()))
        .build();
  }

  @PostMapping("/updateDocField")
  public UpdateDocFieldResponse updateDocField(@RequestParam Map<String, String> body, HttpSession session) throws Exception {
    log.info(body.toString());

    try {
      if (body.get("action").equals(ACTION_EDIT)) {

        if (StringUtils.isNotEmpty(body.get("fieldId"))) {
          step3TempService.updateDocField(Integer.valueOf(body.get("fieldId")), body.get("value"),
              Integer.valueOf(body.get("fieldTypeId")));
        } else {
          step3TempService.saveDocField(body.get("value"), Integer.valueOf(body.get("fieldTypeId")));
        }

      } else if (body.get("action").equals(ACTION_DELETE)) {
        if (StringUtils.isNotEmpty(body.get("fieldId"))) {
          step3TempService.deleteDocField(Integer.valueOf(body.get("fieldId")));
        } else {
          log.warn("No doc_field by docFieldId: {}", body.get("fieldId"));
        }
      }
    } catch (Exception e) {
      log.error("Failed to update DocField!");
      throw new Exception("Failed to update DocField: " + e.getMessage());
    }

    return UpdateDocFieldResponse.builder()
        .success(true)
        .build();
  }

  // ip relations
  @GetMapping(value = "/docuscanIpRelResult", produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse docuscanIpRelResult() {
    log.info("Getting docuscan DocFieldIpRelations results from db..");

    return step3TempService.getIpRelations();
  }

  @PostMapping("/updateIpRelation")
  public UpdateIpRelationsResponse updateIpRelation(@RequestParam Map<String, String> body, HttpSession session) throws Exception {

    String resulMsg = "";

    try {
      if (body.get("action").equals(ACTION_EDIT)) {

        if (StringUtils.isNotEmpty(body.get("ipRelId"))) {
          step3TempService.updateIpRelation(Integer.valueOf(body.get("ipRelId")), Integer.valueOf(body.get("ipField.id")),
              Integer.valueOf(body.get("portField.id")), Integer.valueOf(body.get("startDateField.id")),
              Integer.valueOf(body.get("startTimeField.id")));
          resulMsg = "Updated IpRelations successfully!";
        } else {
          step3TempService.saveIpRelation(Integer.valueOf(body.get("ipField.id")),
              Integer.valueOf(body.get("portField.id")), Integer.valueOf(body.get("startDateField.id")),
              Integer.valueOf(body.get("startTimeField.id")));
          resulMsg = "Saved new IpRelations successfully!";
        }

      } else if (body.get("action").equals(ACTION_DELETE)) {
        if (StringUtils.isNotEmpty(body.get("ipRelId"))) {
          step3TempService.deleteIpRelation(Integer.valueOf(body.get("ipRelId")));
          resulMsg = "Deleted IpRelations successfully!";
        } else {
          log.warn("No doc_field by docFieldId: {}", body.get("fieldId"));
        }
      }

    } catch (Exception e) {
      log.error("Failed to update DocField!");
      throw new Exception("Failed to update IpRelations: " + e.getMessage());
    }
    return UpdateIpRelationsResponse.builder()
        .success(true)
        .build();
  }

}
