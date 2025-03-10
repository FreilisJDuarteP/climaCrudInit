package com.tiempo.clima.dto;

import java.util.List;

public class PronosticoResponse {
    private List<PronosticoDia> list;

    public List<PronosticoDia> getList() {
        return list;
    }

    public void setList(List<PronosticoDia> list) {
        this.list = list;
    }
}
