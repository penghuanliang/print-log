package com.phl.print.transform.asm;

import java.io.IOException;
import java.io.InputStream;

/**
 * description: Define two methods: 
 * author: phl
 * date: 2020/6/19 10:47
 * update:
 */
public interface IWeaver {


     /**
      * Check a certain file is weavable
      */
     public boolean isWeavableClass(String filePath) throws IOException;

     /**
      * Weave single class to byte array
      */
     public byte[] weaveSingleClassToByteArray(InputStream inputStream) throws IOException;

}
