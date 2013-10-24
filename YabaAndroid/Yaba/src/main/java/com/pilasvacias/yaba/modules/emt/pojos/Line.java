package com.pilasvacias.yaba.modules.emt.pojos;

import com.pilasvacias.yaba.modules.emt.models.EmtBody;

public class Line {

    public String GroupNumber;
    public String DateFirst;
    public String DateEnd;
    public String Line;
    public String Label;
    public String NameA;
    public String NameB;

    public static class GetListLines extends EmtBody {
        /**
         * c
         * Format {@code dd-mm-yyyy}
         */
        public String SelectDate = "19-8-2013";

        /**
         * Format {@code 134|90|... or empty for all}
         */
        public String Lines = " ";
    }

}