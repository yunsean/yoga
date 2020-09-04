/*
 * Copyright (c) 2018, 吴汶泽 (wuwz@live.com).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yoga.excelkit.pojo;

import lombok.ToString;

@ToString
public class ExcelErrorField {
    private Integer cellIndex;
    private String name;
    private String column;
    private String errorMessage;

    @java.beans.ConstructorProperties({"cellIndex", "name", "column", "errorMessage"})
    public ExcelErrorField(Integer cellIndex, String name, String column, String errorMessage) {
        this.cellIndex = cellIndex;
        this.name = name;
        this.column = column;
        this.errorMessage = errorMessage;
    }

    public ExcelErrorField() {
    }

    public static ExcelErrorFieldBuilder builder() {
        return new ExcelErrorFieldBuilder();
    }

    public Integer getCellIndex() {
        return this.cellIndex;
    }

    public String getName() {
        return this.name;
    }

    public String getColumn() {
        return this.column;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setCellIndex(Integer cellIndex) {
        this.cellIndex = cellIndex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ExcelErrorField)) return false;
        final ExcelErrorField other = (ExcelErrorField) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$cellIndex = this.getCellIndex();
        final Object other$cellIndex = other.getCellIndex();
        if (this$cellIndex == null ? other$cellIndex != null : !this$cellIndex.equals(other$cellIndex)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$column = this.getColumn();
        final Object other$column = other.getColumn();
        if (this$column == null ? other$column != null : !this$column.equals(other$column)) return false;
        final Object this$errorMessage = this.getErrorMessage();
        final Object other$errorMessage = other.getErrorMessage();
        if (this$errorMessage == null ? other$errorMessage != null : !this$errorMessage.equals(other$errorMessage))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $cellIndex = this.getCellIndex();
        result = result * PRIME + ($cellIndex == null ? 43 : $cellIndex.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $column = this.getColumn();
        result = result * PRIME + ($column == null ? 43 : $column.hashCode());
        final Object $errorMessage = this.getErrorMessage();
        result = result * PRIME + ($errorMessage == null ? 43 : $errorMessage.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ExcelErrorField;
    }

    public static class ExcelErrorFieldBuilder {
        private Integer cellIndex;
        private String name;
        private String column;
        private String errorMessage;

        ExcelErrorFieldBuilder() {
        }

        public ExcelErrorFieldBuilder cellIndex(Integer cellIndex) {
            this.cellIndex = cellIndex;
            return this;
        }

        public ExcelErrorFieldBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ExcelErrorFieldBuilder column(String column) {
            this.column = column;
            return this;
        }

        public ExcelErrorFieldBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public ExcelErrorField build() {
            return new ExcelErrorField(cellIndex, name, column, errorMessage);
        }

        public String toString() {
            return "ExcelErrorField.ExcelErrorFieldBuilder(cellIndex=" + this.cellIndex + ", name=" + this.name + ", column=" + this.column + ", errorMessage=" + this.errorMessage + ")";
        }
    }
}
