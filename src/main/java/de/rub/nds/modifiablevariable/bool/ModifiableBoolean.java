/**
 * ModifiableVariable - A Variable Concept for Runtime Modifications
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.modifiablevariable.bool;

import de.rub.nds.modifiablevariable.CustomModification;
import de.rub.nds.modifiablevariable.ModifiableVariable;
import de.rub.nds.modifiablevariable.VariableModification;
import de.rub.nds.modifiablevariable.biginteger.BigIntegerModificationFactory;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Robert Merget <robert.merget@rub.de>
 */
@XmlRootElement
@XmlSeeAlso({ BooleanExplicitValueModification.class, BooleanExplicitValueModification.class, CustomModification.class })
@XmlType(propOrder = { "originalValue", "modification", })
public class ModifiableBoolean extends ModifiableVariable<Boolean> {

    private Boolean originalValue;

    public ModifiableBoolean() {
    }

    public ModifiableBoolean(Boolean originalValue) {
        this.originalValue = originalValue;
    }

    @Override
    public Boolean getOriginalValue() {
        return originalValue;
    }

    @Override
    public void setOriginalValue(Boolean originalValue) {
        this.originalValue = originalValue;
    }

    @Override
    protected void createRandomModification() {
        VariableModification<Boolean> vm = BooleanModificationFactory.createRandomModification();
        setModification(vm);
    }

    @Override
    public boolean isOriginalValueModified() {
        return getOriginalValue() != null && (getOriginalValue().compareTo(getValue()) != 0);
    }

    @Override
    public boolean validateAssertions() {
        if (assertEquals != null) {
            if (assertEquals.compareTo(getValue()) != 0) {
                return false;
            }
        }
        return true;
    }
}
