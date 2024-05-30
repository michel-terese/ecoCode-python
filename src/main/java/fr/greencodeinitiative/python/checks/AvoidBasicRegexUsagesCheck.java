/*
 * ecoCode - Python language - Provides rules to reduce the environmental footprint of your Python programs
 * Copyright © 2023 Green Code Initiative (https://www.ecocode.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.greencodeinitiative.python.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.SubscriptionContext;
import org.sonar.plugins.python.api.symbols.Symbol;
import org.sonar.plugins.python.api.tree.*;
import org.sonar.python.tree.StringLiteralImpl;
import org.sonar.python.tree.TreeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Rule(key = "EC1000")
public class AvoidBasicRegexUsagesCheck extends PythonSubscriptionCheck{

    private static final Map<String, Integer> REGEX_FUNCTIONS_TO_FLAG_PARAM = new HashMap<>();

    public static final String MESSAGE_RULE_FOR_START_WITH = "Use startswith instead of regex";
    public static final String MESSAGE_RULE_FOR_ENDS_WITH = "Use endsWith instead of regex";

    private static final Pattern PATTERN_STARTS_WITH_FIND = Pattern.compile("^r?['\\\"](?:\\^|\\\\A)(.+)['\\\"]$");
    private static final Pattern PATTERN_SPECIAL_CHARS = Pattern.compile("[^\\\\][.+*^$?]");
    private static final Pattern PATTERN_ENDS_WITH = Pattern.compile("^r?['\\\"](.+)(?:\\$|\\\\Z)['\\\"]$");

    static {
        REGEX_FUNCTIONS_TO_FLAG_PARAM.put("re.compile", 0);
        REGEX_FUNCTIONS_TO_FLAG_PARAM.put("re.search", 0);
        REGEX_FUNCTIONS_TO_FLAG_PARAM.put("re.match", 0);
    }

    /**
     * Should return a map whose keys are the functions the check is interested in
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
                checkAndReportIssue(stringElement, ctx);
            }
        }
    }

    private void checkAndReportIssue(StringElement stringElement, SubscriptionContext ctx) {
        Matcher matcher = PATTERN_STARTS_WITH_FIND.matcher(stringElement.value());
        if (matcher.find()) {
            String s = matcher.group(1);
            if (!PATTERN_SPECIAL_CHARS.matcher(s).find()) {
                ctx.addIssue(stringElement, MESSAGE_RULE_FOR_START_WITH);
            }
        }
        else if (PATTERN_ENDS_WITH.matcher(stringElement.value()).find()) {
            ctx.addIssue(stringElement, MESSAGE_RULE_FOR_ENDS_WITH);
        }
    }
}