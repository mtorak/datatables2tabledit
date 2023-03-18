package com.dt2te.poc.service;

import com.dt2te.poc.model.DataResponse;
import com.dt2te.poc.model.DocFieldModel;
import com.dt2te.poc.model.FieldTypes;
import com.dt2te.poc.model.IpRelationsModel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

/**
 * @Deprecated In memory DocField & DocFieldIpRelations providing service to be used while developping ui, since actual backend abby service
 * is not ready yet.
 */

@Slf4j
@Service
public class Step3TempService {


  private static final Integer DOC_CONTENT_COUNT_PER_DOC = 1;
  private static final Integer DOC_FIELD_COUNT_PER_DOC_CONTENT = 5;

  private List<DocFieldModel> docFields = new ArrayList<>();

  private List<IpRelationsModel> ipRelations = new ArrayList<>();
  private List<Map<Integer, String>> validIpRelationsSeletcs = new ArrayList<>();

  private static final Integer[] FIELD_TYPES_ARRAY = {
      FieldTypes.NAME.getFieldTypeId(), FieldTypes.DOCUMENT_RELATION.getFieldTypeId(), FieldTypes.DOMAIN.getFieldTypeId(),
      FieldTypes.PROSECUTORS_OFFICE.getFieldTypeId(), FieldTypes.DOCUMENT_SUBJECT.getFieldTypeId(),
      FieldTypes.DOCUMENT_DEFINER.getFieldTypeId(), FieldTypes.DOCUMENT_RELATION.getFieldTypeId(),
      FieldTypes.REQUESTED_OFFICE.getFieldTypeId()
  };

  public synchronized List<DocFieldModel> getDocFieldsForJob() {

    if (CollectionUtils.isNotEmpty(docFields)) {
      return docFields;
    }

    for (int index = 0; index < 4; index++) {
      for (int i = 0; i < DOC_CONTENT_COUNT_PER_DOC; i++) {
        for (int j = 0; j < DOC_FIELD_COUNT_PER_DOC_CONTENT; j++) {

          Integer fieldTypeIdRnd = FIELD_TYPES_ARRAY[randomBetween(0, FIELD_TYPES_ARRAY.length - 1)];
          String label = FieldTypes.valueOfId(fieldTypeIdRnd).getLabel();

          switch (j) {
            case 0:
              fieldTypeIdRnd = FieldTypes.IP.getFieldTypeId();
              label = FieldTypes.IP.getLabel();
              break;
            case 1:
              fieldTypeIdRnd = FieldTypes.PORT.getFieldTypeId();
              label = FieldTypes.PORT.getLabel();
              break;
            case 2:
              fieldTypeIdRnd = FieldTypes.DATE.getFieldTypeId();
              label = FieldTypes.DATE.getLabel();
              break;
            case 3:
              fieldTypeIdRnd = FieldTypes.TIME.getFieldTypeId();
              label = FieldTypes.TIME.getLabel();
              break;
            default:
              break;
          }

          docFields.add(DocFieldModel.builder()
              .id(randomBetween(0, Integer.MAX_VALUE))
              .docId(i)
              .docContentId(i)
              .fieldTypeId(fieldTypeIdRnd)
              .fieldTypeText(label)
              .value(RandomStringUtils.randomAlphabetic(randomBetween(5, 15)))
              .addTime(getNow())
              .build());
        }
      }
    }

    return docFields;
  }

  // todo update doc_field record via DiyasorDAO
  public void updateDocField(Integer docFieldId, String value, Integer fieldTypeId) {
    int itemIdx = findDocFieldIndex(docFieldId);

    if (itemIdx >= 0) {
      DocFieldModel dfm = docFields.get(itemIdx);
      dfm.setValue(value);
      dfm.setFieldTypeId(fieldTypeId);
      dfm.setFieldTypeText(FieldTypes.valueOfId(fieldTypeId).getLabel());

      docFields.set(itemIdx, dfm);

      // update ipRelations
      updateIpRelationsForDocFieldUpdate(docFieldId, fieldTypeId, value);
      validIpRelationsSeletcs.clear();
    }
  }

  private int findDocFieldIndex(Integer docFieldId) {
    int itemIdx = -1;
    for (int i = 0; i < docFields.size(); i++) {
      if (docFields.get(i).getId().equals(docFieldId)) {
        itemIdx = i;
        break;
      }
    }
    return itemIdx;
  }

  public void deleteDocField(Integer docFieldId) {
    if (docFieldId == null) {
      log.warn("No docField by docFİeldId: {}", docFieldId);
    }
    int itemIdx = findDocFieldIndex(docFieldId);
    if (itemIdx >= 0) {
      docFields.remove(itemIdx);

      // update ipRelations
      updateIpRelationsForDocFieldDelete(docFieldId);
      validIpRelationsSeletcs.clear();
    } else {
      log.warn("No docField by docFİeldId: {}", docFieldId);
    }
  }

  // todo insert into doc_field record via DiyasorDAO
  public void saveDocField(String value, Integer fieldTypeId) {
    DocFieldModel df = docFields.get(0);

    docFields.add(DocFieldModel.builder()
        .id(randomBetween(0, Integer.MAX_VALUE))
        .docId(df.getDocId())
        .docContentId(df.getDocContentId())
        .fieldTypeId(fieldTypeId)
        .fieldTypeText(FieldTypes.valueOfId(fieldTypeId).getLabel())
        .value(value)
        .addTime(getNow())
        .build());

    validIpRelationsSeletcs.clear();
  }

  public DataResponse getIpRelations() {

    if (CollectionUtils.isNotEmpty(ipRelations) && CollectionUtils.isNotEmpty(validIpRelationsSeletcs)) {
      return DataResponse.builder()
          .ipRelations(ipRelations)
          .validIpRelationsSeletcs(validIpRelationsSeletcs)
          .build();
    }

    getDocFieldsForJob();

    List<DocFieldModel> ipFields = docFields.stream().filter(df -> df.getFieldTypeId().equals(FieldTypes.IP.getFieldTypeId()))
        .collect(Collectors.toList());

    List<DocFieldModel> portFields = docFields.stream().filter(df -> df.getFieldTypeId().equals(FieldTypes.PORT.getFieldTypeId()))
        .collect(Collectors.toList());

    List<DocFieldModel> dateFields = docFields.stream().filter(df -> df.getFieldTypeId().equals(FieldTypes.DATE.getFieldTypeId()))
        .collect(Collectors.toList());

    List<DocFieldModel> timeFields = docFields.stream().filter(df -> df.getFieldTypeId().equals(FieldTypes.TIME.getFieldTypeId()))
        .collect(Collectors.toList());

    if (CollectionUtils.isEmpty(ipRelations)) {
      if (CollectionUtils.isNotEmpty(ipFields) && CollectionUtils.isNotEmpty(portFields)
          && CollectionUtils.isNotEmpty(dateFields) && CollectionUtils.isNotEmpty(timeFields)) {

        for (int ipRel = 0; ipRel < randomBetween(12, 15); ipRel++) {
          IpRelationsModel ipRelationsModel = IpRelationsModel.builder()
              .id(randomBetween(0, Integer.MAX_VALUE))
              .ipField(ipFields.get(randomBetween(0, ipFields.size())))
              .portField(portFields.get(randomBetween(0, portFields.size())))
              .startDateField(dateFields.get(randomBetween(0, dateFields.size())))
              .startTimeField(timeFields.get(randomBetween(0, timeFields.size())))
              .build();

          ipRelations.add(ipRelationsModel);
        }
      }
    }

    // ip/port/date/time select values
    if (CollectionUtils.isEmpty(validIpRelationsSeletcs)) {
      final Map<Integer, String> ipSelectValuesMap = new HashMap<>();
      final Map<Integer, String> portSelectValuesMap = new HashMap<>();
      final Map<Integer, String> startDateSelectValuesMap = new HashMap<>();
      final Map<Integer, String> startTimeSelectValuesMap = new HashMap<>();

      ipFields.forEach(field -> ipSelectValuesMap.put(field.getId(), field.getValue()));

      portFields.forEach(field -> portSelectValuesMap.put(field.getId(), field.getValue()));

      dateFields.forEach(field -> startDateSelectValuesMap.put(field.getId(), field.getValue()));

      timeFields.forEach(field -> startTimeSelectValuesMap.put(field.getId(), field.getValue()));

      validIpRelationsSeletcs = Stream.of(ipSelectValuesMap, portSelectValuesMap, startDateSelectValuesMap, startTimeSelectValuesMap)
          .collect(Collectors.toList());
    }

    return DataResponse.builder()
        .ipRelations(ipRelations)
        .validIpRelationsSeletcs(validIpRelationsSeletcs)
        .build();

  }

  private void updateIpRelationsForDocFieldUpdate(Integer docFieldId, Integer newDocFieldTypeId, String newValue) {
    List<Integer> ipRelsToRemove = new ArrayList<>();

    for (int i = 0; i < ipRelations.size(); i++) {
      IpRelationsModel ipRel = ipRelations.get(i);

      if (ipRel.getIpField().getId().equals(docFieldId)) {
        if (!ipRel.getIpField().getFieldTypeId().equals(newDocFieldTypeId)) {
          ipRelsToRemove.add(ipRel.getId());
        } else {
          ipRel.getIpField().setValue(newValue);
        }
      } else if (ipRel.getPortField().getId().equals(docFieldId)) {
        if (!ipRel.getPortField().getFieldTypeId().equals(newDocFieldTypeId)) {
          ipRelsToRemove.add(ipRel.getId());
        } else {
          ipRel.getPortField().setValue(newValue);
        }
      } else if (ipRel.getStartDateField().getId().equals(docFieldId)) {
        if (!ipRel.getStartDateField().getFieldTypeId().equals(newDocFieldTypeId)) {
          ipRelsToRemove.add(ipRel.getId());
        } else {
          ipRel.getStartDateField().setValue(newValue);
        }
      } else if (ipRel.getStartTimeField().getId().equals(docFieldId)) {
        if (!ipRel.getStartTimeField().getFieldTypeId().equals(newDocFieldTypeId)) {
          ipRelsToRemove.add(ipRel.getId());
        } else {
          ipRel.getStartTimeField().setValue(newValue);
        }
      }
    }

    ipRelations = ipRelations.stream().filter(ipRel -> !ipRelsToRemove.contains(ipRel.getId())).collect(Collectors.toList());
  }

  private void updateIpRelationsForDocFieldDelete(Integer docFieldId) {
    List<Integer> ipRelsToRemove = new ArrayList<>();

    for (int i = 0; i < ipRelations.size(); i++) {
      IpRelationsModel ipRel = ipRelations.get(i);

      if (ipRel.getIpField().getId().equals(docFieldId)) {
        ipRelsToRemove.add(ipRel.getId());
      } else if (ipRel.getPortField().getId().equals(docFieldId)) {
        ipRelsToRemove.add(ipRel.getId());
      } else if (ipRel.getStartDateField().getId().equals(docFieldId)) {
        ipRelsToRemove.add(ipRel.getId());
      } else if (ipRel.getStartTimeField().getId().equals(docFieldId)) {
        ipRelsToRemove.add(ipRel.getId());
      }
    }

    ipRelations = ipRelations.stream().filter(ipRel -> !ipRelsToRemove.contains(ipRel.getId())).collect(Collectors.toList());
  }

  public void updateIpRelation(Integer ipRelId, Integer ipFieldId, Integer portFieldId, Integer startDateFieldId,
      Integer startTimeFieldId) {
    List<IpRelationsModel> ipRelFound = ipRelations.stream().filter(ipRel -> ipRel.getId().equals(ipRelId)).collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(ipRelFound)) {
      IpRelationsModel ipRel = ipRelFound.get(0);

      ipRel.setIpField(docFields.stream().filter(df -> df.getId().equals(ipFieldId)).collect(Collectors.toList()).get(0));
      ipRel.setPortField(docFields.stream().filter(df -> df.getId().equals(portFieldId)).collect(Collectors.toList()).get(0));
      ipRel.setStartDateField(docFields.stream().filter(df -> df.getId().equals(startDateFieldId)).collect(Collectors.toList()).get(0));
      ipRel.setStartTimeField(docFields.stream().filter(df -> df.getId().equals(startTimeFieldId)).collect(Collectors.toList()).get(0));

      log.info("Updated ipRelation with Id: {}", ipRelId);
    }

  }

  public void saveIpRelation(Integer ipFieldId, Integer portFieldId, Integer startDateFieldId,
      Integer startTimeFieldId) {

    IpRelationsModel ipRel = IpRelationsModel.builder()
        .id(randomBetween(0, Integer.MAX_VALUE))
        .build();

    ipRel.setIpField(docFields.stream().filter(df -> df.getId().equals(ipFieldId)).collect(Collectors.toList()).get(0));
    ipRel.setPortField(docFields.stream().filter(df -> df.getId().equals(portFieldId)).collect(Collectors.toList()).get(0));
    ipRel.setStartDateField(docFields.stream().filter(df -> df.getId().equals(startDateFieldId)).collect(Collectors.toList()).get(0));
    ipRel.setStartTimeField(docFields.stream().filter(df -> df.getId().equals(startTimeFieldId)).collect(Collectors.toList()).get(0));

    ipRelations.add(ipRel);
  }

  public void deleteIpRelation(Integer ipRelId) {
    ipRelations.removeIf(ipRel -> ipRel.getId().equals(ipRelId));
  }

  public static int randomBetween(int min, int max) {
    Random random = new Random();
    return random.ints(min, max)
        .findFirst()
        .getAsInt();
  }

  public static Timestamp getNow() {
    return new Timestamp(new Date().getTime());
  }

}
