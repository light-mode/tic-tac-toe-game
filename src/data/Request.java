/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;

/**
 *
 * @author ADMIN
 */
public class Request implements Serializable {

    private final RequestKey key;
    private final Object value;

    public Request(RequestKey key, Object value) {
        this.key = key;
        this.value = value;
    }

    public RequestKey getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
