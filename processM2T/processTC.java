package processM2T;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class processTC {
	
	private final static String tcFile  ="C:\\codes\\Java\\processM2T\\output.tests.txt";
	private final static String tcLabel0File  ="C:\\codes\\Java\\processM2T\\output.tests.label0.txt";
	private final static String contextLabel0File = "C:\\codes\\Java\\processM2T\\input.methods.plusfc.label0.txt";
	private final static String fcLabel0File = "C:\\codes\\Java\\processM2T\\focal.methods.label0.txt";
	private final static String idxFile  ="C:\\codes\\Java\\processM2T\\label0.idxs.txt";
	private final static String prefixFile = "C:\\codes\\Java\\processM2T\\output.prefix.txt";
	
	private static BufferedReader br;
	private static BufferedWriter bw,bwIdx;
	
	public static int getAssertNum(String testcase){
		int assert_num = 0;
		String target = "assert";
		int idx=-2;
		while(idx!=-1) {
			idx = testcase.indexOf(target, Math.max(0, idx+1));
			if(idx>=0)assert_num++;
		}
		return assert_num;
	}
	
	public static ArrayList<Integer> getAssertIdxArr(String testcase) {
		ArrayList<Integer> idxArr = new ArrayList<Integer>();
		String target = "assert";
		int idx=-2;
		while(idx!=-1) {
			idx = testcase.indexOf(target, Math.max(0, idx+1));
			if(idx>=0)idxArr.add(idx);
		}
		
		return idxArr;
	}
	
	public static boolean isInterLaced(String testcase) {

		String target = "assert";		
		//现在就差把上面的思维泛化了
		ArrayList<Integer> idxArr  = getAssertIdxArr(testcase);
		for(int i=0;i<idxArr.size();i++) {
			int idx = idxArr.get(i).intValue();
			int idx1 = testcase.indexOf(";",idx);
			int idx2 = testcase.indexOf(target,idx1);
			if(idx == -1) {
				continue;
			}else {
				if(idx2-idx1>13) {
					return true;
				}else {
					continue;
				}
			}
		}
		
		return false;
	}
	
	public static boolean isExceptionalOracle(String testcase) {
		//找到第一个assert的idx - idxA
		int idxA = testcase.indexOf("ssert");//有Assert和assert两种情况
		//找到这个idx前的最后一个分号的idx - idxS
		int idxS = testcase.lastIndexOf(";",idxA);
		if(idxA==-1||idxS==-1)
			return true;
		else
			return false;
	}
	
	public static int getLabel(String testcase) {
		int label = 0;
		
		//鉴定是否为类型1
		if(getAssertNum(testcase)>5)return 1;
		
		//鉴定是否为类型2
		if(isInterLaced(testcase))return 2;
		
		//鉴定是否为类型3
		if(isExceptionalOracle(testcase))return 3;
		
		return label;
	}
	
	public static void main(String[] args) throws IOException {
		//以下三行仅仅是验证模块，而非功能模块
//		String addStr = new File(prefixFile).getPath();
//		long lineNum = Files.lines(Paths.get(addStr)).count();
//		System.out.println("Total number of lines : " + lineNum);
		//以下2行也是验证模块
		BufferedReader vbr = new BufferedReader(new FileReader(fcLabel0File), 200);
		int i=0;
		while(vbr.readLine()!=null)i++;
		System.out.println("Total number of lines : " + i);
		
//		br = new BufferedReader(new FileReader(tcFile), 310);	
//		
//		String line;
//		StringBuffer sb= new StringBuffer("");
//		StringBuffer sbIdx= new StringBuffer("");
//		
//		int labelArr[] = new int[] {0,0,0,0};
//		int idx = 0;
//	    while ((line = br.readLine()) != null) {
//	    	labelArr[getLabel(line)]++;
//	    	if(getLabel(line)==0) {
//	    		sb.append(line+"\n");
//	    		sbIdx.append(idx+"\n");
//	    	}
//	    	idx ++;
//	    }
//	    br.close();
//
//		for(int j=0;j<4;j++) {
//			System.out.print(j);
//			System.out.print(" : ");
//			System.out.print(labelArr[j]);
//			System.out.print("个测试用例\n");
//		}
//		
//		bw = new BufferedWriter(new FileWriter(tcLabel0File),300);
//		bw.write(sb.toString());
//		bw.close();
//		
//		bwIdx = new BufferedWriter(new FileWriter(idxFile),300);
//		bwIdx.write(sbIdx.toString());
//		bwIdx.close();
//		
//		System.out.println("已将label==0的测试用例写入新文件");
	}
}
