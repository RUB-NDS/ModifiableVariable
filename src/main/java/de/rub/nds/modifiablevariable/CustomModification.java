package de.rub.nds.modifiablevariable;

import de.rub.nds.modifiablevariable.util.ByteArrayAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.*;
import java.util.function.Function;

@XmlRootElement
@XmlType(propOrder = { "code", "modificationFilter", "postModification" })
public class CustomModification<T> extends VariableModification<T> {

    private Function<T, T> function = null;

    private byte[] code;

    @XmlAttribute
    private String description;

    public CustomModification() {
    }

    public CustomModification(String description, Function<T, T> function) {
        this.description = description;
        this.function = function;
        serializeFunction();
    }

    @Override
    protected T modifyImplementationHook(T input) {
        if (function == null)
            deserializeFunction();
        return function.apply(input);
    }

    private void serializeFunction() {
        Function<T, T> f = (Function<T, T> & Serializable) function;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(f);
            code = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            code = "failed".getBytes();
        }
    }

    private void deserializeFunction() {
        try {
            function = (Function<T, T>) new ObjectInputStream(new ByteArrayInputStream(code)).readObject();
        } catch (ClassNotFoundException | IOException e) {

        }
    }

    public void setCode(byte[] code) {
        this.code = code;
    }

    @XmlJavaTypeAdapter(ByteArrayAdapter.class)
    public byte[] getCode() {
        return code;
    }
}
