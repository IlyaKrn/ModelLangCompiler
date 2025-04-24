package com.ilyakrn.lexer.items;

public class BinOperationItem {
    private final String operation;
    private final IdentifierItem.TYPE operand1;
    private final IdentifierItem.TYPE operand2;
    private final IdentifierItem.TYPE result;

    public BinOperationItem(String operation, IdentifierItem.TYPE operand1, IdentifierItem.TYPE operand2, IdentifierItem.TYPE result) {
        this.operation = operation;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }

    public String getOperation() {
        return operation;
    }

    public IdentifierItem.TYPE getOperand1() {
        return operand1;
    }

    public IdentifierItem.TYPE getOperand2() {
        return operand2;
    }

    public IdentifierItem.TYPE getResult() {
        return result;
    }
}
