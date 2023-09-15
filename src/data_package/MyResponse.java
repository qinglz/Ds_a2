package data_package;

import java.io.Serializable;
import java.util.List;

public class MyResponse implements Serializable {
    private int status;
    private String msg;
    private List<String> meanings;

    public MyResponse(int status, String msg, List<String> meanings) {
        this.status = status;
        this.msg = msg;
        this.meanings = meanings;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }

    @Override
    public String toString() {
        return "DataPackage.MyResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", meanings=" + meanings +
                '}';
    }
}
