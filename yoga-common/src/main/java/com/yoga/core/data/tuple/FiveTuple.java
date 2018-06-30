package com.yoga.core.data.tuple;

public class FiveTuple<A, B, C, D, E> extends FourTuple<A, B, C, D> {
    public final E fifth;

    public FiveTuple(A first, B second, C third, D fourth, E fifth) {
        super(first, second, third, fourth);
        this.fifth = fifth;
    }
}
