package fr.greencodeinitiative.python.checks;

import org.junit.Test;
import org.sonar.python.checks.utils.PythonCheckVerifier;

public class AvoidBasicRegexUsagesTest {
    @Test
    public void test(){
        PythonCheckVerifier.verify("src/test/resources/checks/AvoidBasicRegexUsages.py", new AvoidBasicRegexUsagesCheck());
    }
}