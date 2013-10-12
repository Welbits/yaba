package com.pilasvacias.yaba.modules.soap;

import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.TreeStrategy;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.strategy.Visitor;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

import java.util.Map;

class SoapVisitorStrategy implements Strategy {

    /**
     * This is the strategy that is delegated to by this strategy.
     */
    private final Strategy strategy;
    /**
     * This is the visitor that is used to intercept serialization.
     */
    private Visitor visitor;

    /**
     * Constructor for the <code>VisitorStrategy</code> object. This
     * strategy requires a visitor implementation that can be used
     * to intercept the serialization and deserialization process.
     *
     * @param visitor this is the visitor used for interception
     */
    public SoapVisitorStrategy(Visitor visitor) {
        this(visitor, new TreeStrategy());
    }

    /**
     * Constructor for the <code>VisitorStrategy</code> object. This
     * strategy requires a visitor implementation that can be used
     * to intercept the serialization and deserialization process.
     *
     * @param visitor  this is the visitor used for interception
     * @param strategy this is the strategy to be delegated to
     */
    public SoapVisitorStrategy(Visitor visitor, Strategy strategy) {
        this.strategy = strategy;
        this.visitor = visitor;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    /**
     * This method will read with  an internal strategy after it has
     * been intercepted by the visitor. Interception of the XML node
     * before it is delegated to the internal strategy allows the
     * visitor to change some attributes or details before the node
     * is interpreted by the strategy.
     *
     * @param type this is the type of the root element expected
     * @param node this is the node map used to resolve an override
     * @param map  this is used to maintain contextual information
     * @return the value that should be used to describe the instance
     */
    public Value read(Type type, NodeMap<InputNode> node, Map map) throws Exception {
        if (visitor != null) {
            visitor.read(type, node);
        }
        return strategy.read(type, node, map);
    }

    /**
     * This method will write with an internal strategy before it has
     * been intercepted by the visitor. Interception of the XML node
     * before it is delegated to the internal strategy allows the
     * visitor to change some attributes or details before the node
     * is interpreted by the strategy.
     *
     * @param type this is the type of the root element expected
     * @param node this is the node map used to resolve an override
     * @param map  this is used to maintain contextual information
     * @return the value that should be used to describe the instance
     */
    public boolean write(Type type, Object value, NodeMap<OutputNode> node, Map map) throws Exception {
        boolean result = strategy.write(type, value, node, map);

        if (visitor != null) {
            visitor.write(type, node);
        }
        return result;
    }

    public static class SoapVisitor implements Visitor {

        Class<?> outClass;

        public SoapVisitor(Class<?> outClass) {
            this.outClass = outClass;
        }

        @Override public void read(Type type, NodeMap<InputNode> node) throws Exception {
            if (node.getName().equals("Body"))
                node.getNode().getAttributes().put("class", outClass.getName());
        }

        @Override public void write(Type type, NodeMap<OutputNode> node) throws Exception {
            node.getNode().getAttributes().remove("class");
        }
    }
}