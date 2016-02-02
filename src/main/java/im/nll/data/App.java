package im.nll.data;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.functors.Map;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    public Parser whitespaceString() {
        return Scanners.WHITESPACES.next(Scanners.IDENTIFIER).map(new Map() {
            public Object map(Object o) {
                return String.valueOf(o);
            }
        });
    }
}
