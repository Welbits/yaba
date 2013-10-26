package com.pilasvacias.yaba.modules.emt.pojos;

import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.TreeSet;

/**
 * Created by Pablo Orgaz - 10/26/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class Node extends Pojo {

    public String Node;
    String PosxNode;
    String PosyNode;
    String Name;
    String Lines;

    public static class GetNodesLines extends EmtBody {

        /**
         * Format {@code 134|90|... or empty for all}
         */
        @XStreamAlias("Nodes")
        private String nodes = "";

        @XStreamOmitField
        private TreeSet<String> nodeSet;

        public void setNodes(TreeSet<String> nodes, boolean allIfEmpty) {
            StringBuilder builder = new StringBuilder();
            for (String node : nodes) {
                builder.append(node.trim()).append("|");
            }
            this.nodeSet = nodes;

            //Append -1 at the end so
            //no nodes will be returned instead of all
            if (!allIfEmpty)
                builder.append("-1");

            this.nodes = builder.toString();
        }

        public void setNodes(String[] nodes, boolean allIfEmpty) {
            TreeSet<String> nodeSet = new TreeSet<String>();
            for (String node : nodes) {
                nodeSet.add(node);
            }
            setNodes(nodeSet, allIfEmpty);
        }

        public String getNodesAsString() {
            return nodes;
        }

        /**
         * NOTE: Modifying this set WON'T CHANGE THE NODES SENT.
         * {@link #setNodes(java.util.TreeSet, boolean)} afterwards.
         *
         * @return
         */
        public TreeSet<String> getNodes() {
            return nodeSet;
        }
    }


}
