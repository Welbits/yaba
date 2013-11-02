package com.pilasvacias.yaba.modules.emt.requests;

import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.TreeSet;

/**
 * Created by Pablo Orgaz - 10/28/13 - pabloogc@gmail.com - https://github.com/pabloogc
 * <p/>
 * Returns {@link com.pilasvacias.yaba.modules.emt.pojos.Stop}
 */
public class GetNodesLines extends EmtBody {

    /**
     * Format {@code 134|90|... or empty for all}
     */
    @XStreamAlias("Nodes") private String stops = "";
    @XStreamOmitField private TreeSet<String> stopsSet;

    public void setNodes(TreeSet<String> stopsTreeSet, boolean allIfEmpty) {
        StringBuilder builder = new StringBuilder();
        for (String node : stopsTreeSet) {
            builder.append(node.trim()).append("|");
        }
        this.stopsSet = stopsTreeSet;

        //Append -1 at the end so
        //no stops will be returned instead of all
        if (!allIfEmpty)
            builder.append("-1");

        this.stops = builder.toString();
    }

    public void setNodes(String[] nodes, boolean allIfEmpty) {
        TreeSet<String> nodeSet = new TreeSet<String>();
        for (String node : nodes) {
            nodeSet.add(node);
        }
        setNodes(nodeSet, allIfEmpty);
    }

    public String getStopsString() {
        return stops;
    }

    /**
     * NOTE: Modifying this set WON'T CHANGE THE NODES SENT.
     * {@link #setNodes(java.util.TreeSet, boolean)} afterwards.
     *
     * @return
     */
    public TreeSet<String> getStops() {
        return stopsSet;
    }
}
