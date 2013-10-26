package com.pilasvacias.yaba.modules.emt.pojos;

import com.pilasvacias.yaba.modules.emt.models.EmtBody;

/**
 * Created by Pablo Orgaz - 10/26/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class Node extends Pojo {

    String Node;
    String PosxNode;
    String PosyNode;
    String Name;
    String Lines;

    public static class GetNodesLines extends EmtBody {
        public String Nodes;
    }
}
