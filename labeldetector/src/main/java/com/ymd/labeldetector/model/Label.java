package com.ymd.labeldetector.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Label {

    protected String name;
    protected float score;
}
