package com.yoga.utility.qr.dto;

import com.yoga.setting.annotation.Settable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UploadedFileBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private long size;
    private long id;
    private String url;
    private String name;
}
