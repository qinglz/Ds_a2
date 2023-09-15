package data_package;

import java.io.Serializable;
import java.util.List;

public class MyRequest implements Serializable {
    private String operation;
    private String word;
    private List<String> meanings;
    public MyRequest(String operation, String word, List<String> meanings){
        this.meanings = meanings;
        this.operation = operation;
        this.word = word;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }

    @Override
    public String toString() {
        return "DataPackage.MyRequest{" +
                "operation='" + operation + '\'' +
                ", word='" + word + '\'' +
                ", meanings=" + meanings +
                '}';
    }
}
