package com.dt2te.poc.model;

import java.util.HashMap;
import java.util.Map;

public enum FieldTypes {

  DATE(1, null, false, "Tarih", true, true, "tarih"),
  DOMAIN(2, null, false, "Domain", true, true, "alan adı"),
  IP(3, null, false, "Ip Adresi", true, true, "ip adresi"),

  NAME(4, null, false, "İsim", true, true, "isim"),
  TIME(5, null, false, "Zaman", true, true, "zaman"),
  DATE_TIME(7, null, false, "Tarih ve Zaman", true, true, "tarih ve zaman"),
  IP_RANGE(9, null, false, "Ip Adresi Aralığı", true, true, "ip adresi aralığı"),
  PROSECUTORS_OFFICE(10, null, false, "Savcılık", true, true, "savcılık"),

  SOURCE_IP(17, 3, false, "Kaynak Ip Bilgisi", true, true, "kaynak ip"),
  TARGET_IP(18, 3, false, "Hedef Ip Bilgisi", true, true, "hedef ip"),

  PORT(19, null, false, "Port", true, true, "iletişim portu"),

  DOCUMENT_DATE(21, 7, false, "Evrak Tarihi", true, false, "evrak tarihi"),

  DOCUMENT_SUBJECT(22, null, false, "Konu", true, false, "konu"),
  DOCUMENT_DEFINER(23, null, false, "Sayı", true, false, "sayı"),
  DOCUMENT_RELATION(24, null, false, "İlgi", true, false, "ilgi"),

  REQUESTED_OFFICE(29, null, false, "Talep sahibi kurum", true, false, "talep sahibi"),

  IP_PORT(30, null, false, "Ip Port Bilgisi", true, true, "ip port bilgisi"),
  SOURCE_IP_PORT(31, 30, false, "Kaynak Ip Port Bilgisi", true, true, "kaynak ip port bilgisi"),
  TARGET_IP_PORT(32, 30, false, "Hedef Ip Port Bilgisi", true, true, "hedef ip port bilgisi"),

  REQUEST_DETECT_FIELDS(10001, null, false, "Talep Edilen Alan Tespiti", false, false, "null"),
  REQUEST_DETECT_ID(10002, 10001, false, "Talep Edilen ID Tespiti", false, false, "null"),
  REQUEST_DETECT_ADDRESS(10003, 10001, false, "Talep Edilen Adres Tespiti", false, false, "null"),
  REQUEST_DETECT_DATE_TIME(10004, 10001, false, "Talep Edilen Tarih Tespiti", false, false, "null"),
  REQUEST_DETECT_PHONE_NUMBER(10005, 10001, false, "Talep Edilen Telefon Tespiti", false, false, "null"),
  REQUEST_CONCAT_INFO_NUMBER(10006, 10001, false, "Talep Edilen Bilgi Numarası", false, false, "null"),
  REQUEST_GIVEN_DATE_TIME(10007, 10001, false, "Talep Edilen Tarih", false, false, "null"),
  REQUEST_GIVEN_IP(10008, 10001, false, "Talep Edilen Ip", false, false, "null"),
  REQUEST_GIVEN_IP_KULLANAN(10009, 10001, false, "Talep Edilen KUllanılan Ip", false, false, "null");

  private static final Map<Integer, FieldTypes> TYPE_BY_ID = new HashMap<>();
  private static final Map<String, FieldTypes> TYPE_BY_HEADER = new HashMap<>();

  static {
    for (FieldTypes e : values()) {
      TYPE_BY_ID.put(e.fieldTypeId, e);
    }
  }

  static {
    for (FieldTypes e : values()) {
      TYPE_BY_HEADER.put(e.header, e);
    }
  }

  private final String label;
  private final Integer fieldTypeId;
  private final Integer parentFieldId;
  private final boolean isUsedInElastic;
  private final boolean isElasticFieldArray;
  private final boolean hidden;
  private final String ELASTIC_FIELDS_PREFIX = "dys_doc_fields";
  public String header;

  FieldTypes(Integer fieldTypeId, Integer parentFieldId, boolean hidden, String label, boolean isUsedInElastic, boolean isElasticFieldArray,
      String header) {
    this.fieldTypeId = fieldTypeId;
    this.parentFieldId = parentFieldId;
    this.hidden = hidden;
    this.label = label;
    this.isUsedInElastic = isUsedInElastic;
    this.isElasticFieldArray = isElasticFieldArray;
    this.header = header;
  }

  public static FieldTypes valueOfId(Integer FieldTypes) {
    return TYPE_BY_ID.get(FieldTypes);
  }

  public static FieldTypes valueOfHeader(String header) {
    return TYPE_BY_HEADER.get(header);
  }

  public Integer getFieldTypeId() {
    return fieldTypeId;
  }

  public Integer getParentFieldId() {
    return parentFieldId;
  }

  public boolean isHidden() {
    return hidden;
  }

  public String getLabel() {
    return label;
  }

  public boolean isUsedInElastic() {
    return isUsedInElastic;
  }

  public boolean isElasticFieldArray() {
    return isElasticFieldArray;
  }

  public String getElasticFieldKey() {
    return ELASTIC_FIELDS_PREFIX + "." + this.name().toLowerCase();
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

}
