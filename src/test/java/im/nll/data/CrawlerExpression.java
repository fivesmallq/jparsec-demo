package im.nll.data;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/2/2 下午2:51
 */
public class CrawlerExpression extends Expression{
    public CrawlerExpression(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrawlerExpression)) return false;

        CrawlerExpression that = (CrawlerExpression) o;

        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CrawlerExpression{name='" + name + "'}";
    }
}
