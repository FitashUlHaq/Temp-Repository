/* ====================================================================
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==================================================================== */

package org.apache.poi.ss.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.poi.hssf.record.TestcaseRecordInputStream;
import org.apache.poi.util.LittleEndianOutputStream;

public final class TestCellRangeAddress extends TestCase {
    byte[] data = new byte[] { (byte) 0x02, (byte) 0x00, (byte) 0x04,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, };

    public void testLoad() {
        CellRangeAddress ref = new CellRangeAddress(
                TestcaseRecordInputStream.create(0x000, data));
        assertEquals(2, ref.getFirstRow());
        assertEquals(4, ref.getLastRow());
        assertEquals(0, ref.getFirstColumn());
        assertEquals(3, ref.getLastColumn());

        assertEquals(8, CellRangeAddress.ENCODED_SIZE);
    }

    public void testLoadInvalid() {
        try {
            assertNotNull(new CellRangeAddress(
                TestcaseRecordInputStream.create(0x000, new byte[] { (byte)0x02 })));
        } catch (RuntimeException e) {
            assertTrue("Had: " + e, e.getMessage().contains("Ran out of data"));
        }
    }

    public void testStore() throws IOException {
        CellRangeAddress ref = new CellRangeAddress(0, 0, 0, 0);

        byte[] recordBytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LittleEndianOutputStream out = new LittleEndianOutputStream(baos);
        try {
            // With nothing set
            ref.serialize(out);
            recordBytes = baos.toByteArray();
            assertEquals(recordBytes.length, data.length);
            for (int i = 0; i < data.length; i++) {
                assertEquals("At offset " + i, 0, recordBytes[i]);
            }

            // Now set the flags
            ref.setFirstRow((short) 2);
            ref.setLastRow((short) 4);
            ref.setFirstColumn((short) 0);
            ref.setLastColumn((short) 3);

            // Re-test
            baos.reset();
            ref.serialize(out);
            recordBytes = baos.toByteArray();

            assertEquals(recordBytes.length, data.length);
            for (int i = 0; i < data.length; i++) {
                assertEquals("At offset " + i, data[i], recordBytes[i]);
            }
        } finally {
            out.close();
        }
    }

    @SuppressWarnings("deprecation")
    public void testStoreDeprecated() throws IOException {
        CellRangeAddress ref = new CellRangeAddress(0, 0, 0, 0);

        byte[] recordBytes = new byte[CellRangeAddress.ENCODED_SIZE];
        // With nothing set
        ref.serialize(0, recordBytes);
        assertEquals(recordBytes.length, data.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals("At offset " + i, 0, recordBytes[i]);
        }

        // Now set the flags
        ref.setFirstRow((short) 2);
        ref.setLastRow((short) 4);
        ref.setFirstColumn((short) 0);
        ref.setLastColumn((short) 3);

        // Re-test
        ref.serialize(0, recordBytes);

        assertEquals(recordBytes.length, data.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals("At offset " + i, data[i], recordBytes[i]);
        }
    }
    
    public void testCreateIllegal() throws IOException {
        // for some combinations we expected exceptions
        try {
            assertNotNull(new CellRangeAddress(1, 0, 0, 0));
            fail("Expect to catch an exception");
        } catch (IllegalArgumentException e) {
            // expected here
        }
        try {
            assertNotNull(new CellRangeAddress(0, 0, 1, 0));
            fail("Expect to catch an exception");
        } catch (IllegalArgumentException e) {
            // expected here
        }
    }

    public void testCopy() throws IOException {
        CellRangeAddress ref = new CellRangeAddress(1, 2, 3, 4);
        CellRangeAddress copy = ref.copy();
        assertEquals(ref.toString(), copy.toString());
    }

    public void testGetEncodedSize() throws IOException {
        assertEquals(2*CellRangeAddress.ENCODED_SIZE, CellRangeAddress.getEncodedSize(2));
    }

    public void testFormatAsString() throws IOException {
        CellRangeAddress ref = new CellRangeAddress(1, 2, 3, 4);
        
        assertEquals("D2:E3", ref.formatAsString());
        assertEquals("D2:E3", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString());

        assertEquals("sheet1!$D$2:$E$3", ref.formatAsString("sheet1", true));
        assertEquals("sheet1!$D$2:$E$3", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString("sheet1", true));
        assertEquals("sheet1!$D$2:$E$3", CellRangeAddress.valueOf(ref.formatAsString("sheet1", true)).formatAsString("sheet1", true));

        assertEquals("sheet1!D2:E3", ref.formatAsString("sheet1", false));
        assertEquals("sheet1!D2:E3", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString("sheet1", false));
        assertEquals("sheet1!D2:E3", CellRangeAddress.valueOf(ref.formatAsString("sheet1", false)).formatAsString("sheet1", false));

        assertEquals("D2:E3", ref.formatAsString(null, false));
        assertEquals("D2:E3", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString(null, false));
        assertEquals("D2:E3", CellRangeAddress.valueOf(ref.formatAsString(null, false)).formatAsString(null, false));
        
        assertEquals("$D$2:$E$3", ref.formatAsString(null, true));
        assertEquals("$D$2:$E$3", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString(null, true));
        assertEquals("$D$2:$E$3", CellRangeAddress.valueOf(ref.formatAsString(null, true)).formatAsString(null, true));
        
        ref = new CellRangeAddress(-1, -1, 3, 4);
        assertEquals("D:E", ref.formatAsString());
        assertEquals("sheet1!$D:$E", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString("sheet1", true));
        assertEquals("sheet1!$D:$E", CellRangeAddress.valueOf(ref.formatAsString("sheet1", true)).formatAsString("sheet1", true));
        assertEquals("$D:$E", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString(null, true));
        assertEquals("$D:$E", CellRangeAddress.valueOf(ref.formatAsString(null, true)).formatAsString(null, true));
        assertEquals("sheet1!D:E", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString("sheet1", false));
        assertEquals("sheet1!D:E", CellRangeAddress.valueOf(ref.formatAsString("sheet1", false)).formatAsString("sheet1", false));

        ref = new CellRangeAddress(1, 2, -1, -1);
        assertEquals("2:3", ref.formatAsString());
        assertEquals("sheet1!$2:$3", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString("sheet1", true));
        assertEquals("sheet1!$2:$3", CellRangeAddress.valueOf(ref.formatAsString("sheet1", true)).formatAsString("sheet1", true));
        assertEquals("$2:$3", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString(null, true));
        assertEquals("$2:$3", CellRangeAddress.valueOf(ref.formatAsString(null, true)).formatAsString(null, true));
        assertEquals("sheet1!2:3", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString("sheet1", false));
        assertEquals("sheet1!2:3", CellRangeAddress.valueOf(ref.formatAsString("sheet1", false)).formatAsString("sheet1", false));

        ref = new CellRangeAddress(1, 1, 2, 2);
        assertEquals("C2", ref.formatAsString());
        assertEquals("sheet1!$C$2", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString("sheet1", true));
        assertEquals("sheet1!$C$2", CellRangeAddress.valueOf(ref.formatAsString("sheet1", true)).formatAsString("sheet1", true));
        assertEquals("$C$2", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString(null, true));
        assertEquals("$C$2", CellRangeAddress.valueOf(ref.formatAsString(null, true)).formatAsString(null, true));
        assertEquals("sheet1!C2", CellRangeAddress.valueOf(ref.formatAsString()).formatAsString("sheet1", false));
        assertEquals("sheet1!C2", CellRangeAddress.valueOf(ref.formatAsString("sheet1", false)).formatAsString("sheet1", false));

        // is this a valid address?
        ref = new CellRangeAddress(-1, -1, -1, -1);
        assertEquals(":", ref.formatAsString());
    }
}
