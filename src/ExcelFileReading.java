import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.Detail;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelFileReading {
	public void ReadFile(ArrayList<StudentDetails> sd)
	{
		ArrayList<details> array= new ArrayList<details>();

        try
        {
            FileInputStream file = new FileInputStream(new File("/Users/Fitash/Documents/workspace/DWHlab/Students.xlsx"));
 
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            for(int i=0;i<workbook.getNumberOfSheets();i++)
            {
            XSSFSheet sheet = workbook.getSheetAt(i);
 
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                details d= new details();
                 
                int k=0;
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    
                    switch (cell.getCellType())
                    {
                        case Cell.CELL_TYPE_STRING:
                        	
//                            System.out.print(cell.getStringCellValue() + "t");
                            if(k==0)
                            {
                            	d.setSt_ID(cell.getStringCellValue());
                            	k++;
                            }
                            else if(k==1)
                            {
                            	d.setName(cell.getStringCellValue());
                            	k++;
                            }
                            else if(k==2)
                            {
                            	d.setFather(cell.getStringCellValue());
                            	k++;
                            }
                            else if(k==3)
                            {
                            	d.setDoB(cell.getStringCellValue());
                            	k++;
                            }
                            else if(k==4)
                            {
                            	d.setMF(cell.getStringCellValue());
                            	k++;
                            }
                            else if(k==5)
                            {
                            	d.setDoReg(cell.getStringCellValue());
                            	k++;
                            }
                            else if(k==6)
                            {
                            	d.setRStatus(cell.getStringCellValue());
                            	k++;
                            }
                            else if(k==7)
                            {
                            	d.setDStatus(cell.getStringCellValue());
                            	k++;
                            }
                            else if(k==8)
                            {
                            	d.setAddress(cell.getStringCellValue());
                            	k++;
                            }
                            else if(k==9)
                            {
                            	d.setQualification(cell.getStringCellValue());
                            	k++;
                            }
                            else if(k==10)
                            {
                            	d.setName(cell.getStringCellValue());
                                	
                            }
                            break;
                    }
                }
                array.add(d);
            }
          
        }
            file.close();
            
            PrintWriter pw= new PrintWriter(new FileWriter( new File("Data.txt"),true));
            pw.write("\n\n\n============================================\n\n\n");
            for(details student: array)
    		{
    			pw.write(student.getSt_ID()+"--"+student.getName()+"--"+student.getFather()+"--"+student.getDoB()+"--"+student.getAddress()+"--"+student.getMF()+"--"+student.getRStatus()+"--"+student.getDStatus()+"---"+student.getRStatus()+"\n\n");
    		}
        }        
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}

}
