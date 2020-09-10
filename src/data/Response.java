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
public class Response implements Serializable {

    private final ResponseKey key;
    private final Object value;

    public Response(ResponseKey key, Object value) {
        this.key = key;
        this.value = value;
    }

    public ResponseKey getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
