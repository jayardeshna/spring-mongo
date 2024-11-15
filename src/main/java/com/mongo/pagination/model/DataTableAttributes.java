package com.mongo.pagination.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataTableAttributes {

    private long recordsTotal;

    private long recordsFiltered;
}
