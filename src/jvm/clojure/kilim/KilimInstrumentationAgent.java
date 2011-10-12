package clojure.kilim;

import java.lang.instrument.Instrumentation;

/**
 * User: antonio
 * Date: 07/10/2011
 * Time: 14:41
 */
public class KilimInstrumentationAgent {
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        instrumentation.addTransformer(new KilimTransformerAgent());
    }
}
