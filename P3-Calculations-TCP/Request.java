public class Request {
    public Integer tml;
    public Integer id;
    public Integer opCode;
    public Integer numberOfOperands;
    public Integer operandOne;
    public Integer operandTwo;

    public Request(int id, int opCode, int operandOne) {
        this.tml = 6;
        this.id = id;
        this.opCode = opCode;
        this.numberOfOperands = 1;
        this.operandOne = operandOne;
        this.operandTwo = null;
    }

    public Request(int id, int opCode, int operandOne, int operandTwo) {
        this.tml = 8;
        this.id = id;
        this.opCode = opCode;
        this.numberOfOperands = 2;
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public int compute() {
        switch (this.opCode) {
            case 0:
                return this.operandOne + this.operandTwo;
            case 1:
                return this.operandOne - this.operandTwo;
            case 2:
                return this.operandOne | this.operandTwo;
            case 3:
                return this.operandOne & this.operandTwo;
            case 4:
                return this.operandOne >> this.operandTwo;
            case 5:
                return this.operandOne << this.operandTwo;
            case 6:
                return ~this.operandOne;
            default:
                return -1;
        }
    }

    public String toString() {
        String operator = "";
        switch (this.opCode) {
            case 0:
                operator = "+";
                break;
            case 1:
                operator = "-";
                break;
            case 2:
                operator = "|";
                break;
            case 3:
                operator = "&";
                break;
            case 4:
                operator = ">>";
                break;
            case 5:
                operator = "<<";
                break;
            case 6:
                operator = "~";
                break;
        }
        if (operator != "~") {
            return "Request #" + this.id + ": " + this.operandOne + " " + operator + " " + this.operandTwo;
        } else {
            return "Request #" + this.id + ": " + operator + this.operandOne;
        }
    }
}
