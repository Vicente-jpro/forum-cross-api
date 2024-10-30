package ao.angola.demo.util;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class ApiErrors {

    @Getter
    private List<String> errors;

    @Getter
    private int code;

    public ApiErrors(List<String> errors, int code) {

        this.errors = errors;
        this.code = code;
    }

    public ApiErrors(String mensagemErro, int code) {
        this.errors = Arrays.asList(mensagemErro);
        this.code = code;
    }
}
