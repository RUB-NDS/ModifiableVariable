/**
 * ModifiableVariable - A Variable Concept for Runtime Modifications
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.modifiablevariable.singlebyte;

import de.rub.nds.modifiablevariable.CustomModification;
import de.rub.nds.modifiablevariable.ModifiableVariable;
import de.rub.nds.modifiablevariable.VariableModification;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author Juraj Somorovsky - juraj.somorovsky@rub.de
 */
@XmlRootElement
@XmlSeeAlso({ ByteAddModification.class, ByteExplicitValueModification.class, ByteSubtractModification.class,
        ByteXorModification.class, CustomModification.class })
@XmlType(propOrder = { "originalValue", "modification", "assertEquals" })
public class ModifiableByte extends ModifiableVariable<Byte> implements Serializable {

    private Byte originalValue;

    @Override
    protected void createRandomModification() {
        VariableModification<Byte> vm = ByteModificationFactory.createRandomModification();
        setModification(vm);
    }

    public Byte getAssertEquals() {
        return assertEquals;
    }

    public void setAssertEquals(Byte assertEquals) {
        this.assertEquals = assertEquals;
    }

    @Override
    public boolean isOriginalValueModified() {
        return originalValue != null && originalValue.compareTo(getValue()) != 0;
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
    public Byte getOriginalValue() {
        return originalValue;
    }

    @Override
    public void setOriginalValue(Byte originalValue) {
        this.originalValue = originalValue;
    }
}
