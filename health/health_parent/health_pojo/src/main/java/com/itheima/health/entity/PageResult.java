package com.itheima.health.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 分页结果封装
 * User: Eric
 */
public class PageResult<T> implements Serializable {
    private Long total;
    private List<T> rows;

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
