/**
 * ModifiableVariable - A Variable Concept for Runtime Modifications
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.modifiablevariable.biginteger;

import de.rub.nds.modifiablevariable.CustomModification;
import de.rub.nds.modifiablevariable.ModifiableVariable;
import de.rub.nds.modifiablevariable.VariableModification;
import de.rub.nds.modifiablevariable.util.ArrayConverter;
import java.io.Serializable;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Juraj Somorovsky - juraj.somorovsky@rub.de
 */
@XmlRootElement
@XmlSeeAlso({ BigIntegerAddModification.class, BigIntegerExplicitValueModification.class,
        BigIntegerSubtractModification.class, BigIntegerXorModification.class, VariableModification.class,
        CustomModification.class })
@XmlType(propOrder = { "originalValue", "modification", "assertEquals" })
public class ModifiableBigInteger extends ModifiableVariable<BigInteger> implements Serializable {

    private BigInteger originalValue;

    @Override
    protected void createRandomModification() {
        VariableModification<BigInteger> vm = BigIntegerModificationFactory.createRandomModification();
        setModification(vm);
    }

    public BigInteger getAssertEquals() {
        return assertEquals;
    }

    public void setAssertEquals(BigInteger assertEquals) {
        this.assertEquals = assertEquals;
    }

    @Override
    public boolean isOriginalValueModified() {
        return getOriginalValue() != null && (getOriginalValue().compareTo(getValue()) != 0);
    }

    public byte[] getByteArray() {
        return ArrayConverter.bigIntegerToByteArray(getValue());
    }

    public byte[] getByteArray(int size) {
        return ArrayConverter.bigIntegerToByteArray(getValue(), size, true);
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

    @Override
    public BigInteger getOriginalValue() {
        return originalValue;
    }

    @Override
    public void setOriginalValue(BigInteger originalValue) {
        this.originalValue = originalValue;
    }
}
