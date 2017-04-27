package org.kevin.app.bookcrawler

import java.io._

object Common {
    def nanoTime = System.nanoTime()

    def log(message: String) = println(message)

    def getFileNameWithoutExtension(file: String): String = {

        if ((file != null) && (file.length() > 0)) {   
            val dot = file.lastIndexOf('.');   
            if ((dot > -1) && (dot < (file.length()))) {   
                return file.substring(0, dot);   
            }   
        }   

        return file;  
    }

    def getExtension(file: String): String = {

        if ((file != null) && (file.length() > 0)) {   
            val dot = file.lastIndexOf('.');   
            if ((dot > -1) && (dot < (file.length() - 1))) {   
                return file.substring(dot + 1);   
            }   
        }   
        
        return ""
    } 

    def getIncreasingFileName(file: String, Id: Int) = {

        val fileName = getFileNameWithoutExtension(file)
        val extension = getExtension(file)
        val result = s"${fileName}_${Id}.${extension}"
        result
    }
}