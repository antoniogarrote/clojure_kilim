package clojure.lang;

import kilim.Pausable;

/**
 * User: antonio
 * Date: 25/09/2011
 * Time: 13:42
 */
public interface IGeneratorFn {
    public Object generate(Object self) throws Pausable;
}
