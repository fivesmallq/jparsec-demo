package im.nll.data;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/2/2 下午2:24
 */
public class AdapterExpression extends Expression {
    public AdapterExpression(String... args) {
        this.args = args;
    }

    public AdapterExpression(List<String> args) {
        this.args = args.stream().toArray(String[]::new);
    }

    private String[] args;

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdapterExpression)) return false;

        AdapterExpression that = (AdapterExpression) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(getArgs(), that.getArgs());

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getArgs());
    }

    @Override
    public String toString() {
        return "AdapterExpression{args='" + Arrays.toString(args) + "'}";
    }
}
