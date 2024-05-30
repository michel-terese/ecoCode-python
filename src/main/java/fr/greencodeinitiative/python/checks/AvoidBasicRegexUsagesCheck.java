package fr.greencodeinitiative.python.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.SubscriptionContext;
import org.sonar.plugins.python.api.tree.StringLiteral;
import org.sonar.plugins.python.api.tree.Tree;

import java.util.regex.Pattern;


@Rule(key = "EC1000")
public class AvoidBasicRegexUsagesCheck extends PythonSubscriptionCheck{

    public static final String MESSAGE_RULE = "Use startswith instead of re.search";

    @Override
    public void initialize(Context context){
        context.registerSyntaxNodeConsumer(Tree.Kind.STRING_LITERAL, this::visitNodeString);
    }

    private void visitNodeString(SubscriptionContext subscriptionContext) {
        StringLiteral stringLiteral = (StringLiteral) subscriptionContext.syntaxNode();

        if (!stringLiteral.stringElements().isEmpty()){
            String value =  stringLiteral.stringElements().get(0).value();
            if(isBasicRegex(value)) {
                subscriptionContext.addIssue(stringLiteral, MESSAGE_RULE);
            }
        }
    }

    public boolean isBasicRegex(String entiredRegex){
        String basicsRegex[] = {"^.", "^.[A-Za-z0-9]*"};
        for(String regex: basicsRegex){
            if(Pattern.compile(regex).matcher(entiredRegex).matches()) {
                return true;
            }
        }
        return false;
    }
}