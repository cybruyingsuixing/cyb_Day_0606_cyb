package com.bway.day_0606_rikao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConUtils {


    public static  String in(InputStream inputStream) throws IOException {
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        String s_temp;
        StringBuffer stringBuffer = new StringBuffer();
        while ((s_temp = br.readLine()) != null) {
            stringBuffer.append(s_temp);
        }
        return stringBuffer.toString();

    }

}
