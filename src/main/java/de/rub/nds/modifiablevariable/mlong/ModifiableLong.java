/**
 * ModifiableVariable - A Variable Concept for Runtime Modifications
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.modifiablevariable.mlong;

import de.rub.nds.modifiablevariable.CustomModification;
import de.rub.nds.modifiablevariable.ModifiableVariable;
import de.rub.nds.modifiablevariable.VariableModification;
import de.rub.nds.modifiablevariable.util.ArrayConverter;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author Juraj Somorovsky - juraj.somorovsky@rub.de
 */
@XmlRootElement
@XmlSeeAlso({ LongAddModification.class, LongExplicitValueModification.class, LongSubtractModification.class,
        LongXorModification.class, CustomModification.class })
@XmlType(propOrder = { "originalValue", "modification", "assertEquals" })
public class ModifiableLong extends ModifiableVariable<Long> implements Serializable {

    private Long originalValue;

    @Override
    protected void createRandomModification() {
        VariableModification<Long> vm = LongModificationFactory.createRandomModification();
        setModification(vm);
    }

    public Long getAssertEquals() {
        return assertEquals;
    }

    public void setAssertEquals(Long assertEquals) {
        this.assertEquals = assertEquals;
    }

    @Override
    public boolean isOriginalValueModified() {
        return originalValue != null && originalValue.compareTo(getValue()) != 0;
    }

    public byte[] getByteArray(int size) {
        return ArrayConverter.longToBytes(getValue(), size);
    }

    @Override
    public boolean validateAssertions() {
        boolean valid = true;
        if (assertEquals != null) {
            if (assertEquals.compareTo(getValue()) != 0) {
                valid = false;
            }
        }
        return valid;
    }

    @Override
    public Long getOriginalValue() {
        return originalValue;
    }

    @Override
    public void setOriginalValue(Long originalValue) {
        this.originalValue = originalValue;
    }
}
