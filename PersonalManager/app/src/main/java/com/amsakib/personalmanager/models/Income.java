package com.amsakib.personalmanager.models;

import java.util.Date;

public class Income {
    private int _id;
    private double _amount;
    private Date _date;
    private String _source;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public double get_amount() {
        return _amount;
    }

    public void set_amount(double _amount) {
        this._amount = _amount;
    }

    public Date get_date() {
        return _date;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public String get_source() {
        return _source;
    }

    public void set_source(String _source) {
        this._source = _source;
    }
}

