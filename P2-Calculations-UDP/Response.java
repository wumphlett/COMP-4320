public class Response {
    public Integer tml;
    public Integer id;
    public Integer errorCode;
    public Integer result;

    public Response(int id, int errorCode, int result) {
        this.tml = 7;
        this.id = id;
        this.errorCode = errorCode;
        this.result = result;
    }

    public String toString() {
        return "Request #" + this.id + ": " + this.result + " (error: " + this.errorCode + ")";
    }
}
