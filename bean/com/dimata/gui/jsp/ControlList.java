package com.dimata.gui.jsp;

import com.dimata.util.Command;

import java.util.Vector;
import javax.servlet.jsp.JspWriter;

/**
 * This will gerate list of data based on vectors : Header and content are
 * customizedable. Url lingk may also be setup for a column.
 */
public class ControlList {

    public ControlList() {
        areaWidth = new String("100%");
        areaStyle = new String("listarea");

        listWidth = new String("100%");
        listStyle = new String("listtable");

        title = new String("");
        titleStyle = new String("listtitle");
        cellStyle = new String("cellStyle");
        cellStyleOdd = new String("cellStyleOdd");
        cellSpacing = new String("1");

        header = new Vector(1, 1);
        headerAlign = new Vector(1, 1);
        colsAlign = new Vector(1, 1);
        headerWidth = new Vector(1, 1);
        headerRowspan = new Vector(1, 1);
        headerColspan = new Vector(1, 1);
        headerStyle = new String("listheader");
        footerStyle = new String("listFooter");

        //update by satrya 2013-10-26
        cellRowspan = new Vector(1, 1);
        cellColspan = new Vector(1, 1);
        cellData = new Vector(1, 1);

        data = new Vector(1, 1);
        footer = new Vector(1, 1);
        footerRowspan = new Vector(1, 1);
        footerColspan = new Vector(1, 1);

        linkRow = -1;
        linkData = new Vector(1, 1);
        linkPrefix = new String("");
        linkSufix = new String("");

        rowSelectedStyle = new String("tabtitlehidden");
    }

    public void initDefault() {
    }
    private int maxFreezingTable = 3;
    private String idStlyeFreezing = "GridView1";
    private String areaWidth;
    private String areaStyle;
    private String listWidth;
    private String listStyle;
    private String title;
    private Vector header;
    private Vector headerAlign;
    private Vector colsAlign;
    private Vector headerWidth;
    private Vector headerRowspan;
    private Vector headerColspan;
    //update by satrya 2013-10-26
    private Vector cellRowspan;
    private Vector cellColspan;
    private Vector cellData;
    private String headerStyle;
    private String footerStyle;
    private Vector data;
    private Vector footer;
    private Vector footerRowspan;
    private Vector footerColspan;
    private int linkRow;
    private Vector linkData;
    private String linkPrefix;
    private String linkSufix;
    private String titleStyle;
    //update by satrya 2012-11-3
    private String styleTable;
    private Vector headerToDataMap;
    private String cellStyle;
    private String cellStyleOdd;
    //update by satrya 2012-09-17
    private String cellStyleNew;
    private String cellSpacing;
    private Vector align = new Vector(1, 1);
    private Vector colsFormat = new Vector(1, 1);
    private int rowStep = 10;
    private int rowStart = -1;
    private String rowSelectedStyle;
    //update by satrya 2012-09-17
    private String cellStyles;
    private String rowSelectedStyles;
    private int border;
    private Vector headerSize = new Vector(1, 1);
    private Vector headerWidthSize = new Vector(1, 1);
    public static String SELF_DRAW = "SELF";

    //update by satrya 2014-03-06
    private String styleSelectableTableValue = "";

    public void reset() {
        data.removeAllElements();
        footer.removeAllElements();
        linkData.removeAllElements();
    }

    /**
     * standard draw the list : from "data" and "linkData"
     */
    public String draw() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\" cellspacing=\"0\">";
        if (title != null && title.length() > 0) {
            str = str + "<tr><td><div class=\"" + titleStyle + "\">" + title
                    + "</div></td></tr>";
        }

        str = str + "<tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\" border=\"" + border + "\"><tr>";
        // create header
        for (int h = 0; h < header.size(); h++) {

            str = str + "<td   width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">" + (String) header.get(h) + "</td>";
        }
        str = str + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        try {
            for (int j = 0; j < data.size(); j++) {
                try { //1
                    tmpRow = (Vector) data.get(j);
                    if (tmpRow != null) {
                        str = str + "<tr valign=\"top\">";

                        for (int k = 0; k < tmpRow.size(); k++) {
                            try {
                                if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                                    str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>";
                                } else {
                                    str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                            + (String) tmpRow.get(k) + "</a></td>";
                                }
                            } catch (Exception exc2) {
                                str = str + "<td > Exception drawlist row="+j +" col="+k+" " + exc2 + " data "+ tmpRow.toString()+"</td>";
                            }
                        }
                        str = str + "</tr>";
                    }
                } catch (Exception exc1) {
                    System.out.println("Exception drawlist" + exc1);
                }
            }
        } catch (Exception exc) {
            return ("Exception drawlist" + exc);
        }
        str = str + "</table></td></tr></table>";
        //System.out.println(str);
        return str;
    }

    public String draw(int selectedIndex) {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\" border=\"" + border + "\"><tr>";
        // create header
        for (int h = 0; h < header.size(); h++) {

            str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">" + (String) header.get(h) + "</td>";
        }
        str = str + "</tr>";
        int colFormatSize = colsFormat != null ? colsFormat.size() : 0;
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                str = str + "<tr valign=\"top\">";

                for (int k = 0; k < tmpRow.size(); k++) {
                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }

                        str = str + "\" " + (String) colsFormat.get(k < colFormatSize ? k : colFormatSize - 1) + ">"
                                + (String) tmpRow.get(k) + "</td>";
                    } else {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }
                        str = str + "\" " + (String) colsFormat.get(k < colFormatSize ? k : colFormatSize - 1) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + (String) tmpRow.get(k) + "</a></td>";
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    /**
     * standard draw the list : from "data" and "linkData" table with border
     * field Name with align celldata with align
     */
    public String drawMe() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\"  class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" border=\"" + border + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        for (int h = 0; h < header.size(); h++) {

            str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">"
                    + "<div align=\"" + (String) headerAlign.get(h) + "\">" + (String) header.get(h) + "</div></td>";
        }
        str = str + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                str = str + "<tr>";

                for (int k = 0; k < tmpRow.size(); k++) {
                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">"
                                + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></td>";
                    } else {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></a></td>";
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    /**
     * standard draw the list : from "data" and "linkData" table with border
     * field Name with align celldata with align
     */
    public String drawMe(int selectedIndex) {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\"  class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" border=\"" + border + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        for (int h = 0; h < header.size(); h++) {

            str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">"
                    + "<div align=\"" + (String) headerAlign.get(h) + "\">" + (String) header.get(h) + "</div></td>";
        }
        str = str + "</tr>";
        System.out.println("sjfmhsdhfdsf");
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                str = str + "<tr>";

                for (int k = 0; k < tmpRow.size(); k++) {
                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }

                        str = str + "\" " + (String) colsFormat.get(k) + ">"
                                + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></td>";
                    } else {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }
                        str = str + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></a></td>";
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    /**
     * standard draw the list : from "data" and "linkData" table with border
     * field Name with align celldata with align
     */
    public void drawMe(JspWriter out, int selectedIndex) {
        drawMe(out, selectedIndex, true);
    }

    public void drawMe(JspWriter out, int selectedIndex, boolean nowrap) {
        String tmpStr = new String("");
        //String str = new String("");
        try {
            out.print("<table width=\"" + areaWidth + "\"  class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                    + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" border=\"" + border + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>");
            // create header
            for (int h = 0; h < header.size(); h++) {
                try {
                    out.print("<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">"
                            + "<div align=\"" + (String) headerAlign.get(h) + "\">" + (String) header.get(h) + "</div></td>");
                } catch (Exception exc1) {
                    System.out.println(" header : " + exc1);
                }
            }
            out.print("</tr>");
            //System.out.println("sjfmhsdhfdsf");
            //create list
            Vector tmpRow = new Vector(1, 1);
            for (int j = 0; j < data.size(); j++) {
                try {
                    tmpRow = (Vector) data.get(j);
                    if (tmpRow != null) {
                        out.print("<tr>");

                        for (int k = 0; k < tmpRow.size(); k++) {
                            try {
                                if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                                    out.print("<td " + (nowrap ? "nowrap" : "") + " class=\"");
                                    if (j == selectedIndex) {
                                        out.print(rowSelectedStyle);
                                    } else {
                                        out.print(((j % 2) == 0 ? cellStyle : cellStyleOdd));
                                    }

                                    out.print("\" " + (String) colsFormat.get(k) + ">"
                                            + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></td>");
                                } else {
                                    out.print("<td " + (nowrap ? "nowrap" : "") + " class=\"");
                                    if (j == selectedIndex) {
                                        out.print(rowSelectedStyle);
                                    } else {
                                        out.print(((j % 2) == 0 ? cellStyle : cellStyleOdd));
                                    }
                                    out.print("\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                            + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></a></td>");
                                }
                            } catch (Exception exc3) {
                                System.out.println(" tmpRow.size() k=" + k + " >> " + exc3);
                            }
                        }
                        out.print("</tr>");
                    }
                } catch (Exception exc2) {
                    System.out.println(" data.size() j= " + j + " >>> " + exc2);
                }
            }
            out.print("</table></td></tr></table>");
        } catch (Exception exc) {
            System.out.println(" Draw List : " + exc);
        }
        //return str;
    }

    /**
     * standard draw the list : from "data" and "linkData" table with border
     * field Name with align celldata with align
     */
    public String drawMeContent(int selectedIndex) {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\"  class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" border=\"" + border + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        for (int h = 0; h < header.size(); h++) {
            str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">"
                    + "<div align=\"" + (String) headerAlign.get(h) + "\">" + (String) header.get(h) + "</div></td>";
        }
        str = str + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                str = str + "<tr>";

                for (int k = 0; k < tmpRow.size(); k++) {
                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }

                        str = str + "\" " + (String) colsFormat.get(k) + ">"
                                + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></td>";
                    } else {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }
                        str = str + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></a></td>";
                    }
                }
                str = str + "</tr>";
                if (j == selectedIndex) {
                    int colspan = tmpRow.size();
                    tmpRow = (Vector) data.get(j + 1);
                    str = str + "<tr>\n<td class=\"" + cellStyle + "\" colspan=\"" + colspan + "\">" + tmpRow.get(0) + "</td>\n</tr>";
                    j++;
                }
            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    public String drawMeListContent(int selectedIndex) {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" border=\"" + border + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" "
                        + "align=\"" + headerAlign.get(h) + "\">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + (String) headerWidth.get(h) + "\" class=\"" + headerStyle + "\" align=\"" + (String) headerAlign.get(h) + "\"> "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {

                str = str + "<tr>";
                boolean rowsAlign = false;
                for (int k = 0; k < tmpRow.size(); k++) {
                    int alg = 0;
                    if (((String) headerRowspan.get(k)).equals("0") || (rowsAlign == true)) {
                        alg = k + 1;
                        rowsAlign = true;
                    } else {
                        alg = k;
                    }

                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }

                        str = str + "\" " + (String) colsFormat.get(k) + ">"
                                + "<div align=\"" + (String) colsAlign.get(alg) + "\">"
                                + (String) tmpRow.get(k) + "</div></td>";
                    } else {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }

                        str = str + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + "<div align=\"" + (String) colsAlign.get(alg) + "\">" + (String) tmpRow.get(k) + "</div></a></td>";
                    }
                }
                str = str + "</tr>";
                if (j == selectedIndex) {
                    int colspan = tmpRow.size();
                    tmpRow = (Vector) data.get(j + 1);
                    str = str + "<tr>\n<td class=\"" + cellStyle + "\" colspan=\"" + colspan + "\">" + tmpRow.get(0) + "</td>\n</tr>";
                    j++;
                }
            }
        }
        str = str + "</table></td></tr></table>";

        return str;
    }

    /**
     * Keterangan : fungsi ini di pakai di weekly report
     *
     * @param selectedIndex
     * @return
     */
    //update by satrya 2012-10-10
    public String drawListColor(int selectedIndex) {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\">";
        if (title != null && title.length() > 0) {
            str = str + "<tr><td><div class=\"" + titleStyle + "\">" + title
                    + "</div></td></tr>";
        }
        str = str + "<tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ";
                if (!colspan.equals("0")) {
                    str = str + "align=\"center\"";
                }
                str = str + ">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                str = str + "<tr valign=\"top\">";

                /*for (int k = 0; k < tmpRow.size(); k++) {
                 if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                 str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>";
                 } else {
                 str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                 + (String) tmpRow.get(k) + "</a></td>";
                 }
                 }*/
                //update by satrya 2012-09-17
                for (int k = 0; k < tmpRow.size(); k++) {
                    try {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            str = str + "<td class=\"";
                            if (j == selectedIndex) {
                                str = str + rowSelectedStyles;
                            } else {
                                str = str + ((j % 2) == 0 ? cellStyle : cellStyles);
                            }

                            str = str + "\" " + (String) colsFormat.get(k) + ">"
                                    + (String) tmpRow.get(k) + "</td>";
                        } else {
                            str = str + "<td nowrap class=\"";
                            if (j == selectedIndex) {
                                str = str + rowSelectedStyles;
                            } else {
                                str = str + ((j % 2) == 0 ? cellStyle : cellStyles);
                            }
                            str = str + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                    + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></a></td>";
                        }
                    } catch (Exception exc3) {
                        System.out.println(" tmpRow.size() k=" + k + " >> " + exc3);
                    }
                }
                //out.print("</tr>");
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    /**
     * draw the list : from "data" and "linkData" fieldName with colspan or
     * rowspan
     */
    public String drawList() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\">";
        if (title != null && title.length() > 0) {
            str = str + "<tr><td><div class=\"" + titleStyle + "\">" + title
                    + "</div></td></tr>";
        }
        str = str + "<tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ";
                if (!colspan.equals("0")) {
                    str = str + "align=\"center\"";
                }
                str = str + ">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                str = str + "<tr valign=\"top\">";

                for (int k = 0; k < tmpRow.size(); k++) {
                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>";
                    } else {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + (String) tmpRow.get(k) + "</a></td>";
                    }
                }

                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }
    
    /* created by dedy 20150906*/
    public String drawListWithFooter() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\">";
        if (title != null && title.length() > 0) {
            str = str + "<tr><td><div class=\"" + titleStyle + "\">" + title
                    + "</div></td></tr>";
        }
        str = str + "<tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ";
                if (!colspan.equals("0")) {
                    str = str + "align=\"center\"";
                }
                str = str + ">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                str = str + "<tr valign=\"top\">";

                for (int k = 0; k < tmpRow.size(); k++) {
                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>";
                    } else {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + (String) tmpRow.get(k) + "</a></td>";
                    }
                }

                str = str + "</tr>";
            }
        }
        
        // create Footer
        ifnewrow = false;
        newrow = "";
        for (int h = 0; h < footer.size(); h++) {
            String colspan = String.valueOf(footerColspan.get(h));
            String rowspan = String.valueOf(footerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ";
                if (!colspan.equals("0")) {
                    str = str + "align=\"right\"";
                }
                str = str + ">" + (String) footer.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"\" class=\"" + headerStyle + "\" > "
                        + (String) footer.get(h) + "</td>";
            }
        }
        str = str + newrow + "</tr>";
        
        str = str + "</table></td></tr></table>";
        return str;
    }

    public String drawListCoolPansData() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\">";
        if (title != null && title.length() > 0) {
            str = str + "<tr><td><div class=\"" + titleStyle + "\">" + title
                    + "</div></td></tr>";
        }
        str = str + "<tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ";
                if (!colspan.equals("0")) {
                    str = str + "align=\"center\"";
                }
                str = str + ">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";

        Vector tmpRow = new Vector(1, 1);
        boolean ifnewrows = false;
        String newrows = "";

        for (int h = 0; h < data.size(); h++) {
            ControlList ctControlList = (ControlList) data.get(h);
            Vector cellColspans = ctControlList.cellColspan;
            Vector cellRowspans = ctControlList.cellRowspan;
            tmpRow = ctControlList.cellData;

            if (tmpRow != null) {
                str = str + "<tr valign=\"top\">";
                int icolspan = 0;
                for (int k = 0; k < tmpRow.size(); k++) {
                    String colspan = String.valueOf(cellColspans.get(k));
                    String rowspan = String.valueOf(cellRowspans.get(k));
                    icolspan = colspan != null && colspan.length() > 0 ? Integer.parseInt(colspan) : 0;

                    if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= k)) {
                            str = str + "<td class=\"" + cellStyle + "\"" + (String) colsFormat.get(k)
                                    + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ";
                            if (!colspan.equals("0")) {
                                str = str + "align=\"left\"";
                            }
                            str = str + ">" + (String) tmpRow.get(k) + "</td>";

                        } else {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(k) + linkSufix + "\">"
                                    + (String) tmpRow.get(k) + "</a></td>";

                            str = str + "<td class=\"" + cellStyle + "\"" + (String) colsFormat.get(k)
                                    + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ";
                            if (!colspan.equals("0")) {
                                str = str + "align=\"left\"";
                            }
                            str = str + "><a href=\"" + linkPrefix + (String) linkData.get(k) + linkSufix + "\">" + (String) tmpRow.get(k) + "</a></td>";
                        }//dsdd

                        if (icolspan != 0) {
                            //tmpRow.remove(icolspan);
                            k = k + icolspan;
                        }
                    } else {
                        /*if (ifnewrows == false) {
                         newrows = newrows + "</tr>";
                         ifnewrows = true;
                         }
                         String tagHtml = "";
                         if (newrows.length() > 5) {
                         tagHtml = newrows.substring(newrows.length() - 5, newrows.length());
                         }
                         if (!tagHtml.equals("</td>")) {
                         newrows = newrows + "<tr>";
                         }*/

                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= k)) {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>";

                        } else {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(k) + linkSufix + "\">"
                                    + (String) tmpRow.get(k) + "</a></td>";
                        }
                            //str = str + "</tr>";

                        // if (tmpRow != null) {
                        //}
                    }

                }

                str = str + "</tr>";
            }
        }
        //System.out.println(newrow);
        // str = str + "</tr>";
        /*for (int j = 0; j < data.size(); j++) {
         tmpRow = (Vector) data.get(j);
         if (tmpRow != null) {
         str = str + "<tr valign=\"top\">";

         for (int k = 0; k < tmpRow.size(); k++) {
         if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
         str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>";
         } else {
         str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
         + (String) tmpRow.get(k) + "</a></td>";
         }
         }
                
         str = str + "</tr>";
         }
         }*/
        str = str + "</table></td></tr></table>";
        return str;
    }

    /**
     * Keterangan : draw the list : from "data" and "linkData" fieldName with
     * colspan or fungsi ini di gunakan di daily report presence ini memberikan
     * warna belang belang (contoh putih-biru)
     *
     * @param out
     * @param selectedIndex
     */
    public void drawList(JspWriter out, int selectedIndex) {
        //update by satrya 2012-09-17
        // public void drawList(JspWriter out, int selectedIndex) {
        String tmpStr = new String("");
        //String str = new String("");
        try {
            out.print("<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\">");

            if (title != null && title.length() > 0) {
                out.print("<tr><td><div class=\"" + titleStyle + "\">" + title
                        + "</div></td></tr>");
            }
            out.print("<tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>");
            // create header
            boolean ifnewrow = false;
            String newrow = "";
            for (int h = 0; h < header.size(); h++) {
                String colspan = String.valueOf(headerColspan.get(h));
                String rowspan = String.valueOf(headerRowspan.get(h));
                if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                    out.print("<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                            + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ");
                    if (!colspan.equals("0")) {
                        out.print("align=\"center\"");
                    }
                    out.print(">" + (String) header.get(h) + "</td>");

                } else {
                    if (ifnewrow == false) {
                        newrow = newrow + "</tr>";
                        ifnewrow = true;
                    }
                    String tagHtml = "";
                    if (newrow.length() > 5) {
                        tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                    }
                    if (!tagHtml.equals("</td>")) {
                        newrow = newrow + "<tr>";
                    }
                    newrow = newrow + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                            + (String) header.get(h) + "</td>";
                }

            }
            //System.out.println(newrow);
            out.print(newrow + "</tr>");
            //create list
            Vector tmpRow = new Vector(1, 1);
            for (int j = 0; j < data.size(); j++) {
                tmpRow = (Vector) data.get(j);
                if (tmpRow != null) {
                    out.print("<tr valign=\"top\">");

                    /* for (int k = 0; k < tmpRow.size(); k++) {
                     if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                     out.print("<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>");
                     } else {
                     out.print("<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">" +
                     (String) tmpRow.get(k) + "</a></td>");
                     }
                     }*/
                    //update by satrya 2012-09-17
                    for (int k = 0; k < tmpRow.size(); k++) {
                        try {
                            if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                                out.print("<td class=\"");
                                if (j == selectedIndex) {
                                    out.print(rowSelectedStyles);
                                } else {
                                    out.print(((j % 2) == 0 ? cellStyle : cellStyles));
                                }

                                out.print("\" " + (String) colsFormat.get(k) + ">"
                                        + (String) tmpRow.get(k) + "</td>");
                            } else {
                                out.print("<td nowrap class=\"");
                                if (j == selectedIndex) {
                                    out.print(rowSelectedStyles);
                                } else {
                                    out.print(((j % 2) == 0 ? cellStyle : cellStyles));
                                }
                                out.print("\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                        + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></a></td>");
                            }
                        } catch (Exception exc3) {
                            System.out.println(" tmpRow.size() k=" + k + " >> " + exc3);
                        }
                    }
                    out.print("</tr>");
                }
            }
            out.print("</table></td></tr></table>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public String drawMeList() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" border=\"" + border + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" "
                        + "align=\"" + headerAlign.get(h) + "\">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + (String) headerWidth.get(h) + "\" class=\"" + headerStyle + "\" align=\"" + (String) headerAlign.get(h) + "\"> "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);

        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);

            if (tmpRow != null) {
                str = str + "<tr valign=\"top\">";
                boolean rowsAlign = false;
                for (int k = 0; k < tmpRow.size(); k++) {

                    int alg = 0;
                    if (((String) headerRowspan.get(k)).equals("0") || (rowsAlign == true)) {
                        alg = k + 1;
                        rowsAlign = true;
                    } else {
                        alg = k;
                    }

                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">"
                                + "<div align=\"" + (String) colsAlign.get(alg) + "\">" + (String) tmpRow.get(k) + "</div></td>";
                    } else {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">"
                                + "<div align=\"" + (String) colsAlign.get(alg) + "\"><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + (String) tmpRow.get(k) + "</a></div></td>";
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";

        return str;
    }

    public String drawMeWithColsList() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" border=\"" + border + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";

        System.out.println("header.size()::::::::::::::::::::::::::" + header.size());

        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" "
                        + "align=\"" + headerAlign.get(h) + "\">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + (String) headerWidth.get(h) + "\" class=\"" + headerStyle + "\" align=\"" + (String) headerAlign.get(h) + "\"> "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";

        System.out.println("data.size():::::::::::::::::::::::::::::::::::::::::::::::::::::" + data.size());

        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);

            System.out.println("tmpRow.size():::::::::::::::::::::::::::::::::::::::::::::::::::::" + tmpRow.size());

            if (tmpRow != null) {
                str = str + "<tr valign=\"top\">";
                boolean rowsAlign = false;
                for (int k = 0; k < tmpRow.size(); k++) {

                    int alg = 0;
                    if (((String) headerRowspan.get(k)).equals("0") || (rowsAlign == true)) {
                        alg = k + 1;
                        rowsAlign = true;
                    } else {
                        alg = k;
                    }

                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">"
                                + "<div align=\"" + (String) colsAlign.get(alg) + "\">" + (String) tmpRow.get(k) + "</div></td>";
                    } else {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">"
                                + "<div align=\"" + (String) colsAlign.get(alg) + "\"><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + (String) tmpRow.get(k) + "</a></div></td>";
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";

        return str;
    }

    /**
     * drawMeWithId ---> to field <table>, <tr> and <td> with ID created by
     * gedhy
     */
    public String drawMeWithId() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\"  class=\"" + areaStyle + "\"> "
                + "<tr><td>"
                + "<table width=\"" + listWidth + "\" border=\"" + border + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\">"
                + "<tr>";

        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" "
                        + "align=\"" + headerAlign.get(h) + "\">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + (String) headerWidth.get(h) + "\" class=\"" + headerStyle + "\" align=\"" + (String) headerAlign.get(h) + "\"> "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";

        //create list
        Vector tmpRow = new Vector(1, 1);
        int spanIndex = 0;

        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {

                if (tmpRow.size() != 1) { // not colspan
                    str = str + "<tr>";
                    boolean rowsAlign = false;
                    for (int k = 0; k < tmpRow.size(); k++) {

                        int alg = 0;
                        if (((String) headerRowspan.get(k)).equals("0") || (rowsAlign == true)) {
                            alg = k + 1;
                            rowsAlign = true;
                        } else {
                            alg = k;
                        }

                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">"
                                    + "<div align=\"" + (String) colsAlign.get(alg) + "\">" + (String) tmpRow.get(k) + "</div></td>";
                        } else {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">"
                                    + "<div align=\"" + (String) colsAlign.get(alg) + "\">"
                                    + "<img alt=\"expand or collapse related purchase request list\" class=\"expandable\" onclick=\"javascript:changepic()\" src=\"/hanoman/image/signplus.gif\" width=\"9\" child=\"childlist" + spanIndex + "\">&nbsp;"
                                    + //"<a style=\"text-decoration:none\" href=\"" + linkPrefix + (String)linkData.get(j) + linkSufix + "\">"+(String)tmpRow.get(k) + "</a></div></td>";
                                    (String) tmpRow.get(k) + "</div></td>";
                        }
                    }
                    str = str + "</tr>";
                }

                if (tmpRow.size() == 1) { // colspan
                    str = str + "<tr class=\"collapsed\" id=\"childlist" + spanIndex + "\">"
                            + "<td colspan=\"" + header.size() + "\">" + (String) tmpRow.get(0) + "</td></tr>";
                    spanIndex++;
                }

            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    /**
     * drawListWithId ---> to field <table>, <tr> and <td> with ID created by
     * lkarunia
     */
    public String drawListWithId() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\"  class=\"" + areaStyle + "\"> "
                + "<tr><td>"
                + "<table width=\"" + listWidth + "\" border=\"" + border + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\">"
                + "<tr>";

        boolean ifnewrow = false;
        String newrow = "";
        System.out.println(">>>>>>>>>>>>>>>>>>" + header.size());
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">" + (String) header.get(h) + "</td>";
            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + (String) headerWidth.get(h) + "\" class=\"" + headerStyle + "\"> "
                        + (String) header.get(h) + "</td>";
            }

        }
        str = str + newrow + "</tr>";

        //create list
        Vector tmpRow = new Vector(1, 1);
        int spanIndex = 0;
        System.out.println(">>>>>>>>>>>>>>>>>>data" + data.size());
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            System.out.println(">>>>>>>>>>>>>>>>>>tmpRow" + tmpRow.size());
            if (tmpRow != null) {
                if (tmpRow.size() != 1) { // not colspan
                    str = str + "<tr>";
                    boolean rowsAlign = false;
                    for (int k = 0; k < tmpRow.size(); k++) {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= spanIndex)) {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">"
                                    + (String) tmpRow.get(k) + "</td>";
                        } else {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(spanIndex) + linkSufix + "\">"
                                    + (String) tmpRow.get(k) + "</a></td>";
                        }
                    }
                    spanIndex++;
                    str = str + "</tr>";
                }

                if (tmpRow.size() == 1) { // colspan
                    System.out.println("spanIndex >>" + (spanIndex));
                    str = str + "<tr class=\"collapsed\" id=\"childlist" + (spanIndex - 1) + "\">"
                            + "<td colspan=\"" + header.size() + "\">" + (String) tmpRow.get(0) + "</td></tr>";
                }

            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    public String drawListandChild() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\"  class=\"" + areaStyle + "\"> "
                + "<tr><td>"
                + "<table width=\"" + listWidth + "\" border=\"" + border + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\">"
                + "<tr>";

        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">" + (String) header.get(h) + "</td>";
            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + (String) headerWidth.get(h) + "\" class=\"" + headerStyle + "\"> "
                        + (String) header.get(h) + "</td>";
            }

        }
        str = str + newrow + "</tr>";

        //create list
        Vector tmpRow = new Vector(1, 1);
        int spanIndex = 0;
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                if (tmpRow.size() != 1) { // not colspan
                    str = str + "<tr>";
                    boolean rowsAlign = false;
                    for (int k = 0; k < tmpRow.size(); k++) {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= spanIndex)) {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">"
                                    + (String) tmpRow.get(k) + "</td>";
                        } else {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(spanIndex) + linkSufix + "\">"
                                    + (String) tmpRow.get(k) + "</a></td>";
                        }
                    }
                    spanIndex++;
                    str = str + "</tr>";
                }

                if (tmpRow.size() == 1) { // colspan
                    System.out.println("spanIndex >>" + (spanIndex));
                    str = str + "<tr>"
                            + "<td colspan=\"" + header.size() + "\">" + (String) tmpRow.get(0) + "</td></tr>";
                }

            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    public String drawList(int selectedIndex) {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\">";
        if (title != null && title.length() > 0) {
            str = str + "<tr><td><div class=\"" + titleStyle + "\">" + title
                    + "</div></td></tr>";
        }
        str = str + "<tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ";
                if (!colspan.equals("0")) {
                    str = str + "align=\"center\"";
                }
                str = str + ">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";

        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {

                str = str + "<tr valign=\"top\">";

                for (int k = 0; k < tmpRow.size(); k++) {
                    try {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            str = str + "<td class=\"";
                            if (j == selectedIndex) {
                                str = str + rowSelectedStyle;
                            } else {
                                str = str + cellStyle;
                            }

                            str = str + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>";
                        } else {
                            str = str + "<td class=\"";
                            if (j == selectedIndex) {
                                str = str + rowSelectedStyle;
                            } else {
                                str = str + cellStyle;
                            }

                            str = str + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                    + (String) tmpRow.get(k) + "</a></td>";
                        }
                    } catch (Exception exc) {
                        System.out.println(exc);
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    public String drawList_LinkSize(int selectedIndex) {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ";
                if (!colspan.equals("0")) {
                    str = str + "align=\"center\"";
                }
                str = str + ">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                        + (String) header.get(h) + "</td>";
            }

        }
        str = str + newrow + "</tr>";

        //System.out.println("str : "+str);
        String strSize = "";
        //System.out.println("headerSize : "+headerSize);
        for (int c = 0; c < headerSize.size(); c++) {
            Vector vectHeader = (Vector) headerSize.get(c);
            Vector vectWidth = (Vector) headerWidthSize.get(c);
            strSize = strSize + "<tr>";
            for (int s = 0; s < vectHeader.size(); s++) {
                strSize = strSize + "<td width=\"" + vectWidth.get(s) + "\" class=\"" + headerStyle + "\">"
                        + "<div align=\"center\">" + (String) vectHeader.get(s) + "</div></td>";
            }
            strSize = strSize + "</tr>";
        }

        str = str + strSize;

        System.out.println("str : " + str);
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {

                str = str + "<tr>";

                for (int k = 0; k < tmpRow.size(); k++) {
                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }

                        str = str + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>";
                    } else {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }

                        str = str + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + (String) tmpRow.get(k) + "</a></td>";
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    /**
     * draw the list : from "data" and "linkData" fieldName with colspan or
     * rowspan fieldName with align listCell with align
     */
    public String drawMeList(int selectedIndex) {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" border=\"" + border + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" "
                        + "align=\"" + headerAlign.get(h) + "\">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + (String) headerWidth.get(h) + "\" class=\"" + headerStyle + "\" align=\"" + (String) headerAlign.get(h) + "\"> "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {

                str = str + "<tr>";
                boolean rowsAlign = false;
                for (int k = 0; k < tmpRow.size(); k++) {
                    int alg = 0;
                    if (((String) headerRowspan.get(k)).equals("0") || (rowsAlign == true)) {
                        alg = k + 1;
                        rowsAlign = true;
                    } else {
                        alg = k;
                    }

                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }

                        str = str + "\" " + (String) colsFormat.get(k) + ">"
                                + "<div align=\"" + (String) colsAlign.get(alg) + "\"><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + (String) tmpRow.get(k) + "</a></div></td>";
                    } else {
                        str = str + "<td class=\"";
                        if (j == selectedIndex) {
                            str = str + rowSelectedStyle;
                        } else {
                            str = str + cellStyle;
                        }

                        str = str + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + "<div align=\"" + (String) colsAlign.get(alg) + "\">" + (String) tmpRow.get(k) + "</div></a></td>";
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";

        return str;
    }

    public String draw_LinkJoinData() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        int numOfCols = header.size();
        for (int h = 0; h < header.size(); h++) {

            str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">" + (String) header.get(h) + "</td>";
        }
        str = str + "</tr>";
        //create list
        String strCell = "";
        String strLink = "";
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            strCell = "";
            strLink = "";
            if (tmpRow != null) {
                str = str + "<tr>";
                int dataCols = tmpRow.size();
                Object obj = ((Object) tmpRow.get(0));
                strLink = obj == null ? "" : obj.toString();

                for (int k = 1; k < numOfCols + 1; k++) { // + 1 , cause of link join data
                    if (k < dataCols) {
                        obj = ((Object) tmpRow.get(k));
                        strCell = obj == null ? "" : obj.toString();
                    } else {
                        strCell = "";
                    }

                    if (linkRow != (k - 1)) {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k - 1) + ">" + strCell + "</td>";
                    } else {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k - 1) + "><a href=\"" + linkPrefix + strLink + linkSufix + "\">"
                                + strCell + "</a></td>";
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    /**
     * this will show the linkRow column of data with the type of input/html or
     * others , specified by the linkPrefix and LinkSufix the data on the
     * linkRow column will be randered between the linkPrefix and LinkSufix. so
     * you can build e.g. : <input name="" type="radio" value="data[linkRow]" >
     *
     */
    public String draw_JoinDataLinkSpec() {
        return draw_JoinDataLinkSpec(0, data.size());
    }

    public String draw_JoinDataLinkSpec(int startRow, int endRow) {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\"> <tr><td><div class=\"" + titleStyle + "\">" + title
                + "</div></td></tr><tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        int numOfCols = header.size();
        for (int h = 0; h < header.size(); h++) {

            str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">" + (String) header.get(h) + "</td>";
        }
        str = str + "</tr>";
        //create list
        String strCell = "";
        //String strLink ="";
        Vector tmpRow = new Vector(1, 1);
        for (int j = startRow; (j < data.size()) && (j <= endRow); j++) {
            tmpRow = (Vector) data.get(j);
            strCell = "";
            if (tmpRow != null) {
                str = str + "<tr>";
                int dataCols = tmpRow.size();
                //strLink = ( (Object)tmpRow.get(0)).toString();

                for (int k = 0; k < numOfCols; k++) {
                    if (k < dataCols) {
                        Object obj = ((Object) tmpRow.get(k));
                        strCell = obj == null ? "" : obj.toString();
                    } else {
                        strCell = "";
                    }

                    if (linkRow != (k)) {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + strCell + "</td>";
                    } else {
                        str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + " >" + linkPrefix + strCell + linkSufix
                                + "</td>";
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";

        return str;
    }

    public String drawMeWithId(String imagePath) {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\"  class=\"" + areaStyle + "\"> "
                + "<tr><td>"
                + "<table width=\"" + listWidth + "\" border=\"" + border + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\">"
                + "<tr>";

        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" "
                        + "align=\"" + headerAlign.get(h) + "\">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + (String) headerWidth.get(h) + "\" class=\"" + headerStyle + "\" align=\"" + (String) headerAlign.get(h) + "\"> "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";

        //create list
        Vector tmpRow = new Vector(1, 1);
        int spanIndex = 0;

        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {

                if (tmpRow.size() != 1) { // not colspan
                    str = str + "<tr>";
                    boolean rowsAlign = false;
                    for (int k = 0; k < tmpRow.size(); k++) {

                        int alg = 0;
                        if (((String) headerRowspan.get(k)).equals("0") || (rowsAlign == true)) {
                            alg = k + 1;
                            rowsAlign = true;
                        } else {
                            alg = k;
                        }

                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">"
                                    + "<div align=\"" + (String) colsAlign.get(alg) + "\">" + (String) tmpRow.get(k) + "</div></td>";
                        } else {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">"
                                    + "<div align=\"" + (String) colsAlign.get(alg) + "\">"
                                    + "<img alt=\"expand or collapse related purchase request list\" class=\"expandable\" onclick=\"javascript:changepic()\" src=\"" + imagePath + "\" width=\"9\" child=\"childlist" + spanIndex + "\">&nbsp;"
                                    + //"<a style=\"text-decoration:none\" href=\"" + linkPrefix + (String)linkData.get(j) + linkSufix + "\">"+(String)tmpRow.get(k) + "</a></div></td>";
                                    (String) tmpRow.get(k) + "</div></td>";
                        }
                    }
                    str = str + "</tr>";
                }

                if (tmpRow.size() == 1) { // colspan
                    str = str + "<tr class=\"collapsed\" id=\"childlist" + spanIndex + "\">"
                            + "<td colspan=\"" + header.size() + "\">" + (String) tmpRow.get(0) + "</td></tr>";
                    spanIndex++;
                }

            }
        }
        str = str + "</table></td></tr></table>";
        return str;
    }

    /**
     * Added by wardana may 24, 2004 Method ini berfungsi utk draw list, dimana
     * listnya ada rowspan atau colspannya. Row berupa vector : index 0 : data
     * list index 1 : rowspan index 2 : colspan
     *
     * @return
     *
     */
    public String drawWithRowColSpans() {
        String tmpStr = new String("");
        String str = new String("");
        Vector vTempRow = new Vector();
        int iRowspan = 0;
        int iColspan = 0;
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\" cellspacing=\"0\">";
        if (title != null && title.length() > 0) {
            str = str + "<tr><td><div class=\"" + titleStyle + "\">" + title
                    + "</div></td></tr>";
        }

        str = str + "<tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\" border=\"" + border + "\"><tr>";
        // create header
        for (int h = 0; h < header.size(); h++) {

            str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">" + (String) header.get(h) + "</td>";
        }
        str = str + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                str = str + "<tr valign=\"top\">";
                for (int k = 0; k < tmpRow.size(); k++) {
                    vTempRow = (Vector) tmpRow.get(k);
                    iRowspan = Integer.parseInt((String) vTempRow.get(1));
                    iColspan = Integer.parseInt((String) vTempRow.get(2));
                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"" + cellStyle + "\" " + (iRowspan > 1 ? " rowspan=\"" + iRowspan + "\" " : "") + (iColspan > 1 ? " colspan=\"" + iColspan + "\" " : "") + (String) colsFormat.get(k) + ">" + (String) vTempRow.get(0) + "</td>";
                    } else {
                        str = str + "<td class=\"" + cellStyle + "\" " + (iRowspan > 1 ? " rowspan=\"" + iRowspan + "\" " : "") + (iColspan > 1 ? " colspan=\"" + iColspan + "\" " : "") + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + (String) vTempRow.get(0) + "</a></td>";
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></td></tr></table>";
        //System.out.println(str);
        return str;
    }

    public String drawListWithRowColSpans() {
        String tmpStr = new String("");
        String str = new String("");
        Vector vTempRow = new Vector();
        int iRowspan = 0;
        int iColspan = 0;
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\">";
        if (title != null && title.length() > 0) {
            str = str + "<tr><td><div class=\"" + titleStyle + "\">" + title
                    + "</div></td></tr>";
        }
        str = str + "<tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ";
                if (!colspan.equals("0")) {
                    str = str + "align=\"center\"";
                }
                str = str + ">" + (String) header.get(h) + "</td>";

            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</td>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                        + (String) header.get(h) + "</td>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                str = str + "<tr valign=\"top\">";

                for (int k = 0; k < tmpRow.size(); k++) {
                    vTempRow = (Vector) tmpRow.get(k);
                    iRowspan = Integer.parseInt((String) vTempRow.get(1));
                    iColspan = Integer.parseInt((String) vTempRow.get(2));

                    if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                        str = str + "<td class=\"" + cellStyle + "\" " + (iRowspan > 1 ? " rowspan=\"" + iRowspan + "\" " : "") + (iColspan > 1 ? " colspan=\"" + iColspan + "\" " : "") + (String) colsFormat.get(k) + ">" + (String) vTempRow.get(0) + "</td>";
                    } else {
                        str = str + "<td class=\"" + cellStyle + "\" " + (iRowspan > 1 ? " rowspan=\"" + iRowspan + "\" " : "") + (iColspan > 1 ? " colspan=\"" + iColspan + "\" " : "") + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                + (String) vTempRow.get(0) + "</a></td>";
                    }
                }
                str = str + "</tr>";
            }
        }

        for (int j = 0; j < footer.size(); j++) {
            tmpRow = (Vector) footer.get(j);
            if (tmpRow != null) {
                str = str + "<tr valign=\"top\">";

                for (int k = 0; k < tmpRow.size(); k++) {
                    vTempRow = (Vector) tmpRow.get(k);
                    iRowspan = Integer.parseInt((String) vTempRow.get(1));
                    iColspan = Integer.parseInt((String) vTempRow.get(2));
                    str = str + "<td class=\"" + footerStyle + "\" " + (iRowspan > 1 ? " rowspan=\"" + iRowspan + "\" " : "") + (iColspan > 1 ? " colspan=\"" + iColspan + "\" " : "") + (String) colsFormat.get(k) + ">" + (String) vTempRow.get(0) + "</td>";
                }
                str = str + "</tr>";
            }
        }

        str = str + "</table></td></tr></table>";
        return str;
    }

    public String drawWithSelf() {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\" cellspacing=\"0\">";
        if (title != null && title.length() > 0) {
            str = str + "<tr><td><div class=\"" + titleStyle + "\">" + title
                    + "</div></td></tr>";
        }

        str = str + "<tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\" border=\"" + border + "\"><tr>";
        // create header
        for (int h = 0; h < header.size(); h++) {

            str = str + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\">" + (String) header.get(h) + "</td>";
        }
        str = str + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                if (tmpRow.size() > 1 && tmpRow.get(0).toString().trim().equals(SELF_DRAW)) {
                    str += tmpRow.get(1);
                } else {
                    str = str + "<tr valign=\"top\">";

                    for (int k = 0; k < tmpRow.size(); k++) {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>";
                        } else {
                            str = str + "<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                    + (String) tmpRow.get(k) + "</a></td>";
                        }
                    }
                    str = str + "</tr>";
                }
            }
        }
        str = str + "</table></td></tr></table>";
        //System.out.println(str);
        return str;
    }

    public String getFooterStyle() {
        return footerStyle;
    }

    public void setFooterStyle(String footerStyle) {
        this.footerStyle = footerStyle;
    }

    public Vector getFooter() {
        return footer;
    }

    public void setFooter(Vector footer) {
        this.footer = footer;
    }

    public String getAreaWidth() {
        return areaWidth;
    }

    public void setAreaWidth(java.lang.String areaWidth) {
        this.areaWidth = areaWidth;
    }

    public String getAreaStyle() {
        return areaStyle;
    }

    public void setAreaStyle(String areaStyle) {
        this.areaStyle = areaStyle;
    }

    public String getListWidth() {
        return listWidth;
    }

    public void setListWidth(java.lang.String listWidth) {
        this.listWidth = listWidth;
    }

    public String getListStyle() {
        return listStyle;
    }

    public void setListStyle(String listStyle) {
        this.listStyle = listStyle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Vector getHeader() {
        return header;
    }

    //public void setHeader(Vector header) { this.header = header; }
    public Vector getHeaderWidth() {
        return headerWidth;
    }

    public Vector getHeaderAlign() {
        return headerAlign;
    }

    public Vector getColsAlign() {
        return colsAlign;
    }

    public Vector getHeaderRowspan() {
        return headerRowspan;
    }

    public Vector getHeaderColspan() {
        return headerColspan;
    }
    
    //public void setHeaderWidth(Vector headerWidth) { this.headerWidth = headerWidth; }
    public void addHeader(String header, String width) {
        this.header.add(header);
        this.headerWidth.add(width);
        this.colsFormat.add("");
        //update by satrya 2012-07-25
        this.headerAlign.add("center");
        this.colsAlign.add("center");
    }

    /**
     *
     * @param header
     * @param width
     * @param rowspan
     * @param colspan
     */
    public void addHeader(String header, String width, String rowspan, String colspan) {
        this.header.add(header);
        this.headerWidth.add(width);
        this.headerRowspan.add(rowspan);
        this.headerColspan.add(colspan);
        this.colsFormat.add("");
        //update by satrya 2012-07-25
        this.headerAlign.add("center");
        this.colsAlign.add("center");
    }
    
    public void addFooter(String footer, String rowspan, String colspan) {
        this.footer.add(footer);
        this.footerRowspan.add(rowspan);
        this.footerColspan.add(colspan);
        this.colsFormat.add("");
        //update by satrya 2012-07-25
        this.headerAlign.add("center");
        this.colsAlign.add("center");
    }

    public void addHeader(String header) {
        this.header.add(header);
        this.headerWidth.add("");
        this.headerRowspan.add("");
        this.headerColspan.add("");
        this.colsFormat.add("");
        //update by satrya 2012-07-25
        this.headerAlign.add("center");
        this.colsAlign.add("center");
    }


    /* public void addHeader(String header)
     {
     this.header.add(header); this.headerWidth.add("");
     this.colsFormat.add("");
     }*/
    public void addHeader(String header, String width, String rowspan, String colspan,
            String colFormat) {
        this.header.add(header);
        this.headerWidth.add(width);
        this.headerRowspan.add(rowspan);
        this.headerColspan.add(colspan);
        this.colsFormat.add(colFormat);
        //update by satrya 2012-07-25
        this.headerAlign.add("center");
        this.colsAlign.add("center");
    }

    public void addHeader(String header, String width, String colFormat) {
        this.header.add(header);
        this.headerWidth.add(width);
        this.colsFormat.add(colFormat);
        //update by satrya 2012-07-25
        this.headerAlign.add("center");
        this.colsAlign.add("center");
    }

    public void dataFormat(String header, String width, String headerAlign, String colsAlign) {
        this.header.add(header);
        this.headerWidth.add(width);
        this.headerAlign.add(headerAlign);
        this.colsAlign.add(colsAlign);
        this.colsFormat.add("");
    }

    public void dataFormat(String header, String width, String rowspan, String colspan,
            String headerAlign, String colsAlign) {
        this.header.add(header);
        this.headerWidth.add(width);
        this.headerRowspan.add(rowspan);
        this.headerColspan.add(colspan);
        this.headerAlign.add(headerAlign);
        this.colsAlign.add(colsAlign);
        this.colsFormat.add("");
    }

    //update by satrya 2013-10-26
    /**
     * untuk setting colom collpans
     *
     * @param name
     * @param rowspan
     * @param colspan
     */
    public void addColoms(String name, String rowspan, String colspan) {
        this.cellData.add(name);
        this.cellRowspan.add(rowspan);
        this.cellColspan.add(colspan);

    }

    public void setHeaderCaption(int idx, String caption) {
        if (idx < this.header.size()) {
            this.header.set(idx, caption);
        }
    }

    public String getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public Vector getData() {
        return data;
    }

    public void setData(Vector data) {
        this.data = data;
    }

    public int getLinkRow() {
        return linkRow;
    }

    public void setLinkRow(int linkRow) {
        this.linkRow = linkRow;
    }

    public Vector getLinkData() {
        return linkData;
    }

    public void setLinkData(Vector linkData) {
        this.linkData = linkData;
    }

    public String getLinkPrefix() {
        return linkPrefix;
    }

    public void setLinkPrefix(String linkPrefix) {
        this.linkPrefix = linkPrefix;
    }

    public String getLinkSufix() {
        return linkSufix;
    }

    public void setLinkSufix(String linkSufix) {
        this.linkSufix = linkSufix;
    }

    public String getTitleStyle() {
        return titleStyle;
    }

    public void setTitleStyle(String titleStyle) {
        this.titleStyle = titleStyle;
    }

    public Vector getHeaderToDataMap() {
        return headerToDataMap;
    }

    /**
     * mapping from header index to data index , standard : default header 0->
     * data 0 ... link join data : default link -> data 0 , header 0-> data 1
     * ...
     */
    public void setHeaderToDataMap(Vector headerToDataMap) {
        this.headerToDataMap = headerToDataMap;
    }

    public String getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(String cellStyle) {
        this.cellStyle = cellStyle;
    }

    public String getCellSpacing() {
        return cellSpacing;
    }

    public void setCellSpacing(String cellSpacing) {
        this.cellSpacing = cellSpacing;
    }

    public Vector getColsFormat() {
        return colsFormat;
    }

    public void setColsFormat(Vector colsFormat) {
        this.colsFormat = colsFormat;
    }

    public int getRowStart() {
        return 0;
    }

    public int getRowStep() {
        return rowStep;
    }

    public void setRowStep(int rowStep) {
        this.rowStep = rowStep;
    }

    public String drawFirst_LJoin() {
        rowStart = 0;
        String html = draw_JoinDataLinkSpec(rowStart, rowStart + rowStep - 1);
        /* if(rowStep<data.size()){
         rowStart = rowStep;
         }*/
        return html;
    }

    public String drawLast_LJoin() {
        int rowNum = data.size();
        if ((rowNum % rowStep) > 0) {
            rowStart = rowNum - (rowNum % rowStep);
        } else {
            rowStart = rowNum - rowStep;
        }
        return draw_JoinDataLinkSpec(rowStart, rowStart + rowStep - 1);
    }

    public String drawNext_LJoin() {

        //System.out.println("00000000   row start : "+rowStart);
        if (rowStart < 0) {
// System.out.println("000000000000 in < 0");
            rowStart = 0;
            return draw_JoinDataLinkSpec(rowStart, rowStart + rowStep - 1);
        } else {
            //System.out.println("---)))) > 0 ");
            if ((rowStart + rowStep) < data.size()) {
// System.out.println("000000000  rowStart : "+ (rowStart + rowStep));
                rowStart = rowStart + rowStep;
                return draw_JoinDataLinkSpec(rowStart, rowStart + rowStep - 1);
            } else {
                return draw_JoinDataLinkSpec(rowStart, data.size());
            }
        }
        //return "";
    }

    public String drawPrev_LJoin() {
        if (rowStart <= 0) {
            rowStart = 0;
            return draw_JoinDataLinkSpec(rowStart, rowStart + rowStep - 1);
        } else {
            if (rowStart - rowStep >= 0) {
                rowStart = rowStart - rowStep;
                return draw_JoinDataLinkSpec(rowStart, rowStart + rowStep - 1);
            }
        }
        return "";
    }

    public static int getNewOffset(int iCommand, int totalItem, int maxOnList, int currentOffset) {
        switch (iCommand) {
            case Command.FIRST:
                return 0;

            case Command.PREV:
                if (currentOffset < 0) {
                    return 0;
                } else {
                    if (currentOffset - maxOnList >= 0) {
                        return currentOffset - maxOnList;
                    } else {
                        return 0;
                    }
                }

            case Command.NEXT:
                if (currentOffset < 0) {
                    return 0;
                } else {
                    if ((currentOffset + maxOnList) < totalItem) {
                        return currentOffset + maxOnList;
                    } else {
                        return currentOffset;
                    }
                }

            case Command.LAST:
                if ((totalItem % maxOnList) > 0) {
                    return totalItem - (totalItem % maxOnList);
                } else {
                    return totalItem - maxOnList;
                }

            default:
                return currentOffset;
        }

    }

    public String getRowSelectedStyle() {
        return rowSelectedStyle;
    }

    public void setRowSelectedStyle(String rowSelectedStyle) {
        this.rowSelectedStyle = rowSelectedStyle;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public void setHeaderSize(Vector vect, Vector vt) {
        this.headerSize.add(vect);
        this.headerWidthSize.add(vt);
    }

    /**
     * @return the cellStyles
     */
    public String getCellStyles() {
        return cellStyles;
    }

    /**
     * @param cellStyles the cellStyles to set
     */
    public void setCellStyles(String cellStyles) {
        this.cellStyles = cellStyles;
    }

    /**
     * @return the rowSelectedStyles
     */
    public String getRowSelectedStyles() {
        return rowSelectedStyles;
    }

    /**
     * @param rowSelectedStyles the rowSelectedStyles to set
     */
    public void setRowSelectedStyles(String rowSelectedStyles) {
        this.rowSelectedStyles = rowSelectedStyles;
    }

    /**
     * Keterangan : draw the list header: from "data" and "linkData" fieldName
     * with colspan or fungsi ini untuk printing header saja. ini memberikan
     * warna belang belang (contoh putih-biru)
     *
     * @param out
     * @param selectedIndex
     * @Author : Kartika , 14 Okt 2012
     */
    public void drawListHeader(JspWriter out) {
        //update by satrya 2012-09-17
        // public void drawList(JspWriter out, int selectedIndex) {
        String tmpStr = new String("");
        //String str = new String("");
        try {
            out.print("<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\">");

            if (title != null && title.length() > 0) {
                out.print("<tr><td><div class=\"" + titleStyle + "\">" + title
                        + "</div></td></tr>");
            }
            out.print("<tr><td><table width=\"" + listWidth + "\" class=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\"><tr>");
            // create header
            boolean ifnewrow = false;
            String newrow = "";
            for (int h = 0; h < header.size(); h++) {
                String colspan = headerColspan != null && headerColspan.size() > h ? String.valueOf(headerColspan.get(h)) : "1";
                String rowspan = headerRowspan != null && headerRowspan.size() > h ? String.valueOf(headerRowspan.get(h)) : "1";
                if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                    out.print("<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                            + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ");
                    if (!colspan.equals("0")) {
                        out.print("align=\"center\"");
                    }
                    out.print(">" + (String) header.get(h) + "</td>");

                } else {
                    if (ifnewrow == false) {
                        newrow = newrow + "</tr>";
                        ifnewrow = true;
                    }
                    String tagHtml = "";
                    if (newrow.length() > 5) {
                        tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                    }
                    if (!tagHtml.equals("</td>")) {
                        newrow = newrow + "<tr>";
                    }
                    newrow = newrow + "<td width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                            + (String) header.get(h) + "</td>";
                }

            }
            //System.out.println(newrow);
            out.print(newrow + "</tr>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void drawListHeaderExcel(JspWriter out) {
        //update by satrya 2012-09-17
        // public void drawList(JspWriter out, int selectedIndex) {
        String tmpStr = new String("");
        //String str = new String("");
        titleStyle = "font-family: Arial, Helvetica, sans-serif; font-weight: bold; color: #FFFFFF;";
        listStyle = "";
        headerStyle = "border:thin #000000; font-size: 12px;font-family: Arial, Helvetica, sans-serif; font-weight: bold; color: #000000;background-color: #33AAFF;";
        try {
            out.print("<table width=\"" + areaWidth + "\">");

            if (title != null && title.length() > 0) {
                out.print("<tr><td>" + title
                        + "</td></tr>");
            }
            out.print("<tr><td><table width=\"" + listWidth + "\" style=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\" border=\"1\"><tr>");
            // create header
            boolean ifnewrow = false;
            String newrow = "";
            for (int h = 0; h < header.size(); h++) {
                String colspan = headerColspan != null && headerColspan.size() > h ? String.valueOf(headerColspan.get(h)) : "1";
                String rowspan = headerRowspan != null && headerRowspan.size() > h ? String.valueOf(headerRowspan.get(h)) : "1";
                if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                    out.print("<td  width=\"" + headerWidth.get(h) + "\" style=\"" + headerStyle + "\""
                            + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ");
                    if (!colspan.equals("0")) {
                        out.print("align=\"center\"");
                    }
                    out.print(">" + (String) header.get(h) + "</td>");

                } else {
                    if (ifnewrow == false) {
                        newrow = newrow + "</tr>";
                        ifnewrow = true;
                    }
                    String tagHtml = "";
                    if (newrow.length() > 5) {
                        tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                    }
                    if (!tagHtml.equals("</td>")) {
                        newrow = newrow + "<tr>";
                    }
                    newrow = newrow + "<td   width=\"" + headerWidth.get(h) + "\" style=\"" + headerStyle + "\" > "
                            + (String) header.get(h) + "</td>";
                }

            }
            //System.out.println(newrow);
            out.print(newrow + "</tr>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void drawListHeaderExcel1(JspWriter out) {
        //update by satrya 2012-09-17
        // public void drawList(JspWriter out, int selectedIndex) {
        String tmpStr = new String("");
        //String str = new String("");
        titleStyle = "font-family: Arial, Helvetica, sans-serif; font-weight: bold; color: #FFFFFF;";
        listStyle = "";
        headerStyle = "border:thin #000000; font-size: 12px;font-family: Arial, Helvetica, sans-serif; font-weight: bold; color: #000000;background-color: #FFFFFF;";
        try {
            out.print("<table width=\"" + areaWidth + "\">");

            if (title != null && title.length() > 0) {
                out.print("<tr><td>" + title
                        + "</td></tr>");
            }
            out.print("<tr><td><table width=\"" + listWidth + "\" style=\"" + listStyle + "\" cellspacing=\"" + cellSpacing + "\" border=\"1\"><tr>");
            // create header
            boolean ifnewrow = false;
            String newrow = "";
            for (int h = 0; h < header.size(); h++) {
                String colspan = headerColspan != null && headerColspan.size() > h ? String.valueOf(headerColspan.get(h)) : "1";
                String rowspan = headerRowspan != null && headerRowspan.size() > h ? String.valueOf(headerRowspan.get(h)) : "1";
                if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                    out.print("<td  width=\"" + headerWidth.get(h) + "\" style=\"" + headerStyle + "\""
                            + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ");
                    if (!colspan.equals("0")) {
                        out.print("align=\"center\"");
                    }
                    out.print(">" + (String) header.get(h) + "</td>");

                } else {
                    if (ifnewrow == false) {
                        newrow = newrow + "</tr>";
                        ifnewrow = true;
                    }
                    String tagHtml = "";
                    if (newrow.length() > 5) {
                        tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                    }
                    if (!tagHtml.equals("</td>")) {
                        newrow = newrow + "<tr>";
                    }
                    newrow = newrow + "<td   width=\"" + headerWidth.get(h) + "\" style=\"" + headerStyle + "\" > "
                            + (String) header.get(h) + "</td>";
                }

            }
            //System.out.println(newrow);
            out.print(newrow + "</tr>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    //update by satrya 2012-11-2

    /**
     * Drawlist format satrya , contohnya yaitu presence report daily.jsp
     *
     * @param out
     */
    public void drawListHeaderWithJs(JspWriter out) {
        //update by satrya 2012-09-17
        // public void drawList(JspWriter out, int selectedIndex) {
        String tmpStr = new String("");
        //String str = new String("");

        try {
            out.print("<table width=\"" + areaWidth + "\">");

            if (title != null && title.length() > 0) {
                out.print("<tr><th><div class=\"" + titleStyle + "\">" + title
                        + "</div></td></tr>");
                // out.print("<tr><th>" + title + "</th></tr>");
            }
            //out.print("<tr><td><div class=\"" + titleStyle + "\"><table id=\"" + "demoTable" + "\" class=\"" + "demoTable" + "\">");
            out.print("<tr><td><div  class=\"skinCon\"><div class=\"fakeContainer\"><table id=\"demoTable\"><tr>");
            // create header
            boolean ifnewrow = false;
            String newrow = "";
            for (int h = 0; h < header.size(); h++) {
                String colspan = headerColspan != null && headerColspan.size() > h ? String.valueOf(headerColspan.get(h)) : "1";
                String rowspan = headerRowspan != null && headerRowspan.size() > h ? String.valueOf(headerRowspan.get(h)) : "1";
                if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                    out.print("<th width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                            + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ");
                    if (!colspan.equals("0")) {
                        out.print("align=\"center\"");
                    }
                    out.print(">" + (String) header.get(h) + "</th>");

                } else {
                    if (ifnewrow == false) {
                        newrow = newrow + "</tr>";
                        ifnewrow = true;
                    }
                    String tagHtml = "";
                    if (newrow.length() > 5) {
                        tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                    }
                    if (!tagHtml.equals("</th>")) {
                        newrow = newrow + "<tr>";
                    }
                    newrow = newrow + "<th width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                            + (String) header.get(h) + "</th>";
                }

            }
            //System.out.println(newrow);
            out.print(newrow + "</tr>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void drawListHeaderWithJsVer2(JspWriter out) {
        //update by satrya 2012-09-17
        // public void drawList(JspWriter out, int selectedIndex) {
//        String tmpStr = new String("");
        //String str = new String("");

        try {
            out.print("<table width=\"" + getAreaWidth() + "\" class=\"" + getAreaStyle() + "\" cellspacing=\"0\">");
//            tmpStr = tmpStr + ("<table width=\"" + getAreaWidth() + "\" class=\"" + getAreaStyle() + "\" cellspacing=\"0\">");
            if (title != null && title.length() > 0) {
                out.print("<tr><td><div class=\"" + getTitleStyle() + "\">" + getTitle()
                        + "</div></td></tr>");

//                tmpStr = tmpStr +("<tr><td><div class=\"" + getTitleStyle() + "\">" + getTitle()
//                        + "</div></td></tr>");
                // out.print("<tr><th>" + title + "</th></tr>");
            }
            //out.print("<tr><td><div class=\"" + titleStyle + "\"><table id=\"" + "demoTable" + "\" class=\"" + "demoTable" + "\">");
            out.print("<tr><td><table width=\"100%\" cellspacing=\"0\" id=\"GridView1\" style=\"width:100%;border-collapse:collapse;\" ><tr class=\"GridviewScrollHeader\">");
            // create header
//            tmpStr = tmpStr +("<tr><td><table width=\"100%\" cellspacing=\"0\" id=\"GridView1\" style=\"width:100%;border-collapse:collapse;\" ><tr class=\"GridviewScrollHeader\">");
            boolean ifnewrow = false;
            String newrow = "";
            for (int h = 0; h < header.size(); h++) {
                String colspan = headerColspan != null && headerColspan.size() > h ? String.valueOf(headerColspan.get(h)) : "1";
                String rowspan = headerRowspan != null && headerRowspan.size() > h ? String.valueOf(headerRowspan.get(h)) : "1";
                if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                    if ((!colspan.equals("0"))) {
                        out.print("<td colspan=\"" + colspan + "\"");
//                        tmpStr = tmpStr +("<td colspan=\"" + colspan + "\"");
                    }
                    if ((!rowspan.equals("0"))) {
                        out.print("<td  rowspan=\"" + rowspan + "\"");
//                        tmpStr = tmpStr +("<td  rowspan=\"" + rowspan + "\"");
                    }

                    if (!colspan.equals("0")) {
                        out.print("align=\"center\"");
//                        tmpStr = tmpStr +("align=\"center\"");
                    }
                    out.print(">" + (String) header.get(h) + "</td>");
//                    tmpStr = tmpStr +(">" + (String) header.get(h) + "</td>");
                } else {
                    if (ifnewrow == false) {
                        newrow = newrow + "</tr>";
                        ifnewrow = true;
                    }
                    String tagHtml = "";

                    if (newrow.length() > 5) {
                        tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                    }
                    if (!tagHtml.equals("</td>")) {
                        newrow = newrow + "<tr class=\"GridviewScrollHeader\">";
                    }
                    newrow = newrow + "<td>"
                            + (String) getHeader().get(h) + "</td>";
                }

            }
            //System.out.println(newrow);
            out.print(newrow + "</tr>");
//            tmpStr = tmpStr +(newrow + "</tr>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void drawListHeaderWithJsVer2SelectTable(JspWriter out) {
        //update by satrya 2012-09-17
        // public void drawList(JspWriter out, int selectedIndex) {
        String tmpStr = new String("");
        //String str = new String("");
        String newrow = "";

        try {
            out.print("<table width=\"" + getAreaWidth() + "\" class=\"" + getAreaStyle() + "\" cellspacing=\"0\">");
              //tmpStr = tmpStr+"<table width=\"" + getAreaWidth() + "\" class=\"" + getAreaStyle() + "\" cellspacing=\"0\">";

            //out.print("<tr><td><div class=\"" + titleStyle + "\"><table id=\"" + "demoTable" + "\" class=\"" + "demoTable" + "\">");
            out.print("<tr><td><table width=\"100%\"  id=\"" + idStlyeFreezing + "\" style=\"width:100%;\" cellspacing=\"0\"><tr class=\"GridviewScrollHeaderTabledPaddingOnePx\">");
            // create header
            //tmpStr = tmpStr + "<tr><td><table width=\"100%\"  id=\""+idStlyeFreezing+"\" style=\"width:100%;\" cellspacing=\"0\"><tr class=\"GridviewScrollHeaderTabledPaddingOnePx\">";
            boolean ifnewrow = false;

            for (int h = 0; h < header.size(); h++) {
                String colspan = headerColspan != null && headerColspan.size() > h ? String.valueOf(headerColspan.get(h)) : "1";
                String rowspan = headerRowspan != null && headerRowspan.size() > h ? String.valueOf(headerRowspan.get(h)) : "1";
                if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                    if ((!colspan.equals("0"))) {
                        out.print("<td colspan=\"" + colspan + "\"");
                        //tmpStr = tmpStr + "<td colspan=\"" + colspan + "\"";

                    }
                    //update by devin 2014-04-28

                    if (!colspan.equals("0")) {
                        out.print("align=\"center\"");
                        //tmpStr = tmpStr + "align=\"center\"";

                    }
                     //update by devin 2014-04-28

                    if ((!rowspan.equals("0"))) {
                        out.print("<td  rowspan=\"" + rowspan + "\"");
                        //tmpStr = tmpStr + "<td  rowspan=\"" + rowspan + "\"";

                    }

                    out.print(">" + (String) header.get(h) + "</td>");
                    //tmpStr = tmpStr + ">" + (String) header.get(h) + "</td>";

                } else {
                    if (ifnewrow == false) {
                        newrow = newrow + "</tr>";
                        //tmpStr = tmpStr +  "</tr>";
                        ifnewrow = true;
                    }
                    String tagHtml = "";

                    if (newrow.length() > 5) {
                        tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                    }
                    if (!tagHtml.equals("</td>")) {
                        newrow = newrow + "<tr class=\"GridviewScrollHeaderTabledPaddingOnePx\">";
                        //tmpStr = tmpStr +  "<tr class=\"GridviewScrollHeaderTabledPaddingOnePx\">";
                    }
                    newrow = newrow + "<td>"
                            + (String) getHeader().get(h) + "</td>";
                    //tmpStr = tmpStr + "<td>"
                    //       + (String) getHeader().get(h) + "</td>";

                }

            }
            //System.out.println(newrow);
            out.print(newrow + "</tr>");
            //tmpStr = tmpStr +  "</tr>";
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void drawListRowJsVer2(JspWriter out, int selectedIndex, Vector tmpRow, int currentIndex) {
        // String tmpStr = new String("");
//        String str = new String("");
        int j = currentIndex;
        try {
            //create list

            if (tmpRow != null) {

                out.print("<tr class=\"GridviewScrollItem\">");

//                str = str + ("<tr class=\""+cellStyles+"\">");
                for (int k = 0; k < tmpRow.size(); k++) {
                    try {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            if (k <= maxFreezingTable) {
                                out.print("<td class=\"Freeze\">" + (String) tmpRow.get(k) + "</td>");

//                                  str = str + ("<td class=\"Freeze\">" + (String) tmpRow.get(k) + "</td>");
                            } else {
                                out.print("<td>" + (String) tmpRow.get(k) + "</td>");

//                                 str = str + ("<td>" + (String) tmpRow.get(k) + "</td>");
                            }
                        } else {
                            if (k <= maxFreezingTable) {
                                out.print("<td class=\"Freeze\">" + (String) getColsFormat().get(k) + "<a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"
                                        + (String) tmpRow.get(k) + "</a></td>");

//                                 str = str + ("<td class=\"Freeze\">" + (String) getColsFormat().get(k) + "<a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"
//                                        + (String) tmpRow.get(k) + "</a></td>");
                            } else {
                                out.print("<td " + (String) getColsFormat().get(k) + "><a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"
                                        + (String) tmpRow.get(k) + "</a></td>");

//                                 str = str + ("<td " + (String) getColsFormat().get(k) + "><a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"
//                                        + (String) tmpRow.get(k) + "</a></td>");
                            }
                        }
                    } catch (Exception exc3) {
                        System.out.println(" tmpRow.size() k=" + k + " >> " + exc3);
                    }
                }
                out.print("</tr>");
                //str = str + ("</tr>");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void drawListRowJsVer2CoolspanRowsPan(JspWriter out, int selectedIndex, ControlList ctControlList, int currentIndex) {

        // String tmpStr = new String("");
        String str = new String("");
        int j = currentIndex;
        try {
            //create list
            Vector cellColspans = ctControlList.cellColspan;
            Vector cellRowspans = ctControlList.cellRowspan;
            Vector tmpRow = ctControlList.cellData;
            if (tmpRow != null) {

                out.print("<tr class=\"GridviewScrollItemSelectableTabledPaddingOnePx\">");

                str = str + ("<tr " + (cellStyles == null ? "" : "class=\"" + cellStyles + "\"") + ">");
                for (int k = 0; k < tmpRow.size(); k++) {
                    try {
                        String colspan = cellColspans != null && cellColspans.size() > k ? String.valueOf(cellColspans.get(k)) : "0";
                        String rowspan = cellRowspans != null && cellRowspans.size() > k ? String.valueOf(cellRowspans.get(k)) : "0";
                        int iColspan = Integer.parseInt(colspan);
                        int iRowspan = Integer.parseInt(rowspan);
                        if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
//                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            //masih belum bisa bwt coolspan  
//                            if (k <= maxFreezingTable) {
//                                out.print("<td class=\"Freeze\" cellspacing=\"0\" cellpadding=\"0\" colspan=\""+iColspan+"\">" + (String) tmpRow.get(k) + "</td>");
//                                
//                                str = str + ("<td class=\"Freeze\" cellspacing=\"0\" cellpadding=\"0\" colspan=\""+iColspan+"\">" + (String) tmpRow.get(k) + "</td>");
//                                 // str = str + ("<td class=\"Freeze\">" + (String) tmpRow.get(k) + "</td>");
//                            } else{
//                                out.print("<td cellspacing=\"0\" cellpadding=\"0\" colspan=\""+iColspan+"\">" + (String) tmpRow.get(k) + "</td>");
//                                
//                               str = str + ("<td cellspacing=\"0\" cellpadding=\"0\" colspan=\""+iColspan+"\">" + (String) tmpRow.get(k) + "</td>");
//                                 //str = str + ("<td>" + (String) tmpRow.get(k) + "</td>");
//                            }
//                        } else {
//                            if (k <= maxFreezingTable) {
//                                out.print("<td class=\"Freeze\" cellspacing=\"0\" cellpadding=\"0\" colspan=\""+iColspan+"\" >" + (String) getColsFormat().get(k) + "<a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"
//                                        + (String) tmpRow.get(k) + "</a></td>");
//                                
//                                str = str + ("<td class=\"Freeze\" cellspacing=\"0\" cellpadding=\"0\" colspan=\""+iColspan+"\" >" + (String) getColsFormat().get(k) + "<a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"+ (String) tmpRow.get(k) + "</a></td>");
//                                 //str = str + ("<td class=\"Freeze\">" + (String) getColsFormat().get(k) + "<a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"
//                                 //       + (String) tmpRow.get(k) + "</a></td>");
//                            } else {
//                                out.print("<td cellspacing=\"0\" cellpadding=\"0\" colspan=\""+iColspan+"\" " + (String) getColsFormat().get(k) + "><a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"+ (String) tmpRow.get(k) + "</a></td>");
//                                
//                               str = str + ("<td cellspacing=\"0\" cellpadding=\"0\" colspan=\""+iColspan+"\" " + (String) getColsFormat().get(k) + "><a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"+ (String) tmpRow.get(k) + "</a></td>");
//                                // str = str + ("<td " + (String) getColsFormat().get(k) + "><a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"
//                                 //       + (String) tmpRow.get(k) + "</a></td>");
//                            }
//                        }
//                       if (iColspan!=0) {
//                            //tmpRow.remove(icolspan);
//                            k = k + iColspan;
//                        }
                        } else {

                            if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                                if (k <= maxFreezingTable) {
                                    out.print("<td  class=\"Freeze\">" + (String) tmpRow.get(k) + "</td>");

                                    str = str + ("<td  class=\"Freeze\">" + (String) tmpRow.get(k) + "</td>");
                                } else {
                                    out.print("<td>" + (String) tmpRow.get(k) + "</td>");

                                    str = str + ("<td>" + (String) tmpRow.get(k) + "</td>");
                                }
                            } else {
                                if (k <= maxFreezingTable) {
                                    out.print("<td class=\"Freeze\">" + (String) getColsFormat().get(k) + "<a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"
                                            + (String) tmpRow.get(k) + "</a></td>");

                                    str = str + ("<td class=\"Freeze\">" + (String) getColsFormat().get(k) + "<a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"
                                            + (String) tmpRow.get(k) + "</a></td>");
                                } else {
                                    out.print("<td " + (String) getColsFormat().get(k) + "><a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"
                                            + (String) tmpRow.get(k) + "</a></td>");

                                    str = str + ("<td " + (String) getColsFormat().get(k) + "><a href=\"" + getLinkPrefix() + (String) getLinkData().get(j) + getLinkSufix() + "\">"
                                            + (String) tmpRow.get(k) + "</a></td>");
                                }
                            }

                        }

                    } catch (Exception exc3) {
                        System.out.println(" tmpRow.size() k=" + k + " >> " + exc3);
                    }
                }
                out.print("</tr>");
                str = str + ("</tr>");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void drawListEndTableJsVer2(JspWriter out) {
        try {
            String str = "";
            out.print("</table></td></tr></table>");
//            str = ("</table></td></tr></table>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * //create by gde 115 2013-01-25 Untuk contoh gede
     *
     * @param out
     */
    public void drawListHeaderWithJq(JspWriter out) {
        //update by satrya 2012-09-17
        // public void drawList(JspWriter out, int selectedIndex) {
        String tmpStr = new String("");
        //String str = new String("");

        try {
            out.print("<table width=\"" + areaWidth + "\">");

            if (title != null && title.length() > 0) {
                out.print("<tr><th><div class=\"" + titleStyle + "\">" + title
                        + "</div></td></tr>");
                // out.print("<tr><th>" + title + "</th></tr>");
            }
            //out.print("<tr><td><div class=\"" + titleStyle + "\"><table id=\"" + "demoTable" + "\" class=\"" + "demoTable" + "\">");
            out.print("<tr><td><div id=\"container\"><div id=\"demo\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"display\" id=\"example\" ><tr><thead>");
            // create header
            boolean ifnewrow = false;
            String newrow = "";
            for (int h = 0; h < header.size(); h++) {
                String colspan = headerColspan != null && headerColspan.size() > h ? String.valueOf(headerColspan.get(h)) : "1";
                String rowspan = headerRowspan != null && headerRowspan.size() > h ? String.valueOf(headerRowspan.get(h)) : "1";
                if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {
                    out.print("<th width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                            + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ");
                    if (!colspan.equals("0")) {
                        out.print("align=\"center\"");
                    }
                    out.print(">" + (String) header.get(h) + "</th>");

                } else {
                    if (ifnewrow == false) {
                        newrow = newrow + "</tr>";
                        ifnewrow = true;
                    }
                    String tagHtml = "";
                    if (newrow.length() > 5) {
                        tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                    }
                    if (!tagHtml.equals("</th>")) {
                        newrow = newrow + "<tr>";
                    }
                    newrow = newrow + "<th width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                            + (String) header.get(h) + "</th>";
                }

            }
            //System.out.println(newrow);
            out.print(newrow + "</tr></thead>");

            out.print("<tfoot>");
            boolean ifnewrowX = false;
            String newrowX = "";
            for (int h = 0; h < header.size(); h++) {
                String colspan = headerColspan != null && headerColspan.size() > h ? String.valueOf(headerColspan.get(h)) : "1";
                String rowspan = headerRowspan != null && headerRowspan.size() > h ? String.valueOf(headerRowspan.get(h)) : "1";
                int icolspan = Integer.parseInt(colspan);
                int irowspan = Integer.parseInt(rowspan);
                if ((irowspan != 0 || icolspan != 0)) {

                    out.print("<th width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                            + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ");
                    if (irowspan > icolspan) {
                        out.print("align=\"center\"");
                    }
                    out.print(">" + (String) header.get(h) + "</th>");

                } else {
                    if (ifnewrowX == false) {
                        newrowX = newrowX + "</tr>";
                        ifnewrowX = true;
                    }
                    String tagHtml = "";
                    if (newrowX.length() > 5) {
                        tagHtml = newrowX.substring(newrowX.length() - 5, newrowX.length());
                    }
                    if (!tagHtml.equals("</th>")) {
                        newrowX = newrowX + "<tr>";
                    }
                    newrowX = newrowX + "<th width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                            + (String) header.get(h) + "</th>";
                }

            }
            //System.out.println(newrow);
            out.print(newrowX + "</tfoot>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * Untuk printing row per row secara langsung sebelumnya fungsi
     * drawListHeader(JspWriter out) harus dipanggil untuk memulai table
     *
     * @param out
     * @param selectedIndex: index yg currently selected
     * @param tmpRow
     * @Author : Kartika , 14 Okt 2012
     * @param currentIndex :
     */
    public void drawListRow(JspWriter out, int selectedIndex, Vector tmpRow, int currentIndex) {
        String tmpStr = new String("");
        //String str = new String("");
        int j = currentIndex;
        try {
            //create list            {                
            if (tmpRow != null) {
                out.print("<tr valign=\"top\">");

                /* for (int k = 0; k < tmpRow.size(); k++) {
                 if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                 out.print("<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>");
                 } else {
                 out.print("<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">" +
                 (String) tmpRow.get(k) + "</a></td>");
                 }
                 }*/
                //update by satrya 2012-09-17
                for (int k = 0; k < tmpRow.size(); k++) {
                    try {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            out.print("<td class=\"");
                            if (j == selectedIndex) {
                                out.print(rowSelectedStyles);
                            } else {
                                out.print(((j % 2) == 0 ? cellStyle : cellStyles));
                            }

                            out.print("\" " + (String) colsFormat.get(k) + ">"
                                    + (String) tmpRow.get(k) + "</td>");
                        } else {
                            out.print("<td  class=\"");
                            if (j == selectedIndex) {
                                out.print(rowSelectedStyles);
                            } else {
                                out.print(((j % 2) == 0 ? cellStyle : cellStyles));
                            }
                            out.print("\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                    + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></a></td>");
                        }
                    } catch (Exception exc3) {
                        System.out.println(" tmpRow.size() k=" + k + " >> " + exc3);
                    }
                }
                out.print("</tr>");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void drawListRowExcel(JspWriter out, int selectedIndex, Vector tmpRow, int currentIndex) {
        String tmpStr = new String("");
        //String str = new String("");
        int j = currentIndex;
        try {
            //create list            {                
            if (tmpRow != null) {
                out.print("<tr valign=\"top\">");

                rowSelectedStyles = "";
                cellStyle = "border:thin #000000; color: #000000;background-color: #FFFFFF;";
                cellStyles = "border:thin #000000; color: #000000;background-color: #F2F2F2;";
                for (int k = 0; k < tmpRow.size(); k++) {
                    try {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            out.print("<td style=\"");

                            out.print(((j % 2) == 0 ? cellStyle : cellStyles));

                            out.print("\" " + (String) colsFormat.get(k) + ">"
                                    + (String) tmpRow.get(k) + "</td>");
                        } else {
                            out.print("<td  style=\"");

                            out.print(((j % 2) == 0 ? cellStyle : cellStyles));

                            out.print("\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                    + "<div align=\"" + "center" + "\" >" + (String) tmpRow.get(k) + "</div></a></td>");
                        }
                    } catch (Exception exc3) {
                        System.out.println(" tmpRow.size() k=" + k + " >> " + exc3);
                    }
                }
                out.print("</tr>");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * //update by satrya 2012-11-08
     *
     * @param out
     * @param selectedIndex
     * @param tmpRow
     * @param currentIndex
     */
    public void drawListRowJs(JspWriter out, int selectedIndex, Vector tmpRow, int currentIndex) {
        // String tmpStr = new String("");
        //String str = new String("");
        int j = currentIndex;
        try {
            //create list
            if (tmpRow != null) {
                // out.print("<tr valign=\"top\" bgcolor=\"#FFFFFF\" onMouseOver=\"this.bgColor='gold';\" onMouseOut=\"this.bgColor='#FFFFFF';\">");
                out.print("<tr valign=\"top\" >");

                for (int k = 0; k < tmpRow.size(); k++) {
                    try {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            out.print("<td class=\"");
                            if (j == selectedIndex) {
                                out.print(rowSelectedStyles);
                            } else {
                                out.print(((j % 2) == 0 ? cellStyle : cellStyles));
                            }

                            out.print("\" " + (String) colsFormat.get(k) + ">"
                                    + (String) tmpRow.get(k) + "</td>");
                        } else {
                            out.print("<td  class=\"");
                            if (j == selectedIndex) {
                                out.print(rowSelectedStyles);
                            } else {
                                out.print(((j % 2) == 0 ? cellStyle : cellStyles));
                            }
                            out.print("\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                    + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></a></td>");
                        }
                    } catch (Exception exc3) {
                        System.out.println(" tmpRow.size() k=" + k + " >> " + exc3);
                    }
                }
                out.print("</tr>");
            }
            /*if (tmpRow != null) {
             out.print("<tr valign=\"top\">");

             for (int k = 0; k < tmpRow.size(); k++) {
             if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
             out.print("<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>");
             } else {
             out.print("<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
             + (String) tmpRow.get(k) + "</a></td>");
             }
             }
             out.print("</tr>");
             // }
             }*/

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     *  //create by gde 115 2013-01-25
     *
     * @param out
     * @param selectedIndex
     * @param tmpRow
     * @param currentIndex
     */
    public void drawListRowJq(JspWriter out, int selectedIndex, Vector tmpRow, int currentIndex) {
        // String tmpStr = new String("");
        //String str = new String("");
        int j = currentIndex;
        try {
            //create list
            if (tmpRow != null) {
                out.print("<tbody><tr valign=\"top\">");
                for (int k = 0; k < tmpRow.size(); k++) {
                    try {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            out.print("<td class=\"");
                            if (j == selectedIndex) {
                                out.print(rowSelectedStyles);
                            } else {
                                out.print(((j % 2) == 0 ? cellStyle : cellStyles));
                            }

                            out.print("\" " + (String) colsFormat.get(k) + ">"
                                    + (String) tmpRow.get(k) + "</td>");
                        } else {
                            out.print("<td  class=\"");
                            if (j == selectedIndex) {
                                out.print(rowSelectedStyles);
                            } else {
                                out.print(((j % 2) == 0 ? cellStyle : cellStyles));
                            }
                            out.print("\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                    + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></a></td>");
                        }
                    } catch (Exception exc3) {
                        System.out.println(" tmpRow.size() k=" + k + " >> " + exc3);
                    }
                }
                out.print("</tr></tbody>");
            }
            /*if (tmpRow != null) {
             out.print("<tr valign=\"top\">");

             for (int k = 0; k < tmpRow.size(); k++) {
             if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
             out.print("<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + ">" + (String) tmpRow.get(k) + "</td>");
             } else {
             out.print("<td class=\"" + cellStyle + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
             + (String) tmpRow.get(k) + "</a></td>");
             }
             }
             out.print("</tr>");
             // }
             }*/

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * write out </table></td></tr></table> untuk menutup table list yang
     * sebelumnya di write out dengan dengan : drawListHeader dan drawListRow
     *
     * @param out
     * @Author : Kartika , 14 Okt 2012
     */
    public void drawListEndTable(JspWriter out) {
        try {
            out.print("</table></td></tr></table>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    //update by satrya 2012-11-1

    /**
     * Keterangan: ini pasangannya dengan belakangnya js
     *
     * @param out
     */
    public void drawListEndTableJs(JspWriter out) {
        try {
            out.print("</table></div></div></td></tr></table>");
            //out.print("</table></td></tr></div></table></div>");
//              out.print("</table></div></div>"
//                    + "<script type=\"text/javascript\" src=\"<%=approot%>/javascripts/superTables.js\">"
//                    + "</script><script type=\"text/javascript\">"
//                    + "//<![CDATA[(function () {"
//                    + "new superTable(\"demoTable\", {"
//                    + "cssSkin : \"sDefault\",fixedCols : 8,headerRows : 2});})();//]]></script></td></tr></table>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    //create by gdew 115 2013-01-25
    public void drawListEndTableJq(JspWriter out) {
        try {
            out.print("</table></div></div></td></tr></table>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    //update by satrya 2012-11-10

    /**
     * Untuk drawlist fixed header, header tetap muncul walaupun di scroll
     *
     * @return
     */
    public String drawListFixedHeader(int selectedIndex) {
        String tmpStr = new String("");
        String str = new String("");
        str = "<table width=\"" + areaWidth + "\" class=\"" + areaStyle + "\">";
        if (title != null && title.length() > 0) {
            str = str + "<tr><th><div class=\"" + titleStyle + "\">" + title
                    + "</div></td></tr>";

        }

        str = str + "<tr><td><div class=\"skinCon\"><div class=\"fakeContainer\"><table id=\"demoTable\"><tr>";
        // create header
        boolean ifnewrow = false;
        String newrow = "";
        for (int h = 0; h < header.size(); h++) {
            String colspan = String.valueOf(headerColspan.get(h));
            String rowspan = String.valueOf(headerRowspan.get(h));
            if ((!colspan.equals("0")) || (!rowspan.equals("0"))) {

                str = str + "<th width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\""
                        + " rowspan=\"" + rowspan + "\" colspan=\"" + colspan + "\" ";
                if (!colspan.equals("0")) {
                    str = str + "align=\"center\"";
                }
                str = str + ">" + (String) header.get(h) + "</th>";
            } else {
                if (ifnewrow == false) {
                    newrow = newrow + "</tr>";
                    ifnewrow = true;
                }
                String tagHtml = "";
                if (newrow.length() > 5) {
                    tagHtml = newrow.substring(newrow.length() - 5, newrow.length());
                }
                if (!tagHtml.equals("</th>")) {
                    newrow = newrow + "<tr>";
                }
                newrow = newrow + "<th width=\"" + headerWidth.get(h) + "\" class=\"" + headerStyle + "\" > "
                        + (String) header.get(h) + "</th>";
            }

        }
        //System.out.println(newrow);
        str = str + newrow + "</tr>";
        //create list
        Vector tmpRow = new Vector(1, 1);
        for (int j = 0; j < data.size(); j++) {
            tmpRow = (Vector) data.get(j);
            if (tmpRow != null) {
                str = str + "<tr valign=\"top\">";
                for (int k = 0; k < tmpRow.size(); k++) {
                    try {
                        if ((linkRow != k) || (linkData == null) || (linkData.size() <= j)) {
                            str = str + "<td class=\"";
                            if (j == selectedIndex) {
                                str = str + rowSelectedStyles;
                            } else {
                                str = str + ((j % 2) == 0 ? cellStyle : cellStyles);
                            }

                            str = str + "\" " + (String) colsFormat.get(k) + ">"
                                    + (String) tmpRow.get(k) + "</td>";
                        } else {
                            str = str + "<td  class=\"";
                            if (j == selectedIndex) {
                                str = str + rowSelectedStyles;
                            } else {
                                str = str + ((j % 2) == 0 ? cellStyle : cellStyles);
                            }
                            str = str + "\" " + (String) colsFormat.get(k) + "><a href=\"" + linkPrefix + (String) linkData.get(j) + linkSufix + "\">"
                                    + "<div align=\"" + (String) colsAlign.get(k) + "\" >" + (String) tmpRow.get(k) + "</div></a></td>";
                        }
                    } catch (Exception exc3) {
                        System.out.println(" tmpRow.size() k=" + k + " >> " + exc3);
                    }
                }
                str = str + "</tr>";
            }
        }
        str = str + "</table></div></div></td></tr></table>";
        return str;
    }

    /**
     * @return the styleTable
     */
    public String getStyleTable() {
        return styleTable;
    }

    /**
     * @param styleTable the styleTable to set
     */
    public void setStyleTable(String styleTable) {
        this.styleTable = styleTable;
    }

    /**
     * @return the maxFreezingTable
     */
    public int getMaxFreezingTable() {
        return maxFreezingTable;
    }

    /**
     * @param maxFreezingTable the maxFreezingTable to set
     */
    public void setMaxFreezingTable(int maxFreezingTable) {
        this.maxFreezingTable = maxFreezingTable;
    }

    /**
     * @return the styleSelectableTableValue
     */
    public String getStyleSelectableTableValue() {
        if (styleSelectableTableValue == null || styleSelectableTableValue.length() == 0) {
            return "";
        }
        return styleSelectableTableValue;
    }

    /**
     * @param styleSelectableTableValue the styleSelectableTableValue to set
     */
    public void setStyleSelectableTableValue(String styleSelectableTableValue) {
        this.styleSelectableTableValue = styleSelectableTableValue;
    }

    /**
     * @return the idStlyeFreezing
     */
    public String getIdStlyeFreezing() {
        return idStlyeFreezing;
    }

    /**
     * @param idStlyeFreezing the idStlyeFreezing to set
     */
    public void setIdStlyeFreezing(String idStlyeFreezing) {
        this.idStlyeFreezing = idStlyeFreezing;
    }

    /**
     * @return the footerRowspan
     */
    public Vector getFooterRowspan() {
        return footerRowspan;
    }

    /**
     * @param footerRowspan the footerRowspan to set
     */
    public void setFooterRowspan(Vector footerRowspan) {
        this.footerRowspan = footerRowspan;
    }

    /**
     * @return the footerColspan
     */
    public Vector getFooterColspan() {
        return footerColspan;
    }
}
