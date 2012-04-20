package com.eads.threedviewer.util

import java.text.NumberFormat
import org.jcae.mesh.xmldata.MeshExporter

class AppUtil {

    static List getTriangularList(List list) {
        List triangularList = []
        List temp = []
        list.eachWithIndex {val, index ->
            temp.add(val)
            if ((index % 3 == 2)) {
                triangularList.add(temp)
                temp = []
            }
        }
        return triangularList
    }

    static List createFormatI10List(List list) {
        NumberFormat formatterI10 = new MeshExporter.FormatI10()
        return createFormattedList(list, formatterI10)
    }

    static List createFormatI25List(List list) {
        NumberFormat formatterI25 = new MeshExporter.FormatD25_16()
        return createFormattedList(list, formatterI25)
    }

    static List createFormattedList(List list, NumberFormat formatter) {
        return list.collect {formatter.format(it)}.toList()
    }
}
