/*
 * Copyright (c) 2018, 吴汶泽 (wuwz@live.com).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yoga.excelkit.pojo;

import com.yoga.excelkit.config.Options;
import com.yoga.excelkit.convert.ReadConverter;
import com.yoga.excelkit.convert.WriteConverter;
import com.yoga.excelkit.validator.Validator;
import lombok.ToString;

/**
 * @author wuwenze
 * @date 2018/5/1
 */
@ToString
public class ExcelProperty {
  private String name;
  private String column;
  private Boolean required;
  private Short width;
  private String comment;
  private Integer maxLength;
  private String dateFormat;
  private Options options;
  private String writeConverterExp;
  private WriteConverter writeConverter;
  private String readConverterExp;
  private ReadConverter readConverter;
  private String regularExp;
  private String regularExpMessage;
  private Validator validator;

  @java.beans.ConstructorProperties({"name", "column", "required", "width", "comment", "maxLength", "dateFormat", "options", "writeConverterExp", "writeConverter", "readConverterExp", "readConverter", "regularExp", "regularExpMessage", "validator"})
  public ExcelProperty(String name, String column, Boolean required, Short width, String comment, Integer maxLength, String dateFormat, Options options, String writeConverterExp, WriteConverter writeConverter, String readConverterExp, ReadConverter readConverter, String regularExp, String regularExpMessage, Validator validator) {
    this.name = name;
    this.column = column;
    this.required = required;
    this.width = width;
    this.comment = comment;
    this.maxLength = maxLength;
    this.dateFormat = dateFormat;
    this.options = options;
    this.writeConverterExp = writeConverterExp;
    this.writeConverter = writeConverter;
    this.readConverterExp = readConverterExp;
    this.readConverter = readConverter;
    this.regularExp = regularExp;
    this.regularExpMessage = regularExpMessage;
    this.validator = validator;
  }

  public ExcelProperty() {
  }

  public static ExcelPropertyBuilder builder() {
    return new ExcelPropertyBuilder();
  }

  public String getName() {
    return this.name;
  }

  public String getColumn() {
    return this.column;
  }

  public Boolean getRequired() {
    return this.required;
  }

  public Short getWidth() {
    return this.width;
  }

  public String getComment() {
    return this.comment;
  }

  public Integer getMaxLength() {
    return this.maxLength;
  }

  public String getDateFormat() {
    return this.dateFormat;
  }

  public Options getOptions() {
    return this.options;
  }

  public String getWriteConverterExp() {
    return this.writeConverterExp;
  }

  public WriteConverter getWriteConverter() {
    return this.writeConverter;
  }

  public String getReadConverterExp() {
    return this.readConverterExp;
  }

  public ReadConverter getReadConverter() {
    return this.readConverter;
  }

  public String getRegularExp() {
    return this.regularExp;
  }

  public String getRegularExpMessage() {
    return this.regularExpMessage;
  }

  public Validator getValidator() {
    return this.validator;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  public void setWidth(Short width) {
    this.width = width;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  public void setOptions(Options options) {
    this.options = options;
  }

  public void setWriteConverterExp(String writeConverterExp) {
    this.writeConverterExp = writeConverterExp;
  }

  public void setWriteConverter(WriteConverter writeConverter) {
    this.writeConverter = writeConverter;
  }

  public void setReadConverterExp(String readConverterExp) {
    this.readConverterExp = readConverterExp;
  }

  public void setReadConverter(ReadConverter readConverter) {
    this.readConverter = readConverter;
  }

  public void setRegularExp(String regularExp) {
    this.regularExp = regularExp;
  }

  public void setRegularExpMessage(String regularExpMessage) {
    this.regularExpMessage = regularExpMessage;
  }

  public void setValidator(Validator validator) {
    this.validator = validator;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ExcelProperty)) return false;
    final ExcelProperty other = (ExcelProperty) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$name = this.getName();
    final Object other$name = other.getName();
    if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
    final Object this$column = this.getColumn();
    final Object other$column = other.getColumn();
    if (this$column == null ? other$column != null : !this$column.equals(other$column)) return false;
    final Object this$required = this.getRequired();
    final Object other$required = other.getRequired();
    if (this$required == null ? other$required != null : !this$required.equals(other$required)) return false;
    final Object this$width = this.getWidth();
    final Object other$width = other.getWidth();
    if (this$width == null ? other$width != null : !this$width.equals(other$width)) return false;
    final Object this$comment = this.getComment();
    final Object other$comment = other.getComment();
    if (this$comment == null ? other$comment != null : !this$comment.equals(other$comment)) return false;
    final Object this$maxLength = this.getMaxLength();
    final Object other$maxLength = other.getMaxLength();
    if (this$maxLength == null ? other$maxLength != null : !this$maxLength.equals(other$maxLength)) return false;
    final Object this$dateFormat = this.getDateFormat();
    final Object other$dateFormat = other.getDateFormat();
    if (this$dateFormat == null ? other$dateFormat != null : !this$dateFormat.equals(other$dateFormat))
      return false;
    final Object this$options = this.getOptions();
    final Object other$options = other.getOptions();
    if (this$options == null ? other$options != null : !this$options.equals(other$options)) return false;
    final Object this$writeConverterExp = this.getWriteConverterExp();
    final Object other$writeConverterExp = other.getWriteConverterExp();
    if (this$writeConverterExp == null ? other$writeConverterExp != null : !this$writeConverterExp.equals(other$writeConverterExp))
      return false;
    final Object this$writeConverter = this.getWriteConverter();
    final Object other$writeConverter = other.getWriteConverter();
    if (this$writeConverter == null ? other$writeConverter != null : !this$writeConverter.equals(other$writeConverter))
      return false;
    final Object this$readConverterExp = this.getReadConverterExp();
    final Object other$readConverterExp = other.getReadConverterExp();
    if (this$readConverterExp == null ? other$readConverterExp != null : !this$readConverterExp.equals(other$readConverterExp))
      return false;
    final Object this$readConverter = this.getReadConverter();
    final Object other$readConverter = other.getReadConverter();
    if (this$readConverter == null ? other$readConverter != null : !this$readConverter.equals(other$readConverter))
      return false;
    final Object this$regularExp = this.getRegularExp();
    final Object other$regularExp = other.getRegularExp();
    if (this$regularExp == null ? other$regularExp != null : !this$regularExp.equals(other$regularExp))
      return false;
    final Object this$regularExpMessage = this.getRegularExpMessage();
    final Object other$regularExpMessage = other.getRegularExpMessage();
    if (this$regularExpMessage == null ? other$regularExpMessage != null : !this$regularExpMessage.equals(other$regularExpMessage))
      return false;
    final Object this$validator = this.getValidator();
    final Object other$validator = other.getValidator();
    if (this$validator == null ? other$validator != null : !this$validator.equals(other$validator)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $name = this.getName();
    result = result * PRIME + ($name == null ? 43 : $name.hashCode());
    final Object $column = this.getColumn();
    result = result * PRIME + ($column == null ? 43 : $column.hashCode());
    final Object $required = this.getRequired();
    result = result * PRIME + ($required == null ? 43 : $required.hashCode());
    final Object $width = this.getWidth();
    result = result * PRIME + ($width == null ? 43 : $width.hashCode());
    final Object $comment = this.getComment();
    result = result * PRIME + ($comment == null ? 43 : $comment.hashCode());
    final Object $maxLength = this.getMaxLength();
    result = result * PRIME + ($maxLength == null ? 43 : $maxLength.hashCode());
    final Object $dateFormat = this.getDateFormat();
    result = result * PRIME + ($dateFormat == null ? 43 : $dateFormat.hashCode());
    final Object $options = this.getOptions();
    result = result * PRIME + ($options == null ? 43 : $options.hashCode());
    final Object $writeConverterExp = this.getWriteConverterExp();
    result = result * PRIME + ($writeConverterExp == null ? 43 : $writeConverterExp.hashCode());
    final Object $writeConverter = this.getWriteConverter();
    result = result * PRIME + ($writeConverter == null ? 43 : $writeConverter.hashCode());
    final Object $readConverterExp = this.getReadConverterExp();
    result = result * PRIME + ($readConverterExp == null ? 43 : $readConverterExp.hashCode());
    final Object $readConverter = this.getReadConverter();
    result = result * PRIME + ($readConverter == null ? 43 : $readConverter.hashCode());
    final Object $regularExp = this.getRegularExp();
    result = result * PRIME + ($regularExp == null ? 43 : $regularExp.hashCode());
    final Object $regularExpMessage = this.getRegularExpMessage();
    result = result * PRIME + ($regularExpMessage == null ? 43 : $regularExpMessage.hashCode());
    final Object $validator = this.getValidator();
    result = result * PRIME + ($validator == null ? 43 : $validator.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof ExcelProperty;
  }

  public static class ExcelPropertyBuilder {
    private String name;
    private String column;
    private Boolean required;
    private Short width;
    private String comment;
    private Integer maxLength;
    private String dateFormat;
    private Options options;
    private String writeConverterExp;
    private WriteConverter writeConverter;
    private String readConverterExp;
    private ReadConverter readConverter;
    private String regularExp;
    private String regularExpMessage;
    private Validator validator;

    ExcelPropertyBuilder() {
    }

    public ExcelPropertyBuilder name(String name) {
      this.name = name;
      return this;
    }

    public ExcelPropertyBuilder column(String column) {
      this.column = column;
      return this;
    }

    public ExcelPropertyBuilder required(Boolean required) {
      this.required = required;
      return this;
    }

    public ExcelPropertyBuilder width(Short width) {
      this.width = width;
      return this;
    }

    public ExcelPropertyBuilder comment(String comment) {
      this.comment = comment;
      return this;
    }

    public ExcelPropertyBuilder maxLength(Integer maxLength) {
      this.maxLength = maxLength;
      return this;
    }

    public ExcelPropertyBuilder dateFormat(String dateFormat) {
      this.dateFormat = dateFormat;
      return this;
    }

    public ExcelPropertyBuilder options(Options options) {
      this.options = options;
      return this;
    }

    public ExcelPropertyBuilder writeConverterExp(String writeConverterExp) {
      this.writeConverterExp = writeConverterExp;
      return this;
    }

    public ExcelPropertyBuilder writeConverter(WriteConverter writeConverter) {
      this.writeConverter = writeConverter;
      return this;
    }

    public ExcelPropertyBuilder readConverterExp(String readConverterExp) {
      this.readConverterExp = readConverterExp;
      return this;
    }

    public ExcelPropertyBuilder readConverter(ReadConverter readConverter) {
      this.readConverter = readConverter;
      return this;
    }

    public ExcelPropertyBuilder regularExp(String regularExp) {
      this.regularExp = regularExp;
      return this;
    }

    public ExcelPropertyBuilder regularExpMessage(String regularExpMessage) {
      this.regularExpMessage = regularExpMessage;
      return this;
    }

    public ExcelPropertyBuilder validator(Validator validator) {
      this.validator = validator;
      return this;
    }

    public ExcelProperty build() {
      return new ExcelProperty(name, column, required, width, comment, maxLength, dateFormat, options, writeConverterExp, writeConverter, readConverterExp, readConverter, regularExp, regularExpMessage, validator);
    }

    public String toString() {
      return "ExcelProperty.ExcelPropertyBuilder(name=" + this.name + ", column=" + this.column + ", required=" + this.required + ", width=" + this.width + ", comment=" + this.comment + ", maxLength=" + this.maxLength + ", dateFormat=" + this.dateFormat + ", options=" + this.options + ", writeConverterExp=" + this.writeConverterExp + ", writeConverter=" + this.writeConverter + ", readConverterExp=" + this.readConverterExp + ", readConverter=" + this.readConverter + ", regularExp=" + this.regularExp + ", regularExpMessage=" + this.regularExpMessage + ", validator=" + this.validator + ")";
    }
  }
}
