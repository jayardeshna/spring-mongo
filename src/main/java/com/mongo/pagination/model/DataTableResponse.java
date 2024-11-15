package com.mongo.pagination.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataTableResponse extends DataTableAttributes {

    private List<?> data;
    private String error;
}
