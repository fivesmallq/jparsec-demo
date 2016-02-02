package im.nll.data.expression;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/2/2 下午2:24
 */
public class WorkflowExpression extends Expression {
    public WorkflowExpression(Expression... args) {
        this.args = args;
    }

    public WorkflowExpression(List<Expression> args) {
        this.args = args.stream().toArray(Expression[]::new);
    }

    private Expression[] args;

    public Expression[] getArgs() {
        return args;
    }

    public void setArgs(Expression[] args) {
        this.args = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkflowExpression)) return false;

        WorkflowExpression that = (WorkflowExpression) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(getArgs(), that.getArgs());

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getArgs());
    }

    @Override
    public String toString() {
        return "WorkflowExpression{args='" + Arrays.toString(args) + "'}";
    }
}
