package fr.greencodeinitiative.python.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.SubscriptionContext;
import org.sonar.plugins.python.api.symbols.Symbol;
import org.sonar.plugins.python.api.tree.*;
import org.sonar.python.tree.StringElementImpl;
import org.sonar.python.tree.StringLiteralImpl;
import org.sonar.python.tree.TreeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Rule(key = "EC1000")
public class AvoidBasicRegexUsagesCheck extends PythonSubscriptionCheck{

    public static final String MESSAGE_RULE = "Use startswith instead of re.";
    private static final Map<String, Integer> REGEX_FUNCTIONS_TO_FLAG_PARAM = new HashMap<>();

    private static final Pattern PATTERN = Pattern.compile("^r?['\\\"]\\^");

    static {
        REGEX_FUNCTIONS_TO_FLAG_PARAM.put("re.compile", 1);
        REGEX_FUNCTIONS_TO_FLAG_PARAM.put("re.search", 2);
        REGEX_FUNCTIONS_TO_FLAG_PARAM.put("re.match", 2);
    }

    /**
     * Should return a map whose keys are the functions the check is interested in, and the values are the position of the flags parameter.
     * Set the position of the flags parameter to {@code null} if there is none.
     */
    protected Map<String, Integer> lookedUpFunctions() {
        return REGEX_FUNCTIONS_TO_FLAG_PARAM;
    }

    @Override
    public void initialize(Context context) {
        context.registerSyntaxNodeConsumer(Tree.Kind.CALL_EXPR, this::visitNodeRegexPattern);
    }

    private void visitNodeRegexPattern(SubscriptionContext ctx) {
        CallExpression callExpression = (CallExpression) ctx.syntaxNode();
        Symbol calleeSymbol = callExpression.calleeSymbol();
        if (calleeSymbol == null || calleeSymbol.fullyQualifiedName() == null) {
            return;
        }

        String functionFqn;
        functionFqn = calleeSymbol.fullyQualifiedName();
        if (functionFqn != null && lookedUpFunctions().containsKey(functionFqn)) {
            Expression patternArgument = TreeUtils.nthArgumentOrKeyword(0, "pattern", callExpression.arguments()).expression();
            if (patternArgument != null && patternArgument.is(Tree.Kind.STRING_LITERAL)) {
                StringElement stringElement = ((StringLiteralImpl) patternArgument).stringElements().get(0);
                checkIssue(stringElement, ctx);
            }

        }

    }

    private void checkIssue(StringElement stringElement, SubscriptionContext ctx) {
        if (PATTERN.matcher(stringElement.value()).find()) {
            reportIssue(stringElement, ctx);
        }
    }

    private void reportIssue(StringElement stringElement, SubscriptionContext ctx) {
        ctx.addIssue(stringElement, MESSAGE_RULE);
    }
}