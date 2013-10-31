package com.pilasvacias.yaba.modules.emt.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.pilasvacias.yaba.core.model.Model;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.converters.basic.IntConverter;

import java.util.Comparator;

@DatabaseTable
public class Line extends Model implements Comparable<Line> {

    @XStreamOmitField
    private static final Comparator<Line> labelComparator = new AlphanumericSorting();
    //
    @XStreamAlias("GroupNumber")
    @DatabaseField(index = true)
    private int groupNumber;
    //
    @XStreamAlias("DateFirst")
    @DatabaseField(index = true)
    private String dateFirst;
    //
    @XStreamAlias("DateEnd")
    @DatabaseField(index = true)
    private String dateEnd;
    //
    @XStreamAlias("Line")
    @XStreamConverter(LineConverter.class)
    @DatabaseField(id = true, index = true)
    private int line;
    //
    @XStreamAlias("Label")
    @DatabaseField(index = true)
    private String label;
    //
    @XStreamAlias("NameA")
    @DatabaseField(index = true)
    private String nameA;
    //
    @XStreamAlias("NameB")
    @DatabaseField(index = true)
    private String nameB;

    public Line() {

    }

    public static Comparator<Line> getLabelComparator() {
        return labelComparator;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getDateFirst() {
        return dateFirst;
    }

    public void setDateFirst(String dateFirst) {
        this.dateFirst = dateFirst;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getLine() {
        return line;
    }

    public void setLineNumber(int line) {
        this.line = line;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNameA() {
        return nameA;
    }

    public void setNameA(String nameA) {
        this.nameA = nameA;
    }

    public String getNameB() {
        return nameB;
    }

    public void setNameB(String nameB) {
        this.nameB = nameB;
    }

    @Override
    public int compareTo(Line another) {
        return label.compareTo(another.label);
    }

    public static class LineConverter extends IntConverter {
        @Override public Object fromString(String str) {
            return Integer.parseInt(str);
        }
    }

    /**
     * @author Kushal Paudyal
     *         www.sanjaal.com/java
     *         Last Modified On 16th July 2009
     *         <p/>
     *         This class is used to sort alphanumeric strings.
     *         <p/>
     *         My solution is inspired from a similar C# implementation available at
     *         http://dotnetperls.com/alphanumeric-sorting written by Sam Allen
     */
    private static class AlphanumericSorting implements Comparator<Line> {
        /**
         * The compare method that compares the alphanumeric strings
         */
        public int compare(Line firstObjToCompare, Line secondObjToCompare) {
            String firstString = firstObjToCompare.label;
            String secondString = secondObjToCompare.label;

            if (secondString == null || firstString == null) {
                return 0;
            }

            int lengthFirstStr = firstString.length();
            int lengthSecondStr = secondString.length();

            int index1 = 0;
            int index2 = 0;

            while (index1 < lengthFirstStr && index2 < lengthSecondStr) {
                char ch1 = firstString.charAt(index1);
                char ch2 = secondString.charAt(index2);

                char[] space1 = new char[lengthFirstStr];
                char[] space2 = new char[lengthSecondStr];

                int loc1 = 0;
                int loc2 = 0;

                do {
                    space1[loc1++] = ch1;
                    index1++;

                    if (index1 < lengthFirstStr) {
                        ch1 = firstString.charAt(index1);
                    } else {
                        break;
                    }
                } while (Character.isDigit(ch1) == Character.isDigit(space1[0]));

                do {
                    space2[loc2++] = ch2;
                    index2++;

                    if (index2 < lengthSecondStr) {
                        ch2 = secondString.charAt(index2);
                    } else {
                        break;
                    }
                } while (Character.isDigit(ch2) == Character.isDigit(space2[0]));

                String str1 = new String(space1);
                String str2 = new String(space2);

                int result;

                if (Character.isDigit(space1[0]) && Character.isDigit(space2[0])) {
                    Integer firstNumberToCompare = Integer.parseInt(str1.trim());
                    Integer secondNumberToCompare = Integer.parseInt(str2.trim());
                    result = firstNumberToCompare.compareTo(secondNumberToCompare);
                } else {
                    result = str1.compareTo(str2);
                }

                if (result != 0) {
                    return result;
                }
            }
            return lengthFirstStr - lengthSecondStr;
        }
    }

}