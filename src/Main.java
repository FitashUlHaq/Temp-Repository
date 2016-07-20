import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Main {

	public static void main(String[] args) throws IOException {
		int i=0;
		
		for(int j=0;j<=1;j++)
		{
		String t="/Users/Fitash/Documents/workspace/DWHlab/Lhr_Student_MS_10";
		if(i==0)
		{
			t+="2.txt";
		}else
		{
			t+="3.txt";
		}
		i++;
		
		BufferedReader br= new BufferedReader(new FileReader(t));
		String s= "";
		long startTime = System.nanoTime();
	ArrayList<String> standards = new ArrayList<String>();
	standards.add("MSc");
	standards.add("MS");
	standards.add("BS");
	standards.add("M.Phil");
	standards.add("BE");
	
	int numberofMales=0;
	int numberofFemales=0;
		ArrayList<StudentDetails> sd=new ArrayList<StudentDetails>();
		br.readLine();
		while((s=br.readLine())!=null)
		{
			String tokens[]=s.split(",");
			StudentDetails studentDetails= new StudentDetails();
			studentDetails.setSID(tokens[0]);
			studentDetails.setSt_Name(tokens[1]);
			studentDetails.setFather_Name(tokens[2]);
			
			if(tokens[3].equalsIgnoreCase("M") || tokens[3].equalsIgnoreCase("0") || tokens[3].equalsIgnoreCase("MALE"))
			{ 
				studentDetails.setGender("Male");
				numberofMales++;
			}
			else if(tokens[3].equalsIgnoreCase("F") || tokens[3].equalsIgnoreCase("1") || tokens[3].equalsIgnoreCase("FEMALE"))
			{
				studentDetails.setGender("Female");
				numberofFemales++;
			}
			else
			{
				if(numberofFemales>=numberofMales)
				{
					studentDetails.setGender("Male");
				}
				else
				{
					studentDetails.setGender("Female");
				}
			}
			studentDetails.setAddress(tokens[4]);
			studentDetails.setDate_of_Birth(tokens[5]);
			studentDetails.setReg_Date(tokens[6]);
			studentDetails.setReg_Status(tokens[7]);
			
			if(tokens[8].equalsIgnoreCase("MSC")  || tokens[8].equalsIgnoreCase("MSC.")|| tokens[8].equalsIgnoreCase("M.SC"))
			{ 
				studentDetails.setDegree_Status("MSc");
			} 
			else if(tokens[8].equalsIgnoreCase("M.phil"))
			{ 
				studentDetails.setDegree_Status("M.Phil");
			} 
			else if(tokens[8].equalsIgnoreCase("BE")||tokens[8].equalsIgnoreCase("B.E") )
			{ 
				studentDetails.setDegree_Status("BE");
			} 
			else if(tokens[8].equalsIgnoreCase("MS")||tokens[8].equalsIgnoreCase("M.S"))
			{ 
				studentDetails.setDegree_Status("MS");
			} 
			else if(tokens[8].equalsIgnoreCase("BS")||tokens[8].equalsIgnoreCase("B.S"))
			{ 
				studentDetails.setDegree_Status("BS");
			} 
			else if(tokens[8].equalsIgnoreCase("BSC")  || tokens[8].equalsIgnoreCase("BSC.")|| tokens[8].equalsIgnoreCase("B.SC"))
			{ 
				studentDetails.setDegree_Status("BSc");
			} 
			else
			{
				studentDetails.setDegree_Status(tokens[8]);
			}
			
			
			if(tokens[9].equalsIgnoreCase("MSC")  || tokens[9].equalsIgnoreCase("MSC.")|| tokens[9].equalsIgnoreCase("M.SC"))
			{ 
				studentDetails.setLast_Degree("MSc");
			} 
			else if(tokens[9].equalsIgnoreCase("M.phil"))
			{ 
				studentDetails.setLast_Degree("M.Phil");
			} 
			else if(tokens[9].equalsIgnoreCase("BE")||tokens[9].equalsIgnoreCase("B.E") )
			{ 
				studentDetails.setLast_Degree("BE");
			} 
			else if(tokens[9].equalsIgnoreCase("MS")||tokens[9].equalsIgnoreCase("M.S"))
			{ 
				studentDetails.setLast_Degree("MS");
			} 
			else if(tokens[9].equalsIgnoreCase("BS")||tokens[9].equalsIgnoreCase("B.S"))
			{ 
				studentDetails.setLast_Degree("BS");
			} 
			else if(tokens[9].equalsIgnoreCase("BSC")  || tokens[9].equalsIgnoreCase("BSC.")|| tokens[9].equalsIgnoreCase("B.SC"))
			{ 
				studentDetails.setLast_Degree("BSc");
			} 
			else
			{
				studentDetails.setLast_Degree(getMostUsedLastdegree(sd));
			}


//			studentDetails.setLast_Degree(tokens[9]);
			sd.add(studentDetails);
			
		}
		PrintWriter pw=null;
		if(i==1)
		{
		pw=new PrintWriter( new File("Data.txt"));
		}
		else
		{
			pw=new PrintWriter( new File("Data1.txt"));
		}
		for(StudentDetails student: sd)
		{
			pw.write(student.getSID()+"--"+student.getSt_Name()+"--"+student.getFather_Name()+"--"+student.getDate_of_Birth()+"--"+student.getAddress()+"--"+student.getGender()+"--"+student.getReg_Date()+"--"+student.getReg_Status()+"--"+student.getLast_Degree()+"--"+student.getDegree_Status()+"\n");
		}
		
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		System.out.println("Time for Writing txt file  : "+duration/1000000+"ms");
		
	//	startTime = System.nanoTime();
//		ExcelFileReading efr = new ExcelFileReading();
	//	efr.ReadFile(sd);
//		endTime = System.nanoTime();
		
	//	duration = (endTime - startTime);
	//	System.out.println("Time for Writing excel file  : "+duration/1000000+"ms");
		
		}
	}

	private static String getMostUsedLastdegree(ArrayList<StudentDetails> sd) {
		int bs=0,ms=0,bsc=0,msc=0,be=0,mphil=0;
		for(StudentDetails details:sd)
		{
			if(details.getLast_Degree().equalsIgnoreCase("BS"))
			{
				bs++;
			}
			else if(details.getLast_Degree().equalsIgnoreCase("MS"))
			{
				ms++;
			}
			else if(details.getLast_Degree().equalsIgnoreCase("BSC"))
			{
				bsc++;
			}
			else if(details.getLast_Degree().equalsIgnoreCase("BE"))
			{
				be++;
			}
			else if(details.getLast_Degree().equalsIgnoreCase("m.phil"))
			{
				mphil++;
			}
		}
		
		int max=bs;
		if(max<ms)
		{
			max=ms;
		}
		if(max<bs)
		{
			max=bs;
		}
		if(max<bsc)
		{
			max=bsc;
		}
		if(max<be)
		{
			max=be;
		}
		if(max<mphil)
		{
			max=mphil;
		}
		
		if(max==ms)
		{
			return "MS";
		}
		if(max==bs)
		{
			return "BS";
		}
		if(max==bsc)
		{
			return "BSc";
		}
		if(max==be)
		{
			return "BE";
		}
		if(max==mphil)
		{
			return "M.phil";
		}
		
		
	return null;
	}

}
