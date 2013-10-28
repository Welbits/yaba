package com.pilasvacias.yaba.modules.emt.requests;

import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.util.DateUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Pablo Orgaz - 10/28/13 - pabloogc@gmail.com - https://github.com/pabloogc
 * <p/>
 * returns {@link com.pilasvacias.yaba.modules.emt.pojos.Line}
 */
public class GetListLines extends EmtBody {
    /**
     * Format {@code dd-mm-yyyy}
     */
    @XStreamAlias("SelectDate")
    public String selectDate = DateUtils.getToday();
    /**
     * Format {@code 134|90|... or empty for all}
     */
    @XStreamAlias("Lines")
    public String lines = " ";
}
