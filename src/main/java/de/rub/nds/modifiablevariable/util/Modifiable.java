package de.rub.nds.modifiablevariable.util;

import de.rub.nds.modifiablevariable.CustomModification;
import de.rub.nds.modifiablevariable.VariableModification;
import de.rub.nds.modifiablevariable.biginteger.BigIntegerModificationFactory;
import de.rub.nds.modifiablevariable.biginteger.ModifiableBigInteger;
import de.rub.nds.modifiablevariable.bool.BooleanExplicitValueModification;
import de.rub.nds.modifiablevariable.bool.BooleanToogleModification;
import de.rub.nds.modifiablevariable.bool.ModifiableBoolean;
import de.rub.nds.modifiablevariable.bytearray.ByteArrayModificationFactory;
import de.rub.nds.modifiablevariable.bytearray.ModifiableByteArray;
import de.rub.nds.modifiablevariable.integer.IntegerModificationFactory;
import de.rub.nds.modifiablevariable.integer.ModifiableInteger;
import de.rub.nds.modifiablevariable.mlong.LongModificationFactory;
import de.rub.nds.modifiablevariable.mlong.ModifiableLong;
import de.rub.nds.modifiablevariable.singlebyte.ByteModificationFactory;
import de.rub.nds.modifiablevariable.singlebyte.ModifiableByte;

import java.math.BigInteger;
import java.util.function.Function;

@SuppressWarnings("unused")
public class Modifiable {

    private static ModifiableByteArray getModifiableByteArrayWithModification(VariableModification<byte[]> modification) {
        ModifiableByteArray modifiableByteArray = new ModifiableByteArray();
        modifiableByteArray.setModification(modification);
        return modifiableByteArray;
    }

     private static ModifiableByte getModifiableByteWithModification(VariableModification<Byte> modification) {
        ModifiableByte modifiableByte = new ModifiableByte();
        modifiableByte.setModification(modification);
        return modifiableByte;
    }

    private static ModifiableInteger getModifiableIntegerWithModification(VariableModification<Integer> modification) {
        ModifiableInteger modifiableInteger = new ModifiableInteger();
        modifiableInteger.setModification(modification);
        return modifiableInteger;
    }

    private static ModifiableBigInteger getModifiableBigIntegerWithModification(VariableModification<BigInteger> modification) {
        ModifiableBigInteger modifiableBigInteger = new ModifiableBigInteger();
        modifiableBigInteger.setModification(modification);
        return modifiableBigInteger;
    }

    private static ModifiableLong getModifiableLongWithModification(VariableModification<Long> modification) {
        ModifiableLong modifiableLong = new ModifiableLong();
        modifiableLong.setModification(modification);
        return modifiableLong;
    }

    private static ModifiableBoolean getModifiableBooleanWithModification(VariableModification<Boolean> modification) {
        ModifiableBoolean modifiableBoolean = new ModifiableBoolean();
        modifiableBoolean.setModification(modification);
        return modifiableBoolean;
    }


    public static ModifiableByteArray explicit(byte[] b) {
        return getModifiableByteArrayWithModification(ByteArrayModificationFactory.explicitValue(b));
    }

    public static ModifiableByte explicit(Byte b) {
        return getModifiableByteWithModification(ByteModificationFactory.explicitValue(b));
    }

    public static ModifiableInteger explicit(Integer i) {
        return getModifiableIntegerWithModification(IntegerModificationFactory.explicitValue(i));
    }

    public static ModifiableBigInteger explicit(BigInteger i) {
        return getModifiableBigIntegerWithModification(BigIntegerModificationFactory.explicitValue(i));
    }

    public static ModifiableLong explicit(Long l) {
        return getModifiableLongWithModification(LongModificationFactory.explicitValue(l));
    }

    public static ModifiableBoolean explicit(Boolean b) {
        return getModifiableBooleanWithModification(new BooleanExplicitValueModification(b));
    }


    public static ModifiableByteArray xor(byte[] b, int position) {
        return getModifiableByteArrayWithModification(ByteArrayModificationFactory.xor(b, position));
    }

    public static ModifiableByte xor(Byte b) {
        return getModifiableByteWithModification(ByteModificationFactory.xor(b));
    }

    public static ModifiableInteger xor(Integer i) {
        return getModifiableIntegerWithModification(IntegerModificationFactory.xor(i));
    }

    public static ModifiableBigInteger xor(BigInteger i) {
        return getModifiableBigIntegerWithModification(BigIntegerModificationFactory.xor(i));
    }

    public static ModifiableLong xor(Long l) {
        return getModifiableLongWithModification(LongModificationFactory.xor(l));
    }



    public static ModifiableByte add(Byte b) {
        return getModifiableByteWithModification(ByteModificationFactory.add(b));
    }

    public static ModifiableInteger add(Integer i) {
        return getModifiableIntegerWithModification(IntegerModificationFactory.add(i));
    }

    public static ModifiableBigInteger add(BigInteger i) {
        return getModifiableBigIntegerWithModification(BigIntegerModificationFactory.add(i));
    }

    public static ModifiableLong add(Long l) {
        return getModifiableLongWithModification(LongModificationFactory.add(l));
    }


    public static ModifiableByte sub(Byte b) {
        return getModifiableByteWithModification(ByteModificationFactory.sub(b));
    }

    public static ModifiableInteger sub(Integer i) {
        return getModifiableIntegerWithModification(IntegerModificationFactory.sub(i));
    }

    public static ModifiableBigInteger sub(BigInteger i) {
        return getModifiableBigIntegerWithModification(BigIntegerModificationFactory.sub(i));
    }

    public static ModifiableLong sub(Long l) {
        return getModifiableLongWithModification(LongModificationFactory.sub(l));
    }


    public static ModifiableByteArray insert(byte[] b, int position) {
        return getModifiableByteArrayWithModification(ByteArrayModificationFactory.insert(b, position));
    }

    public static ModifiableByteArray delete(int startPosition, int count) {
        return getModifiableByteArrayWithModification(ByteArrayModificationFactory.delete(startPosition, count));
    }

    public static ModifiableByteArray shuffle(byte[] shuffle) {
        return getModifiableByteArrayWithModification(ByteArrayModificationFactory.shuffle(shuffle));
    }

    public static ModifiableByteArray duplicate() {
        return getModifiableByteArrayWithModification(ByteArrayModificationFactory.duplicate());
    }


    public static ModifiableBoolean toggle() {
        return getModifiableBooleanWithModification(new BooleanToogleModification());
    }


    public static ModifiableBigInteger shiftLeftBigInteger(Integer i) {
        return getModifiableBigIntegerWithModification(BigIntegerModificationFactory.shiftLeft(i));
    }

    public static ModifiableInteger shiftLeft(Integer i) {
        return getModifiableIntegerWithModification(IntegerModificationFactory.shiftLeft(i));
    }


    public static ModifiableBigInteger shiftRightBigInteger(Integer i) {
        return getModifiableBigIntegerWithModification(BigIntegerModificationFactory.shiftRight(i));
    }

    public static ModifiableInteger shiftRight(Integer i) {
        return getModifiableIntegerWithModification(IntegerModificationFactory.shiftRight(i));
    }

    public static ModifiableByteArray customByteArray(String description, Function<byte[], byte[]> function) {
        return getModifiableByteArrayWithModification(new CustomModification<>(description, function));
    }

    public static ModifiableByte customByte(String description, Function<Byte, Byte> function) {
        return getModifiableByteWithModification(new CustomModification<>(description, function));
    }

    public static ModifiableInteger customInteger(String description, Function<Integer, Integer> function) {
        return getModifiableIntegerWithModification(new CustomModification<>(description, function));
    }

    public static ModifiableBigInteger customBigInteger(String description, Function<BigInteger, BigInteger> function) {
        return getModifiableBigIntegerWithModification(new CustomModification<>(description, function));
    }

    public static ModifiableLong customLong(String description, Function<Long, Long> function) {
        return getModifiableLongWithModification(new CustomModification<>(description, function));
    }

}
