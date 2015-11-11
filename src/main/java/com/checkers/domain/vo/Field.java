package com.checkers.domain.vo;

import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Isaiev on 24.09.2015.
 */
public class Field implements Serializable {
    
    public static final long serialVersionUID = 42L;

    private Set<Check> allChecks;

    public Field() {
        allChecks= Sets.newHashSet();
    }

    public Field(Field field) {
        allChecks= Sets.newHashSet();
        for(Check check: field.allChecks){
            this.allChecks.add(new Check(check));
        }
    }

    public Set<Check> getAllChecks() {
        return allChecks;
    }

    public void setAllChecks(Set<Check> allChecks) {
        this.allChecks = allChecks;
    }
}
