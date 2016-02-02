package im.nll.data;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.Terminals;
import org.codehaus.jparsec.functors.Map;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class AppTest {

    static final String exmplae =
            "project{\n" +
                    "crawler url\n" +
                    "generator seed\n" +
                    "crawler a\n" +
                    "adapter (a,b,c)\n" +
                    "}";

    public Parser<String> whitespaceString() {
        return Scanners.WHITESPACES.next(Scanners.IDENTIFIER).map(o -> String.valueOf(o));
    }

    Parser<?> ignored = Scanners.WHITESPACES.skipMany();

    public Parser<ExtractorExpression> extractorExpression() {
        return Scanners.string("extractor").next(whitespaceString()).map(s -> new ExtractorExpression((String) s));
    }

    public Parser<CrawlerExpression> crawlerExpression() {
        return Scanners.string("crawler").next(whitespaceString()).map(s -> new CrawlerExpression((String) s));
    }

    public Parser<GeneratorExpression> generatorExpression() {
        return Scanners.string("generator").next(whitespaceString()).map(s -> new GeneratorExpression((String) s));
    }

    public Parser<AdapterExpression> adapterExpression() {
        return Parsers.between(Scanners.string("adapter").next(ignored).next(Scanners.string("(").next(ignored)),
                parameterExpress(),
                ignored.next(Scanners.string(")").next(ignored))).map(strings -> new AdapterExpression(strings));
    }

    public Parser<List<String>> parameterExpress() {
        Terminals operators = Terminals.operators(","); // only one operator supported so far
        Parser<?> integerTokenizer = Terminals.Identifier.TOKENIZER;
        Parser<String> integerSyntacticParser = Terminals.Identifier.PARSER;
        Parser<?> ignored = Parsers.or(Scanners.JAVA_BLOCK_COMMENT, Scanners.WHITESPACES);
        Parser<?> tokenizer = Parsers.or(operators.tokenizer(), integerTokenizer); // tokenizes the operators and integer
        Parser<List<String>> integers = integerSyntacticParser.sepBy(operators.token(","))
                .from(tokenizer, ignored.skipMany());
        return integers;
    }

    public Parser<Expression> expression() {
        return Parsers.or(crawlerExpression(), extractorExpression(), generatorExpression(), adapterExpression());
    }


    public Parser<List<Expression>> block() {
        return Parsers.between(Scanners.string("project").next(ignored).next(Scanners.string("{")).next(ignored),
                expression().sepBy(Scanners.WHITESPACES),
                ignored.next(Scanners.string("}")));
    }

    public Parser<Expression> workflow() {
        return expression().sepBy(Scanners.WHITESPACES.skipMany1().next(Scanners.string("->")).next(Scanners.WHITESPACES.skipMany1()))
                .map((Map<List<Expression>, Expression>) expressions -> new WorkflowExpression(expressions));
    }

    public Parser<List<Expression>> workflow2() {
        return expression().sepBy(Scanners.WHITESPACES.skipMany1().next(Scanners.string("->")).next(Scanners.WHITESPACES.skipMany1()));
    }


    public Parser<List<List<Expression>>> all() {
        return block().sepBy(Scanners.WHITESPACES);
    }

    @Test
    public void testWhitespaceString() {
        assertThat(whitespaceString().parse("   rule1"), is((Object) "rule1"));
    }

    @Test
    public void testExtractorExpression() {
        assertThat(extractorExpression().parse("extractor rule1"), is(new ExtractorExpression("rule1")));
    }

    @Test
    public void testCrawlerExpression() {
        assertThat(crawlerExpression().parse("crawler rule1"), is(new CrawlerExpression("rule1")));
    }

    @Test
    public void testGeneratorExpression() {
        assertThat(generatorExpression().parse("generator rule1"), is(new GeneratorExpression("rule1")));
    }

    @Test
    public void testAdapterExpression() {
        assertThat(adapterExpression().parse("adapter ( rule1 )"), is(new AdapterExpression("rule1")));
        assertThat(adapterExpression().parse("adapter  ( a ,   b , c ) "), is(new AdapterExpression("a", "b", "c")));
    }

    @Test
    public void testParameterExpression() {
        List<String> strings = parameterExpress().parse(" a , b , c ");
        assertThat(strings, is(Lists.newArrayList("a", "b", "c")));
    }


    @Test
    public void testExpression() {
        assertThat(expression().parse("adapter (rule1)"), is(new AdapterExpression("rule1")));
        assertThat(expression().parse("adapter (a,b,c)"), is(new AdapterExpression("a", "b", "c")));
        assertThat(expression().parse("crawler url"), is(new CrawlerExpression("url")));
        assertThat(expression().parse("generator url"), is(new GeneratorExpression("url")));
        assertThat(expression().parse("extractor url"), is(new ExtractorExpression("url")));
    }

    @Test
    public void testBlockExpression() {
        assertThat(block().parse("project {\n" +
                "crawler a\n" +
                "adapter (rule) \n" +
                "}").toString(), is("[CrawlerExpression{name='a'}, AdapterExpression{args='[rule]'}]"));
    }

    @Test
    public void testWorkflow() {
        assertThat(workflow2().parse("crawler rule -> extractor rule -> adapter (rule)").toString(),
                is("[CrawlerExpression{name='rule'}, ExtractorExpression{name='rule'}, AdapterExpression{args='[rule]'}]"));
    }

    @Test
    public void testFull() {
        assertEquals(all().parse(exmplae).toString(), "[[CrawlerExpression{name='url'}, GeneratorExpression{name='seed'}, CrawlerExpression{name='a'}, AdapterExpression{args='[a, b, c]'}]]");
    }

    @Test
    public void testFull2() throws IOException {
        String project = Resources.toString(Resources.getResource("demo.project"), Charsets.UTF_8);
        System.out.println(all().parse(project).toString());
    }

}
